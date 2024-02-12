package app.entities;
import app.enums.BookingStatus;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Бронирование сидений на рейс, формируется перед оплатой.
 */
@Entity
@Table(name="booking")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "passenger", "flightSeat"})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_booking")
    @SequenceGenerator(name = "seq_booking", initialValue = 1000, allocationSize = 1)
    @Column(name = "id")
    private long id;

    @OneToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @Column(name = "booking_data_time")
    @NotNull(message = "Field bookingDate cannot be null")
    private LocalDateTime bookingDate;

    @OneToOne
    @JoinColumn(name = "flight_seat_id")
    @NotNull(message = "Field flightSeat cannot be null")
    private FlightSeat flightSeat;

    @Column(name = "booking_status")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Field bookingStatus cannot be null")
    private BookingStatus bookingStatus;
}