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
public class ProductColorId implements Serializable {
    private static final long serialVersionUID = -2191420623828029006L;
    @Column(name = "product_code", nullable = false, length = 200)
    private String productCode;

    @Column(name = "id_color", nullable = false)
    private Integer idColor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductColorId entity = (ProductColorId) o;
        return Objects.equals(this.productCode, entity.productCode) &&
                Objects.equals(this.idColor, entity.idColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, idColor);
    }

}