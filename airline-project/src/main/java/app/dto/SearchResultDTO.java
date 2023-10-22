package app.dto;

import app.entities.search.SearchResult;
import app.mappers.FlightMapper;
import app.repositories.SearchResultProjection;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class SearchResultDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private List<FlightDTO> departFlight;

    private List<FlightDTO> returnFlight;

    private Long searchId;

    private FlightMapper flightMapper;

    public SearchResultDTO(SearchResult searchResult) {
        this.id = searchResult.getId();
        this.departFlight = searchResult.getDepartFlight().stream().map(flightMapper::flightToFlightDTO).collect(Collectors.toList());
        if (searchResult.getReturnFlight() != null) {
            this.returnFlight = searchResult.getReturnFlight().stream().map(flightMapper::flightToFlightDTO).collect(Collectors.toList());
        }
        this.searchId = searchResult.getSearch().getId();
    }

    public SearchResultDTO(SearchResultProjection searchResultProjection) {
        this.id = searchResultProjection.getId();
        this.departFlight = searchResultProjection.getDepartFlight().stream().map(flightMapper::flightToFlightDTO).collect(Collectors.toList());
        if (searchResultProjection.getReturnFlight() != null) {
            this.returnFlight = searchResultProjection.getReturnFlight().stream().map(flightMapper::flightToFlightDTO).collect(Collectors.toList());
        }
    }
}
