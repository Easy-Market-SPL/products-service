package co.edu.javeriana.easymarket.productsservice.controllers;

import co.edu.javeriana.easymarket.productsservice.dtos.LabelDTO;
import co.edu.javeriana.easymarket.productsservice.dtos.ProductDTO;
import co.edu.javeriana.easymarket.productsservice.services.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/labels")
@AllArgsConstructor
public class LabelController {
    private final LabelService labelService;
    
    @GetMapping
    public ResponseEntity<List<LabelDTO>> getAllLabels() {
        return ResponseEntity.ok(labelService.getAllLabels());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LabelDTO> getLabelById(@PathVariable Integer id) {
        return ResponseEntity.ok(labelService.getLabelById(id));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getProductsForLabel(@PathVariable Integer id) {
        return ResponseEntity.ok(labelService.getProductCodesForLabel(id));
    }
    
    @PostMapping
    public ResponseEntity<LabelDTO> createLabel(@RequestBody LabelDTO label) {
        LabelDTO createdLabel = labelService.createLabel(label);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLabel);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<LabelDTO> updateLabel(@PathVariable Integer id, @RequestBody LabelDTO label) {
        return ResponseEntity.ok(labelService.updateLabel(id, label));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Integer id) {
        labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }
    
}