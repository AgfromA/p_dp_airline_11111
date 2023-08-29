package app.dto;

import app.entities.Flight;
import app.enums.Airport;
import app.enums.FlightStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class FlightDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank(message = "Code cannot be empty")
    @Size(min = 2, max = 15, message = "Length of Flight code should be between 2 and 15 characters")
    private String code;
    private Airport airportFrom;
    private Airport airportTo;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private Long aircraftId;
    private FlightStatus flightStatus;

    public FlightDTO(Flight flight) {
        this.id = flight.getId();
        this.code = flight.getCode();
        this.airportFrom = flight.getFrom().getAirportCode();
        this.airportTo = flight.getTo().getAirportCode();
        this.departureDateTime = flight.getDepartureDateTime();
        this.arrivalDateTime = flight.getArrivalDateTime();
        this.aircraftId = flight.getAircraft().getId();
        this.flightStatus = flight.getFlightStatus();
    }
}
