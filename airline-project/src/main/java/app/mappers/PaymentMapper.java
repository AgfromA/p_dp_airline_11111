package app.mappers;

import app.dto.PaymentRequest;
import app.dto.PaymentResponse;
import app.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {
    Payment convertToPaymentEntity(PaymentResponse paymentResponse);
    Payment convertToPaymentEntity(PaymentRequest paymentRequest);
    PaymentResponse convertToPaymentResponse(Payment payment);
    PaymentRequest convertToPaymentRequest(Payment payment);
}
