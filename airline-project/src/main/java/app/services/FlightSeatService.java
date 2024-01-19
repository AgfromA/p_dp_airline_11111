package app.services;

import app.dto.FlightSeatDto;
import app.dto.SeatDto;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.entities.Seat;
import app.enums.CategoryType;
import app.exceptions.EntityNotFoundException;
import app.mappers.FlightSeatMapper;
import app.mappers.SeatMapper;
import app.repositories.FlightRepository;
import app.repositories.FlightSeatRepository;
import app.repositories.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightSeatService {

    private final FlightSeatRepository flightSeatRepository;
    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;
    private final SeatService seatService;
    private final FlightService flightService;
    private final FlightSeatMapper flightSeatMapper;
    private final SeatMapper seatMapper;

    public Set<FlightSeat> getAllFlightSeats() {
        Set<FlightSeat> flightSeatSet = new HashSet<>();
        flightSeatRepository.findAll().forEach(flightSeatSet::add);
        return flightSeatSet;
    }

    public List<FlightSeatDto> getAllListFlightSeats() {
        List<FlightSeat> flightSeatList = new ArrayList<>();
        flightSeatRepository.findAll().forEach(flightSeatList::add);
        return flightSeatMapper.toDtoList(flightSeatList, flightService);
    }

    @Transactional(readOnly = true)
    public Page<FlightSeatDto> getFreeSeatsById(Pageable pageable, Long flightId) {
        return flightSeatRepository
                .findFlightSeatByFlightIdAndIsSoldFalseAndIsRegisteredFalseAndIsBookedFalse(flightId, pageable)
                .map(entity -> flightSeatMapper.toDto(entity, flightService));
    }

    public Page<FlightSeatDto> getAllFlightSeats(Integer page, Integer size) {
        return flightSeatRepository.findAll(PageRequest.of(page, size))
                .map(entity -> flightSeatMapper.toDto(entity, flightService));
    }

    public Optional<FlightSeat> getFlightSeatById(Long id) {
        return flightSeatRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Set<FlightSeatDto> getFlightSeatsByFlightId(Long flightId) {
        return flightSeatRepository.findFlightSeatsByFlightId(flightId).stream()
                .map(flightSeat -> flightSeatMapper.toDto(flightSeat, flightService))
                .collect(Collectors.toSet());
    }

    public Page<FlightSeatDto> getFlightSeatsByFlightId(Long flightId, Pageable pageable) {
        return flightSeatRepository.findFlightSeatsByFlightId(flightId, pageable)
                .map(entity -> flightSeatMapper.toDto(entity, flightService));
    }

    public Set<FlightSeat> getFlightSeatsByFlightNumber(String flightNumber) {
        Set<FlightSeat> flightSeatByFlight = flightSeatRepository.findFlightSeatByFlight(flightRepository.getByCode(flightNumber));
        return new HashSet<>(flightSeatByFlight);
    }

    @Transactional
    public Set<FlightSeat> addFlightSeatsByFlightId(Long flightId) {
        Set<FlightSeat> newFlightSeats = new HashSet<>();
        var flight = flightService.getFlightById(flightId).get();
        var seats = seatRepository.findByAircraftId(flight.getAircraft().getId());
        for (Seat s : seats) {
            var flightSeat = new FlightSeat();
            flightSeat.setSeat(s);
            flightSeat.setFlight(flight);
            flightSeat.setIsBooked(false);
            flightSeat.setIsSold(false);
            flightSeat.setIsRegistered(false);
            flightSeat.setFare(generateFareForFlightSeat(s, flight));
            newFlightSeats.add(flightSeat);
        }
        for (FlightSeat f : newFlightSeats) {
            saveFlightSeat(f);
        }
        return newFlightSeats;
    }

    @Transactional
    public Set<FlightSeat> addFlightSeatsByFlightNumber(String flightNumber) {
        Set<FlightSeat> seatsForAdd = new HashSet<>();
        var allFlightSeats = getAllFlightSeats();
        var flight = flightRepository.getByCode(flightNumber);
        if (flight != null) {
            var seatsAircraft = flight.getAircraft().getSeatSet();
            for (Seat s : seatsAircraft) {
                var flightSeat = new FlightSeat();
                flightSeat.setSeat(s);
                flightSeat.setFlight(flight);
                if (allFlightSeats.contains(flightSeat)) {
                    continue;
                }
                flightSeat.setFare(generateFareForFlightSeat(s, flight));
                flightSeat.setIsSold(false);
                flightSeat.setIsRegistered(false);
                seatsForAdd.add(flightSeat);
            }
            flightSeatRepository.saveAll(seatsForAdd);
        }
        return seatsForAdd;
    }

    @Transactional
    public FlightSeat saveFlightSeat(FlightSeat flightSeat) {
        return flightSeatRepository.save(flightSeat);
    }

    @Transactional
    public FlightSeatDto saveFlightSeat(FlightSeatDto flightSeatDto) {
        var flightSeat = flightSeatMapper.toEntity(flightSeatDto, flightService, seatService);

        var flight = flightService.getFlightById(flightSeatDto.getFlightId()).orElseThrow(
                () -> new EntityNotFoundException("Operation was not finished because Flight was not found with id = " + flightSeatDto.getFlightId())
        );
        flightSeat.setFlight(flight);

        var seat = seatService.getSeatById(flightSeatDto.getSeat().getId());
        if (seat == null) {
            throw new EntityNotFoundException("Operation was not finished because Seat was not found with id = " + flightSeatDto.getFlightId());
        }
        flightSeat.setSeat(seat);

        flightSeat.setId(null);
        return flightSeatMapper.toDto(flightSeatRepository.save(flightSeat), flightService);
    }

    @Transactional
    public FlightSeatDto editFlightSeat(Long id, FlightSeatDto flightSeatDto) {
        var existingFlightSeat = flightSeatRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation was not finished because FlightSeat was not found with id = " + id)
        );
        if (flightSeatDto.getFare() == null) {
            existingFlightSeat.setFare(flightSeatDto.getFare());
        }
        if (flightSeatDto.getIsSold() == null) {
            existingFlightSeat.setIsSold(flightSeatDto.getIsSold());
        }
        if (flightSeatDto.getIsBooked() == null) {
            existingFlightSeat.setIsBooked(flightSeatDto.getIsBooked());
        }
        if (flightSeatDto.getIsRegistered() == null) {
            existingFlightSeat.setIsRegistered(flightSeatDto.getIsRegistered());
        }
        return flightSeatMapper.toDto(flightSeatRepository.save(existingFlightSeat), flightService);
    }

    public int getNumberOfFreeSeatOnFlight(Flight flight) {
        return flightSeatRepository
                .findFlightSeatByFlightIdAndIsSoldFalseAndIsRegisteredFalseAndIsBookedFalse(flight.getId()).size();
    }

    public Set<Seat> getSetOfFreeSeatsOnFlightByFlightId(Long id) {
        var targetFlight = flightRepository.getById(id);
        var setOfSeat = targetFlight.getAircraft().getSeatSet();
        var setOfReservedSeat = flightSeatRepository.findFlightSeatByFlight(targetFlight)
                .stream().map(FlightSeat::getSeat)
                .collect(Collectors.toSet());
        for (Seat s : setOfReservedSeat) {
            setOfSeat.remove(s);
        }
        return setOfSeat;
    }

    public List<SeatDto> getAllSeats() {
        return seatRepository.findAll().stream().map(seatMapper::toDto).collect(Collectors.toList());
    }

    public Set<FlightSeat> getFlightSeatsBySeat(Seat seat) {
        return flightSeatRepository.findFlightSeatsBySeat(seat);
    }

    public void deleteFlightSeatById(Long id) {
        flightSeatRepository.deleteById(id);
    }

    public Set<FlightSeat> getNotSoldFlightSeatsById(Long id) {
        return flightSeatRepository.findAllFlightsSeatByFlightIdAndIsSoldFalse(id);
    }

    public Page<FlightSeatDto> findNotRegisteredFlightSeatsById(Long id, Pageable pageable) {
        return flightSeatRepository.findAllFlightsSeatByFlightIdAndIsRegisteredFalse(id, pageable)
                .map(entity -> flightSeatMapper.toDto(entity, flightService));
    }

    public List<FlightSeatDto> getCheapestFlightSeatsByFlightIdAndSeatCategory(Long id, CategoryType type) {
        return flightSeatMapper.toDtoList(flightSeatRepository.findFlightSeatsByFlightIdAndSeatCategory(id, type), flightService);
    }


    public Page<FlightSeatDto> getNotSoldFlightSeatsById(Long id, Pageable pageable) {
        return flightSeatRepository.findAllFlightsSeatByFlightIdAndIsSoldFalse(id, pageable)
                .map(entity -> flightSeatMapper.toDto(entity, flightService));
    }

    @Transactional
    public void editFlightSeatIsSoldToFalseByFlightSeatId(long[] flightSeatId) {
        flightSeatRepository.editIsSoldToFalseByFlightSeatId(flightSeatId);
    }

    public List<FlightSeat> findByFlightId(Long id) {
        return flightSeatRepository.findByFlightId(id);
    }

    public int generateFareForFlightSeat(Seat seat, Flight flight) {

        int baseFare = 5000;
        float emergencyExitRatio;
        float categoryRatio;
        float lockedBackRatio;
        if (seat.getIsNearEmergencyExit()) {
            emergencyExitRatio = 1.3f;
        } else emergencyExitRatio = 1f;
        if (seat.getIsLockedBack()) {
            lockedBackRatio = 0.8f;
        } else lockedBackRatio = 1f;
        switch (seat.getCategory().getCategoryType()) {
            case PREMIUM_ECONOMY:
                categoryRatio = 1.2f;
                break;
            case BUSINESS:
                categoryRatio = 2f;
                break;
            case FIRST:
                categoryRatio = 2.5f;
                break;
            default:
                categoryRatio = 1f;
        }

        float fare = baseFare * emergencyExitRatio * categoryRatio * lockedBackRatio;
        float distance = flightService.getDistance(flight);

        if (distance > 1000) {
            fare += fare * (distance / 10000);
        }
        return Math.round(fare / 10) * 10;
    }
}