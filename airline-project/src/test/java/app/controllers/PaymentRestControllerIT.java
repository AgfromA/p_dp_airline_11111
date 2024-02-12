package app.controllers;

import app.clients.PaymentFeignClient;
import app.dto.BookingDto;
import app.dto.PaymentRequest;
import app.dto.PaymentResponse;
import app.entities.Payment;
import app.services.BookingService;
import app.services.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-payment-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PaymentRestControllerIT extends IntegrationTestBase {

    @Autowired
    private PaymentService paymentService;
    @MockBean
    private PaymentFeignClient feignClientPayment;
    @MockBean
    private BookingService bookingService;

    // Пагинация 2.0
    @Test
    void shouldGetAllPayments() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/payments"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllPaymentsByNullPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/payments?count=2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllPaymentsByNullSize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/payments?page=0"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBadRequestByPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/payments?page=-1&count=2"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBadRequestBySize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/payments?page=0&count=0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetPagePayments() throws Exception {
        var pageable = PageRequest.of(0, 4);
        mockMvc.perform(get("http://localhost:8080/api/payments?page=0&count=4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(paymentService
                        .pagePagination(pageable.getPageNumber(), pageable.getPageSize()))));
    }
    // Пагинация 2.0

    @Test
    void shouldSavePayment() throws Exception {
        var paymentRequest = new PaymentRequest();
        paymentRequest.setBookingsId(Arrays.asList(6001L, 6002L));
        var booking = new BookingDto();
        when(bookingService.getBookingDto(anyLong())).thenReturn(Optional.of(booking));

        var httpHeaders = new HttpHeaders();
        httpHeaders.set("url", "testUrl");
        ResponseEntity<PaymentResponse> response = new ResponseEntity<>(httpHeaders, HttpStatus.OK);
        when(feignClientPayment.makePayment(any(PaymentRequest.class))).thenReturn(response);

        mockMvc.perform(post("http://localhost:8080/api/payments")
                        .content(objectMapper.writeValueAsString(paymentRequest))
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