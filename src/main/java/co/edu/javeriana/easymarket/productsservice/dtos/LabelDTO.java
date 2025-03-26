package co.edu.javeriana.easymarket.productsservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LabelDTO {
    private Integer id;
    private String name;
    private String description;
}
