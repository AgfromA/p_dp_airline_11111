package app.controllers.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import lombok.Getter;

@Getter
public class CommonForm extends VerticalLayout {
    public CommonForm() {
        TabSheet tabSheet = getFormWithTabs();
        add(tabSheet);

        VerticalLayout stocks = getStocks();
        add(stocks);


        setAlignItems(Alignment.CENTER);
        setSizeFull();

    }


//СОЗДАЕМ ФОРМУ СО ВКЛАДКАМИ, КУДА ПОМЕСТИМ SEARCH FORM (покупка), Регистрация на рейс, Мои бронирования, Статус рейса

    private TabSheet getFormWithTabs() {
        TabSheet externalTabs = new TabSheet(); //ВНЕШНИЙ

        TabSheet innerSearchFormTabSheet = new TabSheet(); //ВНУТРЕННИЙ ДЛЯ ВКЛАДКИ "ПОКУПКА"
        innerSearchFormTabSheet.setSizeFull();
        innerSearchFormTabSheet.getElement().getStyle().set("justify-content", "center");
        innerSearchFormTabSheet.getElement().getStyle().set("display", "flex");
        innerSearchFormTabSheet.getElement().getStyle().set("align-items", "center");

        innerSearchFormTabSheet.addThemeVariants(TabSheetVariant.LUMO_BORDERED);


        Tab searchFormTab = new Tab("Покупка");


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
        Tab excursionsTab = new Tab(new Anchor("https://excursionsTab.ru/", "Экскурсии"));
        HorizontalLayout layout = new HorizontalLayout();
        innerSearchFormTabSheet.add(excursionsTab, layout);

        //Апартаменты 5

        Tab apartmentsTab = new Tab(new Anchor("https://apartmentsTab.ru/", "Апартаменты"));
        HorizontalLayout layout2 = new HorizontalLayout();
        innerSearchFormTabSheet.add(apartmentsTab, layout2);

        //Аренда авто 6
        HorizontalLayout rentCar = getRentCar();
        Tab rentCarTab = new Tab("Аренда авто");
        innerSearchFormTabSheet.add(rentCarTab, rentCar);

        //Трансфер 7
        Tab transferTab = new Tab(new Anchor("https://transferTab.ru/", "Трансфер"));
        HorizontalLayout layout3 = new HorizontalLayout();
        innerSearchFormTabSheet.add(transferTab, layout3);

        externalTabs.add(searchFormTab, innerSearchFormTabSheet); //ПОЛОЖИЛИ ВО ВНЕШНИЙ КНОПКУ ПЕРВОЙ ВКЛАДКИ "ПОКУПКА" И ЕЕ СОДЕРЖИМОЕ
        externalTabs.setSizeFull();

        //ПОЛОЖИЛИ ВО ВНЕШНИЙ КНОПКУ ВТОРОЙ ВКЛАДКИ "РЕГИСТРАЦИЯ НА РЕЙС" И ЕЕ СОДЕРЖИМОЕ
        HorizontalLayout checkInFlightLayout = getCheckInFlight();
        Tab checkInFlightTab = new Tab("Регистрация на рейс");
        externalTabs.add(checkInFlightTab, checkInFlightLayout);

        //ПОЛОЖИЛИ ВО ВНЕШНИЙ КНОПКУ ТРЕТЬЕЙ ВКЛАДКИ "МОИ БРОНИРОВАНИЯ" И ЕЕ СОДЕРЖИМОЕ
        HorizontalLayout bookingsLayout = getBookings();
        Tab bookingsTab = new Tab("Мои бронирования");
        externalTabs.add(bookingsTab, bookingsLayout);

        //ПОЛОЖИЛИ ВО ВНЕШНИЙ КНОПКУ ЧЕТВЕРТОЙ ВКЛАДКИ "СТАТУС РЕЙСА" И ЕЕ СОДЕРЖИМОЕ, СОСТОЯЩЕЕ ИЗ ДВУХ ВКЛАДОК
        Tab flightStatusTab = new Tab("Статус рейса");
        TabSheet innerflightStatusTabSheet = new TabSheet();

        HorizontalLayout byRouteLayout = getByRoute();
        Tab byRoute = new Tab("По маршруту");
        innerflightStatusTabSheet.add(byRoute, byRouteLayout);

        HorizontalLayout byFlightNumberLayout = getByFlightNumber();
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


    //СОЗДАЕМ КОМПОНЕНТ, КОТОРЫЙ БУДЕТ ОТОБРАЖАТЬСЯ ВО ВКЛАДКЕ "РЕГИСТРАЦИЯ НА РЕЙС"
    private HorizontalLayout getCheckInFlight() {

        HorizontalLayout layout = new HorizontalLayout();

        TextField textField1 = new TextField();
        textField1.setPlaceholder("Фамилия пассажира");

        TextField textField2 = new TextField();
        textField2.setPlaceholder("Номер заказа, брони или билета");

        Button buttonLogin = new Button("Зарегистрироваться");
        buttonLogin.getElement().getStyle().set("background-color", "#9ACD32");
        buttonLogin.getElement().getStyle().set("color", "white");

        layout.add(textField1, textField2, buttonLogin);
        layout.setAlignItems(Alignment.BASELINE);
        layout.getElement().getStyle().set("justify-content", "center");
        layout.getElement().getStyle().set("display", "flex");
        layout.getElement().getStyle().set("align-items", "center");
        layout.setSizeFull();

        return layout;
    }

    //СОЗДАЕМ КОМПОНЕНТ, КОТОРЫЙ БУДЕТ ОТОБРАЖАТЬСЯ ВО ВКЛАДКЕ "МОИ БРОНИРОВАНИЯ"
    private HorizontalLayout getBookings() {

        HorizontalLayout bookings = new HorizontalLayout();

        TextField lastnameOrEmail = new TextField();
        lastnameOrEmail.setPlaceholder("Фамилия пассажира или email");

        TextField orderOrTicket = new TextField();
        orderOrTicket.setPlaceholder("Номер заказа, брони или билета");

        Button buttonBookings = new Button("Проверить статус");
        buttonBookings.getElement().getStyle().set("background-color", "#9ACD32");
        buttonBookings.getElement().getStyle().set("color", "white");

        bookings.add(lastnameOrEmail, orderOrTicket, buttonBookings);
        bookings.setAlignItems(Alignment.BASELINE);
        bookings.getElement().getStyle().set("justify-content", "center");
        bookings.getElement().getStyle().set("display", "flex");
        bookings.getElement().getStyle().set("align-items", "center");
        bookings.getElement().getStyle().set("border", "1px solid LightGray");
        bookings.setSizeFull();

        return bookings;
    }

    //СОЗДАЕМ КОМПОНЕНТ, КОТОРЫЙ БУДЕТ ОТОБРАЖАТЬСЯ ВО ВКЛАДКЕ "ПО МАРШРУТУ"
    private HorizontalLayout getByRoute() {

        HorizontalLayout byRouteLayout = new HorizontalLayout();

        TextField from = new TextField();
        from.setPlaceholder("Откуда");

        TextField to = new TextField();
        to.setPlaceholder("Куда");

        Button swapButton = new Button(new Icon(VaadinIcon.EXCHANGE));
        swapButton.getElement().getStyle().set("background-color", "#9ACD32");
        swapButton.getElement().getStyle().set("color", "white");

        swapButton.addClickListener(event -> {
            String data = from.getValue();
            from.setValue(to.getValue());
            to.setValue(data);
        });

        DatePicker date = new DatePicker();
        date.setPlaceholder("ДД.ММ.ГГГГ");

        Button checkStatus = new Button("Проверить статус");
        checkStatus.getElement().getStyle().set("background-color", "#9ACD32");
        checkStatus.getElement().getStyle().set("color", "white");

        byRouteLayout.add(from, swapButton, to, date, checkStatus);
        byRouteLayout.setAlignItems(Alignment.BASELINE);
        byRouteLayout.getElement().getStyle().set("justify-content", "center");
        byRouteLayout.getElement().getStyle().set("display", "flex");
        byRouteLayout.getElement().getStyle().set("align-items", "center");
        byRouteLayout.getElement().getStyle().set("border", "1px solid LightGray");
        byRouteLayout.setSizeFull();

        return byRouteLayout;
    }

    //СОЗДАЕМ КОМПОНЕНТ, КОТОРЫЙ БУДЕТ ОТОБРАЖАТЬСЯ ВО ВКЛАДКЕ "ПО НОМЕРУ РЕЙСА"
    private HorizontalLayout getByFlightNumber() {

        HorizontalLayout byFlightNumberLayout = new HorizontalLayout();

        TextField flightNumber = new TextField();
        flightNumber.setPlaceholder("Номер рейса");

        DatePicker date = new DatePicker();
        date.setPlaceholder("ДД.ММ.ГГГГ");

        Button checkStatus = new Button("Проверить статус");
        checkStatus.getElement().getStyle().set("background-color", "#9ACD32");
        checkStatus.getElement().getStyle().set("color", "white");

        byFlightNumberLayout.add(flightNumber, date, checkStatus);
        byFlightNumberLayout.setAlignItems(Alignment.BASELINE);
        byFlightNumberLayout.getElement().getStyle().set("justify-content", "center");
        byFlightNumberLayout.getElement().getStyle().set("display", "flex");
        byFlightNumberLayout.getElement().getStyle().set("align-items", "center");
        byFlightNumberLayout.getElement().getStyle().set("border", "1px solid LightGray");

        byFlightNumberLayout.setSizeFull();

        return byFlightNumberLayout;
    }


    //СОЗДАЕМ ВНИЗУ ФОРМЫ ОПЦИИ ДЛЯ ВКЛАДКИ "АВИАБИЛЕТЫ"
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


    //СОЗДАЕМ КОМПОНЕНТ, КОТОРЫЙ БУДЕТ ОТОБРАЖАТЬСЯ ВО ВКЛАДКЕ "ОТЕЛИ"
    private HorizontalLayout getHotels() {

        HorizontalLayout hotels = new HorizontalLayout();

        TextField cityAndRegion = new TextField();
        cityAndRegion.setPlaceholder("Город, регион");

        DatePicker dateFrom = new DatePicker();
        dateFrom.setPlaceholder("ДД.ММ.ГГГГ");

        DatePicker dateTo = new DatePicker();
        dateTo.setPlaceholder("ДД.ММ.ГГГГ");

        IntegerField adults = new IntegerField("Взрослые");
        adults.setHasControls(true);
        adults.setStep(1);
        adults.setMin(0);

        IntegerField children = new IntegerField("Дети");
        children.setHasControls(true);
        children.setStep(1);
        children.setMin(0);

        ComboBox<IntegerField> people = new ComboBox<>();
        people.setItems(adults, children);

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

        HorizontalLayout layout = new HorizontalLayout(cityAndRegion, dateFrom, dateTo, people, searchButton);
        VerticalLayout verticalLayout = new VerticalLayout(layout, layoutRadioButton);

        hotels.add(verticalLayout);
        hotels.setSizeFull();

        return hotels;
    }

    //СОЗДАЕМ КОМПОНЕНТ АВИА + ОТЕЛЬ
    private HorizontalLayout getFlightsAndHotels() {

        HorizontalLayout flightsAndHotels = new HorizontalLayout();

        TextField cityFrom = new TextField();
        cityFrom.setPlaceholder("Откуда");

        TextField cityTo = new TextField();
        cityTo.setPlaceholder("Куда");

        DatePicker dateFrom = new DatePicker();
        dateFrom.setPlaceholder("ДД.ММ.ГГГГ");

        DatePicker dateTo = new DatePicker();
        dateTo.setPlaceholder("ДД.ММ.ГГГГ");

        IntegerField adults = new IntegerField("Взрослые");
        adults.setHasControls(true);
        adults.setStep(1);
        adults.setMin(0);

        IntegerField children = new IntegerField("Дети");
        children.setHasControls(true);
        children.setStep(1);
        children.setMin(0);

        ComboBox<IntegerField> people = new ComboBox<>();
        people.setItems(adults, children);

        Button searchButton = new Button(new Icon(VaadinIcon.SEARCH));
        searchButton.getElement().getStyle().set("background-color", "#9ACD32");
        searchButton.getElement().getStyle().set("color", "white");

        flightsAndHotels.add(cityFrom, cityTo, dateFrom, dateTo, people, searchButton);
        flightsAndHotels.setSizeFull();

        return flightsAndHotels;
    }

    //СОЗДАЕМ КОМПОНЕНТ, КОТОРЫЙ БУДЕТ ВО ВКЛАДКЕ "АРЕНДА АВТО"
    private HorizontalLayout getRentCar() {

        HorizontalLayout rentCar = new HorizontalLayout();

        TextField cityOrAirportReceipt = new TextField();
        cityOrAirportReceipt.setPlaceholder("Город/Аэропорт получения");

        DatePicker receiving = new DatePicker();
        receiving.setPlaceholder("Получение");

        DatePicker refund = new DatePicker();
        refund.setPlaceholder("Возврат");

        Button searchButton = new Button(new Icon(VaadinIcon.SEARCH));
        searchButton.getElement().getStyle().set("background-color", "#9ACD32");
        searchButton.getElement().getStyle().set("color", "white");

        rentCar.add(cityOrAirportReceipt, receiving, refund, searchButton);
        rentCar.setSizeFull();

        return rentCar;
    }


    //СОЗДАНИЕ БЛОКА "СПЕЦИАЛЬНЫЕ ПРЕДЛОЖЕНИЯ"
    private VerticalLayout getStocks() {

        Text stockText1 = new Text("Специальное предложение №1");
        Anchor stockPrice1 = new Anchor("https://transferTab.ru/", "от 1000р.");
        VerticalLayout verticalLayout1 = new VerticalLayout(stockText1, stockPrice1);
        StreamResource imageResource1 = new StreamResource("city1.jpg",
                () -> getClass().getResourceAsStream("/images/city1.jpg"));
        Image image1 = new Image(imageResource1, "Картинка 1");
        image1.setWidth("100px");
        image1.setHeight("100px");
        image1.getElement().getStyle().set("border-radius", "50%");
        HorizontalLayout stock1 = new HorizontalLayout(image1, verticalLayout1);


        Text stockText2 = new Text("Специальное предложение №2");
        Anchor stockPrice2 = new Anchor("https://transferTab.ru/", "от 1000р.");
        VerticalLayout verticalLayout2 = new VerticalLayout(stockText2, stockPrice2);
        StreamResource imageResource2 = new StreamResource("city2.jpg",
                () -> getClass().getResourceAsStream("/images/city2.jpg"));
        Image image2 = new Image(imageResource2, "Картинка 2");
        image2.setWidth("100px");
        image2.setHeight("100px");
        image2.getElement().getStyle().set("border-radius", "50%");
        HorizontalLayout stock2 = new HorizontalLayout(image2, verticalLayout2);

        Text stockText3 = new Text("Специальное предложение №3");
        Anchor stockPrice3 = new Anchor("https://transferTab.ru/", "от 1000р.");
        VerticalLayout verticalLayout3 = new VerticalLayout(stockText3, stockPrice3);
        StreamResource imageResource3 = new StreamResource("city3.jpg",
                () -> getClass().getResourceAsStream("/images/city3.jpg"));
        Image image3 = new Image(imageResource3, "Картинка 3");
        image3.setWidth("100px");
        image3.setHeight("100px");
        image3.getElement().getStyle().set("border-radius", "50%");
        HorizontalLayout stock3 = new HorizontalLayout(image3, verticalLayout3);


        Text stockText4 = new Text("Специальное предложение №4");
        Anchor stockPrice4 = new Anchor("https://transferTab.ru/", "от 1000р.");
        VerticalLayout verticalLayout4 = new VerticalLayout(stockText4, stockPrice4);
        StreamResource imageResource4 = new StreamResource("city4.jpg",
                () -> getClass().getResourceAsStream("/images/city4.jpg"));
        Image image4 = new Image(imageResource4, "Картинка 4");
        image4.setWidth("100px");
        image4.setHeight("100px");
        image4.getElement().getStyle().set("border-radius", "50%");
        HorizontalLayout stock4 = new HorizontalLayout(image4, verticalLayout4);


        Text stockText5 = new Text("Специальное предложение №5");
        Anchor stockPrice5 = new Anchor("https://transferTab.ru/", "от 1000р.");
        VerticalLayout verticalLayout5 = new VerticalLayout(stockText5, stockPrice5);
        StreamResource imageResource5 = new StreamResource("city5.jpg",
                () -> getClass().getResourceAsStream("/images/city5.jpg"));
        Image image5 = new Image(imageResource5, "Картинка 5");
        image5.setWidth("100px");
        image5.setHeight("100px");
        image5.getElement().getStyle().set("border-radius", "50%");
        HorizontalLayout stock5 = new HorizontalLayout(image5, verticalLayout5);

        Text stockText6 = new Text("Специальное предложение №6");
        Anchor stockPrice6 = new Anchor("https://transferTab.ru/", "от 1000р.");
        VerticalLayout verticalLayout6 = new VerticalLayout(stockText6, stockPrice6);
        StreamResource imageResource6 = new StreamResource("city6.jpg",
                () -> getClass().getResourceAsStream("/images/city6.jpg"));
        Image image6 = new Image(imageResource6, "Картинка 6");
        image6.setWidth("100px");
        image6.setHeight("100px");
        image6.getElement().getStyle().set("border-radius", "50%");
        HorizontalLayout stock6 = new HorizontalLayout(image6, verticalLayout6);

        H1 header = new H1("Специальные предложения");
        HorizontalLayout row1 = new HorizontalLayout(stock1, stock2, stock3);
        HorizontalLayout row2 = new HorizontalLayout(stock4, stock5, stock6);
        VerticalLayout stocks = new VerticalLayout(header, row1, row2);

        return stocks;
    }
}