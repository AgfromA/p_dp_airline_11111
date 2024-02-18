package app.controllers.api;

import app.dto.search.SearchResult;
import app.enums.Airport;
import app.enums.CategoryType;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Api(tags = "Search API")
@Tag(name = "Search API", description = "API поиска посадочных мест (FlightSeat) на рейс по указанным параметрам")
public interface SearchControllerApi {

    @RequestMapping(value = "/api/search", method = RequestMethod.GET)
    @Operation(
            summary = "Поиск посадочных мест (FlightSeat) на рейс по указанным параметрам",
            description = "Для поиска должны быть созданы подходящие Flight и FlightSeat. " +
                    "Поиск возвращает только незабронированные и непроданные FlightSeat. " +
                    "Поиск билетов с пересадками (непрямые) сейчас не реализован."
    )
    ResponseEntity<SearchResult> search(
            @Parameter(description = "Аэропорт отправления") @RequestParam("airportFrom") Airport airportFrom,
            @Parameter(description = "Аэропорт прибытия") @RequestParam("airportTo") Airport airportTo,
            @Parameter(description = "Дата отправления") @RequestParam("departureDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @Parameter(description = "Дата прибытия") @RequestParam(value = "returnDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @Parameter(description = "Количество пассажиров") @RequestParam("numberOfPassengers") Integer numberOfPassengers,
            @Parameter(description = "Категория сиденья") @RequestParam("categoryOfSeats") CategoryType categoryOfSeats);
}