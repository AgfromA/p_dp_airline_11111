package app.dto;

import app.enums.BookingStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class BookingDto {

    @ReadOnlyProperty
    private Long id;

    @NotNull
    @Min(1)
    private Long passengerId;

    @ReadOnlyProperty
    private LocalDateTime bookingDate;

    @ReadOnlyProperty
    private BookingStatus bookingStatus;

    @NotNull
    @Min(1)
    private Long flightSeatId;

    @NotNull
    @Min(5)
    private Long flightId;
}