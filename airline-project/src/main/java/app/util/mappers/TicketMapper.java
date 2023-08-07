package app.util.mappers;

import app.dto.TicketDTO;
import app.entities.Ticket;
import app.exceptions.EntityNotFoundException;
import app.services.interfaces.FlightSeatService;
import app.services.interfaces.FlightService;
import app.services.interfaces.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketMapper {

    private final PassengerService passengerService;
    private final FlightService flightService;
    private final FlightSeatService flightSeatService;

    public Ticket convertToTicketEntity(TicketDTO ticketDTO) {
        var ticket = new Ticket();
        ticket.setTicketNumber(ticketDTO.getTicketNumber());
        ticket.setPassenger(passengerService.getPassengerById(ticketDTO.getPassengerId())
                .orElseThrow(() -> new EntityNotFoundException("Operation was not finished because Passenger was not found with id = "
                        + ticketDTO.getPassengerId())));
        ticket.setFlight(flightService.getFlightById(ticketDTO.getFlightId())
                .orElseThrow(() -> new EntityNotFoundException("Operation was not finished because Flight was not found with id = "
                        + ticketDTO.getFlightId())));
        ticket.setFlightSeat(flightSeatService.getFlightSeatById(ticketDTO.getFlightSeatId())
                .orElseThrow(() -> new EntityNotFoundException("Operation was not finished because FlightSeat was not found with id = "
                        + ticketDTO.getFlightSeatId())));
        return ticket;
    }
}
