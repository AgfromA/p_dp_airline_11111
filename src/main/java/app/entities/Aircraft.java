package app.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "field \"aircraftNumber\" should not be empty!")
    @Column(unique = true, name = "aircraft_number")
    @Size(min = 4, max = 15, message = "Aircraft Number should be between 4 and 15 characters")
    private String aircraftNumber;

    @NotEmpty(message = "field \"model\" should not be empty!")
    private String model;

    @NotEmpty(message = "field \"modelYear\" should not be empty!")
    @Min(value = 2000, message = "modelYear should be later than 2000")
    @Column(unique = true, name = "model_year")
    private int modelYear;

    @NotEmpty(message = "field \"flightRange\" should not be empty!")
    @Column(unique = true, name = "flight_range")
    private int flightRange;

//    @NotEmpty
//    @OneToMany(mappedBy = "aircraft")
//    private List<Seat> seatList;
}