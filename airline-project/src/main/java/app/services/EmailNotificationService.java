package app.services;

import app.entities.Booking;
import app.entities.Passenger;
import app.services.interfaces.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final MailSender mailSender;
    private final BookingService bookingService;

    @Value("${notification.beforeDeparture.seconds}")
    private long beforeDeparture;
    @Value("${notification.periodOfDbCheck.milliseconds}")
    private long periodOfDbCheck;

    @Scheduled(fixedRateString = "${notification.periodOfDbCheck.milliseconds}")
    public void sendEmailNotification() {
        List<Booking> books = bookingService.getAllBookingsForEmailNotification(LocalDateTime.now().plusSeconds(beforeDeparture),
                LocalDateTime.now().plusSeconds(beforeDeparture - (periodOfDbCheck / 1000)));

        List<Passenger> passengers = books.stream()
                .map(Booking::getPassenger)
                .collect(Collectors.toList());

        for (Passenger passenger : passengers) {
            mailSender.sendEmail(passenger.getEmail(), "Регистрация на рейс", "Ваш вылет через 24 часа, " +
                                                                              "пожалуйста, зарегистрируйтесь на рейс!");
        }
    }
}
