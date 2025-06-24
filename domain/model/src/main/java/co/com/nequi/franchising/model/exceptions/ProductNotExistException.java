package co.com.nequi.franchising.model.exceptions;

public class ProductNotExistException extends RuntimeException {
    public ProductNotExistException(String message) {
        super(message);
    }
}
