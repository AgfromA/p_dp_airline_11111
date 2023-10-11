package app.mappers;
import app.dto.PaymentRequest;
import app.dto.PaymentResponse;
import app.entities.Payment;
import app.enums.Currency;
import app.enums.State;
import org.junit.Assert;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentMapperTest {

    private final PaymentMapper paymentMapper = Mappers.getMapper(PaymentMapper.class);
    @Test
    public void testConvertPaymentResponseToPayment() {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId(1L);
        paymentResponse.setBookingsId(Arrays.asList(1000L, 2000L, 3000L, 4000L, 5000L));
        paymentResponse.setPaymentState(State.SUCCESS);
        paymentResponse.setPrice(new BigDecimal("235.98"));
        paymentResponse.setCurrency(Currency.RUB);

        Payment payment = paymentMapper.convertToPaymentEntity(paymentResponse);

        assertEquals(payment.getId(), paymentResponse.getId());
        assertEquals(payment.getBookingsId(), paymentResponse.getBookingsId());
        assertEquals(payment.getPaymentState(), paymentResponse.getPaymentState());
        assertEquals(payment.getPrice(), paymentResponse.getPrice());
        assertEquals(payment.getCurrency(), paymentResponse.getCurrency());
    }

    @Test
    public void testConvertPaymentRequestToPayment() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setId(1111L);
        paymentRequest.setBookingsId(Arrays.asList(111L, 222L, 333L, 444L, 555L));
        paymentRequest.setPaymentState(State.SUCCESS);
        paymentRequest.setPrice(new BigDecimal("87656.98888"));
        paymentRequest.setCurrency(Currency.EUR);

        Payment payment = paymentMapper.convertToPaymentEntity(paymentRequest);

        assertEquals(payment.getId(), paymentRequest.getId());
        assertEquals(payment.getBookingsId(), paymentRequest.getBookingsId());
        assertEquals(payment.getPaymentState(), paymentRequest.getPaymentState());
        assertEquals(payment.getPrice(), paymentRequest.getPrice());
        assertEquals(payment.getCurrency(), paymentRequest.getCurrency());
    }

    @Test
    public void testConvertPaymentToPaymentRequest() {
        Payment payment = new Payment();
        payment.setId(222L);
        payment.setBookingsId(Arrays.asList(122L, 2333L, 3444L, 455L, 566L));
        payment.setPaymentState(State.SUCCESS);
        payment.setPrice(new BigDecimal("2123132324.45454"));
        payment.setCurrency(Currency.CZK);

        PaymentRequest paymentRequest = paymentMapper.convertToPaymentRequest(payment);

        Assert.assertEquals(paymentRequest.getId(), payment.getId());
        Assert.assertEquals(paymentRequest.getBookingsId(), payment.getBookingsId());
        Assert.assertEquals(paymentRequest.getPaymentState(), payment.getPaymentState());
        Assert.assertEquals(paymentRequest.getPrice(), payment.getPrice());
        Assert.assertEquals(paymentRequest.getCurrency(), payment.getCurrency());
    }

    @Test
    public void testConvertPaymentToPaymentResponse() {
        Payment payment = new Payment();
        payment.setId(333L);
        payment.setBookingsId(Arrays.asList(1223L, 23334L, 34445L, 45556L, 56667L));
        payment.setPaymentState(State.SUCCESS);
        payment.setPrice(new BigDecimal("21566456924.79789"));
        payment.setCurrency(Currency.PLN);

        PaymentResponse paymentResponse = paymentMapper.convertToPaymentResponse(payment);

        Assert.assertEquals(paymentResponse.getId(), payment.getId());
        Assert.assertEquals(paymentResponse.getBookingsId(), payment.getBookingsId());
        Assert.assertEquals(paymentResponse.getPaymentState(), payment.getPaymentState());
        Assert.assertEquals(paymentResponse.getPrice(), payment.getPrice());
        Assert.assertEquals(paymentResponse.getCurrency(), payment.getCurrency());
    }
}
