package app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@JsonTypeName(value = "accountUpdate")
public class AccountUpdateDto {
    @ReadOnlyProperty
    private Long id;

    @Size(max = 128, message = "Size username cannot be less than 2 and more than 128 characters")
    private String username;

    @Size(max = 128, message = "Size first_name cannot be less than 2 and more than 128 characters")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "First name must contain only letters")
    private String firstName;

    @Size(max = 128, message = "Size last_name cannot be less than 2 and more than 128 characters")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Last name must contain only letters")
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth can not be a future time")
    private LocalDate birthDate;

    @Email
    @Size(max = 256, message = "Size email cannot be more than 256 characters")
    @Pattern(regexp = "^[\\w.-]+@[a-zA-Z_-]+\\.[a-zA-Z]{2,}$", message = "Email address must adhere to the standard format: example@example.com")
    private String email;

    @Size(max = 64, message = "Size phone cannot be less than 6 and more than 64 characters")
    private String phoneNumber;


    private String password;

    @Size(max = 256, message = "Size security question cannot be more than 256 characters")
    private String securityQuestion;


    private String answerQuestion;

    private Set<RoleDto> roles;
}
