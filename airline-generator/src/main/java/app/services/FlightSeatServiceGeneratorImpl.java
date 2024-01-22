package app.services;

import app.clients.FlightSeatGeneratorClient;
import app.dto.FlightSeatDto;
import app.dto.SeatDto;
import app.enums.CategoryType;
import app.services.interfaces.FlightSeatServiceGenerator;
import app.util.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightSeatServiceGeneratorImpl implements FlightSeatServiceGenerator {
    private final RandomGenerator randomGenerator;
    private final FlightSeatGeneratorClient generatorClient;
    private List<SeatDto> listSeatDTO;

    @PostConstruct
    public void init() {
        listSeatDTO = generatorClient.getAllFlightSeats(null, null,
                        null, false, false).getBody()
                .stream()
                .map(FlightSeatDto::getSeat)
                .collect(Collectors.toList());
    }


    @Override
    public FlightSeatDto createRandomFlightSeatDTO() {
        FlightSeatDto flightSeatDto = new FlightSeatDto();
        flightSeatDto.setId(1L);// должен игнорироваться при сохранении
        flightSeatDto.setFare(randomGenerator.random.nextInt(10000) + 1000);
        flightSeatDto.setIsRegistered(randomGenerator.random.nextBoolean());
        flightSeatDto.setIsSold(randomGenerator.random.nextBoolean());
        flightSeatDto.setIsBooked(randomGenerator.random.nextBoolean());
        flightSeatDto.setFlightId(randomGenerator.getRandomBoundId(302)); // т.к. всего 302 полётов в базе
        flightSeatDto.setSeat(randomGenerator.getRandomElementOfList(listSeatDTO));
        flightSeatDto.setCategory(randomGenerator.randomEnum(CategoryType.class));
        return flightSeatDto;
    }

    @Override
    public List<FlightSeatDto> generateRandomFlightSeatDTO(Integer amt) {
        List<FlightSeatDto> result = new ArrayList<>();
        if (amt < 1) {
            return result;
        }
        for (int i = 0; i < amt; i++) {
            FlightSeatDto flightSeatDto = createRandomFlightSeatDTO();
            FlightSeatDto savedFlightSeatDto = generatorClient.createFlightSeat(flightSeatDto).getBody();
            result.add(savedFlightSeatDto);
        }
        return result;
    }
}
