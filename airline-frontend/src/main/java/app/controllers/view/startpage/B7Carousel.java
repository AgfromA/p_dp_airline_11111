//package app.controllers.view.startpage;
//
//import com.vaadin.flow.component.Component;
//import com.vaadin.flow.component.Tag;
//import com.vaadin.flow.component.dependency.JsModule;
//import com.vaadin.flow.dom.Element;
//
//import java.util.List;
//
//@Tag("b7-carousel")
//@JsModule("./src/B7Carousel.js")
//public class B7Carousel extends Component {
//    private final Element carousel;
//
//    public B7Carousel() {
//        carousel = new Element("b7-carousel");
//    }
//
//    public void setImages(List<String> images) {
//        String imagesString = String.join(", ", images);
//        carousel.setAttribute("images", imagesString);
//
//
//    }
//
//    @Override
//    public Element getElement() {
//        return carousel;
//    }
//}