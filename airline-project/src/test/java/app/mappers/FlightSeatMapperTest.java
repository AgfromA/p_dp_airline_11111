package app.mappers;

import app.dto.FlightSeatDto;
import app.dto.SeatDto;
import app.entities.*;
import app.services.interfaces.FlightService;
import app.services.interfaces.SeatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
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

        FlightSeatDto result = SUT.convertToFlightSeatDtoEntity(flightSeat, flightServiceMock);

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
        SeatDto seatDTO = new SeatDto();
        seatDTO.setId(42L);
        seatDTO.setSeatNumber("42L");

        FlightSeatDto flightSeatDTO = new FlightSeatDto();
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

    @Test
    @DisplayName("Должен корректно конвертировать  коллекцию entity в DTO")
    public void shouldConvertFlightSeatListToFlightSeatDTOList() {
        List<FlightSeat> flightSeatList = new ArrayList<>();
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

        flightSeatList.add(flightSeat);

        List<FlightSeatDto> flightSeatDtoList = SUT.convertToFlightSeatDtoList(flightSeatList, flightServiceMock);
        Assertions.assertEquals(flightSeatList.size(), flightSeatDtoList.size());
        Assertions.assertEquals(flightSeatList.get(0).getId(), flightSeatDtoList.get(0).getId());
        Assertions.assertEquals(flightSeatList.get(0).getFare(), flightSeatDtoList.get(0).getFare());
        Assertions.assertEquals(flightSeatList.get(0).getIsBooked(), flightSeatDtoList.get(0).getIsBooked());
        Assertions.assertEquals(flightSeatList.get(0).getIsRegistered(), flightSeatDtoList.get(0).getIsRegistered());
        Assertions.assertEquals(flightSeatList.get(0).getIsSold(), flightSeatDtoList.get(0).getIsSold());
        Assertions.assertEquals(flightSeatList.get(0).getFlight().getId(), flightSeatDtoList.get(0).getFlightId());
        Assertions.assertEquals(flightSeatList.get(0).getSeat().getSeatNumber(), flightSeatDtoList.get(0).getSeat().getSeatNumber());
    }

    @Test
    @DisplayName("Должен корректно конвертировать  коллекцию DTO в entity")
    public void shouldConvertFlightSeatDTOListToFlightSeatList() {
        List<FlightSeatDto> flightSeatDtoList = new ArrayList<>();
        Flight flight = new Flight();
        flight.setId(4001L);
        when(flightServiceMock.getFlightById(4001L)).thenReturn(Optional.of(flight));

        Seat seat = new Seat();
        seat.setId(42);
        seat.setSeatNumber("42L");

        when(seatService.getSeatById(42L)).thenReturn(seat);
        SeatDto seatDTO = new SeatDto();
        seatDTO.setId(42L);
        seatDTO.setSeatNumber("42L");

        FlightSeatDto flightSeatDTO = new FlightSeatDto();
        flightSeatDTO.setId(1L);
        flightSeatDTO.setFare(100500);
        flightSeatDTO.setIsBooked(false);
        flightSeatDTO.setIsRegistered(true);
        flightSeatDTO.setIsSold(true);
        flightSeatDTO.setFlightId(4001L);
        flightSeatDTO.setSeat(seatDTO);

        when(seatService.getSeatById(42L)).thenReturn(seat);
        flightSeatDtoList.add(flightSeatDTO);
        List<FlightSeat> flightSeatList = SUT.convertToFlightSeatEntityList(flightSeatDtoList, flightServiceMock, seatService);

        Assertions.assertEquals(flightSeatDtoList.get(0).getId(), flightSeatList.get(0).getId());
        Assertions.assertEquals(flightSeatDtoList.get(0).getFare(), flightSeatList.get(0).getFare());
        Assertions.assertEquals(flightSeatDtoList.get(0).getIsBooked(), flightSeatList.get(0).getIsBooked());
        Assertions.assertEquals(flightSeatDtoList.get(0).getIsRegistered(), flightSeatList.get(0).getIsRegistered());
        Assertions.assertEquals(flightSeatDtoList.get(0).getIsSold(), flightSeatList.get(0).getIsSold());
        Assertions.assertEquals(flightSeatDtoList.get(0).getFlightId(), flightSeatList.get(0).getFlight().getId());
        Assertions.assertEquals(flightSeatDtoList.get(0).getSeat().getSeatNumber(), flightSeatList.get(0).getSeat().getSeatNumber());
    }
}
