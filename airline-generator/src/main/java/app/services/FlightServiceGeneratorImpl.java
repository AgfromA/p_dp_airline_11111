package app.services;

import app.clients.FlightGeneratorClient;
import app.clients.FlightSeatGeneratorClient;
import app.dto.FlightDto;
import app.dto.FlightSeatDto;
import app.enums.Airport;
import app.enums.FlightStatus;
import app.services.interfaces.FlightServiceGenerator;
import app.util.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightServiceGeneratorImpl implements FlightServiceGenerator {
    private final RandomGenerator randomGenerator;
    private List<FlightSeatDto> flightSeatDTOList;
    private final FlightGeneratorClient generatorClient;
    private final FlightSeatGeneratorClient flightSeatGeneratorClient;
    private List<Airport> airports = new ArrayList<>();

    @PostConstruct
    public void init() {
        //airports = List.of(Airport.values()); // т.к. не все направления есть в базе
        airports.addAll(Arrays.asList(Airport.VKO, Airport.VOG, Airport.MQF, Airport.OMS, Airport.VOZ,
                Airport.SVX, Airport.AAQ, Airport.ARH, Airport.KGD, Airport.KRR, Airport.KLD, Airport.KLF,
                Airport.KZN, Airport.KMW, Airport.KUF, Airport.IAR, Airport.IWA, Airport.GRV, Airport.IJK,
                Airport.GOJ, Airport.LPK, Airport.NAL));
        flightSeatDTOList = flightSeatGeneratorClient.getAllFlightSeats(null, null,
                null, false, false).getBody();
    }


    @Override
    public FlightDto createRandomFlightDTO() {
        FlightDto flightDto = new FlightDto();
        flightDto.setId(10000L); // должен игнорироваться при сохранении
        flightDto.setAirportFrom(randomGenerator.getRandomElementOfList(airports));
        flightDto.setAirportTo(randomGenerator.getRandomElementOfList(airports));
        flightDto.setCode(flightDto.getAirportFrom().getAirportInternalCode() + flightDto.getAirportTo().getAirportInternalCode());
        flightDto.setSeats(randomGenerator.getRandomElements(flightSeatDTOList));
        flightDto.setDepartureDateTime(LocalDateTime.now().withNano(0));
        flightDto.setArrivalDateTime(randomGenerator.randomLocalDateTime());
        flightDto.setAircraftId(randomGenerator.getRandomBoundId(10)); // т.к. всего 10 самолетов в базе
        flightDto.setFlightStatus(randomGenerator.randomEnum(FlightStatus.class));
        return flightDto;
    }

    @Override
    public List<FlightDto> generateRandomFlightDTO(Integer amt) {
        List<FlightDto> result = new ArrayList<>();
        if (amt < 1) {
            return result;
        }
        for (int i = 0; i < amt; i++) {
            FlightDto flightDto = createRandomFlightDTO();
            FlightDto savedFlightDto = generatorClient.createFlight(flightDto).getBody();
            result.add(savedFlightDto);
        }
        return result;
    }
}
