package app.dto;

import app.enums.CategoryType;
import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class FlightSeatDto {

    @ReadOnlyProperty
    private Long id;

    @PositiveOrZero(message = "Fare must be positive")
    private Integer fare;

    @NotNull(message = "isRegistered shouldn't be null")
    private Boolean isRegistered;

    @NotNull(message = "isSold shouldn't be null")
    private Boolean isSold;

    private Boolean isBooked;

    @NotNull(message = "flightId shouldn't be null")
    private Long flightId;

    @NotNull(message = "seatNumber shouldn't be null")
    private SeatDto seat;

//    @NotNull(message = "category cannot be null")
//    private CategoryType category;

//    public CategoryType getCategory() {
//        return category;
//    }

//    public void setCategory(CategoryType category) {
//        this.category = category;
//    }
}