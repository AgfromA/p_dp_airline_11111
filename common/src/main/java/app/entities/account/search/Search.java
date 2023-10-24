package app.entities.account.search;

import app.enums.Airport;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Поиск рейсов по заданным параметрам.
 */
@Setter
@Getter
@NoArgsConstructor
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Search {

    private Airport from;

    private Airport to;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate departureDate;

    @Column(name = "return_date", columnDefinition = "DATE")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate returnDate;

    @Positive
    @Column(name = "number_of_passengers", nullable = false)
    private Integer numberOfPassengers;

    public Search(Airport from, Airport to, LocalDate departureDate, LocalDate returnDate, Integer numberOfPassengers) {
        this.from = from;
        this.to = to;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.numberOfPassengers = numberOfPassengers;
    }
}
