package app.services;

import app.dto.SeatDto;
import app.entities.Aircraft;
import app.entities.Seat;
import app.enums.CategoryType;
import app.enums.seats.AircraftSeatsInformation;
import app.exceptions.EntityNotFoundException;
import app.repositories.SeatRepository;
import app.mappers.SeatMapper;
import org.springframework.data.domain.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final CategoryService categoryService;
    private final AircraftService aircraftService;
    private final SeatMapper seatMapper;

    public List<SeatDto> getAllSeats() {
        return seatMapper.toDtoList(seatRepository.findAll());
    }

    public Page<SeatDto> getAllSeats(Integer page, Integer size) {
        return seatRepository.findAll(PageRequest.of(page, size)).map(seatMapper::toDto);
    }

    public Page<SeatDto> getAllSeatsByAircraftId(Integer page, Integer size, Long aircraftId) {
        checkIfAircraftExists(aircraftId);
        return seatRepository.findByAircraftId(aircraftId, PageRequest.of(page, size)).map(seatMapper::toDto);
    }

    @Transactional
    public SeatDto saveSeat(SeatDto seatDto) {
        checkIfAircraftExists(seatDto.getAircraftId());
        var seat = seatMapper.toEntity(seatDto, categoryService, aircraftService);
        seat.setId(null);
        return seatMapper.toDto(seatRepository.save(seat));
    }

    public Seat getSeat(long id) {
        return seatRepository.findById(id).orElse(null);
    }

    public Optional<SeatDto> getSeatDto(long id) {
        return seatRepository.findById(id).map(seatMapper::toDto);
    }

    @Transactional
    public SeatDto editSeat(Long id, SeatDto seatDto) {
        checkIfSeatExists(id);
        var aircraft = checkIfAircraftExists(seatDto.getAircraftId());
        var existingSeat = seatRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation was not finished because Seat was not found with id = " + id)
        );
        existingSeat.setAircraft(aircraft);

        if (seatDto.getSeatNumber() != null) {
            existingSeat.setSeatNumber(seatDto.getSeatNumber());
        }
        if (seatDto.getIsLockedBack() != null) {
            existingSeat.setIsLockedBack(seatDto.getIsLockedBack());
        }
        if (seatDto.getIsNearEmergencyExit() != null) {
            existingSeat.setIsNearEmergencyExit(seatDto.getIsNearEmergencyExit());
        }
        if (seatDto.getCategory() != null) {
            existingSeat.setCategory(categoryService.getCategoryByType(seatDto.getCategory()));
        }
        return seatMapper.toDto(seatRepository.save(existingSeat));
    }

    @Transactional
    public void deleteSeat(Long id) {
        checkIfSeatExists(id);
        seatRepository.deleteById(id);
    }

    @Transactional
    public List<SeatDto> generateSeats(Long aircraftId) {
        var aircraft = checkIfAircraftExists(aircraftId);
        if (!aircraft.getSeatSet().isEmpty()) {
            return seatMapper.toDtoList(new ArrayList<>(aircraft.getSeatSet()));
        }
        var aircraftSeatsInformation = getNumbersOfSeatsByAircraftId(aircraftId);
        var aircraftSeats = aircraftSeatsInformation.getAircraftSeats();
        var seats = new ArrayList<Seat>();
        for (int i = 0; i < aircraftSeats.length; i++) {
            var seat = new Seat();
            if (i < aircraftSeatsInformation.getNumberOfBusinessClassSeats()) {
                seat.setCategory(categoryService.getCategoryByType(CategoryType.BUSINESS));
            } else {
                seat.setCategory(categoryService.getCategoryByType(CategoryType.ECONOMY));
            }
            seat.setAircraft(aircraft);
            seat.setSeatNumber(aircraftSeats[i].getNumber());
            seat.setIsNearEmergencyExit(aircraftSeats[i].isNearEmergencyExit());
            seat.setIsLockedBack(aircraftSeats[i].isLockedBack());

            seats.add(seat);
        }
        return seatMapper.toDtoList(seatRepository.saveAll(seats));
    }

    private AircraftSeatsInformation getNumbersOfSeatsByAircraftId(long aircraftId) {
        var aircraft = aircraftService.getAircraft(aircraftId);
        return AircraftSeatsInformation.valueOf(aircraft.getModel()
                .toUpperCase().replaceAll("[^A-Za-z0-9]", "_"));
    }

    private Aircraft checkIfAircraftExists(Long aircraftId) {
        var aircraft = aircraftService.getAircraft(aircraftId);
        if (aircraft == null) {
            throw new EntityNotFoundException(
                    "Operation was not finished because Aircraft was not found with id = " + aircraftId);
        }
        return aircraft;
    }

    private Seat checkIfSeatExists(Long seatId) {
        return seatRepository.findById(seatId).orElseThrow(() -> new EntityNotFoundException(
                "Operation was not finished because Seat was not found with id = " + seatId)
        );
    }
    public Set<Seat> findByAircraftId(Long id) {
       return seatRepository.findByAircraftId(id);
    }
}