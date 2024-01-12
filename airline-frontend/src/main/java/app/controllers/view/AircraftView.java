package app.controllers.view;

import app.clients.AircraftClient;
import app.dto.AircraftDTO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import feign.FeignException;
import org.springframework.http.HttpStatus;

import java.util.List;

/*
 * Вью сделана на основе ExempleView, SeatView, TimezoneView с небольшим рефакторингом
 */
@Route(value = "aircrafts", layout = MainLayout.class)
@CssImport(value = "grid.css")
public class AircraftView extends VerticalLayout {
    private final Grid<AircraftDTO> grid = new Grid<>(AircraftDTO.class, false);
    private final Editor<AircraftDTO> editor = grid.getEditor();
    private final Binder<AircraftDTO> binder = createBinder();
    private final AircraftClient aircraftClient;
    private final List<AircraftDTO> dataSource;

    private final Tabs tabs = new Tabs();
    private final Div contentTabContainer = new Div();

    private Integer currentPage = 0;
    private Integer maxPages;

    public AircraftView(AircraftClient aircraftClient) {
        this.aircraftClient = aircraftClient;
        List<AircraftDTO> aircrafts = aircraftClient.getAllAircrafts(null, null).getBody();
        int pageSize = 10;
        this.maxPages = (int) Math.ceil((double) aircrafts.size() / pageSize);
        this.dataSource = aircraftClient.getAllAircrafts(0, 10).getBody();
        getThemeList().clear();
        getThemeList().add("spacing-s");
        contentTabContainer.setSizeFull();


        //      Создали первую вкладку и кнопки навигации
        Tab tableTab = createTable();
        HorizontalLayout navigationButtons = createNavigationButtons();
        contentTabContainer.add(grid, navigationButtons);

//      Создали вторую вкладку
        FormLayout createTabContentContainer = new FormLayout();
        Tab createTab = createCreateTab(createTabContentContainer);

        tabs.add(tableTab, createTab);
        tabs.setSelectedTab(tableTab);

        tabs.addSelectedChangeListener(event -> {
            Tab selectedTab = tabs.getSelectedTab();
            if (selectedTab == tableTab) {
                contentTabContainer.removeAll();
                contentTabContainer.add(grid, navigationButtons);
            } else if (selectedTab == createTab) {
                contentTabContainer.removeAll();
                contentTabContainer.add(createTabContentContainer);
                grid.getListDataView().refreshAll();
            }
        });
        add(tabs, contentTabContainer);
    }

    private Tab createTable() {
        Tab tableTab = new Tab("Aircrafts table");
        createIdColumn();
        createAircraftNumberColumn();
        createModelColumn();
        createModelYearColumn();
        createFlightRangeColumn();
        createEditColumn();
        createDeleteColumn();

        grid.setAllRowsVisible(true);
        grid.setItems(dataSource);

        editor.addSaveListener(e -> {
            aircraftClient.updateAircraftById(e.getItem().getId(), e.getItem());
        });
        return tableTab;
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
        List<AircraftDTO> newData = aircraftClient.getAllAircrafts(currentPage, 10).getBody();
        dataSource.addAll(newData);
        grid.getListDataView().refreshAll();
    }

    private Tab createCreateTab(FormLayout formLayout) {
        Tab createTab = new Tab("Create airctaft");
        TextField aircraftNumber = new TextField("Aircraft Number");
        TextField model = new TextField("Model");
        IntegerField modelYear = new IntegerField("Model year");
        IntegerField flightRange = new IntegerField("Flight range");

        aircraftNumber.setMinLength(4);
        aircraftNumber.setMaxLength(15);
        aircraftNumber.setRequired(true);
        aircraftNumber.setRequiredIndicatorVisible(true);

        model.setRequired(true);
        model.setRequiredIndicatorVisible(true);

        modelYear.setMin(2000);
        modelYear.setRequiredIndicatorVisible(true);

        flightRange.setRequiredIndicatorVisible(true);

        Button createButton = new Button("Create");
        formLayout.add(aircraftNumber, model, modelYear, flightRange, createButton);
        createButton.addClickListener(event -> {
            // Сообщения, если поле заполненно некорректно
            if (aircraftNumber.isInvalid() || model.isInvalid() || modelYear.isInvalid() || flightRange.isInvalid() ||
                    aircraftNumber.isEmpty() || model.isEmpty() || modelYear.isEmpty() || flightRange.isEmpty()) {

                if (flightRange.isEmpty()) {
                    flightRange.setErrorMessage("Flight range must not be empty");
                    flightRange.setInvalid(true);
                }
                if (modelYear.isEmpty() || modelYear.isInvalid()) {
                    modelYear.setErrorMessage("Model year must more than 2000");
                    modelYear.setInvalid(true);
                }
                if (model.isEmpty()) {
                    model.setErrorMessage("Model must not be empty");
                    model.setInvalid(true);
                }
                if (aircraftNumber.isEmpty() || aircraftNumber.isInvalid()) {
                    aircraftNumber.setErrorMessage("Length of Aircraft Number should be between 4 and 15 characters");
                    aircraftNumber.setInvalid(true);
                }
                Notification.show("Please fill in all fields correctly.", 3000, Notification.Position.TOP_CENTER);
                return;
            }

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

            aircraftNumber.setInvalid(false);
            model.setInvalid(false);
            modelYear.setInvalid(false);
            flightRange.setInvalid(false);

            grid.getListDataView().refreshAll();
            Notification.show("Aircraft created successfully.", 3000, Notification.Position.TOP_CENTER);
        });
        return createTab;
    }

    private Binder<AircraftDTO> createBinder() {
        Binder<AircraftDTO> binder = new Binder<>(AircraftDTO.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        return binder;
    }

    private void createDeleteColumn() {
        grid.addComponentColumn(aircraftDTO -> new Button("Delete", e -> {
            HttpStatus statusCode = null;
            if (editor.isOpen()) editor.cancel();
            try {
                statusCode = aircraftClient.deleteAircraftById(aircraftDTO.getId()).getStatusCode();
            } catch (FeignException.MethodNotAllowed except) {
                Notification.show("You can't delete an aircraft because it has seats assigned to it", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            if (statusCode != null && statusCode.is2xxSuccessful()) {
                dataSource.remove(aircraftDTO);
                grid.getListDataView().refreshAll();
            } else {
                Notification.show("ERROR.  Something went wrong", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }));
    }

    private void createEditColumn() {
        // Кнопка при закрытом editor (внешняя)
        var updateColumn = grid.addComponentColumn(aircraftDTO -> new Button("Update", e -> {
            if (editor.isOpen()) {
                editor.cancel();
            } else {
                editor.editItem(aircraftDTO);
            }
        }));

        // Кнопки при открытии editor (нажатие на внешнюю)
        Button updateButton = new Button("Update", e -> {
            editor.save();
            grid.setItems(dataSource);
            grid.getListDataView().refreshAll();
        });
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);

        HorizontalLayout actions = new HorizontalLayout(updateButton, cancelButton);
        actions.setPadding(false);

        updateColumn.setEditorComponent(actions);
    }

    private void createFlightRangeColumn() {
        Grid.Column<AircraftDTO> flightRangeColumn = grid.addColumn(AircraftDTO::getFlightRange).setHeader("Flight Range").setWidth("120px");
        IntegerField flightRangeField = new IntegerField();
        flightRangeField.setWidthFull();
        binder.forField(flightRangeField)
                .asRequired("Flight range must not be empty")
                .bind(AircraftDTO::getFlightRange, AircraftDTO::setFlightRange);
        flightRangeColumn.setEditorComponent(flightRangeField);
    }

    private void createModelYearColumn() {
        Grid.Column<AircraftDTO> modelYearColumn = grid.addColumn(AircraftDTO::getModelYear).setHeader("Model Year").setWidth("120px");
        IntegerField modelYearField = new IntegerField();
        modelYearField.setWidthFull();
        binder.forField(modelYearField)
                .asRequired("Model year must not be empty")
                .withValidator(year -> year >= 2000,
                        "Model Year should be later than 2000")
                .bind(AircraftDTO::getModelYear, AircraftDTO::setModelYear);
        modelYearColumn.setEditorComponent(modelYearField);
    }

    private void createModelColumn() {
        Grid.Column<AircraftDTO> modelColumn = grid.addColumn(AircraftDTO::getModel).setHeader("Model").setWidth("120px");
        TextField modelField = new TextField();
        modelField.setWidthFull();
        binder.forField(modelField)
                .asRequired("Model must not be empty")
                .bind(AircraftDTO::getModel, AircraftDTO::setModel);
        modelColumn.setEditorComponent(modelField);
    }

    private void createAircraftNumberColumn() {
        Grid.Column<AircraftDTO> aircraftNumberColumn = grid.addColumn(AircraftDTO::getAircraftNumber).setHeader("Aircraft Number").setWidth("120px");
        TextField aircraftNumberField = new TextField();
        aircraftNumberField.setWidthFull();
        binder.forField(aircraftNumberField)
                .asRequired("Aircraft number must not be empty")
                .withValidator(string -> string.length() >= 4 && string.length() <= 15,
                        "Length of Aircraft Number should be between 4 and 15 characters")
                .bind(AircraftDTO::getAircraftNumber, AircraftDTO::setAircraftNumber);
        aircraftNumberColumn.setEditorComponent(aircraftNumberField);
    }

    private void createIdColumn() {
        Grid.Column<AircraftDTO> idColumn = grid.addColumn(aircraftDTO -> aircraftDTO.getId().intValue()).setHeader("ID").setWidth("120px").setFlexGrow(0);
        IntegerField idField = new IntegerField();
        idField.setWidthFull();
        binder.forField(idField)
                .bindReadOnly(aircraftDTO -> aircraftDTO.getId().intValue());
        idColumn.setEditorComponent(idField);
    }
}