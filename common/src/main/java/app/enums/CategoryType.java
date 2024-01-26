package app.enums;

import lombok.Getter;

@Getter
public enum CategoryType {
    FIRST(2.5f),
    BUSINESS(2f),
    PREMIUM_ECONOMY(1.2f),
    ECONOMY(1f);

    private final float categoryRatio;

    CategoryType(float categoryRatio) {
        this.categoryRatio = categoryRatio;
    }
}