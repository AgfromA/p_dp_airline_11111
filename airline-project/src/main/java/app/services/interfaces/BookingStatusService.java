package app.services.interfaces;

import app.entities.BookingStatus;
import app.enums.BookingStatusType;

public interface BookingStatusService {
    BookingStatus getBookingStatusByType(BookingStatusType bookingStatusType);
}
