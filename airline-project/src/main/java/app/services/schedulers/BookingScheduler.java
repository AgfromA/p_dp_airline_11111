package app.services.schedulers;

import app.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingScheduler {

    private final BookingService bookingService;

    @Scheduled(fixedRate = 600000)
    public void scheduleBookingAndFlightSeatStatus() {
        bookingService.updateBookingAndFlightSeatStatusIfExpired();
    }
}
