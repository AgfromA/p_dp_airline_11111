package app.services.interfaces;

import app.dto.FlightSeatDto;

import java.util.List;

public interface FlightSeatServiceGenerator {
    FlightSeatDto createRandomFlightSeatDTO();

    List<FlightSeatDto> generateRandomFlightSeatDTO(Integer amt);
}
