package app.util.mappers;

import app.dto.SeatDTO;
import app.entities.Seat;
import app.services.interfaces.AircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatMapper {

    private final AircraftService aircraftService;

    public Seat convertToSeatEntity(SeatDTO seatDTO) {
        var seat = new Seat();
        seat.setId(seatDTO.getId());
        seat.setSeatNumber(seatDTO.getSeatNumber());
        seat.setIsNearEmergencyExit(seatDTO.getIsNearEmergencyExit());
        seat.setIsLockedBack(seatDTO.getIsLockedBack());
        seat.setCategory(seatDTO.getCategory());
        seat.setAircraft(aircraftService.getAircraftById(seatDTO.getAircraftId()));
        return seat;
    }

    public SeatDTO convertToSeatDTOEntity(Seat seat) {
        var seatDTO = new SeatDTO();
        seatDTO.setId(seat.getId());
        seatDTO.setSeatNumber(seat.getSeatNumber());
        seatDTO.setIsNearEmergencyExit(seat.getIsNearEmergencyExit());
        seatDTO.setIsLockedBack(seat.getIsLockedBack());
        seatDTO.setCategory(seat.getCategory());
        seatDTO.setAircraftId(aircraftService.getAircraftById(seat.getAircraft().getId()).getId());
        return seatDTO;
    }
}
