package app.repositories;

import app.entities.BookingStatus;
import app.enums.BookingStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingStatusRepository extends JpaRepository <BookingStatus, Long> {
    Optional<BookingStatus> findBookingStatusByBookingStatusType(BookingStatusType bookingStatusType);
}
