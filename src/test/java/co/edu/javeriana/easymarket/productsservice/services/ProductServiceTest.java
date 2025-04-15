package co.edu.javeriana.easymarket.productsservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.*;

import co.edu.javeriana.easymarket.productsservice.dtos.*;
import co.edu.javeriana.easymarket.productsservice.exception.ErrorMessages;
import co.edu.javeriana.easymarket.productsservice.exception.businessexceptions.BadRequestException;
import co.edu.javeriana.easymarket.productsservice.mappers.*;
import co.edu.javeriana.easymarket.productsservice.model.*;
import co.edu.javeriana.easymarket.productsservice.repository.*;
import co.edu.javeriana.easymarket.productsservice.services.util.ValidationService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private LabelRepository labelRepository;
    
    @Mock
    private VariantRepository variantRepository;
    
    @Mock
    private ColorRepository colorRepository;
    
    @Mock
    private ProductMapper productMapper;
    
    @Mock
    private LabelMapper labelMapper;
    
    @Mock
    private VariantMapper variantMapper;
    
    @Mock
    private ColorMapper colorMapper;
    
    @Mock
    private ValidationService validationService;
    
    @InjectMocks
    private ProductService productService;
    
    private Product testProduct;
    private ProductDTO testProductDTO;
    private Label testLabel;
    private LabelDTO testLabelDTO;
    private Variant testVariant;
    private VariantDTO testVariantDTO;
    private Color testColor;
    private ColorDTO testColorDTO;
    
    @BeforeEach
    void setUp() {
        // Setup test data
        testProduct = new Product();
        testProduct.setCode("TEST001");
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(100.0f);
        testProduct.setDeleted(false);
        
        testProductDTO = new ProductDTO();
        testProductDTO.setCode("TEST001");
        testProductDTO.setName("Test Product");
        testProductDTO.setDescription("Test Description");
        testProductDTO.setPrice(100.0f);
        
        testLabel = new Label();
        testLabel.setId(1);
        testLabel.setName("Test Label");
        testLabel.setDescription("Test Label Description");
        
        testLabelDTO = new LabelDTO();
        testLabelDTO.setId(1);
        testLabelDTO.setName("Test Label");
        testLabelDTO.setDescription("Test Label Description");
        
        testVariant = new Variant();
        testVariant.setId(1);
        testVariant.setName("Test Variant");
        testVariant.setProductCode(testProduct);
        
        testVariantDTO = new VariantDTO();
        testVariantDTO.setId(1);
        testVariantDTO.setName("Test Variant");
        
        testColor = new Color();
        testColor.setIdColor(1);
        testColor.setName("Test Color");
        testColor.setHexaCode("FFFFFF");
        
        testColorDTO = new ColorDTO();
        testColorDTO.setIdColor(1);
        testColorDTO.setName("Test Color");
        testColorDTO.setHexaCode("FFFFFF");
    }
    
    @Test
    void getAllProducts_ShouldReturnAllNonDeletedProducts() {
        // Arrange
        List<Product> products = List.of(testProduct);
        List<ProductDTO> expectedDTOs = List.of(testProductDTO);
        
        when(productRepository.findByDeletedFalse()).thenReturn(products);
        when(productMapper.productToProductDTO(any(Product.class))).thenReturn(testProductDTO);
        
        // Act
        List<ProductDTO> result = productService.getAllProducts();
        
        // Assert
        assertEquals(expectedDTOs.size(), result.size());
        assertEquals(expectedDTOs.get(0).getCode(), result.get(0).getCode());
        verify(productRepository).findByDeletedFalse();
        verify(productMapper, times(products.size())).productToProductDTO(any(Product.class));
    }
    
    @Test
    void getProductByCode_ShouldReturnProduct_WhenProductExists() {
        // Arrange
        String productCode = "TEST001";
        Optional<Product> optionalProduct = Optional.of(testProduct);
        
        when(productRepository.findByCode(productCode)).thenReturn(optionalProduct);
        when(validationService.validateExists(eq(optionalProduct), anyString())).thenReturn(testProduct);
        when(productMapper.productToProductDTO(testProduct)).thenReturn(testProductDTO);
        
        // Act
        ProductDTO result = productService.getProductByCode(productCode);
        
        // Assert
        assertNotNull(result);
        assertEquals(testProductDTO.getCode(), result.getCode());
        verify(productRepository).findByCode(productCode);
        verify(validationService).validateExists(eq(optionalProduct), anyString());
        verify(productMapper).productToProductDTO(testProduct);
    }
    
    @Test
    void getProductLabels_ShouldReturnLabels_WhenProductExists() {
        // Arrange
        String productCode = "TEST001";
        Optional<Product> optionalProduct = Optional.of(testProduct);
        List<LabelDTO> expectedDTOs = List.of(testLabelDTO);
        
        when(productRepository.findByCode(productCode)).thenReturn(optionalProduct);
        when(validationService.validateExists(eq(optionalProduct), anyString())).thenReturn(testProduct);
        when(labelMapper.labelsToLabelDTOs(any())).thenReturn(expectedDTOs);
        
        // Act
        List<LabelDTO> result = productService.getProductLabels(productCode);
        
        // Assert
        assertNotNull(result);
        assertEquals(expectedDTOs.size(), result.size());
        verify(productRepository).findByCode(productCode);
        verify(validationService).validateExists(eq(optionalProduct), anyString());
        verify(labelMapper).labelsToLabelDTOs(any());
    }
    
    @Test
    void getProductVariants_ShouldReturnVariants_WhenProductExists() {
        // Arrange
        String productCode = "TEST001";
        Optional<Product> optionalProduct = Optional.of(testProduct);
        Set<Variant> variants = new HashSet<>();
        variants.add(testVariant);
        testProduct.setVariants(variants);
        
        when(productRepository.findByCode(productCode)).thenReturn(optionalProduct);
        when(validationService.validateExists(eq(optionalProduct), anyString())).thenReturn(testProduct);
        when(variantMapper.variantToVariantDTO(testVariant)).thenReturn(testVariantDTO);
        
        // Act
        List<VariantDTO> result = productService.getProductVariants(productCode);
        
        // Assert
        assertNotNull(result);
        assertEquals(variants.size(), result.size());
        verify(productRepository).findByCode(productCode);
        verify(validationService).validateExists(eq(optionalProduct), anyString());
        verify(variantMapper).variantToVariantDTO(testVariant);
    }
    
    @Test
    void getProductColors_ShouldReturnColors_WhenProductExists() {
        // Arrange
        String productCode = "TEST001";
        Optional<Product> optionalProduct = Optional.of(testProduct);
        List<ColorDTO> expectedDTOs = List.of(testColorDTO);
        
        when(productRepository.findByCode(productCode)).thenReturn(optionalProduct);
        when(validationService.validateExists(eq(optionalProduct), anyString())).thenReturn(testProduct);
        when(colorMapper.colorsToColorDTOs(any())).thenReturn(expectedDTOs);
        
        // Act
        List<ColorDTO> result = productService.getProductColors(productCode);
        
        // Assert
        assertNotNull(result);
        assertEquals(expectedDTOs.size(), result.size());
        verify(productRepository).findByCode(productCode);
        verify(validationService).validateExists(eq(optionalProduct), anyString());
        verify(colorMapper).colorsToColorDTOs(any());
    }
    
    @Test
    void createProduct_ShouldCreateProduct_WhenValidDataProvided() {
        // Arrange
        Optional<Product> emptyOptional = Optional.empty();
        
        when(productRepository.findByCode(testProductDTO.getCode())).thenReturn(emptyOptional);
        doNothing().when(validationService).validateNotExists(eq(emptyOptional), anyString());
        when(productMapper.productDTOToProduct(testProductDTO)).thenReturn(testProduct);
        when(productRepository.save(testProduct)).thenReturn(testProduct);
        when(productMapper.productToProductDTO(testProduct)).thenReturn(testProductDTO);
        
        // Act
        ProductDTO result = productService.createProduct(testProductDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals(testProductDTO.getCode(), result.getCode());
        verify(productRepository).findByCode(testProductDTO.getCode());
        verify(validationService).validateNotExists(eq(emptyOptional), anyString());
        verify(productMapper).productDTOToProduct(testProductDTO);
        verify(productRepository).save(testProduct);
        verify(productMapper).productToProductDTO(testProduct);
    }
    
    @Test
    void createProduct_ShouldThrowException_WhenInvalidDataProvided() {
        // Arrange
        ProductDTO invalidDTO = new ProductDTO();
        invalidDTO.setCode("TEST001");
        // Missing required fields
        
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, 
            () -> productService.createProduct(invalidDTO));
        
        assertTrue(exception.getMessage().contains(ErrorMessages.ProductErrorMessages.invalidData()));
    }
    
    @Test
    void updateProduct_ShouldUpdateProduct_WhenValidDataProvided() {
        // Arrange
        String productCode = "TEST001";
        Optional<Product> optionalProduct = Optional.of(testProduct);
        
        when(productRepository.findByCode(productCode)).thenReturn(optionalProduct);
        when(validationService.validateExists(eq(optionalProduct), anyString())).thenReturn(testProduct);
        doNothing().when(productMapper).updateProductFromDTO(testProductDTO, testProduct);
        when(productRepository.save(testProduct)).thenReturn(testProduct);
        when(productMapper.productToProductDTO(testProduct)).thenReturn(testProductDTO);
        
        // Act
        ProductDTO result = productService.updateProduct(productCode, testProductDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals(testProductDTO.getCode(), result.getCode());
        verify(productRepository).findByCode(productCode);
        verify(validationService).validateExists(eq(optionalProduct), anyString());
        verify(productMapper).updateProductFromDTO(testProductDTO, testProduct);
        verify(productRepository).save(testProduct);
        verify(productMapper).productToProductDTO(testProduct);
    }
    
    @Test
    void updateProductLabels_ShouldUpdateLabels_WhenValidDataProvided() {
        // Arrange
        String productCode = "TEST001";
        Optional<Product> optionalProduct = Optional.of(testProduct);
        List<LabelDTO> labelDTOs = List.of(testLabelDTO);
        Optional<Label> optionalLabel = Optional.of(testLabel);
        
        when(productRepository.findByCode(productCode)).thenReturn(optionalProduct);
        when(validationService.validateExists(eq(optionalProduct), anyString())).thenReturn(testProduct);
        when(labelRepository.findById(testLabelDTO.getId())).thenReturn(optionalLabel);
        when(validationService.validateExists(eq(optionalLabel), anyString())).thenReturn(testLabel);
        when(productRepository.save(testProduct)).thenReturn(testProduct);
        when(labelMapper.labelsToLabelDTOs(any())).thenReturn(labelDTOs);
        
        // Act
        List<LabelDTO> result = productService.updateProductLabels(productCode, labelDTOs);
        
        // Assert
        assertNotNull(result);
        assertEquals(labelDTOs.size(), result.size());
        verify(productRepository).findByCode(productCode);
        verify(validationService, times(2)).validateExists(any(), anyString());
        verify(labelRepository).findById(testLabelDTO.getId());
        verify(productRepository).save(testProduct);
        verify(labelMapper).labelsToLabelDTOs(any());
    }
    
    @Test
    void updateProductVariants_ShouldUpdateVariants_WhenValidDataProvided() {
        // Arrange
        String productCode = "TEST001";
        Optional<Product> optionalProduct = Optional.of(testProduct);
        
        VariantOptionDTO optionDTO = new VariantOptionDTO();
        optionDTO.setId(1);
        optionDTO.setName("Test Option");
        testVariantDTO.setOptions(List.of(optionDTO));
        
        List<VariantDTO> variantDTOs = List.of(testVariantDTO);
        
        when(productRepository.findByCode(productCode)).thenReturn(optionalProduct);
        when(validationService.validateExists(eq(optionalProduct), anyString())).thenReturn(testProduct);
        when(variantRepository.save(any(Variant.class))).thenReturn(testVariant);
        when(productRepository.save(testProduct)).thenReturn(testProduct);
        when(variantMapper.variantsToVariantDTOs(any())).thenReturn(variantDTOs);
        
        // Act
        List<VariantDTO> result = productService.updateProductVariants(productCode, variantDTOs);
        
        // Assert
        assertNotNull(result);
        assertEquals(variantDTOs.size(), result.size());
        verify(productRepository).findByCode(productCode);
        verify(validationService).validateExists(eq(optionalProduct), anyString());
        verify(variantRepository).save(any(Variant.class));
        verify(productRepository).save(testProduct);
        verify(variantMapper).variantsToVariantDTOs(any());
    }
    
    @Test
    void updateProductColors_ShouldUpdateColors_WhenValidDataProvided() {
        // Arrange
        String productCode = "TEST001";
        Optional<Product> optionalProduct = Optional.of(testProduct);
        List<ColorDTO> colorDTOs = List.of(testColorDTO);
        Optional<Color> optionalColor = Optional.of(testColor);
        
        when(productRepository.findByCode(productCode)).thenReturn(optionalProduct);
        when(validationService.validateExists(eq(optionalProduct), anyString())).thenReturn(testProduct);
        when(colorRepository.findById(testColorDTO.getIdColor())).thenReturn(optionalColor);
        when(validationService.validateExists(eq(optionalColor), anyString())).thenReturn(testColor);
        when(productRepository.save(testProduct)).thenReturn(testProduct);
        when(colorMapper.colorsToColorDTOs(any())).thenReturn(colorDTOs);
        
        // Act
        List<ColorDTO> result = productService.updateProductColors(productCode, colorDTOs);
        
        // Assert
        assertNotNull(result);
        assertEquals(colorDTOs.size(), result.size());
        verify(productRepository).findByCode(productCode);
        verify(validationService, times(2)).validateExists(any(), anyString());
        verify(colorRepository).findById(testColorDTO.getIdColor());
        verify(productRepository).save(testProduct);
        verify(colorMapper).colorsToColorDTOs(any());
    }
    
    @Test
    void deleteProduct_ShouldMarkProductAsDeleted_WhenProductExists() {
        // Arrange
        String productCode = "TEST001";
        Optional<Product> optionalProduct = Optional.of(testProduct);
        
        when(productRepository.findByCode(productCode)).thenReturn(optionalProduct);
        when(validationService.validateExists(eq(optionalProduct), anyString())).thenReturn(testProduct);
        when(productRepository.save(testProduct)).thenReturn(testProduct);
        
        // Act
        productService.deleteProduct(productCode);
        
        // Assert
        assertTrue(testProduct.getDeleted());
        verify(productRepository).findByCode(productCode);
        verify(validationService).validateExists(eq(optionalProduct), anyString());
        verify(productRepository).save(testProduct);
    }
    
    @Test
    void deleteProduct_ShouldThrowException_WhenSaveFails() {
        // Arrange
        String productCode = "TEST001";
        Optional<Product> optionalProduct = Optional.of(testProduct);
        
        when(productRepository.findByCode(productCode)).thenReturn(optionalProduct);
        when(validationService.validateExists(eq(optionalProduct), anyString())).thenReturn(testProduct);
        when(productRepository.save(testProduct)).thenThrow(new RuntimeException("Database error"));
        
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, 
            () -> productService.deleteProduct(productCode));
        
        assertTrue(exception.getMessage().contains(ErrorMessages.ProductErrorMessages.failedToDelete(productCode)));
    }
}