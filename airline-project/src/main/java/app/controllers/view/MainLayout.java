package app.controllers.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        addToNavbar(true, viewTitle);
    }

    private void addDrawerContent() {
        DrawerToggle toggle = new DrawerToggle();
        addToDrawer(new Footer());
        Tabs tabs = getTabs();
        addToDrawer(tabs);
        addToNavbar(toggle);
    }

    /**
     * Не копируй эту же иконку, возьми другую подходяющую сущности по смыслу
     *
     * @link <a href="https://vaadin.com/docs/v23/components/icons">Icons</a>
     */
    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.HANDS_UP, "Hello", AdminView.class));
        tabs.add(createTab(VaadinIcon.CLOSE_CIRCLE_O, "Examples", ExampleView.class));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class<? extends Component> routeClass) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");
        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setRoute(routeClass);
        link.setTabIndex(-1);
        return new Tab(link);
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}