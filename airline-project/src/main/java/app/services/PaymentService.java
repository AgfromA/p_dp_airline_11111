package app.services;

import app.clients.PaymentFeignClient;
import app.dto.PaymentRequest;
import app.dto.PaymentResponse;
import app.entities.Payment;
import app.enums.State;
import app.mappers.PaymentMapper;
import app.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentFeignClient paymentFeignClient;
    private final PaymentRepository paymentRepository;
    private final BookingService bookingService;
    private final PaymentMapper paymentMapper;

    @Transactional
    public ResponseEntity<PaymentResponse> createPayment(PaymentRequest paymentRequest) {
        paymentRequest.getBookingsId().forEach(id -> {
            var bookingFromDb = bookingService.getBookingDto(id);
            if (bookingFromDb.isEmpty()) {
                throw new NoSuchElementException(String.format("booking with id=%d not exists", id));
            }
        });
        paymentRequest.setPaymentState(State.CREATED);
        var savedPayment = paymentRepository.save(paymentMapper.convertToPaymentEntity(paymentRequest));
        log.info("create: new payment saved with id = {}", savedPayment.getId());
        var response = paymentFeignClient.makePayment(paymentRequest);
        var paymentResponse = paymentMapper.convertToPaymentResponse(savedPayment);
        var url = response.getHeaders().getFirst("url");
        return ResponseEntity.status(response.getStatusCode())
                .header("url",url)
                .body(paymentResponse);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Page<Payment> pagePagination(int page, int count) {
        return paymentRepository.findAll(PageRequest.of(page, count));
    }
}