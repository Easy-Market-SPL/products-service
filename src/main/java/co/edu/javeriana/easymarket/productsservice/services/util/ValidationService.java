package co.edu.javeriana.easymarket.productsservice.services.util;

import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.javeriana.easymarket.productsservice.exception.businessexceptions.BadRequestException;
import co.edu.javeriana.easymarket.productsservice.exception.businessexceptions.ResourceNotFoundException;

@Service
public class ValidationService {
    public <T> void validateNotExists(Optional<T> entity, String errorMessage) {
        if (entity.isPresent()) {
            throw new BadRequestException(errorMessage);
        }
    }
    
    public <T> T validateExists(Optional<T> entity, String errorMessage) {
        return entity.orElseThrow(() -> new ResourceNotFoundException(errorMessage));
    }
}
