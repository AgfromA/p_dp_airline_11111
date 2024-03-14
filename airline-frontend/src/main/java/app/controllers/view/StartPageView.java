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

    private Footer footer = new Footer();
    private Carousel carousel = new Carousel();
    private Header header = new Header();


    public StartPageView() {
        add(header);

        add(carousel);

        TabSheet tabSheet = getFormWithTabs();
        add(tabSheet);

        VerticalLayout stocks = getStocks();
        add(stocks);

        VerticalLayout promos = getPromos();
        add(promos);

        add(footer);

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

    //БЛОК "СПЕЦИАЛЬНЫЕ ПРЕДЛОЖЕНИЯ"
    private VerticalLayout getStocks() {

        Label stockText1 = new Label("Специальное предложение №1");
        stockText1.getElement().getStyle().set("font-size", "20px");
        stockText1.getElement().getStyle().set("font-weight", "bold");

        Anchor stockPrice1 = new Anchor("ссылка", "от 1000р.");
        stockPrice1.getElement().getStyle().set("font-size", "20px");
        stockPrice1.getElement().getStyle().set("color", "#9ACD32");
        stockPrice1.getElement().getStyle().set("font-weight", "bold");

        VerticalLayout verticalLayout1 = new VerticalLayout(stockText1, stockPrice1);
        StreamResource imageResource1 = new StreamResource("city1.jpg",
                () -> getClass().getResourceAsStream("/images/cities/city1.jpg"));
        Image image1 = new Image(imageResource1, "Картинка 1");
        image1.setWidth("100px");
        image1.setHeight("100px");
        image1.getElement().getStyle().set("border-radius", "50%");
        HorizontalLayout stock1 = new HorizontalLayout(image1, verticalLayout1);

        Label stockText2 = new Label("Специальное предложение №2");
        stockText2.getElement().getStyle().set("font-size", "20px");
        stockText2.getElement().getStyle().set("font-weight", "bold");

        Anchor stockPrice2 = new Anchor("ссылка", "от 1000р.");
        stockPrice2.getElement().getStyle().set("font-size", "20px");
        stockPrice2.getElement().getStyle().set("color", "#9ACD32");
        stockPrice2.getElement().getStyle().set("font-weight", "bold");

        VerticalLayout verticalLayout2 = new VerticalLayout(stockText2, stockPrice2);
        StreamResource imageResource2 = new StreamResource("city2.jpg",
                () -> getClass().getResourceAsStream("/images/cities/city2.jpg"));
        Image image2 = new Image(imageResource2, "Картинка 2");
        image2.setWidth("100px");
        image2.setHeight("100px");
        image2.getElement().getStyle().set("border-radius", "50%");
        HorizontalLayout stock2 = new HorizontalLayout(image2, verticalLayout2);

        Label stockText3 = new Label("Специальное предложение №3");
        stockText3.getElement().getStyle().set("font-size", "20px");
        stockText3.getElement().getStyle().set("font-weight", "bold");

        Anchor stockPrice3 = new Anchor("ссылка", "от 1000р.");
        stockPrice3.getElement().getStyle().set("font-size", "20px");
        stockPrice3.getElement().getStyle().set("color", "#9ACD32");
        stockPrice3.getElement().getStyle().set("font-weight", "bold");

        VerticalLayout verticalLayout3 = new VerticalLayout(stockText3, stockPrice3);
        StreamResource imageResource3 = new StreamResource("city3.jpg",
                () -> getClass().getResourceAsStream("/images/cities/city3.jpg"));
        Image image3 = new Image(imageResource3, "Картинка 3");
        image3.setWidth("100px");
        image3.setHeight("100px");
        image3.getElement().getStyle().set("border-radius", "50%");
        HorizontalLayout stock3 = new HorizontalLayout(image3, verticalLayout3);

        Label stockText4 = new Label("Специальное предложение №4");
        stockText4.getElement().getStyle().set("font-size", "20px");
        stockText4.getElement().getStyle().set("font-weight", "bold");

        Anchor stockPrice4 = new Anchor("ссылка", "от 1000р.");
        stockPrice4.getElement().getStyle().set("font-size", "20px");
        stockPrice4.getElement().getStyle().set("color", "#9ACD32");
        stockPrice4.getElement().getStyle().set("font-weight", "bold");

        VerticalLayout verticalLayout4 = new VerticalLayout(stockText4, stockPrice4);
        StreamResource imageResource4 = new StreamResource("city4.jpg",
                () -> getClass().getResourceAsStream("/images/cities/city4.jpg"));
        Image image4 = new Image(imageResource4, "Картинка 4");
        image4.setWidth("100px");
        image4.setHeight("100px");
        image4.getElement().getStyle().set("border-radius", "50%");
        HorizontalLayout stock4 = new HorizontalLayout(image4, verticalLayout4);

        Label stockText5 = new Label("Специальное предложение №5");
        stockText5.getElement().getStyle().set("font-size", "20px");
        stockText5.getElement().getStyle().set("font-weight", "bold");

        Anchor stockPrice5 = new Anchor("ссылка", "от 1000р.");
        stockPrice5.getElement().getStyle().set("font-size", "20px");
        stockPrice5.getElement().getStyle().set("color", "#9ACD32");
        stockPrice5.getElement().getStyle().set("font-weight", "bold");

        VerticalLayout verticalLayout5 = new VerticalLayout(stockText5, stockPrice5);
        StreamResource imageResource5 = new StreamResource("city5.jpg",
                () -> getClass().getResourceAsStream("/images/cities/city5.jpg"));
        Image image5 = new Image(imageResource5, "Картинка 5");
        image5.setWidth("100px");
        image5.setHeight("100px");
        image5.getElement().getStyle().set("border-radius", "50%");
        HorizontalLayout stock5 = new HorizontalLayout(image5, verticalLayout5);

        Label stockText6 = new Label("Специальное предложение №6");
        stockText6.getElement().getStyle().set("font-size", "20px");
        stockText6.getElement().getStyle().set("font-weight", "bold");

        Anchor stockPrice6 = new Anchor("ссылка", "от 1000р.");
        stockPrice6.getElement().getStyle().set("font-size", "20px");
        stockPrice6.getElement().getStyle().set("color", "#9ACD32");
        stockPrice6.getElement().getStyle().set("font-weight", "bold");

        VerticalLayout verticalLayout6 = new VerticalLayout(stockText6, stockPrice6);
        StreamResource imageResource6 = new StreamResource("city6.jpg",
                () -> getClass().getResourceAsStream("/images/cities/city6.jpg"));
        Image image6 = new Image(imageResource6, "Картинка 6");
        image6.setWidth("100px");
        image6.setHeight("100px");
        image6.getElement().getStyle().set("border-radius", "50%");
        HorizontalLayout stock6 = new HorizontalLayout(image6, verticalLayout6);

        H1 header = new H1("Специальные предложения");
        HorizontalLayout row1 = new HorizontalLayout(stock1, stock2, stock3);
        row1.setWidthFull();
        row1.getElement().getStyle().set("justify-content", "space-around");
        HorizontalLayout row2 = new HorizontalLayout(stock4, stock5, stock6);
        row2.setWidthFull();
        row2.getElement().getStyle().set("justify-content", "space-around");
        VerticalLayout stocks = new VerticalLayout(header, row1, row2);
        stocks.setWidthFull();
        stocks.getElement().getStyle().set("justify-content", "space-around");

        return stocks;
    }

    //БЛОК "АКЦИИ"
    private VerticalLayout getPromos() {

        H1 headerPromo = new H1("Акции");
        Button allPromosButton = new Button("Все акции", new Icon(VaadinIcon.ANGLE_RIGHT));
        allPromosButton.setIconAfterText(true);
        allPromosButton.getElement().getStyle().set("background-color", "transparent");
        allPromosButton.getElement().getStyle().set("color", "grey");

        HorizontalLayout headerAndButton = new HorizontalLayout(headerPromo, allPromosButton);
        headerAndButton.setWidthFull();
        headerAndButton.setAlignItems(Alignment.BASELINE);
        headerAndButton.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        VerticalLayout promo1 = new VerticalLayout();
        StreamResource imageResourcePromo1 = new StreamResource("promo1.jpg",
                () -> getClass().getResourceAsStream("/images/promos/promo1.jpg"));
        Image imagePromo1 = new Image(imageResourcePromo1, "Промо 1");
        imagePromo1.setWidthFull();
        imagePromo1.setHeight("250px");
        imagePromo1.getElement().getStyle().set("align-self", "flex-start");
        imagePromo1.getElement().getStyle().set("border-radius", "30px 30px 0px 0px");
        imagePromo1.getElement().getStyle().set("margin", "0");

        Label titlePromo1 = new Label("Акция №1");
        titlePromo1.getElement().getStyle().set("font-size", "20px");
        titlePromo1.getElement().getStyle().set("font-weight", "bold");
        Label textPromo1 = new Label("Описание акции №1");
        Button buttonPromo1 = new Button("Подробнее");
        buttonPromo1.getElement().getStyle().set("background-color", "#C8C8C8");
        buttonPromo1.getElement().getStyle().set("color", "black");

        VerticalLayout bodyPromo1 = new VerticalLayout(titlePromo1, textPromo1, buttonPromo1);
        bodyPromo1.getElement().getStyle().set("padding-left", "30px");

        promo1.add(imagePromo1, bodyPromo1);
        promo1.setWidth("400px");
        promo1.setHeight("450px");
        promo1.getElement().getStyle().set("border-radius", "30px");
        promo1.getElement().getStyle().set("border", "1px solid #C8C8C8");
        promo1.getElement().getStyle().set("padding-top", "0");
        promo1.getElement().getStyle().set("padding-left", "0");
        promo1.getElement().getStyle().set("padding-right", "0");

        VerticalLayout promo2 = new VerticalLayout();
        StreamResource imageResourcePromo2 = new StreamResource("promo2.jpg",
                () -> getClass().getResourceAsStream("/images/promos/promo2.jpg"));
        Image imagePromo2 = new Image(imageResourcePromo2, "Промо 2");
        imagePromo2.setWidthFull();
        imagePromo2.setHeight("250px");
        imagePromo2.getElement().getStyle().set("align-self", "flex-start");
        imagePromo2.getElement().getStyle().set("border-radius", "30px 30px 0px 0px");
        imagePromo2.getElement().getStyle().set("margin", "0");

        Label titlePromo2 = new Label("Акция №2");
        titlePromo2.getElement().getStyle().set("font-size", "20px");
        titlePromo2.getElement().getStyle().set("font-weight", "bold");
        Label textPromo2 = new Label("Описание акции №2");
        Button buttonPromo2 = new Button("Подробнее");
        buttonPromo2.getElement().getStyle().set("background-color", "#C8C8C8");
        buttonPromo2.getElement().getStyle().set("color", "black");

        VerticalLayout bodyPromo2 = new VerticalLayout(titlePromo2, textPromo2, buttonPromo2);
        bodyPromo2.getElement().getStyle().set("padding-left", "30px");
        bodyPromo2.setHeightFull();

        promo2.add(imagePromo2, bodyPromo2);
        promo2.setWidth("400px");
        promo2.setHeight("450px");
        promo2.getElement().getStyle().set("border-radius", "30px");
        promo2.getElement().getStyle().set("border", "1px solid #C8C8C8");
        promo2.getElement().getStyle().set("padding-top", "0");
        promo2.getElement().getStyle().set("padding-left", "0");
        promo2.getElement().getStyle().set("padding-right", "0");

        VerticalLayout promo3 = new VerticalLayout();

        Label title = new Label("Подписка на акции");
        title.getElement().getStyle().set("font-size", "30px");
        title.getElement().getStyle().set("font-weight", "bold");

        HorizontalLayout titlePromo3 = new HorizontalLayout(title);
        titlePromo3.setWidthFull();
        titlePromo3.getElement().getStyle().set("justify-content", "center");
        titlePromo3.getElement().getStyle().set("align-items", "center");

        Label textPromo3 = new Label("Скидки на авиабилеты и предложения придут на вашу почту:");
        TextField textFieldPromo3 = new TextField();
        textFieldPromo3.setSizeFull();
        textFieldPromo3.getElement().getStyle().set("padding-top", "30px");
        textFieldPromo3.setPlaceholder("Email");

        HorizontalLayout permissionRow1 = new HorizontalLayout();

        Checkbox checkboxPromo3 = new Checkbox();

        Label text1 = new Label("Я подтверждаю согласие с ");
        text1.getElement().getStyle().set("padding-right", "5px");
        text1.getElement().getStyle().set("padding-left", "5px");

        Anchor href = new Anchor("ссылка", "условиями");
        href.getElement().getStyle().set("color", "#9ACD32");

        permissionRow1.add(checkboxPromo3, text1, href);
        permissionRow1.getElement().getStyle().set("font-size", "15px");
        permissionRow1.setAlignItems(Alignment.END);
        permissionRow1.setSpacing(false);

        Label text2 = new Label("обработки персональных данных");

        HorizontalLayout permissionRow2 = new HorizontalLayout(text2);
        permissionRow2.getElement().getStyle().set("font-size", "15px");
        permissionRow2.setAlignItems(Alignment.START);

        VerticalLayout allPermission = new VerticalLayout(permissionRow1, permissionRow2);
        allPermission.setSpacing(false);

        Button buttonPromo3 = new Button("Подписаться");
        buttonPromo3.getElement().getStyle().set("background-color", "#9ACD32");
        buttonPromo3.getElement().getStyle().set("color", "white");
        buttonPromo3.setSizeFull();

        promo3.add(titlePromo3, textPromo3, textFieldPromo3, allPermission, buttonPromo3);

        promo3.setWidth("400px");
        promo3.setHeight("450px");
        promo3.getElement().getStyle().set("border-radius", "30px");
        promo3.getElement().getStyle().set("border", "1px solid #C8C8C8");
        promo3.getElement().getStyle().set("padding", "35px");
        promo3.setSpacing(true);

        HorizontalLayout promos = new HorizontalLayout(promo1, promo2, promo3);
        promos.setWidthFull();
        promos.getElement().getStyle().set("justify-content", "space-around");

        VerticalLayout promosAndHeader = new VerticalLayout(headerAndButton, promos);
        promosAndHeader.setWidthFull();
        promosAndHeader.getElement().getStyle().set("justify-content", "space-around");
        promosAndHeader.getElement().getStyle().set("padding-right", "30px");
        promosAndHeader.getElement().getStyle().set("padding-left", "30px");

        return promosAndHeader;
    }
}

