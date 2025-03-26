package co.edu.javeriana.easymarket.productsservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "label_product")
public class LabelProduct {
    @SequenceGenerator(name = "label_product_id_gen", sequenceName = "label_id_label_seq", allocationSize = 1)
    @EmbeddedId
    private LabelProductId id;

    @MapsId("labelIdLabel")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "label_id_label", nullable = false)
    private Label labelIdLabel;

    @MapsId("productCode")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_code", nullable = false)
    private Product productCode;

}