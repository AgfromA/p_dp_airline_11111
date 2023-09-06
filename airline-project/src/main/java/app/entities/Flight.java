package app.entities;

import app.enums.FlightStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "flights",
    indexes = {
        @Index(columnList = "departure_date", name = "departure_index")
    })
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"code"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_flights")
    @SequenceGenerator(name = "seq_flights", initialValue = 1000, allocationSize = 1)
    private Long id;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference
    private List<FlightSeat> seats;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference
    private List<Ticket> ticket;

    @Column(name = "code")
    private String code;

    @ManyToOne
    private Destination from;

    @ManyToOne
    private Destination to;

    @Column(name = "departure_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime departureDateTime;

    @Column(name = "arrival_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime arrivalDateTime;

    @ManyToOne
    @JoinColumn(name = "aircraft_id")
    private Aircraft aircraft;

    @Enumerated(EnumType.STRING)
    @Column(name = "flight_status")
    private FlightStatus flightStatus;
}