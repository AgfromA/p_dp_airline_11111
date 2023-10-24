package app.controllers.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class HeaderView extends HorizontalLayout {
    private final H1 header = new H1("Airline-project");
    private final Button goHomeButton = new Button(header, e -> UI.getCurrent().navigate("airlines7"));

    public HeaderView() {
        add(goHomeButton);
    }
}