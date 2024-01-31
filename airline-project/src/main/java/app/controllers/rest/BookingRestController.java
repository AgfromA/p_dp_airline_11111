package app.controllers.rest;

import app.controllers.api.rest.BookingRestApi;
import app.dto.BookingDto;
import app.enums.BookingStatus;
import app.services.BookingService;
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
public class BookingRestController implements BookingRestApi {

    private final BookingService bookingService;

    @Override
    public ResponseEntity<Page<BookingDto>> getAllBookings(Integer page, Integer size) {
        log.info("getAll: search all Bookings");
        if (page == null || size == null) {
            log.info("getAll: get all list Bookings");
            return createUnPagedResponse();
        }

        var bookings = bookingService.getAllBookings(page, size);
        return bookings.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(bookings,HttpStatus.OK);
    }

    private ResponseEntity<Page<BookingDto>> createUnPagedResponse() {
        var bookings = bookingService.findAll();
        if (bookings.isEmpty()) {
            log.info("getAll: Bookings not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAll:found {} Bookings", bookings.size());
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(bookings)));
        }
    }

    @Override
    public ResponseEntity<BookingDto> getBookingById(Long id) {
        log.info("getById: search Booking by id = {}", id);
        var booking = bookingService.getBookingById(id);
        if (booking == null) {
            log.info("getById: not found Booking with id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingDto> getBookingByBookingNumber(String bookingNumber) {
        log.info("getByNumber: search Booking by number = {}", bookingNumber);
        var booking = bookingService.getBookingByNumber(bookingNumber);
        if (booking == null) {
            log.info("getByNumber: not found Booking with number = {}", bookingNumber);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingDto> createBooking(BookingDto bookingDTO) {
        log.info("create: creating a new Booking");
        bookingDTO.setBookingStatus(BookingStatus.NOT_PAID);
        return new ResponseEntity<>(bookingService.saveBooking(bookingDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BookingDto> updateBookingById(Long id, BookingDto bookingDTO) {
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