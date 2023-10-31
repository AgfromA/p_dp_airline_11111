package app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Физическое сиденье на самолете.
 */
@Entity
@Table(name = "seats")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"seatNumber", "aircraft"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_seat")
    @SequenceGenerator(name = "seq_seat", initialValue = 1000, allocationSize = 1)
    private long id;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "is_near_emergency_exit")
    private Boolean isNearEmergencyExit;

    @Column(name = "is_locked_back")
    private Boolean isLockedBack;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @NotNull(message = "Field aircraft cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_id")
    @JsonBackReference
    private Aircraft aircraft;
}