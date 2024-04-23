package app.dto;

import app.enums.BookingStatus;
import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingUpdateDto {
    @ReadOnlyProperty
    private Long id;

    @Min(1)
    private Long passengerId;

    @ReadOnlyProperty
    private LocalDateTime bookingDate;

    @ReadOnlyProperty
    private BookingStatus bookingStatus;

    @Min(1)
    private Long flightSeatId;

    @Min(5)
    private Long flightId;
}
