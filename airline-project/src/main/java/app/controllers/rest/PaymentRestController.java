package app.controllers.rest;

import app.controllers.api.rest.PaymentRestApi;
import app.dto.PaymentRequest;
import app.dto.PaymentResponse;
import app.entities.Payment;
import app.services.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentRestController implements PaymentRestApi {

    private final PaymentService paymentService;

    @Override
    public ResponseEntity<Page<Payment>> getAllPagesPayments(Integer page, Integer count) {
        var payments = paymentService.getAllPayments();
        if (payments == null) {
            log.info("getListOfAllPayments: not found any payments");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("getListOfAllPayments: found {} payments", payments.size());
        return new ResponseEntity<>(paymentService.pagePagination(page, count), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Payment> getPaymentById(Long id) {
        var payment = paymentService.getPaymentById(id);
        if (payment != null) {
            log.info("get: find payment with id = {}", id);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } else {
            log.info("get: not find payment with id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> createPayment(PaymentRequest paymentDto) {
        try {
            ResponseEntity<PaymentResponse> response = paymentService.createPayment(paymentDto);
            var paypalUrl = response.getHeaders().getFirst("url");
            var responseDto = response.getBody();
            return ResponseEntity.status(HttpStatus.CREATED).header("paypalurl", paypalUrl).body(responseDto);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}