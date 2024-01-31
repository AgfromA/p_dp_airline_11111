package app.controllers.rest;

import app.controllers.api.FlightSeatRestApiGenerator;
import app.dto.FlightSeatDto;
import app.services.FlightSeatServiceGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FlightSeatRestGeneratorController implements FlightSeatRestApiGenerator {
    private final FlightSeatServiceGenerator flightSeatServiceGenerator;

    @Override
    public ResponseEntity<Page<FlightSeatDto>> generateFlightSeatDTO(Integer amt) {
        log.info("generate Flight Seat amount = {}", amt);
        var flights = flightSeatServiceGenerator.generateRandomFlightSeatDTO(amt);
        return flights != null
                ? ResponseEntity.ok(new PageImpl<>(new ArrayList<>(flights)))
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
