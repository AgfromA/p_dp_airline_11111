package app.entities;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;

/**
 * Билет. Формируется после оплаты бронирования.
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tickets")
@EqualsAndHashCode(of = {"ticketNumber"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tickets")
    @SequenceGenerator(name = "seq_tickets", allocationSize = 1)
    private Long id;

    @Column(name = "ticket_number")
    private String ticketNumber;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @OneToOne
    @JoinColumn(name = "flight_seat_id")
    private FlightSeat flightSeat;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "booking_id")
    private Booking booking;
}