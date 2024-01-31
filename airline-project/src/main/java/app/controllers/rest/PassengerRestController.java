package app.controllers.rest;

import app.controllers.api.rest.PassengerRestApi;
import app.dto.PassengerDto;
import app.services.PassengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PassengerRestController implements PassengerRestApi {

    private final PassengerService passengerService;

    @Override
    public ResponseEntity<Page<PassengerDto>> getAllPassengers(Integer page,
                                                               Integer size,
                                                               String firstName,
                                                               String lastName,
                                                               String email,
                                                               String serialNumberPassport) {
        log.info("getAllPassengers:");
        if (page == null || size == null) {
            return createUnPagedResponse();
        }
        Page<PassengerDto> passengers;
        Pageable pageable = PageRequest.of(page, size);
        if (firstName == null && lastName == null && email == null && serialNumberPassport == null) {
            passengers = passengerService.getAllPassengers(pageable);
        } else {
            log.info("getAllPassengers: filtered");
            passengers = passengerService.getAllPassengersFiltered(pageable, firstName, lastName, email, serialNumberPassport);
        }
        return passengers.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : ResponseEntity.ok(passengers);
    }

    private ResponseEntity<Page<PassengerDto>> createUnPagedResponse() {
        var passengers = passengerService.getAllPassengers();
        if (passengers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAllPassengers: count: {}", passengers.size());
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(passengers)));
        }
    }

    @Override
    public ResponseEntity<PassengerDto> getPassenger(Long id) {
        log.info("getPassenger: by id: {}", id);
        var passenger = passengerService.getPassengerDto(id);
        return passenger.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<PassengerDto> createPassenger(PassengerDto passengerDTO) {
        log.info("createPassenger:");
        return new ResponseEntity<>(passengerService.createPassenger(passengerDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PassengerDto> updatePassenger(Long id, PassengerDto passengerDTO) {
        log.info("updatePassenger: by id: {}", id);
        return ResponseEntity.ok(passengerService.updatePassenger(id, passengerDTO));
    }

    @Override
    public ResponseEntity<HttpStatus> deletePassenger(Long id) {
        log.info("deletePassenger: by id: {}", id);
        passengerService.deletePassenger(id);
        return ResponseEntity.ok().build();
    }
}