package app.controllers.view;

import app.clients.FlightSeatClient;
import app.clients.SearchClient;
import app.dto.search.Search;
import app.dto.search.SearchResult;

import app.dto.search.SearchResultCard;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


@Route(value = "search", layout = MainLayout.class)
public class SearchResultView extends VerticalLayout {
    private final SearchClient searchClient;
    private final FlightSeatClient flightSeatClient;
    private final SearchForm searchForm = new SearchForm();
    private final Header header = new Header();
    private final Grid<SearchResultCard> flightsGrid = new Grid<>(SearchResultCard.class, false);
    private final Grid<SearchResultCard> flightSeatGridDepart = new Grid<>(SearchResultCard.class, false);
    private final Grid<SearchResultCard> flightSeatGridReturn = new Grid<>(SearchResultCard.class, false);
    private final List<SearchResultCard> flights = new ArrayList<>();
    private SearchResult searchResult;
    private final H5 noFlightsMessage = new H5("Flights not found");

    public SearchResultView(SearchClient searchClient, FlightSeatClient flightSeatClient) {

        this.searchClient = searchClient;
        this.flightSeatClient = flightSeatClient;

        setFlightsGrids();
        setNoFlightsMessage();
        setSearchButton();
        setSearchViewFromOutside();
        Grid.Column<SearchResultCard> totalPrice = createTotalPrice(flightsGrid);
        Grid.Column<SearchResultCard> flightSeatsList = createFlightSeatsColumn(flightsGrid);
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


        Grid.Column<SearchResultCard> categorySeatDepart = createCategorySeatColumn(flightSeatGridDepart, false);
        Grid.Column<SearchResultCard> numberFlightSeatDepart = createNumberSeatColumn(flightSeatGridDepart, false);
        Grid.Column<SearchResultCard> fareFlightSeatDepart = createFareColumn(flightSeatGridDepart, false);

        Grid.Column<SearchResultCard> categorySeatReturn = createCategorySeatColumn(flightSeatGridReturn, true);
        Grid.Column<SearchResultCard> numberFlightSeatReturn = createNumberSeatColumn(flightSeatGridReturn, true);
        Grid.Column<SearchResultCard> fareFlightSeatReturn = createFareColumn(flightSeatGridReturn, true);

        flightsGrid.setItems(flights);

        VerticalLayout departFlightsLayout = new VerticalLayout(noFlightsMessage, flightsGrid);
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
                        , searchForm.getSearch().getReturnDate(), searchForm.getSearch().getNumberOfPassengers(),
                        searchForm.getSearch().getCategoryOfSeats());
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

    private Grid.Column<SearchResultCard> createCategorySeatColumn(Grid<SearchResultCard> grid, boolean isReturn) {
        return grid.addColumn(card -> {
            if (isReturn) {
                return card.getDataBack() != null ? flightSeatClient.getFlightSeat(card.getDataBack().getFlightSeatId()).getBody().getCategory() : "";
            } else {
                return flightSeatClient.getFlightSeat(card.getDataTo().getFlightSeatId()).getBody().getCategory();
            }
        }).setHeader("Категория места");
    }

    private Grid.Column<SearchResultCard> createNumberSeatColumn(Grid<SearchResultCard> grid, boolean isReturn) {
        return grid.addColumn(card -> {
            if (isReturn) {
                return card.getDataBack() != null ? flightSeatClient.getFlightSeat(card.getDataBack().getFlightSeatId()).getBody().getSeat().getSeatNumber() : "";
            } else {
                return flightSeatClient.getFlightSeat(card.getDataTo().getFlightSeatId()).getBody().getSeat().getSeatNumber();
            }
        }).setHeader("Место");
    }

    private Grid.Column<SearchResultCard> createFareColumn(Grid<SearchResultCard> grid, boolean isReturn) {
        return grid.addColumn(card -> {
            if (isReturn) {
                return card.getDataBack() != null ? flightSeatClient.getFlightSeat(card.getDataBack().getFlightSeatId()).getBody().getFare() : "";
            } else {
                return flightSeatClient.getFlightSeat(card.getDataTo().getFlightSeatId()).getBody().getFare();
            }
        }).setHeader("Стоимость");
    }

    private Grid.Column<SearchResultCard> createTotalPrice(Grid<SearchResultCard> grid) {
        return grid.addColumn(card -> {
            String totalPrice = String.valueOf(card.getTotalPrice());
            return totalPrice + " ₽";
        });
    }

    private void clearContent() {
        flights.clear();
    }

    private void refreshGridsOfFlights() {
        flightsGrid.getDataProvider().refreshAll();
    }

    private Grid.Column<SearchResultCard> createFlightSeatsColumn(Grid<SearchResultCard> grid) {
        return grid.addComponentColumn(flight -> {
            Button button = new Button("Билеты");
            button.addClickListener(e -> {
                openFlightSeatsTable(flight);
            });
            return button;
        });
    }

    private void openFlightSeatsTable(SearchResultCard flight) {
        Dialog dialog = new Dialog();
        Div div = new Div();
        List<Long> list = new ArrayList<>();
        Long flightSeatIdDepart = flight.getDataTo().getFlightSeatId();
        Long flightSeatIdReturn = flight.getDataBack().getFlightSeatId();
        list.add(flightSeatIdDepart);
        list.add(flightSeatIdReturn);
        Button reserveButton = new Button("Выбрать места");
        reserveButton.addClickListener(event -> openFlightSeatsTable(list));
        div.add(reserveButton, new Text(" "));

        dialog.add(div);
        dialog.open();
    }

    private void openFlightSeatsTable(List<Long> seatIds) {
        flightSeatGridDepart.setItems(new ArrayList<>(flights));
        flightSeatGridDepart.getDataProvider().refreshAll();
        flightSeatGridReturn.setItems(new ArrayList<>(flights));
        flightSeatGridReturn.getDataProvider().refreshAll();

        flightSeatGridDepart.addComponentColumn(card -> {
            Button bookButton = new Button("Забронировать");
            bookButton.addClickListener(event -> {
                Dialog reservationDialog = new Dialog(new Text("Забронировано"));
                reservationDialog.open();
            });
            return bookButton;
        });

        flightSeatGridReturn.addComponentColumn(card -> {
            Button bookButton = new Button("Забронировать");
            bookButton.addClickListener(event -> {
                Dialog reservationDialog = new Dialog(new Text("Забронировано"));
                reservationDialog.open();
            });
            return bookButton;
        });

        Dialog flightSeatsDialog = new Dialog();
        flightSeatsDialog.setWidth("50%");
        flightSeatsDialog.add(flightSeatGridDepart);
        flightSeatsDialog.add(flightSeatGridReturn);
        flightSeatsDialog.open();
    }
}

