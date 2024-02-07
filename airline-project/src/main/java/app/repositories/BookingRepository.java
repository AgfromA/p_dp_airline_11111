package app.repositories;

import app.entities.Booking;
import app.entities.FlightSeat;
import app.entities.Ticket;
import app.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT booking FROM Booking booking LEFT JOIN FETCH booking.flightSeat flightSeat " +
            "LEFT JOIN FETCH flightSeat.flight flight " +
            "LEFT JOIN FETCH booking.passenger WHERE flight.departureDateTime BETWEEN ?2 AND ?1")
    List<Booking> getAllBooksForEmailNotification(LocalDateTime departureIn, LocalDateTime gap);

    Optional<Booking> findByBookingNumber(String number);

    Optional<Booking> findByFlightSeat(FlightSeat flightSeat);
    Optional<Booking> findByFlightSeatId(Long flightSeatId);

    @Modifying
    @Query(value = "DELETE FROM Booking b WHERE b.passenger.id = :passengerId")
    void deleteBookingByPassengerId(@Param("passengerId") long passengerId);

    List<Booking> findByBookingStatusAndCreateTime(BookingStatus status, LocalDateTime createTime);


}
