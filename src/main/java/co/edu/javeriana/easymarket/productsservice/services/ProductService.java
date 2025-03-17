package co.edu.javeriana.easymarket.productsservice.services;

import java.util.List;
import java.util.stream.Collectors;

import co.edu.javeriana.easymarket.productsservice.model.Product;
import co.edu.javeriana.easymarket.productsservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import co.edu.javeriana.easymarket.productsservice.dtos.ProductDTO;
import co.edu.javeriana.easymarket.productsservice.exception.ErrorMessages;
import co.edu.javeriana.easymarket.productsservice.exception.businessexceptions.BadRequestException;
import co.edu.javeriana.easymarket.productsservice.mappers.ProductMapper;
import co.edu.javeriana.easymarket.productsservice.services.util.ValidationService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ValidationService validationService;

    public List<ProductDTO> getAllProducts() {
        List<Product> products = (List<Product>) productRepository.findByDeletedFalse();
        return products.stream()
                .map(product -> productMapper.productToProductDTO(product))
                .collect(Collectors.toList());
    }

    public ProductDTO getProductByCode(String code) {

        Product product = validationService.validateExists(
                productRepository.findByCode(code),
                ErrorMessages.ProductErrorMessages.notFound(code));

        return productMapper.productToProductDTO(product);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        validateProductData(productDTO);

        validationService.validateNotExists(
                productRepository.findByCode(productDTO.getCode()),
                ErrorMessages.ProductErrorMessages.alreadyExists(productDTO.getCode()));

        Product product = productMapper.productDTOToProduct(productDTO);
        product.setDeleted(false);
        Product savedProduct = productRepository.save(product);
        return productMapper.productToProductDTO(savedProduct);
    }

    public ProductDTO updateProduct(String code, ProductDTO productDTO) {
        validateProductData(productDTO);
        
        // Ensure product exists
        Product existingProduct = validationService.validateExists(
                productRepository.findByCode(code),
                ErrorMessages.ProductErrorMessages.notFound(code));
        
        // Update fields but maintain original code and relationships
        productMapper.updateProductFromDTO(productDTO, existingProduct);
        
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.productToProductDTO(updatedProduct);
    }

    public void deleteProduct(String code) {
        Product existingProduct = validationService.validateExists(
            productRepository.findByCode(code),
            ErrorMessages.ProductErrorMessages.notFound(code));

        try {
            existingProduct.setDeleted(true);
            productRepository.save(existingProduct);
        } catch (Exception e) {
            throw new BadRequestException(ErrorMessages.ProductErrorMessages.failedToDelete(code));
        }
    }
    
    private void validateProductData(ProductDTO product) {
        if (product == null || product.getName() == null || product.getName().trim().isEmpty() 
                || product.getPrice() == null || product.getPrice() <= 0) {
            throw new BadRequestException(ErrorMessages.ProductErrorMessages.invalidData());
        }
    }
}
