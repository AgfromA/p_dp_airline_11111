package app.controllers.view;

import app.clients.AircraftClient;
import app.clients.FlightClient;
import app.dto.FlightDTO;
import app.dto.FlightSeatDTO;
import app.enums.Airport;
import app.enums.FlightStatus;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import feign.FeignException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "flight", layout = MainLayout.class)
public class FlightView extends VerticalLayout {

    private final FlightClient flightClient;
    private final AircraftClient aircraftClient;
    private List<FlightDTO> dataSource;
    private final Grid<FlightDTO> grid = new Grid<>(FlightDTO.class, false);
    private final Grid<FlightSeatDTO> flightSeatGrid = new Grid<>(FlightSeatDTO.class, false);
    private final Editor<FlightDTO> editor = grid.getEditor();
    private final Button updateButton;
    private final Button cancelButton;
    private final Button nextButton;
    private final Button previousButton;
    private final Button refreshButton;
    private final Button searchByIdButton;
    private final Button searchByDestinationsAndDatesButton;
    private final IntegerField idSearchField;
    private final TextField cityFromSearchByDestinationsAndDatesField;
    private final TextField cityToSearchByDestinationsAndDatesField;
    private final DateTimePicker dateStartSearchByDestinationsAndDatesField;
    private final DateTimePicker dateFinishSearchByDestinationsAndDatesField;
    private Integer currentPage;
    private Integer maxPages;
    private boolean isSearchById;
    private boolean isSearchByDestinationsAndDates;

    public FlightView(FlightClient flightClient, AircraftClient aircraftClient) {
        this.flightClient = flightClient;
        this.aircraftClient = aircraftClient;
        currentPage = 0;
        PageRequest pageable = PageRequest.of(currentPage, 10, Sort.by("id").ascending());
        isSearchById = false;
        isSearchByDestinationsAndDates = false;
        var response = flightClient.getAllPagesFlightsByDestinationsAndDates(
                null, null, null, null, pageable);
        maxPages = response.getBody().getTotalPages() - 1;
        dataSource = response.getBody().stream().collect(Collectors.toList());

        ValidationMessage idValidationMessage = new ValidationMessage();
        ValidationMessage codeValidationMessage = new ValidationMessage();
        ValidationMessage airportFromValidationMessage = new ValidationMessage();
        ValidationMessage airportToValidationMessage = new ValidationMessage();
        ValidationMessage departureDateTimeValidationMessage = new ValidationMessage();
        ValidationMessage arrivalDateTimeValidationMessage = new ValidationMessage();
        ValidationMessage aircraftIdValidationMessage = new ValidationMessage();
        ValidationMessage flightStatusValidationMessage = new ValidationMessage();

        Grid.Column<FlightDTO> idColumn = createIdColumn();
        Grid.Column<FlightDTO> codeColumn = createCodeColumn();
        Grid.Column<FlightDTO> airportFromColumn = createAirportFromColumn();
        Grid.Column<FlightDTO> airportToColumn = createAirportToColumn();
        Grid.Column<FlightDTO> departureDateTimeColumn = createDepartureDateTimeColumn();
        Grid.Column<FlightDTO> arrivalDateTimeColumn = createArrivalDateTimeColumn();
        Grid.Column<FlightDTO> aircraftIdColumn = createAircraftIdColumn();
        Grid.Column<FlightDTO> flightStatusColumn = createFlightStatusColumn();
        createViewSeatsColumn();
        Grid.Column<FlightDTO> updateColumn = createEditColumn();
        createDeleteColumn();

        createCategorySeatColumn();
        createNumberSeatColumn();
        createFareColumn();

        Binder<FlightDTO> binder = createBinder();

        createIdField(binder, idValidationMessage, idColumn);
        createCodeField(binder, codeValidationMessage, codeColumn);
        createAirportFromField(binder, airportFromValidationMessage, airportFromColumn);
        createAirportToField(binder, airportToValidationMessage, airportToColumn);
        createDepartureDateTimeField(binder, departureDateTimeValidationMessage, departureDateTimeColumn);
        createArrivalDateTimeField(binder, arrivalDateTimeValidationMessage, arrivalDateTimeColumn);
        createAircraftIdField(binder, aircraftIdValidationMessage, aircraftIdColumn);
        createFlightStatusField(binder, flightStatusValidationMessage, flightStatusColumn);

        updateButton = new Button("Update", e ->
        {
            editor.save();
            grid.setItems(dataSource);
            grid.getListDataView().refreshAll();

        }

        );
        cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        previousButton = new Button(VaadinIcon.ARROW_LEFT.create(), e -> previousPage());
        nextButton = new Button(VaadinIcon.ARROW_RIGHT.create(), e -> nextPage());
        refreshButton = createRefreshButton();
        searchByIdButton = createSearchByIdButton();
        idSearchField = createSearchByIdField();
        searchByDestinationsAndDatesButton = createSearchByDestinationsAndDatesButton();
        cityFromSearchByDestinationsAndDatesField = createCityFromSearchByDestinationsAndDatesField();
        cityToSearchByDestinationsAndDatesField = createCityToSearchByDestinationsAndDatesField();
        dateStartSearchByDestinationsAndDatesField = createDateStartSearchByDestinationsAndDatesField();
        dateFinishSearchByDestinationsAndDatesField = createDateFinishSearchByDestinationsAndDatesField();

        addEditorListeners();
        grid.setItems(dataSource);
        grid.setAllRowsVisible(true);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        flightSeatGrid.setAllRowsVisible(true);
        flightSeatGrid.setSelectionMode(Grid.SelectionMode.NONE);
        addTheme();

        Div contentContainer = new Div();
        contentContainer.setSizeFull();
        Tabs tabs = createTabs(contentContainer);
        HorizontalLayout actions = new HorizontalLayout(updateButton, cancelButton);
        actions.setPadding(false);
        updateColumn.setEditorComponent(actions);

        HorizontalLayout changedPages = new HorizontalLayout(refreshButton, previousButton, nextButton);

        HorizontalLayout searchByIdLayout = new HorizontalLayout(idSearchField, searchByIdButton);
        searchByIdLayout.setAlignItems(FlexComponent.Alignment.END);

        HorizontalLayout searchByDestinationsAndDatesLayout1 = new HorizontalLayout(
                cityFromSearchByDestinationsAndDatesField,
                cityToSearchByDestinationsAndDatesField
        );
        searchByDestinationsAndDatesLayout1.setAlignItems(FlexComponent.Alignment.END);

        HorizontalLayout searchByDestinationsAndDatesLayout2 = new HorizontalLayout(
                dateStartSearchByDestinationsAndDatesField,
                dateFinishSearchByDestinationsAndDatesField,
                searchByDestinationsAndDatesButton
        );
        searchByDestinationsAndDatesLayout2.setAlignItems(FlexComponent.Alignment.END);

        add(tabs
                , contentContainer
                , idValidationMessage
                , codeValidationMessage
                , airportFromValidationMessage
                , airportToValidationMessage
                , departureDateTimeValidationMessage
                , arrivalDateTimeValidationMessage
                , aircraftIdValidationMessage
                , flightStatusValidationMessage
                , changedPages
                , searchByIdLayout,
                searchByDestinationsAndDatesLayout1,
                searchByDestinationsAndDatesLayout2
        );
    }

    private Button createSearchByDestinationsAndDatesButton() {
        return new Button("Search By Destinations And Dates", e -> {
            isSearchById = false;
            isSearchByDestinationsAndDates = true;
            currentPage = 0;
            searchByDestinationsAndDates();
        });
    }

    private void searchByDestinationsAndDates() {
        if (dateStartSearchByDestinationsAndDatesField.getValue() == null
                || dateFinishSearchByDestinationsAndDatesField.getValue() == null
                || dateStartSearchByDestinationsAndDatesField.getValue()
                .isBefore(dateFinishSearchByDestinationsAndDatesField.getValue())
        ) {
            String cityFrom = getNullIfEmpty(cityFromSearchByDestinationsAndDatesField.getValue());
            String cityTo = getNullIfEmpty(cityToSearchByDestinationsAndDatesField.getValue());
            String dateStartString = dateTimeConverterToString(dateStartSearchByDestinationsAndDatesField.getValue());
            String dateFinishString = dateTimeConverterToString(dateFinishSearchByDestinationsAndDatesField.getValue());
            dataSource.clear();
            grid.getDataProvider().refreshAll();
            PageRequest pageable = PageRequest.of(currentPage, 10, Sort.by("id").ascending());
            var response = flightClient.getAllPagesFlightsByDestinationsAndDates(
                    cityFrom,
                    cityTo,
                    dateStartString,
                    dateFinishString,
                    pageable
            );
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                Notification.show("Flights with these parameters not found.", 3000, Notification.Position.TOP_CENTER);
                maxPages = 0;
                List<FlightDTO> emptyList = Collections.emptyList();
                grid.setItems(emptyList);
            } else {
                maxPages = response.getBody().getTotalPages() - 1;
                dataSource = response.getBody().stream().collect(Collectors.toList());
                grid.setItems(dataSource);
            }
        } else {
            if (dateStartSearchByDestinationsAndDatesField.getValue()
                    .isAfter(dateFinishSearchByDestinationsAndDatesField.getValue())) {
                Notification.show("Date start must be early then date finish", 3000, Notification.Position.TOP_CENTER);
            }
        }
    }

    private String getNullIfEmpty(String value) {
        if (value.trim().isEmpty()) {
            return null;
        } else return value;
    }

    private String dateTimeConverterToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        } else {
            return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

    private DateTimePicker createDateStartSearchByDestinationsAndDatesField() {
        DateTimePicker dateStar = new DateTimePicker();
        dateStar.setLabel("Date start (not required)");
        dateStar.setStep(Duration.ofSeconds(1));
        return dateStar;
    }

    private DateTimePicker createDateFinishSearchByDestinationsAndDatesField() {
        DateTimePicker dateFinish = new DateTimePicker();
        dateFinish.setLabel("Date finish (not required)");
        dateFinish.setStep(Duration.ofSeconds(1));
        return dateFinish;
    }

    private TextField createCityFromSearchByDestinationsAndDatesField() {
        TextField cityFromField = new TextField();
        cityFromField.setLabel("City from (not required)");
        return cityFromField;
    }

    private TextField createCityToSearchByDestinationsAndDatesField() {
        TextField cityToField = new TextField();
        cityToField.setLabel("City to (not required)");
        cityToField.addKeyPressListener(Key.ENTER, e -> searchByDestinationsAndDatesButton.click());
        return cityToField;
    }

    private IntegerField createSearchByIdField() {
        IntegerField searchField = new IntegerField();
        searchField.setLabel("Flight Id (required)");
        searchField.setWidth("200px");
        searchField.addKeyPressListener(Key.ENTER, e -> searchByIdButton.click());
        return searchField;
    }

    private Button createSearchByIdButton() {
        return new Button("Search", e -> {
            if (idSearchField.isEmpty() || idSearchField.getValue() <= 0) {
                Notification.show("Id must be a valid number", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            isSearchById = true;
            isSearchByDestinationsAndDates = false;
            currentPage = 0;
            maxPages = 1;
            try {
                searchById();
            } catch (FeignException.NotFound ex) {
                Notification.show("Flight with id=" + idSearchField.getValue() +
                        " not found.", 3000, Notification.Position.TOP_CENTER);
            }
            idSearchField.clear();
            cityFromSearchByDestinationsAndDatesField.clear();
            cityToSearchByDestinationsAndDatesField.clear();
            dateStartSearchByDestinationsAndDatesField.clear();
            dateFinishSearchByDestinationsAndDatesField.clear();
        });
    }

    private void addTheme() {
        getThemeList().clear();
        getThemeList().add("spacing-s");
    }

    private Button createRefreshButton() {
        isSearchById = false;
        isSearchByDestinationsAndDates = false;
        return new Button("Refresh", e -> {
            currentPage = 0;
            refreshGridData();
            cityFromSearchByDestinationsAndDatesField.clear();
            cityToSearchByDestinationsAndDatesField.clear();
            dateStartSearchByDestinationsAndDatesField.clear();
            dateFinishSearchByDestinationsAndDatesField.clear();
        });
    }

    private void nextPage() {
        if (isSearchById) {
            return;
        }
        if (!isSearchByDestinationsAndDates) {
            if (currentPage < maxPages) {
                currentPage++;
                defaultCurrentPageOfFlights();
            }
        } else {
            if (currentPage < maxPages) {
                currentPage++;
                searchByDestinationsAndDates();
            }
        }
    }

    private void previousPage() {
        if (isSearchById) {
            return;
        }
        if (!isSearchByDestinationsAndDates) {
            if (currentPage > 0) {
                currentPage--;
                defaultCurrentPageOfFlights();
            }
        } else {
            if (currentPage > 0) {
                currentPage--;
                searchByDestinationsAndDates();
            }
        }
    }

    private void refreshGridData() {
        isSearchById = false;
        isSearchByDestinationsAndDates = false;
        currentPage = 0;
        defaultCurrentPageOfFlights();
    }

    private void searchById() {
        dataSource.clear();
        grid.getDataProvider().refreshAll();
        var a = flightClient.getFlightDTOById(idSearchField.getValue().longValue());
        if (a.getStatusCode() == HttpStatus.NOT_FOUND) {
            List<FlightDTO> emptyList = Collections.emptyList();
            grid.setItems(emptyList);
        }
        dataSource.add(a.getBody());
        grid.setItems(dataSource);
    }

    private void defaultCurrentPageOfFlights() {
        dataSource.clear();
        PageRequest pageable = PageRequest.of(currentPage, 10, Sort.by("id").ascending());
        var response = flightClient.getAllPagesFlightsByDestinationsAndDates(
                null, null, null, null, pageable);
        maxPages = response.getBody().getTotalPages() - 1;
        dataSource = response.getBody().stream().collect(Collectors.toList());


        grid.getDataProvider().refreshAll();
        grid.setItems(dataSource);
    }

    private Grid.Column<FlightDTO> createIdColumn() {
        return grid.addColumn(FlightDTO -> FlightDTO.getId()
                .intValue()).setHeader("Id").setWidth("80px").setFlexGrow(0);
    }

    private Grid.Column<FlightDTO> createCodeColumn() {
        return grid.addColumn(FlightDTO::getCode).setHeader("Flight code").setWidth("70px");
    }

    private Grid.Column<FlightDTO> createAirportFromColumn() {
        return grid.addColumn(FlightDTO::getAirportFrom).setHeader("Airport From Id").setWidth("50px");
    }

    private Grid.Column<FlightDTO> createAirportToColumn() {
        return grid.addColumn(FlightDTO::getAirportTo).setHeader("Airport To Id").setWidth("50px");
    }

    private Grid.Column<FlightDTO> createDepartureDateTimeColumn() {
        return grid.addColumn(FlightDTO::getDepartureDateTime).setHeader("Departure Date And Time").setWidth("190px");
    }

    private Grid.Column<FlightDTO> createArrivalDateTimeColumn() {
        return grid.addColumn(FlightDTO::getArrivalDateTime).setHeader("Arrival Date And Time").setWidth("190px");
    }

    private Grid.Column<FlightDTO> createAircraftIdColumn() {
        return grid.addColumn(FlightDTO::getAircraftId).setHeader("Aircraft Id").setWidth("50px");
    }

    private Grid.Column<FlightDTO> createFlightStatusColumn() {
        return grid.addColumn(FlightDTO::getFlightStatus).setHeader("Flight Status").setWidth("120px");
    }

    private Grid.Column<FlightDTO> createViewSeatsColumn() {
        return grid.addComponentColumn(flight -> {
            List<FlightSeatDTO> seatList = flight.getSeats();
            Button viewSeatsButton = new Button("View seats");
            viewSeatsButton.addClickListener(e -> {
                openFlightSeatsTable(seatList);
            });
            return viewSeatsButton;
        });
    }

    private void openFlightSeatsTable(List<FlightSeatDTO> seatList) {
        Dialog flightSeatsDialog = new Dialog();
        flightSeatsDialog.setWidth("40%");
        if (!seatList.isEmpty()) {
            flightSeatGrid.getDataProvider().refreshAll();
            flightSeatGrid.setItems(seatList);
            flightSeatsDialog.add(flightSeatGrid);
            flightSeatsDialog.open();
        } else {
            VerticalLayout layout = new VerticalLayout();
            Label label = new Label("There are no seats for display");
            layout.add(label);
            layout.setHorizontalComponentAlignment(Alignment.CENTER, label);
            flightSeatsDialog.add(layout);
            flightSeatsDialog.open();
        }
    }

    private Grid.Column<FlightSeatDTO> createFareColumn() {
        return flightSeatGrid.addColumn(FlightSeatDTO::getFare).setHeader("fare");
    }

    private Grid.Column<FlightSeatDTO> createNumberSeatColumn() {
        return flightSeatGrid.addColumn(e -> e.getSeat().getSeatNumber()).setHeader("seat number");
    }

    private Grid.Column<FlightSeatDTO> createCategorySeatColumn() {
        return flightSeatGrid.addColumn(FlightSeatDTO::getCategory).setHeader("category");
    }


    private Grid.Column<FlightDTO> createEditColumn() {
        return grid.addComponentColumn(flight -> {
            Button updateButton = new Button("Update");
            updateButton.addClickListener(e -> {


                if (editor.isOpen())
                    editor.cancel();


                grid.getEditor().editItem(flight);
            });
            return updateButton;
        });
    }

    private Grid.Column<FlightDTO> createDeleteColumn() {
        return grid.addComponentColumn(flight -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                if (grid.getDataProvider().isInMemory() && grid.getDataProvider().getClass() == ListDataProvider.class) {
                    ListDataProvider<FlightDTO> dataProvider = (ListDataProvider<FlightDTO>) grid.getDataProvider();
                    try {
                        flightClient.deleteFlightById(flight.getId());
                    } catch (Exception exception) {
                        Notification.show("Error of delete flight",
                                3000, Notification.Position.TOP_CENTER);
                        return;
                    }
                    dataProvider.getItems().remove(flight);
                }
                grid.getDataProvider().refreshAll();
            });
            return deleteButton;
        }).setWidth("80px").setFlexGrow(0);
    }

    private Binder<FlightDTO> createBinder() {
        Binder<FlightDTO> binder = new Binder<>(FlightDTO.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        return binder;
    }

    private void createIdField(Binder<FlightDTO> binder,
                               ValidationMessage idValidationMessage,
                               Grid.Column<FlightDTO> idColumn) {
        IntegerField idField = new IntegerField();
        idField.setWidthFull();
        idField.setReadOnly(true);
        binder.forField(idField)
                .asRequired("Id must not be empty")
                .withStatusLabel(idValidationMessage)
                .bind(FlightDTO -> FlightDTO.getId().intValue(),
                        (FlightDTO, Integer) -> FlightDTO.setId(Integer.longValue()));
        idColumn.setEditorComponent(idField);
    }

    private void createCodeField(Binder<FlightDTO> binder,
                                 ValidationMessage codeValidationMessage,
                                 Grid.Column<FlightDTO> codeColumn) {
        TextField codeField = new TextField();
        binder.forField(codeField).asRequired("Flight code must not be empty")
                .withStatusLabel(codeValidationMessage)
                .withValidator(number -> number.length() >= 2 && number.length() <= 15,
                        "Length of Flight code should be between 2 and 15 characters")
                .bind(FlightDTO::getCode, FlightDTO::setCode);
        codeColumn.setEditorComponent(codeField);
    }

    private void createAirportFromField(Binder<FlightDTO> binder,
                                        ValidationMessage airportFromValidationMessage,
                                        Grid.Column<FlightDTO> airportFromColumn) {
        ComboBox<Airport> airportFromField = new ComboBox<>();
        airportFromField.setItems(Airport.values());
        airportFromField.setWidthFull();
        binder.forField(airportFromField).asRequired("Airport must not be empty")
                .withStatusLabel(airportFromValidationMessage)
                .bind(FlightDTO::getAirportFrom, FlightDTO::setAirportFrom);
        airportFromColumn.setEditorComponent(airportFromField);
    }

    private void createAirportToField(Binder<FlightDTO> binder,
                                      ValidationMessage airportToValidationMessage,
                                      Grid.Column<FlightDTO> airportToColumn) {
        ComboBox<Airport> airportToField = new ComboBox<>();
        airportToField.setItems(Airport.values());
        airportToField.setWidthFull();
        binder.forField(airportToField).asRequired("Airport must not be empty")
                .withStatusLabel(airportToValidationMessage)
                .bind(FlightDTO::getAirportFrom, FlightDTO::setAirportFrom);
        airportToColumn.setEditorComponent(airportToField);
    }

    private void createDepartureDateTimeField(Binder<FlightDTO> binder,
                                              ValidationMessage departureDateTimeValidationMessage,
                                              Grid.Column<FlightDTO> departureDateTimeColumn) {
        DateTimePicker departureDateTimeField = new DateTimePicker();
        departureDateTimeField.setWidthFull();
        binder.forField(departureDateTimeField)
                .asRequired("Field must not be empty")
                .withStatusLabel(departureDateTimeValidationMessage)
                .bind(FlightDTO::getDepartureDateTime, FlightDTO::setDepartureDateTime);
        departureDateTimeColumn.setEditorComponent(departureDateTimeField);
    }

    private void createArrivalDateTimeField(Binder<FlightDTO> binder,
                                            ValidationMessage arrivalDateTimeValidationMessage,
                                            Grid.Column<FlightDTO> arrivalDateTimeColumn) {
        DateTimePicker arrivalDateTimeField = new DateTimePicker();
        arrivalDateTimeField.setWidthFull();
        binder.forField(arrivalDateTimeField)
                .asRequired("Field must not be empty")
                .withStatusLabel(arrivalDateTimeValidationMessage)
                .bind(FlightDTO::getArrivalDateTime, FlightDTO::setArrivalDateTime);
        arrivalDateTimeColumn.setEditorComponent(arrivalDateTimeField);
    }

    private void createAircraftIdField(Binder<FlightDTO> binder,
                                       ValidationMessage aircraftIdValidationMessage,
                                       Grid.Column<FlightDTO> aircraftIdColumn) {
        IntegerField aircraftIdField = new IntegerField();
        aircraftIdField.setWidthFull();
        binder.forField(aircraftIdField)
                .asRequired("Aircraft Id must not be empty")
                .withStatusLabel(aircraftIdValidationMessage)
                .bind(FlightDTO -> FlightDTO.getAircraftId().intValue(),
                        (FlightDTO, Integer) -> FlightDTO.setAircraftId(Integer.longValue()));
        aircraftIdColumn.setEditorComponent(aircraftIdField);
    }

    private void createFlightStatusField(Binder<FlightDTO> binder,
                                         ValidationMessage flightStatusValidationMessage,
                                         Grid.Column<FlightDTO> flightStatusColumn) {
        ComboBox<FlightStatus> flightStatusField = new ComboBox<>();
        flightStatusField.setItems(FlightStatus.values());
        flightStatusField.setWidthFull();
        binder.forField(flightStatusField).asRequired("Flight Status must not be empty")
                .withStatusLabel(flightStatusValidationMessage)
                .bind(FlightDTO::getFlightStatus, FlightDTO::setFlightStatus);
        flightStatusColumn.setEditorComponent(flightStatusField);
    }

    private void addEditorListeners() {
        editor.addSaveListener(e -> {


            if (e.getItem().getCode().length() != 6) {
                Notification.show("Flight code must be 6 symbols long",
                        3000, Notification.Position.TOP_CENTER);
                return;
            }
            if (!e.getItem().getCode().equals(e.getItem().getAirportFrom().getAirportInternalCode()
                    .concat(e.getItem().getAirportTo().getAirportInternalCode())
            )) {
                Notification.show("Flight code must contain \"Airport from\" code and \"Airport to\" code",
                        3000, Notification.Position.TOP_CENTER);
                return;
            }
            if (e.getItem().getFlightStatus() == null) {
                Notification.show("Flight status must be filled", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            if (e.getItem().getDepartureDateTime() == null || e.getItem().getArrivalDateTime() == null) {
                Notification.show("Fields \"Departure date and time\" and \"Arrival date and time\" must be filled",
                        3000, Notification.Position.TOP_CENTER);
                return;
            }
            if (e.getItem().getDepartureDateTime()
                    .isAfter(e.getItem().getArrivalDateTime())) {
                Notification.show("\"Departure date and time\" must be early then \"Arrival date and time\"", 3000, Notification.Position.TOP_CENTER);
                return;
            }


            try {
                var a = aircraftClient.getAircraftDTOById(e.getItem().getAircraftId());
            } catch (FeignException.NotFound ex) {
                Notification.show("Aircraft with id = " + e.getItem().getAircraftId() + " not exists, ",
                        3000, Notification.Position.TOP_CENTER);
                return;
            }


            try {
                flightClient.updateFlightById(e.getItem().getId(), e.getItem());
            } catch (Exception exception) {
                Notification.show("Server error", 3000, Notification.Position.TOP_CENTER);
            }


            grid.getDataProvider().refreshAll();
        });
    }

    private Tabs createTabs(Div contentContainer) {
        Tabs tabs = new Tabs();
        Tab tableTab = new Tab("Flight table");
        FormLayout formLayout = new FormLayout();
        Tab createTab = createCreateTab(formLayout);
        contentContainer.add(grid);
        tabs.add(tableTab, createTab);
        tabs.setSelectedTab(tableTab);
        tabs.addSelectedChangeListener(event -> {
            Tab selectedTab = tabs.getSelectedTab();
            if (selectedTab == tableTab) {
                contentContainer.removeAll();
                contentContainer.add(grid);
                updateButton.setVisible(true);
                cancelButton.setVisible(true);
                nextButton.setVisible(true);
                previousButton.setVisible(true);
                refreshButton.setVisible(true);
                searchByIdButton.setVisible(true);
                idSearchField.setVisible(true);
                searchByDestinationsAndDatesButton.setVisible(true);
                cityFromSearchByDestinationsAndDatesField.setVisible(true);
                cityToSearchByDestinationsAndDatesField.setVisible(true);
                dateStartSearchByDestinationsAndDatesField.setVisible(true);
                dateFinishSearchByDestinationsAndDatesField.setVisible(true);
            } else if (selectedTab == createTab) {
                contentContainer.removeAll();
                contentContainer.add(formLayout);
                updateButton.setVisible(false);
                cancelButton.setVisible(false);
                nextButton.setVisible(false);
                previousButton.setVisible(false);
                refreshButton.setVisible(false);
                searchByIdButton.setVisible(false);
                idSearchField.setVisible(false);
                searchByDestinationsAndDatesButton.setVisible(false);
                cityFromSearchByDestinationsAndDatesField.setVisible(false);
                cityToSearchByDestinationsAndDatesField.setVisible(false);
                dateStartSearchByDestinationsAndDatesField.setVisible(false);
                dateFinishSearchByDestinationsAndDatesField.setVisible(false);
                grid.getDataProvider().refreshAll();
            }
        });
        return tabs;
    }

    private Tab createCreateTab(FormLayout formLayout) {
        Tab createTab = new Tab("Create flight");

        TextField code = new TextField("Flight code");
        code.setWidth("200px");

        ComboBox<Airport> airportFrom = new ComboBox<>("Airport from");
        airportFrom.setWidth("200px");
        airportFrom.setItems(Airport.values());
        airportFrom.setRenderer(new TextRenderer<Airport>(e -> e.getAirportName() + " (" + e.getCity() + ")"));

        ComboBox<Airport> airportTo = new ComboBox<>("Airport to");
        airportTo.setWidth("200px");
        airportTo.setItems(Airport.values());
        airportTo.setRenderer(new TextRenderer<Airport>(e -> e.getAirportName() + " (" + e.getCity() + ")"));

        IntegerField aircraftId = new IntegerField("Aircraft id");
        aircraftId.setWidth("200px");

        ComboBox<FlightStatus> flightStatus = new ComboBox<>("Flight status");
        flightStatus.setWidth("200px");
        flightStatus.setItems(FlightStatus.values());


        DateTimePicker departureDateTime = new DateTimePicker("Departure date and time");
        departureDateTime.setWidth("400px");
        DateTimePicker arrivalDateTime = new DateTimePicker("Arrival date and time");
        arrivalDateTime.setWidth("400px");

        Button createButton = new Button("Create");
        createButton.setWidth("240px");

        HorizontalLayout layout1 = new HorizontalLayout();
        layout1.add(code, airportFrom, airportTo, aircraftId, flightStatus);
        HorizontalLayout layout2 = new HorizontalLayout();
        layout2.add(departureDateTime, arrivalDateTime, createButton);
        layout2.setAlignItems(FlexComponent.Alignment.END);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(layout1, layout2);

        formLayout.add(verticalLayout);
        createButton.addClickListener(event -> {

            if (code.getValue().length() != 6) {
                Notification.show("Flight code must be 6 symbols long",
                        3000, Notification.Position.TOP_CENTER);
                return;
            }
            if (!code.getValue().equals(airportFrom.getValue().getAirportInternalCode()
                    .concat(airportTo.getValue().getAirportInternalCode())
            )) {
                Notification.show("Flight code must contain \"Airport from\" code and \"Airport to\" code",
                        3000, Notification.Position.TOP_CENTER);
                return;
            }
            if (flightStatus.getValue() == null) {
                Notification.show("Flight status must be filled", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            if (departureDateTime.getValue() == null || arrivalDateTime.getValue() == null) {
                Notification.show("Fields \"Departure date and time\" and \"Arrival date and time\" must be filled",
                        3000, Notification.Position.TOP_CENTER);
                return;
            }
            if (departureDateTime.getValue()
                    .isAfter(arrivalDateTime.getValue())) {
                Notification.show("\"Departure date and time\" must be early then \"Arrival date and time\"", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            try {
                var a = aircraftClient.getAircraftDTOById(aircraftId.getValue().longValue());
            } catch (FeignException.NotFound ex) {
                Notification.show("Aircraft with id = " + aircraftId.getValue().toString() + " not exists, ",
                        3000, Notification.Position.TOP_CENTER);
                return;
            }
            FlightDTO flightDTO = new FlightDTO();
            flightDTO.setId(0L);
            flightDTO.setCode(code.getValue());
            flightDTO.setAirportFrom(airportFrom.getValue());
            flightDTO.setAirportTo(airportTo.getValue());
            flightDTO.setDepartureDateTime(departureDateTime.getValue());
            flightDTO.setArrivalDateTime(arrivalDateTime.getValue());
            flightDTO.setAircraftId(aircraftId.getValue().longValue());
            flightDTO.setFlightStatus(flightStatus.getValue());
            FlightDTO savedFlight = flightClient.createFlight(flightDTO).getBody();
            dataSource.add(savedFlight);
            code.clear();
            airportFrom.clear();
            airportTo.clear();
            departureDateTime.clear();
            arrivalDateTime.clear();
            aircraftId.clear();
            flightStatus.clear();
            grid.getDataProvider().refreshAll();
            Notification.show("Flight created successfully.", 3000, Notification.Position.TOP_CENTER);
        });
        return createTab;
    }
}
