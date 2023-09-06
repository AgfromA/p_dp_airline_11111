package app.services;

import app.dto.BookingDTO;
import app.entities.Booking;
import app.enums.BookingStatusType;
import app.exceptions.FlightSeatIsBookedException;
import app.mappers.BookingMapper;
import app.repositories.BookingRepository;
import app.repositories.BookingStatusRepository;
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
    private final BookingStatusRepository bookingStatusRepository;
    private final BookingStatusService bookingStatusService;


    @Transactional
    @Override
    public Booking saveBooking(BookingDTO bookingDTO) {
        var booking = BookingMapper.INSTANCE
                .convertToBookingEntity(bookingDTO,passengerService,flightSeatService, bookingStatusService);
        if (booking.getFlightSeat().getIsBooked()){
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

        return bookingRepository.save(booking);
    }

    @Override
    public Page<BookingDTO> getAllBookings(Integer page, Integer size) {
        return bookingRepository.findAll(PageRequest.of(page, size)).map(BookingMapper.INSTANCE::convertToBookingDTOEntity);
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
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
    public Booking getBookingByNumber(String number) {
        return bookingRepository.findByBookingNumber(number).orElse(null);
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
        List<Booking> bookingList = bookingRepository.findByStatusAndCreateTime(bookingStatusRepository.findBookingStatusByBookingStatusType(BookingStatusType.NOT_PAID).get(),
                LocalDateTime.now().minusMinutes(10));

        for (Booking booking : bookingList) {
            booking.getStatus().setBookingStatusType(BookingStatusType.OVERDUE);
            booking.getFlightSeat().setIsBooked(false);
            booking.setCreateTime(null);
            bookingRepository.save(booking);
        }
    }
}
