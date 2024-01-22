package app.services.interfaces;

import app.dto.FlightDto;

import java.util.List;

public interface FlightServiceGenerator {
    FlightDto createRandomFlightDTO();

    List<FlightDto> generateRandomFlightDTO(Integer amt);
}
