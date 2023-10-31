package app.controllers.view;

import app.clients.AircraftClient;
import app.dto.AircraftDTO;
import app.dto.ExampleDto;
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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "aircrafts", layout = MainLayout.class)
public class AircraftView extends VerticalLayout {
    private final Grid<AircraftDTO> grid = new Grid<>(AircraftDTO.class, false);
    private final Editor<AircraftDTO> editor = grid.getEditor();
    private final AircraftClient aircraftClient;
    private final List<AircraftDTO> dataSource;
    private Integer currentPage = 0;
    private Integer maxPages;

    public AircraftView(AircraftClient aircraftClient) {
        this.aircraftClient = aircraftClient;
        this.maxPages = aircraftClient.getAllPagesAircraftsDTO(0, 10).getBody().getTotalPages() - 1;
        this.dataSource = aircraftClient.getAllPagesAircraftsDTO(0, 10).getBody().stream().collect(Collectors.toList());

        ValidationMessage idValidationMessage = new ValidationMessage();
        ValidationMessage aircraftNumberValidationMessage = new ValidationMessage();
        ValidationMessage modelValidationMessage = new ValidationMessage();
        ValidationMessage modelYearValidationMessage = new ValidationMessage();
        ValidationMessage flightRangeValidationMessage = new ValidationMessage();

        Grid.Column<AircraftDTO> idColumn = createIdColumn();
        Grid.Column<AircraftDTO> aircraftNumberColumn = createAircraftNumberColumn();
        Grid.Column<AircraftDTO> modelColumn = createModelColumn();
        Grid.Column<AircraftDTO> modelYearColumn = createModelYearColumn();
        Grid.Column<AircraftDTO> flightRangeColumn = createFlightRangeColumn();
        Grid.Column<AircraftDTO> updateColumn = createEditColumn();
        createDeleteColumn();

        Binder<AircraftDTO> binder = createBinder();

        createIdField(binder, idValidationMessage, idColumn);
        createAircraftNumberField(binder, aircraftNumberValidationMessage, aircraftNumberColumn);
        createModelField(binder, modelValidationMessage, modelColumn);
        createModelYearField(binder, modelYearValidationMessage, modelYearColumn);
        createFlightRangeField(binder, flightRangeValidationMessage, flightRangeColumn);

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

        add(tabs, contentContainer, idValidationMessage, aircraftNumberValidationMessage, modelValidationMessage, modelYearValidationMessage, flightRangeValidationMessage);


    }

    private HorizontalLayout createNavigationButtons() {
        Button nextButton = new Button("Next", VaadinIcon.ARROW_RIGHT.create());
        Button prevButton = new Button("Back", VaadinIcon.ARROW_LEFT.create());

        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        prevButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        nextButton.addClickListener(event -> {
            if (currentPage < maxPages) {
                currentPage++;
                refreshGrid();
            }
        });

        prevButton.addClickListener(event -> {
            if (currentPage > 0) {
                currentPage--;
                refreshGrid();
            }
        });
        return new HorizontalLayout(prevButton, nextButton);
    }

    private void refreshGrid() {
        dataSource.clear();
        List<AircraftDTO> newData = aircraftClient.getAllPagesAircraftsDTO(currentPage, 10).getBody().stream().collect(Collectors.toList());
        dataSource.addAll(newData);
        grid.getDataProvider().refreshAll();
    }

    private Tabs createTabs(Div contentContainer) {

        Tabs tabs = new Tabs();

        Tab tableTab = new Tab("Aircrafts table");
        FormLayout formLayout = new FormLayout();
        Tab createTab = createCreateTab(formLayout);

        HorizontalLayout navigationButtons = createNavigationButtons();

        contentContainer.add(grid);
        contentContainer.add(navigationButtons);


        tabs.add(tableTab, createTab);
        tabs.setSelectedTab(tableTab);

        tabs.addSelectedChangeListener(event -> {
            Tab selectedTab = tabs.getSelectedTab();
            if (selectedTab == tableTab) {
                contentContainer.removeAll();
                contentContainer.add(grid);
                contentContainer.add(navigationButtons);
            } else if (selectedTab == createTab) {
                contentContainer.removeAll();
                contentContainer.add(formLayout);
                grid.getDataProvider().refreshAll();
            }
        });
        return tabs;
    }

    private Tab createCreateTab(FormLayout formLayout) {
        Tab createTab = new Tab("Create airctaft");
        TextField aircraftNumber = new TextField("Aircraft Number");
        TextField model = new TextField("Model");
        IntegerField modelYear = new IntegerField("Model year");
        IntegerField flightRange = new IntegerField("Flight range");

        aircraftNumber.setMinLength(4);
        aircraftNumber.setMaxLength(15);
        modelYear.setMin(2000);

        Button createButton = new Button("Create");
        formLayout.add(aircraftNumber, model, modelYear, flightRange, createButton);
        createButton.addClickListener(event -> {
            AircraftDTO aircraftDTO = new AircraftDTO();
            aircraftDTO.setAircraftNumber(aircraftNumber.getValue());
            aircraftDTO.setModel(model.getValue());
            aircraftDTO.setModelYear(modelYear.getValue());
            aircraftDTO.setFlightRange(flightRange.getValue());

            AircraftDTO savedAircraftDTO = aircraftClient.createAircraft(aircraftDTO).getBody();
            dataSource.add(savedAircraftDTO);
            aircraftNumber.clear();
            model.clear();
            modelYear.clear();
            flightRange.clear();
            grid.getDataProvider().refreshAll();
        });
        return createTab;
    }

    private void addTheme() {
        getThemeList().clear();
        getThemeList().add("spacing-s");
    }

    private void addEditorListeners() {
        editor.addSaveListener(e -> {
            aircraftClient.updateAircraftById(e.getItem().getId(), e.getItem());
            grid.getDataProvider().refreshAll();
        });
    }

    private void createFlightRangeField(Binder<AircraftDTO> binder,
                                        ValidationMessage flightRangeValidationMessage,
                                        Grid.Column<AircraftDTO> flightRangeColumn) {
        IntegerField flightRangeField = new IntegerField();
        flightRangeField.setWidthFull();
        binder.forField(flightRangeField)
                .asRequired("Flight range must not be empty")
                .withStatusLabel(flightRangeValidationMessage)
                .bind(AircraftDTO::getFlightRange, AircraftDTO::setFlightRange);
        flightRangeColumn.setEditorComponent(flightRangeField);
    }

    private void createModelYearField(Binder<AircraftDTO> binder,
                                      ValidationMessage modelYearValidationMessage,
                                      Grid.Column<AircraftDTO> modelYearColumn) {
        IntegerField modelYearField = new IntegerField();
        modelYearField.setWidthFull();
        binder.forField(modelYearField)
                .asRequired("Model year must not be empty")
                .withValidator(year -> year >= 2000,
                        "Model Year should be later than 2000")
                .withStatusLabel(modelYearValidationMessage)
                .bind(AircraftDTO::getModelYear, AircraftDTO::setModelYear);
        modelYearColumn.setEditorComponent(modelYearField);
    }

    private void createModelField(Binder<AircraftDTO> binder,
                                  ValidationMessage modelValidationMessage,
                                  Grid.Column<AircraftDTO> modelColumn) {
        TextField modelField = new TextField();
        modelField.setWidthFull();
        binder.forField(modelField)
                .asRequired("Model must not be empty")
                .withStatusLabel(modelValidationMessage)
                .bind(AircraftDTO::getModel, AircraftDTO::setModel);
        modelColumn.setEditorComponent(modelField);
    }

    private void createAircraftNumberField(Binder<AircraftDTO> binder,
                                           ValidationMessage aircraftNumberValidationMessage,
                                           Grid.Column<AircraftDTO> aircraftNumberColumn) {
        TextField aircraftNumberField = new TextField();
        aircraftNumberField.setWidthFull();
        binder.forField(aircraftNumberField)
                .asRequired("Aircraft number must not be empty")
                .withValidator(string -> string.length() >= 4 && string.length() <= 15,
                        "Length of Aircraft Number should be between 4 and 15 characters")
                .withStatusLabel(aircraftNumberValidationMessage)
                .bind(AircraftDTO::getAircraftNumber, AircraftDTO::setAircraftNumber);
        aircraftNumberColumn.setEditorComponent(aircraftNumberField);
    }

    private void createIdField(Binder<AircraftDTO> binder,
                               ValidationMessage idValidationMessage,
                               Grid.Column<AircraftDTO> idColumn) {
        IntegerField idField = new IntegerField();
        idField.setWidthFull();
        binder.forField(idField)
                .asRequired("ID must not be empty")
                .withStatusLabel(idValidationMessage)
                .bind(aircraftDTO -> aircraftDTO.getId().intValue(),
                        (aircraftDTO, integer) -> aircraftDTO.setId(integer.longValue()));
        idColumn.setEditorComponent(idField);

    }

    private Binder<AircraftDTO> createBinder() {
        Binder<AircraftDTO> binder = new Binder<>(AircraftDTO.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        return binder;
    }

    private Grid.Column<AircraftDTO> createDeleteColumn() {
        return grid.addComponentColumn(aircraftDTO -> new Button("Delete", e -> {
            if (editor.isOpen()) editor.cancel();

        }));
    }

    private Grid.Column<AircraftDTO> createEditColumn() {
        return grid.addComponentColumn(aircraftDTO -> new Button( "Update", e -> {
                if (editor.isOpen()) editor.cancel();
                grid.getEditor().editItem(aircraftDTO);
                if (grid.getDataProvider().isInMemory() && grid.getDataProvider().getClass() == ListDataProvider.class) {
                    ListDataProvider<AircraftDTO> dataProvider = (ListDataProvider<AircraftDTO>) grid.getDataProvider();
                }
            }));
    }

    private Grid.Column<AircraftDTO> createFlightRangeColumn() {
        return grid.addColumn(AircraftDTO::getFlightRange).setHeader("Flight Range").setWidth("120px");

    }

    private Grid.Column<AircraftDTO> createModelYearColumn() {
        return grid.addColumn(AircraftDTO::getModelYear).setHeader("Model Year").setWidth("120px");
    }

    private Grid.Column<AircraftDTO> createModelColumn() {
        return grid.addColumn(AircraftDTO::getModel).setHeader("Model").setWidth("120px");
    }

    private Grid.Column<AircraftDTO> createAircraftNumberColumn() {
        return grid.addColumn(AircraftDTO::getAircraftNumber).setHeader("Aircraft Number").setWidth("120px");
    }

    private Grid.Column<AircraftDTO> createIdColumn() {
        return grid.addColumn(aircraftDTO -> aircraftDTO.getId().intValue()).setHeader("ID").setWidth("120px").setFlexGrow(0);
    }


}
