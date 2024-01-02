package app.services;

import app.clients.FlightSeatGeneratorClient;
import app.dto.FlightSeatDTO;
import app.dto.SeatDTO;
import app.enums.CategoryType;
import app.services.interfaces.FlightSeatServiceGenerator;
import app.util.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FlightSeatServiceGeneratorImpl implements FlightSeatServiceGenerator {
    private final RandomGenerator randomGenerator;
    private final FlightSeatGeneratorClient generatorClient;
    private List<SeatDTO> listSeatDTO;
    @PostConstruct
    public void init() {
        listSeatDTO = generatorClient.getAllSeatDTO().getBody();
    }



    @Override
    public FlightSeatDTO createRandomFlightSeatDTO() {
        FlightSeatDTO flightSeatDTO = new FlightSeatDTO(); // не отображается id в свагере

        flightSeatDTO.setFare(randomGenerator.random.nextInt(10000) + 1000); //работает

        flightSeatDTO.setIsRegistered(randomGenerator.random.nextBoolean()); // работает

        flightSeatDTO.setIsSold(randomGenerator.random.nextBoolean()); // работает

        flightSeatDTO.setIsBooked(randomGenerator.random.nextBoolean()); // работает

        flightSeatDTO.setFlightId(5L); //заглушка // работает
        //flightSeatDTO.setFlightId(randomGenerator.getRandomId());
        //flightSeatDTO.setFlightId(randomGenerator.random.nextLong()); //откорректировать


        flightSeatDTO.setSeat(randomGenerator.getRandomElementOfList(listSeatDTO));// работает


        flightSeatDTO.setCategory(randomGenerator.randomEnum(CategoryType.class)); // работает

        return flightSeatDTO;
    }

    @Override
    public List<FlightSeatDTO> generateRandomFlightSeatDTO(Integer amt) {
        List<FlightSeatDTO> result = new ArrayList<>();
        for (int i = 0; i < amt; i++) {
            FlightSeatDTO flightSeatDTO = createRandomFlightSeatDTO();
            generatorClient.createFlightSeatDTO(flightSeatDTO);
            result.add(flightSeatDTO);

        }
       /* return Stream.generate(this::createRandomFlightSeatDTO)
                .limit(amt)
                .parallel()
                .peek(generatorClient::createFlightSeatDTO)
                .collect(Collectors.toList());*/
        return result;
    }

}
