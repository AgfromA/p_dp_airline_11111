package app.repositories;

import app.entities.Booking;
import app.enums.BookingStatus;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByFlightSeatId(Long flightSeatId);

    List<Booking> findByBookingStatusAndBookingDate(BookingStatus status, LocalDateTime bookingDate);
    @Modifying
    @Query(value = "DELETE FROM Booking b WHERE b.passenger.id = :passengerId")
    void deleteBookingByPassengerId(@Param("passengerId") long passengerId);
}