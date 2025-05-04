package co.edu.javeriana.easymarket.productsservice.mappers;

import co.edu.javeriana.easymarket.productsservice.model.Product;
import lombok.AllArgsConstructor;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import co.edu.javeriana.easymarket.productsservice.dtos.ProductDTO;

@Component
@AllArgsConstructor
public class ProductMapper {
    private final ModelMapper modelMapper;
    private final LabelMapper labelMapper;

    public ProductDTO productToProductDTO(Product product) {
        ProductDTO dto = modelMapper.map(product, ProductDTO.class);
        
        if (product.getLabels() != null) {
            dto.setLabels(
                product.getLabels().stream()
                    .map(labelMapper::labelToLabelDTO)
                    .collect(Collectors.toList())
            );
        }
        
        return dto;
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
