package app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Сиденье на рейс. Создается на основе физического сиденья на самолете.
 */
@Entity
@Table(name = "flight_seats")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"flight", "seat"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FlightSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_flight_seats")
    @SequenceGenerator(name = "seq_flight_seats", initialValue = 1000, allocationSize = 1)
    private Long id;

    @Column(name = "fare")
    private Integer fare;

    @Column(name = "is_registered")
    private Boolean isRegistered;

    @Column(name = "is_sold")
    private Boolean isSold;

    @Column(name = "is_booked")
    private Boolean isBooked;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    @JsonBackReference
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

}
