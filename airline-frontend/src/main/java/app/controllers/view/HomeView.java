package app.controllers.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "airlines7")
public class HomeView extends VerticalLayout {

    private final HeaderView header;
    private final SearchView searchView;

    public HomeView() {
        header = new HeaderView();
        searchView = new SearchView();

        add(header, searchView);
    }
}