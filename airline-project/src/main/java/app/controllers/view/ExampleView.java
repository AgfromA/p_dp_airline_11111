package app.controllers.view;


import app.dto.ExampleDto;
import app.entities.Example;
import app.services.ExampleService;
import app.mappers.ExampleMapper;
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

/**
 * Тут страшно, но мы справимся
 */
@Route(value = "example", layout = MainLayout.class)
public class ExampleView extends VerticalLayout {

    private final Grid<ExampleDto> grid = new Grid<>(ExampleDto.class, false);
    private final Editor<ExampleDto> editor = grid.getEditor();
    private final ExampleService exampleService;
    private final ExampleMapper exampleMapper;
    private final List<ExampleDto> dataSource;

    public ExampleView(ExampleService exampleService, ExampleMapper exampleMapper) {
        this.exampleService = exampleService;
        this.exampleMapper = exampleMapper;
        this.dataSource = exampleService.findAll().stream().map(exampleMapper::toDto).collect(Collectors.toList());
        ValidationMessage idValidationMessage = new ValidationMessage();
        ValidationMessage exampleTextValidationMessage = new ValidationMessage();

        Grid.Column<ExampleDto> idColumn = createIdColumn();
        Grid.Column<ExampleDto> exampleTextColumn = createExampleTextColumn();
        Grid.Column<ExampleDto> updateColumn = createEditColumn();
        createDeleteColumn();

        Binder<ExampleDto> binder = createBinder();

        createIdField(binder, idValidationMessage, idColumn);
        createExampleTextField(binder, exampleTextValidationMessage, exampleTextColumn);

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
    }

    private Grid.Column<ExampleDto> createIdColumn() {
        return grid.addColumn(exampleDto -> exampleDto.getId().intValue())
                .setHeader("Id").setWidth("120px").setFlexGrow(0);
    }

    private Grid.Column<ExampleDto> createExampleTextColumn() {
        return grid.addColumn(ExampleDto::getExampleText).setHeader("Example text").setWidth("650px");
    }

    private Grid.Column<ExampleDto> createEditColumn() {
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

    private Grid.Column<ExampleDto> createDeleteColumn() {
        return grid.addComponentColumn(example -> {
            Button deleteButton = new Button("Delete");
            // FIXME сомнительная конструкция
            deleteButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                if (grid.getDataProvider().isInMemory() && grid.getDataProvider().getClass() == ListDataProvider.class) {
                    ListDataProvider<ExampleDto> dataProvider = (ListDataProvider<ExampleDto>) grid.getDataProvider();
                    exampleService.delete(example.getId());
                    dataProvider.getItems().remove(example);
                }
                grid.getDataProvider().refreshAll();
            });
            return deleteButton;
        }).setWidth("150px").setFlexGrow(0);
    }

    private Binder<ExampleDto> createBinder() {
        Binder<ExampleDto> binder = new Binder<>(ExampleDto.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        return binder;
    }

    private void createIdField(Binder<ExampleDto> binder,
                               ValidationMessage idValidationMessage,
                               Grid.Column<ExampleDto> idColumn) {
        IntegerField idField = new IntegerField();
        idField.setWidthFull();
        binder.forField(idField)
                .asRequired("Id must not be empty")
                .withStatusLabel(idValidationMessage)
                .bind(exampleDto -> exampleDto.getId().intValue(),
                        (exampleDto, integer) -> exampleDto.setId(integer.longValue()));
        idColumn.setEditorComponent(idField);
    }

    private void createExampleTextField(Binder<ExampleDto> binder,
                                        ValidationMessage exampleTextValidationMessage,
                                        Grid.Column<ExampleDto> exampleTextColumn) {
        TextField exampleTextField = new TextField();
        exampleTextField.setWidthFull();
        binder.forField(exampleTextField).asRequired("Example text must not be empty")
                .withStatusLabel(exampleTextValidationMessage)
                .bind(ExampleDto::getExampleText, ExampleDto::setExampleText);
        exampleTextColumn.setEditorComponent(exampleTextField);
    }

    private void addEditorListeners() {
        editor.addSaveListener(e -> {
            Example exampleDto = exampleMapper.toEntity(e.getItem());
            exampleService.update(exampleDto.getId(), exampleDto);
            grid.getDataProvider().refreshAll();
        });
    }

    private void addTheme() {
        getThemeList().clear();
        getThemeList().add("spacing-s");
    }

    private Tabs createTabs(Div contentContainer) {
        Tabs tabs = new Tabs();

        Tab tableTab = new Tab("Examples table");
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
        Tab createTab = new Tab("Create example");
        TextField exampleTextField = new TextField("Example Text");
        Button createButton = new Button("Create");
        formLayout.add(exampleTextField, createButton);
        createButton.addClickListener(event -> {
            Example example = new Example();
            example.setExampleText(exampleTextField.getValue());
            Example savedExample = exampleService.save(example);
            dataSource.add(exampleMapper.toDto(savedExample));
            exampleTextField.clear();
            grid.getDataProvider().refreshAll();
        });
        return createTab;
    }
}