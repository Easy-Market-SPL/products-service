package co.edu.javeriana.easymarket.productsservice.mappers;

import co.edu.javeriana.easymarket.productsservice.dtos.ColorDTO;
import co.edu.javeriana.easymarket.productsservice.model.Color;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class ColorMapper {
    private final ModelMapper modelMapper;

    public ColorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    
    public ColorDTO colorToColorDTO(Color color) {
        return modelMapper.map(color, ColorDTO.class);
    }
    
    public List<ColorDTO> colorsToColorDTOs(Set<Color> colors) {
        if (colors == null) {
            return null;
        }
        
        return colors.stream()
                .map(this::colorToColorDTO)
                .toList();
    }
    
    public Color colorDTOToColor(ColorDTO dto) {
        return modelMapper.map(dto, Color.class);
    }
    
    public void updateColorFromDTO(ColorDTO dto, Color color) {
        if (dto == null || color == null) {
            return;
        }
        
        // Don't update ID
        color.setHexaCode(dto.getHexaCode());
        color.setImgUrl(dto.getImgUrl());
        color.setName(dto.getName());
    }
}