package co.edu.javeriana.easymarket.productsservice.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "variant")
public class Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "variant_id_gen")
    @SequenceGenerator(name = "variant_id_gen", sequenceName = "variant_id_variant_seq", allocationSize = 1)
    @Column(name = "id_variant", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_code", nullable = false)
    private Product productCode;

    @OneToMany(mappedBy = "variantIdVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VariantOption> options = new HashSet<>();

    public void addOption(VariantOption option) {
        option.setVariantIdVariant(this);
        options.add(option);
    }
    
    public void removeOption(VariantOption option) {
        options.remove(option);
        option.setVariantIdVariant(null);
    }

}