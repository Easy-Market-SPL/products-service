package co.edu.javeriana.easymarket.productsservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "variant_option")
public class VariantOption {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "variant_option_id_gen")
    @SequenceGenerator(name = "variant_option_id_gen", sequenceName = "variant_option_id_variant_option_seq", allocationSize = 1)
    @Column(name = "id_variant_option", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "img_url", length = 700)
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id_variant", nullable = false)
    private Variant variantIdVariant;

}