package app.controllers.view;


import app.clients.AccountClient;
import app.dto.AccountDTO;
import app.dto.RoleDTO;
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
    private final Grid<AccountDTO> grid = new Grid<>(AccountDTO.class, false);
    private final Editor<AccountDTO> editor = grid.getEditor();

    private final AccountClient accountClient;

    private final List<AccountDTO> dataSource;

    private final Button updateButton;
    private final Button cancelButton;
    private Integer page;
    private Integer size;

    public AccountView(AccountClient accountClient) {
        this.accountClient = accountClient;
        page = 0;
        size = 10;
        this.dataSource = accountClient.getPage(page, size)
                .getBody()
                .stream().collect(Collectors.toList());
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


        Grid.Column<AccountDTO> idColumn = createIdColumn();
        Grid.Column<AccountDTO> nameColumn = createNameColumn();
        Grid.Column<AccountDTO> lastnameColumn = createLastnameColumn();
        Grid.Column<AccountDTO> birthDateColumn = createBirthDateColumn();
        Grid.Column<AccountDTO> emailColumn = createEmailColumn();
        Grid.Column<AccountDTO> rolesColumn = createRolesColumn();
        Grid.Column<AccountDTO> phoneNumberColumn = createphoneNumberColumn();
        Grid.Column<AccountDTO> passwordColumn = createpasswordColumn();
        Grid.Column<AccountDTO> secQuestionColumn = createSecQuestionColumn();
        Grid.Column<AccountDTO> ansQuestionColumn = createAnsQuestionColumn();
        Grid.Column<AccountDTO> updateColumn = createEditColumn();
        createDeleteColumn();

        Binder <AccountDTO> binder = createBinder();

        createIdField(binder, idValidationMessage, idColumn);
        createNameField(binder, nameValidationMessage, nameColumn);
        createLastnameField(binder, lastnameValidationMessage, lastnameColumn);
        createBirthDateField(binder, bithDateValidationMessage, birthDateColumn);
        createEmailField(binder, emailValidationMessage, emailColumn);
        createRolesField(binder, rolesValidationMessage, rolesColumn);
        createPhoneNumberField(binder, phoneNumberValidationMessage, phoneNumberColumn);
        createPasswordField(binder, passwordValidationMessage, passwordColumn);
        createSecQuestionField(binder,secQuestionValidationMessage,secQuestionColumn);
        createAnsQuestionField(binder,ansQuestionValidationMessage,ansQuestionColumn);


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

    private void createLastnameField(Binder<AccountDTO> binder,
                                     ValidationMessage lastnameValidationMessage,
                                     Grid.Column<AccountDTO> lastnameColumn) {
        TextField lastnameField = new TextField();
        lastnameField.setWidthFull();
        binder.forField(lastnameField).asRequired("Answer question must not be empty")
                .withStatusLabel(lastnameValidationMessage)
                .bind(AccountDTO::getLastName, AccountDTO::setLastName);
        lastnameColumn.setEditorComponent(lastnameField);

    }

    private Grid.Column<AccountDTO> createLastnameColumn() {
        return grid.addColumn(AccountDTO::getLastName)
                .setHeader("Lastname").setWidth("100px").setFlexGrow(0);
    }

    private void createAnsQuestionField(Binder<AccountDTO> binder,
                                        ValidationMessage ansQuestionValidationMessage,
                                        Grid.Column<AccountDTO> ansQuestionColumn) {

        PasswordField ansQuestionField = new PasswordField();
        ansQuestionField.setWidthFull();
        binder.forField(ansQuestionField).asRequired("Answer question must not be empty")
                .withStatusLabel(ansQuestionValidationMessage)
                .bind(AccountDTO::getAnswerQuestion, AccountDTO::setAnswerQuestion);
        ansQuestionColumn.setEditorComponent(ansQuestionField);
    }

    private Grid.Column<AccountDTO> createAnsQuestionColumn() {
        return grid.addColumn(AccountDTO::getAnswerQuestion).setHeader("Answer Question")
                .setWidth("100px")
                .setFlexGrow(0);
    }

    private void createSecQuestionField(Binder<AccountDTO> binder,
                                        ValidationMessage secQuestionValidationMessage,
                                        Grid.Column<AccountDTO> secQuestionColumn) {
        TextField secQuestionField = new TextField();
        secQuestionField.setWidthFull();
        binder.forField(secQuestionField).asRequired("Secret Question must not be empty")
                .withStatusLabel(secQuestionValidationMessage)
                .bind(AccountDTO::getSecurityQuestion, AccountDTO::setSecurityQuestion);
        secQuestionColumn.setEditorComponent(secQuestionField);
    }

    private Grid.Column<AccountDTO> createSecQuestionColumn() {
        return grid.addColumn(AccountDTO::getSecurityQuestion).setHeader("Secret Question")
                .setWidth("100px")
                .setFlexGrow(0);
    }

    private void createPasswordField(Binder<AccountDTO> binder,
                                     ValidationMessage passwordValidationMessage,
                                     Grid.Column<AccountDTO> passwordColumn) {
        PasswordField passwordField = new PasswordField();
        passwordField.setWidthFull();
        binder.forField(passwordField).asRequired("Password must not be empty")
                .withStatusLabel(passwordValidationMessage)
                .bind(AccountDTO::getPassword, AccountDTO::setPassword);
        passwordColumn.setEditorComponent(passwordField);
    }

    private Grid.Column<AccountDTO> createpasswordColumn() {
        return grid.addColumn(AccountDTO::getPassword)
                .setHeader("Password").setWidth("100px").setFlexGrow(1);
    }

    private void createPhoneNumberField(Binder<AccountDTO> binder,
                                        ValidationMessage phoneNumberValidationMessage,
                                        Grid.Column<AccountDTO> phoneNumberColumn) {
        TextField phoneNumberField = new TextField();
        phoneNumberField.setWidthFull();
        binder.forField(phoneNumberField).asRequired("PhoneNumber must not be empty")
                .withStatusLabel(phoneNumberValidationMessage)
                .bind(AccountDTO::getPhoneNumber, AccountDTO::setPhoneNumber);
        phoneNumberColumn.setEditorComponent(phoneNumberField);
    }

    private Grid.Column<AccountDTO> createphoneNumberColumn() {
        return grid.addColumn(AccountDTO::getPhoneNumber)
                .setHeader("Phone Number").setWidth("100px").setFlexGrow(0);
    }

    private void createRolesField(Binder<AccountDTO> binder,
                                  ValidationMessage rolesValidationMessage,
                                  Grid.Column<AccountDTO> rolesColumn) {
        MultiSelectComboBox<RoleDTO> comboBox = new MultiSelectComboBox<>();
        comboBox.setWidthFull();
        binder.forField(comboBox).asRequired("Role must not be empty")
                .withStatusLabel(rolesValidationMessage)
                .bind(AccountDTO::getRoles, AccountDTO::setRoles);
        Set<RoleDTO> roles = accountClient.getAllRoles().getBody();
        Iterator iterator = roles.iterator();
        var list = new ArrayList<RoleDTO>();
        while (iterator.hasNext()) {
            list.add((RoleDTO) iterator.next());
        }
        var roleArray = new RoleDTO[list.size()];
        for (int i = 0; i < roleArray.length-1; i++) {
            roleArray[i] = list.get(i);
        }
            comboBox.setItems(roleArray);
        rolesColumn.setEditorComponent(comboBox);
    }

    private void createEmailField(Binder<AccountDTO> binder,
                                  ValidationMessage emailValidationMessage,
                                  Grid.Column<AccountDTO> emailColumn) {
        TextField emailField = new TextField();
        emailField.setWidthFull();
        binder.forField(emailField).asRequired("Email must not be empty")
                .withStatusLabel(emailValidationMessage)
                .bind(AccountDTO::getEmail, AccountDTO::setEmail);
        emailColumn.setEditorComponent(emailField);
    }

    private void createBirthDateField(Binder<AccountDTO> binder,
                                      ValidationMessage bithDateValidationMessage,
                                      Grid.Column<AccountDTO> birthDateColumn) {
        DatePicker birthDateField = new DatePicker();
        birthDateField.setWidthFull();
        binder.forField(birthDateField).asRequired("BirthDate must not be empty")
                .withStatusLabel(bithDateValidationMessage)
                .bind(AccountDTO::getBirthDate, AccountDTO::setBirthDate);
        birthDateColumn.setEditorComponent(birthDateField);
    }


    private void createNameField(Binder<AccountDTO> binder,
                                 ValidationMessage nameValidationMessage,
                                 Grid.Column<AccountDTO> nameColumn) {
        TextField nameField = new TextField();
        nameField.setWidthFull();
        binder.forField(nameField).asRequired("Name must not be empty")
                .withStatusLabel(nameValidationMessage)
                .withValidator(name -> name.length() >= 2 && name.length() <= 20,
                        "Name must be between 2 and 20 characters")
                .bind(AccountDTO::getFirstName, AccountDTO::setFirstName);
        nameColumn.setEditorComponent(nameField);
    }

    private void createIdField(Binder<AccountDTO> binder,
                               ValidationMessage idValidationMessage,
                               Grid.Column<AccountDTO> idColumn) {
        IntegerField idField = new IntegerField();
        idField.setWidthFull();
        binder.forField(idField)
                .asRequired("Id must not be empty")
                .withStatusLabel(idValidationMessage)
                .bind(accountDTO -> Math.toIntExact(accountDTO.getId()),
                        (accountDTO, Integer) -> accountDTO.setId(Integer.longValue()));
        idColumn.setEditorComponent(idField);
    }

    private Binder<AccountDTO> createBinder() {
        Binder<AccountDTO> binder = new Binder<>(AccountDTO.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        return binder;
    }

    private Grid.Column<AccountDTO> createDeleteColumn() {
        return grid.addComponentColumn(account -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                if (editor.isOpen()) {
                    editor.cancel();
                }
                if (grid.getDataProvider().isInMemory() && grid.getDataProvider().getClass() == ListDataProvider.class) {
                    ListDataProvider<AccountDTO> dataProvider = (ListDataProvider<AccountDTO>) grid.getDataProvider();
                    accountClient.deleteAccountById(account.getId());
                    dataProvider.getItems().remove(account);
                }
                grid.getDataProvider().refreshAll();
            });
            return deleteButton;
        }).setWidth("150px").setFlexGrow(0);
    }


    private Grid.Column<AccountDTO> createEditColumn() {
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

    private Grid.Column<AccountDTO> createRolesColumn() {
        return grid.addColumn(AccountDTO::getRoles).setHeader("AccountRoles")
                .setWidth("400px").setFlexGrow(0);
    }

    private Grid.Column<AccountDTO> createEmailColumn() {
        return grid.addColumn(AccountDTO::getEmail).setHeader("Email")
                .setWidth("150px").setFlexGrow(0);
    }

    private Grid.Column<AccountDTO> createBirthDateColumn() {
        return grid.addColumn(AccountDTO::getBirthDate).setHeader("BirthDate")
                .setWidth("100px").setFlexGrow(0);
    }


    private Grid.Column<AccountDTO> createNameColumn() {
        return grid.addColumn(AccountDTO::getFirstName).setHeader("Name")
                .setWidth("100px").setFlexGrow(0);
    }

    private Grid.Column<AccountDTO> createIdColumn() {
        return grid.addColumn(AccountDTO::getId)
                .setHeader("Id").setWidth("60px").setFlexGrow(0);
    }

    private void addEditorListeners() {
        editor.addSaveListener(e -> {
            accountClient.updateAccountDTOById(e.getItem().getId(), e.getItem());
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

        MultiSelectComboBox<RoleDTO> rolesSelectBox = new MultiSelectComboBox<>("Roles");
        Set<RoleDTO> roles = accountClient.getAllRoles().getBody()
                .stream().collect(Collectors.toSet());
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            rolesSelectBox.setItems((RoleDTO) iterator.next(), (RoleDTO)iterator.next());
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
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setFirstName(nameTextField.getValue());
            accountDTO.setLastName(lastnameTextField.getValue());
            accountDTO.setBirthDate(birthDayDateField.getValue());
            accountDTO.setPhoneNumber(phoneNumberTextField.getValue());
            accountDTO.setEmail(emailTextField.getValue());
            accountDTO.setRoles(rolesSelectBox.getValue());
            accountDTO.setPassword(passwordField.getValue());
            accountDTO.setSecurityQuestion(securityQuestionField.getValue());
            accountDTO.setAnswerQuestion(answerQuestionField.getValue());
            AccountDTO savedAccount = accountClient.createAccountDTO(accountDTO).getBody();
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
