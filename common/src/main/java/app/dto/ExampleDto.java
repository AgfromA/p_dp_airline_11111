package app.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;



@Data
@Setter
public class ExampleDto {

    @ReadOnlyProperty
    private Long id;

    @Size(min = 1, max = 255, message = "Length of Example's exampleText should be between 1 and 255 characters")
    @NotEmpty(message = "Example's exampleText should have at least one character")
    private String exampleText;
}