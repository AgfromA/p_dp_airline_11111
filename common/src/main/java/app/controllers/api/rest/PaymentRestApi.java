package app.controllers.api.rest;

import app.dto.PaymentRequest;
import app.entities.Payment;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Hidden
@RequestMapping("/api/payments")
public interface PaymentRestApi {

    @GetMapping
    ResponseEntity<Page<Payment>> getAllPayments(@RequestParam(value = "page", required = false) Integer page,
                                                 @RequestParam(value = "count", required = false) Integer count);

    @GetMapping("/{id}")
    ResponseEntity<Payment> getPaymentById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<?> createPayment(@RequestBody @Valid PaymentRequest paymentRequest);
}