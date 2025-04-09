package co.edu.javeriana.easymarket.productsservice.services;

import co.edu.javeriana.easymarket.productsservice.dtos.ColorDTO;
import co.edu.javeriana.easymarket.productsservice.exception.ErrorMessages;
import co.edu.javeriana.easymarket.productsservice.exception.businessexceptions.BadRequestException;
import co.edu.javeriana.easymarket.productsservice.mappers.ColorMapper;
import co.edu.javeriana.easymarket.productsservice.model.Color;
import co.edu.javeriana.easymarket.productsservice.repository.ColorRepository;
import co.edu.javeriana.easymarket.productsservice.services.util.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ColorService {
    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;
    private final ValidationService validationService;
    
    public List<ColorDTO> getAllColors() {
        List<Color> colors = colorRepository.findAll();
        return colors.stream()
                .map(colorMapper::colorToColorDTO)
                .toList();
    }
    
    public ColorDTO getColorById(Integer id) {
        Color color = validationService.validateExists(
                colorRepository.findById(id),
                ErrorMessages.ColorErrorMessages.notFound(id));
        
        return colorMapper.colorToColorDTO(color);
    }
    
    public ColorDTO createColor(ColorDTO colorDTO) {
        validateColorData(colorDTO);
        
        Color color = colorMapper.colorDTOToColor(colorDTO);
        Color savedColor = colorRepository.save(color);
        return colorMapper.colorToColorDTO(savedColor);
    }
    
    public ColorDTO updateColor(Integer id, ColorDTO colorDTO) {
        validateColorData(colorDTO);
        
        Color existingColor = validationService.validateExists(
                colorRepository.findById(id),
                ErrorMessages.ColorErrorMessages.notFound(id));
        
        colorMapper.updateColorFromDTO(colorDTO, existingColor);
        
        Color updatedColor = colorRepository.save(existingColor);
        return colorMapper.colorToColorDTO(updatedColor);
    }
    
    public void deleteColor(Integer id) {
        Color existingColor = validationService.validateExists(
                colorRepository.findById(id),
                ErrorMessages.ColorErrorMessages.notFound(id));
        
        try {
            colorRepository.delete(existingColor);
        } catch (Exception e) {
            throw new BadRequestException(ErrorMessages.ColorErrorMessages.failedToDelete(id));
        }
    }
    
    private void validateColorData(ColorDTO color) {
        if (color == null || color.getHexaCode() == null || color.getHexaCode().trim().isEmpty()) {
            throw new BadRequestException(ErrorMessages.ColorErrorMessages.invalidData());
        }
        
        // Validate hex code format
        if (!color.getHexaCode().matches("^[0-9A-Fa-f]{6}$")) {
            throw new BadRequestException(ErrorMessages.ColorErrorMessages.invalidHexCode());
        }
    }
}