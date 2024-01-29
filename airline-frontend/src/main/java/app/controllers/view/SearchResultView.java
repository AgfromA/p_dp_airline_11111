package app.controllers.view;

import app.clients.SearchClient;
import app.dto.search.Search;
import app.dto.search.SearchResult;

import app.dto.search.SearchResultCard;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
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
    private final Grid<SearchResultCard> flightsGrid = new Grid<>(SearchResultCard.class, false);
    private final Grid<Long> flightSeatGrid = new Grid<>(Long.class, false);
    private final List<SearchResultCard> flights = new ArrayList<>();

    private final List<SearchResultCard> common = new ArrayList<>();
    private SearchResult searchResult;
    private final H5 noFlightsMessage = new H5("Flights not found");


    public SearchResultView(SearchClient searchClient) {

        this.searchClient = searchClient;

        setFlightsGrids();
        setNoFlightsMessage();
        setSearchButton();
        setSearchViewFromOutside();
        Grid.Column<SearchResultCard> totalPrice = createTotalPrice(flightsGrid);
        Grid.Column<SearchResultCard> flightSeatsListDepartFlight = createFlightSeatsColumn(flightsGrid);
        Grid.Column<SearchResultCard> departureDataTimeDepartureFlight = createDepartureDataTimeColumn(flightsGrid, false);
        Grid.Column<SearchResultCard> airportFromDepartureFlight = createAirportFromColumn(flightsGrid, false);
        Grid.Column<SearchResultCard> airportToDepartureFlight = createAirportToColumn(flightsGrid, false);
        Grid.Column<SearchResultCard> arrivalDataTimeDepartureFlight = createArrivalDataTimeColumn(flightsGrid, false);
        Grid.Column<SearchResultCard> flightTimeDepartFlight = createFlightTimeColumn(flightsGrid, false);



        Grid.Column<SearchResultCard> departureDataTimeReturnFlight = createDepartureDataTimeColumn(flightsGrid, true);
        Grid.Column<SearchResultCard> airportFromReturnFlight = createAirportFromColumn(flightsGrid, true);
        Grid.Column<SearchResultCard> airportToReturnFlight = createAirportToColumn(flightsGrid, true);
        Grid.Column<SearchResultCard> arrivalDataTimeReturnFlight = createArrivalDataTimeColumn(flightsGrid, true);
        Grid.Column<SearchResultCard> flightTimeReturnFlight = createFlightTimeColumn(flightsGrid, true);


        flightsGrid.setItems(flights);

        H3 departureGridHeader = new H3("flights");
        VerticalLayout departFlightsLayout = new VerticalLayout(departureGridHeader
                , noFlightsMessage, flightsGrid);
        add(header, searchForm, departFlightsLayout);

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
            noFlightsMessage.setVisible(true);
            if (searchForm.createSearch()) {
                ResponseEntity<SearchResult> response = searchClient.search(searchForm.getSearch().getFrom()
                        , searchForm.getSearch().getTo(), searchForm.getSearch().getDepartureDate()
                        , searchForm.getSearch().getReturnDate(), searchForm.getSearch().getNumberOfPassengers());
                if (!(response.getStatusCode() == HttpStatus.NO_CONTENT)) {
                    searchResult = response.getBody();
                    if (!searchResult.getFlights().isEmpty()) {
                        flights.addAll(searchResult.getFlights());
                        noFlightsMessage.setVisible(false);
                    }
                }
                refreshGridsOfFlights();
            }
        });
    }

    private void setFlightsGrids() {
        flightsGrid.setAllRowsVisible(true);
        flightsGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        flightsGrid.setSelectionMode(Grid.SelectionMode.NONE);
        flightSeatGrid.setAllRowsVisible(true);
        flightSeatGrid.setSelectionMode(Grid.SelectionMode.NONE);

    }

    private void setNoFlightsMessage() {
        noFlightsMessage.setVisible(false);
    }

    private Grid.Column<SearchResultCard> createDepartureDataTimeColumn(Grid<SearchResultCard> grid, boolean isReturn) {
        return grid.addColumn(card -> {
            if (isReturn) {
                return card.getDataBack() != null ? card.getDataBack().getDepartureDateTime() : "";
            } else {
                return card.getDataTo().getDepartureDateTime();
            }
        });
    }

    private Grid.Column<SearchResultCard> createAirportFromColumn(Grid<SearchResultCard> grid, boolean isReturn) {
        return grid.addColumn(card -> {
            if (isReturn) {
                return card.getDataBack() != null ? card.getDataBack().getCityFrom() : "";
            } else {
                return card.getDataTo().getCityFrom();
            }
        });
    }

    private Grid.Column<SearchResultCard> createAirportToColumn(Grid<SearchResultCard> grid, boolean isReturn) {
        return grid.addColumn(card -> {
            if (isReturn) {
                return card.getDataBack() != null ? card.getDataBack().getCityTo() : "";
            } else {
                return card.getDataTo().getCityTo();
            }
        });
    }

    private Grid.Column<SearchResultCard> createArrivalDataTimeColumn(Grid<SearchResultCard> grid, boolean isReturn) {
        return grid.addColumn(card -> {
            if (isReturn) {
                return card.getDataBack() != null ? card.getDataBack().getArrivalDateTime() : "";
            } else {
                return card.getDataTo().getArrivalDateTime();
            }
        });
    }

    private Grid.Column<SearchResultCard> createFlightTimeColumn(Grid<SearchResultCard> grid, boolean isReturn) {
        return grid.addColumn(card -> {
            if (isReturn) {
                return card.getDataBack() != null ? card.getDataBack().getFlightTime() : "";
            } else {
                return card.getDataTo().getFlightTime();
            }
        });
    }

    private Grid.Column<SearchResultCard> createTotalPrice(Grid<SearchResultCard> grid) {
        return grid.addColumn(card -> card.getTotalPrice());
    }


    private void clearContent() {
        flights.clear();
    }

    private void refreshGridsOfFlights() {
        flightsGrid.getDataProvider().refreshAll();
    }

    private Grid.Column<SearchResultCard> createSeatsButton(Grid<SearchResultCard> grid) {
        return grid.addComponentColumn(seat -> {
            Button button = new Button("View details");
            button.addClickListener(e -> openViewDetails(seat.getDataTo().getFlightSeatId()));
            return button;
        }).setHeader("details");
    }

    //Здесь реализуем действия при нажатии на кнопку details, как вариант перебросить на страницу бронирования
    private void openViewDetails(Long seatNumber) {
        Div message = new Div();
        message.setText("");
        Dialog flightSeatsDialog = new Dialog();
        flightSeatsDialog.addClassName("width-auto");
        flightSeatsDialog.setWidth("50%");

        SearchResultCard selectedFlight = flightsGrid.asSingleSelect().getValue();
        if (selectedFlight != null && selectedFlight.getDataBack() != null) {
            Long flightSeatId = selectedFlight.getDataBack().getFlightSeatId();
            message.setText("Selected Flight Seat ID: " + flightSeatId);
        }
        flightSeatsDialog.add(message);
        flightSeatsDialog.open();
    }

    //  теперь будут отображаться только свободные flightSeats
    private Grid.Column<SearchResultCard> createFlightSeatsColumn(Grid<SearchResultCard> grid) {
        return grid.addComponentColumn(flight -> {
            List<Long> list = new ArrayList<>();
            Long flightSeatIdDepart = flight.getDataTo().getFlightSeatId();
            Long flightSeatIdReturn = flight.getDataBack().getFlightSeatId();
            list.add(flightSeatIdDepart);
            list.add(flightSeatIdReturn);
            Button button = new Button("Выбрать билет");
            button.addClickListener(e -> {
                Notification.show("Flight Seat ID: " + flightSeatIdDepart);
                Notification.show("Flight Seat ID: " + flightSeatIdReturn);
                openFlightSeatsTable(list);
            });
            return button;
        });
    }
    // Открываем список билетов для конкретного рейса при нажатии на кнопку Выбрать билет
    private void openFlightSeatsTable(List<Long> list) {
        flightSeatGrid.getDataProvider().refreshAll();
        flightSeatGrid.setItems(list);
        Dialog flightSeatsDialog = new Dialog();
        flightSeatsDialog.setWidth("50%");
        flightSeatsDialog.add(flightSeatGrid);
        flightSeatsDialog.open();
    }
}

