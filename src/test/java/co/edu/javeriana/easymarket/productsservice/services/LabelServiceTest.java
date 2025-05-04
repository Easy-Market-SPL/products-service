package co.edu.javeriana.easymarket.productsservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
public class LabelServiceTest {

    @Mock
    private LabelRepository labelRepository;
    
    @Mock
    private LabelMapper labelMapper;
    
    @Mock
    private ProductMapper productMapper;
    
    @Mock
    private ValidationService validationService;
    
    @InjectMocks
    private LabelService labelService;
    
    private Label testLabel;
    private LabelDTO testLabelDTO;
    private Product testProduct;
    private ProductDTO testProductDTO;
    private Set<Product> testProducts;
    
    @BeforeEach
    void setUp() {
        // Setup test data
        testLabel = new Label();
        testLabel.setId(1);
        testLabel.setName("Test Label");
        testLabel.setDescription("Test Description");
        
        testLabelDTO = new LabelDTO();
        testLabelDTO.setId(1);
        testLabelDTO.setName("Test Label");
        testLabelDTO.setDescription("Test Description");
        
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
        
        testProducts = new HashSet<>();
        testProducts.add(testProduct);
    }
    
    @Test
    void getAllLabels_ShouldReturnAllLabels() {
        // Arrange
        List<Label> labels = List.of(testLabel);
        
        when(labelRepository.findAll()).thenReturn(labels);
        when(labelMapper.labelToLabelDTO(testLabel)).thenReturn(testLabelDTO);
        
        // Act
        List<LabelDTO> result = labelService.getAllLabels();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(testLabelDTO.getId(), result.get(0).getId());
        assertEquals(testLabelDTO.getName(), result.get(0).getName());
        verify(labelRepository).findAll();
        verify(labelMapper).labelToLabelDTO(testLabel);
    }
    
    @Test
    void getLabelById_ShouldReturnLabel_WhenLabelExists() {
        // Arrange
        Integer labelId = 1;
        Optional<Label> optionalLabel = Optional.of(testLabel);
        
        when(labelRepository.findById(labelId)).thenReturn(optionalLabel);
        when(validationService.validateExists(eq(optionalLabel), anyString())).thenReturn(testLabel);
        when(labelMapper.labelToLabelDTO(testLabel)).thenReturn(testLabelDTO);
        
        // Act
        LabelDTO result = labelService.getLabelById(labelId);
        
        // Assert
        assertNotNull(result);
        assertEquals(testLabelDTO.getId(), result.getId());
        assertEquals(testLabelDTO.getName(), result.getName());
        verify(labelRepository).findById(labelId);
        verify(validationService).validateExists(eq(optionalLabel), anyString());
        verify(labelMapper).labelToLabelDTO(testLabel);
    }
    
    @Test
    void createLabel_ShouldCreateLabel_WhenValidDataProvided() {
        // Arrange
        when(labelMapper.labelDTOToLabel(testLabelDTO)).thenReturn(testLabel);
        when(labelRepository.save(testLabel)).thenReturn(testLabel);
        when(labelMapper.labelToLabelDTO(testLabel)).thenReturn(testLabelDTO);
        
        // Act
        LabelDTO result = labelService.createLabel(testLabelDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals(testLabelDTO.getId(), result.getId());
        assertEquals(testLabelDTO.getName(), result.getName());
        verify(labelMapper).labelDTOToLabel(testLabelDTO);
        verify(labelRepository).save(testLabel);
        verify(labelMapper).labelToLabelDTO(testLabel);
    }
    
    @Test
    void createLabel_ShouldThrowException_WhenInvalidDataProvided() {
        // Arrange
        LabelDTO invalidDTO = new LabelDTO();
        invalidDTO.setDescription("Test Description");
        // Missing name
        
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, 
            () -> labelService.createLabel(invalidDTO));
        
        assertTrue(exception.getMessage().contains(ErrorMessages.LabelErrorMessages.invalidData()));
    }
    
    @Test
    void updateLabel_ShouldUpdateLabel_WhenValidDataProvided() {
        // Arrange
        Integer labelId = 1;
        Optional<Label> optionalLabel = Optional.of(testLabel);
        
        when(labelRepository.findById(labelId)).thenReturn(optionalLabel);
        when(validationService.validateExists(eq(optionalLabel), anyString())).thenReturn(testLabel);
        doNothing().when(labelMapper).updateLabelFromDTO(testLabelDTO, testLabel);
        when(labelRepository.save(testLabel)).thenReturn(testLabel);
        when(labelMapper.labelToLabelDTO(testLabel)).thenReturn(testLabelDTO);
        
        // Act
        LabelDTO result = labelService.updateLabel(labelId, testLabelDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals(testLabelDTO.getId(), result.getId());
        assertEquals(testLabelDTO.getName(), result.getName());
        verify(labelRepository).findById(labelId);
        verify(validationService).validateExists(eq(optionalLabel), anyString());
        verify(labelMapper).updateLabelFromDTO(testLabelDTO, testLabel);
        verify(labelRepository).save(testLabel);
        verify(labelMapper).labelToLabelDTO(testLabel);
    }
    
    @Test
    void deleteLabel_ShouldDeleteLabel_WhenLabelExists() {
        // Arrange
        Integer labelId = 1;
        Optional<Label> optionalLabel = Optional.of(testLabel);
        
        when(labelRepository.findById(labelId)).thenReturn(optionalLabel);
        when(validationService.validateExists(eq(optionalLabel), anyString())).thenReturn(testLabel);
        doNothing().when(labelRepository).delete(testLabel);
        
        // Act
        labelService.deleteLabel(labelId);
        
        // Assert
        verify(labelRepository).findById(labelId);
        verify(validationService).validateExists(eq(optionalLabel), anyString());
        verify(labelRepository).delete(testLabel);
    }
    
    @Test
    void deleteLabel_ShouldThrowException_WhenDeleteFails() {
        // Arrange
        Integer labelId = 1;
        Optional<Label> optionalLabel = Optional.of(testLabel);
        
        when(labelRepository.findById(labelId)).thenReturn(optionalLabel);
        when(validationService.validateExists(eq(optionalLabel), anyString())).thenReturn(testLabel);
        doThrow(new RuntimeException("Database error")).when(labelRepository).delete(testLabel);
        
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, 
            () -> labelService.deleteLabel(labelId));
        
        assertTrue(exception.getMessage().contains(ErrorMessages.LabelErrorMessages.failedToDelete(labelId)));
    }
    
    @Test
    void getProductCodesForLabel_ShouldReturnProducts_WhenLabelExists() {
        // Arrange
        Integer labelId = 1;

        // Create a mock just for this test
        Label mockLabel = mock(Label.class);
        Optional<Label> optionalLabel = Optional.of(mockLabel);
        
        // Mock the relationship between label and products
        when(labelRepository.findById(labelId)).thenReturn(optionalLabel);
        when(validationService.validateExists(eq(optionalLabel), anyString())).thenReturn(mockLabel);
        when(mockLabel.getProducts()).thenReturn(testProducts);
        when(productMapper.productToProductDTO(testProduct)).thenReturn(testProductDTO);
        
        // Act
        List<ProductDTO> result = labelService.getProductCodesForLabel(labelId);
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(testProductDTO.getCode(), result.get(0).getCode());
        assertEquals(testProductDTO.getName(), result.get(0).getName());
        verify(labelRepository).findById(labelId);
        verify(validationService).validateExists(eq(optionalLabel), anyString());
        verify(productMapper).productToProductDTO(testProduct);
    }
}