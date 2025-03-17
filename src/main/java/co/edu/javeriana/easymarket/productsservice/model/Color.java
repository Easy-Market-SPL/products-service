package co.edu.javeriana.easymarket.productsservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "color_id_gen")
    @SequenceGenerator(name = "color_id_gen", sequenceName = "color_id_color_seq", allocationSize = 1)
    @Column(name = "id_color", nullable = false)
    private Integer id;

    @Column(name = "hexa_code", nullable = false, length = 6)
    private String hexaCode;

    @Column(name = "img_url", length = 700)
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_code", nullable = false)
    private co.edu.javeriana.easymarket.productsservice.model.Product productCode;

}