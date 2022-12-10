package app.repositories;

import app.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT booking FROM Booking booking LEFT JOIN FETCH booking.flight flight " +
            "LEFT JOIN FETCH booking.passenger WHERE flight.departureDateTime BETWEEN ?2 AND ?1")
    List<Booking> getAllBooksForEmailNotification(LocalDateTime departureIn, LocalDateTime gap);

    Optional<Booking> findByBookingNumber(String number);
}
