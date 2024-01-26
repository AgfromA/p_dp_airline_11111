package app.controllers.view;


import app.clients.AccountClient;
import app.dto.AccountDto;
import app.dto.RoleDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@PageTitle("Airline Account")
@Route(value = "account", layout = MainLayout.class)
public class AccountView extends VerticalLayout {
    private final Grid<AccountDto> grid = new Grid<>(AccountDto.class, false);
    private final Editor<AccountDto> editor = grid.getEditor();

    private final AccountClient accountClient;

    private final List<AccountDto> dataSource;

    private final Button updateButton;
    private final Button cancelButton;
    private Integer page;
    private Integer size;

    public AccountView(AccountClient accountClient) {
        this.accountClient = accountClient;
        page = 0;
        size = 10;
        this.dataSource = accountClient.getAllAccounts(page, size).getBody();
        ValidationMessage idValidationMessage = new ValidationMessage();
        ValidationMessage nameValidationMessage = new ValidationMessage();
        ValidationMessage lastnameValidationMessage = new ValidationMessage();
        ValidationMessage bithDateValidationMessage = new ValidationMessage();
        ValidationMessage emailValidationMessage = new ValidationMessage();
        ValidationMessage rolesValidationMessage = new ValidationMessage();
        ValidationMessage phoneNumberValidationMessage = new ValidationMessage();
        ValidationMessage passwordValidationMessage = new ValidationMessage();
        ValidationMessage secQuestionValidationMessage = new ValidationMessage();
        ValidationMessage ansQuestionValidationMessage = new ValidationMessage();


        Grid.Column<AccountDto> idColumn = createIdColumn();
        Grid.Column<AccountDto> nameColumn = createNameColumn();
        Grid.Column<AccountDto> lastnameColumn = createLastnameColumn();
        Grid.Column<AccountDto> birthDateColumn = createBirthDateColumn();
        Grid.Column<AccountDto> emailColumn = createEmailColumn();
        Grid.Column<AccountDto> rolesColumn = createRolesColumn();
        Grid.Column<AccountDto> phoneNumberColumn = createphoneNumberColumn();
        Grid.Column<AccountDto> passwordColumn = createpasswordColumn();
        Grid.Column<AccountDto> secQuestionColumn = createSecQuestionColumn();
        Grid.Column<AccountDto> ansQuestionColumn = createAnsQuestionColumn();
        Grid.Column<AccountDto> updateColumn = createEditColumn();
        createDeleteColumn();

        Binder<AccountDto> binder = createBinder();

        createIdField(binder, idValidationMessage, idColumn);
        createNameField(binder, nameValidationMessage, nameColumn);
        createLastnameField(binder, lastnameValidationMessage, lastnameColumn);
        createBirthDateField(binder, bithDateValidationMessage, birthDateColumn);
        createEmailField(binder, emailValidationMessage, emailColumn);
        createRolesField(binder, rolesValidationMessage, rolesColumn);
        createPhoneNumberField(binder, phoneNumberValidationMessage, phoneNumberColumn);
        createPasswordField(binder, passwordValidationMessage, passwordColumn);
        createSecQuestionField(binder, secQuestionValidationMessage, secQuestionColumn);
        createAnsQuestionField(binder, ansQuestionValidationMessage, ansQuestionColumn);


        updateButton = new Button("Update", e -> editor.save());
        cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);

        addEditorListeners();

        grid.setItems(dataSource);
        addTheme();

        Div contentContainer = new Div();
        contentContainer.setSizeFull();
        Tabs tabs = createTabs(contentContainer);

        HorizontalLayout action = new HorizontalLayout(updateButton, cancelButton);
        action.setPadding(false);
        updateColumn.setEditorComponent(action);


        add(tabs, contentContainer,
                idValidationMessage,
                nameValidationMessage,
                lastnameValidationMessage,
                bithDateValidationMessage,
                emailValidationMessage,
                rolesValidationMessage,
                passwordValidationMessage,
                phoneNumberValidationMessage);
    }

    private void createLastnameField(Binder<AccountDto> binder,
                                     ValidationMessage lastnameValidationMessage,
                                     Grid.Column<AccountDto> lastnameColumn) {
        TextField lastnameField = new TextField();
        lastnameField.setWidthFull();
        binder.forField(lastnameField).asRequired("Answer question must not be empty")
                .withStatusLabel(lastnameValidationMessage)
                .bind(AccountDto::getLastName, AccountDto::setLastName);
        lastnameColumn.setEditorComponent(lastnameField);

    }

    private Grid.Column<AccountDto> createLastnameColumn() {
        return grid.addColumn(AccountDto::getLastName)
                .setHeader("Lastname").setWidth("100px").setFlexGrow(0);
    }

    private void createAnsQuestionField(Binder<AccountDto> binder,
                                        ValidationMessage ansQuestionValidationMessage,
                                        Grid.Column<AccountDto> ansQuestionColumn) {

        PasswordField ansQuestionField = new PasswordField();
        ansQuestionField.setWidthFull();
        binder.forField(ansQuestionField).asRequired("Answer question must not be empty")
                .withStatusLabel(ansQuestionValidationMessage)
                .bind(AccountDto::getAnswerQuestion, AccountDto::setAnswerQuestion);
        ansQuestionColumn.setEditorComponent(ansQuestionField);
    }

    private Grid.Column<AccountDto> createAnsQuestionColumn() {
        return grid.addColumn(AccountDto::getAnswerQuestion).setHeader("Answer Question")
                .setWidth("100px")
                .setFlexGrow(0);
    }

    private void createSecQuestionField(Binder<AccountDto> binder,
                                        ValidationMessage secQuestionValidationMessage,
                                        Grid.Column<AccountDto> secQuestionColumn) {
        TextField secQuestionField = new TextField();
        secQuestionField.setWidthFull();
        binder.forField(secQuestionField).asRequired("Secret Question must not be empty")
                .withStatusLabel(secQuestionValidationMessage)
                .bind(AccountDto::getSecurityQuestion, AccountDto::setSecurityQuestion);
        secQuestionColumn.setEditorComponent(secQuestionField);
    }

    private Grid.Column<AccountDto> createSecQuestionColumn() {
        return grid.addColumn(AccountDto::getSecurityQuestion).setHeader("Secret Question")
                .setWidth("100px")
                .setFlexGrow(0);
    }

    private void createPasswordField(Binder<AccountDto> binder,
                                     ValidationMessage passwordValidationMessage,
                                     Grid.Column<AccountDto> passwordColumn) {
        PasswordField passwordField = new PasswordField();
        passwordField.setWidthFull();
        binder.forField(passwordField).asRequired("Password must not be empty")
                .withStatusLabel(passwordValidationMessage)
                .bind(AccountDto::getPassword, AccountDto::setPassword);
        passwordColumn.setEditorComponent(passwordField);
    }

    private Grid.Column<AccountDto> createpasswordColumn() {
        return grid.addColumn(AccountDto::getPassword)
                .setHeader("Password").setWidth("100px").setFlexGrow(1);
    }

    private void createPhoneNumberField(Binder<AccountDto> binder,
                                        ValidationMessage phoneNumberValidationMessage,
                                        Grid.Column<AccountDto> phoneNumberColumn) {
        TextField phoneNumberField = new TextField();
        phoneNumberField.setWidthFull();
        binder.forField(phoneNumberField).asRequired("PhoneNumber must not be empty")
                .withStatusLabel(phoneNumberValidationMessage)
                .bind(AccountDto::getPhoneNumber, AccountDto::setPhoneNumber);
        phoneNumberColumn.setEditorComponent(phoneNumberField);
    }

    private Grid.Column<AccountDto> createphoneNumberColumn() {
        return grid.addColumn(AccountDto::getPhoneNumber)
                .setHeader("Phone Number").setWidth("100px").setFlexGrow(0);
    }

    private void createRolesField(Binder<AccountDto> binder,
                                  ValidationMessage rolesValidationMessage,
                                  Grid.Column<AccountDto> rolesColumn) {
        MultiSelectComboBox<RoleDto> comboBox = new MultiSelectComboBox<>();
        comboBox.setWidthFull();
        binder.forField(comboBox).asRequired("Role must not be empty")
                .withStatusLabel(rolesValidationMessage)
                .bind(AccountDto::getRoles, AccountDto::setRoles);
        Set<RoleDto> roles = accountClient.getAllRoles().getBody();
        Iterator iterator = roles.iterator();
        var list = new ArrayList<RoleDto>();
        while (iterator.hasNext()) {
            list.add((RoleDto) iterator.next());
        }
        var roleArray = new RoleDto[list.size()];
        for (int i = 0; i < roleArray.length - 1; i++) {
            roleArray[i] = list.get(i);
        }
        comboBox.setItems(roleArray);
        rolesColumn.setEditorComponent(comboBox);
    }

    private void createEmailField(Binder<AccountDto> binder,
                                  ValidationMessage emailValidationMessage,
                                  Grid.Column<AccountDto> emailColumn) {
        TextField emailField = new TextField();
        emailField.setWidthFull();
        binder.forField(emailField).asRequired("Email must not be empty")
                .withStatusLabel(emailValidationMessage)
                .bind(AccountDto::getEmail, AccountDto::setEmail);
        emailColumn.setEditorComponent(emailField);
    }

    private void createBirthDateField(Binder<AccountDto> binder,
                                      ValidationMessage bithDateValidationMessage,
                                      Grid.Column<AccountDto> birthDateColumn) {
        DatePicker birthDateField = new DatePicker();
        birthDateField.setWidthFull();
        binder.forField(birthDateField).asRequired("BirthDate must not be empty")
                .withStatusLabel(bithDateValidationMessage)
                .bind(AccountDto::getBirthDate, AccountDto::setBirthDate);
        birthDateColumn.setEditorComponent(birthDateField);
    }


    private void createNameField(Binder<AccountDto> binder,
                                 ValidationMessage nameValidationMessage,
                                 Grid.Column<AccountDto> nameColumn) {
        TextField nameField = new TextField();
        nameField.setWidthFull();
        binder.forField(nameField).asRequired("Name must not be empty")
                .withStatusLabel(nameValidationMessage)
                .withValidator(name -> name.length() >= 2 && name.length() <= 20,
                        "Name must be between 2 and 20 characters")
                .bind(AccountDto::getFirstName, AccountDto::setFirstName);
        nameColumn.setEditorComponent(nameField);
    }

    private void createIdField(Binder<AccountDto> binder,
                               ValidationMessage idValidationMessage,
                               Grid.Column<AccountDto> idColumn) {
        IntegerField idField = new IntegerField();
        idField.setWidthFull();
        binder.forField(idField)
                .asRequired("Id must not be empty")
                .withStatusLabel(idValidationMessage)
                .bind(accountDTO -> Math.toIntExact(accountDTO.getId()),
                        (accountDTO, Integer) -> accountDTO.setId(Integer.longValue()));
        idColumn.setEditorComponent(idField);
    }

    private Binder<AccountDto> createBinder() {
        Binder<AccountDto> binder = new Binder<>(AccountDto.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        return binder;
    }

    private Grid.Column<AccountDto> createDeleteColumn() {
        return grid.addComponentColumn(account -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                if (editor.isOpen()) {
                    editor.cancel();
                }
                if (grid.getDataProvider().isInMemory() && grid.getDataProvider().getClass() == ListDataProvider.class) {
                    ListDataProvider<AccountDto> dataProvider = (ListDataProvider<AccountDto>) grid.getDataProvider();
                    accountClient.deleteAccount(account.getId());
                    dataProvider.getItems().remove(account);
                }
                grid.getDataProvider().refreshAll();
            });
            return deleteButton;
        }).setWidth("150px").setFlexGrow(0);
    }


    private Grid.Column<AccountDto> createEditColumn() {
        return grid.addComponentColumn(account -> {

            Button update = new Button("Update");
            update.addClickListener(e -> {
                if (editor.isOpen()) {
                    editor.cancel();
                }
                editor.editItem(account);
            });
            return update;
        }).setWidth("150px");
    }

    private Grid.Column<AccountDto> createRolesColumn() {
        return grid.addColumn(AccountDto::getRoles).setHeader("AccountRoles")
                .setWidth("400px").setFlexGrow(0);
    }

    private Grid.Column<AccountDto> createEmailColumn() {
        return grid.addColumn(AccountDto::getEmail).setHeader("Email")
                .setWidth("150px").setFlexGrow(0);
    }

    private Grid.Column<AccountDto> createBirthDateColumn() {
        return grid.addColumn(AccountDto::getBirthDate).setHeader("BirthDate")
                .setWidth("100px").setFlexGrow(0);
    }


    private Grid.Column<AccountDto> createNameColumn() {
        return grid.addColumn(AccountDto::getFirstName).setHeader("Name")
                .setWidth("100px").setFlexGrow(0);
    }

    private Grid.Column<AccountDto> createIdColumn() {
        return grid.addColumn(AccountDto::getId)
                .setHeader("Id").setWidth("60px").setFlexGrow(0);
    }

    private void addEditorListeners() {
        editor.addSaveListener(e -> {
            accountClient.updateAccount(e.getItem().getId(), e.getItem());
            grid.getDataProvider().refreshAll();
        });
    }

    private void addTheme() {
        getThemeList().clear();
        getThemeList().add("spacing-s");
    }

    private Tabs createTabs(Div contentContainer) {
        Tabs tabs = new Tabs();

        Tab tableTab = new Tab("Account table");
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
                grid.getDataProvider().refreshAll();
            }
        });
        return tabs;
    }

    private Tab createCreateTab(FormLayout formLayout) {
        Tab createTab = new Tab("Create Account");
        TextField nameTextField = new TextField("Name");
        TextField lastnameTextField = new TextField("Lastname");
        DatePicker birthDayDateField = new DatePicker("BirthDate");
        TextField phoneNumberTextField = new TextField("Phone Number");
        TextField emailTextField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        TextField securityQuestionField = new TextField("Security Question");
        PasswordField answerQuestionField = new PasswordField("Answer Question");

        MultiSelectComboBox<RoleDto> rolesSelectBox = new MultiSelectComboBox<>("Roles");
        Set<RoleDto> roles = accountClient.getAllRoles().getBody()
                .stream().collect(Collectors.toSet());
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            rolesSelectBox.setItems((RoleDto) iterator.next(), (RoleDto) iterator.next());
        }


        Button createButton = new Button("Create");
        formLayout.add(nameTextField,
                lastnameTextField,
                birthDayDateField,
                phoneNumberTextField,
                emailTextField,
                rolesSelectBox,
                passwordField,
                securityQuestionField,
                answerQuestionField,
                createButton);


        createButton.addClickListener(event -> {
            AccountDto accountDto = new AccountDto();
            accountDto.setFirstName(nameTextField.getValue());
            accountDto.setLastName(lastnameTextField.getValue());
            accountDto.setBirthDate(birthDayDateField.getValue());
            accountDto.setPhoneNumber(phoneNumberTextField.getValue());
            accountDto.setEmail(emailTextField.getValue());
            accountDto.setRoles(rolesSelectBox.getValue());
            accountDto.setPassword(passwordField.getValue());
            accountDto.setSecurityQuestion(securityQuestionField.getValue());
            accountDto.setAnswerQuestion(answerQuestionField.getValue());
            AccountDto savedAccount = accountClient.createAccount(accountDto).getBody();
            dataSource.add(savedAccount);
            nameTextField.clear();
            lastnameTextField.clear();
            birthDayDateField.clear();
            phoneNumberTextField.clear();
            emailTextField.clear();
            passwordField.clear();
            securityQuestionField.clear();
            answerQuestionField.clear();
            rolesSelectBox.clear();
            grid.getDataProvider().refreshAll();
        });
        return createTab;
    }
}
