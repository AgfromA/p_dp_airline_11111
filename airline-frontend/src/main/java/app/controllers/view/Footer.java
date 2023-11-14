package app.controllers.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

@Getter
public class Footer extends HorizontalLayout {


    public Footer() {


        HorizontalLayout footer = getFooter();
        add(footer);
        setWidthFull();
    }

    private HorizontalLayout getFooter() {

        Anchor anchor1 = new Anchor("ссылка", "Ссылка №1");
        Anchor anchor2 = new Anchor("ссылка", "Ссылка №2");
        Anchor anchor3 = new Anchor("ссылка", "Ссылка №3");
        VerticalLayout column1 = new VerticalLayout(anchor1, anchor2, anchor3);
        column1.setWidthFull();

        Anchor anchor4 = new Anchor("ссылка", "Ссылка №4");
        Anchor anchor5 = new Anchor("ссылка", "Ссылка №5");
        Anchor anchor6 = new Anchor("ссылка", "Ссылка №6");
        VerticalLayout column2 = new VerticalLayout(anchor4, anchor5, anchor6);
        column2.setWidthFull();

        Label title = new Label("Новостная рассылка");

        TextField email = new TextField();
        email.setPlaceholder("Email");

        Button rightArrow = new Button(new Icon (VaadinIcon.ARROW_RIGHT));
        rightArrow.getElement().getStyle().set("background-color", "#9ACD32");
        rightArrow.getElement().getStyle().set("color", "white");
        rightArrow.getElement().getStyle().set("width", "10px");
        rightArrow.getElement().getStyle().set("height", "30px");
        rightArrow.getElement().setAttribute("slot", "suffix");
        email.getElement().appendChild(rightArrow.getElement());

        VerticalLayout mailing = new VerticalLayout(title, email);
        mailing.setAlignItems(Alignment.CENTER);
        mailing.setWidthFull();

        HorizontalLayout footer = new HorizontalLayout(column1, column2, mailing);
        footer.getElement().getStyle().set("border", "1px solid #C8C8C8");
        footer.getElement().getStyle().set("justify-content", "space-between");
        footer.getElement().getStyle().set("flex-grow", "1");

        footer.setMargin(false);

        footer.setAlignItems(Alignment.BASELINE);
        footer.setWidthFull();

        return footer;
    }
}
