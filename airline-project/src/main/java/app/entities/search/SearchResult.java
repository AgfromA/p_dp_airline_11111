package app.entities.search;

import app.dto.FlightDTO;
import app.entities.Flight;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Найденные рейсы.
 */

@Getter
@Setter
@ToString
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SearchResult {

    @NotNull
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @ToString.Exclude
    private List<FlightDTO> departFlight;

    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @ToString.Exclude
    private List<FlightDTO> returnFlight;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Search search;
}
