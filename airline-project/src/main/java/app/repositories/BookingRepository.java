package app.repositories;

import app.entities.Booking;
import app.entities.FlightSeat;
import app.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByFlightSeat(FlightSeat flightSeat);
    Optional<Booking> findByFlightSeatId(Long flightSeatId);

    List<Booking> findByBookingStatusAndBookingDate(BookingStatus status, LocalDateTime bookingDate);

}
