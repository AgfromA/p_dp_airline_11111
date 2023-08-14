package app.controllers.rest;

import app.dto.PaymentRequest;
import app.dto.PaymentResponse;
import app.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.base.rest.PayPalRESTException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "Payment REST")
@Tag(name = "Payment REST", description = "API для операций с оплатой (Payment)")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentRestController {

    private final PaypalService paypalService;

    @ApiOperation(value = "Create Paypal Payment")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Paypal payment added")
    })
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestBody PaymentRequest paymentDto
    ) throws PayPalRESTException {
        String url = null;
        var payment = paypalService.createPayment(
                paymentDto.getPrice().doubleValue(),
                paymentDto.getCurrency().toString(),
                "paypal",
                "sale", "airline project " + paymentDto.getId(),
                "http://localhost:8080",
                "http://localhost:8080");
        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                url = link.getHref();
            }
        }
        return ResponseEntity.status(HttpStatus.valueOf(201)).header("url", url).build();
    }
}