package co.edu.javeriana.easymarket.productsservice.exception.businessexceptions;

import co.edu.javeriana.easymarket.productsservice.exception.BusinessException;

public class BadRequestException extends BusinessException {
    public BadRequestException(String message) {
        super(message);
    }
}
