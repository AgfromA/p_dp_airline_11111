package app.controllers.rest;

import app.controllers.api.rest.BookingRestApi;
import app.dto.BookingDTO;
import app.enums.BookingStatus;
import app.services.interfaces.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookingRestController implements BookingRestApi {

    private final BookingService bookingService;

    @Override
    public ResponseEntity<List<BookingDTO>> getAllBookings(Integer page, Integer size) {
        log.info("getAll: search all Bookings");
        if (page == null || size == null) {
            log.info("getAll: get all list Bookings");
            return createUnPagedResponse();
        }
        if (page < 0 || size < 1) {
            log.info("getAll: no correct data");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        var bookings = bookingService.getAllBookings(page, size);
        return bookings.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(bookings.getContent(), HttpStatus.OK);
    }

    private ResponseEntity<List<BookingDTO>> createUnPagedResponse() {
        var bookings = bookingService.findAll();
        if (bookings.isEmpty()) {
            log.info("getAll: Bookings not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAll:found {} Bookings", bookings.size());
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<BookingDTO> getBookingById(Long id) {
        log.info("getById: search Booking by id = {}", id);
        var booking = bookingService.getBookingById(id);
        if (booking == null) {
            log.info("getById: not found Booking with id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingDTO> getBookingByBookingNumber(String bookingNumber) {
        log.info("getByNumber: search Booking by number = {}", bookingNumber);
        var booking = bookingService.getBookingByNumber(bookingNumber);
        if (booking == null) {
            log.info("getByNumber: not found Booking with number = {}", bookingNumber);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingDTO> createBooking(BookingDTO bookingDTO) {
        log.info("create: creating a new Booking");
        bookingDTO.setBookingStatus(BookingStatus.NOT_PAID);
        return new ResponseEntity<>(bookingService.saveBooking(bookingDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BookingDTO> updateBookingById(Long id, BookingDTO bookingDTO) {
        log.info("update: edit Booking with id = {}", id);
        var booking = bookingService.getBookingById(id);
        if (booking == null) {
            log.info("update: not found Booking with id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookingDTO.setBookingStatus(booking.getBookingStatus());
        bookingDTO.setId(id);
        return new ResponseEntity<>(bookingService.saveBooking(bookingDTO), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteBookingById(Long id) {
        log.info("deleteAircraftById: deleting a Booking with id = {}", id);
        try {
            bookingService.deleteBookingById(id);
        } catch (Exception e) {
            log.error("deleteAircraftById: error of deleting - Booking with id = {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}