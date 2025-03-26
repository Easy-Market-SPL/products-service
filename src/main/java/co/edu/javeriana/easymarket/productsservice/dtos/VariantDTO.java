package co.edu.javeriana.easymarket.productsservice.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariantDTO {
    private Integer id;
    private String name;
    private List<VariantOptionDTO> options;
}
