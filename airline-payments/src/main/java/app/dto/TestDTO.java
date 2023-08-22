package app.dto;


import app.entities.account.Test;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@JsonTypeName(value = "Test")
public class TestDTO {


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Field should not be empty")
    @Size(min = 2, max = 128, message = "Size first_name cannot be less than 2 and more than 128 characters")
    private String firstName;

    @NotBlank(message = "Field should not be empty")
    @Size(min = 2, max = 128, message = "Size last_name cannot be less than 2 and more than 128 characters")
    private String lastName;


    @Email
    @NotBlank(message = "The field cannot be empty")
    private String email;


    @NotBlank(message = "The field cannot be empty")
    private String password;


    public TestDTO(Test test) {
        this.id = test.getId();
        this.firstName = test.getFirstName();
        this.lastName = test.getLastName();
        this.email = test.getEmail();
        this.password = test.getPassword();

    }
}
