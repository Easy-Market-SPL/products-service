package co.edu.javeriana.easymarket.productsservice.services;

import co.edu.javeriana.easymarket.productsservice.dtos.LabelDTO;
import co.edu.javeriana.easymarket.productsservice.dtos.ProductDTO;
import co.edu.javeriana.easymarket.productsservice.exception.ErrorMessages;
import co.edu.javeriana.easymarket.productsservice.exception.businessexceptions.BadRequestException;
import co.edu.javeriana.easymarket.productsservice.mappers.LabelMapper;
import co.edu.javeriana.easymarket.productsservice.mappers.ProductMapper;
import co.edu.javeriana.easymarket.productsservice.model.Label;
import co.edu.javeriana.easymarket.productsservice.repository.LabelRepository;
import co.edu.javeriana.easymarket.productsservice.services.util.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;
    private final ProductMapper productMapper;
    private final ValidationService validationService;
    
    public List<LabelDTO> getAllLabels() {
        List<Label> labels = labelRepository.findAll();
        return labels.stream()
                .map(labelMapper::labelToLabelDTO)
                .collect(Collectors.toList());
    }
    
    public LabelDTO getLabelById(Integer id) {
        Label label = validationService.validateExists(
                labelRepository.findById(id),
                ErrorMessages.LabelErrorMessages.notFound(id));
        
        return labelMapper.labelToLabelDTO(label);
    }
    
    public LabelDTO createLabel(LabelDTO labelDTO) {
        validateLabelData(labelDTO);
        
        labelDTO.setId(null);
        
        Label label = labelMapper.labelDTOToLabel(labelDTO);
        Label savedLabel = labelRepository.save(label);
        return labelMapper.labelToLabelDTO(savedLabel);
    }
    
    public LabelDTO updateLabel(Integer id, LabelDTO labelDTO) {
        validateLabelData(labelDTO);
        
        Label existingLabel = validationService.validateExists(
                labelRepository.findById(id),
                ErrorMessages.LabelErrorMessages.notFound(id));
        
        labelMapper.updateLabelFromDTO(labelDTO, existingLabel);
        
        Label updatedLabel = labelRepository.save(existingLabel);
        return labelMapper.labelToLabelDTO(updatedLabel);
    }
    
    public void deleteLabel(Integer id) {
        Label existingLabel = validationService.validateExists(
                labelRepository.findById(id),
                ErrorMessages.LabelErrorMessages.notFound(id));
        
        try {
            labelRepository.delete(existingLabel);
        } catch (Exception e) {
            throw new BadRequestException(ErrorMessages.LabelErrorMessages.failedToDelete(id));
        }
    }
    
    public List<ProductDTO> getProductCodesForLabel(Integer labelId) {
        Label label = validationService.validateExists(
                labelRepository.findById(labelId),
                ErrorMessages.LabelErrorMessages.notFound(labelId));
        
        return label.getProducts().stream()
                .map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
    }
    
    private void validateLabelData(LabelDTO label) {
        if (label == null || label.getName() == null || label.getName().trim().isEmpty()) {
            throw new BadRequestException(ErrorMessages.LabelErrorMessages.invalidData());
        }
    }
}