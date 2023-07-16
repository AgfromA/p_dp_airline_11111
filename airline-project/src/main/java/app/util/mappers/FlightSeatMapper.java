package app.util.mappers;

import app.dto.FlightSeatDTO;
import app.entities.FlightSeat;
import app.services.interfaces.FlightService;
import app.services.interfaces.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlightSeatMapper {
    private final SeatService seatService;
    private final FlightService flightService;

    public FlightSeat convertToFlightSeatEntity(FlightSeatDTO dto) {
        var flightSeat = new FlightSeat();
        flightSeat.setId(dto.getId());
        flightSeat.setFare(dto.getFare());
        flightSeat.setIsRegistered(dto.getIsRegistered());
        flightSeat.setIsSold(dto.getIsSold());
        flightSeat.setIsBooking(dto.getIsBooking());
        flightSeat.setFlight(flightService.findById(dto.getFlightId()).get());
        flightSeat.setSeat(seatService.findById(dto.getSeatId()));
        return flightSeat;
    }

    public FlightSeatDTO convertToFlightSeatDTOEntity(FlightSeat flightSeat) {
        var flightSeatDTO = new FlightSeatDTO();
        flightSeatDTO.setId(flightSeat.getId());
        flightSeatDTO.setFare(flightSeat.getFare());
        flightSeatDTO.setIsRegistered(flightSeat.getIsRegistered());
        flightSeatDTO.setIsSold(flightSeat.getIsSold());
        flightSeatDTO.setIsBooking(flightSeat.getIsBooking());
        flightSeatDTO.setFlightId(flightService.findById(flightSeat.getFlight().getId()).get().getId());
        flightSeatDTO.setSeatId(seatService.findById(flightSeat.getSeat().getId()).getId());
        return flightSeatDTO;
    }
}
