package app.services;


import app.entities.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final MailSender mailSender;
    private final TicketService ticketService;

    @Value("${notification.beforeDeparture.seconds}")
    private long beforeDeparture;
    @Value("${notification.periodOfDbCheck.milliseconds}")
    private long periodOfDbCheck;

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