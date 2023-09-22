package app.controllers.view;

import app.clients.TimezoneClient;
import app.dto.TimezoneDTO;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route( value = "timezone", layout = MainLayout.class)
public class TimezoneView extends VerticalLayout {

    private final TimezoneClient timezoneClient;

    private final Grid<TimezoneDTO> grid = new Grid<>(TimezoneDTO.class);

    @Autowired
    public TimezoneView(TimezoneClient timezoneClient) {
        this.timezoneClient = timezoneClient;

        List<TimezoneDTO> timezoneDTOList = timezoneClient.getAllPagesTimezonesDTO(0,10).getBody().stream().collect(Collectors.toList());

        grid.setItems(timezoneDTOList);

        grid.setColumns("countryName", "cityName", "gmt", "gmtWinter");

        add(grid);
    }


}
