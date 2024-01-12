package app.mappers;

import app.dto.PaymentRequest;
import app.dto.PaymentResponse;
import app.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    Payment convertToPaymentEntity(PaymentResponse paymentResponse);

    Payment convertToPaymentEntity(PaymentRequest paymentRequest);

    PaymentResponse convertToPaymentResponse(Payment payment);

    PaymentRequest convertToPaymentRequest(Payment payment);

    List<Payment> convertToPaymentEntityListOfResponse(List<PaymentResponse> paymentResponseList);

    List<Payment> convertToPaymentEntityListOfRequest(List<PaymentRequest> paymentRequestList);

    List<PaymentResponse> convertToPaymentResponseList(List<Payment> paymentList);

    List<PaymentRequest> convertToPaymentRequestList(List<Payment> paymentList);
}