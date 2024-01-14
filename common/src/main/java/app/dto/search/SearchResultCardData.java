package app.dto.search;

import app.enums.Airport;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
public class SearchResultCardData {
    private Airport airportFrom;

    private Airport airportTo;

    private LocalDateTime departureDateTime;

    private LocalDateTime arrivalDateTime;

    private LocalDateTime flightTime;
}
