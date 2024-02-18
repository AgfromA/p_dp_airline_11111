package app.controllers.view;

import app.clients.DestinationClient;
import app.dto.DestinationDto;
import app.enums.Airport;
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
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "destination", layout = MainLayout.class)
public class DestinationView extends VerticalLayout {

    private final Grid<DestinationDto> grid = new Grid<>(DestinationDto.class, false);
    private final Editor<DestinationDto> editor = grid.getEditor();
    private final DestinationClient destinationClient;
    private final List<DestinationDto> dataSource;
    private ResponseEntity<Page<DestinationDto>> response;
    private final Button updateButton;
    private final Button cancelButton;
    private final Button nextButton;
    private final Button previousButton;
    private final Button refreshButton;
    private final Button searchFilteredButton;
    private final TextField cityFilterField;
    private final TextField countryFilterField;
    private final TextField timezoneFilterField;
    private Integer currentPage;
    private Integer maxPages;
    private String city;
    private String country;
    private String timezone;
    private boolean isFilteredSearch;

    public DestinationView(DestinationClient destinationClient) {

        this.destinationClient = destinationClient;
        this.currentPage = 0; // сначала инициализация страницы иначе вытаскивает весь список
        this.response = destinationClient.getAllDestinations(currentPage, 10, city, country, timezone);
        this.city = null;
        this.country = null;
        this.timezone = null;
        this.isFilteredSearch = false;
        List<DestinationDto> dtos = destinationClient.getAllDestinations(null, null, null, null, null).getBody()
                .stream().collect(Collectors.toList());
        int pageSize = 10;
        this.maxPages = (int) Math.ceil((double) dtos.size() / pageSize);
        this.dataSource = response.getBody().stream().collect(Collectors.toList());

        ValidationMessage idValidationMessage = new ValidationMessage();
        ValidationMessage airportCodeValidationMessage = new ValidationMessage();
        ValidationMessage timezoneValidationMessage = new ValidationMessage();

        Grid.Column<DestinationDto> idColumn = createIdColumn();
        Grid.Column<DestinationDto> airportCodeColumn = createAirportCodeColumn();
        Grid.Column<DestinationDto> airportNameColumn = createAirportNameColumn();
        Grid.Column<DestinationDto> cityColumn = createCityColumn();
        Grid.Column<DestinationDto> countryColumn = createCountryColumn();
        Grid.Column<DestinationDto> timezoneColumn = createTimezoneColumn();
        Grid.Column<DestinationDto> updateColumn = createEditColumn();
        createDeleteColumn();

        Binder<DestinationDto> binder = createBinder();

        createIdField(binder, idValidationMessage, idColumn);
        createAirportCodeField(binder, airportCodeValidationMessage, airportCodeColumn);
        createTimezoneField(binder, timezoneValidationMessage, timezoneColumn);

        updateButton = new Button("Update", e -> editor.save());
        cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        previousButton = new Button(VaadinIcon.ARROW_LEFT.create(), e -> previousPage());
        nextButton = new Button(VaadinIcon.ARROW_RIGHT.create(), e -> nextPage());
        refreshButton = createRefreshButton();

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

        cityFilterField = createCityFilterField();
        countryFilterField = createCountryFilterField();
        timezoneFilterField = createTimezoneFilterField();
        searchFilteredButton = createSearchButtonFiltered();
        HorizontalLayout filter = new HorizontalLayout(cityFilterField, countryFilterField
                , timezoneFilterField, searchFilteredButton);

        add(tabs
                , contentContainer
                , idValidationMessage
                , airportCodeValidationMessage
                , timezoneValidationMessage
                , changedPages
                , filter
        );
    }

    private void nextPage() {
        if (currentPage < maxPages) {
            currentPage++;
            if (isFilteredSearch) {
                searchByFilter();
            } else {
                updateGridData();
            }
        }
    }

    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            if (isFilteredSearch) {
                searchByFilter();
            } else {
                updateGridData();
            }
        }
    }

    private Button createRefreshButton() {
        return new Button("Refresh", e -> {
            currentPage = 0;
            isFilteredSearch = false;
            clearFilterData();
            updateGridData();
        });
    }

    private Button createSearchButtonFiltered() {
        return new Button("Search", e -> {
            currentPage = 0;
            city = cityFilterField.getValue();
            country = countryFilterField.getValue();
            timezone = timezoneFilterField.getValue();
            searchByFilter();
        });
    }

    private void searchByFilter() {
        dataSource.clear();
        isFilteredSearch = true;
        if (!isFoundDestinations(city, country, timezone)) {
            Notification.show("Destinations not found", 3000, Notification.Position.TOP_CENTER);
        }
        grid.getDataProvider().refreshAll();
    }

    private boolean isFoundDestinations(String city, String country, String timezone) {
        try {
            ResponseEntity<Page<DestinationDto>> filteredResponse = destinationClient
                    .getAllDestinations(currentPage, 10, city, country, timezone);
            List<DestinationDto> dtos = destinationClient.getAllDestinations(null, null,
                    null, null, null).getBody().stream().collect(Collectors.toList());
            int pageSize = 10;
            maxPages = (int) Math.ceil((double) dtos.size() / pageSize);
            dataSource.addAll(filteredResponse.getBody().stream().collect(Collectors.toList()));
            return true;
        } catch (FeignException.NotFound ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

    private void clearFilterData() {
        cityFilterField.clear();
        countryFilterField.clear();
        timezoneFilterField.clear();
        city = null;
        country = null;
        timezone = null;
    }

    private void updateGridData() {
        dataSource.clear();
        response = destinationClient.getAllDestinations(currentPage, 10, city, country, timezone);
        List<DestinationDto> dtos = destinationClient.getAllDestinations(null, null,
                null, null, null).getBody().stream().collect(Collectors.toList());
        int pageSize = 10;
        maxPages = (int) Math.ceil((double) dtos.size() / pageSize);
        dataSource.addAll(response.getBody().stream().collect(Collectors.toList()));
        grid.getDataProvider().refreshAll();
    }

    private TextField createCityFilterField() {
        TextField cityField = new TextField();
        cityField.setPlaceholder("City filter");
        return cityField;
    }

    private TextField createCountryFilterField() {
        TextField countryField = new TextField();
        countryField.setPlaceholder("Country filter");
        return countryField;
    }

    private TextField createTimezoneFilterField() {
        TextField timezoneField = new TextField();
        timezoneField.setPlaceholder("Timezone filter");
        return timezoneField;
    }

    private Grid.Column<DestinationDto> createIdColumn() {
        return grid.addColumn(destinationDto -> destinationDto.getId()
                .intValue()).setHeader("Id").setWidth("120px").setFlexGrow(0);
    }

    private Grid.Column<DestinationDto> createAirportCodeColumn() {
        return grid.addColumn(DestinationDto::getAirportCode).setHeader("Airport Code").setWidth("120px");
    }

    private Grid.Column<DestinationDto> createAirportNameColumn() {
        return grid.addColumn(dto -> dto.getAirportCode().getAirportName()).setHeader("Airport name").setWidth("120px");
    }

    private Grid.Column<DestinationDto> createCityColumn() {
        return grid.addColumn(dto -> dto.getAirportCode().getCity()).setHeader("City").setWidth("120px");
    }

    private Grid.Column<DestinationDto> createCountryColumn() {
        return grid.addColumn(dto -> dto.getAirportCode().getCountry()).setHeader("Country").setWidth("120px");
    }

    private Grid.Column<DestinationDto> createTimezoneColumn() {
        return grid.addColumn(DestinationDto::getTimezone).setHeader("Timezone").setWidth("240px");
    }

    private Grid.Column<DestinationDto> createEditColumn() {
        return grid.addComponentColumn(destination -> {
            Button updateButton = new Button("Update");
            updateButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(destination);
            });
            return updateButton;
        });
    }

    private Grid.Column<DestinationDto> createDeleteColumn() {
        return grid.addComponentColumn(destination -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                if (grid.getDataProvider().isInMemory() && grid.getDataProvider().getClass() == ListDataProvider.class) {
                    ListDataProvider<DestinationDto> dataProvider = (ListDataProvider<DestinationDto>) grid.getDataProvider();
                    destinationClient.deleteDestination(destination.getId());
                    Notification.show("Destination deleted successfully.", 3000, Notification.Position.TOP_CENTER);
                    dataProvider.getItems().remove(destination);
                }
                grid.getDataProvider().refreshAll();
            });
            return deleteButton;
        }).setWidth("150px").setFlexGrow(0);
    }

    private Binder<DestinationDto> createBinder() {
        Binder<DestinationDto> binder = new Binder<>(DestinationDto.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        return binder;
    }

    private void createIdField(Binder<DestinationDto> binder,
                               ValidationMessage idValidationMessage,
                               Grid.Column<DestinationDto> idColumn) {
        IntegerField idField = new IntegerField();
        idField.setWidthFull();
        binder.forField(idField)
                .asRequired("Id must not be empty")
                .withStatusLabel(idValidationMessage)
                .bind(destinationDto -> destinationDto.getId().intValue(),
                        (destinationDto, Integer) -> destinationDto.setId(Integer.longValue()));
        idColumn.setEditorComponent(idField);
    }

    private void createAirportCodeField(Binder<DestinationDto> binder,
                                        ValidationMessage airportCodeValidationMessage,
                                        Grid.Column<DestinationDto> airportCodeColumn) {
        ComboBox<Airport> airportCodeField = new ComboBox<>();
        airportCodeField.setItems(Airport.values());
        airportCodeField.setRenderer(new TextRenderer<>(airport ->
                airport.getAirportInternalCode() + " - " + airport.getAirportName() + " ("
                        + airport.getCity() + "), " + airport.getCountry()));
        airportCodeField.setWidthFull();
        binder.forField(airportCodeField).asRequired("Airport code must not be empty")
                .withStatusLabel(airportCodeValidationMessage)
                .bind(DestinationDto::getAirportCode, DestinationDto::setAirportCode);
        airportCodeColumn.setEditorComponent(airportCodeField);
    }

    private void createTimezoneField(Binder<DestinationDto> binder,
                                     ValidationMessage timezoneValidationMessage,
                                     Grid.Column<DestinationDto> timezoneColumn) {
        TextField timezoneField = new TextField();
        binder.forField(timezoneField).asRequired("Field should not be empty")
                .withStatusLabel(timezoneValidationMessage)
                .withValidator(gmt -> gmt.length() >= 2 && gmt.length() <= 9
                        , "Timezone must be between 2 and 9 characters")
                .bind(DestinationDto::getTimezone, DestinationDto::setTimezone);
        timezoneColumn.setEditorComponent(timezoneField);
    }

    private void addEditorListeners() {
        editor.addSaveListener(e -> {
            if (isEditedDestination(e.getItem().getId(), e.getItem())) {
                Notification.show("Destination edited successfully.", 3000, Notification.Position.TOP_CENTER);
                grid.getDataProvider().refreshAll();
            } else {
                updateGridData();
            }
        });
    }

    private boolean isEditedDestination(Long id, DestinationDto destinationDto) {
        try {
            destinationClient.updateDestination(id, destinationDto);
            return true;
        } catch (FeignException.BadRequest ex) {
            log.error(ex.getMessage());
            Notification.show("Destination with airport code " + destinationDto.getAirportCode() +
                    " already exists.", 3000, Notification.Position.TOP_CENTER);
        } catch (FeignException.NotFound ex) {
            log.error(ex.getMessage());
            Notification.show("Destination with id " + destinationDto.getId() + " not found."
                    , 3000, Notification.Position.TOP_CENTER);
        }
        return false;
    }

    private void addTheme() {
        getThemeList().clear();
        getThemeList().add("spacing-s");
    }

    private Tabs createTabs(Div contentContainer) {
        Tabs tabs = new Tabs();

        Tab tableTab = new Tab("Destination table");
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
                cityFilterField.setVisible(true);
                countryFilterField.setVisible(true);
                timezoneFilterField.setVisible(true);
                searchFilteredButton.setVisible(true);
            } else if (selectedTab == createTab) {
                contentContainer.removeAll();
                contentContainer.add(formLayout);
                updateButton.setVisible(false);
                cancelButton.setVisible(false);
                nextButton.setVisible(false);
                previousButton.setVisible(false);
                refreshButton.setVisible(false);
                cityFilterField.setVisible(false);
                countryFilterField.setVisible(false);
                timezoneFilterField.setVisible(false);
                searchFilteredButton.setVisible(false);
                grid.getDataProvider().refreshAll();
            }
        });
        return tabs;
    }

    private Tab createCreateTab(FormLayout formLayout) {
        Tab createTab = new Tab("Create destination");
        ComboBox<Airport> airportCodeField = new ComboBox<>("Airport code");
        airportCodeField.setItems(Airport.values());
        airportCodeField.setItemLabelGenerator(airport ->
                airport.getAirportInternalCode() + " - " + airport.getAirportName() + " ("
                        + airport.getCity() + "), " + airport.getCountry());
        TextField timezoneField = new TextField("Timezone");
        Button createButton = new Button("Create");
        formLayout.add(airportCodeField, timezoneField, createButton);
        createButton.addClickListener(event -> {
            if (timezoneField.getValue().length() < 2 || timezoneField.getValue().length() > 9) {
                Notification.show("Timezone must be between 2 and 9 characters.", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            if (airportCodeField.isEmpty() || timezoneField.isEmpty()) {
                Notification.show("Please fill in all required fields correctly.", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            DestinationDto destinationDto = new DestinationDto();
            destinationDto.setAirportCode(airportCodeField.getValue());
            destinationDto.setTimezone(timezoneField.getValue());
            if (isCreatedDestination(destinationDto)) {
                grid.getDataProvider().refreshAll();
                airportCodeField.clear();
                timezoneField.clear();
                Notification.show("Destination created successfully.", 3000, Notification.Position.TOP_CENTER);
            }
        });
        return createTab;
    }

    private boolean isCreatedDestination(DestinationDto destinationDto) {
        try {
            ResponseEntity<DestinationDto> responseCreated = destinationClient.createDestination(destinationDto);
            if (responseCreated.getStatusCode() == HttpStatus.CREATED) {
                DestinationDto savedDestination = responseCreated.getBody();
                dataSource.add(savedDestination);
                return true;
            }
        } catch (FeignException.BadRequest ex) {
            log.error(ex.getMessage());
            Notification.show("Destination with airport code " + destinationDto.getAirportCode() +
                    " already exists.", 3000, Notification.Position.TOP_CENTER);
        }
        return false;
    }

}
