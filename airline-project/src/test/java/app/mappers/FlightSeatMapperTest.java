package app.mappers;

import app.dto.FlightSeatDTO;
import app.dto.SeatDTO;
import app.entities.*;
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
        Aircraft aircraft = new Aircraft();
        aircraft.setId(55L);
        Flight flight = new Flight();
        flight.setId(4001L);
        when(flightServiceMock.getFlightById(4001L)).thenReturn(Optional.of(flight));

        Seat seat = new Seat();
        Category category = new Category();
        category.setCategoryType(BUSINESS);
        seat.setId(42);
        seat.setSeatNumber("42A");
        seat.setCategory(category);
        seat.setAircraft(aircraft);
        when(seatService.getSeatById(42)).thenReturn(seat);

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(1L);
        flightSeat.setFare(100500);
        flightSeat.setIsBooked(false);
        flightSeat.setIsRegistered(true);
        flightSeat.setIsSold(true);
        flightSeat.setFlight(flight);
        flightSeat.setSeat(seat);

        FlightSeatDTO result = SUT.convertToFlightSeatDTOEntity(flightSeat, flightServiceMock);

        Assertions.assertEquals(flightSeat.getId(), result.getId());
        Assertions.assertEquals(flightSeat.getFare(), result.getFare());
        Assertions.assertEquals(flightSeat.getIsBooked(), result.getIsBooked());
        Assertions.assertEquals(flightSeat.getIsRegistered(), result.getIsRegistered());
        Assertions.assertEquals(flightSeat.getIsSold(), result.getIsSold());
        Assertions.assertEquals(flightSeat.getFlight().getId(), result.getFlightId());
        Assertions.assertEquals(flightSeat.getSeat().getSeatNumber(), result.getSeat().getSeatNumber());
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
        SeatDTO seatDTO = new SeatDTO();
        seatDTO.setId(42L);
        seatDTO.setSeatNumber("42L");

        FlightSeatDTO flightSeatDTO = new FlightSeatDTO();
        flightSeatDTO.setId(1L);
        flightSeatDTO.setFare(100500);
        flightSeatDTO.setIsBooked(false);
        flightSeatDTO.setIsRegistered(true);
        flightSeatDTO.setIsSold(true);
        flightSeatDTO.setFlightId(4001L);
        flightSeatDTO.setSeat(seatDTO);

        when(seatService.getSeatById(42L)).thenReturn(seat);
        FlightSeat result = SUT.convertToFlightSeatEntity(flightSeatDTO, flightServiceMock, seatService);

        Assertions.assertEquals(flightSeatDTO.getId(), result.getId());
        Assertions.assertEquals(flightSeatDTO.getFare(), result.getFare());
        Assertions.assertEquals(flightSeatDTO.getIsBooked(), result.getIsBooked());
        Assertions.assertEquals(flightSeatDTO.getIsRegistered(), result.getIsRegistered());
        Assertions.assertEquals(flightSeatDTO.getIsSold(), result.getIsSold());
        Assertions.assertEquals(flightSeatDTO.getFlightId(), result.getFlight().getId());
        Assertions.assertEquals(flightSeatDTO.getSeat().getSeatNumber(), result.getSeat().getSeatNumber());
    }
}
