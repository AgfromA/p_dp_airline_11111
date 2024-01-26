package app.services;

import app.dto.PassengerDto;
import app.entities.Passenger;
import app.exceptions.EntityNotFoundException;
import app.mappers.PassengerMapper;
import app.repositories.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PassengerService {

    @Lazy // FIXME костыль
    @Autowired
    private BookingService bookingService;
    @Lazy // FIXME костыль
    @Autowired
    private TicketService ticketService;
    private final FlightSeatService flightSeatService;
    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    public List<PassengerDto> getAllPassengers() {
        return passengerMapper.toDtoList(passengerRepository.findAll());
    }

    public Page<PassengerDto> getAllPassengers(Pageable pageable) {
        return passengerRepository.findAll(pageable).map(passengerMapper::toDto);
    }

    // FIXME страшновтая портянка. Отрефакторить
    public Page<PassengerDto> getAllPassengersFiltered(Pageable pageable,
                                                       String firstName,
                                                       String lastName,
                                                       String email,
                                                       String serialNumberPassport) {
        if (firstName != null && lastName != null && !firstName.isEmpty() && !lastName.isEmpty()) {
            return passengerRepository.findByFirstNameAndLastName(pageable, firstName, lastName)
                    .map(passengerMapper::toDto);
        }
        if (firstName != null && !firstName.isEmpty()) {
            return passengerRepository.findAllByFirstName(pageable, firstName)
                    .map(passengerMapper::toDto);
        }
        if (lastName != null && !lastName.isEmpty()) {
            return passengerRepository.findByLastName(pageable, lastName)
                    .map(passengerMapper::toDto);
        }
        if (email != null && !email.isEmpty()) {
            return passengerRepository.findByEmail(pageable, email)
                    .map(passengerMapper::toDto);
        }
        if (serialNumberPassport != null && !serialNumberPassport.isEmpty()) {
            return passengerRepository.findByPassportSerialNumber(pageable, serialNumberPassport)
                    .map(passengerMapper::toDto);
        }
        return passengerRepository.findAll(pageable).map(passengerMapper::toDto);
    }

    public Optional<Passenger> getPassenger(Long id) {
        return passengerRepository.findById(id);
    }

    public Optional<PassengerDto> getPassengerDto(Long id) {
        return passengerRepository.findById(id).map(passengerMapper::toDto);
    }

    @Transactional
    public PassengerDto createPassenger(PassengerDto passengerDto) {
        passengerDto.setId(null);
        var passenger = passengerMapper.toEntity(passengerDto);
        return passengerMapper.toDto(passengerRepository.save(passenger));
    }

    @Transactional
    public PassengerDto updatePassenger(Long id, PassengerDto passengerDto) {
        var existingPassenger = passengerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation was not finished because Passenger was not found with id = " + id)
        );
        if (passengerDto.getFirstName() != null) {
            existingPassenger.setFirstName(passengerDto.getFirstName());
        }
        if (passengerDto.getLastName() != null) {
            existingPassenger.setLastName(passengerDto.getLastName());
        }
        if (passengerDto.getBirthDate() != null) {
            existingPassenger.setBirthDate(passengerDto.getBirthDate());
        }
        if (passengerDto.getPhoneNumber() != null) {
            existingPassenger.setPhoneNumber(passengerDto.getPhoneNumber());
        }
        if (passengerDto.getEmail() != null) {
            existingPassenger.setEmail(passengerDto.getEmail());
        }
        if (passengerDto.getPassport() != null) {
            existingPassenger.setPassport(passengerDto.getPassport());
        }
        return passengerMapper.toDto(passengerRepository.save(existingPassenger));
    }

    @Transactional
    public void deletePassenger(Long id) {
        flightSeatService.makeFlightSeatNotSold(ticketService.getFlightSeatIdsByPassengerId(id));
        bookingService.deleteBookingByPassengerId(id);
        ticketService.deleteTicketByPassengerId(id);
        passengerRepository.deleteById(id);
    }
}