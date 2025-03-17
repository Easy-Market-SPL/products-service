package co.edu.javeriana.easymarket.productsservice.repository;

import co.edu.javeriana.easymarket.productsservice.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Integer> {
}