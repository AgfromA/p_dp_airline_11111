package app.services.interfaces;

import app.dto.FlightDTO;

import java.util.List;

public interface FlightServiceGenerator {
    FlightDTO createRandomFlightDTO();

    List<FlightDTO> generateRandomFlightDTO(Integer amt);
}
