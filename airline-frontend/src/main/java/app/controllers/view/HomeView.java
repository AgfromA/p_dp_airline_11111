package app.controllers.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "airlines", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

    private final Header header;
    private final SearchForm searchView;

    public HomeView() {
        header = new Header();
        searchView = new SearchForm();

        add(header, searchView);
    }
}