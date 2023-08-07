package app.controllers.rest;

import app.controllers.api.rest.BookingRestApi;
import app.dto.BookingDTO;
import app.services.interfaces.BookingService;
import app.util.mappers.BookingMapper;
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
public class BookingRestController implements BookingRestApi {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @Override
    public ResponseEntity<Page<BookingDTO>> getAllPagesBookingsDTO(Pageable pageable) {
        log.info("getAll: search all Bookings");
        Page<BookingDTO> bookings = bookingService.getAllBookings(pageable).map(entity -> {
            var dto = bookingMapper.convertToBookingDTOEntity(entity);
            return dto;
        });
        if (bookings == null) {
            log.info("getAll: Bookings not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingDTO> getBookingDTOById(Long id) {
        log.info("getById: search Booking by id = {}", id);
        var booking = bookingService.getBookingById(id);
        if (booking == null) {
            log.info("getById: not found Booking with id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new BookingDTO(booking), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingDTO> getBookingDTOByBookingNumber(String bookingNumber) {
        log.info("getByNumber: search Booking by number = {}", bookingNumber);
        var booking = bookingService.getBookingByNumber(bookingNumber);
        if (booking == null) {
            log.info("getByNumber: not found Booking with number = {}", bookingNumber);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new BookingDTO(booking), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingDTO> createBookingDTO(BookingDTO bookingDTO) {
        log.info("create: creating a new Booking");
        return new ResponseEntity<>(new BookingDTO(bookingService.saveBooking(bookingMapper
                .convertToBookingEntity(bookingDTO))),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BookingDTO> updateBookingDTOById(Long id, BookingDTO bookingDTO) {
        log.info("update: edit Booking with id = {}", id);
        if (bookingService.getBookingById(id) == null) {
            log.info("update: not found Booking with id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookingDTO.setId(id);
        return new ResponseEntity<>(new BookingDTO(bookingService.saveBooking(bookingMapper
                .convertToBookingEntity(bookingDTO))),
                HttpStatus.OK);
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