package app.controllers.view;

import app.clients.SearchClient;
import app.dto.FlightDto;
import app.dto.FlightSeatDto;
import app.dto.search.Search;
import app.dto.search.SearchResult;

import app.dto.search.SearchResultCard;
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
    private final Grid<SearchResultCard> departFlightsGrid = new Grid<>(SearchResultCard.class, false);
    private final Grid<SearchResultCard> returnFlightsGrid = new Grid<>(SearchResultCard.class, false);
    private final Grid<FlightSeatDto> flightSeatGrid = new Grid<>(FlightSeatDto.class, false);
    private final List<SearchResultCard> departFlights = new ArrayList<>();
    private final List<SearchResultCard> returnFlights = new ArrayList<>();
    private SearchResult searchResult;
    private final H5 noDepartFlightsMessage = new H5("Flights not found");
    private final H5 noReturnFlightsMessage = new H5("Flights not found");

    public SearchResultView(SearchClient searchClient) {

        this.searchClient = searchClient;

        setFlightsGrids();
        setNoFlightsMessage();
        setSearchButton();
        setSearchViewFromOutside();

        Grid.Column<SearchResultCard> departureDataTimeDepartureFlight = createDepartureDataTimeColumnDepart(departFlightsGrid);
        Grid.Column<SearchResultCard> airportFromDepartureFlight = createAirportFromColumnDepart(departFlightsGrid);
        Grid.Column<SearchResultCard> airportToDepartureFlight = createAirportToColumnDepart(departFlightsGrid);
        Grid.Column<SearchResultCard> arrivalDataTimeDepartureFlight = createArrivalDataTimeColumnDepart(departFlightsGrid);
     //   Grid.Column<SearchResultCard> flightSeatsListDepartureFlight = createFlightSeatsColumn(departFlightsGrid);

        Grid.Column<SearchResultCard> departureDataTimeReturnFlight = createDepartureDataTimeColumnReturn(returnFlightsGrid);
        Grid.Column<SearchResultCard> airportFromReturnFlight = createAirportFromColumnReturn(returnFlightsGrid);
        Grid.Column<SearchResultCard> airportToReturnFlight = createAirportToColumnReturn(returnFlightsGrid);
        Grid.Column<SearchResultCard> arrivalDataTimeReturnFlight = createArrivalDataTimeColumnReturn(returnFlightsGrid);
     //   Grid.Column<SearchResultCard> flightSeatsListReturnFlight = createFlightSeatsColumn(returnFlightsGrid);

        Grid.Column<FlightSeatDto> categorySeat = createCategorySeatColumn(flightSeatGrid);
        Grid.Column<FlightSeatDto> numberFlightSeat = createNumberSeatColumn(flightSeatGrid);
        Grid.Column<FlightSeatDto> fareFlightSeat = createFareColumn(flightSeatGrid);


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
                    if (!searchResult.getFlights().isEmpty()) {
                        departFlights.addAll(searchResult.getFlights());
                        noDepartFlightsMessage.setVisible(false);
                    }
                    if (!searchResult.getFlights().isEmpty()) {
                        returnFlights.addAll(searchResult.getFlights());
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

    private Grid.Column<SearchResultCard> createDepartureDataTimeColumnDepart(Grid<SearchResultCard> grid) {
        return grid.addColumn(card -> card.getDataTo().getDepartureDateTime());
    }

    private Grid.Column<SearchResultCard> createDepartureDataTimeColumnReturn(Grid<SearchResultCard> grid) {
        return grid.addColumn(card -> card.getDataBack().getDepartureDateTime());
    }

    private Grid.Column<SearchResultCard> createAirportFromColumnDepart(Grid<SearchResultCard> grid) {
        return grid.addColumn(card -> card.getDataTo().getAirportFrom());
    }

    private Grid.Column<SearchResultCard> createAirportFromColumnReturn(Grid<SearchResultCard> grid) {
        return grid.addColumn(card -> card.getDataBack().getAirportFrom());
    }

    private Grid.Column<SearchResultCard> createAirportToColumnDepart(Grid<SearchResultCard> grid) {
        return grid.addColumn(card -> card.getDataTo().getAirportTo());
    }

    private Grid.Column<SearchResultCard> createAirportToColumnReturn(Grid<SearchResultCard> grid) {
        return grid.addColumn(card -> card.getDataBack().getAirportTo());
    }

    private Grid.Column<SearchResultCard> createArrivalDataTimeColumnDepart(Grid<SearchResultCard> grid) {
        return grid.addColumn(card -> card.getDataTo().getArrivalDateTime());
    }

    private Grid.Column<SearchResultCard> createArrivalDataTimeColumnReturn(Grid<SearchResultCard> grid) {
        return grid.addColumn(card -> card.getDataBack().getArrivalDateTime());
    }

    //  теперь будут отображаться только свободные flightSeats
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

    // Открываем список билетов для конкретного рейса при нажатии на кнопку View Flight Seats
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
    private void openViewDetails(String seatNumber) {
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

