package app.services;

import app.entities.Passenger;
import app.repositories.PassengerRepository;
import app.services.interfaces.BookingService;
import app.services.interfaces.FlightSeatService;
import app.services.interfaces.PassengerService;
import app.services.interfaces.TicketService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final BookingService bookingService;
    private final TicketService ticketService;
    private final FlightSeatService flightSeatService;

    // FIXME Отрефакторить
    public PassengerServiceImpl(PassengerRepository passengerRepository,
                                @Lazy BookingService bookingService, @Lazy TicketService ticketService,
                                @Lazy FlightSeatService flightSeatService) {
        this.passengerRepository = passengerRepository;
        this.bookingService = bookingService;
        this.ticketService = ticketService;
        this.flightSeatService = flightSeatService;
    }

    @Override
    @Transactional
    public Passenger savePassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    @Override
    public Optional<Passenger> getPassengerById(Long id) {
        return passengerRepository.findById(id);
    }


    @Override
    @Transactional
    public Passenger updatePassengerById(Long id, Passenger passenger) {
        var editPassenger = new Passenger();
        editPassenger.setFirstName(passenger.getFirstName());
        editPassenger.setLastName(passenger.getLastName());
        editPassenger.setBirthDate(passenger.getBirthDate());
        editPassenger.setPhoneNumber(passenger.getPhoneNumber());
        editPassenger.setEmail(passenger.getEmail());
        editPassenger.setPassport(passenger.getPassport());

        return passengerRepository.save(passenger);
    }

    @Override
    public Page<Passenger> getAllPagesPassengerByKeyword(Pageable pageable, String firstName, String lastName, String email, String serialNumberPassport) {
        if (firstName != null) {
            return passengerRepository.findAllByFirstName(pageable, firstName);
        }
        if (lastName != null) {
            return passengerRepository.findByLastName(pageable, lastName);
        }
        if (email != null) {
            return passengerRepository.findByEmail(pageable, email);
        }
        if (serialNumberPassport != null) {
            return passengerRepository.findByPassportSerialNumber(pageable, serialNumberPassport);
        }
        return passengerRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deletePassengerById(Long id) {
        flightSeatService.editFlightSeatIsSoldToFalseByFlightSeatId(ticketService.getArrayOfFlightSeatIdByPassengerId(id));
        bookingService.deleteBookingByPassengerId(id);
        ticketService.deleteTicketByPassengerId(id);
        passengerRepository.deleteById(id);
    }

    @Override
    public Page<Passenger> getAllPagesPassengers(Pageable pageable) {
        return passengerRepository.findAll(pageable);
    }

    @Override
    public Page<Passenger> getAllPagesPassengers(int page, int size) {
        return passengerRepository.findAll(PageRequest.of(page, size));
    }

}

