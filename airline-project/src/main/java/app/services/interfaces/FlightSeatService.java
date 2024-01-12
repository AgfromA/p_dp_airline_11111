package app.services.interfaces;

import app.dto.FlightSeatDto;
import app.dto.SeatDto;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.entities.Seat;
import app.enums.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FlightSeatService {

    Set<FlightSeat> getAllFlightSeats();

    Page<FlightSeatDto> getAllFlightSeats(Integer page, Integer size);

    Optional<FlightSeat> getFlightSeatById(Long id);

    Set<FlightSeatDto> getFlightSeatsByFlightId(Long flightId);


    List<FlightSeatDto> getAllListFlightSeats();

    Page<FlightSeatDto> getFreeSeatsById(Pageable pageable, Long id);

    Page<FlightSeatDto> getFlightSeatsByFlightId(Long flightId, Pageable pageable);

    Set<FlightSeat> getFlightSeatsByFlightNumber(String flightNumber);

    Set<FlightSeat> addFlightSeatsByFlightId(Long flightId);

    Set<FlightSeat> addFlightSeatsByFlightNumber(String flightNumber);

    FlightSeat saveFlightSeat(FlightSeat flightSeat);

    FlightSeatDto saveFlightSeat(FlightSeatDto flightSeatDTO);

    int getNumberOfFreeSeatOnFlight(Flight flight);

    Set<Seat> getSetOfFreeSeatsOnFlightByFlightId(Long id);

    List<SeatDto> getAllSeats();

    Set<FlightSeat> getFlightSeatsBySeat(Seat seat);

    void deleteFlightSeatById(Long id);

    Set<FlightSeat> getNotSoldFlightSeatsById(Long id);

    List<FlightSeatDto> getCheapestFlightSeatsByFlightIdAndSeatCategory(Long id, CategoryType type);

    Page<FlightSeatDto> getNotSoldFlightSeatsById(Long id, Pageable pageable);

    Page<FlightSeatDto> findNotRegisteredFlightSeatsById(Long id, Pageable pageable);

    void editFlightSeatIsSoldToFalseByFlightSeatId(long[] flightSeatId);

    List<FlightSeat> findByFlightId(Long id);

}
