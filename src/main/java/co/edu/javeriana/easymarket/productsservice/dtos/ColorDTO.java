package co.edu.javeriana.easymarket.productsservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColorDTO {
    private Integer idColor;
    private String hexaCode;
    private String imgUrl;
    private String name;
}