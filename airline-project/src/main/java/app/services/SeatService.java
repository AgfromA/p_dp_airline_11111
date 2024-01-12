package app.services;

import app.dto.SeatDto;
import app.entities.Seat;
import app.enums.CategoryType;
import app.enums.seats.SeatsNumbersByAircraft;
import app.enums.seats.interfaces.AircraftSeats;
import app.exceptions.ViolationOfForeignKeyConstraintException;
import app.repositories.FlightSeatRepository;
import app.repositories.SeatRepository;
import app.mappers.SeatMapper;
import org.springframework.data.domain.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final CategoryService categoryService;
    private final AircraftService aircraftService;
    private final FlightSeatRepository flightSeatRepository;
    private final SeatMapper seatMapper;

    public List<SeatDto> getAllSeats() {
        return seatMapper.toDtoList(seatRepository.findAll());
    }

    @Transactional
    public Seat saveSeat(SeatDto seatDTO) {
        var seat = seatMapper.toEntity(seatDTO, categoryService, aircraftService);
        if (seat.getId() != 0) {
            Seat aldSeat = getSeatById(seat.getId());
            if (aldSeat != null && aldSeat.getAircraft() != null) {
                seat.setAircraft(aldSeat.getAircraft());
            }
        }
        seat.setCategory(categoryService.getCategoryByType(seat.getCategory().getCategoryType()));
        return seatRepository.saveAndFlush(seat);
    }

    public Seat getSeatById(long id) {
        return seatRepository.findById(id).orElse(null);
    }

    @Transactional
    public Seat editSeatById(Long id, SeatDto seatDTO) {
        var seat = seatMapper.toEntity(seatDTO, categoryService, aircraftService);
        var targetSeat = seatRepository.findById(id).orElse(null);
        if (seat.getCategory() != null && seat.getCategory().getCategoryType() != targetSeat.getCategory().getCategoryType()) {
            targetSeat.setCategory(categoryService.getCategoryByType(seat.getCategory().getCategoryType()));
        }
        if (seat.getAircraft() != null && !seat.getAircraft().equals(targetSeat.getAircraft())) {
            targetSeat.setAircraft(aircraftService.getAircraftByAircraftNumber(seat.getAircraft().getAircraftNumber()));
        }
        targetSeat.setSeatNumber(seat.getSeatNumber());
        targetSeat.setIsNearEmergencyExit(seat.getIsNearEmergencyExit());
        targetSeat.setIsLockedBack(seat.getIsLockedBack());
        return seatRepository.saveAndFlush(targetSeat);
    }

    @Transactional
    public void deleteSeatById(Long id) throws ViolationOfForeignKeyConstraintException {
        if (!(flightSeatRepository.findFlightSeatsBySeat(getSeatById(id))).isEmpty()) {
            throw new ViolationOfForeignKeyConstraintException(
                    String.format("Seat with id = %d cannot be deleted because it is locked by the table \"flight_seat\"", id));
        }
        seatRepository.deleteById(id);
    }

    public Page<SeatDto> getPagesSeatsByAircraftId(Long id, Pageable pageable) {
        return seatRepository.findByAircraftId(id, pageable)
                .map(seatMapper::toDto);
    }

    @Transactional
    public List<SeatDto> generateSeatsDTOByAircraftId(long aircraftId) {

        List<SeatDto> savedSeatsDTO = new ArrayList<>(getNumbersOfSeatsByAircraftId(aircraftId).getTotalNumberOfSeats());
        if (getPagesSeatsByAircraftId(aircraftId, Pageable.unpaged()).getTotalElements() > 0) {
            return savedSeatsDTO;
        }
        int enumSeatsCounter = 0;
        for (SeatDto seatDTO : getSeatsDTOByAircraftId(aircraftId)) {
            seatDTO.setSeatNumber(getAircraftSeatsByAircraftId(aircraftId)[enumSeatsCounter].getNumber());
            seatDTO.setAircraftId(aircraftId);
            if (enumSeatsCounter < getNumbersOfSeatsByAircraftId(aircraftId).getNumberOfBusinessClassSeats()) { //Назначаем категории
                seatDTO.setCategory(CategoryType.BUSINESS);
            } else {
                seatDTO.setCategory(CategoryType.ECONOMY);
            }
            seatDTO.setIsNearEmergencyExit(getAircraftSeatsByAircraftId(aircraftId)[enumSeatsCounter].isNearEmergencyExit());
            seatDTO.setIsLockedBack(getAircraftSeatsByAircraftId(aircraftId)[enumSeatsCounter].isLockedBack());
            enumSeatsCounter += 1;

            var savedSeat = saveSeat(seatDTO);

            savedSeatsDTO.add(seatMapper.toDto(savedSeat));
        }
        return savedSeatsDTO;
    }

    private SeatsNumbersByAircraft getNumbersOfSeatsByAircraftId(long aircraftId) {
        var aircraft = aircraftService.getAircraftById(aircraftId); //создается объект САМОЛЕТ
        return SeatsNumbersByAircraft.valueOf(aircraft.getModel() //количество мест в самолете
                .toUpperCase().replaceAll("[^A-Za-z0-9]", "_"));
    }

    private AircraftSeats[] getAircraftSeatsByAircraftId(long aircraftId) {
        return getNumbersOfSeatsByAircraftId(aircraftId).getAircraftSeats();
    }

    private List<SeatDto> getSeatsDTOByAircraftId(long aircraftId) {
        return Stream.generate(SeatDto::new)
                .limit(getNumbersOfSeatsByAircraftId(aircraftId).getTotalNumberOfSeats())
                .collect(Collectors.toList());
    }

    public Page<SeatDto> getAllPagesSeats(Integer page, Integer size) {
        return seatRepository.findAll(PageRequest.of(page, size))
                .map(seatMapper::toDto);
    }
}