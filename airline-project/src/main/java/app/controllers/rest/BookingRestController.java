package app.controllers.rest;

import app.controllers.api.rest.BookingRestApi;
import app.dto.BookingDTO;

import app.mappers.BookingMapper;
import app.services.interfaces.BookingService;
import app.services.interfaces.CategoryService;
import app.services.interfaces.FlightService;
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
public class BookingRestController implements BookingRestApi {

    private final BookingService bookingService;
    private final PassengerService passengerService;
    private final FlightService flightService;
    private final CategoryService categoryService;

    @Override
    public ResponseEntity<Page<BookingDTO>> getAll(Pageable pageable) {
        log.info("getAll: search all Bookings");
        Page<BookingDTO> bookings = bookingService.findAll(pageable).map(entity -> {
            return BookingMapper.INSTANCE.convertToBookingDTOEntity(entity,passengerService,flightService,categoryService);
        });
        if (bookings == null) {
            log.info("getAll: Bookings not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingDTO> getById(Long id) {
        log.info("getById: search Booking by id = {}", id);
        var booking = bookingService.findById(id);
        if (booking == null) {
            log.info("getById: not found Booking with id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new BookingDTO(booking), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingDTO> getByNumber(String bookingNumber) {
        log.info("getByNumber: search Booking by number = {}", bookingNumber);
        var booking = bookingService.findByBookingNumber(bookingNumber);
        if (booking == null) {
            log.info("getByNumber: not found Booking with number = {}", bookingNumber);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new BookingDTO(booking), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingDTO> create(BookingDTO bookingDTO) {
        log.info("create: creating a new Booking");
        return new ResponseEntity<>(new BookingDTO(bookingService.save
                (BookingMapper.INSTANCE.convertToBookingEntity(bookingDTO,passengerService,flightService,categoryService))),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BookingDTO> update(Long id, BookingDTO bookingDTO) {
        log.info("update: edit Booking with id = {}", id);
        if (bookingService.findById(id) == null) {
            log.info("update: not found Booking with id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookingDTO.setId(id);
        return new ResponseEntity<>(new BookingDTO(bookingService.save
                (BookingMapper.INSTANCE.convertToBookingEntity(bookingDTO,passengerService,flightService,categoryService))),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> delete(Long id) {
        log.info("delete: deleting a Booking with id = {}", id);
        try {
            bookingService.deleteById(id);
        } catch (Exception e) {
            log.error("delete: error of deleting - Booking with id = {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}