package app.mappers;

import app.dto.SeatDto;
import app.entities.Aircraft;
import app.entities.Category;
import app.entities.Seat;
import app.enums.CategoryType;
import app.services.AircraftService;
import app.services.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
@SpringBootTest
public class SeatMapperTest {

    @Autowired
    private final SeatMapper seatMapper = Mappers.getMapper(SeatMapper.class);
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private AircraftService aircraftService;

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
        when(aircraftService.getAircraft(1002L)).thenReturn(aircraft);

        Seat seat = new Seat();
        seat.setId(1003L);
        seat.setSeatNumber("400A");
        seat.setIsNearEmergencyExit(true);
        seat.setIsLockedBack(true);
        seat.setCategory(category);
        seat.setAircraft(aircraft);

        SeatDto result = seatMapper.toDto(seat);
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
        when(aircraftService.getAircraft(1002L)).thenReturn(aircraft);

        SeatDto seatDTO = new SeatDto();
        seatDTO.setId(1003L);
        seatDTO.setSeatNumber("400A");
        seatDTO.setIsNearEmergencyExit(true);
        seatDTO.setIsLockedBack(true);
        seatDTO.setCategory(category.getCategoryType());
        seatDTO.setAircraftId(aircraft.getId());

        Seat result = seatMapper.toEntity(seatDTO);
        Assertions.assertEquals(seatDTO.getId(), result.getId());
        Assertions.assertEquals(seatDTO.getSeatNumber(), result.getSeatNumber());
        Assertions.assertEquals(seatDTO.getIsNearEmergencyExit(), result.getIsNearEmergencyExit());
        Assertions.assertEquals(seatDTO.getIsLockedBack(), result.getIsLockedBack());
        Assertions.assertEquals(seatDTO.getCategory(), result.getCategory().getCategoryType());
        Assertions.assertEquals(seatDTO.getAircraftId(), result.getAircraft().getId());
    }

    @Test
    @DisplayName("Должен корректно конвертировать коллекцию сущностей в ДТО")
    public void shouldConvertSeatListToSeatDTOList() {
        List<Seat> seatList = new ArrayList<>();
        Category category = new Category();
        category.setId(1001L);
        category.setCategoryType(CategoryType.BUSINESS);
        when(categoryService.getCategoryByType(CategoryType.BUSINESS)).thenReturn(category);

        Aircraft aircraft = new Aircraft();
        aircraft.setId(1002L);
        aircraft.setAircraftNumber("77777");
        when(aircraftService.getAircraft(1002L)).thenReturn(aircraft);

        Seat seat = new Seat();
        seat.setId(1003L);
        seat.setSeatNumber("400A");
        seat.setIsNearEmergencyExit(true);
        seat.setIsLockedBack(true);
        seat.setCategory(category);
        seat.setAircraft(aircraft);

        seatList.add(seat);

        List<SeatDto> seatDtoList = seatMapper.toDtoList(seatList);
        Assertions.assertEquals(seatList.size(), seatDtoList.size());
        Assertions.assertEquals(seatList.get(0).getId(), seatDtoList.get(0).getId());
        Assertions.assertEquals(seatList.get(0).getSeatNumber(), seatDtoList.get(0).getSeatNumber());
        Assertions.assertEquals(seatList.get(0).getIsNearEmergencyExit(), seatDtoList.get(0).getIsNearEmergencyExit());
        Assertions.assertEquals(seatList.get(0).getIsLockedBack(), seatDtoList.get(0).getIsLockedBack());
        Assertions.assertEquals(seatList.get(0).getCategory().getCategoryType(), seatDtoList.get(0).getCategory());
        Assertions.assertEquals(seatList.get(0).getAircraft().getId(), seatDtoList.get(0).getAircraftId());
    }

    @Test
    @DisplayName("Должен корректно конвертировать коллекцию ДТО в сущности")
    public void shouldConvertSeatDTOListToSeatList() {
        List<SeatDto> seatDtoList = new ArrayList<>();
        Category category = new Category();
        category.setId(1001L);
        category.setCategoryType(CategoryType.BUSINESS);
        when(categoryService.getCategoryByType(CategoryType.BUSINESS)).thenReturn(category);

        Aircraft aircraft = new Aircraft();
        aircraft.setId(1002L);
        aircraft.setAircraftNumber("77777");
        when(aircraftService.getAircraft(1002L)).thenReturn(aircraft);

        SeatDto seatDTO = new SeatDto();
        seatDTO.setId(1003L);
        seatDTO.setSeatNumber("400A");
        seatDTO.setIsNearEmergencyExit(true);
        seatDTO.setIsLockedBack(true);
        seatDTO.setCategory(category.getCategoryType());
        seatDTO.setAircraftId(aircraft.getId());
        seatDtoList.add(seatDTO);

        List<Seat> seatList = seatMapper.toEntityList(seatDtoList);
        Assertions.assertEquals(seatDtoList.size(), seatList.size());
        Assertions.assertEquals(seatDtoList.get(0).getId(), seatList.get(0).getId());
        Assertions.assertEquals(seatDtoList.get(0).getSeatNumber(), seatList.get(0).getSeatNumber());
        Assertions.assertEquals(seatDtoList.get(0).getIsNearEmergencyExit(), seatList.get(0).getIsNearEmergencyExit());
        Assertions.assertEquals(seatDtoList.get(0).getIsLockedBack(), seatList.get(0).getIsLockedBack());
        Assertions.assertEquals(seatDtoList.get(0).getCategory(), seatList.get(0).getCategory().getCategoryType());
        Assertions.assertEquals(seatDtoList.get(0).getAircraftId(), seatList.get(0).getAircraft().getId());
    }
}
