package app.controllers.view;

import app.clients.FlightClient;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "flight", layout = MainLayout.class)
public class FlightView extends VerticalLayout {

    private FlightClient flightClient;

    public FlightView(FlightClient flightClient) {
        this.flightClient = flightClient;
    }
}
