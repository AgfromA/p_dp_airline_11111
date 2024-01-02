package app.services;

import app.clients.FlightGeneratorClient;
import app.clients.FlightSeatGeneratorClient;
import app.dto.FlightDTO;
import app.dto.FlightSeatDTO;
import app.enums.Airport;
import app.enums.FlightStatus;
import app.services.interfaces.FlightServiceGenerator;
import app.util.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightServiceGeneratorImpl implements FlightServiceGenerator {
    private final RandomGenerator randomGenerator;
    private List<FlightSeatDTO> flightSeatDTOList;
    private final FlightGeneratorClient generatorClient;
    private final FlightSeatGeneratorClient flightSeatGeneratorClient;
    private List<Airport> airports;

    @PostConstruct
    public void init() {
        airports = List.of(Airport.values());
        flightSeatDTOList=flightSeatGeneratorClient.getAllListFlightSeatDTO().getBody();
    }


    @Override
    public FlightDTO createRandomFlightDTO() {
        FlightDTO flightDTO = new FlightDTO();

        flightDTO.setId(0L);                                  //?


        flightDTO.setSeats(randomGenerator.getRandomElements(flightSeatDTOList));

        //flightDTO.setAirportFrom(randomGenerator.randomEnum(Airport.class)); // не работает
        //flightDTO.setAirportTo(randomGenerator.randomEnum(Airport.class)); // не работает

         flightDTO.setAirportFrom(airports.get(2)); // не работает
        flightDTO.setAirportTo(airports.get(5)); // не работает

       //flightDTO.setAirportFrom(Airport.AAQ);
       //flightDTO.setAirportTo(Airport.AAQ); //не работает когда другой airport

        flightDTO.setCode(flightDTO.getAirportFrom().getAirportInternalCode() + flightDTO.getAirportTo().getAirportInternalCode()); //должен содержать код от код ту


        flightDTO.setDepartureDateTime(LocalDateTime.now().withNano(0)); //откорректировать работает
        flightDTO.setArrivalDateTime(randomGenerator.randomLocalDateTime()); //работат

        //flightDTO.setAircraftId(randomGenerator.getRandomId());   не работает
        flightDTO.setAircraftId(5L); //заглушка

        flightDTO.setFlightStatus(randomGenerator.randomEnum(FlightStatus.class)); // работает

        return flightDTO;
    }

    @Override
    public List<FlightDTO> generateRandomFlightDTO(Integer amt) {
        List<FlightDTO> result = new ArrayList<>();
        for (int i = 0; i < amt; i++) {
            FlightDTO flightDTO = createRandomFlightDTO();
            generatorClient.createFlight(flightDTO);
            result.add(flightDTO);
        }
        /*return Stream.generate(this::createRandomFlightDTO)
                .limit(amt)
                .parallel()
                .peek(generatorClient::createFlight)
                .collect(Collectors.toList());*/
        return result;
    }
}
