package app.mappers;

import app.dto.SeatDTO;
import app.entities.Aircraft;
import app.entities.Category;
import app.entities.Seat;
import app.enums.CategoryType;
import app.services.interfaces.AircraftService;
import app.services.interfaces.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class SeatMapperTest {

    private final SeatMapper seatMapper = Mappers.getMapper(SeatMapper.class);
    @Mock
    private CategoryService categoryService = Mockito.mock(CategoryService.class);
    @Mock
    private AircraftService aircraftService = Mockito.mock(AircraftService.class);

    @Test
    @DisplayName("Должен корректно конвертировать сущность в ДТО")
    public void shouldConvertSeatToSeatDTO() {
        Category category = new Category();
        category.setId(1001L);
        category.setCategoryType(CategoryType.BUSINESS);
        when(categoryService.getCategoryByType(CategoryType.BUSINESS)).thenReturn(category);

        Aircraft aircraft = new Aircraft();
        aircraft.setId(1002L);
        aircraft.setAircraftNumber("77777");
        when(aircraftService.getAircraftById(1002L)).thenReturn(aircraft);

        Seat seat = new Seat();
        seat.setId(1003L);
        seat.setSeatNumber("400A");
        seat.setIsNearEmergencyExit(true);
        seat.setIsLockedBack(true);
        seat.setCategory(category);
        seat.setAircraft(aircraft);

        SeatDTO result = seatMapper.convertToSeatDTOEntity(seat);
        Assertions.assertEquals(seat.getId(), result.getId());
        Assertions.assertEquals(seat.getSeatNumber(), result.getSeatNumber());
        Assertions.assertEquals(seat.getIsNearEmergencyExit(), result.getIsNearEmergencyExit());
        Assertions.assertEquals(seat.getIsLockedBack(), result.getIsLockedBack());
        Assertions.assertEquals(seat.getCategory().getCategoryType(), result.getCategory());
        Assertions.assertEquals(seat.getAircraft().getId(), result.getAircraftId());

    }

    @Test
    @DisplayName("Должен корректно конвертировать ДТО в сущность")
    public void shouldConvertSeatDTOToSeat() {
        Category category = new Category();
        category.setId(1001L);
        category.setCategoryType(CategoryType.BUSINESS);
        when(categoryService.getCategoryByType(CategoryType.BUSINESS)).thenReturn(category);

        Aircraft aircraft = new Aircraft();
        aircraft.setId(1002L);
        aircraft.setAircraftNumber("77777");
        when(aircraftService.getAircraftById(1002L)).thenReturn(aircraft);

        SeatDTO seatDTO = new SeatDTO();
        seatDTO.setId(1003L);
        seatDTO.setSeatNumber("400A");
        seatDTO.setIsNearEmergencyExit(true);
        seatDTO.setIsLockedBack(true);
        seatDTO.setCategory(category.getCategoryType());
        seatDTO.setAircraftId(aircraft.getId());

        Seat result = seatMapper.convertToSeatEntity(seatDTO, categoryService, aircraftService);
        Assertions.assertEquals(seatDTO.getId(), result.getId());
        Assertions.assertEquals(seatDTO.getSeatNumber(), result.getSeatNumber());
        Assertions.assertEquals(seatDTO.getIsNearEmergencyExit(), result.getIsNearEmergencyExit());
        Assertions.assertEquals(seatDTO.getIsLockedBack(), result.getIsLockedBack());
        Assertions.assertEquals(seatDTO.getCategory(), result.getCategory().getCategoryType());
        Assertions.assertEquals(seatDTO.getAircraftId(), result.getAircraft().getId());

    }
}
