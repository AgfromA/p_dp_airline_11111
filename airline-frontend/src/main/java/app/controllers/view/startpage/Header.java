package app.controllers.view.startpage;


import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import java.util.Locale;


@JsModule("./generated/jar-resources/custom-icon.js")
public class Header extends HorizontalLayout {

    private I18NProvider i18NProvider;


    public Header() {
        HorizontalLayout buttonsLeft = getButtonsLeft();
        HorizontalLayout buttonsRight = getButtonsRight();
        add(buttonsLeft, buttonsRight);
        setWidthFull();

    }

    public HorizontalLayout getButtonsLeft() {
        HorizontalLayout buttonsLeft = new HorizontalLayout();

        StreamResource imageResource = new StreamResource("logo.svg",
                () -> getClass().getResourceAsStream("/icons/logo.svg"));
        Image logoS7 = new Image(imageResource, "Logo S7");
        logoS7.setWidth("150px");
        logoS7.setHeight("auto");

        Button button1 = new Button("Buying and management");
        button1.getElement().getStyle().set("background-color", "transparent");
        button1.getElement().getStyle().set("color", "black");

        Button button2 = new Button("S7 Priority");
        button2.getElement().getStyle().set("background-color", "transparent");
        button2.getElement().getStyle().set("color", "black");

        Button button3 = new Button("Information");
        button3.getElement().getStyle().set("background-color", "transparent");
        button3.getElement().getStyle().set("color", "black");

        Button button4 = new Button("Business");
        button4.getElement().getStyle().set("background-color", "transparent");
        button4.getElement().getStyle().set("color", "black");

        Button button5 = new Button("Gate 7");
        button5.getElement().getStyle().set("background-color", "transparent");
        button5.getElement().getStyle().set("color", "black");

        buttonsLeft.add(logoS7);
        buttonsLeft.add(button1);
        buttonsLeft.add(button2);
        buttonsLeft.add(button3);
        buttonsLeft.add(button4);
        buttonsLeft.add(button5);

        buttonsLeft.getElement().getStyle().set("position", "sticky");
        buttonsLeft.getElement().getStyle().set("top", "0");
        buttonsLeft.setAlignItems(Alignment.END);

        return buttonsLeft;
    }


    //СОЗДАЛИ КНОПКИ СПРАВА
    private HorizontalLayout getButtonsRight() {
        HorizontalLayout buttonsRight = new HorizontalLayout();

        Button buttonLoup = getButtonLoup();
        Button currencyButton = getCurrencyButton();
        Button languageButton = getLanguageButton();
        Button loginButton = getLoginButton();

        buttonsRight.add(buttonLoup);
        buttonsRight.add(currencyButton);
        buttonsRight.add(languageButton);
        buttonsRight.add(loginButton);

        buttonsRight.setAlignItems(Alignment.END);
        buttonsRight.getElement().getStyle().set("margin-left", "auto");
        buttonsRight.getElement().getStyle().set("position", "sticky");
        buttonsRight.getElement().getStyle().set("top", "0");

        return buttonsRight;
    }


    //СОЗДАЛИ ОДНУ ИЗ КНОПОК, КОТОРАЯ БУДЕТ СПРАВА - ЗНАЧОК ЛУПЫ, КОТОРУЮ НАЖИМАЕШЬ И ПОЯВЛЯЕТСЯ ПОЛЕ ДЛЯ ВВОДА
    private Button getButtonLoup() {
        Button searchButton = new Button(VaadinIcon.SEARCH.create());
        TextField searchField = new TextField();
        Dialog dialog = new Dialog();
        dialog.add(searchField);
        searchButton.addClickListener(event -> dialog.open());
        searchButton.getElement().getStyle().set("background-color", "transparent");
        searchButton.getElement().getStyle().set("color", "black");
        return searchButton;
    }


    //СОЗДАЛИ ОДНУ ИЗ КНОПОК СПРАВА СО ЗНАКОМ RUB, КОТОРУЮ НАЖИМАЕШЬ И МОЖНО ПОМЕНЯТЬ ВАЛЮТУ
    private Button getCurrencyButton() {

        Button mainButton = new Button(new Icon(VaadinIcon.COIN_PILES));
        mainButton.getElement().getStyle().set("background-color", "transparent");
        mainButton.getElement().getStyle().set("color", "black");

        Button button1 = new Button("DOLLAR");
        button1.setIcon(new Icon(VaadinIcon.DOLLAR));

        Button button2 = new Button("EURO");
        button2.setIcon(new Icon(VaadinIcon.EURO));

        Button button3 = new Button("COINS");
        button3.setIcon(new Icon(VaadinIcon.COINS));

        Select<Button> select = new Select<>();
        select.setItems(button1, button2, button3);

        Dialog dialog = new Dialog();

        Label title = new Label("Choose a currency");
        title.getElement().getStyle().set("font-weight", "bold");

        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        closeButton.getElement().getStyle().set("background-color", "transparent");
        closeButton.getElement().getStyle().set("color", "black");
        closeButton.addClickListener(event -> dialog.close());

        HorizontalLayout headerDialog = new HorizontalLayout();
        headerDialog.setWidthFull();
        headerDialog.add(title, closeButton);
        headerDialog.setAlignItems(Alignment.CENTER);

        VerticalLayout layout = new VerticalLayout();
        layout.add(headerDialog, select);

        dialog.add(layout);

        mainButton.addClickListener(e -> {
            dialog.open();

            button1.addClickListener(event -> {
                mainButton.setIcon(button1.getIcon());
                dialog.close();
            });

            button2.addClickListener(event -> {
                mainButton.setIcon(button2.getIcon());
                dialog.close();
            });

            button3.addClickListener(event -> {
                mainButton.setIcon(button3.getIcon());
                dialog.close();
            });
        });

        return mainButton;
    }


    //СОЗДАЛИ ОДНУ ИЗ КНОПОК СПРАВА - КНОПКА ВХОДА НА САЙТ
    private Button getLoginButton() {
        Button loginButton = new Button("Login");
        loginButton.setIcon(new Icon(VaadinIcon.USER));

        LoginOverlay loginOverlay = new LoginOverlay();
        loginButton.addClickListener(event -> loginOverlay.setOpened(true));
        loginButton.getElement().getStyle().set("background-color", "transparent");
        loginButton.getElement().getStyle().set("color", "black");

        return loginButton;
    }


    private Button getLanguageButton() {
        Button languageButton = new Button(new Icon(VaadinIcon.FLAG));


        ComboBox<Locale> comboBox = new ComboBox<>();
        comboBox.setLabel("Choose the language");

        comboBox.setItems(
                new Locale("ru"),
                new Locale("en"),
                new Locale("fr"),
                new Locale("de"),
                new Locale("es"),
                new Locale("it")
        );

        comboBox.setItemLabelGenerator(locale -> {
            switch (locale.getLanguage()) {
                case "ru":
                    return "Русский";
                case "en":
                    return "English";
                case "fr":
                    return "Français";
                case "de":
                    return "Deutsch";
                case "es":
                    return "Español";
                case "it":
                    return "Italiano";
                default:
                    return locale.getDisplayLanguage();
            }
        });

        comboBox.setRenderer(new ComponentRenderer<>(locale -> {
            Icon icon;
            switch (locale.getLanguage()) {
                case "ru":
                    icon = new Icon(VaadinIcon.FLAG_O);
                    break;
                case "en":
                    icon = new Icon(VaadinIcon.ACADEMY_CAP);
                    break;
                case "fr":
                    icon = new Icon(VaadinIcon.ALARM);
                    break;
                case "de":
                    icon = new Icon(VaadinIcon.GLOBE);
                    break;
                case "es":
                    icon = new Icon(VaadinIcon.ARCHIVE);
                    break;
                case "it":
                    icon = new Icon(VaadinIcon.ARROWS);
                    break;
                default:
                    icon = new Icon(VaadinIcon.QUESTION);
                    break;
            }
            return new HorizontalLayout(icon, new Text(locale.getDisplayLanguage()));
        }));

        comboBox.addValueChangeListener(event -> {
            VaadinSession.getCurrent().setLocale(event.getValue());
            comboBox.setOpened(false);
        });

        Dialog dialog = new Dialog();
        dialog.add(comboBox);
        languageButton.addClickListener(event -> dialog.open());

        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        closeButton.getElement().getStyle().set("background-color", "transparent");
        closeButton.getElement().getStyle().set("color", "black");
        closeButton.addClickListener(event -> dialog.close());

        return languageButton;
    }


}


