package app.controllers.rest;

import app.controllers.api.rest.PassengerRestApi;
import app.dto.PassengerDTO;
import app.services.interfaces.PassengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PassengerRestController implements PassengerRestApi {

    private final PassengerService passengerService;

    @Override
    public ResponseEntity<Page<PassengerDTO>> getAll(Pageable pageable, String firstName, String lastName, String email, String serialNumberPassport) {
        Page<PassengerDTO> passengers;
        if (firstName == null && lastName == null && email == null && serialNumberPassport == null) {
            passengers = passengerService.getAllPagesPassengers(pageable);
            log.info("getAll: get all Passenger");
            log.info(passengers.toString());
        } else {
            log.info("filter: filter Passenger by firstname or lastname or email or serialNumberPassport");
            passengers = passengerService.getAllPagesPassengerByKeyword(pageable, firstName, lastName, email, serialNumberPassport);
            log.info(passengers.toString());
        }
        log.info("passenger пустой: " + passengers.isEmpty());
        if (passengers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(passengers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PassengerDTO> getById(Long id) {
        log.info("getById: get passenger by ID = {}", id);
        var passenger = passengerService.getPassengerById(id);

        if (passenger.isEmpty()) {
            log.error("getById: passenger with this id={} doesnt exist", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(new PassengerDTO(passenger.get()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PassengerDTO> create(PassengerDTO passengerDTO) {
        if (passengerDTO.getId() != null) {
            log.error("create: passenger already exist in database");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("create: new passenger added");
        return new ResponseEntity<>(new PassengerDTO(passengerService.savePassenger(passengerDTO)),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PassengerDTO> updateById(Long id, PassengerDTO passengerDTO) {
        passengerDTO.setId(id);
        log.info("update: update Passenger with id = {}", id);
        return new ResponseEntity<>(new PassengerDTO(passengerService.updatePassengerById(id, passengerDTO)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteById(Long id) {
        log.info("delete: passenger with id={} deleted", id);
        passengerService.deletePassengerById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}