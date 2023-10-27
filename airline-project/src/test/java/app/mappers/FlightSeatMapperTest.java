package app.mappers;

import app.dto.FlightSeatDTO;
import app.entities.Category;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.entities.Seat;
import app.services.interfaces.FlightService;
import app.services.interfaces.SeatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static app.enums.CategoryType.BUSINESS;
import static org.mockito.Mockito.when;


class FlightSeatMapperTest {

    private final FlightSeatMapper SUT = Mappers.getMapper(FlightSeatMapper.class);
    @Mock
    private FlightService flightServiceMock = Mockito.mock(FlightService.class);
    @Mock
    private SeatService seatService = Mockito.mock(SeatService.class);

    @Test
    @DisplayName("Должен корректно конвертировать сущность в ДТО")
    public void shouldConvertFlightSeatToFlightSeatDTO() {

        Flight flight = new Flight();
        flight.setId(4001L);
        when(flightServiceMock.getFlightById(4001L)).thenReturn(Optional.of(flight));

        Seat seat = new Seat();
        Category category = new Category();
        category.setCategoryType(BUSINESS);
        seat.setId(42);
        seat.setSeatNumber("42A");
        seat.setCategory(category);
        when(seatService.getSeatById(42)).thenReturn(seat);

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(1L);
        flightSeat.setFare(100500);
        flightSeat.setIsBooked(false);
        flightSeat.setIsRegistered(true);
        flightSeat.setIsSold(true);
        flightSeat.setFlight(flight);
        flightSeat.setSeat(seat);

        FlightSeatDTO result = SUT.convertToFlightSeatDTOEntity(flightSeat, flightServiceMock, seatService);

        Assertions.assertEquals(flightSeat.getId(), result.getId());
        Assertions.assertEquals(flightSeat.getFare(), result.getFare());
        Assertions.assertEquals(flightSeat.getIsBooked(), result.getIsBooked());
        Assertions.assertEquals(flightSeat.getIsRegistered(), result.getIsRegistered());
        Assertions.assertEquals(flightSeat.getIsSold(), result.getIsSold());
        Assertions.assertEquals(flightSeat.getFlight().getId(), result.getFlightId());
        Assertions.assertEquals(flightSeat.getSeat().getSeatNumber(), result.getSeatNumber());
    }

    @Test
    @DisplayName("Должен корректно конвертировать ДТО в сущность")
    public void shouldConvertFlightSeatDTOToFlightSeat() {

        Flight flight = new Flight();
        flight.setId(4001L);
        when(flightServiceMock.getFlightById(4001L)).thenReturn(Optional.of(flight));

        Seat seat = new Seat();
        seat.setId(42);
        seat.setSeatNumber("42L");
        when(seatService.getSeatById(42L)).thenReturn(seat);

        FlightSeatDTO flightSeatDTO = new FlightSeatDTO();
        flightSeatDTO.setId(1L);
        flightSeatDTO.setFare(100500);
        flightSeatDTO.setIsBooked(false);
        flightSeatDTO.setIsRegistered(true);
        flightSeatDTO.setIsSold(true);
        flightSeatDTO.setFlightId(4001L);
        flightSeatDTO.setSeatId(42L);
        flightSeatDTO.setSeatNumber("42L");

        when(seatService.getSeatById(42L)).thenReturn(seat);
        FlightSeat result = SUT.convertToFlightSeatEntity(flightSeatDTO, flightServiceMock, seatService);

        Assertions.assertEquals(flightSeatDTO.getId(), result.getId());
        Assertions.assertEquals(flightSeatDTO.getFare(), result.getFare());
        Assertions.assertEquals(flightSeatDTO.getIsBooked(), result.getIsBooked());
        Assertions.assertEquals(flightSeatDTO.getIsRegistered(), result.getIsRegistered());
        Assertions.assertEquals(flightSeatDTO.getIsSold(), result.getIsSold());
        Assertions.assertEquals(flightSeatDTO.getFlightId(), result.getFlight().getId());
        Assertions.assertEquals(flightSeatDTO.getSeatNumber(), result.getSeat().getSeatNumber());
    }
}
