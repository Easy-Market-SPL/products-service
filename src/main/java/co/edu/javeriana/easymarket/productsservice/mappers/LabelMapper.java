package co.edu.javeriana.easymarket.productsservice.mappers;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import co.edu.javeriana.easymarket.productsservice.dtos.LabelDTO;
import co.edu.javeriana.easymarket.productsservice.model.Label;

@Component
public class LabelMapper {
    private final ModelMapper modelMapper;

    public LabelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public LabelDTO labelToLabelDTO(Label label) {
        return modelMapper.map(label, LabelDTO.class);
    }

    public List<LabelDTO> labelsToLabelDTOs(Collection<Label> labels) {
        return labels.stream()
                .map(label -> labelToLabelDTO(label))
                .collect(Collectors.toList());
    }

    public Label labelDTOToLabel(LabelDTO labelDTO) {
        return modelMapper.map(labelDTO, Label.class);
    }

    public Set<Label> labelDTOsToLabels(Collection<LabelDTO> labelDTOs) {
        return labelDTOs.stream()
                .map(labelDTO -> labelDTOToLabel(labelDTO))
                .collect(Collectors.toSet());
    }

    public void updateLabelFromDTO(LabelDTO labelDTO, Label existingLabel) {
        // Update only specific fields manually
        existingLabel.setName(labelDTO.getName());
        existingLabel.setDescription(labelDTO.getDescription());
    }

}
