package co.edu.javeriana.easymarket.productsservice.exception.businessexceptions;

import co.edu.javeriana.easymarket.productsservice.exception.BusinessException;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
