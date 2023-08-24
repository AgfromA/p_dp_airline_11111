package app.controllers.view;

import app.dto.DestinationDTO;
import app.entities.Destination;
import app.enums.Airport;
import app.services.interfaces.DestinationService;
import app.util.mappers.DestinationMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "destination", layout = MainLayout.class)
public class DestinationView extends VerticalLayout {

    private final Grid<DestinationDTO> grid = new Grid<>(DestinationDTO.class, false);
    private final Editor<DestinationDTO> editor = grid.getEditor();
    private final DestinationService destinationService;
    private final DestinationMapper destinationMapper;
    private final List<DestinationDTO> dataSource;

    public DestinationView(DestinationService destinationService, DestinationMapper destinationMapper) {
        this.destinationService = destinationService;
        this.destinationMapper = destinationMapper;
        int pageNumber = 0; // Номер страницы
        int pageSize = 100; // Количество элементов на странице
        this.dataSource = destinationService.getAllDestinations(pageNumber, pageSize).stream().collect(Collectors.toList());

        ValidationMessage idValidationMessage = new ValidationMessage();
        ValidationMessage destinationTextValidationMessage = new ValidationMessage();

        Grid.Column<DestinationDTO> idColumn = createIdColumn();
        Grid.Column<DestinationDTO> destinationAirportNameColumn = createDestinationAirportNameColumn();
        Grid.Column<DestinationDTO> destinationCityNameColumn = createDestinationCityNameColumn();
        Grid.Column<DestinationDTO> destinationCountryNameColumn = createDestinationCountryNameColumn();
        Grid.Column<DestinationDTO> destinationTimezoneColumn = createDestinationTimezoneColumn();
        Grid.Column<DestinationDTO> destinationAirportCodeColumn = createDestinationAirportCodeColumn();
        Grid.Column<DestinationDTO> updateColumn = createEditColumn();
        createDeleteColumn();

        Binder<DestinationDTO> binder = createBinder();

        createDestinationAirportNameField(binder, destinationTextValidationMessage, destinationAirportNameColumn);
        createDestinationCityNameField(binder, destinationTextValidationMessage, destinationCityNameColumn);
        createDestinationCountryNameField(binder, destinationTextValidationMessage, destinationCountryNameColumn);
        createDestinationTimezoneField(binder, destinationTextValidationMessage, destinationTimezoneColumn);

        Button updateButton = new Button("Update", e -> editor.save());
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(updateButton, cancelButton);
        actions.setPadding(false);
        updateColumn.setEditorComponent(actions);

        addEditorListeners();
        grid.setItems(dataSource);
        addTheme();

        Div contentContainer = new Div();
        contentContainer.setSizeFull();
        Tabs tabs = createTabs(contentContainer);

        add(tabs, contentContainer, idValidationMessage, destinationTextValidationMessage);
    }


    private Grid.Column<DestinationDTO> createIdColumn() {
        return grid.addColumn(destinationDTO -> destinationDTO.getId().intValue())
                .setHeader("Id").setWidth("120px").setFlexGrow(0);
    }

    private Grid.Column<DestinationDTO> createDestinationAirportNameColumn() {
        return grid.addColumn(DestinationDTO::getAirportName).setHeader("AirportName").setWidth("200px");
    }

    private Grid.Column<DestinationDTO> createDestinationCityNameColumn() {
        return grid.addColumn(DestinationDTO::getCityName).setHeader("CityName").setWidth("200px");
    }

    private Grid.Column<DestinationDTO> createDestinationCountryNameColumn() {
        return grid.addColumn(DestinationDTO::getCountryName).setHeader("CountryName").setWidth("200px");
    }

    private Grid.Column<DestinationDTO> createDestinationTimezoneColumn() {
        return grid.addColumn(DestinationDTO::getTimezone).setHeader("Timezone").setWidth("200px");
    }

    private Grid.Column<DestinationDTO> createDestinationAirportCodeColumn() {
        return grid.addColumn(DestinationDTO::getAirportCode).setHeader("AirportCode").setWidth("200px");
    }

    private Grid.Column<DestinationDTO> createEditColumn() {
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

    private Grid.Column<DestinationDTO> createDeleteColumn() {
        return grid.addComponentColumn(destination -> {
            Button deleteButton = new Button("Delete");
            // FIXME сомнительная конструкция
            deleteButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                if (grid.getDataProvider().isInMemory() && grid.getDataProvider().getClass() == ListDataProvider.class) {
                    ListDataProvider<DestinationDTO> dataProvider = (ListDataProvider<DestinationDTO>) grid.getDataProvider();
                    destinationService.deleteDestinationById(destination.getId());
                    dataProvider.getItems().remove(destination);
                }
                grid.getDataProvider().refreshAll();
            });
            return deleteButton;
        }).setWidth("150px").setFlexGrow(0);
    }

    private Binder<DestinationDTO> createBinder() {
        Binder<DestinationDTO> binder = new Binder<>(DestinationDTO.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        return binder;
    }

    private void createDestinationAirportNameField(Binder<DestinationDTO> binder,
                                            ValidationMessage destinationTextValidationMessage,
                                            Grid.Column<DestinationDTO> destinationTextColumn) {
        TextField destinationAirportNameField = new TextField();
        destinationAirportNameField.setWidthFull();
        binder.forField(destinationAirportNameField).asRequired("Destination AirportName must not be empty")
                .withStatusLabel(destinationTextValidationMessage)
                .bind(DestinationDTO::getAirportName, DestinationDTO::setAirportName);

        destinationTextColumn.setEditorComponent(destinationAirportNameField);
    }

    private void createDestinationCityNameField(Binder<DestinationDTO> binder,
                                            ValidationMessage destinationTextValidationMessage,
                                            Grid.Column<DestinationDTO> destinationTextColumn) {

        TextField destinationCityNameField = new TextField();
        destinationCityNameField.setWidthFull();
        binder.forField(destinationCityNameField).asRequired("Destination CityName must not be empty")
                .withStatusLabel(destinationTextValidationMessage)
                .bind(DestinationDTO::getCityName, DestinationDTO::setCityName);
        destinationTextColumn.setEditorComponent(destinationCityNameField);
    }

    private void createDestinationCountryNameField(Binder<DestinationDTO> binder,
                                                ValidationMessage destinationTextValidationMessage,
                                                Grid.Column<DestinationDTO> destinationTextColumn) {

        TextField destinationCountryNameField = new TextField();
        destinationCountryNameField.setWidthFull();
        binder.forField(destinationCountryNameField).asRequired("Destination CountryName must not be empty")
                .withStatusLabel(destinationTextValidationMessage)
                .bind(DestinationDTO::getCountryName, DestinationDTO::setCountryName);
        destinationTextColumn.setEditorComponent(destinationCountryNameField);
    }

    private void createDestinationTimezoneField(Binder<DestinationDTO> binder,
                                                   ValidationMessage destinationTextValidationMessage,
                                                   Grid.Column<DestinationDTO> destinationTextColumn) {

        TextField destinationTimezoneField = new TextField();
        destinationTimezoneField.setWidthFull();
        binder.forField(destinationTimezoneField).asRequired("Destination Timezone must not be empty")
                .withStatusLabel(destinationTextValidationMessage)
                .bind(DestinationDTO::getTimezone, DestinationDTO::setTimezone);
        destinationTextColumn.setEditorComponent(destinationTimezoneField);
    }

    private void addEditorListeners() {
        editor.addSaveListener(e -> {
            Destination destination = destinationMapper.convertToDestinationEntity(e.getItem());
            destinationService.saveDestination(destinationMapper.convertToDestinationDTOEntity(destination));
            grid.getDataProvider().refreshAll();
        });
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
            } else if (selectedTab == createTab) {
                contentContainer.removeAll();
                contentContainer.add(formLayout);
            }
        });
        return tabs;
    }

    private Tab createCreateTab(FormLayout formLayout) {
        Tab createTab = new Tab("Create destination");
        TextField destinationAirportNameField = new TextField("AirportName");
        TextField destinationCityNameField = new TextField("CityName");
        TextField destinationCountryNameField = new TextField("CountryName");
        TextField destinationAirportCodeField = new TextField("AirportCode");
        TextField destinationTimezoneField = new TextField("Timezone");

        Button createButton = new Button("Create");
        formLayout.add(destinationAirportNameField, destinationCityNameField, destinationCountryNameField,
                destinationAirportCodeField, destinationTimezoneField, createButton);
        createButton.addClickListener(event -> {
            Destination destination = new Destination();
            destination.setAirportName(destinationAirportNameField.getValue());
            destination.setCityName(destinationCityNameField.getValue());
            destination.setCountryName(destinationCountryNameField.getValue());
            destination.setAirportCode(Airport.valueOf(destinationAirportCodeField.getValue()));
            destination.setTimezone(destinationTimezoneField.getValue());
            Destination savedDestination =
                    destinationService.saveDestination(destinationMapper.convertToDestinationDTOEntity(destination));
            dataSource.add(destinationMapper.convertToDestinationDTOEntity(savedDestination));
            destinationAirportNameField.clear();
            destinationCityNameField.clear();
            destinationCountryNameField.clear();
            destinationAirportCodeField.clear();
            destinationTimezoneField.clear();
            grid.getDataProvider().refreshAll();
        });
        return createTab;
    }
}
