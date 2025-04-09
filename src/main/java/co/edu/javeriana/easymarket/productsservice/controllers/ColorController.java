package co.edu.javeriana.easymarket.productsservice.controllers;

import co.edu.javeriana.easymarket.productsservice.dtos.ColorDTO;
import co.edu.javeriana.easymarket.productsservice.services.ColorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/colors")
@AllArgsConstructor
public class ColorController {
    private final ColorService colorService;
    
    @GetMapping
    public ResponseEntity<List<ColorDTO>> getAllColors() {
        return ResponseEntity.ok(colorService.getAllColors());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ColorDTO> getColorById(@PathVariable Integer id) {
        return ResponseEntity.ok(colorService.getColorById(id));
    }
    
    @PostMapping
    public ResponseEntity<ColorDTO> createColor(@RequestBody ColorDTO color) {
        ColorDTO createdColor = colorService.createColor(color);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdColor);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ColorDTO> updateColor(@PathVariable Integer id, @RequestBody ColorDTO color) {
        return ResponseEntity.ok(colorService.updateColor(id, color));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColor(@PathVariable Integer id) {
        colorService.deleteColor(id);
        return ResponseEntity.noContent().build();
    }
}
