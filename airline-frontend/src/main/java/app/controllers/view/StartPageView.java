package app.controllers.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import com.vaadin.flow.component.html.Image;

@Route(value = "/")
@PageTitle("Start Page")
public class StartPageView extends VerticalLayout {

    private Carousel carousel = new Carousel();
    private Header header = new Header();


    public StartPageView() {
        add(header);
        add(carousel);

        TabSheet tabSheet = getFormWithTabs();
        add(tabSheet);
        setSizeFull();
    }


    //ФОРМА С ВКЛАДКАМИ "ПОКУПКА", "РЕГИСТРАЦИЯ НА РЕЙС", "МОИ БРОНИРОВАНИЯ", "СТАТУС РЕЙСА"

    private TabSheet getFormWithTabs() {
        TabSheet externalTabs = new TabSheet(); //ВНЕШНИЙ
        externalTabs.setSizeFull();
        externalTabs.addThemeVariants(TabSheetVariant.LUMO_BORDERED);

        Tab searchFormTab = new Tab("Покупка");

        TabSheet innerSearchFormTabSheet = new TabSheet(); //ВНУТРЕННИЙ ДЛЯ ВКЛАДКИ "ПОКУПКА", куда кладем Авиабилеты, Отели, Авиа + Отель, Экскурсии, Апартаменты, Аренда авто, Трансфер
        innerSearchFormTabSheet.setHeight("auto");
        innerSearchFormTabSheet.getElement().getStyle().set("justify-content", "center");
        innerSearchFormTabSheet.getElement().getStyle().set("display", "flex");
        innerSearchFormTabSheet.getElement().getStyle().set("align-items", "center");

        //Авиабилеты 1
        SearchForm searchForm = new SearchForm();
        HorizontalLayout options = getOptions();
        options.setSizeFull();

        Button createComplexRouteButton = new Button("Составить сложный маршрут");
        createComplexRouteButton.getElement().getStyle().set("color", "#000");
        createComplexRouteButton.getElement().getStyle().set("font-weight", "lighter");
        createComplexRouteButton.getElement().getStyle().set("font-size", "medium");
        createComplexRouteButton.getElement().getStyle().set("position", "absolute");
        createComplexRouteButton.getElement().getStyle().set("left", "0");
        createComplexRouteButton.getElement().getStyle().set("background-color", "transparent");
        createComplexRouteButton.getElement().getStyle().set("justify-content", "flex-start");
        createComplexRouteButton.getElement().getStyle().set("align-items", "flex-end");
        Icon icon = new Icon(VaadinIcon.CAR);
        icon.getElement().getStyle().set("color", "#9ACD32");
        createComplexRouteButton.setIcon(icon);

        HorizontalLayout footer = new HorizontalLayout(createComplexRouteButton, options);
        footer.setSizeFull();
        footer.setAlignItems(Alignment.BASELINE);

        VerticalLayout contentForSearchForm = new VerticalLayout(searchForm, footer);
        contentForSearchForm.setSizeFull();

        Tab flightsTickets = new Tab("Авиабилеты");
        innerSearchFormTabSheet.add(flightsTickets, contentForSearchForm);

        //Отели 2
        HorizontalLayout hotelsLayout = getHotels();
        Tab hotelsTab = new Tab("Отели");
        innerSearchFormTabSheet.add(hotelsTab, hotelsLayout);

        //Авиа + Отель 3
        HorizontalLayout flightsAndHotels = getFlightsAndHotels();
        Tab flightsAndHotelsTab = new Tab("Авиа + Отель");
        innerSearchFormTabSheet.add(flightsAndHotelsTab, flightsAndHotels);

        //Экскурсии 4
        Tab excursionsTab = new Tab(new Anchor("ссылка", "Экскурсии"));
        HorizontalLayout layout = new HorizontalLayout();
        innerSearchFormTabSheet.add(excursionsTab, layout);

        //Апартаменты 5
        Tab apartmentsTab = new Tab(new Anchor("ссылка", "Апартаменты"));
        HorizontalLayout layout2 = new HorizontalLayout();
        innerSearchFormTabSheet.add(apartmentsTab, layout2);

        //Аренда авто 6
        HorizontalLayout rentCar = getRentCar();
        Tab rentCarTab = new Tab("Аренда авто");
        innerSearchFormTabSheet.add(rentCarTab, rentCar);

        //Трансфер 7
        Tab transferTab = new Tab(new Anchor("ссылка", "Трансфер"));
        HorizontalLayout layout3 = new HorizontalLayout();
        innerSearchFormTabSheet.add(transferTab, layout3);

        externalTabs.add(searchFormTab, innerSearchFormTabSheet); //ПОЛОЖИЛИ ВО ВНЕШНИЙ КНОПКУ ПЕРВОЙ ВКЛАДКИ "ПОКУПКА" И ЕЕ СОДЕРЖИМОЕ
        externalTabs.setSizeFull();

        //ПОЛОЖИЛИ ВО ВНЕШНИЙ КНОПКУ ВТОРОЙ ВКЛАДКИ "РЕГИСТРАЦИЯ НА РЕЙС" И ЕЕ СОДЕРЖИМОЕ
        VerticalLayout checkInFlightLayout = getCheckInFlight();
        Tab checkInFlightTab = new Tab("Регистрация на рейс");
        externalTabs.add(checkInFlightTab, checkInFlightLayout);

        //ПОЛОЖИЛИ ВО ВНЕШНИЙ КНОПКУ ТРЕТЬЕЙ ВКЛАДКИ "МОИ БРОНИРОВАНИЯ" И ЕЕ СОДЕРЖИМОЕ
        VerticalLayout bookingsLayout = getBookings();
        Tab bookingsTab = new Tab("Мои бронирования");
        externalTabs.add(bookingsTab, bookingsLayout);

        //ПОЛОЖИЛИ ВО ВНЕШНИЙ КНОПКУ ЧЕТВЕРТОЙ ВКЛАДКИ "СТАТУС РЕЙСА" И ЕЕ СОДЕРЖИМОЕ, СОСТОЯЩЕЕ ИЗ ДВУХ ВКЛАДОК
        Tab flightStatusTab = new Tab("Статус рейса");
        TabSheet innerflightStatusTabSheet = new TabSheet();

        VerticalLayout byRouteLayout = getByRoute();
        Tab byRoute = new Tab("По маршруту");
        innerflightStatusTabSheet.add(byRoute, byRouteLayout);

        VerticalLayout byFlightNumberLayout = getByFlightNumber();
        Tab byFlightNumber = new Tab("По номеру рейса");
        innerflightStatusTabSheet.add(byFlightNumber, byFlightNumberLayout);
        innerflightStatusTabSheet.setSizeFull();

        externalTabs.add(flightStatusTab, innerflightStatusTabSheet);

        externalTabs.setSizeFull();
        externalTabs.getElement().getStyle().set("justify-content", "center");
        externalTabs.getElement().getStyle().set("display", "flex");
        externalTabs.getElement().getStyle().set("align-items", "center");

        return externalTabs;
    }


    //ВКЛАДКА "РЕГИСТРАЦИЯ НА РЕЙС"
    private VerticalLayout getCheckInFlight() {

        HorizontalLayout layout = new HorizontalLayout();

        TextField textField1 = new TextField("Фамилия пассажира");

        TextField textField2 = new TextField("Номер заказа, брони или билета");

        Button hintButton = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE));
        hintButton.getElement().getStyle().set("background-color", "transparent");
        hintButton.getElement().getStyle().set("width", "10px");
        hintButton.getElement().getStyle().set("height", "10px");
        hintButton.getElement().setAttribute("slot", "suffix");
        textField2.getElement().appendChild(hintButton.getElement());

        Dialog dialog = new Dialog();

        Button closeDialog = new Button(new Icon(VaadinIcon.CLOSE));
        closeDialog.getElement().getStyle().set("background-color", "transparent");
        closeDialog.getElement().getStyle().set("color", "#9ACD32");

        H1 headerHint = new H1("Название подсказки");

        HorizontalLayout headerDialog = new HorizontalLayout(headerHint, closeDialog);
        headerDialog.setWidth("100%");
        ;
        headerDialog.setAlignItems(Alignment.BASELINE);
        headerDialog.getElement().getStyle().set("display", "flex");
        headerDialog.getElement().getStyle().set("justify-content", "space-between");

        Label textHint = new Label("Текст подсказки");

        Div divider = new Div();
        divider.getElement().getStyle().set("height", "2px");
        divider.getElement().getStyle().set("background-color", "#C8C8C8");
        divider.setWidth("100%");

        VerticalLayout allDialog = new VerticalLayout(headerDialog, divider, textHint);
        allDialog.setWidth("100%");
        dialog.add(allDialog);
        dialog.setHeight("800px");
        dialog.setWidth("600px");

        closeDialog.addClickListener(e -> dialog.close());

        hintButton.addClickListener(event -> {
            dialog.open();
        });

        Button buttonLogin = new Button("Зарегистрироваться");
        buttonLogin.getElement().getStyle().set("background-color", "#9ACD32");
        buttonLogin.getElement().getStyle().set("color", "white");

        layout.add(textField1, textField2, buttonLogin);
        layout.getElement().getStyle().set("justify-content", "center");
        layout.getElement().getStyle().set("display", "flex");
        layout.getElement().getStyle().set("align-items", "center");
        layout.setSizeFull();
        layout.setAlignItems(Alignment.END);

        HorizontalLayout textDown = new HorizontalLayout();
        Label text = new Label("Онлайн-регистрация на рейс открывается за 30 часов до планового времени вылета");
        Anchor href = new Anchor("ссылка", "Подробнее");
        href.getElement().getStyle().set("color", "#9ACD32");
        textDown.add(text, href);
        textDown.getElement().getStyle().set("display", "flex");
        textDown.getElement().getStyle().set("justify-content", "flex-start");

        VerticalLayout verticalLayout = new VerticalLayout(layout, textDown);

        return verticalLayout;
    }

    //ВВКЛАДКА "МОИ БРОНИРОВАНИЯ"
    private VerticalLayout getBookings() {

        HorizontalLayout bookingsBody = new HorizontalLayout();

        TextField lastnameOrEmail = new TextField("Фамилия пассажира или email");

        TextField orderOrTicket = new TextField("Номер заказа, брони или билета");

        Button hintButton = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE));
        hintButton.getElement().getStyle().set("background-color", "transparent");
        hintButton.getElement().getStyle().set("width", "10px");
        hintButton.getElement().getStyle().set("height", "10px");
        hintButton.getElement().setAttribute("slot", "suffix");
        orderOrTicket.getElement().appendChild(hintButton.getElement());

        Dialog dialog = new Dialog();

        Button closeDialog = new Button(new Icon(VaadinIcon.CLOSE));
        closeDialog.getElement().getStyle().set("background-color", "transparent");
        closeDialog.getElement().getStyle().set("color", "#9ACD32");

        H1 headerHint = new H1("Название подсказки");

        HorizontalLayout headerDialog = new HorizontalLayout(headerHint, closeDialog);
        headerDialog.setWidth("100%");
        ;
        headerDialog.setAlignItems(Alignment.BASELINE);
        headerDialog.getElement().getStyle().set("display", "flex");
        headerDialog.getElement().getStyle().set("justify-content", "space-between");

        Label textHint = new Label("Текст подсказки");

        Div divider = new Div();
        divider.getElement().getStyle().set("height", "2px");
        divider.getElement().getStyle().set("background-color", "#C8C8C8");
        divider.setWidth("100%");

        VerticalLayout allDialog = new VerticalLayout(headerDialog, divider, textHint);
        allDialog.setWidth("100%");
        dialog.add(allDialog);
        dialog.setHeight("800px");
        dialog.setWidth("600px");

        closeDialog.addClickListener(e -> dialog.close());

        hintButton.addClickListener(event -> {
            dialog.open();
        });

        Button buttonBookings = new Button("Проверить статус");
        buttonBookings.getElement().getStyle().set("background-color", "#9ACD32");
        buttonBookings.getElement().getStyle().set("color", "white");

        bookingsBody.add(lastnameOrEmail, orderOrTicket, buttonBookings);
        bookingsBody.getElement().getStyle().set("justify-content", "center");
        bookingsBody.getElement().getStyle().set("display", "flex");
        bookingsBody.getElement().getStyle().set("align-items", "center");
        bookingsBody.setSizeFull();
        bookingsBody.setAlignItems(Alignment.END);

        HorizontalLayout textDown = new HorizontalLayout();
        Label text = new Label("Управление бронированием, отслеживание статуса и добавление дополнительных продуктов.");
        Anchor href = new Anchor("ссылка", "Подробнее");
        href.getElement().getStyle().set("color", "#9ACD32");
        textDown.add(text, href);
        textDown.getElement().getStyle().set("display", "flex");
        textDown.getElement().getStyle().set("justify-content", "flex-start");

        VerticalLayout bookings = new VerticalLayout(bookingsBody, textDown);
        bookings.setWidthFull();

        return bookings;
    }

    //ВВКЛАДКА "ПО МАРШРУТУ"
    private VerticalLayout getByRoute() {

        HorizontalLayout byRouteBody = new HorizontalLayout();

        TextField from = new TextField("Откуда");
        TextField to = new TextField("Куда");

        Button swapButton = new Button(new Icon(VaadinIcon.EXCHANGE));
        swapButton.getElement().getStyle().set("background-color", "#9ACD32");
        swapButton.getElement().getStyle().set("color", "white");

        swapButton.addClickListener(event -> {
            String data = from.getValue();
            from.setValue(to.getValue());
            to.setValue(data);
        });

        DatePicker date = new DatePicker("Дата");

        Button checkStatus = new Button("Проверить статус");
        checkStatus.getElement().getStyle().set("background-color", "#9ACD32");
        checkStatus.getElement().getStyle().set("color", "white");

        byRouteBody.add(from, swapButton, to, date, checkStatus);
        byRouteBody.getElement().getStyle().set("justify-content", "center");
        byRouteBody.getElement().getStyle().set("display", "flex");
        byRouteBody.getElement().getStyle().set("align-items", "center");
        byRouteBody.setSizeFull();
        byRouteBody.setAlignItems(Alignment.END);

        Label text = new Label("Поиск рейсов, отслеживание статуса, времени вылета и посадки самолёта.");

        VerticalLayout byRouteLayout = new VerticalLayout();
        byRouteLayout.add(byRouteBody, text);
        byRouteLayout.setWidthFull();

        return byRouteLayout;
    }

    //ВКЛАДКА "ПО НОМЕРУ РЕЙСА"
    private VerticalLayout getByFlightNumber() {

        HorizontalLayout byFlightNumberLayoutBody = new HorizontalLayout();

        TextField flightNumber = new TextField("Номер рейса");

        DatePicker date = new DatePicker("Дата");

        Button checkStatus = new Button("Проверить статус");
        checkStatus.getElement().getStyle().set("background-color", "#9ACD32");
        checkStatus.getElement().getStyle().set("color", "white");

        byFlightNumberLayoutBody.add(flightNumber, date, checkStatus);
        byFlightNumberLayoutBody.getElement().getStyle().set("justify-content", "center");
        byFlightNumberLayoutBody.getElement().getStyle().set("display", "flex");
        byFlightNumberLayoutBody.getElement().getStyle().set("align-items", "center");
        byFlightNumberLayoutBody.setSizeFull();
        byFlightNumberLayoutBody.setAlignItems(Alignment.END);

        Label text = new Label("Поиск рейсов, отслеживание статуса, времени вылета и посадки самолёта.");

        VerticalLayout byFlightNumberLayout = new VerticalLayout();
        byFlightNumberLayout.add(byFlightNumberLayoutBody, text);
        byFlightNumberLayout.setSizeFull();

        return byFlightNumberLayout;
    }

    //ВКЛАДКА "АВИАБИЛЕТЫ"
    private HorizontalLayout getOptions() {

        Checkbox flyingForWork = new Checkbox("Лечу по работе");
        Checkbox flyingWithPet = new Checkbox("Лечу с питомцем");
        Checkbox paymentByMiles = new Checkbox("Оплата милями");

        HorizontalLayout options = new HorizontalLayout(flyingForWork, flyingWithPet, paymentByMiles);
        options.getElement().getStyle().set("justify-content", "flex-end");
        options.getElement().getStyle().set("align-items", "flex-end");
        options.setSizeFull();

        return options;
    }

    //ВКЛАДКА "ОТЕЛИ"
    private HorizontalLayout getHotels() {

        HorizontalLayout hotels = new HorizontalLayout();

        TextField cityAndRegion = new TextField("Город, регион");

        DatePicker dateFrom = new DatePicker("Дата заселения");
        DatePicker dateTo = new DatePicker("Дата выселения");

        IntegerField adults = new IntegerField("Взрослые");
        adults.setHasControls(true);
        adults.setStep(1);
        adults.setMin(1);
        adults.setMax(10);

        IntegerField children = new IntegerField("Дети");
        children.setHasControls(true);
        children.setStep(1);
        children.setMin(1);
        children.setMax(10);

        Button searchButton = new Button(new Icon(VaadinIcon.SEARCH));
        searchButton.getElement().getStyle().set("background-color", "#9ACD32");
        searchButton.getElement().getStyle().set("color", "white");

        Text text = new Text("Цель поездки:");
        RadioButtonGroup<String> targetsTrip = new RadioButtonGroup<>();
        targetsTrip.setItems("Отдых", "Работа");
        targetsTrip.getElement().getStyle().set("justify-content", "flex-start");
        targetsTrip.getElement().getStyle().set("align-items", "flex-end");
        targetsTrip.getElement().getStyle().set("color", "#9ACD32");

        HorizontalLayout layoutRadioButton = new HorizontalLayout(text, targetsTrip);
        layoutRadioButton.setAlignItems(Alignment.BASELINE);

        HorizontalLayout layout = new HorizontalLayout(cityAndRegion, dateFrom, dateTo, adults, children, searchButton);
        layout.setAlignItems(Alignment.END);

        VerticalLayout verticalLayout = new VerticalLayout(layout, layoutRadioButton);

        hotels.add(verticalLayout);
        hotels.setSizeFull();

        return hotels;
    }

    //ВКЛАДКА АВИА + ОТЕЛЬ
    private HorizontalLayout getFlightsAndHotels() {

        HorizontalLayout flightsAndHotels = new HorizontalLayout();

        TextField cityFrom = new TextField("Откуда");
        TextField cityTo = new TextField("Куда");

        DatePicker dateFrom = new DatePicker("Дата заселения");
        DatePicker dateTo = new DatePicker("Дата выселения");

        IntegerField adults = new IntegerField("Взрослые");
        adults.setHasControls(true);
        adults.setStep(1);
        adults.setMin(1);
        adults.setMax(10);

        IntegerField children = new IntegerField("Дети");
        children.setHasControls(true);
        children.setStep(1);
        children.setMin(1);
        children.setMax(10);

        Button searchButton = new Button(new Icon(VaadinIcon.SEARCH));
        searchButton.getElement().getStyle().set("background-color", "#9ACD32");
        searchButton.getElement().getStyle().set("color", "white");

        flightsAndHotels.add(cityFrom, cityTo, dateFrom, dateTo, adults, children, searchButton);
        flightsAndHotels.setSizeFull();
        flightsAndHotels.setAlignItems(Alignment.END);

        return flightsAndHotels;
    }

    //ВКЛАДКА "АРЕНДА АВТО"
    private HorizontalLayout getRentCar() {

        HorizontalLayout rentCar = new HorizontalLayout();

        TextField cityOrAirportReceipt = new TextField("Город/Аэропорт получения");

        DatePicker receiving = new DatePicker("Дата получения");
        DatePicker refund = new DatePicker("Дата возврата");

        Button searchButton = new Button(new Icon(VaadinIcon.SEARCH));
        searchButton.getElement().getStyle().set("background-color", "#9ACD32");
        searchButton.getElement().getStyle().set("color", "white");

        rentCar.add(cityOrAirportReceipt, receiving, refund, searchButton);
        rentCar.setSizeFull();
        rentCar.setAlignItems(Alignment.END);

        return rentCar;
    }
}