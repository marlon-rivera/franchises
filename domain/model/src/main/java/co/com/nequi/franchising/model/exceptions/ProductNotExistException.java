package co.com.nequi.franchising.model.exceptions;

public class ProductNotExistException extends BusinessException {
    public ProductNotExistException(String message) {
        super(message, 404);
    }
}
