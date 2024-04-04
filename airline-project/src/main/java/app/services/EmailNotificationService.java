package app.services;

import app.clients.MailClient;
import app.entities.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Сервис для отправки электронных уведомлений пассажирам о предстоящей регистрации на рейс
 */
@Service
@RequiredArgsConstructor
public class
EmailNotificationService {

    private final MailClient mailClient;
    private final TicketService ticketService;

    /**
     * Количество секунд до вылета рейса, за которое нужно отправить уведомление
     */
    @Value("${notification.beforeDeparture.seconds}")
    private long beforeDeparture;

    /**
     * Периодичность проверки наличия рейсов для отправки уведомлений в миллисекундах.
     */
    @Scheduled(fixedRateString = "${notification.periodOfDbCheck.milliseconds}")
    public void sendEmailNotification() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime departureTime = now.plusSeconds(beforeDeparture);
        ticketService.getAllTicketsForEmailNotification(departureTime, now)
                .stream()
                .map(Ticket::getPassenger)
                .collect(Collectors.toList())
                .forEach(passenger -> mailClient.sendEmail(
                        passenger.getEmail(),
                        "Регистрация на рейс",
                        "Ваш вылет через 24 часа, пожалуйста, зарегистрируйтесь на рейс!")
                );
    }
}