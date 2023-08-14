package app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Сущность - пример
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "example")
public class Example {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "example_text")
    private String exampleText;
}