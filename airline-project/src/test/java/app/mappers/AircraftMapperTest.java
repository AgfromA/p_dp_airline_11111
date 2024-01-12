package app.mappers;

import app.dto.AircraftDto;
import app.entities.Aircraft;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AircraftMapperTest {

    private AircraftMapper aircraftMapper = Mappers.getMapper(AircraftMapper.class);

    @Test
    public void testConvertToAircraftEntityWhenGivenAircraftDTOThenReturnsAircraft() {
        AircraftDto aircraftDTO = new AircraftDto();
        aircraftDTO.setId(1L);
        aircraftDTO.setAircraftNumber("1234");
        aircraftDTO.setModel("Boeing");
        aircraftDTO.setModelYear(2020);
        aircraftDTO.setFlightRange(10000);

        Aircraft aircraft = aircraftMapper.toEntity(aircraftDTO);

        assertEquals(aircraftDTO.getId(), aircraft.getId());
        assertEquals(aircraftDTO.getAircraftNumber(), aircraft.getAircraftNumber());
        assertEquals(aircraftDTO.getModel(), aircraft.getModel());
        assertEquals(aircraftDTO.getModelYear(), aircraft.getModelYear());
        assertEquals(aircraftDTO.getFlightRange(), aircraft.getFlightRange());
    }

    @Test
    public void testConvertToAircraftEntityWhenGivenNullThenReturnsNull() {
        Aircraft aircraft = aircraftMapper.toEntity(null);
        assertNull(aircraft);
    }

    @Test
    public void testConvertToAircraftEntityWhenGivenEmptyAircraftDTOThenReturnsEmptyAircraft() {
        AircraftDto aircraftDTO = new AircraftDto();
        Aircraft aircraft = aircraftMapper.toEntity(aircraftDTO);

        assertNull(aircraft.getId());
        assertNull(aircraft.getAircraftNumber());
        assertNull(aircraft.getModel());
        assertEquals(0, aircraft.getModelYear());
        assertEquals(0, aircraft.getFlightRange());
    }

    @Test
    public void shouldConvertToAircraftEntityList() {
        List<AircraftDto> aircraftDtoList = new ArrayList<>();
        AircraftDto aircraftDtoOne = new AircraftDto();
        aircraftDtoOne.setId(1L);
        aircraftDtoOne.setAircraftNumber("1234");
        aircraftDtoOne.setModel("Boeing");
        aircraftDtoOne.setModelYear(2020);
        aircraftDtoOne.setFlightRange(10000);
        AircraftDto aircraftDtoTwo = new AircraftDto();
        aircraftDtoTwo.setId(2L);
        aircraftDtoTwo.setAircraftNumber("1235");
        aircraftDtoTwo.setModel("Airbus");
        aircraftDtoTwo.setModelYear(2022);
        aircraftDtoTwo.setFlightRange(20000);

        aircraftDtoList.add(aircraftDtoOne);
        aircraftDtoList.add(aircraftDtoTwo);

        List<Aircraft> aircraftList = aircraftMapper.toEntityList(aircraftDtoList);

        assertEquals(aircraftDtoList.size(), aircraftList.size());
        assertEquals(aircraftDtoList.get(0).getId(), aircraftList.get(0).getId());
        assertEquals(aircraftDtoList.get(0).getAircraftNumber(), aircraftList.get(0).getAircraftNumber());
        assertEquals(aircraftDtoList.get(0).getModel(), aircraftList.get(0).getModel());
        assertEquals(aircraftDtoList.get(0).getModelYear(), aircraftList.get(0).getModelYear());
        assertEquals(aircraftDtoList.get(0).getFlightRange(), aircraftList.get(0).getFlightRange());

        assertEquals(aircraftDtoList.get(1).getId(), aircraftList.get(1).getId());
        assertEquals(aircraftDtoList.get(1).getAircraftNumber(), aircraftList.get(1).getAircraftNumber());
        assertEquals(aircraftDtoList.get(1).getModel(), aircraftList.get(1).getModel());
        assertEquals(aircraftDtoList.get(1).getModelYear(), aircraftList.get(1).getModelYear());
        assertEquals(aircraftDtoList.get(1).getFlightRange(), aircraftList.get(1).getFlightRange());
    }

    @Test
    public void shouldConvertToAircraftDTOList() {
        List<Aircraft> aircraftList = new ArrayList<>();
        Aircraft aircraftOne = new Aircraft();
        aircraftOne.setId(1L);
        aircraftOne.setAircraftNumber("1234");
        aircraftOne.setModel("Boeing");
        aircraftOne.setModelYear(2020);
        aircraftOne.setFlightRange(10000);

        Aircraft aircraftTwo = new Aircraft();
        aircraftTwo.setId(2L);
        aircraftTwo.setAircraftNumber("1235");
        aircraftTwo.setModel("Airbus");
        aircraftTwo.setModelYear(2022);
        aircraftTwo.setFlightRange(20000);

        aircraftList.add(aircraftOne);
        aircraftList.add(aircraftTwo);

        List<AircraftDto> aircraftDtoList = aircraftMapper.toDtoList(aircraftList);

        assertEquals(aircraftList.size(), aircraftDtoList.size());
        assertEquals(aircraftList.get(0).getId(), aircraftDtoList.get(0).getId());
        assertEquals(aircraftList.get(0).getAircraftNumber(), aircraftDtoList.get(0).getAircraftNumber());
        assertEquals(aircraftList.get(0).getModel(), aircraftDtoList.get(0).getModel());
        assertEquals(aircraftList.get(0).getModelYear(), aircraftDtoList.get(0).getModelYear());
        assertEquals(aircraftList.get(0).getFlightRange(), aircraftDtoList.get(0).getFlightRange());

        assertEquals(aircraftList.get(1).getId(), aircraftDtoList.get(1).getId());
        assertEquals(aircraftList.get(1).getAircraftNumber(), aircraftDtoList.get(1).getAircraftNumber());
        assertEquals(aircraftList.get(1).getModel(), aircraftDtoList.get(1).getModel());
        assertEquals(aircraftList.get(1).getModelYear(), aircraftDtoList.get(1).getModelYear());
        assertEquals(aircraftList.get(1).getFlightRange(), aircraftDtoList.get(1).getFlightRange());
    }
}