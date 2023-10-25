package app.dto;

import app.enums.CategoryType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class FlightSeatDTO {
    private Long id;

    @PositiveOrZero(message = "Fare must be positive")
    private Integer fare;

    @NotNull
    private Boolean isRegistered;

    @NotNull(message = "isSold shouldn't be null")
    private Boolean isSold;

    private Boolean isBooked;

    @NotNull(message = "flightId shouldn't be null")
    private Long flightId;

    @NotNull(message = "seatNumber shouldn't be null")
    private String seatNumber;

    @NotNull(message = "category cannot be null")
    private CategoryType category;
}
