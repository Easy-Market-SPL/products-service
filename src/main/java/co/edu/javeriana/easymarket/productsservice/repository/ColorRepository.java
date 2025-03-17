package co.edu.javeriana.easymarket.productsservice.repository;

import co.edu.javeriana.easymarket.productsservice.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Integer> {
}