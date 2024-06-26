package app.dto;

import app.enums.Currency;
import app.enums.State;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PaymentRequest {

    @ReadOnlyProperty
    private Long id;

    private List<Long> bookingsId = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private State paymentState;

    private BigDecimal price;

    private Currency currency;
}