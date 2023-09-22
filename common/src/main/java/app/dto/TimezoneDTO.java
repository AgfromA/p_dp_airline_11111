package app.dto;

//import app.entities.Timezone;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TimezoneDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Field should not be empty")
    @Size(min = 3, max = 30, message = "Country name must be between 3 and 30 characters")
    private String countryName;

    @NotBlank(message = "Field should not be empty")
    @Size(min = 3, max = 30, message = "Country name must be between 3 and 30 characters")
    private String cityName;

    @NotBlank(message = "Field should not be empty")
    @Size(min = 2, max = 9, message = "GMT name must be between 3 and 7 characters")
    private String gmt;

    @NotBlank(message = "Field should not be empty")
    @Size(min = 2, max = 9, message = "GMT winter name must be between 2 and 7 characters")
    private String gmtWinter;
//
//    public TimezoneDTO(Timezone timezone) {
//        this.id = timezone.getId();
//        this.countryName = timezone.getCountryName();
//        this.cityName = timezone.getCityName();
//        this.gmt = timezone.getGmt();
//        this.gmtWinter = timezone.getGmtWinter();
//    }
}