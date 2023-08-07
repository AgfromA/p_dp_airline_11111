package app.services.interfaces;

import app.dto.PaymentRequest;
import app.dto.PaymentResponse;
import app.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PaymentService {

    ResponseEntity<PaymentResponse> createPayment(PaymentRequest payment);

    List<Payment> getAllPayments();

    Payment getPaymentById(long id);

    Page<Payment> pagePagination(int page, int count);
}
