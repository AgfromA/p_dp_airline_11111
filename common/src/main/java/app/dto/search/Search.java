package app.dto.search;

import app.enums.Airport;
import app.enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Поиск рейсов по заданным параметрам.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Search {

    @NotNull(message = "destination cannot be null")
    private Airport from;

    @NotNull(message = "destination cannot be null")
    private Airport to;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;

    @Positive
    private Integer numberOfPassengers;

    @NotNull(message = "category cannot be null")
    private CategoryType categoryOfSeats;
}