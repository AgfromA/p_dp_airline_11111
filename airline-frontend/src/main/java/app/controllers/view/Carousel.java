package app.controllers.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import lombok.Getter;

@Getter
public class Carousel extends VerticalLayout {

    private int currentIndex = 0;

    public Carousel() {
        StreamResource imageResource1 = new StreamResource("airplane1.jpg",
                () -> getClass().getResourceAsStream("/images/carousel/airplane1.jpg"));
        StreamResource imageResource2 = new StreamResource("airplane2.jpg",
                () -> getClass().getResourceAsStream("/images/carousel/airplane2.jpg"));
        StreamResource imageResource3 = new StreamResource("airplane3.jpg",
                () -> getClass().getResourceAsStream("/images/carousel/airplane3.jpg"));

        Image image1 = new Image(imageResource1, "Картинка 1");
        image1.getStyle().set("box-shadow", "0 0 10px rgba(0,0,0,10)");

        Image image2 = new Image(imageResource2, "Картинка 2");
        image2.getStyle().set("box-shadow", "0 0 10px rgba(0,0,0,10)");

        Image image3 = new Image(imageResource3, "Картинка 3");
        image3.getStyle().set("box-shadow", "0 0 10px rgba(0,0,0,10)");

        Image[] images = new Image[]{image1, image2, image3};
        for (int i = 0; i < images.length; i++) {
            images[i].setVisible(false); //чтобы изначально все картинки были скрыты
        }
        images[currentIndex].setVisible(true); //показываем первую картинку

        Button previous = new Button("<<");
        previous.getElement().getStyle().set("background-color", "transparent");
        previous.getElement().getStyle().set("color", "#9ACD32");

        previous.addClickListener(event -> {
            images[currentIndex].setVisible(false);
            currentIndex = (currentIndex - 1 + images.length) % images.length; //Чтобы при нажатии на кнопку "<=" после первого изображения, отображалось последнее, т.е. например, (0 - 1 + 3) % 3 = 2 (последняя картинка)
            images[currentIndex].setVisible(true);
        });

        Button next = new Button(">>");
        next.getElement().getStyle().set("background-color", "transparent");
        next.getElement().getStyle().set("color", "#9ACD32");


        next.addClickListener(event -> {
            images[currentIndex].setVisible(false);
            currentIndex = (currentIndex + 1) % images.length;//Чтобы при нажатии на кнопку "=>" после последнего изображения, отображалось первое, т.е. например, (2 + 1) % 3 = 0 (первая картинка)
            images[currentIndex].setVisible(true);
        });

        HorizontalLayout buttonsLayout = new HorizontalLayout(previous, next);

        for (Image image : images) {
            add(images);
        }
        add(buttonsLayout);
        setWidthFull();
        setAlignItems(Alignment.CENTER);
    }
}

