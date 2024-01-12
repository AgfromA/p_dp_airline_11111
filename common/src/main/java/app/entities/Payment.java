package app.entities;
import app.enums.Currency;
import app.enums.State;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Оплата забронированных сидений.
 */
@Entity
@Table(name = "payments")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Payment {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_payment")
    @SequenceGenerator(name = "seq_payment", initialValue = 1000, allocationSize = 1)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="payments_bookings_id",
            joinColumns=@JoinColumn(name="payments_id")
    )
    @Column(name="bookings_id")
    private List<Long> bookingsId = new ArrayList<>();

    @Column(name = "payment_state")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private State paymentState;

    @Column
    private BigDecimal price;

    @Column
    @Enumerated(EnumType.STRING)
    private Currency currency;
}