package app.entities;

import app.enums.Airport;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Точка назначения.
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "destination")
@SQLDelete(sql = "update destination set is_deleted=true where id=?")
@Where(clause = "is_deleted = false")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_destination")
    @SequenceGenerator(name = "seq_destination", initialValue = 1000, allocationSize = 1)
    private Long id;

    @Column(name = "airport_code")
    @Enumerated(EnumType.STRING)
    private Airport airportCode;

    @Column(name = "airport_name")
    private String airportName;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}
