package co.edu.javeriana.easymarket.productsservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "label")
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "label_id_gen")
    @SequenceGenerator(name = "label_id_gen", sequenceName = "label_id_label_seq", allocationSize = 1)
    @Column(name = "id_label", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "description", length = 45)
    private String description;

}