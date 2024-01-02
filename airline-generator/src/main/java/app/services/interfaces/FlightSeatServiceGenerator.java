package app.services.interfaces;

import app.dto.FlightSeatDTO;

import java.util.List;

public interface FlightSeatServiceGenerator {
    FlightSeatDTO createRandomFlightSeatDTO();
    List<FlightSeatDTO> generateRandomFlightSeatDTO(Integer amt);
}
