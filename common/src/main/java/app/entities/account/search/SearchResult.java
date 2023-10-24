package app.entities.account.search;

import app.dto.FlightDTO;
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
