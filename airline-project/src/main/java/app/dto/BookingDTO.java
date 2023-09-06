package app.dto;

import app.enums.BookingStatusType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class BookingDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String bookingNumber;

    @NotNull
    private LocalDateTime bookingDate;

    @NotNull
    private Long passengerId;

    @JsonIgnore
    private LocalDateTime createTime;

    @JsonIgnore
    private BookingStatusType bookingStatusType;

    @NotNull
    private Long flightSeatId;
}
