package co.edu.javeriana.easymarket.productsservice.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "labelIdLabel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LabelProduct> labelProducts = new HashSet<>();

    // Helper method to get all Products
    public Set<Product> getProducts() {
        return labelProducts.stream()
            .map(LabelProduct::getProductCode)
            .collect(Collectors.toSet());
    }
}