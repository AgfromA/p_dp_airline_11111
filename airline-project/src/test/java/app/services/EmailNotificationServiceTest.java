package app.services;

import app.clients.MailClient;
import app.entities.Passenger;
import app.entities.Ticket;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class EmailNotificationServiceTest {

    @Mock
    TicketService ticketService;
    @Mock
    MailClient mailClient;
    @InjectMocks
    EmailNotificationService emailNotificationService;

    @Test
    void testSendEmailNotification(){
        var testPassenger = new Passenger();
        testPassenger.setEmail("test@mail.ru");
        var testTicket = new Ticket();
        testTicket.setPassenger(testPassenger);

        when(ticketService.getAllTicketsForEmailNotification(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(testTicket));
        emailNotificationService.sendEmailNotification();

        verify(mailClient, times(1)).sendEmail(eq("test@mail.ru"), anyString(), anyString());
    }
}