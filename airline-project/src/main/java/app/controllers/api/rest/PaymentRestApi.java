package app.controllers.api.rest;

import app.dto.PaymentRequest;
import app.entities.Payment;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Payment REST")
@Tag(name = "Payment REST", description = "API для оплаты бронирования")
@RequestMapping("/api/payments")
public interface PaymentRestApi {

    @GetMapping
    @ApiOperation(value = "Get list of all Payments")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "payments found"),
            @ApiResponse(code = 404, message = "payments not found")
    })
    ResponseEntity<List<Payment>> getAllPagesPayments(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "count", required = false) Integer count
    );

    @GetMapping("/{id}")
    @ApiOperation(value = "Get Payment by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "payment found"),
            @ApiResponse(code = 404, message = "payment not found")
    })
    ResponseEntity<Payment> getPaymentById(
            @ApiParam(
                    name = "id",
                    value = "Payment.id"
            )
            @PathVariable("id") Long id);

    @PostMapping
    @ApiOperation(value = "Create new Payment")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "payment created"),
            @ApiResponse(code = 400, message = "payment not created")
    })
    ResponseEntity<?> createPayment(
            @ApiParam(
                    name = "payment",
                    value = "Payment model"
            )
            @RequestBody @Valid PaymentRequest paymentRequest);
}