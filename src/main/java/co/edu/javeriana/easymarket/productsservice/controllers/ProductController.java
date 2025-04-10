package co.edu.javeriana.easymarket.productsservice.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.easymarket.productsservice.dtos.ColorDTO;
import co.edu.javeriana.easymarket.productsservice.dtos.LabelDTO;
import co.edu.javeriana.easymarket.productsservice.dtos.ProductDTO;
import co.edu.javeriana.easymarket.productsservice.dtos.VariantDTO;
import co.edu.javeriana.easymarket.productsservice.services.ProductService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;


    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }


    @GetMapping("/{code}")
    public ResponseEntity<ProductDTO> getProductByCode(@PathVariable String code) {
        return ResponseEntity.ok(productService.getProductByCode(code));
    }

    @GetMapping("/{code}/labels")
    public ResponseEntity<List<LabelDTO>> getProductLabels(@PathVariable String code) {
        return ResponseEntity.ok(productService.getProductLabels(code));
    }

    @GetMapping("/{code}/variants")
    public ResponseEntity<List<VariantDTO>> getProductVariants(@PathVariable String code) {
        return ResponseEntity.ok(productService.getProductVariants(code));
    }

    @GetMapping("/{code}/colors")
    public ResponseEntity<List<ColorDTO>> getProductColors(@PathVariable String code) {
        return ResponseEntity.ok(productService.getProductColors(code));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product) {
        ProductDTO createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }


    @PutMapping("/{code}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable String code, @RequestBody ProductDTO product) {
        return ResponseEntity.ok(productService.updateProduct(code, product));
    }

    @PutMapping("/{code}/labels")
    public ResponseEntity<List<LabelDTO>> updateProductLabels(@PathVariable String code,
            @RequestBody List<LabelDTO> newLabelsDTO) {
        return ResponseEntity.ok(productService.updateProductLabels(code, newLabelsDTO));
    }

    @PutMapping("/{code}/variants")
    public ResponseEntity<List<VariantDTO>> updateProductVariants(
            @PathVariable String code,
            @RequestBody List<VariantDTO> variantDTOs) {
        return ResponseEntity.ok(productService.updateProductVariants(code, variantDTOs));
    }

    @PutMapping("/{code}/colors")
    public ResponseEntity<List<ColorDTO>> updateProductColors(
            @PathVariable String code,
            @RequestBody List<ColorDTO> colorDTOs) {
        return ResponseEntity.ok(productService.updateProductColors(code, colorDTOs));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String code) {
        productService.deleteProduct(code);
        return ResponseEntity.noContent().build();
    }
}
