//package app.controllers.view.startpage;
//
//import com.vaadin.flow.component.Component;
//import com.vaadin.flow.component.Tag;
//import com.vaadin.flow.component.dependency.JsModule;
//import com.vaadin.flow.dom.Element;
//import com.vaadin.flow.server.StreamResource;
//import lombok.Getter;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Getter
//@Tag("b7-carousel")
//@JsModule("./src/B7Carousel.js")
//public class B7Carousel extends Component {
//    private final Element carousel;
//
//    public B7Carousel() {
//        carousel = new Element("b7-carousel");
//    }
//
//    public void setImages(List<String> imageUrls) {
//        List<StreamResource> imageResources = imageUrls.stream()
//                .map(url -> new StreamResource(url, () -> getImageStream(url)))
//                .collect(Collectors.toList());
//
//        carousel.getElement().setAttribute("images", imageResources);
//    }
//
//    @Override
//    public Element getElement() {
//        return carousel;
//    }
//}