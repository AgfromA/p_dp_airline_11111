package app.dto;

import app.entities.FlightSeat;
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
    private Long seatNumber;

    public FlightSeatDTO(FlightSeat entity){
        this.id = entity.getId();
        this.fare = entity.getFare();
        this.isRegistered = entity.getIsRegistered();
        this.isSold = entity.getIsSold();
        this.isBooked = entity.getIsBooked();
        this.flightId = entity.getFlight().getId();
        this.seatNumber = entity.getSeat().getId();
    }

}
