package app.controllers.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "/", layout = MainLayout.class)
public class StartPageView extends VerticalLayout {

    private final Header header;
    private final SearchForm searchView;

    public StartPageView() {
        header = new Header();
        searchView = new SearchForm();

    }
}

