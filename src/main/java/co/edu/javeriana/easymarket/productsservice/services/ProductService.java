package co.edu.javeriana.easymarket.productsservice.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import co.edu.javeriana.easymarket.productsservice.model.Color;
import co.edu.javeriana.easymarket.productsservice.model.Label;
import co.edu.javeriana.easymarket.productsservice.model.Product;
import co.edu.javeriana.easymarket.productsservice.model.Variant;
import co.edu.javeriana.easymarket.productsservice.model.VariantOption;
import co.edu.javeriana.easymarket.productsservice.repository.ColorRepository;
import co.edu.javeriana.easymarket.productsservice.repository.LabelRepository;
import co.edu.javeriana.easymarket.productsservice.repository.ProductRepository;
import co.edu.javeriana.easymarket.productsservice.repository.VariantRepository;

import org.springframework.stereotype.Service;

import co.edu.javeriana.easymarket.productsservice.dtos.ColorDTO;
import co.edu.javeriana.easymarket.productsservice.dtos.LabelDTO;
import co.edu.javeriana.easymarket.productsservice.dtos.ProductDTO;
import co.edu.javeriana.easymarket.productsservice.dtos.VariantDTO;
import co.edu.javeriana.easymarket.productsservice.dtos.VariantOptionDTO;
import co.edu.javeriana.easymarket.productsservice.exception.ErrorMessages;
import co.edu.javeriana.easymarket.productsservice.exception.businessexceptions.BadRequestException;
import co.edu.javeriana.easymarket.productsservice.mappers.ColorMapper;
import co.edu.javeriana.easymarket.productsservice.mappers.LabelMapper;
import co.edu.javeriana.easymarket.productsservice.mappers.ProductMapper;
import co.edu.javeriana.easymarket.productsservice.mappers.VariantMapper;
import co.edu.javeriana.easymarket.productsservice.services.util.ValidationService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final LabelRepository labelRepository;
    private final VariantRepository variantRepository;
    private final ColorRepository colorRepository;
    private final ProductMapper productMapper;
    private final LabelMapper labelMapper;
    private final VariantMapper variantMapper;
    private final ColorMapper colorMapper;
    private final ValidationService validationService;

    public List<ProductDTO> getAllProducts() {
        List<Product> products = (List<Product>) productRepository.findByDeletedFalse();
        return products.stream()
                .map(product -> productMapper.productToProductDTO(product))
                .collect(Collectors.toList());
    }

    public ProductDTO getProductByCode(String code) {
        Product product = findProductByCode(code);

        return productMapper.productToProductDTO(product);
    }

    public List<LabelDTO> getProductLabels(String productCode) {
        Product product = findProductByCode(productCode);

        return labelMapper.labelsToLabelDTOs(product.getLabels());
    }

    public List<VariantDTO> getProductVariants(String productCode) {
        Product product = findProductByCode(productCode);

        return product.getVariants().stream()
                .map(variant -> variantMapper.variantToVariantDTO(variant))
                .collect(Collectors.toList());
    }

    public List<ColorDTO> getProductColors(String productCode) {
        Product product = findProductByCode(productCode);
        
        return colorMapper.colorsToColorDTOs(product.getColors());
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
        
        Product existingProduct = findProductByCode(code);
        
        // Update fields but maintain original code and relationships
        productMapper.updateProductFromDTO(productDTO, existingProduct);
        
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.productToProductDTO(updatedProduct);
    }
    
    public List<LabelDTO> updateProductLabels(String productCode, List<LabelDTO> newLabelsDTO) {
        Product product = findProductByCode(productCode);
        
        Set<Label> newLabels = newLabelsDTO.stream()
                .map(labelDTO -> validationService.validateExists(
                        labelRepository.findById(labelDTO.getId()),
                        ErrorMessages.LabelErrorMessages.notFound(labelDTO.getId())))
                .collect(Collectors.toSet());

        // This single method handles all the complex relationship updates
        product.updateLabels(newLabels);
        
        productRepository.save(product);

        return labelMapper.labelsToLabelDTOs(product.getLabels());
    }

    public List<VariantDTO> updateProductVariants(String code, List<VariantDTO> variantDTOs) {
        Product product = findProductByCode(code);
        
        // Clear existing variants if replacing them all
        product.getVariants().clear();
        
        // Add each new variant
        if (variantDTOs != null) {
            for (VariantDTO variantDTO : variantDTOs) {
                // Create and add the variant to product
                Variant variant = new Variant();
                variant.setName(variantDTO.getName());
                variant.setProductCode(product);
                product.getVariants().add(variant);

                // Save the product to ensure variant has an ID
                variantRepository.save(variant);

                // Now add options to the variant
                if (variantDTO.getOptions() != null) {
                    for (VariantOptionDTO optionDTO : variantDTO.getOptions()) {
                        VariantOption option = new VariantOption();
                        option.setName(optionDTO.getName());
                        option.setImgUrl(optionDTO.getImgUrl());
                        option.setVariantIdVariant(variant);
                        variant.getOptions().add(option);
                    }
                }
            }
        }

        // Final save of everything
        Product savedProduct = productRepository.save(product);

        // Convert back to DTOs for response
        return variantMapper.variantsToVariantDTOs(savedProduct.getVariants());
    }

    public List<ColorDTO> updateProductColors(String productCode, List<ColorDTO> colorDTOs) {
        Product product = findProductByCode(productCode);
        
        Set<Color> newColors = colorDTOs.stream()
                .map(colorDTO -> validationService.validateExists(
                        colorRepository.findById(colorDTO.getIdColor()),
                        ErrorMessages.ColorErrorMessages.notFound(colorDTO.getIdColor())))
                .collect(Collectors.toSet());

        // This handles all the relationship updates
        product.updateColors(newColors);
        
        productRepository.save(product);

        return colorMapper.colorsToColorDTOs(product.getColors());
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

    private Product findProductByCode(String code) {
        return validationService.validateExists(
            productRepository.findByCode(code),
            ErrorMessages.ProductErrorMessages.notFound(code));
    }

}
