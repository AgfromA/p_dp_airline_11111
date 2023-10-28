package app.controllers.view;

import app.clients.FlightClient;
import app.dto.FlightDTO;
import app.enums.Airport;
import app.enums.FlightStatus;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "flight", layout = MainLayout.class)
public class FlightView extends VerticalLayout {

    private final FlightClient flightClient;
    private Integer currentPage;
    private Integer maxPages;
    private List<FlightDTO> dataSource;

    private final Grid<FlightDTO> grid = new Grid<>(FlightDTO.class, false);
    private final Editor<FlightDTO> editor = grid.getEditor();

    private final Button updateButton;
    private final Button cancelButton;
    private final Button nextButton;
    private final Button previousButton;
    private final Button refreshButton;
    private final Button searchButtonById;
    private final IntegerField idSearchField;
    private boolean isSearchById;

    public FlightView(FlightClient flightClient) {
        this.flightClient = flightClient;
        currentPage = 0;

        PageRequest pageable = PageRequest.of(currentPage, 10, Sort.by("id").ascending());
        isSearchById = false;
        maxPages = flightClient.getAllPagesFlightsByDestinationsAndDates(
                null, null, null, null, pageable).getBody().getTotalPages() - 1;
        dataSource = flightClient.getAllPagesFlightsByDestinationsAndDates(
                null,
                null,
                null,
                null,
                pageable).getBody().stream().collect(Collectors.toList());

        System.out.println(flightClient.getAllPagesFlightsByDestinationsAndDates(
                null,
                null,
                null,
                null,
                pageable).getBody().stream().map(e -> e.getId()).collect(Collectors.toList()));

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
        Grid.Column<FlightDTO> updateColumn = createEditColumn();
        createDeleteColumn();

        Binder<FlightDTO> binder = createBinder();

        createIdField(binder, idValidationMessage, idColumn);
        createCodeField(binder, codeValidationMessage, codeColumn);
        createAirportFromField(binder, airportFromValidationMessage, airportFromColumn);
        createAirportToField(binder, airportToValidationMessage, airportToColumn);
        createDepartureDateTimeField(binder, departureDateTimeValidationMessage, departureDateTimeColumn);
        createArrivalDateTimeField(binder, arrivalDateTimeValidationMessage, arrivalDateTimeColumn);
        createAircraftIdField(binder, aircraftIdValidationMessage, aircraftIdColumn);
        createFlightStatusField(binder, flightStatusValidationMessage, flightStatusColumn);


        updateButton = new Button("Update", e -> editor.save());
        cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        previousButton = new Button(VaadinIcon.ARROW_LEFT.create(), e -> previousPage());
        nextButton = new Button(VaadinIcon.ARROW_RIGHT.create(), e -> nextPage());
        refreshButton = createRefreshButton();
        searchButtonById = createSearchButtonById();
        idSearchField = createSearchByIdField();

        addEditorListeners();

        grid.setItems(dataSource);

        addTheme();


        Div contentContainer = new Div();
        contentContainer.setSizeFull();
        Tabs tabs = createTabs(contentContainer);

        HorizontalLayout actions = new HorizontalLayout(updateButton, cancelButton);
        actions.setPadding(false);
        updateColumn.setEditorComponent(actions);
        HorizontalLayout changedPages = new HorizontalLayout(refreshButton, previousButton, nextButton);
        HorizontalLayout searchLayoutById = new HorizontalLayout(idSearchField, searchButtonById);


        add(tabs
                , contentContainer
                , idValidationMessage
                , idValidationMessage
                , codeValidationMessage
                , airportFromValidationMessage
                , airportToValidationMessage
                , departureDateTimeValidationMessage
                , arrivalDateTimeValidationMessage
                , aircraftIdValidationMessage
                , flightStatusValidationMessage
                , changedPages
                , searchLayoutById
        );
    }

    private IntegerField createSearchByIdField() {
        IntegerField searchField = new IntegerField();
        searchField.setPlaceholder("Flight Id");
        searchField.addKeyPressListener(Key.ENTER, e -> searchButtonById.click());
        return searchField;
    }

    private Button createSearchButtonById() {
        return new Button("Search", e -> {
            if (idSearchField.isEmpty() || idSearchField.getValue() <= 0) {
                Notification.show("Id must be a valid number", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            isSearchById = true;
            currentPage = 0;
            maxPages = 1;
            searchById();
            idSearchField.clear();
        });
    }


    private void addTheme() {
        getThemeList().clear();
        getThemeList().add("spacing-s");
    }

    private Button createRefreshButton() {
        isSearchById = false;
        return new Button("Refresh", e -> {
            currentPage = 0;
            refreshGridData();
        });
    }


    private void nextPage() {
        if (isSearchById) {
            return;
        }
        if (currentPage < maxPages) {
            currentPage++;
            defaultCurrentPageOfFlights();
        }
    }

    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            defaultCurrentPageOfFlights();
        }
    }

    private void refreshGridData() {
        isSearchById = false;
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
        maxPages = flightClient.getAllPagesFlightsByDestinationsAndDates(
                null, null, null, null, pageable).getBody().getTotalPages() - 1;
        dataSource = flightClient.getAllPagesFlightsByDestinationsAndDates(
                null,
                null,
                null,
                null,
                pageable).getBody().stream().collect(Collectors.toList());
        grid.getDataProvider().refreshAll();
        grid.setItems(dataSource);
    }

    private Grid.Column<FlightDTO> createIdColumn() {
        return grid.addColumn(FlightDTO -> FlightDTO.getId()
                .intValue()).setHeader("Id").setWidth("50px").setFlexGrow(0);
    }

    private Grid.Column<FlightDTO> createCodeColumn() {
        return grid.addColumn(FlightDTO::getCode).setHeader("Flight code").setWidth("50px");
    }

    private Grid.Column<FlightDTO> createAirportFromColumn() {
        return grid.addColumn(FlightDTO::getAirportFrom).setHeader("Airport From Id").setWidth("50px");
    }

    private Grid.Column<FlightDTO> createAirportToColumn() {
        return grid.addColumn(FlightDTO::getAirportTo).setHeader("Airport To Id").setWidth("50px");
    }

    private Grid.Column<FlightDTO> createDepartureDateTimeColumn() {
        return grid.addColumn(FlightDTO::getDepartureDateTime).setHeader("Departure Date And Time").setWidth("200px");
    }

    private Grid.Column<FlightDTO> createArrivalDateTimeColumn() {
        return grid.addColumn(FlightDTO::getArrivalDateTime).setHeader("Arrival Date And Time").setWidth("200px");
    }

    private Grid.Column<FlightDTO> createAircraftIdColumn() {
        return grid.addColumn(FlightDTO::getAircraftId).setHeader("Aircraft Id").setWidth("50px");
    }

    private Grid.Column<FlightDTO> createFlightStatusColumn() {
        return grid.addColumn(FlightDTO::getFlightStatus).setHeader("Flight Status").setWidth("120px");
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
                    flightClient.deleteFlightById(flight.getId());
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
        codeField.setWidthFull();
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
            flightClient.updateFlightById(e.getItem().getId(), e.getItem());
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
                searchButtonById.setVisible(true);
                idSearchField.setVisible(true);

            } else if (selectedTab == createTab) {
                contentContainer.removeAll();
                contentContainer.add(formLayout);
                updateButton.setVisible(false);
                cancelButton.setVisible(false);
                nextButton.setVisible(false);
                previousButton.setVisible(false);
                refreshButton.setVisible(false);
                grid.getDataProvider().refreshAll();
            }
        });
        return tabs;
    }

    private Tab createCreateTab(FormLayout formLayout) {
        return new Tab("Create flight");
    }


}
