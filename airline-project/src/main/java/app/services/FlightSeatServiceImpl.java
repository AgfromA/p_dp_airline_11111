package app.services;

import app.entities.Flight;
import app.entities.FlightSeat;
import app.entities.Seat;
import app.repositories.FlightRepository;
import app.repositories.FlightSeatRepository;
import app.services.interfaces.FlightSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightSeatServiceImpl implements FlightSeatService {

    private final FlightSeatRepository flightSeatRepository;
    private final FlightRepository flightRepository;


    @Override
    public Set<FlightSeat> findAll() {
        Set<FlightSeat> flightSeatSet = new HashSet<>();
        flightSeatRepository.findAll().forEach(flightSeatSet::add);
        return flightSeatSet;
    }

    @Override
    public FlightSeat findById(Long id) {
        return flightSeatRepository.findById(id).orElse(null);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @Override
    public Set<FlightSeat> findByFlightId(Long flightId) {
        return flightSeatRepository.findFlightSeatsByFlightId(flightId);
    }

    @Override
    public Set<FlightSeat> findByFlightNumber(String flightNumber) {
        Set<FlightSeat> flightSeatSet = new HashSet<>();
        flightSeatRepository.findFlightSeatByFlight(flightRepository.getByCode(flightNumber))
                .forEach(flightSeatSet::add);
        return flightSeatSet;
    }

    @Transactional
    @Override
    public Set<FlightSeat> addFlightSeatsByFlightId(Long flightId) {
        Set<FlightSeat> seatsForAdd = new HashSet<>();
        Set<FlightSeat> allFlightSeats = findAll();
        Flight flight = flightRepository.getById(flightId);
        if (flight != null) {
            Set<Seat> seatsAircraft = flight.getAircraft().getSeatSet();

            for (Seat s : seatsAircraft) {
                FlightSeat flightSeat = new FlightSeat();
                flightSeat.setSeat(s);
                flightSeat.setFlight(flight);
                if (allFlightSeats.contains(flightSeat)) {
                    continue;
                }
                flightSeat.setFare(0);
                flightSeat.setIsSold(false);
                flightSeat.setIsRegistered(false);
                seatsForAdd.add(flightSeat);
            }

            for (FlightSeat f : seatsForAdd) {
                f = flightSeatRepository.save(f);
            }
        }
        return seatsForAdd;
    }

    @Override
    @Transactional
    public Set<FlightSeat> addFlightSeatsByFlightNumber(String flightNumber) {
        Set<FlightSeat> seatsForAdd = new HashSet<>();
        Set<FlightSeat> allFlightSeats = findAll();
        Flight flight = flightRepository.getByCode(flightNumber);
        if (flight != null) {
            Set<Seat> seatsAircraft = flight.getAircraft().getSeatSet();

            for (Seat s : seatsAircraft) {
                FlightSeat flightSeat = new FlightSeat();
                flightSeat.setSeat(s);
                flightSeat.setFlight(flight);
                if (allFlightSeats.contains(flightSeat)) {
                    continue;
                }
                flightSeat.setFare(0);
                flightSeat.setIsSold(false);
                flightSeat.setIsRegistered(false);
                seatsForAdd.add(flightSeat);
            }


            for (FlightSeat f : seatsForAdd) {
                f = flightSeatRepository.save(f);
            }
        }
        return seatsForAdd;
    }

    @Override
    @Transactional
    public FlightSeat saveFlightSeat(FlightSeat flightSeat) {
        return flightSeatRepository.save(flightSeat);
    }

    public FlightSeat editFlightSeat(Long id, FlightSeat flightSeat) {
        var targetFlightSeat = flightSeatRepository.findById(id).orElse(null);
        flightSeat.setId(id);

        if(flightSeat.getFare() == null) {
            flightSeat.setFare(targetFlightSeat.getFare());
        }
        if(flightSeat.getIsSold() == null) {
            flightSeat.setIsSold(targetFlightSeat.getIsSold());
        }
        if(flightSeat.getIsBooking() == null) {
            flightSeat.setIsBooking(targetFlightSeat.getIsBooking());
        }
        if(flightSeat.getFlight() == null) {
            flightSeat.setFlight(targetFlightSeat.getFlight());
        }
        if(flightSeat.getSeat() == null) {
            flightSeat.setSeat(targetFlightSeat.getSeat());
        }
        return flightSeatRepository.save(flightSeat);
    }

   @Override
    public int getNumberOfFreeSeatOnFlight(Flight flight) {
        return flight.getAircraft().getSeatSet().size() - flightSeatRepository.findFlightSeatByFlight(flight).size();
    }

    @Override
    public Set<Seat> getSetOfFeeSeatOnFlightByFlightId(Long id) {
        var targetFlight = flightRepository.getById(id);
        var setOfSeat = targetFlight.getAircraft().getSeatSet();
        var setOfReservedSeat = flightSeatRepository.findFlightSeatByFlight(targetFlight)
                .stream().map(FlightSeat::getSeat)
                .collect(Collectors.toSet());
        for(Seat s : setOfReservedSeat) {
            setOfSeat.remove(s);
        }
        return setOfSeat;
    }

    @Override
    public void deleteById(Long id) {
        flightSeatRepository.deleteById(id);
    }

    @Override
    public Set<FlightSeat> findNotSoldById(Long id) {
        return flightSeatRepository.findAllFlightsSeatByFlightIdAndIsSoldFalse(id);
    }
}