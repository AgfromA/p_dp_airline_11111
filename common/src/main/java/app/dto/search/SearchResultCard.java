package app.dto.search;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchResultCard {
    private Integer totalPrice;

    private SearchResultCardData dataTo;

    private SearchResultCardData dataBack;
}
