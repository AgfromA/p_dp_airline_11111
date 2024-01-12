package app.services;

import app.dto.PassengerDto;
import app.entities.Passenger;
import app.mappers.PassengerMapper;
import app.repositories.PassengerRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final BookingService bookingService;
    private final TicketService ticketService;
    private final FlightSeatService flightSeatService;
    private final PassengerMapper passengerMapper;

    // FIXME Отрефакторить
    public PassengerService(PassengerRepository passengerRepository,
                            @Lazy BookingService bookingService, @Lazy TicketService ticketService,
                            @Lazy FlightSeatService flightSeatService, PassengerMapper passengerMapper) {
        this.passengerRepository = passengerRepository;
        this.bookingService = bookingService;
        this.ticketService = ticketService;
        this.flightSeatService = flightSeatService;
        this.passengerMapper = passengerMapper;
    }

    public List<PassengerDto> getAllPassengers() {
        return passengerMapper.convertToPassengerDtoList(passengerRepository.findAll());
    }

    @Transactional
    public Passenger savePassenger(PassengerDto passengerDTO) {
        var passenger = passengerMapper.convertToPassenger(passengerDTO);
        return passengerRepository.save(passenger);
    }

    public Optional<Passenger> getPassengerById(Long id) {
        return passengerRepository.findById(id);
    }

    @Transactional
    public Passenger updatePassengerById(Long id, PassengerDto passengerDTO) {
        var passenger = passengerMapper.convertToPassenger(passengerDTO);
        var editPassenger = new Passenger();
        editPassenger.setFirstName(passenger.getFirstName());
        editPassenger.setLastName(passenger.getLastName());
        editPassenger.setBirthDate(passenger.getBirthDate());
        editPassenger.setPhoneNumber(passenger.getPhoneNumber());
        editPassenger.setEmail(passenger.getEmail());
        editPassenger.setPassport(passenger.getPassport());

        return passengerRepository.save(passenger);
    }

    public Page<PassengerDto> getAllPagesPassengerByKeyword(Pageable pageable, String firstName, String lastName, String email, String serialNumberPassport) {
        if (firstName != null && lastName != null && !firstName.isEmpty() && !lastName.isEmpty()) {
            return passengerRepository.findByFirstNameAndLastName(pageable, firstName, lastName)
                    .map(passengerMapper::convertToPassengerDto);
        }
        if (firstName != null && !firstName.isEmpty()) {
            return passengerRepository.findAllByFirstName(pageable, firstName)
                    .map(passengerMapper::convertToPassengerDto);
        }
        if (lastName != null && !lastName.isEmpty()) {
            return passengerRepository.findByLastName(pageable, lastName)
                    .map(passengerMapper::convertToPassengerDto);
        }
        if (email != null && !email.isEmpty()) {
            return passengerRepository.findByEmail(pageable, email)
                    .map(passengerMapper::convertToPassengerDto);
        }
        if (serialNumberPassport != null && !serialNumberPassport.isEmpty()) {
            return passengerRepository.findByPassportSerialNumber(pageable, serialNumberPassport)
                    .map(passengerMapper::convertToPassengerDto);
        }
        return passengerRepository.findAll(pageable)
                .map(passengerMapper::convertToPassengerDto);
    }

    @Transactional
    public void deletePassengerById(Long id) {
        flightSeatService.editFlightSeatIsSoldToFalseByFlightSeatId(ticketService.getArrayOfFlightSeatIdByPassengerId(id));
        bookingService.deleteBookingByPassengerId(id);
        ticketService.deleteTicketByPassengerId(id);
        passengerRepository.deleteById(id);
    }

    public Page<PassengerDto> getAllPagesPassengers(Pageable pageable) {
        return passengerRepository.findAll(pageable).map(passengerMapper::convertToPassengerDto);
    }
}