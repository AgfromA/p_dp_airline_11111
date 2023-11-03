package app.controllers.view;

import app.clients.SearchClient;
import app.dto.FlightDTO;
import app.dto.FlightSeatDTO;
import app.dto.search.Search;
import app.dto.search.SearchResult;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H3;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Route(value = "search", layout = MainLayout.class)
public class SearchResultView extends VerticalLayout {

    private final SearchClient searchClient;
    private final SearchForm searchForm = new SearchForm();
    private final Header header = new Header();
    private final Grid<FlightDTO> departFlightsGrid = new Grid<>(FlightDTO.class, false);
    private final Grid<FlightDTO> returnFlightsGrid = new Grid<>(FlightDTO.class, false);
    private final Grid<FlightSeatDTO> flightSeatGrid = new Grid<>(FlightSeatDTO.class, false);
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
        Grid.Column<FlightDTO> flightSeatsListDepartureFlight = createFlightSeatsColumn(departFlightsGrid);

        Grid.Column<FlightDTO> departureDataTimeReturnFlight = createDepartureDataTimeColumn(returnFlightsGrid);
        Grid.Column<FlightDTO> airportFromReturnFlight = createAirportFromColumn(returnFlightsGrid);
        Grid.Column<FlightDTO> airportToReturnFlight = createAirportToColumn(returnFlightsGrid);
        Grid.Column<FlightDTO> arrivalDataTimeReturnFlight = createArrivalDataTimeColumn(returnFlightsGrid);
        Grid.Column<FlightDTO> flightSeatsListReturnFlight = createFlightSeatsColumn(returnFlightsGrid);

        Grid.Column<FlightSeatDTO> categorySeat = createCategorySeatColumn(flightSeatGrid);
        Grid.Column<FlightSeatDTO> numberFlightSeat = createNumberSeatColumn(flightSeatGrid);
        Grid.Column<FlightSeatDTO> fareFlightSeat = createFareColumn(flightSeatGrid);
        Grid.Column<FlightSeatDTO> buttonSeatView = createSeatsButton(flightSeatGrid);

        departFlightsGrid.setItems(departFlights);
        returnFlightsGrid.setItems(returnFlights);

        H3 departureGridHeader = new H3("Departure flights");
        VerticalLayout departFlightsLayout = new VerticalLayout(departureGridHeader
                , noDepartFlightsMessage, departFlightsGrid);
        H3 returnGridHeader = new H3("Return flights");
        VerticalLayout returnFlightsLayout = new VerticalLayout(returnGridHeader
                , noReturnFlightsMessage, returnFlightsGrid);

        add(header, searchForm, departFlightsLayout, returnFlightsLayout);

    }

    private void setSearchViewFromOutside() {
        Search searchOutside = (Search) VaadinSession.getCurrent().getAttribute("search");
        if (searchOutside != null) {
            searchForm.getFromField().setValue(searchOutside.getFrom());
            searchForm.getToField().setValue(searchOutside.getTo());
            searchForm.getDepartureDateField().setValue(searchOutside.getDepartureDate());
            searchForm.getReturnDateField().setValue(searchOutside.getReturnDate());
            searchForm.getNumberOfPassengersField().setValue(searchOutside.getNumberOfPassengers());
            searchForm.getSearchButton().click();
        }
    }

    private void setSearchButton() {
        searchForm.getSearchButton().addClickListener(e -> {
            clearContent();
            noDepartFlightsMessage.setVisible(true);
            noReturnFlightsMessage.setVisible(true);
            if (searchForm.createSearch()) {
                ResponseEntity<SearchResult> response = searchClient.get(searchForm.getSearch().getFrom()
                        , searchForm.getSearch().getTo(), searchForm.getSearch().getDepartureDate()
                        , searchForm.getSearch().getReturnDate(), searchForm.getSearch().getNumberOfPassengers());
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
        departFlightsGrid.setSelectionMode(Grid.SelectionMode.NONE);
        returnFlightsGrid.setAllRowsVisible(true);
        returnFlightsGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        returnFlightsGrid.setSelectionMode(Grid.SelectionMode.NONE);
        flightSeatGrid.setAllRowsVisible(true);
        flightSeatGrid.setSelectionMode(Grid.SelectionMode.NONE);
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

    //теперь будут отображаться только свободные flightSeats
    private Grid.Column<FlightDTO> createFlightSeatsColumn(Grid<FlightDTO> grid) {
        return grid.addComponentColumn(flight -> {
            List<FlightSeatDTO> list = new ArrayList<>();
            for (FlightSeatDTO flightSeat : flight.getSeats()) {
                if (flightSeat.getIsSold() || flightSeat.getIsBooked() || flightSeat.getIsRegistered()) {
                    continue;
                }
                list.add(flightSeat);
            }
            Button button = new Button("View flight seats");
            button.addClickListener(e -> openFlightSeatsTable(list));
            return button;
        });
    }

    //Открываем список билетов для конкретного рейса при нажатии на кнопку View Flight Seats
    private void openFlightSeatsTable(List<FlightSeatDTO> list) {
        flightSeatGrid.getDataProvider().refreshAll();
            flightSeatGrid.setItems(list);
            Dialog flightSeatsDialog = new Dialog();
            flightSeatsDialog.setWidth("50%");
            flightSeatsDialog.add(flightSeatGrid);
            flightSeatsDialog.open();
    }

    private Grid.Column<FlightSeatDTO> createFareColumn(Grid<FlightSeatDTO> grid) {
        return grid.addColumn(FlightSeatDTO::getFare).setHeader("fare");
    }

    private Grid.Column<FlightSeatDTO> createNumberSeatColumn(Grid<FlightSeatDTO> grid) {
        return grid.addColumn(e -> e.getSeat().getSeatNumber()).setHeader("seat number");
    }

    private Grid.Column<FlightSeatDTO> createCategorySeatColumn(Grid<FlightSeatDTO> grid) {
        return grid.addColumn(FlightSeatDTO::getCategory).setHeader("category");
    }

    private Grid.Column<FlightSeatDTO> createSeatsButton(Grid<FlightSeatDTO> grid) {
        return grid.addComponentColumn(seat -> {
            Button button = new Button("View details");
            button.addClickListener(e -> openViewDetails(seat.getSeat().getSeatNumber()));
            return button;
        }).setHeader("details");
    }

    //Здесь реализуем действия при нажатии на кнопку details, как вариант перебросить на страницу бронирования
    private void  openViewDetails(String seatNumber) {
        Div message = new Div();
        message.setText("Дальше думаем что можно сделать :)");
        Dialog flightSeatsDialog = new Dialog();
        flightSeatsDialog.addClassName("width-auto");
        flightSeatsDialog.setWidth("50%");
        flightSeatsDialog.add(message);
        flightSeatsDialog.open();
    }

    private void clearContent() {
        departFlights.clear();
        returnFlights.clear();
    }


    private void refreshGridsOfFlights() {
        departFlightsGrid.getDataProvider().refreshAll();
        returnFlightsGrid.getDataProvider().refreshAll();
    }

}