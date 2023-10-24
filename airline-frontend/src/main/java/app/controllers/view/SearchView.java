package app.controllers.view;

import app.entities.account.search.Search;
import app.enums.Airport;
import app.enums.CategoryType;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.VaadinSession;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SearchView extends HorizontalLayout {

    private final Search search = new Search();
    private final Binder<Search> binder = new Binder<>(Search.class);
    private final ComboBox<Airport> fromField = new ComboBox<>("From");
    private final ComboBox<Airport> toField = new ComboBox<>("To");
    private final DatePicker departureDateField = new DatePicker("Departure");
    private final DatePicker returnDateField = new DatePicker("Return");
    private final IntegerField numberOfPassengersField = new IntegerField("Number of passengers");
    private final ComboBox<CategoryType> categoryField = new ComboBox<>("Category");
    private final Button searchButton = new Button(VaadinIcon.SEARCH.create());
    private final Button reverseButton = new Button(VaadinIcon.EXCHANGE.create());

    public SearchView() {

        setFromField();
        setToField();
        setDepartureDateField();
        setReturnDateField();
        setNumberOfPassengersField();
        setCategoryField();

        reverseButton.addClickListener(e -> {
            Airport swap = fromField.getValue();
            fromField.setValue(toField.getValue());
            toField.setValue(swap);
        });

        searchButton.addClickListener(e -> {
            if (createSearch()) {
                VaadinSession.getCurrent().setAttribute("searchView", search);
                UI.getCurrent().navigate("search-flights");
            }
        });

        add(fromField, reverseButton, toField, departureDateField
                , returnDateField, numberOfPassengersField, categoryField, searchButton);

        expand(fromField, toField, departureDateField
                , returnDateField, numberOfPassengersField, categoryField);

        setWidthFull();
        setAlignItems(Alignment.BASELINE);

    }

    private void setFromField() {
        fromField.setWidth("15%");
        fromField.setItems(Airport.values());
        fromField.setItemLabelGenerator(airport ->
                airport.getCity() + " (" + airport.getAirportName() + ") " + airport.getAirportInternalCode());
        fromField.setValue(Airport.VKO);
        binder.forField(fromField)
                .asRequired("Destination cannot be empty")
                .bind(Search::getFrom, Search::setFrom);
    }

    private void setToField() {
        toField.setWidth("15%");
        toField.setItems(Airport.values());
        toField.setItemLabelGenerator(airport ->
                airport.getCity() + " (" + airport.getAirportName() + ") " + airport.getAirportInternalCode());
        toField.setValue(Airport.OMS);
        binder.forField(toField)
                .asRequired("Destination cannot be empty")
                .bind(Search::getTo, Search::setTo);
    }

    private void setDepartureDateField() {
        departureDateField.setWidth("10%");
        departureDateField.setValue(LocalDate.of(2024, 1, 1));
        binder.forField(departureDateField)
                .asRequired("Departure date cannot be empty")
                .bind(Search::getDepartureDate, Search::setDepartureDate);
    }

    private void setReturnDateField() {
        returnDateField.setWidth("10%");
        returnDateField.setValue(departureDateField.getValue().plusDays(13));
        binder.forField(returnDateField)
                .withValidator(date -> date == null || !date.isBefore(departureDateField.getValue())
                        , "Return date must be after departure date")
                .bind(Search::getReturnDate, Search::setReturnDate);
    }

    private void setNumberOfPassengersField() {
        numberOfPassengersField.setWidth("5%");
        numberOfPassengersField.setValue(1);
        numberOfPassengersField.setStepButtonsVisible(true);
        numberOfPassengersField.setMin(1);
        numberOfPassengersField.setMax(9);
        binder.forField(numberOfPassengersField)
                .asRequired("Number of passengers must be between 1 and 9")
                .withValidator(value -> value >= 1 && value <= 9, "Number of passengers must be between 1 and 9")
                .bind(Search::getNumberOfPassengers, Search::setNumberOfPassengers);
    }

    private void setCategoryField() {
        categoryField.setWidth("10%");
        categoryField.setItems(CategoryType.values());
        categoryField.setValue(CategoryType.ECONOMY);
    }

    boolean createSearch() {
        if (binder.writeBeanIfValid(search)) {
            search.setFrom(fromField.getValue());
            search.setTo(toField.getValue());
            search.setDepartureDate(departureDateField.getValue());
            search.setReturnDate(returnDateField.getValue());
            search.setNumberOfPassengers(numberOfPassengersField.getValue());
            return true;
        }
        return false;
    }

}