package app.dto.search;

import app.dto.FlightDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Найденные рейсы.
 */
@Getter
@Setter
@ToString
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SearchResult {

    @NotNull
    @ToString.Exclude
    private List<FlightDTO> departFlights;


    @ToString.Exclude
    private List<FlightDTO> returnFlights;

//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Search search;
}
