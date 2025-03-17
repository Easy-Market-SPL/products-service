package co.edu.javeriana.easymarket.productsservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class LabelProductId implements Serializable {
    private static final long serialVersionUID = 7219375530807871793L;
    @Column(name = "label_id_label", nullable = false)
    private Integer labelIdLabel;

    @Column(name = "product_code", nullable = false, length = 200)
    private String productCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LabelProductId entity = (LabelProductId) o;
        return Objects.equals(this.productCode, entity.productCode) &&
                Objects.equals(this.labelIdLabel, entity.labelIdLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, labelIdLabel);
    }

}