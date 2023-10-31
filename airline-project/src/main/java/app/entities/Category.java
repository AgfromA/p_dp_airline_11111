package app.entities;

import app.enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
 * Класс сиденья на рейс.
 */
@Entity
@Table(name = "category")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_category")
    @SequenceGenerator(name = "seq_category", initialValue = 1000, allocationSize = 1)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    @Column(name = "category_type")
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;
}
