package co.edu.javeriana.easymarket.productsservice.repository;

import co.edu.javeriana.easymarket.productsservice.model.LabelProduct;
import co.edu.javeriana.easymarket.productsservice.model.LabelProductId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelProductRepository extends JpaRepository<LabelProduct, LabelProductId> {
}