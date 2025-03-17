package co.edu.javeriana.easymarket.productsservice.mappers;

import co.edu.javeriana.easymarket.productsservice.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import co.edu.javeriana.easymarket.productsservice.dtos.ProductDTO;

@Component
public class ProductMapper {
    private final ModelMapper modelMapper;

    public ProductMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProductDTO productToProductDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }
    
    public Product productDTOToProduct(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }

    public void updateProductFromDTO(ProductDTO productDTO, Product existingProduct) {
        // Update only specific fields manually
        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setImgUrl(productDTO.getImgUrl());
    }
}
