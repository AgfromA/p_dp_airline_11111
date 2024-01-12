package app.controllers.view;

import app.clients.SearchClient;
import app.dto.FlightDto;
import app.dto.FlightSeatDto;
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
    private final Grid<FlightDto> departFlightsGrid = new Grid<>(FlightDto.class, false);
    private final Grid<FlightDto> returnFlightsGrid = new Grid<>(FlightDto.class, false);
    private final Grid<FlightSeatDto> flightSeatGrid = new Grid<>(FlightSeatDto.class, false);
    private final List<FlightDto> departFlights = new ArrayList<>();
    private final List<FlightDto> returnFlights = new ArrayList<>();
    private SearchResult searchResult;
    private final H5 noDepartFlightsMessage = new H5("Flights not found");
    private final H5 noReturnFlightsMessage = new H5("Flights not found");

    public SearchResultView(SearchClient searchClient) {

        this.searchClient = searchClient;

        setFlightsGrids();
        setNoFlightsMessage();
        setSearchButton();
        setSearchViewFromOutside();

        Grid.Column<FlightDto> departureDataTimeDepartureFlight = createDepartureDataTimeColumn(departFlightsGrid);
        Grid.Column<FlightDto> airportFromDepartureFlight = createAirportFromColumn(departFlightsGrid);
        Grid.Column<FlightDto> airportToDepartureFlight = createAirportToColumn(departFlightsGrid);
        Grid.Column<FlightDto> arrivalDataTimeDepartureFlight = createArrivalDataTimeColumn(departFlightsGrid);
        Grid.Column<FlightDto> flightSeatsListDepartureFlight = createFlightSeatsColumn(departFlightsGrid);

        Grid.Column<FlightDto> departureDataTimeReturnFlight = createDepartureDataTimeColumn(returnFlightsGrid);
        Grid.Column<FlightDto> airportFromReturnFlight = createAirportFromColumn(returnFlightsGrid);
        Grid.Column<FlightDto> airportToReturnFlight = createAirportToColumn(returnFlightsGrid);
        Grid.Column<FlightDto> arrivalDataTimeReturnFlight = createArrivalDataTimeColumn(returnFlightsGrid);
        Grid.Column<FlightDto> flightSeatsListReturnFlight = createFlightSeatsColumn(returnFlightsGrid);

        Grid.Column<FlightSeatDto> categorySeat = createCategorySeatColumn(flightSeatGrid);
        Grid.Column<FlightSeatDto> numberFlightSeat = createNumberSeatColumn(flightSeatGrid);
        Grid.Column<FlightSeatDto> fareFlightSeat = createFareColumn(flightSeatGrid);
        Grid.Column<FlightSeatDto> buttonSeatView = createSeatsButton(flightSeatGrid);

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
                ResponseEntity<SearchResult> response = searchClient.search(searchForm.getSearch().getFrom()
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

    private Grid.Column<FlightDto> createDepartureDataTimeColumn(Grid<FlightDto> grid) {
        return grid.addColumn(FlightDto::getDepartureDateTime);
    }

    private Grid.Column<FlightDto> createAirportFromColumn(Grid<FlightDto> grid) {
        return grid.addColumn(FlightDto::getAirportFrom);
    }

    private Grid.Column<FlightDto> createAirportToColumn(Grid<FlightDto> grid) {
        return grid.addColumn(FlightDto::getAirportTo);
    }

    private Grid.Column<FlightDto> createArrivalDataTimeColumn(Grid<FlightDto> grid) {
        return grid.addColumn(FlightDto::getArrivalDateTime);
    }

    //теперь будут отображаться только свободные flightSeats
    private Grid.Column<FlightDto> createFlightSeatsColumn(Grid<FlightDto> grid) {
        return grid.addComponentColumn(flight -> {
            List<FlightSeatDto> list = new ArrayList<>();
            for (FlightSeatDto flightSeat : flight.getSeats()) {
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
    private void openFlightSeatsTable(List<FlightSeatDto> list) {
        flightSeatGrid.getDataProvider().refreshAll();
            flightSeatGrid.setItems(list);
            Dialog flightSeatsDialog = new Dialog();
            flightSeatsDialog.setWidth("50%");
            flightSeatsDialog.add(flightSeatGrid);
            flightSeatsDialog.open();
    }

    private Grid.Column<FlightSeatDto> createFareColumn(Grid<FlightSeatDto> grid) {
        return grid.addColumn(FlightSeatDto::getFare).setHeader("fare");
    }

    private Grid.Column<FlightSeatDto> createNumberSeatColumn(Grid<FlightSeatDto> grid) {
        return grid.addColumn(e -> e.getSeat().getSeatNumber()).setHeader("seat number");
    }

    private Grid.Column<FlightSeatDto> createCategorySeatColumn(Grid<FlightSeatDto> grid) {
        return grid.addColumn(FlightSeatDto::getCategory).setHeader("category");
    }

    private Grid.Column<FlightSeatDto> createSeatsButton(Grid<FlightSeatDto> grid) {
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