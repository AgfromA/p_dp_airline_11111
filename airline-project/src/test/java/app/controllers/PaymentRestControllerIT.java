package app.controllers;

import app.clients.PaymentFeignClient;
import app.dto.PaymentRequest;
import app.dto.PaymentResponse;
import app.entities.Payment;
import app.services.interfaces.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-payment-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PaymentRestControllerIT extends IntegrationTestBase {

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private PaymentFeignClient feignClientPayment;

    @Test
    void shouldSavePayment() throws Exception {
        var payment = new Payment();
        List<Long> bookingsId = new ArrayList<>();
        bookingsId.add(6001L);
        bookingsId.add(6002L);
        payment.setBookingsId(bookingsId);

        var httpHeaders = new HttpHeaders();
        httpHeaders.set("url", "testUrl");

        ResponseEntity<PaymentResponse> response = new ResponseEntity<>(httpHeaders, HttpStatus.OK);

        when(feignClientPayment.makePayment(any(PaymentRequest.class))).thenReturn(response);
        mockMvc.perform(post("http://localhost:8080/api/payments")
                        .content(objectMapper.writeValueAsString(payment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotSavePayment() throws Exception {
        var payment = new Payment();
        List<Long> bookingsId = new ArrayList<>();
        bookingsId.add(6001L);
        bookingsId.add(8002L);
        payment.setBookingsId(bookingsId);

        mockMvc.perform(post("http://localhost:8080/api/payments")
                        .content(objectMapper.writeValueAsString(payment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldGetPaymentById() throws Exception {
        long id = 3001;
        mockMvc.perform(get("http://localhost:8080/api/payments/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(paymentService.getPaymentById(id))));
    }


    @Test
    void shouldNotGetNotExistedPaymentById() throws Exception {
        long id = 3010;
        mockMvc.perform(get("http://localhost:8080/api/payments/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}