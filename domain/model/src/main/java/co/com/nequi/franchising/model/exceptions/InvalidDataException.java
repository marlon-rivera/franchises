package co.com.nequi.franchising.model.exceptions;

public class InvalidDataException extends BusinessException {
    public InvalidDataException(String message) {
        super(message, 400);
    }
}
