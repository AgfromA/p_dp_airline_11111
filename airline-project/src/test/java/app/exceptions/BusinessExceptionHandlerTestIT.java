package app.exceptions;

import app.controllers.IntegrationTestBase;
import app.services.BookingService;
import app.services.FlightSeatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BusinessExceptionHandlerTestIT extends IntegrationTestBase {

    @Autowired
    FlightSeatService flightSeatService;
    @Autowired
    BookingService bookingService;

    @Test
    void testHandleSearchControllerException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/search")
                        .param("airportFrom", "AAQ")
                        .param("airportTo", (String) null)
                        .param("departureDate", "2022-01-01")
                        .param("returnDate", "2022-01-10")
                        .param("numberOfPassengers", "2")) // Количество пассажиров устанавливаем 0
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Required request parameter 'airportTo' for method parameter type Airport is not present"));
    }

    @Test
    void testHandleEntityNotFoundException() throws Exception {
        long id = 1488;

        mockMvc.perform(get("http://localhost:8080/api/passengers/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}