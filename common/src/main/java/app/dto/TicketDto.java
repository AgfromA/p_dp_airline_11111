package app.dto;

import app.enums.Airport;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class TicketDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String ticketNumber;

    @NotNull(message = "Passenger ID cannot be null")
    private Long passengerId;

    @NotBlank(message = "Field should not be empty")
    @Size(min = 2, max = 128, message = "Firstname size cannot be less than 2 and more than 128 characters")
    private String firstName;

    @NotBlank(message = "Field should not be empty")
    @Size(min = 2, max = 128, message = "Lastname size cannot be less than 2 and more than 128 characters")
    private String lastName;

    @NotBlank(message = "Flight code cannot be empty")
    @Size(min = 2, max = 15, message = "Length of flight code should be between 2 and 15 characters")
    private String code;

    @NotNull(message = "Departure airport cannot be null")
    private Airport from;

    @NotNull(message = "Arrival airport cannot be null")
    private Airport to;

    @NotNull(message = "Departure date and time cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departureDateTime;

    @NotNull(message = "Arrival date and time cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivalDateTime;

    @NotNull(message = "Flight seat ID cannot be null")
    private Long flightSeatId;

    @NotBlank(message = "Field seat number cannot be null")
    @Size(min = 2, max = 5, message = "Seat number must be between 2 and 5 characters")
    private String seatNumber;

    @NotNull(message = "Booking ID cannot be null")
    private Long bookingId;
}