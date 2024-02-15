package app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@JsonTypeName(value = "account")
public class AccountDto {

    @ReadOnlyProperty
    private Long id;

    @NotBlank(message = "Field should not be empty")
    @Size(min = 2, max = 128, message = "Size first_name cannot be less than 2 and more than 128 characters")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "First name must contain only letters")
    private String firstName;

    @NotBlank(message = "Field should not be empty")
    @Size(min = 2, max = 128, message = "Size last_name cannot be less than 2 and more than 128 characters")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Last name must contain only letters")
    private String lastName;

    @NotNull(message = "Field should not be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth can not be a future time")
    private LocalDate birthDate;

    @Email
    @NotBlank(message = "The field cannot be empty")
    @Size(max = 256, message = "Size email cannot be more than 256 characters")
    @Pattern(regexp = "^[\\w.-]+@[a-zA-Z_-]+\\.[a-zA-Z]{2,}$", message = "Email address must adhere to the standard format: example@example.com")
    private String email;

    @NotBlank(message = "Field should not be empty")
    @Size(min = 6, max = 64, message = "Size phone cannot be less than 6 and more than 64 characters")
    private String phoneNumber;

    @NotBlank(message = "The field cannot be empty")
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,}", message = "min 8 characters, 1 uppercase latter" +
            "1 lowercase latter, at least 1 number, 1 special character")
    private String password;

    @NotBlank(message = "Field should not be empty")
    @Size(max = 256, message = "Size security question cannot be more than 256 characters")
    private String securityQuestion;

    @NotBlank(message = "Field should not be empty")
    private String answerQuestion;

    private Set<RoleDto> roles;
}