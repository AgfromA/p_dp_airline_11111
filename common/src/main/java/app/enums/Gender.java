package app.enums;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    @JsonProperty("male")
    @JsonAlias({"MALE", "m", "M"})
    MALE,
    @JsonProperty("female")
    @JsonAlias({"FEMALE", "f", "F"})
    FEMALE
}
