package app.controllers.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.StreamResource;
import lombok.Getter;

import java.util.Locale;

@Getter

//@JsModule("./generated/jar-resources/custom-icon.js")
public class Header extends HorizontalLayout {

    public Header() {
        HorizontalLayout buttonsLeft = getButtonsLeft();
        HorizontalLayout buttonsRight = getButtonsRight();
        add(buttonsLeft, buttonsRight);
        setWidthFull();
        setHeight("100px");
        setAlignItems(Alignment.CENTER);
    }

    public HorizontalLayout getButtonsLeft() {
        HorizontalLayout buttonsLeft = new HorizontalLayout();

        StreamResource imageResource = new StreamResource("logo.svg",
                () -> getClass().getResourceAsStream("/icons/logo.svg"));
        Image logoS7 = new Image(imageResource, "Logo S7");
        logoS7.setWidth("150px");
        logoS7.setHeight("auto");

        Button button1 = new Button("Покупка и управление");
        button1.getElement().getStyle().set("background-color", "transparent");
        button1.getElement().getStyle().set("color", "black");

        Button button2 = new Button("S7 Priority");
        button2.getElement().getStyle().set("background-color", "transparent");
        button2.getElement().getStyle().set("color", "black");

        Button button3 = new Button("Информация");
        button3.getElement().getStyle().set("background-color", "transparent");
        button3.getElement().getStyle().set("color", "black");

        Button button4 = new Button("Бизнесу");
        button4.getElement().getStyle().set("background-color", "transparent");
        button4.getElement().getStyle().set("color", "black");

        Button button5 = new Button("Gate 7");
        button5.getElement().getStyle().set("background-color", "transparent");
        button5.getElement().getStyle().set("color", "black");

        buttonsLeft.add(logoS7);
        buttonsLeft.add(button1, button2, button3, button4, button5);

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

        buttonsRight.add(buttonLoup, currencyButton, languageButton, loginButton);

        buttonsRight.setAlignItems(Alignment.END);
        buttonsRight.getElement().getStyle().set("margin-left", "auto");
        buttonsRight.getElement().getStyle().set("position", "sticky");
        buttonsRight.getElement().getStyle().set("top", "0");

        return buttonsRight;
    }


    //СОЗДАЛИ ОДНУ ИЗ КНОПОК, КОТОРАЯ БУДЕТ СПРАВА - ЗНАЧОК ЛУПЫ, КОТОРУЮ НАЖИМАЕШЬ И ПОЯВЛЯЕТСЯ ПОЛЕ ДЛЯ ВВОДА
    private Button getButtonLoup() {
        Button searchButton = new Button(new Icon (VaadinIcon.SEARCH));
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

        Button currencyButton = new Button(new Icon(VaadinIcon.COIN_PILES));
        currencyButton.getElement().getStyle().set("background-color", "transparent");
        currencyButton.getElement().getStyle().set("color", "black");

        ComboBox<Button> currencies = new ComboBox<>();
        currencies.setItems(
                new Button("RUB"),
                new Button("DOLLAR"),
                new Button("EURO")
        );

        Label titleDialogCurr = new Label("Выберите валюту");
        titleDialogCurr.getElement().getStyle().set("font-weight", "bold");

        Dialog dialogCurr = new Dialog();

        Button closeCurrencyButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        closeCurrencyButton.getElement().getStyle().set("background-color", "transparent");
        closeCurrencyButton.getElement().getStyle().set("color", "black");

        currencyButton.addClickListener(event -> dialogCurr.open());
        closeCurrencyButton.addClickListener(event -> dialogCurr.close());

        HorizontalLayout headerDialogCurr = new HorizontalLayout();
        headerDialogCurr.add(titleDialogCurr, closeCurrencyButton);
        headerDialogCurr.setAlignItems(Alignment.BASELINE);

        VerticalLayout allDialogCurr = new VerticalLayout();
        allDialogCurr.add(headerDialogCurr, currencies);

        dialogCurr.add(allDialogCurr);

        currencies.setRenderer(new ComponentRenderer<>(button -> {
            Icon icon = null;
            switch (button.getText()) {
                case "RUB":
                    icon = new Icon(VaadinIcon.COINS);
                    break;
                case "DOLLAR":
                    icon = new Icon(VaadinIcon.DOLLAR);
                    break;
                case "EURO":
                    icon = new Icon(VaadinIcon.EURO);
                    break;
            }
            return new HorizontalLayout(icon, new Text(button.getText()));
        }));

        currencies.addValueChangeListener(e -> {
            Button selectedButton = e.getValue();
            switch (selectedButton.getText()) {
                case "RUB":
                    currencyButton.setIcon(new Icon(VaadinIcon.COINS));
                    break;
                case "DOLLAR":
                    currencyButton.setIcon(new Icon(VaadinIcon.DOLLAR));
                    break;
                case "EURO":
                    currencyButton.setIcon(new Icon(VaadinIcon.EURO));
                    break;
            }
            closeCurrencyButton.click();
        });

        return currencyButton;
    }


    private Button getLanguageButton() {
        Button languageButton = new Button(new Icon(VaadinIcon.FLAG));
        languageButton.getElement().getStyle().set("background-color", "transparent");
        languageButton.getElement().getStyle().set("color", "black");

        Button closeLanguageButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        closeLanguageButton.getElement().getStyle().set("background-color", "transparent");
        closeLanguageButton.getElement().getStyle().set("color", "black");

        Label titleDialogLang = new Label("Выберите язык");
        titleDialogLang.getElement().getStyle().set("font-weight", "bold");

        HorizontalLayout headerDialogLang = new HorizontalLayout();
        headerDialogLang.add(titleDialogLang, closeLanguageButton);
        headerDialogLang.setAlignItems(Alignment.BASELINE);

        ComboBox<Locale> languages = new ComboBox<>();

        VerticalLayout allDialogLang = new VerticalLayout();
        allDialogLang.add(headerDialogLang, languages);

        Dialog dialog = new Dialog();
        dialog.add(allDialogLang);

        languageButton.addClickListener(event -> dialog.open());
        closeLanguageButton.addClickListener(event -> dialog.close());

        languages.setItems(
                new Locale("ru"),
                new Locale("en"),
                new Locale("fr"),
                new Locale("de"),
                new Locale("es"),
                new Locale("it")
        );

        languages.setItemLabelGenerator(locale -> {
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

        languages.setRenderer(new ComponentRenderer<>(locale -> {
            Icon icon = null;
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
            }
            return new HorizontalLayout(icon, new Text(locale.getDisplayLanguage()));
        }));


        languages.addValueChangeListener(e -> {
            Locale selectedLocale = e.getValue();
            switch (selectedLocale.toLanguageTag()) {
                case "ru":
                    languageButton.setIcon(new Icon(VaadinIcon.FLAG_O));
                    break;
                case "en":
                    languageButton.setIcon(new Icon(VaadinIcon.ACADEMY_CAP));
                    break;
                case "fr":
                    languageButton.setIcon(new Icon(VaadinIcon.ALARM));
                    break;
                case "de":
                    languageButton.setIcon(new Icon(VaadinIcon.GLOBE));
                    break;
                case "es":
                    languageButton.setIcon(new Icon(VaadinIcon.ARCHIVE));
                    break;
                case "it":
                    languageButton.setIcon(new Icon(VaadinIcon.ARROWS));
                    break;
            }
            closeLanguageButton.click();
        });

        return languageButton;
    }


    //СОЗДАЛИ ОДНУ ИЗ КНОПОК СПРАВА - КНОПКА ВХОДА НА САЙТ
    private Button getLoginButton() {
        Button loginButton = new Button("Войти");
        loginButton.setIcon(new Icon(VaadinIcon.USER));

        LoginOverlay loginOverlay = new LoginOverlay();
        loginButton.addClickListener(event -> loginOverlay.setOpened(true));
        loginButton.getElement().getStyle().set("background-color", "transparent");
        loginButton.getElement().getStyle().set("color", "black");

        return loginButton;
    }
}


