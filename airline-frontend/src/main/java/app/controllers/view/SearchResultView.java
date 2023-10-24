package app.controllers.view;

import app.clients.SearchClient;
import app.dto.FlightDTO;
import app.entities.account.search.Search;
import app.entities.account.search.SearchResult;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H3;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Route(value = "search-flights")
public class SearchResultView extends VerticalLayout {

    private final SearchClient searchClient;
    private final SearchView searchView = new SearchView();
    private final HeaderView header = new HeaderView();
    private final Grid<FlightDTO> departFlightsGrid = new Grid<>(FlightDTO.class, false);
    private final Grid<FlightDTO> returnFlightsGrid = new Grid<>(FlightDTO.class, false);
    private final List<FlightDTO> departFlights = new ArrayList<>();
    private final List<FlightDTO> returnFlights = new ArrayList<>();
    private SearchResult searchResult;
    private final H5 noDepartFlightsMessage = new H5("Flights not found");
    private final H5 noReturnFlightsMessage = new H5("Flights not found");

    public SearchResultView(SearchClient searchClient) {

        this.searchClient = searchClient;

        setFlightsGrids();
        setNoFlightsMessage();
        setSearchButton();
        setSearchViewFromOutside();

        Grid.Column<FlightDTO> departureDataTimeDepartureFlight = createDepartureDataTimeColumn(departFlightsGrid);
        Grid.Column<FlightDTO> airportFromDepartureFlight = createAirportFromColumn(departFlightsGrid);
        Grid.Column<FlightDTO> airportToDepartureFlight = createAirportToColumn(departFlightsGrid);
        Grid.Column<FlightDTO> arrivalDataTimeDepartureFlight = createArrivalDataTimeColumn(departFlightsGrid);
//        Grid.Column<FlightDTO> flightSeatsListDepartureFlight = createFlightSeatsColumn(departFlightsGrid);

        Grid.Column<FlightDTO> departureDataTimeReturnFlight = createDepartureDataTimeColumn(returnFlightsGrid);
        Grid.Column<FlightDTO> airportFromReturnFlight = createAirportFromColumn(returnFlightsGrid);
        Grid.Column<FlightDTO> airportToReturnFlight = createAirportToColumn(returnFlightsGrid);
        Grid.Column<FlightDTO> arrivalDataTimeReturnFlight = createArrivalDataTimeColumn(returnFlightsGrid);
//        Grid.Column<FlightDTO> flightSeatsListReturnFlight = createFlightSeatsColumn(returnFlightsGrid);

        departFlightsGrid.setItems(departFlights);
        returnFlightsGrid.setItems(returnFlights);

        H3 departureGridHeader = new H3("Departure flights");
        VerticalLayout departFlightsLayout = new VerticalLayout(departureGridHeader
                , noDepartFlightsMessage, departFlightsGrid);
        H3 returnGridHeader = new H3("Return flights");
        VerticalLayout returnFlightsLayout = new VerticalLayout(returnGridHeader
                , noReturnFlightsMessage, returnFlightsGrid);

        add(header, searchView, departFlightsLayout, returnFlightsLayout);

    }

    private void setSearchViewFromOutside() {
        Search searchOutside = (Search) VaadinSession.getCurrent().getAttribute("searchView");
        if (searchOutside != null) {
            searchView.getFromField().setValue(searchOutside.getFrom());
            searchView.getToField().setValue(searchOutside.getTo());
            searchView.getDepartureDateField().setValue(searchOutside.getDepartureDate());
            searchView.getReturnDateField().setValue(searchOutside.getReturnDate());
            searchView.getNumberOfPassengersField().setValue(searchOutside.getNumberOfPassengers());
            searchView.getSearchButton().click();
        }
    }

    private void setSearchButton() {
        searchView.getSearchButton().addClickListener(e -> {
            clearContent();
            noDepartFlightsMessage.setVisible(true);
            noReturnFlightsMessage.setVisible(true);
            if (searchView.createSearch()) {
                ResponseEntity<SearchResult> response = searchClient.save(searchView.getSearch());
                if (!(response.getStatusCode() == HttpStatus.NO_CONTENT)) {
                    searchResult = response.getBody();
                    if (!searchResult.getDepartFlights().isEmpty()) {
                        departFlights.addAll(searchResult.getDepartFlights());
                        noDepartFlightsMessage.setVisible(false);
                    }
                    if (!searchResult.getReturnFlights().isEmpty()) {
                        returnFlights.addAll(searchResult.getReturnFlights());
                        noReturnFlightsMessage.setVisible(false);
                    }
                }
                refreshGridsOfFlights();
            }
        });
    }

    private void setFlightsGrids() {
        departFlightsGrid.setAllRowsVisible(true);
        departFlightsGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        returnFlightsGrid.setAllRowsVisible(true);
        returnFlightsGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    private void setNoFlightsMessage() {
        noDepartFlightsMessage.setVisible(false);
        noReturnFlightsMessage.setVisible(false);
    }

    private Grid.Column<FlightDTO> createDepartureDataTimeColumn(Grid<FlightDTO> grid) {
        return grid.addColumn(FlightDTO::getDepartureDateTime);
    }

    private Grid.Column<FlightDTO> createAirportFromColumn(Grid<FlightDTO> grid) {
        return grid.addColumn(FlightDTO::getAirportFrom);
    }

    private Grid.Column<FlightDTO> createAirportToColumn(Grid<FlightDTO> grid) {
        return grid.addColumn(FlightDTO::getAirportTo);
    }

    private Grid.Column<FlightDTO> createArrivalDataTimeColumn(Grid<FlightDTO> grid) {
        return grid.addColumn(FlightDTO::getArrivalDateTime);
    }

//    private Grid.Column<FlightDTO> createFlightSeatsColumn(Grid<FlightDTO> grid) {
//        return grid.addColumn(flight -> {
//            Button button = new Button("View FlightSeats");
//            button.addClickListener(e -> openFlightSeatsTable(flight.getId()));
//            return button;
//        });
//    }

//    private void openFlightSeatsTable(Long id) {
//        flightSeatGrid.getDataProvider().refreshAll();
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("fare"));
//        ResponseEntity<Page<FlightSeatDTO>> response = flightSeatClient
//                .getAllPagesFlightSeatsDTO(pageable, Optional.of(id),false,false);
//        if(response.getStatusCode() == HttpStatus.OK) {
//            flightSeatGrid.setItems(response.getBody().get());
//            Dialog flightSeatsDialog = new Dialog();
//            flightSeatsDialog.add(flightSeatGrid);
//            flightSeatsDialog.open();
//        }
//    }

    private void clearContent() {
        departFlights.clear();
        returnFlights.clear();
    }

    private void refreshGridsOfFlights() {
        departFlightsGrid.getDataProvider().refreshAll();
        returnFlightsGrid.getDataProvider().refreshAll();
    }

}