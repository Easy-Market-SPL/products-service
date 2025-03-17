package co.edu.javeriana.easymarket.productsservice.repository;

import co.edu.javeriana.easymarket.productsservice.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepository extends JpaRepository<Variant, Integer> {
}