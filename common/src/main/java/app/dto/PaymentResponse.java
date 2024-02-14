package app.dto;

import app.enums.Currency;
import app.enums.State;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
public class PaymentResponse {

    @ReadOnlyProperty
    private Long id;


    private List<Long> bookingsId = new ArrayList<>();


    private State paymentState;

    private BigDecimal price;

    private Currency currency;
}
