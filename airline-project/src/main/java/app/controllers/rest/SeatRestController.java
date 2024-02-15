package app.controllers.rest;

import app.controllers.api.rest.SeatRestApi;
import app.dto.SeatDto;

import app.services.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class SeatRestController implements SeatRestApi {

    private final SeatService seatService;

    @Override
    public ResponseEntity<Page<SeatDto>> getAllSeats(Integer page, Integer size, Long aircraftId) {
        log.info("getAllSeats:");
        if (page == null || size == null) {
            return createUnPagedResponse();
        }

        Page<SeatDto> seats;
        if (aircraftId != null) {
            seats = seatService.getAllSeatsByAircraftId(page, size, aircraftId);
        } else {
            seats = seatService.getAllSeats(page, size);
        }
        return seats.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : ResponseEntity.ok(seats);
    }

    private ResponseEntity<Page<SeatDto>> createUnPagedResponse() {
        var seats = seatService.getAllSeats();
        if (seats.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAllSeats: count: {}", seats.size());
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(seats)));
        }
    }

    @Override
    public ResponseEntity<SeatDto> getSeat(Long id) {
        log.info("getSeat: by id: {}", id);
        var seat = seatService.getSeatDto(id);
        return seat.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<SeatDto> createSeat(SeatDto seatDTO) {
        log.info("createSeat:");
        return new ResponseEntity<>(seatService.saveSeat(seatDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<SeatDto> updateSeat(Long id, SeatDto seatDTO) {
        log.info("updateSeat: by id: {}", id);
        return ResponseEntity.ok(seatService.editSeat(id, seatDTO));
    }

    @Override
    public ResponseEntity<String> deleteSeat(Long id) {
        log.info("deleteSeat: by id: {}", id);
        seatService.deleteSeat(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Page<SeatDto>> generateSeats(Long aircraftId) {
        log.info("generateSeats: by aircraftId: {}", aircraftId);
        List<SeatDto> seatsList = seatService.generateSeats(aircraftId);
        Page<SeatDto> seatsPage = new PageImpl<>(seatsList);
        return new ResponseEntity<>(seatsPage, HttpStatus.CREATED);
    }
}