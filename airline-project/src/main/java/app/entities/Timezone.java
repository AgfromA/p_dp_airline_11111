package app.entities;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "timezones")
@EqualsAndHashCode(of = {"cityName", "countryName"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Timezone {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY, generator = "seq_timezones")
    @SequenceGenerator(name = "seq_timezones", initialValue = 1000, allocationSize = 1)
    private Long id;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "gmt")
    private String gmt;

    @Column(name = "gmt_winter")
    private String gmtWinter;
}