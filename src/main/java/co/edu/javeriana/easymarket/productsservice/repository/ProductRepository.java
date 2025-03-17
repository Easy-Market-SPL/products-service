package co.edu.javeriana.easymarket.productsservice.repository;

import java.util.Optional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.javeriana.easymarket.productsservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByDeletedFalse();
    Optional<Product> findByCode(String code);
  }