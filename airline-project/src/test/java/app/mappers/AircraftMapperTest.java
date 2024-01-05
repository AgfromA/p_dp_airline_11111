package app.mappers;

import app.dto.AircraftDTO;
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
        AircraftDTO aircraftDTO = new AircraftDTO();
        aircraftDTO.setId(1L);
        aircraftDTO.setAircraftNumber("1234");
        aircraftDTO.setModel("Boeing");
        aircraftDTO.setModelYear(2020);
        aircraftDTO.setFlightRange(10000);

        Aircraft aircraft = aircraftMapper.convertToAircraftEntity(aircraftDTO);

        assertEquals(aircraftDTO.getId(), aircraft.getId());
        assertEquals(aircraftDTO.getAircraftNumber(), aircraft.getAircraftNumber());
        assertEquals(aircraftDTO.getModel(), aircraft.getModel());
        assertEquals(aircraftDTO.getModelYear(), aircraft.getModelYear());
        assertEquals(aircraftDTO.getFlightRange(), aircraft.getFlightRange());
    }

    @Test
    public void testConvertToAircraftEntityWhenGivenNullThenReturnsNull() {
        Aircraft aircraft = aircraftMapper.convertToAircraftEntity(null);
        assertNull(aircraft);
    }

    @Test
    public void testConvertToAircraftEntityWhenGivenEmptyAircraftDTOThenReturnsEmptyAircraft() {
        AircraftDTO aircraftDTO = new AircraftDTO();
        Aircraft aircraft = aircraftMapper.convertToAircraftEntity(aircraftDTO);

        assertNull(aircraft.getId());
        assertNull(aircraft.getAircraftNumber());
        assertNull(aircraft.getModel());
        assertEquals(0, aircraft.getModelYear());
        assertEquals(0, aircraft.getFlightRange());
    }

    @Test
    public void shouldConvertToAircraftEntityList() {
        List<AircraftDTO> aircraftDTOList = new ArrayList<>();
        AircraftDTO aircraftDTOOne = new AircraftDTO();
        aircraftDTOOne.setId(1L);
        aircraftDTOOne.setAircraftNumber("1234");
        aircraftDTOOne.setModel("Boeing");
        aircraftDTOOne.setModelYear(2020);
        aircraftDTOOne.setFlightRange(10000);
        AircraftDTO aircraftDTOTwo = new AircraftDTO();
        aircraftDTOTwo.setId(2L);
        aircraftDTOTwo.setAircraftNumber("1235");
        aircraftDTOTwo.setModel("Airbus");
        aircraftDTOTwo.setModelYear(2022);
        aircraftDTOTwo.setFlightRange(20000);

        aircraftDTOList.add(aircraftDTOOne);
        aircraftDTOList.add(aircraftDTOTwo);

        List<Aircraft> aircraftList = aircraftMapper.convertToAircraftEntityList(aircraftDTOList);

        assertEquals(aircraftDTOList.size(), aircraftList.size());
        assertEquals(aircraftDTOList.get(0).getId(), aircraftList.get(0).getId());
        assertEquals(aircraftDTOList.get(0).getAircraftNumber(), aircraftList.get(0).getAircraftNumber());
        assertEquals(aircraftDTOList.get(0).getModel(), aircraftList.get(0).getModel());
        assertEquals(aircraftDTOList.get(0).getModelYear(), aircraftList.get(0).getModelYear());
        assertEquals(aircraftDTOList.get(0).getFlightRange(), aircraftList.get(0).getFlightRange());

        assertEquals(aircraftDTOList.get(1).getId(), aircraftList.get(1).getId());
        assertEquals(aircraftDTOList.get(1).getAircraftNumber(), aircraftList.get(1).getAircraftNumber());
        assertEquals(aircraftDTOList.get(1).getModel(), aircraftList.get(1).getModel());
        assertEquals(aircraftDTOList.get(1).getModelYear(), aircraftList.get(1).getModelYear());
        assertEquals(aircraftDTOList.get(1).getFlightRange(), aircraftList.get(1).getFlightRange());
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

        List<AircraftDTO> aircraftDTOList = aircraftMapper.convertToAircarftDTOList(aircraftList);

        assertEquals(aircraftList.size(), aircraftDTOList.size());
        assertEquals(aircraftList.get(0).getId(), aircraftDTOList.get(0).getId());
        assertEquals(aircraftList.get(0).getAircraftNumber(), aircraftDTOList.get(0).getAircraftNumber());
        assertEquals(aircraftList.get(0).getModel(), aircraftDTOList.get(0).getModel());
        assertEquals(aircraftList.get(0).getModelYear(), aircraftDTOList.get(0).getModelYear());
        assertEquals(aircraftList.get(0).getFlightRange(), aircraftDTOList.get(0).getFlightRange());

        assertEquals(aircraftList.get(1).getId(), aircraftDTOList.get(1).getId());
        assertEquals(aircraftList.get(1).getAircraftNumber(), aircraftDTOList.get(1).getAircraftNumber());
        assertEquals(aircraftList.get(1).getModel(), aircraftDTOList.get(1).getModel());
        assertEquals(aircraftList.get(1).getModelYear(), aircraftDTOList.get(1).getModelYear());
        assertEquals(aircraftList.get(1).getFlightRange(), aircraftDTOList.get(1).getFlightRange());
    }
}