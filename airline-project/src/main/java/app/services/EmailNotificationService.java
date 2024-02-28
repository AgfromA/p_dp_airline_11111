package app.services;


import app.entities.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.stream.Collectors;
/**
 * Сервис для отправки электронных уведомлений пассажирам о предстоящей регистрации на рейс.
 * Этот сервис автоматически проверяет список рейсов, время вылета которых приближается,
 * и отправляет уведомления соответствующим пассажирам.
 */
@Service
@RequiredArgsConstructor
public class EmailNotificationService {
    /**
     * Определяется внедряемый экземпляр сервиса для отправки электронной почты.
     */
    private final MailSender mailSender;

    /**
     * Определяется внедряемый экземпляр сервиса для работы с билетами.
     */
    private final TicketService ticketService;

    /**
     * Количество секунд до вылета рейса, за которое нужно отправить уведомление.
     */
    @Value("${notification.beforeDeparture.seconds}")
    private long beforeDeparture;

    /**
     * Периодичность проверки наличия рейсов для отправки уведомлений в миллисекундах.
     */
    @Value("${notification.periodOfDbCheck.milliseconds}")
    private long periodOfDbCheck;

    /**
     * Метод, который отправляет уведомление пассажирам на электронную почту, о том что вылет их
     * рейса через определенное количество времени и нужно пройти регистрацию.
     * Метод запускается с определенной в аннотации Scheduled периодичностью.
     */
    @Scheduled(fixedRateString = "${notification.periodOfDbCheck.milliseconds}")
    public void sendEmailNotification() {
        var tickets = ticketService.getAllTicketsForEmailNotification(LocalDateTime.now()
                        .plusSeconds(beforeDeparture),
                        LocalDateTime.now().plusSeconds(beforeDeparture - (periodOfDbCheck / 1000)));
        var passengers = tickets.stream()
                .map(Ticket::getPassenger)
                .collect(Collectors.toList());
        for (var passenger : passengers) {
            mailSender.sendEmail(passenger.getEmail()
                    , "Регистрация на рейс"
                    , "Ваш вылет через 24 часа, пожалуйста, зарегистрируйтесь на рейс!");
        }
    }
}