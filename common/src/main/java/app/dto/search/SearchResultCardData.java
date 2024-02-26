package app.dto.search;

import app.enums.Airport;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultCardData {

    private Airport airportFrom;

    private Airport airportTo;

    private String cityFrom;

    private String cityTo;

    private LocalDateTime departureDateTime;

    private LocalDateTime arrivalDateTime;

    private String flightTime;

    private Long flightSeatId;
}