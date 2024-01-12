package app.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class AircraftDto {
    @ReadOnlyProperty
    private Long id;

    @NotBlank(message = "field \"aircraftNumber\" should not be empty!")
    @Size(min = 4, max = 15, message = "Length of Aircraft Number should be between 4 and 15 characters")
    private String aircraftNumber;

    @NotBlank(message = "field \"model\" should not be empty!")
    private String model;

    @NotNull(message = "field \"modelYear\" should not be empty!")
    @Min(value = 2000, message = "modelYear should be later than 2000")
    private int modelYear;

    @NotNull(message = "field \"flightRange\" should not be empty!")
    private int flightRange;

}