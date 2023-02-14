package app.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.util.Set;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"email"})
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Admin.class, name = "admin"),
        @JsonSubTypes.Type(value = AirlineManager.class, name = "manager"),
        @JsonSubTypes.Type(value = Passenger.class, name = "passenger")}
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
    @SequenceGenerator(name = "seq_user", initialValue = 1000, allocationSize = 1)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Email
    @NotBlank(message = "The field cannot be empty")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "The field cannot be empty")
    @Column(name = "password")
    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9@#$%]).{8,}", message = "min 8 characters, 1 uppercase latter" +
            "1 lowercase latter, at least 1 number, 1 special character")
    private String password;

    @NotBlank(message = "The field cannot be empty")
    @Column (name = "security_question")
    private String securityQuestion;

    @NotBlank(message = "The field cannot be empty")
    @Column (name = "answer_question")
    private String answerQuestion;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Role> roles;
}
