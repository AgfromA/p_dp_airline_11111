package app.mappers;

import app.dto.AircraftDTO;
import app.entities.Aircraft;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

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
}