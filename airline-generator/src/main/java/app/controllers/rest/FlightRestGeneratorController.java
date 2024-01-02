package app.controllers.rest;

import app.controllers.api.FlightRestApiGenerator;
import app.dto.FlightDTO;
import app.services.interfaces.FlightServiceGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
public class FlightRestGeneratorController implements FlightRestApiGenerator {
    private final FlightServiceGenerator serviceGenerator;
    @Override
    public ResponseEntity<List<FlightDTO>> generateFlightDTO(Integer amt) {
        log.info("generate Flight amount = {}", amt);
        var flights = serviceGenerator.generateRandomFlightDTO(amt);
        return flights!=null
                ? new ResponseEntity<>(flights, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
