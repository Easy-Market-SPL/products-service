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
public class ColorServiceTest {

    @Mock
    private ColorRepository colorRepository;
    
    @Mock
    private ColorMapper colorMapper;
    
    @Mock
    private ValidationService validationService;
    
    @InjectMocks
    private ColorService colorService;
    
    private Color testColor;
    private ColorDTO testColorDTO;
    
    @BeforeEach
    void setUp() {
        // Setup test data
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
    void getAllColors_ShouldReturnAllColors() {
        // Arrange
        List<Color> colors = List.of(testColor);
        
        when(colorRepository.findAll()).thenReturn(colors);
        when(colorMapper.colorToColorDTO(testColor)).thenReturn(testColorDTO);
        
        // Act
        List<ColorDTO> result = colorService.getAllColors();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(testColorDTO.getIdColor(), result.get(0).getIdColor());
        assertEquals(testColorDTO.getName(), result.get(0).getName());
        verify(colorRepository).findAll();
        verify(colorMapper).colorToColorDTO(testColor);
    }
    
    @Test
    void getColorById_ShouldReturnColor_WhenColorExists() {
        // Arrange
        Integer colorId = 1;
        Optional<Color> optionalColor = Optional.of(testColor);
        
        when(colorRepository.findById(colorId)).thenReturn(optionalColor);
        when(validationService.validateExists(eq(optionalColor), anyString())).thenReturn(testColor);
        when(colorMapper.colorToColorDTO(testColor)).thenReturn(testColorDTO);
        
        // Act
        ColorDTO result = colorService.getColorById(colorId);
        
        // Assert
        assertNotNull(result);
        assertEquals(testColorDTO.getIdColor(), result.getIdColor());
        assertEquals(testColorDTO.getName(), result.getName());
        verify(colorRepository).findById(colorId);
        verify(validationService).validateExists(eq(optionalColor), anyString());
        verify(colorMapper).colorToColorDTO(testColor);
    }
    
    @Test
    void createColor_ShouldCreateColor_WhenValidDataProvided() {
        // Arrange
        when(colorMapper.colorDTOToColor(testColorDTO)).thenReturn(testColor);
        when(colorRepository.save(testColor)).thenReturn(testColor);
        when(colorMapper.colorToColorDTO(testColor)).thenReturn(testColorDTO);
        
        // Act
        ColorDTO result = colorService.createColor(testColorDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals(testColorDTO.getIdColor(), result.getIdColor());
        assertEquals(testColorDTO.getName(), result.getName());
        verify(colorMapper).colorDTOToColor(testColorDTO);
        verify(colorRepository).save(testColor);
        verify(colorMapper).colorToColorDTO(testColor);
    }
    
    @Test
    void createColor_ShouldThrowException_WhenInvalidDataProvided() {
        // Arrange
        ColorDTO invalidDTO = new ColorDTO();
        invalidDTO.setName("Invalid Color");
        // Missing hex code
        
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, 
            () -> colorService.createColor(invalidDTO));
        
        assertTrue(exception.getMessage().contains(ErrorMessages.ColorErrorMessages.invalidData()));
    }
    
    @Test
    void createColor_ShouldThrowException_WhenInvalidHexCodeProvided() {
        // Arrange
        ColorDTO invalidDTO = new ColorDTO();
        invalidDTO.setName("Invalid Color");
        invalidDTO.setHexaCode("XYZ"); // Invalid hex code
        
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, 
            () -> colorService.createColor(invalidDTO));
        
        assertTrue(exception.getMessage().contains(ErrorMessages.ColorErrorMessages.invalidHexCode()));
    }
    
    @Test
    void updateColor_ShouldUpdateColor_WhenValidDataProvided() {
        // Arrange
        Integer colorId = 1;
        Optional<Color> optionalColor = Optional.of(testColor);
        
        when(colorRepository.findById(colorId)).thenReturn(optionalColor);
        when(validationService.validateExists(eq(optionalColor), anyString())).thenReturn(testColor);
        doNothing().when(colorMapper).updateColorFromDTO(testColorDTO, testColor);
        when(colorRepository.save(testColor)).thenReturn(testColor);
        when(colorMapper.colorToColorDTO(testColor)).thenReturn(testColorDTO);
        
        // Act
        ColorDTO result = colorService.updateColor(colorId, testColorDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals(testColorDTO.getIdColor(), result.getIdColor());
        assertEquals(testColorDTO.getName(), result.getName());
        verify(colorRepository).findById(colorId);
        verify(validationService).validateExists(eq(optionalColor), anyString());
        verify(colorMapper).updateColorFromDTO(testColorDTO, testColor);
        verify(colorRepository).save(testColor);
        verify(colorMapper).colorToColorDTO(testColor);
    }
    
    @Test
    void deleteColor_ShouldDeleteColor_WhenColorExists() {
        // Arrange
        Integer colorId = 1;
        Optional<Color> optionalColor = Optional.of(testColor);
        
        when(colorRepository.findById(colorId)).thenReturn(optionalColor);
        when(validationService.validateExists(eq(optionalColor), anyString())).thenReturn(testColor);
        doNothing().when(colorRepository).delete(testColor);
        
        // Act
        colorService.deleteColor(colorId);
        
        // Assert
        verify(colorRepository).findById(colorId);
        verify(validationService).validateExists(eq(optionalColor), anyString());
        verify(colorRepository).delete(testColor);
    }
    
    @Test
    void deleteColor_ShouldThrowException_WhenDeleteFails() {
        // Arrange
        Integer colorId = 1;
        Optional<Color> optionalColor = Optional.of(testColor);
        
        when(colorRepository.findById(colorId)).thenReturn(optionalColor);
        when(validationService.validateExists(eq(optionalColor), anyString())).thenReturn(testColor);
        doThrow(new RuntimeException("Database error")).when(colorRepository).delete(testColor);
        
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, 
            () -> colorService.deleteColor(colorId));
        
        assertTrue(exception.getMessage().contains(ErrorMessages.ColorErrorMessages.failedToDelete(colorId)));
    }
}