package app.services;

import app.dto.FlightSeatDto;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.entities.Seat;
import app.exceptions.EntityNotFoundException;
import app.mappers.FlightSeatMapper;
import app.repositories.FlightSeatRepository;
import app.repositories.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static app.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class FlightSeatService {

    private final FlightSeatMapper flightSeatMapper;
    private final FlightSeatRepository flightSeatRepository;
    private final SeatRepository seatRepository;
    private final SeatService seatService;
    @Lazy
    @Autowired
    private final FlightService flightService;


    public List<FlightSeatDto> getAllFlightSeats() {
        var flightSeatList = new ArrayList<FlightSeat>();
        flightSeatRepository.findAll().forEach(flightSeatList::add);
        return flightSeatMapper.toDtoList(flightSeatList);
    }

    public Page<FlightSeatDto> getAllFlightSeats(Integer page, Integer size) {
        return flightSeatRepository.findAll(PageRequest.of(page, size))
                .map(flightSeatMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<FlightSeatDto> getAllFlightSeatsFiltered(Integer page, Integer size, Long flightId, Boolean isSold, Boolean isRegistered) {
        var pageable = PageRequest.of(page, size);
        if (Boolean.FALSE.equals(isSold) && Boolean.FALSE.equals(isRegistered)) {
            return getFreeSeatsById(pageable, flightId);
        } else if (Boolean.FALSE.equals(isSold)) {
            return getNotSoldFlightSeatsById(flightId, pageable);
        } else if (Boolean.FALSE.equals(isRegistered)) {
            return findNotRegisteredFlightSeatsById(flightId, pageable);
        } else {
            return getFlightSeatsByFlightId(flightId, pageable);
        }
    }

    public Optional<FlightSeat> getFlightSeat(Long id) {
        return flightSeatRepository.findById(id);
    }

    public Optional<FlightSeatDto> getFlightSeatDto(Long id) {
        return flightSeatRepository.findById(id).map(flightSeatMapper::toDto);
    }

    public List<FlightSeatDto> getFlightSeatsByFlightId(Long flightId) {
        return flightSeatRepository.findFlightSeatsByFlightId(flightId).stream()
                .map(flightSeatMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public FlightSeatDto createFlightSeat(FlightSeatDto flightSeatDto) {
        var flightSeat = flightSeatMapper.toEntity(flightSeatDto);
        var flight = flightService.checkIfFlightExists(flightSeatDto.getFlightId());
        flightSeat.setFlight(flight);

        var seat = seatService.getSeat(flightSeatDto.getSeat().getId());
        if (seat == null) {
            throw new EntityNotFoundException("Operation was not finished because Seat was not found with id = " + flightSeatDto.getFlightId());
        }
        flightSeat.setSeat(seat);
        return flightSeatMapper.toDto(flightSeatRepository.save(flightSeat));
    }

    @Transactional
    public FlightSeatDto editFlightSeat(Long id, FlightSeatDto flightSeatDto) {
        var existingFlightSeat = flightSeatRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation was not finished because FlightSeat was not found with id = " + id)
        );
        if (flightSeatDto.getFare() != null) {
            existingFlightSeat.setFare(flightSeatDto.getFare());
        }
        if (flightSeatDto.getIsSold() != null) {
            existingFlightSeat.setIsSold(flightSeatDto.getIsSold());
        }
        if (flightSeatDto.getIsBooked() != null) {
            existingFlightSeat.setIsBooked(flightSeatDto.getIsBooked());
        }
        if (flightSeatDto.getIsRegistered() != null) {
            existingFlightSeat.setIsRegistered(flightSeatDto.getIsRegistered());
        }
        return flightSeatMapper.toDto(flightSeatRepository.save(existingFlightSeat));
    }

    public int getNumberOfFreeSeatOnFlight(Flight flight) {
        return flightSeatRepository
                .findFlightSeatByFlightIdAndIsSoldFalseAndIsRegisteredFalseAndIsBookedFalse(flight.getId()).size();
    }

    @Transactional
    public void deleteFlightSeatById(Long id) {
        flightService.checkIfFlightExists(id);
        flightSeatRepository.deleteById(id);
    }

    @Transactional
    public void makeFlightSeatNotSold(long[] flightSeatId) {
        flightSeatRepository.editIsSoldToFalseByFlightSeatId(flightSeatId);
    }

    public List<FlightSeat> findByFlightId(Long id) {
        return flightSeatRepository.findByFlightId(id);
    }

    @Transactional
    public List<FlightSeatDto> generateFlightSeats(Long flightId) {
        var flight = flightService.checkIfFlightExists(flightId);
        var flightSeats = getFlightSeatsByFlightId(flightId);
        if (!flightSeats.isEmpty()) {
            return flightSeats;
        }
        var newFlightSeats = new ArrayList<FlightSeat>();
        var seats = seatRepository.findByAircraftId(flight.getAircraft().getId());
        for (Seat seat : seats) {
            newFlightSeats.add(generateFlightSeat(seat, flight));
        }
        flightSeatRepository.saveAll(newFlightSeats);
        return flightSeatMapper.toDtoList(newFlightSeats);
    }

    private FlightSeat generateFlightSeat(Seat seat, Flight flight) {
        var flightSeat = new FlightSeat();
        flightSeat.setSeat(seat);
        flightSeat.setFlight(flight);
        flightSeat.setIsBooked(false);
        flightSeat.setIsSold(false);
        flightSeat.setIsRegistered(false);
        flightSeat.setFare(generateFareForFlightSeat(seat, flight));
        return flightSeat;
    }

    public int generateFareForFlightSeat(Seat seat, Flight flight) {
        float fare = BASE_FLIGHT_SEAT_FARE * seat.getCategory().getCategoryType().getCategoryRatio();
        if (Boolean.TRUE.equals(seat.getIsNearEmergencyExit())) {
            fare *= EMERGENCY_EXIT_SEAT_PRICE_RATIO;
        }
        if (Boolean.TRUE.equals(seat.getIsLockedBack())) {
            fare *= LOCK_BACK_SEAT_PRICE_RATIO;
        }
        float distance = flightService.getDistance(flight);
        if (distance > 1000) {
            fare += fare * (distance / 10000);
        }
        return Math.round(fare / 10) * 10;
    }

    private Page<FlightSeatDto> getFreeSeatsById(Pageable pageable, Long flightId) {
        flightService.checkIfFlightExists(flightId);
        return flightSeatRepository
                .findFlightSeatByFlightIdAndIsSoldFalseAndIsRegisteredFalseAndIsBookedFalse(flightId, pageable)
                .map(flightSeatMapper::toDto);
    }

    private Page<FlightSeatDto> getNotSoldFlightSeatsById(Long flightId, Pageable pageable) {
        flightService.checkIfFlightExists(flightId);
        return flightSeatRepository.findAllFlightsSeatByFlightIdAndIsSoldFalse(flightId, pageable)
                .map(flightSeatMapper::toDto);
    }

    private Page<FlightSeatDto> findNotRegisteredFlightSeatsById(Long flightId, Pageable pageable) {
        flightService.checkIfFlightExists(flightId);
        return flightSeatRepository.findAllFlightsSeatByFlightIdAndIsRegisteredFalse(flightId, pageable)
                .map(flightSeatMapper::toDto);
    }

    private Page<FlightSeatDto> getFlightSeatsByFlightId(Long flightId, Pageable pageable) {
        flightService.checkIfFlightExists(flightId);
        return flightSeatRepository.findFlightSeatsByFlightId(flightId, pageable)
                .map(flightSeatMapper::toDto);
    }

    public List<Long> findFlightSeatIdsByFlight(Flight flight) {
        List<Long> seatIds = new ArrayList<>();
        Set<FlightSeat> flightSeats = flightSeatRepository.findFlightSeatByFlight(flight);
        for (FlightSeat flightSeat : flightSeats) {
            seatIds.add(flightSeat.getId());
        }
        return seatIds;
    }

    public FlightSeat checkIfFlightSeatExist(Long flightSeatId) {
        return flightSeatRepository.findById(flightSeatId).orElseThrow(
                () -> new EntityNotFoundException("Operation was not finished because FlightSeat was not found with id = " + flightSeatId)
        );
    }
}