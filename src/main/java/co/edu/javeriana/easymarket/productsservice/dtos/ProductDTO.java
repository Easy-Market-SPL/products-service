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
public class ProductDTO {
    private String code;
    private String name;
    private String description;
    private Float price;
    private String imgUrl;
    private List<LabelDTO> labels;
}
