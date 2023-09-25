package app.dto;

import app.entities.Category;
import app.entities.Seat;
import app.enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class SeatDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @NotBlank(message = "Field seat number cannot be null")
    @Size(min = 2, max = 5, message = "Seat number must be between 2 and 5 characters")
    private String seatNumber;

    @NotNull(message = "Field isNearEmergencyExit cannot be null")
    private Boolean isNearEmergencyExit;

    @NotNull(message = "Field isLockedBack cannot be null")
    private Boolean isLockedBack;

    private CategoryType category;

//    @NotNull(message = "Field aircraft cannot be null")         // эта проверка осталась в сущности, т.к. при сохранении многих Seat по aircraftID это поле сеттится отдельно.
    private long aircraftId;

    public SeatDTO(Seat seat){
        this.id = seat.getId();
        this.seatNumber = seat.getSeatNumber();
        this.isNearEmergencyExit = seat.getIsNearEmergencyExit();
        this.isLockedBack = seat.getIsLockedBack();
        this.category = seat.getCategory().getCategoryType();
        this.aircraftId=seat.getAircraft().getId();
    }
}