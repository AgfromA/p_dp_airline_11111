package app.services;

import app.entities.BookingStatus;
import app.enums.BookingStatusType;
import app.repositories.BookingStatusRepository;
import app.services.interfaces.BookingStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingStatusServiceImpl implements BookingStatusService {

    private final BookingStatusRepository bookingStatusRepository;

    @Override
    public BookingStatus getBookingStatusByType(BookingStatusType bookingStatusType) {
        return bookingStatusRepository.findBookingStatusByBookingStatusType(bookingStatusType).orElse(null);
    }
}
