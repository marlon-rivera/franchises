package co.com.nequi.franchising.model.exceptions;

public class ProductCreationException extends BusinessException {
    public ProductCreationException(String message) {
        super(message, 400);
    }
}
