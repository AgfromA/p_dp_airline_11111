package app.services;

import app.dto.SeatDTO;
import app.entities.Aircraft;
import app.entities.Category;
import app.entities.Seat;
import app.enums.seats.SeatsNumbersByAircraft;
import app.enums.seats.interfaces.AircraftSeats;
import app.enums.CategoryType;
import app.exceptions.ViolationOfForeignKeyConstraintException;
import app.repositories.FlightSeatRepository;
import app.repositories.SeatRepository;
import app.services.interfaces.AircraftService;
import app.services.interfaces.CategoryService;
import app.services.interfaces.SeatService;
import lombok.RequiredArgsConstructor;
import app.util.mappers.SeatMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final CategoryService categoryService;
    private final AircraftService aircraftService;
    private final SeatMapper seatMapper;
    private final FlightSeatRepository flightSeatRepository;

    @Transactional
    @Override
    public Seat saveSeat(Seat seat) {
        if (seat.getId() != 0) {
            Seat aldSeat = getSeatById(seat.getId());
            if (aldSeat != null && aldSeat.getAircraft() != null) {
                seat.setAircraft(aldSeat.getAircraft());
            }
        }
        seat.setCategory(categoryService.getCategoryByType(seat.getCategory().getCategoryType()));
        return seatRepository.saveAndFlush(seat);
    }

    @Override
    public Seat getSeatById(long id) {
        return seatRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Seat editSeatById(Long id, Seat seat) {
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

    @Override
    @Transactional
    public void deleteSeatById(Long id) throws ViolationOfForeignKeyConstraintException {
        if (!(flightSeatRepository.findFlightSeatsBySeat(getSeatById(id))).isEmpty()) {
            throw new ViolationOfForeignKeyConstraintException(
                    String.format("Seat with id = %d cannot be deleted because it is locked by the table \"flight_seat\"", id));
        }
        seatRepository.deleteById(id);
    }

    @Override
    public Page<Seat> getPagesSeatsByAircraftId(Long id, Pageable pageable) {
        return seatRepository.findByAircraftId(id, pageable);
    }

    @Override
    @Transactional
    public List<SeatDTO> generateSeatsDTOByAircraftId(long aircraftId) {
       var economyCategory = categoryService.getCategoryByType(CategoryType.ECONOMY);
       var businessCategory = categoryService.getCategoryByType(CategoryType.BUSINESS);

        List<SeatDTO> savedSeatsDTO = new ArrayList<>(getNumbersOfSeatsByAircraftId(aircraftId).getTotalNumberOfSeats());
        if (getPagesSeatsByAircraftId(aircraftId, Pageable.unpaged()).getTotalElements() > 0) {
            return savedSeatsDTO;
        }
        int enumSeatsCounter = 0;
        for (SeatDTO seatDTO : getSeatsDTOByAircraftId(aircraftId)) {
            seatDTO.setSeatNumber(getAircraftSeatsByAircraftId(aircraftId)[enumSeatsCounter].getNumber());
            seatDTO.setAircraftId(aircraftId);
            if (enumSeatsCounter < getNumbersOfSeatsByAircraftId(aircraftId).getNumberOfBusinessClassSeats()) { //Назначаем категории
                seatDTO.setCategory(businessCategory);
            } else {
                seatDTO.setCategory(economyCategory);
            }
            seatDTO.setIsNearEmergencyExit(getAircraftSeatsByAircraftId(aircraftId)[enumSeatsCounter].isNearEmergencyExit());
            seatDTO.setIsLockedBack(getAircraftSeatsByAircraftId(aircraftId)[enumSeatsCounter].isLockedBack());
            enumSeatsCounter += 1;

            var savedSeat = saveSeat(seatMapper.convertToSeatEntity(seatDTO));
            savedSeatsDTO.add(new SeatDTO(savedSeat));
        }
        return savedSeatsDTO;
    }

    private SeatsNumbersByAircraft getNumbersOfSeatsByAircraftId(long aircraftId) {
        var aircraft = aircraftService.getAircraftById(aircraftId); //создается объект САМОЛЕТ
        return SeatsNumbersByAircraft.valueOf(aircraft.getModel() //количество мест в самолете
                .toUpperCase().replaceAll("[^A-Za-z0-9]","_"));
    }

    private AircraftSeats[] getAircraftSeatsByAircraftId(long aircraftId) {
        return getNumbersOfSeatsByAircraftId(aircraftId).getAircraftSeats();
    }

    private List<SeatDTO> getSeatsDTOByAircraftId(long aircraftId) {
        return Stream.generate(SeatDTO::new)
                .limit(getNumbersOfSeatsByAircraftId(aircraftId).getTotalNumberOfSeats())
                .collect(Collectors.toList());
    }

    @Override
    public Page<Seat> getAllPagesSeats(Pageable pageable) {
        return seatRepository.findAll(pageable);
    }
}
