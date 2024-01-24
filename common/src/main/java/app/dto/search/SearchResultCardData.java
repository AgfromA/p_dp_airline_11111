package app.dto.search;

import app.enums.Airport;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@Builder
public class SearchResultCardData {
    private Airport airportFrom;

    private Airport airportTo;

    private LocalDateTime departureDateTime;

    private LocalDateTime arrivalDateTime;

    private String flightTime;
}
