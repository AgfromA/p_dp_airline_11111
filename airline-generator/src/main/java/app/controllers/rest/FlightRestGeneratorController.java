package app.controllers.rest;

import app.controllers.api.FlightRestApiGenerator;
import app.dto.FlightDto;
import app.services.FlightServiceGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@Slf4j
@RestController
@RequiredArgsConstructor
public class FlightRestGeneratorController implements FlightRestApiGenerator {
    private final FlightServiceGenerator serviceGenerator;

    @Override
    public ResponseEntity<Page<FlightDto>> generateFlightDTO(Integer amt) {
        log.info("generate Flight amount = {}", amt);
        var flights = serviceGenerator.generateRandomFlightDTO(amt);
        return flights != null
                ? ResponseEntity.ok(new PageImpl<>(new ArrayList<>(flights)))
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
