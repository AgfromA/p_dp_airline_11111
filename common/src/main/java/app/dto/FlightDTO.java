package app.dto;

import app.dto.FlightSeatDTO;
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
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class FlightDTO {

    private Long id;
    @NotBlank(message = "Code cannot be empty")
    @Size(min = 2, max = 15, message = "Length of Flight code should be between 2 and 15 characters")
    private String code;
    private List<FlightSeatDTO> seats;
    private Airport airportFrom;
    private Airport airportTo;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private Long aircraftId;
    private FlightStatus flightStatus;

}
