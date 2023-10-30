package app.controllers.view;

import app.clients.SeatClient;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Route(value = "seats", layout = MainLayout.class)
public class SeatView extends VerticalLayout {

    private final Grid<SeatDTO> grid = new Grid<>(SeatDTO.class, false);
    private final Editor<SeatDTO> editor = grid.getEditor();
    private ResponseEntity<Page<SeatDTO>> response;
    private final SeatClient seatClient;
    private final List<SeatDTO> dataSource;
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
        response = seatClient.getAllPagesSeatsDTO(currentPage, 10);
        currentPage = 0;
        maxPages = response.getBody().getTotalPages() - 1;
        isSearchById = false;
        isSearchByAircraftId = false;
        dataSource = response.getBody().stream().collect(Collectors.toList());

        ValidationMessage idValidationMessage = new ValidationMessage();
        ValidationMessage seatNumberValidationMessage = new ValidationMessage();
        ValidationMessage isNearEmergencyExitValidationMessage = new ValidationMessage();
        ValidationMessage isLockedBackValidationMessage = new ValidationMessage();
        ValidationMessage categoryValidationMessage = new ValidationMessage();
        ValidationMessage aircraftIdValidationMessage = new ValidationMessage();

        Grid.Column<SeatDTO> idColumn = createIdColumn();
        Grid.Column<SeatDTO> seatNumberColumn = createSeatNumberColumn();
        Grid.Column<SeatDTO> isNearEmergencyExitColumn = createIsNearEmergencyExitColumn();
        Grid.Column<SeatDTO> isLockedBackColumn = createIsLockedBackColumn();
        Grid.Column<SeatDTO> categoryColumn = createCategoryColumn();
        Grid.Column<SeatDTO> aircraftIdColumn = createAircraftIdColumn();
        Grid.Column<SeatDTO> updateColumn = createEditColumn();
        createDeleteColumn();

        Binder<SeatDTO> binder = createBinder();

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
        response = seatClient.getAllPagesSeatsDTO(currentPage, 10);
        dataSource.addAll(response.getBody().stream().collect(Collectors.toList()));
        maxPages = response.getBody().getTotalPages() - 1;
        grid.getDataProvider().refreshAll();
    }

    private void searchById() {
        dataSource.clear();
        isSearchById = true;
        isSearchByAircraftId = false;
        currentPage = 0;
        maxPages = 1;
        dataSource.add(seatClient.getSeatDTOById(idSearchField.getValue().longValue()).getBody());
        grid.getDataProvider().refreshAll();
    }

    private void searchByAircraftId() {
        isSearchById = false;
        PageRequest pageable = PageRequest.of(currentPage, 10, Sort.by("id").ascending());
        dataSource.clear();
        response = seatClient.getAllPagesSeatsDTOByAircraftId(pageable, searchByAircraftId);
        dataSource.addAll(response.getBody().stream().collect(Collectors.toList()));
        grid.getDataProvider().refreshAll();
        maxPages = response.getBody().getTotalPages() - 1;
    }

    private Grid.Column<SeatDTO> createIdColumn() {
        return grid.addColumn(seatDTO -> seatDTO.getId()
                .intValue()).setHeader("Id").setWidth("120px").setFlexGrow(0);
    }

    private Grid.Column<SeatDTO> createSeatNumberColumn() {
        return grid.addColumn(SeatDTO::getSeatNumber).setHeader("Seat Number").setWidth("120px");
    }

    private Grid.Column<SeatDTO> createIsNearEmergencyExitColumn() {
        return grid.addColumn(SeatDTO::getIsNearEmergencyExit).setHeader("Is Near Emergency Exit").setWidth("120px");
    }

    private Grid.Column<SeatDTO> createIsLockedBackColumn() {
        return grid.addColumn(SeatDTO::getIsLockedBack).setHeader("Is Locked Back").setWidth("120px");
    }

    private Grid.Column<SeatDTO> createCategoryColumn() {
        return grid.addColumn(SeatDTO::getCategory).setHeader("Category").setWidth("240px");
    }

    private Grid.Column<SeatDTO> createAircraftIdColumn() {
        return grid.addColumn(SeatDTO::getAircraftId).setHeader("Aircraft Id").setWidth("120px");
    }

    private Grid.Column<SeatDTO> createEditColumn() {
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

    private Grid.Column<SeatDTO> createDeleteColumn() {
        return grid.addComponentColumn(seat -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                if (grid.getDataProvider().isInMemory() && grid.getDataProvider().getClass() == ListDataProvider.class) {
                    ListDataProvider<SeatDTO> dataProvider = (ListDataProvider<SeatDTO>) grid.getDataProvider();
                    if (seatDeleted(seat)) {
                        Notification.show("Seat deleted successfully.", 3000, Notification.Position.TOP_CENTER);
                        dataProvider.getItems().remove(seat);
                    }
                }
                grid.getDataProvider().refreshAll();
            });
            return deleteButton;
        }).setWidth("150px").setFlexGrow(0);
    }

    private boolean seatDeleted(SeatDTO seat) {
        try {
            seatClient.deleteSeatById(seat.getId());
            return true;
        } catch (FeignException.MethodNotAllowed feignException) {
            log.error(feignException.getMessage());
            Notification.show("Seat is locked by FlightSeat", 3000, Notification.Position.TOP_CENTER);
            return false;
        }
    }

    private Binder<SeatDTO> createBinder() {
        Binder<SeatDTO> binder = new Binder<>(SeatDTO.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        return binder;
    }

    private void createIdField(Binder<SeatDTO> binder,
                               ValidationMessage idValidationMessage,
                               Grid.Column<SeatDTO> idColumn) {
        IntegerField idField = new IntegerField();
        idField.setWidthFull();
        binder.forField(idField)
                .asRequired("Id must not be empty")
                .withStatusLabel(idValidationMessage)
                .bind(seatDTO -> seatDTO.getId().intValue(),
                        (seatDTO, Integer) -> seatDTO.setId(Integer.longValue()));
        idColumn.setEditorComponent(idField);
    }

    private void createSeatNumberField(Binder<SeatDTO> binder,
                                       ValidationMessage seatNumberValidationMessage,
                                       Grid.Column<SeatDTO> seatNumberColumn) {
        TextField seatNumberField = new TextField();
        seatNumberField.setWidthFull();
        binder.forField(seatNumberField).asRequired("Seat number must not be empty")
                .withStatusLabel(seatNumberValidationMessage)
                .withValidator(number -> number.length() >= 2 && number.length() <= 5,
                        "Seat number must be between 2 and 5 characters")
                .bind(SeatDTO::getSeatNumber, SeatDTO::setSeatNumber);
        seatNumberColumn.setEditorComponent(seatNumberField);
    }

    private void createIsNearEmergencyExitField(Binder<SeatDTO> binder,
                                                ValidationMessage isNearEmergencyExitValidationMessage,
                                                Grid.Column<SeatDTO> isNearEmergencyExitColumn) {
        ComboBox<Boolean> isNearEmergencyExitField = new ComboBox<>();
        isNearEmergencyExitField.setItems(true, false);
        binder.forField(isNearEmergencyExitField).asRequired("Is Near Emergency Exit must be true or false")
                .withStatusLabel(isNearEmergencyExitValidationMessage)
                .bind(SeatDTO::getIsNearEmergencyExit, SeatDTO::setIsNearEmergencyExit);
        isNearEmergencyExitColumn.setEditorComponent(isNearEmergencyExitField);
    }

    private void createIsLockedBackField(Binder<SeatDTO> binder,
                                         ValidationMessage isLockedBackValidationMessage,
                                         Grid.Column<SeatDTO> isLockedBackColumn) {
        ComboBox<Boolean> isLockedBackField = new ComboBox<>();
        isLockedBackField.setItems(true, false);
        binder.forField(isLockedBackField).asRequired("Is Locked Back must be true or false")
                .withStatusLabel(isLockedBackValidationMessage)
                .bind(SeatDTO::getIsLockedBack, SeatDTO::setIsLockedBack);
        isLockedBackColumn.setEditorComponent(isLockedBackField);
    }

    private void createCategoryField(Binder<SeatDTO> binder,
                                     ValidationMessage categoryValidationMessage,
                                     Grid.Column<SeatDTO> categoryColumn) {
        ComboBox<CategoryType> categoryField = new ComboBox<>();
        categoryField.setItems(CategoryType.values());
        categoryField.setWidthFull();
        binder.forField(categoryField).asRequired("Category must not be empty")
                .withStatusLabel(categoryValidationMessage)
                .bind(SeatDTO::getCategory, SeatDTO::setCategory);
        categoryColumn.setEditorComponent(categoryField);
    }

    private void createAircraftIdField(Binder<SeatDTO> binder,
                                       ValidationMessage aircraftIdValidationMessage,
                                       Grid.Column<SeatDTO> aircraftIdColumn) {
        IntegerField aircraftIdField = new IntegerField();
        aircraftIdField.setWidthFull();
        binder.forField(aircraftIdField).asRequired("Aircraft Id must not be empty")
                .withStatusLabel(aircraftIdValidationMessage)
                .bind(seatDTO -> seatDTO.getAircraftId().intValue(),
                        (seatDTO, Integer) -> seatDTO.setAircraftId(Integer.longValue()));
        aircraftIdColumn.setEditorComponent(aircraftIdField);
    }

    private void addEditorListeners() {
        editor.addSaveListener(e -> {
            if (seatEdited(e.getItem().getId(), e.getItem())) {
                Notification.show("Seat edited successfully.", 3000, Notification.Position.TOP_CENTER);
            }
            grid.getDataProvider().refreshAll();
        });
    }

    private boolean seatEdited(Long id, SeatDTO seatDTO) {
        try {
            seatClient.updateSeatDTOById(id, seatDTO);
            return true;
        } catch (FeignException.BadRequest ex) {
            log.error(ex.getMessage());
            Notification.show("Aircraft with id = " + seatDTO.getAircraftId() + " not found.",3000, Notification.Position.TOP_CENTER);
        } catch (FeignException.NotFound ex) {
            log.error(ex.getMessage());
            Notification.show("Seat with id = " + seatDTO.getId() + " not found.",3000, Notification.Position.TOP_CENTER);
        }
        return false;
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
            SeatDTO seatDTO = new SeatDTO();
            seatDTO.setSeatNumber(seatNumber.getValue());
            seatDTO.setIsNearEmergencyExit(isNearEmergencyExit.getValue());
            seatDTO.setIsLockedBack(isLockedBack.getValue());
            seatDTO.setCategory(category.getValue());
            seatDTO.setAircraftId(aircraftIdField.getValue().longValue());
            if(!seatCreated(seatDTO)){
                return;
            }
            seatNumber.clear();
            isNearEmergencyExit.clear();
            isLockedBack.clear();
            category.clear();
            aircraftIdField.clear();
            grid.getDataProvider().refreshAll();
            Notification.show("Seat created successfully.", 3000, Notification.Position.TOP_CENTER);
        });
        return createTab;
    }

    private boolean seatCreated(SeatDTO seatDTO) {
        try {
            SeatDTO savedSeat = seatClient.createSeatDTO(seatDTO).getBody();
            dataSource.add(savedSeat);
                return true;
        } catch (FeignException.BadRequest ex) {
            log.error(ex.getMessage());
            Notification.show("Aircraft with id = " + seatDTO.getAircraftId() + " not found.",3000, Notification.Position.TOP_CENTER);
        }
        return false;
    }
}