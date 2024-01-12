package app.services;

import app.dto.SeatDto;
import app.entities.Aircraft;
import app.entities.Category;
import app.entities.Seat;
import app.enums.CategoryType;
import app.mappers.SeatMapper;
import app.repositories.FlightSeatRepository;
import app.repositories.SeatRepository;
import app.services.interfaces.AircraftService;
import app.services.interfaces.CategoryService;
import app.services.interfaces.SeatService;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SeatServiceImplTest {

    private static final Long AIRCRAFT_TEST_ID = 1L;
    private static final String AIRCRAFT_TEST_MODEL = "Boeing 737-800";

    private final SeatRepository seatRepository = mock(SeatRepository.class);
    private final CategoryService categoryService = mock(CategoryService.class);
    private final AircraftService aircraftService = mock(AircraftService.class);
    private final SeatMapper seatMapper = mock(SeatMapper.class);
    private final FlightSeatRepository flightSeatRepository = mock(FlightSeatRepository.class);

    private final SeatService seatService = new SeatServiceImpl(
            seatRepository,
            categoryService,
            aircraftService,
            flightSeatRepository
    );

    @Test
    public void generateSuccessfullyTest() {

        var economyCategory = new Category();
        economyCategory.setCategoryType(CategoryType.ECONOMY);

        var businessCategory = new Category();
        businessCategory.setCategoryType(CategoryType.BUSINESS);

        var aircraft = new Aircraft();
        aircraft.setId(AIRCRAFT_TEST_ID);
        aircraft.setModel(AIRCRAFT_TEST_MODEL);

        when(categoryService.getCategoryByType(CategoryType.ECONOMY))
                .thenReturn(economyCategory);
        when(categoryService.getCategoryByType(CategoryType.BUSINESS))
                .thenReturn(businessCategory);
        when(aircraftService.getAircraftById(AIRCRAFT_TEST_ID))
                .thenReturn(aircraft);
        when(seatRepository.findByAircraftId(eq(AIRCRAFT_TEST_ID), any()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        when(seatMapper.convertToSeatEntity(any(), categoryService, aircraftService))
                .thenAnswer(ans -> {
                    SeatDto seatDTO = ans.getArgument(0);
                    var seat = new Seat();
                    seat.setCategory(categoryService.getCategoryByType(seatDTO.getCategory()));
                    seat.setAircraft(aircraft);
                    return seat;
                });

        when(seatRepository.saveAndFlush(any()))
                .thenAnswer(s -> {
                    Seat seat = s.getArgument(0);
                    seat.setId(1L);
                    return seat;
                });

        List<SeatDto> seatDtos = seatService.generateSeatsDTOByAircraftId(AIRCRAFT_TEST_ID);

        long businessSeatsCount = seatDtos.stream()
                .map(SeatDto::getCategory)
                .filter(categoryType -> CategoryType.BUSINESS == categoryType)
                .count();

        assertFalse(seatDtos.isEmpty());
        assertEquals(168, seatDtos.size());
        assertEquals(12, businessSeatsCount);
    }

}
