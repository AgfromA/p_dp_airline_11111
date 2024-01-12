package app.controllers.view;

import app.clients.SeatClient;
import app.dto.SeatDto;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
@Route(value = "seats", layout = MainLayout.class)
public class SeatView extends VerticalLayout {

    private final Grid<SeatDto> grid = new Grid<>(SeatDto.class, false);
    private final Editor<SeatDto> editor = grid.getEditor();
    private ResponseEntity<List<SeatDto>> response;
    private final SeatClient seatClient;
    private final List<SeatDto> dataSource;
    private final Button updateButton;
    private final Button cancelButton;
    private final Button nextButton;
    private final Button previousButton;
    private final Button searchButtonById;
    private final Button searchButtonByAircraftId;
    private final Button refreshButton;
    private final IntegerField idSearchField;
    private final IntegerField aircraftIdSearchField;
    private boolean isSearchById;
    private boolean isSearchByAircraftId;
    private Integer currentPage;
    private Integer maxPages;
    private Long searchByAircraftId;

    public SeatView(SeatClient seatClient) {
        this.seatClient = seatClient;
        currentPage = 0;
        response = seatClient.getAllSeats(currentPage, 10);
        List<SeatDto> seatDtoList = seatClient.getAllSeats(null, null).getBody();
        maxPages = (int) Math.ceil((double) seatDtoList.size() / 10);
        isSearchById = false;
        isSearchByAircraftId = false;
        dataSource = response.getBody();

        ValidationMessage idValidationMessage = new ValidationMessage();
        ValidationMessage seatNumberValidationMessage = new ValidationMessage();
        ValidationMessage isNearEmergencyExitValidationMessage = new ValidationMessage();
        ValidationMessage isLockedBackValidationMessage = new ValidationMessage();
        ValidationMessage categoryValidationMessage = new ValidationMessage();
        ValidationMessage aircraftIdValidationMessage = new ValidationMessage();

        Grid.Column<SeatDto> idColumn = createIdColumn();
        Grid.Column<SeatDto> seatNumberColumn = createSeatNumberColumn();
        Grid.Column<SeatDto> isNearEmergencyExitColumn = createIsNearEmergencyExitColumn();
        Grid.Column<SeatDto> isLockedBackColumn = createIsLockedBackColumn();
        Grid.Column<SeatDto> categoryColumn = createCategoryColumn();
        Grid.Column<SeatDto> aircraftIdColumn = createAircraftIdColumn();
        Grid.Column<SeatDto> updateColumn = createEditColumn();
        createDeleteColumn();

        Binder<SeatDto> binder = createBinder();

        createIdField(binder, idValidationMessage, idColumn);
        createSeatNumberField(binder, seatNumberValidationMessage, seatNumberColumn);
        createIsNearEmergencyExitField(binder, isNearEmergencyExitValidationMessage, isNearEmergencyExitColumn);
        createIsLockedBackField(binder, isLockedBackValidationMessage, isLockedBackColumn);
        createCategoryField(binder, categoryValidationMessage, categoryColumn);
        createAircraftIdField(binder, aircraftIdValidationMessage, aircraftIdColumn);

        updateButton = new Button("Update", e -> editor.save());
        cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        previousButton = new Button(VaadinIcon.ARROW_LEFT.create(), e -> previousPage());
        nextButton = new Button(VaadinIcon.ARROW_RIGHT.create(), e -> nextPage());
        refreshButton = createRefreshButton();
        searchButtonById = createSearchButtonById();
        searchButtonByAircraftId = createSearchButtonByAircraftId();
        idSearchField = createSearchByIdField();
        aircraftIdSearchField = createSearchByAircraftIdField();

        addEditorListeners();

        grid.setItems(dataSource);
        grid.setAllRowsVisible(true);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        addTheme();

        Div contentContainer = new Div();
        contentContainer.setSizeFull();
        Tabs tabs = createTabs(contentContainer);

        HorizontalLayout actions = new HorizontalLayout(updateButton, cancelButton);
        actions.setPadding(false);
        updateColumn.setEditorComponent(actions);
        HorizontalLayout changedPages = new HorizontalLayout(refreshButton, previousButton, nextButton);
        HorizontalLayout searchLayoutById = new HorizontalLayout(idSearchField, searchButtonById);
        HorizontalLayout searchLayoutByAircraftId = new HorizontalLayout(aircraftIdSearchField, searchButtonByAircraftId);

        add(tabs
                , contentContainer
                , idValidationMessage
                , seatNumberValidationMessage
                , isNearEmergencyExitValidationMessage
                , isLockedBackValidationMessage
                , categoryValidationMessage
                , aircraftIdValidationMessage
                , changedPages
                , searchLayoutById
                , searchLayoutByAircraftId
        );
    }

    private void nextPage() {
        if (isSearchById) {
            return;
        }
        if (currentPage < maxPages) {
            currentPage++;
            if (isSearchByAircraftId) {
                searchByAircraftId();
            } else {
                updateGridData();
            }
        }
    }

    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            if (isSearchByAircraftId) {
                searchByAircraftId();
            } else {
                updateGridData();
            }
        }
    }

    private Button createRefreshButton() {
        return new Button("Refresh", e -> {
            currentPage = 0;
            isSearchById = false;
            isSearchByAircraftId = false;
            idSearchField.clear();
            aircraftIdSearchField.clear();
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
        searchField.setPlaceholder("Seat Id");
        searchField.addKeyPressListener(Key.ENTER, e -> searchButtonById.click());
        return searchField;
    }

    private IntegerField createSearchByAircraftIdField() {
        IntegerField searchField = new IntegerField();
        searchField.setPlaceholder("Aircraft Id");
        searchField.addKeyPressListener(Key.ENTER, e -> searchButtonByAircraftId.click());
        return searchField;
    }

    private Button createSearchButtonByAircraftId() {
        return new Button("Search", e -> {
            if (aircraftIdSearchField.isEmpty() || aircraftIdSearchField.getValue() <= 0) {
                Notification.show("Aircraft Id must be a valid number", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            currentPage = 0;
            isSearchByAircraftId = true;
            searchByAircraftId = aircraftIdSearchField.getValue().longValue();
            searchByAircraftId();
            aircraftIdSearchField.clear();
        });
    }

    private void updateGridData() {
        isSearchById = false;
        dataSource.clear();
        response = seatClient.getAllSeats(currentPage, 10);
        dataSource.addAll(response.getBody());
        List<SeatDto> seatDtoList = seatClient.getAllSeats(null, null).getBody();
        maxPages = (int) Math.ceil((double) seatDtoList.size() / 10);
        grid.getDataProvider().refreshAll();
    }

    private void searchById() {
        if (isFoundSeatById(idSearchField.getValue().longValue())) {
            dataSource.clear();
            isSearchById = true;
            isSearchByAircraftId = false;
            currentPage = 0;
            maxPages = 1;
            dataSource.add(seatClient.getSeatById(idSearchField.getValue().longValue()).getBody());
            grid.getDataProvider().refreshAll();
        }
    }

    private boolean isFoundSeatById(Long id) {
        try {
            dataSource.add(seatClient.getSeatById(id).getBody());
            return true;
        } catch (FeignException.NotFound ex) {
            log.error(ex.getMessage());
            Notification.show("Seat with id = " + id + " not found.", 3000, Notification.Position.TOP_CENTER);
        }
        return false;
    }

    private void searchByAircraftId() {
        PageRequest pageable = PageRequest.of(currentPage, 10, Sort.by("id").ascending());
        if (isFoundSeatsByAircraftId(pageable, searchByAircraftId)) {
            isSearchById = false;
            dataSource.clear();
            dataSource.addAll(response.getBody());
            grid.getDataProvider().refreshAll();
            List<SeatDto> seatDtoList = seatClient.getAllSeats(null, null).getBody();
            maxPages = (int) Math.ceil((double) seatDtoList.size() / 10);
        }
    }

    private boolean isFoundSeatsByAircraftId(PageRequest pageable, Long id) {
        try {
            response = seatClient.getAllSeatsByAircraftId(pageable, id);
            return true;
        } catch (FeignException.NotFound ex) {
            log.error(ex.getMessage());
            Notification.show("Seat with aircraftId = " + id + " not found.", 3000, Notification.Position.TOP_CENTER);
        }
        return false;
    }

    private Grid.Column<SeatDto> createIdColumn() {
        return grid.addColumn(seatDto -> seatDto.getId()
                .intValue()).setHeader("Id").setWidth("120px").setFlexGrow(0);
    }

    private Grid.Column<SeatDto> createSeatNumberColumn() {
        return grid.addColumn(SeatDto::getSeatNumber).setHeader("Seat Number").setWidth("120px");
    }

    private Grid.Column<SeatDto> createIsNearEmergencyExitColumn() {
        return grid.addColumn(SeatDto::getIsNearEmergencyExit).setHeader("Is Near Emergency Exit").setWidth("120px");
    }

    private Grid.Column<SeatDto> createIsLockedBackColumn() {
        return grid.addColumn(SeatDto::getIsLockedBack).setHeader("Is Locked Back").setWidth("120px");
    }

    private Grid.Column<SeatDto> createCategoryColumn() {
        return grid.addColumn(SeatDto::getCategory).setHeader("Category").setWidth("240px");
    }

    private Grid.Column<SeatDto> createAircraftIdColumn() {
        return grid.addColumn(SeatDto::getAircraftId).setHeader("Aircraft Id").setWidth("120px");
    }

    private Grid.Column<SeatDto> createEditColumn() {
        return grid.addComponentColumn(seat -> {
            Button updateButton = new Button("Update");
            updateButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(seat);
            });
            return updateButton;
        });
    }

    private void addEditorListeners() {
        editor.addSaveListener(e -> {
            if (isEditedSeat(e.getItem().getId(), e.getItem())) {
                Notification.show("Seat edited successfully.", 3000, Notification.Position.TOP_CENTER);
                grid.getDataProvider().refreshAll();
            } else {
                updateGridData();
            }
        });
    }

    private boolean isEditedSeat(Long id, SeatDto seatDto) {
        try {
            seatClient.updateSeatById(id, seatDto);
            return true;
        } catch (FeignException.BadRequest ex) {
            log.error(ex.getMessage());
            Notification.show("Aircraft with id = " + seatDto.getAircraftId() + " not found.", 3000, Notification.Position.TOP_CENTER);
        } catch (FeignException.NotFound ex) {
            log.error(ex.getMessage());
            Notification.show("Seat with id = " + seatDto.getId() + " not found.", 3000, Notification.Position.TOP_CENTER);
        }
        return false;
    }

    private Grid.Column<SeatDto> createDeleteColumn() {
        return grid.addComponentColumn(seat -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                if (grid.getDataProvider().isInMemory() && grid.getDataProvider().getClass() == ListDataProvider.class) {
                    ListDataProvider<SeatDto> dataProvider = (ListDataProvider<SeatDto>) grid.getDataProvider();
                    if (isDeletedSeat(seat)) {
                        Notification.show("Seat deleted successfully.", 3000, Notification.Position.TOP_CENTER);
                        dataProvider.getItems().remove(seat);
                    }
                }
                grid.getDataProvider().refreshAll();
            });
            return deleteButton;
        }).setWidth("150px").setFlexGrow(0);
    }

    private boolean isDeletedSeat(SeatDto seat) {
        try {
            seatClient.deleteSeatById(seat.getId());
            return true;
        } catch (FeignException.MethodNotAllowed feignException) {
            log.error(feignException.getMessage());
            Notification.show("Seat is locked by FlightSeat", 3000, Notification.Position.TOP_CENTER);
            return false;
        }
    }

    private Binder<SeatDto> createBinder() {
        Binder<SeatDto> binder = new Binder<>(SeatDto.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        return binder;
    }

    private void createIdField(Binder<SeatDto> binder,
                               ValidationMessage idValidationMessage,
                               Grid.Column<SeatDto> idColumn) {
        IntegerField idField = new IntegerField();
        idField.setWidthFull();
        binder.forField(idField)
                .asRequired("Id must not be empty")
                .withStatusLabel(idValidationMessage)
                .bind(seatDto -> seatDto.getId().intValue(),
                        (seatDto, Integer) -> seatDto.setId(Integer.longValue()));
        idColumn.setEditorComponent(idField);
    }

    private void createSeatNumberField(Binder<SeatDto> binder,
                                       ValidationMessage seatNumberValidationMessage,
                                       Grid.Column<SeatDto> seatNumberColumn) {
        TextField seatNumberField = new TextField();
        seatNumberField.setWidthFull();
        binder.forField(seatNumberField).asRequired("Seat number must not be empty")
                .withStatusLabel(seatNumberValidationMessage)
                .withValidator(number -> number.length() >= 2 && number.length() <= 5,
                        "Seat number must be between 2 and 5 characters")
                .bind(SeatDto::getSeatNumber, SeatDto::setSeatNumber);
        seatNumberColumn.setEditorComponent(seatNumberField);
    }

    private void createIsNearEmergencyExitField(Binder<SeatDto> binder,
                                                ValidationMessage isNearEmergencyExitValidationMessage,
                                                Grid.Column<SeatDto> isNearEmergencyExitColumn) {
        ComboBox<Boolean> isNearEmergencyExitField = new ComboBox<>();
        isNearEmergencyExitField.setItems(true, false);
        binder.forField(isNearEmergencyExitField).asRequired("Is Near Emergency Exit must be true or false")
                .withStatusLabel(isNearEmergencyExitValidationMessage)
                .bind(SeatDto::getIsNearEmergencyExit, SeatDto::setIsNearEmergencyExit);
        isNearEmergencyExitColumn.setEditorComponent(isNearEmergencyExitField);
    }

    private void createIsLockedBackField(Binder<SeatDto> binder,
                                         ValidationMessage isLockedBackValidationMessage,
                                         Grid.Column<SeatDto> isLockedBackColumn) {
        ComboBox<Boolean> isLockedBackField = new ComboBox<>();
        isLockedBackField.setItems(true, false);
        binder.forField(isLockedBackField).asRequired("Is Locked Back must be true or false")
                .withStatusLabel(isLockedBackValidationMessage)
                .bind(SeatDto::getIsLockedBack, SeatDto::setIsLockedBack);
        isLockedBackColumn.setEditorComponent(isLockedBackField);
    }

    private void createCategoryField(Binder<SeatDto> binder,
                                     ValidationMessage categoryValidationMessage,
                                     Grid.Column<SeatDto> categoryColumn) {
        ComboBox<CategoryType> categoryField = new ComboBox<>();
        categoryField.setItems(CategoryType.values());
        categoryField.setWidthFull();
        binder.forField(categoryField).asRequired("Category must not be empty")
                .withStatusLabel(categoryValidationMessage)
                .bind(SeatDto::getCategory, SeatDto::setCategory);
        categoryColumn.setEditorComponent(categoryField);
    }

    private void createAircraftIdField(Binder<SeatDto> binder,
                                       ValidationMessage aircraftIdValidationMessage,
                                       Grid.Column<SeatDto> aircraftIdColumn) {
        IntegerField aircraftIdField = new IntegerField();
        aircraftIdField.setWidthFull();
        binder.forField(aircraftIdField).asRequired("Aircraft Id must not be empty")
                .withStatusLabel(aircraftIdValidationMessage)
                .bind(seatDto -> seatDto.getAircraftId().intValue(),
                        (seatDto, Integer) -> seatDto.setAircraftId(Integer.longValue()));
        aircraftIdColumn.setEditorComponent(aircraftIdField);
    }

    private void addTheme() {
        getThemeList().clear();
        getThemeList().add("spacing-s");
    }

    private Tabs createTabs(Div contentContainer) {
        Tabs tabs = new Tabs();

        Tab tableTab = new Tab("Seats table");
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
                aircraftIdSearchField.setVisible(true);
                searchButtonByAircraftId.setVisible(true);
            } else if (selectedTab == createTab) {
                contentContainer.removeAll();
                contentContainer.add(formLayout);
                updateButton.setVisible(false);
                cancelButton.setVisible(false);
                nextButton.setVisible(false);
                previousButton.setVisible(false);
                refreshButton.setVisible(false);
                searchButtonById.setVisible(false);
                idSearchField.setVisible(false);
                aircraftIdSearchField.setVisible(false);
                searchButtonByAircraftId.setVisible(false);
                grid.getDataProvider().refreshAll();
            }
        });
        return tabs;
    }

    private Tab createCreateTab(FormLayout formLayout) {
        Tab createTab = new Tab("Create seat");
        TextField seatNumber = new TextField("Seat Number");
        ComboBox<Boolean> isNearEmergencyExit = new ComboBox<>("Is Near Emergency Exit");
        ComboBox<Boolean> isLockedBack = new ComboBox<>("Is Locked Back");
        ComboBox<CategoryType> category = new ComboBox<>("Category");
        IntegerField aircraftIdField = new IntegerField("Aircraft Id");
        isNearEmergencyExit.setItems(true, false);
        isLockedBack.setItems(true, false);
        category.setItems(CategoryType.values());
        Button createButton = new Button("Create");
        formLayout.add(seatNumber, category, isNearEmergencyExit, isLockedBack, aircraftIdField, createButton);
        createButton.addClickListener(event -> {
            if (seatNumber.getValue().length() < 2 || seatNumber.getValue().length() > 5) {
                Notification.show("Seat number must be between 2 and 5 characters.", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            if (seatNumber.isEmpty() || category.isEmpty() || aircraftIdField.isEmpty() || aircraftIdField.getValue() <= 0
                    || isNearEmergencyExit.isEmpty() || isLockedBack.isEmpty()) {
                Notification.show("Please fill in all required fields correctly.", 3000, Notification.Position.TOP_CENTER);
                return;

            }
            SeatDto seatDto = new SeatDto();
            seatDto.setSeatNumber(seatNumber.getValue());
            seatDto.setIsNearEmergencyExit(isNearEmergencyExit.getValue());
            seatDto.setIsLockedBack(isLockedBack.getValue());
            seatDto.setCategory(category.getValue());
            seatDto.setAircraftId(aircraftIdField.getValue().longValue());
            if (isCreatedSeat(seatDto)) {
                seatNumber.clear();
                isNearEmergencyExit.clear();
                isLockedBack.clear();
                category.clear();
                aircraftIdField.clear();
                grid.getDataProvider().refreshAll();
                Notification.show("Seat created successfully.", 3000, Notification.Position.TOP_CENTER);
            }
        });
        return createTab;
    }

    private boolean isCreatedSeat(SeatDto seatDto) {
        try {
            SeatDto savedSeat = seatClient.createSeat(seatDto).getBody();
            dataSource.add(savedSeat);
            return true;
        } catch (FeignException.BadRequest ex) {
            log.error(ex.getMessage());
            Notification.show("Aircraft with id = " + seatDto.getAircraftId() + " not found.", 3000, Notification.Position.TOP_CENTER);
        }
        return false;
    }
}