package app.entities;

import app.entities.Passport;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDate;

/**
 * Данные пассажира, необходимые для покупки билетов.
 */
@Entity
@Table(name = "passengers")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"firstName", "lastName", "birthDate", "phoneNumber", "passport"})
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_passenger")
    @SequenceGenerator(name = "seq_passenger", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Valid
    @Embedded
    private Passport passport;
}