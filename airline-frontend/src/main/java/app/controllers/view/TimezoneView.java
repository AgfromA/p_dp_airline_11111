package app.controllers.view;

import app.clients.TimezoneClient;

import app.dto.TimezoneDto;
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


/**
 * Тут страшно, но мы справимся
 */
@Route(value = "timezones", layout = MainLayout.class)
public class TimezoneView extends VerticalLayout {

    private final Grid<TimezoneDto> grid = new Grid<>(TimezoneDto.class, false);
    private final Editor<TimezoneDto> editor = grid.getEditor();
    private final TimezoneClient timezoneClient;
    private final List<TimezoneDto> dataSource;

    private Integer currentPage;
    private Button nextButton = new Button("Next", VaadinIcon.ARROW_RIGHT.create());
    private Button prevButton = new Button("Back", VaadinIcon.ARROW_LEFT.create());

    private Integer maxPages;

    public TimezoneView(TimezoneClient timezoneClient) {
        this.timezoneClient = timezoneClient;
        currentPage = 0;
        var response = timezoneClient.getAllTimezones(currentPage, 10);
        dataSource = response.getBody();
        List<TimezoneDto> timezoneDtoList = timezoneClient.getAllTimezones(null, null).getBody();
        maxPages = (int) Math.ceil((double) timezoneDtoList.size() / 10);

        ValidationMessage idValidationMessage = new ValidationMessage();
        ValidationMessage exampleTextValidationMessage = new ValidationMessage();


        Grid.Column<TimezoneDto> idColumn = createIdColumn();
        Grid.Column<TimezoneDto> countryNameColumn = createCountryNameColumn();
        Grid.Column<TimezoneDto> cityNameColumn = createCityNameColumn();
        Grid.Column<TimezoneDto> gmtColumn = createGMTColumn();
        Grid.Column<TimezoneDto> gmtWinterColumn = createGMTWinterColumn();
        Grid.Column<TimezoneDto> updateColumn = createEditColumn();
        createDeleteColumn();


        Binder<TimezoneDto> binder = createBinder();

        createIdField(binder, idValidationMessage, idColumn);
        createCountryNameField(binder, exampleTextValidationMessage, countryNameColumn);
        createCityNameField(binder, exampleTextValidationMessage, cityNameColumn);
        createGMTField(binder, exampleTextValidationMessage, gmtColumn);
        createGMTWinterField(binder, exampleTextValidationMessage, gmtWinterColumn);

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

        add(tabs, contentContainer, idValidationMessage, exampleTextValidationMessage);

        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        prevButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout navigationButtons = new HorizontalLayout(prevButton, nextButton);

        add(navigationButtons);
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

    }


    private void refreshGrid() {
        dataSource.clear();
        List<TimezoneDto> newData = timezoneClient.getAllTimezones(currentPage, 10).getBody();
        dataSource.addAll(newData);
        grid.getDataProvider().refreshAll();
    }

    private Grid.Column<TimezoneDto> createIdColumn() {
        return grid.addColumn(timezoneDto -> timezoneDto.getId().intValue()).setHeader("Id").setWidth("120px").setFlexGrow(0);
    }

    private Grid.Column<TimezoneDto> createCountryNameColumn() {
        return grid.addColumn(TimezoneDto::getCountryName).setHeader("Country").setWidth("50px");
    }

    private Grid.Column<TimezoneDto> createCityNameColumn() {
        return grid.addColumn(TimezoneDto::getCityName).setHeader("City").setWidth("50px");
    }

    private Grid.Column<TimezoneDto> createGMTColumn() {
        return grid.addColumn(TimezoneDto::getGmt).setHeader("GMT").setWidth("50px");
    }

    private Grid.Column<TimezoneDto> createGMTWinterColumn() {
        return grid.addColumn(TimezoneDto::getGmtWinter).setHeader("GMT Winter").setWidth("50px");
    }

    private Grid.Column<TimezoneDto> createEditColumn() {
        return grid.addComponentColumn(example -> {
            Button updateButton = new Button("Update");
            updateButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(example);
            });
            return updateButton;
        });
    }

    private Grid.Column<TimezoneDto> createDeleteColumn() {
        return grid.addComponentColumn(example -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                if (grid.getDataProvider().isInMemory() && grid.getDataProvider().getClass() == ListDataProvider.class) {
                    ListDataProvider<TimezoneDto> dataProvider = (ListDataProvider<TimezoneDto>) grid.getDataProvider();
                    timezoneClient.deleteTimezoneById(example.getId());
                    dataProvider.getItems().remove(example);
                }
                grid.getDataProvider().refreshAll();
            });
            return deleteButton;
        }).setWidth("150px").setFlexGrow(0);
    }

    private Binder<TimezoneDto> createBinder() {
        Binder<TimezoneDto> binder = new Binder<>(TimezoneDto.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        return binder;
    }

    private void createIdField(Binder<TimezoneDto> binder,
                               ValidationMessage idValidationMessage,
                               Grid.Column<TimezoneDto> idColumn) {
        IntegerField idField = new IntegerField();
        idField.setWidthFull();
        binder.forField(idField)
                .asRequired("Id must not be empty")
                .withStatusLabel(idValidationMessage)
                .bind(exampleDto -> exampleDto.getId().intValue(),
                        (exampleDto, integer) -> exampleDto.setId(integer.longValue()));
        idColumn.setEditorComponent(idField);
    }

    private void createCountryNameField(Binder<TimezoneDto> binder,
                                        ValidationMessage exampleTextValidationMessage,
                                        Grid.Column<TimezoneDto> exampleTextColumn) {
        TextField countryNameFiled = new TextField();
        countryNameFiled.setWidthFull();
        binder.forField(countryNameFiled).asRequired("Country name text must not be empty")
                .withStatusLabel(exampleTextValidationMessage)
                .withValidator(number -> number.length() >= 2 && number.length() <= 15,
                        "Country name must be between 2 and 15 characters")
                .bind(TimezoneDto::getCountryName, TimezoneDto::setCountryName);
        exampleTextColumn.setEditorComponent(countryNameFiled);

    }


    private void createCityNameField(Binder<TimezoneDto> binder,
                                     ValidationMessage exampleTextValidationMessage,
                                     Grid.Column<TimezoneDto> exampleTextColumn) {
        TextField cityNameField = new TextField();
        cityNameField.setWidthFull();
        binder.forField(cityNameField).asRequired("City name text must not be empty")
                .withStatusLabel(exampleTextValidationMessage)
                .withValidator(number -> number.length() >= 2 && number.length() <= 15,
                        "City name must be between 2 and 15 characters")
                .bind(TimezoneDto::getCityName, TimezoneDto::setCityName);
        exampleTextColumn.setEditorComponent(cityNameField);

    }

    private void createGMTField(Binder<TimezoneDto> binder,
                                ValidationMessage exampleTextValidationMessage,
                                Grid.Column<TimezoneDto> exampleTextColumn) {
        TextField gmtField = new TextField();
        gmtField.setWidthFull();
        binder.forField(gmtField).asRequired("GMT must not be empty")
                .withStatusLabel(exampleTextValidationMessage)
                .withValidator(number -> number.length() >= 2 && number.length() <= 15,
                        "GMT name must be between 4 and 7 characters")
                .bind(TimezoneDto::getGmt, TimezoneDto::setGmt);
        exampleTextColumn.setEditorComponent(gmtField);

    }

    private void createGMTWinterField(Binder<TimezoneDto> binder,
                                      ValidationMessage exampleTextValidationMessage,
                                      Grid.Column<TimezoneDto> exampleTextColumn) {
        TextField gmtWinterField = new TextField();
        gmtWinterField.setWidthFull();
        binder.forField(gmtWinterField).asRequired("GMT Winter must not be empty")
                .withStatusLabel(exampleTextValidationMessage)
                .withValidator(number -> number.length() >= 2 && number.length() <= 15,
                        "GMT name must be between 4 and 7 characters")
                .bind(TimezoneDto::getGmtWinter, TimezoneDto::setGmtWinter);
        exampleTextColumn.setEditorComponent(gmtWinterField);

    }

    private void addEditorListeners() {
        editor.addSaveListener(e -> {
            timezoneClient.updateTimezoneById(e.getItem().getId(), e.getItem());
            grid.getDataProvider().refreshAll();
        });
    }

    private void addTheme() {
        getThemeList().clear();
        getThemeList().add("spacing-s");
    }

    private Tabs createTabs(Div contentContainer) {
        Tabs tabs = new Tabs();

        Tab tableTab = new Tab("Timezones table");
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
                nextButton.setVisible(true);
                prevButton.setVisible(true);
            } else if (selectedTab == createTab) {
                contentContainer.removeAll();
                contentContainer.add(formLayout);
                nextButton.setVisible(false);
                prevButton.setVisible(false);
            }
        });
        tabs.addSelectedChangeListener(event -> {
            Tab selectedTab = tabs.getSelectedTab();
            if (selectedTab == tableTab) {
                contentContainer.removeAll();
                contentContainer.add(grid);
            } else if (selectedTab == createTab) {
                contentContainer.removeAll();
                contentContainer.add(formLayout);
                grid.getDataProvider().refreshAll();
            }
        });

        return tabs;

    }

    private Tab createCreateTab(FormLayout formLayout) {
        Tab createTab = new Tab("Create timezone");
        TextField countryNameField = new TextField("Country name");
        TextField cityNameField = new TextField("City name");
        TextField gmtField = new TextField("GMT");
        TextField gmtWinterField = new TextField("GMT Winter");
        Button createButton = new Button("Create");
        formLayout.add(countryNameField, createButton);
        formLayout.add(cityNameField, createButton);
        formLayout.add(gmtField, createButton);
        formLayout.add(gmtWinterField, createButton);
        createButton.addClickListener(event -> {
            TimezoneDto timezoneDto = new TimezoneDto();
            timezoneDto.setCountryName(countryNameField.getValue());
            timezoneDto.setCityName(cityNameField.getValue());
            timezoneDto.setGmt(gmtField.getValue());
            timezoneDto.setGmtWinter(gmtWinterField.getValue());
            timezoneClient.createTimezone(timezoneDto);
            countryNameField.clear();
            cityNameField.clear();
            gmtField.clear();
            gmtWinterField.clear();
            grid.getDataProvider().refreshAll();
            refreshGrid();
        });

        return createTab;
    }
}