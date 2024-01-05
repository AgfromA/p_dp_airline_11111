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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        assertEquals(paymentRequest.getId(), payment.getId());
        assertEquals(paymentRequest.getBookingsId(), payment.getBookingsId());
        assertEquals(paymentRequest.getPaymentState(), payment.getPaymentState());
        assertEquals(paymentRequest.getPrice(), payment.getPrice());
        assertEquals(paymentRequest.getCurrency(), payment.getCurrency());
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

        assertEquals(paymentResponse.getId(), payment.getId());
        assertEquals(paymentResponse.getBookingsId(), payment.getBookingsId());
        assertEquals(paymentResponse.getPaymentState(), payment.getPaymentState());
        assertEquals(paymentResponse.getPrice(), payment.getPrice());
        assertEquals(paymentResponse.getCurrency(), payment.getCurrency());
    }

    @Test
    public void testConvertPaymentListOfResponseList() {
        List<PaymentResponse> paymentResponseList = new ArrayList<>();

        PaymentResponse paymentResponseOne = new PaymentResponse();
        paymentResponseOne.setId(1L);
        paymentResponseOne.setBookingsId(Arrays.asList(1000L, 2000L, 3000L, 4000L, 5000L));
        paymentResponseOne.setPaymentState(State.SUCCESS);
        paymentResponseOne.setPrice(new BigDecimal("235.98"));
        paymentResponseOne.setCurrency(Currency.RUB);

        PaymentResponse paymentResponseTwo = new PaymentResponse();
        paymentResponseTwo.setId(2L);
        paymentResponseTwo.setBookingsId(Arrays.asList(1001L, 2002L, 3003L, 4004L, 5005L));
        paymentResponseTwo.setPaymentState(State.REJECTED);
        paymentResponseTwo.setPrice(new BigDecimal("526.74"));
        paymentResponseTwo.setCurrency(Currency.EUR);

        paymentResponseList.add(paymentResponseOne);
        paymentResponseList.add(paymentResponseTwo);

        List<Payment> paymentList = paymentMapper.convertToPaymentEntityListOfResponse(paymentResponseList);
        assertEquals(paymentList.size(), paymentResponseList.size());
        assertEquals(paymentList.get(0).getId(), paymentResponseList.get(0).getId());
        assertEquals(paymentList.get(0).getBookingsId(), paymentResponseList.get(0).getBookingsId());
        assertEquals(paymentList.get(0).getPaymentState(), paymentResponseList.get(0).getPaymentState());
        assertEquals(paymentList.get(0).getPrice(), paymentResponseList.get(0).getPrice());
        assertEquals(paymentList.get(0).getCurrency(), paymentResponseList.get(0).getCurrency());

        assertEquals(paymentList.get(1).getId(), paymentResponseList.get(1).getId());
        assertEquals(paymentList.get(1).getBookingsId(), paymentResponseList.get(1).getBookingsId());
        assertEquals(paymentList.get(1).getPaymentState(), paymentResponseList.get(1).getPaymentState());
        assertEquals(paymentList.get(1).getPrice(), paymentResponseList.get(1).getPrice());
        assertEquals(paymentList.get(1).getCurrency(), paymentResponseList.get(1).getCurrency());
    }

    @Test
    public void testConvertPaymentListOfRequestList() {
        List<PaymentRequest> paymentRequestList = new ArrayList<>();

        PaymentRequest paymentRequestOne = new PaymentRequest();
        paymentRequestOne.setId(1111L);
        paymentRequestOne.setBookingsId(Arrays.asList(111L, 222L, 333L, 444L, 555L));
        paymentRequestOne.setPaymentState(State.SUCCESS);
        paymentRequestOne.setPrice(new BigDecimal("87656.98888"));
        paymentRequestOne.setCurrency(Currency.EUR);

        PaymentRequest paymentRequestTwo = new PaymentRequest();
        paymentRequestTwo.setId(2111L);
        paymentRequestTwo.setBookingsId(Arrays.asList(112L, 223L, 334L, 445L, 556L));
        paymentRequestTwo.setPaymentState(State.ERROR);
        paymentRequestTwo.setPrice(new BigDecimal("57256.95528"));
        paymentRequestTwo.setCurrency(Currency.RUB);

        paymentRequestList.add(paymentRequestOne);
        paymentRequestList.add(paymentRequestTwo);

        List<Payment> paymentList = paymentMapper.convertToPaymentEntityListOfRequest(paymentRequestList);
        assertEquals(paymentList.size(), paymentRequestList.size());
        assertEquals(paymentList.get(0).getId(), paymentRequestList.get(0).getId());
        assertEquals(paymentList.get(0).getBookingsId(), paymentRequestList.get(0).getBookingsId());
        assertEquals(paymentList.get(0).getPaymentState(), paymentRequestList.get(0).getPaymentState());
        assertEquals(paymentList.get(0).getPrice(), paymentRequestList.get(0).getPrice());
        assertEquals(paymentList.get(0).getCurrency(), paymentRequestList.get(0).getCurrency());

        assertEquals(paymentList.get(1).getId(), paymentRequestList.get(1).getId());
        assertEquals(paymentList.get(1).getBookingsId(), paymentRequestList.get(1).getBookingsId());
        assertEquals(paymentList.get(1).getPaymentState(), paymentRequestList.get(1).getPaymentState());
        assertEquals(paymentList.get(1).getPrice(), paymentRequestList.get(1).getPrice());
        assertEquals(paymentList.get(1).getCurrency(), paymentRequestList.get(1).getCurrency());
    }

    @Test
    public void testConvertPaymentListToPaymentRequestList() {
        List<Payment> paymentList = new ArrayList<>();

        Payment paymentOne = new Payment();
        paymentOne.setId(222L);
        paymentOne.setBookingsId(Arrays.asList(122L, 2333L, 3444L, 455L, 566L));
        paymentOne.setPaymentState(State.SUCCESS);
        paymentOne.setPrice(new BigDecimal("2123132324.45454"));
        paymentOne.setCurrency(Currency.CZK);

        Payment paymentTwo = new Payment();
        paymentTwo.setId(333L);
        paymentTwo.setBookingsId(Arrays.asList(124L, 2335L, 34564L, 456L, 567L));
        paymentTwo.setPaymentState(State.CREATED);
        paymentTwo.setPrice(new BigDecimal("4520524.45454"));
        paymentTwo.setCurrency(Currency.EUR);

        paymentList.add(paymentOne);
        paymentList.add(paymentTwo);

        List<PaymentRequest> paymentRequestList = paymentMapper.convertToPaymentRequestList(paymentList);
        assertEquals(paymentRequestList.size(), paymentList.size());
        assertEquals(paymentRequestList.get(0).getId(), paymentList.get(0).getId());
        assertEquals(paymentRequestList.get(0).getBookingsId(), paymentList.get(0).getBookingsId());
        assertEquals(paymentRequestList.get(0).getPaymentState(), paymentList.get(0).getPaymentState());
        assertEquals(paymentRequestList.get(0).getPrice(), paymentList.get(0).getPrice());
        assertEquals(paymentRequestList.get(0).getCurrency(), paymentList.get(0).getCurrency());

        assertEquals(paymentRequestList.get(1).getId(), paymentList.get(1).getId());
        assertEquals(paymentRequestList.get(1).getBookingsId(), paymentList.get(1).getBookingsId());
        assertEquals(paymentRequestList.get(1).getPaymentState(), paymentList.get(1).getPaymentState());
        assertEquals(paymentRequestList.get(1).getPrice(), paymentList.get(1).getPrice());
        assertEquals(paymentRequestList.get(1).getCurrency(), paymentList.get(1).getCurrency());
    }

    @Test
    public void testConvertPaymentListToPaymentResponseList() {
        List<Payment> paymentList = new ArrayList<>();

        Payment paymentOne = new Payment();
        paymentOne.setId(222L);
        paymentOne.setBookingsId(Arrays.asList(122L, 2333L, 3444L, 455L, 566L));
        paymentOne.setPaymentState(State.SUCCESS);
        paymentOne.setPrice(new BigDecimal("2123132324.45454"));
        paymentOne.setCurrency(Currency.CZK);

        Payment paymentTwo = new Payment();
        paymentTwo.setId(333L);
        paymentTwo.setBookingsId(Arrays.asList(124L, 2335L, 34564L, 456L, 567L));
        paymentTwo.setPaymentState(State.CREATED);
        paymentTwo.setPrice(new BigDecimal("4520524.45454"));
        paymentTwo.setCurrency(Currency.EUR);

        paymentList.add(paymentOne);
        paymentList.add(paymentTwo);

        List<PaymentResponse> paymentResponseList = paymentMapper.convertToPaymentResponseList(paymentList);
        assertEquals(paymentResponseList.size(), paymentList.size());
        assertEquals(paymentResponseList.get(0).getId(), paymentList.get(0).getId());
        assertEquals(paymentResponseList.get(0).getBookingsId(), paymentList.get(0).getBookingsId());
        assertEquals(paymentResponseList.get(0).getPaymentState(), paymentList.get(0).getPaymentState());
        assertEquals(paymentResponseList.get(0).getPrice(), paymentList.get(0).getPrice());
        assertEquals(paymentResponseList.get(0).getCurrency(), paymentList.get(0).getCurrency());

        assertEquals(paymentResponseList.get(1).getId(), paymentList.get(1).getId());
        assertEquals(paymentResponseList.get(1).getBookingsId(), paymentList.get(1).getBookingsId());
        assertEquals(paymentResponseList.get(1).getPaymentState(), paymentList.get(1).getPaymentState());
        assertEquals(paymentResponseList.get(1).getPrice(), paymentList.get(1).getPrice());
        assertEquals(paymentResponseList.get(1).getCurrency(), paymentList.get(1).getCurrency());
    }
}
