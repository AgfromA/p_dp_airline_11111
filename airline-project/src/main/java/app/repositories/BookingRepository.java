package app.repositories;

import app.entities.Booking;
import app.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookingStatusAndBookingDate(BookingStatus status, LocalDateTime bookingDate);

    @Modifying
    @Query(value = "DELETE FROM Booking b WHERE b.passenger.id = :passengerId")
    void deleteBookingByPassengerId(Long passengerId);
}