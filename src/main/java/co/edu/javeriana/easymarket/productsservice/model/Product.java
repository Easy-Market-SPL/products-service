package co.edu.javeriana.easymarket.productsservice.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import co.edu.javeriana.easymarket.productsservice.exception.ErrorMessages;
import co.edu.javeriana.easymarket.productsservice.exception.businessexceptions.BadRequestException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "code", nullable = false, length = 200)
    private String code;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Column(name = "img_url", length = 700)
    private String imgUrl;

    @OneToMany(mappedBy = "productCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LabelProduct> labelProducts = new HashSet<>();

    @OneToMany(mappedBy = "productCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Variant> variants = new HashSet<>();

    @OneToMany(mappedBy = "productCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductColor> productColors = new HashSet<>();
    
    // Helper method to get all Labels
    public Set<Label> getLabels() {
        return labelProducts.stream()
            .map(LabelProduct::getLabelIdLabel)
            .collect(Collectors.toSet());
    }

    public Set<Variant> getVariants() {
        return variants;
    }

    public Set<Color> getColors() {
        return productColors.stream()
            .map(ProductColor::getIdColor)
            .collect(Collectors.toSet());
    }

    public void updateLabels(Set<Label> newLabels) {
        // Clear existing relationships
        labelProducts.clear();

        // Add new relationships
        if (newLabels != null) {
            for (Label label : newLabels) {
                addLabel(label);
            }
        }
    }

    public void updateVariants(Set<Variant> newVariants) {
        // Clear existing relationships
        variants.clear();

        // Add new relationships
        if (newVariants != null) {
            for (Variant variant : newVariants) {
                if (variant.getOptions() != null && !variant.getOptions().isEmpty()) {
                    addVariant(variant);
                }
                else {
                    throw new BadRequestException(ErrorMessages.VariantsErrorMesages.emptyOptions());
                }
            }
        }
    }

    public void addLabel(Label label) {
        LabelProduct labelProduct = new LabelProduct();

        // Create composite id
        LabelProductId id = new LabelProductId();
        id.setLabelIdLabel(label.getId());
        id.setProductCode(this.code);

        // Set up relationship
        labelProduct.setId(id);
        labelProduct.setLabelIdLabel(label);
        labelProduct.setProductCode(this);

        this.labelProducts.add(labelProduct);
    }

    public void addVariant(Variant variant) {
        variant.setProductCode(this);
        variants.add(variant);
    }

    public void addColor(Color color) {
        ProductColor productColor = new ProductColor();
        
        // Create composite id
        ProductColorId id = new ProductColorId();
        id.setIdColor(color.getIdColor());
        id.setProductCode(this.code);
        
        // Set up relationship
        productColor.setId(id);
        productColor.setIdColor(color);
        productColor.setProductCode(this);
        
        this.productColors.add(productColor);
    }
    
    // Helper method to update all colors
    public void updateColors(Set<Color> newColors) {
        // Clear existing relationships
        productColors.clear();
        
        // Add new relationships
        if (newColors != null) {
            for (Color color : newColors) {
                addColor(color);
            }
        }
    }
}