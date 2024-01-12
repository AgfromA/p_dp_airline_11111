package app.controllers.view;

import app.clients.FlightSeatClient;
import app.dto.FlightSeatDTO;
import app.dto.SeatDTO;
import app.enums.CategoryType;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Route(value = "flightSeats", layout = MainLayout.class)
public class FlightSeatView extends VerticalLayout {
    private final Grid<FlightSeatDTO> grid = new Grid<>(FlightSeatDTO.class, false);
    private final Editor<FlightSeatDTO> editor = grid.getEditor();
    private ResponseEntity<List<FlightSeatDTO>> response;
    private final List<SeatDTO> dataSourceAll;
    private final FlightSeatClient flightSeatClient;
    private final List<FlightSeatDTO> dataSource;
    private final Button updateButton;
    private final Button cancelButton;
    private final Button nextButton;
    private final Button endButton;
    private final Button previousButton;
    private final Button startButton;
    private final Button refreshButton;
    private final Button searchButtonById;
    private final Button searchButtonByFare;
    private Integer currentPage;
    private final IntegerField idSearchField;
    private final IntegerField minFareSearchField;
    private final IntegerField maxFareSearchField;
    private boolean isSearchById;
    private boolean isSearchByFare;
    private Integer maxPages;

    public FlightSeatView(FlightSeatClient flightSeatClient) {
        this.flightSeatClient = flightSeatClient;
        this.currentPage = 0;
        Optional<Long> id = null;
        this.response = flightSeatClient.getAllFlightSeats(currentPage, 10, id, null, null);
        this.dataSourceAll = flightSeatClient.getAllSeat().getBody();
        this.isSearchByFare = false;
        this.isSearchById = false;

        List<FlightSeatDTO> flightSeatDTOList = flightSeatClient.getAllFlightSeats(null, null, id, null, null).getBody();
        this.maxPages = (int) Math.ceil((double) flightSeatDTOList.size() / 10);
        this.dataSource = response.getBody();

        ValidationMessage idValidationMessage = new ValidationMessage();
        ValidationMessage fareValidationMessage = new ValidationMessage();
        ValidationMessage isRegisteredValidationMessage = new ValidationMessage();
        ValidationMessage isSoldValidationMessage = new ValidationMessage();
        ValidationMessage isBookedValidationMessage = new ValidationMessage();
        ValidationMessage flightIdValidationMessage = new ValidationMessage();
        ValidationMessage seatValidationMessage = new ValidationMessage();
        ValidationMessage categoryValidationMessage = new ValidationMessage();

        Grid.Column<FlightSeatDTO> idColumn = createIdColumn();
        Grid.Column<FlightSeatDTO> fareColumn = createFareColumn();
        Grid.Column<FlightSeatDTO> isRegisteredColumn = createIsRegisteredColumn();
        Grid.Column<FlightSeatDTO> isSoldColumn = createIsSoldColumn();
        Grid.Column<FlightSeatDTO> isBookedColumn = createIsBookedColumn();
        Grid.Column<FlightSeatDTO> flightIdColumn = createFlightIdColumn();
        Grid.Column<FlightSeatDTO> seatColumn = createSeatColumn();
        Grid.Column<FlightSeatDTO> categoryColumn = createCategoryColumn();
        Grid.Column<FlightSeatDTO> updateColumn = createEditColumn();
        createDeleteColumn();

        Binder<FlightSeatDTO> binder = createBinder();

        createIdField(binder, idValidationMessage, idColumn);
        createFareField(binder, fareValidationMessage, fareColumn);
        createIsRegisteredField(binder, isRegisteredValidationMessage, isRegisteredColumn);
        createIsSoldField(binder, isSoldValidationMessage, isSoldColumn);
        createIsBookedField(binder, isBookedValidationMessage, isBookedColumn);
        createFlightIdField(binder, flightIdValidationMessage, flightIdColumn);
        createSeatField(binder, seatValidationMessage, seatColumn);

        updateButton = new Button("Update", e -> editor.save());
        cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);

        nextButton = new Button(VaadinIcon.ARROW_RIGHT.create(), e -> nextPage());
        previousButton = new Button(VaadinIcon.ARROW_LEFT.create(), e -> previousPage());
        refreshButton = createRefreshButton();

        searchButtonById = createSearchButtonById();
        idSearchField = createSearchByIdField();

        searchButtonByFare = createSearchButtonByFare();
        minFareSearchField = createSearchByMinFareField();
        maxFareSearchField = createSearchByMaxFareField();

        endButton = new Button(VaadinIcon.ARROW_CIRCLE_RIGHT_O.create(), e -> endPage());
        startButton = new Button(VaadinIcon.ARROW_CIRCLE_LEFT_O.create(), e -> startPage());

        HorizontalLayout actions = new HorizontalLayout(updateButton, cancelButton);
        actions.setPadding(false);
        updateColumn.setEditorComponent(actions);

        HorizontalLayout changedPages = new HorizontalLayout(refreshButton, startButton, previousButton, nextButton, endButton);
        HorizontalLayout searchLayoutById = new HorizontalLayout(idSearchField, searchButtonById);
        HorizontalLayout searchLayoutByFare = new HorizontalLayout(minFareSearchField, maxFareSearchField, searchButtonByFare);

        addEditorListeners();

        grid.setItems(dataSource);
        grid.setAllRowsVisible(true);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        addTheme();

        Div contentContainer = new Div();
        contentContainer.setSizeFull();
        Tabs tabs = createTabs(contentContainer);

        add(tabs
                , contentContainer
                , idValidationMessage
                , fareValidationMessage
                , isRegisteredValidationMessage
                , isSoldValidationMessage
                , isBookedValidationMessage
                , flightIdValidationMessage
                , seatValidationMessage
                , categoryValidationMessage
                , changedPages
                , searchLayoutById
                , searchLayoutByFare
        );
    }

    private void nextPage() {
        if (isSearchById) {
            return;
        }
        if (isSearchByFare) {
            return;
        }
        if (currentPage < maxPages) {
            currentPage++;
            updateGridData();
        }
    }

    private void endPage() {
        if (isSearchById) {
            return;
        }
        if (isSearchByFare) {
            return;
        }
        currentPage = maxPages;
        updateGridData();
    }

    private void startPage() {
        if (isSearchById) {
            return;
        }
        if (isSearchByFare) {
            return;
        }
        currentPage = 0;
        updateGridData();
    }

    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateGridData();
        }
    }

    private Button createRefreshButton() {
        return new Button("Refresh", e -> {
            isSearchById = false;
            isSearchByFare = false;
            idSearchField.clear();
            minFareSearchField.clear();
            maxFareSearchField.clear();
            currentPage = 0;
            updateGridData();
        });
    }

    private Button createSearchButtonById() {
        return new Button("Search", e -> {
            if (idSearchField.isEmpty() || idSearchField.getValue() <= 0) {
                Notification.show("Id must be a valid number", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            searchById();
            idSearchField.clear();
        });
    }

    private IntegerField createSearchByIdField() {
        IntegerField searchField = new IntegerField();
        searchField.setPlaceholder("Flight Seat Id");
        searchField.addKeyPressListener(Key.ENTER, e -> searchButtonById.click());
        return searchField;
    }

    private void searchById() {
        if (isFoundSeatById(idSearchField.getValue().longValue())) {
            dataSource.clear();
            isSearchById = true;
            currentPage = 0;
            maxPages = 1;
            dataSource.add(flightSeatClient.getFlightSeatById(idSearchField.getValue().longValue()).getBody());
            grid.getDataProvider().refreshAll();
        }
    }

    private boolean isFoundSeatById(Long id) {
        try {
            dataSource.add(flightSeatClient.getFlightSeatById(id).getBody());
            return true;
        } catch (FeignException.NotFound ex) {
            log.error(ex.getMessage());
            Notification.show(" Flight Seat with id = " + id + " not found.", 3000, Notification.Position.TOP_CENTER);
        }
        return false;
    }

    private Button createSearchButtonByFare() {
        return new Button("Search", e -> {
            if (minFareSearchField.isEmpty() || minFareSearchField.getValue() <= 0
                    || maxFareSearchField.isEmpty() || maxFareSearchField.getValue() <= 0) {
                Notification.show("Fare must be a valid number", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            searchByFare();
            minFareSearchField.clear();
            maxFareSearchField.clear();
        });
    }

    private IntegerField createSearchByMinFareField() {
        IntegerField searchField = new IntegerField();
        searchField.setPlaceholder("Min Fare Flight Seat");
        searchField.setMin(0);
        searchField.addKeyPressListener(Key.ENTER, e -> searchButtonByFare.click());
        return searchField;
    }

    private IntegerField createSearchByMaxFareField() {
        IntegerField searchField = new IntegerField();
        searchField.setPlaceholder("Max Fare Flight Seat");
        searchField.addKeyPressListener(Key.ENTER, e -> searchButtonByFare.click());
        return searchField;
    }

    private void searchByFare() {
        Optional<Long> id = null; //работает только так
        List<FlightSeatDTO> allFlightSeatDTO = flightSeatClient.getAllFlightSeats(null, null,
                id, null, null).getBody();
        Integer min = minFareSearchField.getValue();
        Integer max = maxFareSearchField.getValue();
        List<FlightSeatDTO> filteredListSeats = new ArrayList<>();
        for (FlightSeatDTO seat : allFlightSeatDTO) {
            Integer fare = seat.getFare();
            if (fare >= min && fare <= max) {
                filteredListSeats.add(seat);
            }
        }
        if (filteredListSeats.isEmpty()) {
            Notification.show(" Flight Seat with this cost not found.", 3000, Notification.Position.TOP_CENTER);
            return;
        }
        filteredListSeats.sort(Comparator.comparing(FlightSeatDTO::getFare));
        dataSource.clear();
        dataSource.addAll(filteredListSeats);
        isSearchByFare = true;
        grid.getDataProvider().refreshAll();
    }


    private Grid.Column<FlightSeatDTO> createIdColumn() {
        return grid.addColumn(flightSeatDTO -> flightSeatDTO.getId()
                .intValue()).setHeader("Id").setWidth("120px").setFlexGrow(0);
    }

    private Grid.Column<FlightSeatDTO> createFareColumn() {
        return grid.addColumn(FlightSeatDTO::getFare).setHeader("Fare").setWidth("120px").setFlexGrow(0);
    }

    private Grid.Column<FlightSeatDTO> createIsRegisteredColumn() {
        return grid.addColumn(FlightSeatDTO::getIsRegistered).setHeader("Is Registered").setWidth("120px");
    }

    private Grid.Column<FlightSeatDTO> createIsSoldColumn() {
        return grid.addColumn(FlightSeatDTO::getIsSold).setHeader("Is Sold").setWidth("120px");
    }

    private Grid.Column<FlightSeatDTO> createIsBookedColumn() {
        return grid.addColumn(FlightSeatDTO::getIsBooked).setHeader("Is Booked").setWidth("120px");
    }

    private Grid.Column<FlightSeatDTO> createFlightIdColumn() {
        return grid.addColumn(FlightSeatDTO::getFlightId).setHeader("Flight Id").setWidth("120px");
    }

    private Grid.Column<FlightSeatDTO> createSeatColumn() {
        return grid.addColumn(flightSeatDTO -> flightSeatDTO.getSeat().getSeatNumber()).setHeader("Seat").setWidth("240px");
    }

    private Grid.Column<FlightSeatDTO> createCategoryColumn() {
        return grid.addColumn(FlightSeatDTO::getCategory).setHeader("Category").setWidth("240px");
    }

    private Grid.Column<FlightSeatDTO> createEditColumn() {
        return grid.addComponentColumn(flightSeat -> {
            Button updateButton = new Button("Update");
            updateButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(flightSeat);
            });
            return updateButton;
        });
    }

    private Grid.Column<FlightSeatDTO> createDeleteColumn() {
        return grid.addComponentColumn(flightSeat -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                if (grid.getDataProvider().isInMemory() && grid.getDataProvider().getClass() == ListDataProvider.class) {
                    ListDataProvider<FlightSeatDTO> dataProvider = (ListDataProvider<FlightSeatDTO>) grid.getDataProvider();
                    if (isDeletedSeat(flightSeat)) {
                        Notification.show("Flight Seat deleted successfully.", 3000, Notification.Position.TOP_CENTER);
                        dataProvider.getItems().remove(flightSeat);
                    }
                }
                grid.getDataProvider().refreshAll();
            });
            return deleteButton;
        }).setWidth("150px").setFlexGrow(0);
    }

    private boolean isDeletedSeat(FlightSeatDTO flightSeat) {
        try {
            flightSeatClient.deleteFlightSeatById(flightSeat.getId());
            return true;
        } catch (FeignException.MethodNotAllowed feignException) {
            log.error(feignException.getMessage());
            Notification.show("Seat is locked by ???", 3000, Notification.Position.TOP_CENTER);
            return false;
        }
    }

    private Binder<FlightSeatDTO> createBinder() {
        Binder<FlightSeatDTO> binder = new Binder<>(FlightSeatDTO.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        return binder;
    }

    private void createIdField(Binder<FlightSeatDTO> binder,
                               ValidationMessage idValidationMessage,
                               Grid.Column<FlightSeatDTO> idColumn) {
        IntegerField idField = new IntegerField();
        idField.setWidthFull();
        binder.forField(idField)
                .asRequired("Id must not be empty")
                .withStatusLabel(idValidationMessage)
                .bind(flightSeatDTO -> flightSeatDTO.getId().intValue(),
                        (flightSeatDTO, integer) -> flightSeatDTO.setId(integer.longValue()));
        idColumn.setEditorComponent(idField);
    }

    private void createFareField(Binder<FlightSeatDTO> binder,
                                 ValidationMessage fareValidationMessage,
                                 Grid.Column<FlightSeatDTO> fareColumn) {
        IntegerField fareField = new IntegerField();
        fareField.setWidthFull();
        binder.forField(fareField)
                .asRequired("Fare must be positive")
                .withStatusLabel(fareValidationMessage)
                .bind(FlightSeatDTO::getFare, FlightSeatDTO::setFare);
        fareColumn.setEditorComponent(fareField);
    }

    private void createIsRegisteredField(Binder<FlightSeatDTO> binder,
                                         ValidationMessage isRegisteredValidationMessage,
                                         Grid.Column<FlightSeatDTO> isRegisteredColumn) {
        ComboBox<Boolean> isRegisteredField = new ComboBox<>();
        isRegisteredField.setItems(true, false);
        binder.forField(isRegisteredField).asRequired("Is Registered Field must be true or false")
                .withStatusLabel(isRegisteredValidationMessage)
                .bind(FlightSeatDTO::getIsRegistered, FlightSeatDTO::setIsRegistered);
        isRegisteredColumn.setEditorComponent(isRegisteredField);
    }

    private void createIsSoldField(Binder<FlightSeatDTO> binder,
                                   ValidationMessage isSoldValidationMessage,
                                   Grid.Column<FlightSeatDTO> isSoldColumn) {
        ComboBox<Boolean> isSoldField = new ComboBox<>();
        isSoldField.setItems(true, false);
        binder.forField(isSoldField).asRequired("Is Sold Field must be true or false")
                .withStatusLabel(isSoldValidationMessage)
                .bind(FlightSeatDTO::getIsSold, FlightSeatDTO::setIsSold);
        isSoldColumn.setEditorComponent(isSoldField);
    }

    private void createIsBookedField(Binder<FlightSeatDTO> binder,
                                     ValidationMessage isBookedValidationMessage,
                                     Grid.Column<FlightSeatDTO> isBookedColumn) {
        ComboBox<Boolean> isBookedField = new ComboBox<>();
        isBookedField.setItems(true, false);
        binder.forField(isBookedField).asRequired("Is Booked Field must be true or false")
                .withStatusLabel(isBookedValidationMessage)
                .bind(FlightSeatDTO::getIsBooked, FlightSeatDTO::setIsBooked);
        isBookedColumn.setEditorComponent(isBookedField);
    }

    private void createFlightIdField(Binder<FlightSeatDTO> binder,
                                     ValidationMessage flightIdValidationMessage,
                                     Grid.Column<FlightSeatDTO> flightIdColumn) {
        IntegerField flightIdField = new IntegerField();
        flightIdField.setWidthFull();
        binder.forField(flightIdField)
                .asRequired("Flight Id must not be empty")
                .withStatusLabel(flightIdValidationMessage)
                .bind(flightSeatDTO -> flightSeatDTO.getFlightId().intValue(),
                        (flightSeatDTO, integer) -> flightSeatDTO.setFlightId(integer.longValue()));
        flightIdColumn.setEditorComponent(flightIdField);
    }

    private void createSeatField(Binder<FlightSeatDTO> binder,
                                 ValidationMessage seatValidationMessage,
                                 Grid.Column<FlightSeatDTO> seatColumn) {
        ComboBox<SeatDTO> seatField = new ComboBox<>();
        seatField.setItems(dataSourceAll);
        seatField.setItemLabelGenerator(seatDTO ->
                "Seat id " + seatDTO.getId() + " - " + seatDTO.getSeatNumber() + " ("
                        + "IsNearEmergencyExit - " + seatDTO.getIsNearEmergencyExit()
                        + ", IsLockedBack - " + seatDTO.getIsLockedBack() + ") "
                        + "Category - " + seatDTO.getCategory() + " Aircraft id " + seatDTO.getAircraftId());
        seatField.setWidthFull();
        binder.forField(seatField).asRequired("Seat number must not be empty")
                .withStatusLabel(seatValidationMessage)
                .bind(FlightSeatDTO::getSeat, FlightSeatDTO::setSeat);
        seatColumn.setEditorComponent(seatField);
    }

    private void addEditorListeners() {
        editor.addSaveListener(e -> {
            if (isEditedFlightSeat(e.getItem().getId(), e.getItem())) {
                Notification.show("Flight Seat edited successfully.", 3000, Notification.Position.TOP_CENTER);
                grid.getDataProvider().refreshAll();
            } else {
                updateGridData();
            }
        });
    }

    private boolean isEditedFlightSeat(Long id, FlightSeatDTO flightSeatDTO) {
        try {
            flightSeatClient.updateFlightSeatById(id, flightSeatDTO);
            return true;
        } catch (FeignException.BadRequest ex) {
            log.error(ex.getMessage());
            Notification.show("Aircraft with id = " + flightSeatDTO.getFlightId() + " not found.", 3000, Notification.Position.TOP_CENTER);
        } catch (FeignException.NotFound ex) {
            log.error(ex.getMessage());
            Notification.show("Flight Seat with id = " + flightSeatDTO.getId() + " not found.", 3000, Notification.Position.TOP_CENTER);
        }
        return false;
    }

    private void updateGridData() {
        dataSource.clear();
        isSearchById = false;
        isSearchByFare = false;
        Optional<Long> id = null;
        response = flightSeatClient.getAllFlightSeats(currentPage, 10, id, null, null);
        List<FlightSeatDTO> flightSeatDTOList = flightSeatClient.getAllFlightSeats(null, null, id, null, null).getBody();
        this.maxPages = (int) Math.ceil((double) flightSeatDTOList.size() / 10);
        dataSource.addAll(response.getBody());
        grid.getDataProvider().refreshAll();
    }

    private void addTheme() {
        getThemeList().clear();
        getThemeList().add("spacing-s");
    }

    private Tabs createTabs(Div contentContainer) {
        Tabs tabs = new Tabs();

        Tab tableTab = new Tab("Flight Seats table");
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
                endButton.setVisible(true);
                startButton.setVisible(true);
                searchButtonById.setVisible(true);
                idSearchField.setVisible(true);
                searchButtonByFare.setVisible(true);
                minFareSearchField.setVisible(true);
                maxFareSearchField.setVisible(true);
            } else if (selectedTab == createTab) {
                contentContainer.removeAll();
                contentContainer.add(formLayout);
                updateButton.setVisible(false);
                cancelButton.setVisible(false);
                nextButton.setVisible(false);
                previousButton.setVisible(false);
                refreshButton.setVisible(false);
                endButton.setVisible(false);
                startButton.setVisible(false);
                searchButtonById.setVisible(false);
                idSearchField.setVisible(false);
                searchButtonByFare.setVisible(false);
                minFareSearchField.setVisible(false);
                maxFareSearchField.setVisible(false);
                grid.getDataProvider().refreshAll();
            }
        });
        return tabs;
    }

    private Tab createCreateTab(FormLayout formLayout) {
        Tab createTab = new Tab("Create flightSeat");

        IntegerField fareField = new IntegerField("Fare");
        ComboBox<Boolean> isRegisteredField = new ComboBox<>("Is Registered");
        ComboBox<Boolean> isSoldField = new ComboBox<>("Is Sold");
        ComboBox<Boolean> isBookedField = new ComboBox<>("Is Booked");
        IntegerField flightIdField = new IntegerField("Flight Id");
        ComboBox<SeatDTO> seatField = new ComboBox<>("Seat");
        ComboBox<CategoryType> categoryField = new ComboBox<>("Category");

        isRegisteredField.setItems(true, false);
        isSoldField.setItems(true, false);
        isBookedField.setItems(true, false);
        seatField.setItems(dataSourceAll);
        seatField.setItemLabelGenerator(seatDTO ->
                "Seat id " + seatDTO.getId() + " - " + seatDTO.getSeatNumber() + " ("
                        + "IsNearEmergencyExit - " + seatDTO.getIsNearEmergencyExit()
                        + ", IsLockedBack - " + seatDTO.getIsLockedBack() + ") "
                        + "Category - " + seatDTO.getCategory() + " Aircraft id " + seatDTO.getAircraftId());
        categoryField.setItems(CategoryType.values());

        Button createButton = new Button("Create");

        formLayout.add(fareField, isRegisteredField, isSoldField, isBookedField, flightIdField, seatField, createButton);
        createButton.addClickListener(event -> {
            if (fareField.getValue() < 0 || flightIdField.getValue() < 0) {
                Notification.show("Please fill in the required field with a positive number.", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            if (fareField.isEmpty() || isRegisteredField.isEmpty() || isSoldField.isEmpty()
                    || isBookedField.isEmpty() || flightIdField.isEmpty() || seatField.isEmpty()) {
                Notification.show("Please fill in all required fields correctly.", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            FlightSeatDTO flightSeatDTO = new FlightSeatDTO();
            flightSeatDTO.setFare(fareField.getValue());
            flightSeatDTO.setIsRegistered(isRegisteredField.getValue());
            flightSeatDTO.setIsSold(isSoldField.getValue());
            flightSeatDTO.setIsBooked(isBookedField.getValue());
            flightSeatDTO.setFlightId(flightIdField.getValue().longValue());
            flightSeatDTO.setSeat(seatField.getValue());
            flightSeatDTO.setCategory(seatField.getValue().getCategory());

            if (isCreatedSeat(flightSeatDTO)) {
                fareField.clear();
                isRegisteredField.clear();
                isSoldField.clear();
                isBookedField.clear();
                flightIdField.clear();
                seatField.clear();
                categoryField.clear();
                grid.getDataProvider().refreshAll();
                Notification.show("Flight Seat created successfully.", 3000, Notification.Position.TOP_CENTER);
            }
        });
        return createTab;
    }

    private boolean isCreatedSeat(FlightSeatDTO flightSeatDTO) {
        try {
            FlightSeatDTO savedFlightSeat = flightSeatClient.createFlightSeat(flightSeatDTO).getBody();
            dataSource.add(savedFlightSeat);
            return true;
        } catch (FeignException.BadRequest ex) {
            log.error(ex.getMessage());
            Notification.show("Flight with id = " + flightSeatDTO.getFlightId() + " not found.", 3000, Notification.Position.TOP_CENTER);
        }
        return false;
    }
}