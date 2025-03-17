package co.edu.javeriana.easymarket.productsservice.exception.businessexceptions;

import co.edu.javeriana.easymarket.productsservice.exception.BusinessException;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
