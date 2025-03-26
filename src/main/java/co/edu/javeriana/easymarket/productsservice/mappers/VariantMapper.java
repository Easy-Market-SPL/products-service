package co.edu.javeriana.easymarket.productsservice.mappers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import co.edu.javeriana.easymarket.productsservice.dtos.VariantDTO;
import co.edu.javeriana.easymarket.productsservice.dtos.VariantOptionDTO;
import co.edu.javeriana.easymarket.productsservice.model.Variant;
import co.edu.javeriana.easymarket.productsservice.model.VariantOption;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class VariantMapper {
    private final ModelMapper modelMapper;

    public VariantDTO variantToVariantDTO(Variant variant) {
        return modelMapper.map(variant, VariantDTO.class);
    }

    public Variant variantDTOToVariant(VariantDTO variantDTO) {
        Variant variant = new Variant();
        variant.setName(variantDTO.getName());
        
        // Handle options
        if (variantDTO.getOptions() != null) {
            for (VariantOptionDTO optionDTO : variantDTO.getOptions()) {
                VariantOption option = new VariantOption();
                option.setName(optionDTO.getName());
                option.setImgUrl(optionDTO.getImgUrl());
                variant.addOption(option);  // Use the helper method
            }
        }

        return variant;
    }

    public List<VariantDTO> variantsToVariantDTOs(Collection<Variant> variants) {
        return variants.stream()
                .map(variant -> variantToVariantDTO(variant))
                .collect(Collectors.toList());
    }
}
