package app.controllers.view;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Header extends HorizontalLayout {


    //ЭТО ВСЕ КНОПКИ СЛЕВА
    private final H1 header = new H1("Airlines");
    private final Button buttonStart = new Button(header, e -> UI.getCurrent().navigate("/"));
    Button buyAndManage = new Button("Покупка и управление", VaadinIcon.SHOP.create());
    Button s7Priority = new Button("S7 Priority", VaadinIcon.ANGLE_DOUBLE_UP.create());
    Button info = new Button("Информация", VaadinIcon.INFO.create());
    Button business = new Button("Бизнесу", VaadinIcon.BOOK.create());
    Button gate7 = new Button("Gate 7", VaadinIcon.AUTOMATION.create());
    HorizontalLayout buttonsLeft = new HorizontalLayout(buttonStart, buyAndManage, s7Priority, info, business, gate7);


    //ЭТО ВСЕ КНОПКИ СПРАВА
    Button searchButton = new Button(VaadinIcon.SEARCH.create());
    Accordion languages = setLanguages();
    Accordion currencies = setCurrencies();
    Button login = addLoginButton();



    HorizontalLayout buttonsRight = new HorizontalLayout(searchButton, currencies, languages, login);

    public Header() {
        //надо установить этому контейнеру выравнивание слева
        Div containerLeft = new Div();
        containerLeft.add(buttonsLeft);

        //надо установить этому контейнеру выравнивание справа. Здесь значок поиска (лупа), выбор валюты, выбор языка, иконка с кнопкой логина
        Div containerRight = new Div();
        containerRight.add(buttonsRight);

        add(containerLeft, containerRight);

        setWidthFull();
        setAlignItems(Alignment.BASELINE);

    }


    private Accordion setLanguages() {
        Accordion accordionForLanguages = new Accordion();

        Span russian = new Span("russian");
        Span english = new Span("english");
        Span italian = new Span("italian");

        VerticalLayout languages = new VerticalLayout(russian,
                english, italian);
        languages.setSpacing(false);
        languages.setPadding(false);
        accordionForLanguages.add("languages", languages);
        return accordionForLanguages;
    }

    private Accordion setCurrencies() {
        Accordion accordionForCurrencies = new Accordion();

        Span rub = new Span("rub");
        Span dollar = new Span("dollar");
        Span euro = new Span("euro");

        VerticalLayout currencies = new VerticalLayout(rub,
                dollar, euro);
        currencies.setSpacing(false);
        currencies.setPadding(false);
        accordionForCurrencies.add("currencies", currencies);
        return accordionForCurrencies;
    }


    private Button addLoginButton() {
        Button loginButton = new Button("Войти");
        loginButton.addClickListener(e -> {
            UI.getCurrent().getPage().setLocation("http://localhost:8084/login");
        });
        return loginButton;
    }



}