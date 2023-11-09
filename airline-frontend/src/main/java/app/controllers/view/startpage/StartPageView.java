package app.controllers.view.startpage;

import app.controllers.view.MainLayout;
import app.controllers.view.SearchForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route(value = "/", layout = MainLayout.class)
public class StartPageView extends VerticalLayout {

    private final SearchForm searchForm = new SearchForm();
    private final Header header = new Header();



    public StartPageView() {

        add (header,searchForm);
//        getCarousel();

    }



    //СОЗДАНИЕ КАРУСЕЛИ

//    private void getCarousel() {
//        B7Carousel carousel = new B7Carousel();
//        List<String> images = Arrays.asList("airplane.jpg", "airplane2.jpg", "airplane3.jpg");
//        carousel.setImages(images);
//        add(carousel);
//    }
}

