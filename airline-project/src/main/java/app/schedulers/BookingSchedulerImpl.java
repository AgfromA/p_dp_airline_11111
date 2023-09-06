package app.schedulers;

import app.schedulers.interfaces.BookingScheduler;
import app.services.interfaces.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingSchedulerImpl implements BookingScheduler {

    private final BookingService bookingService;
    @Override
    @Scheduled(fixedRate = 600000)
    public void scheduleBookingAndFlightSeatStatus() {
        bookingService.updateBookingAndFlightSeatStatusIfExpired();
    }
}
