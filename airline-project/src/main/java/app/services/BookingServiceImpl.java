package app.services;

import app.dto.BookingDTO;
import app.entities.Booking;
import app.enums.BookingStatus;
import app.exceptions.FlightSeatIsBookedException;
import app.mappers.BookingMapper;
import app.repositories.BookingRepository;
import app.services.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerService passengerService;
    private final FlightSeatService flightSeatService;
    private final BookingMapper bookingMapper;


    @Override
    public List<BookingDTO> findAll() {
        return bookingMapper.convertToBookingDTOEntityList(bookingRepository.findAll());
    }

    @Transactional
    @Override
    public BookingDTO saveBooking(BookingDTO bookingDTO) {
        var booking = bookingMapper
                .convertToBookingEntity(bookingDTO, passengerService, flightSeatService);
        if (booking.getFlightSeat().getIsBooked()) {
            throw new FlightSeatIsBookedException("FlightSeat is already booked.");
        } else {
            booking.getFlightSeat().setIsBooked(true);
        }

        if (booking.getCreateTime() == null) {
            booking.setCreateTime(LocalDateTime.now());
        }

        if (booking.getId() == 0) {
            booking.setBookingNumber(generateBookingNumber());
        } else {
            booking.setBookingNumber(bookingRepository.findById(booking.getId()).get().getBookingNumber());
        }

        return bookingMapper.convertToBookingDTOEntity(bookingRepository.save(booking));
    }

    @Override
    public Page<BookingDTO> getAllBookings(Integer page, Integer size) {
        return bookingRepository.findAll(PageRequest.of(page, size)).map(bookingMapper::convertToBookingDTOEntity);
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        return bookingMapper.convertToBookingDTOEntity(bookingRepository.findById(id).orElse(null));
    }

    @Transactional
    @Override
    public void deleteBookingById(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<Booking> getAllBookingsForEmailNotification(LocalDateTime departureIn, LocalDateTime gap) {
        return bookingRepository.getAllBooksForEmailNotification(departureIn, gap);
    }

    @Override
    public BookingDTO getBookingByNumber(String number) {
        return bookingMapper.convertToBookingDTOEntity(bookingRepository.findByBookingNumber(number).orElse(null));
    }

    @Override
    @Transactional
    public void deleteBookingByPassengerId(long passengerId) {
        bookingRepository.deleteBookingByPassengerId(passengerId);
    }

    private String generateBookingNumber() {
        return UUID.randomUUID().toString().substring(0, 9);
    }

    @Override
    @Transactional
    public void updateBookingAndFlightSeatStatusIfExpired() {
        List<Booking> bookingList = bookingRepository.findByBookingStatusAndCreateTime(BookingStatus.NOT_PAID,
                LocalDateTime.now().minusMinutes(10));

        for (Booking booking : bookingList) {
            booking.setBookingStatus(BookingStatus.OVERDUE);
            booking.getFlightSeat().setIsBooked(false);
            booking.setCreateTime(null);
            bookingRepository.save(booking);
        }
    }
}
