package app.controllers.view;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "/", layout = MainLayout.class)
public class StartPageView extends HorizontalLayout{



    private CommonForm commonForm = new CommonForm();

    public StartPageView() {
        add(commonForm);
        setWidthFull();
    }
}

