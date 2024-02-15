package app.dto;

import app.enums.Airport;
import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DestinationDto {

    @ReadOnlyProperty
    private Long id;

    @NotNull(message = "Airport cannot be null")
    private Airport airportCode;

    @NotBlank(message = "Field should not be empty")
    @Size(min = 2, max = 9, message = "Timezone must be between 2 and 9 characters")
    private String timezone;
}