package co.com.nequi.franchising.model.exceptions;

public class FranchiseNotExistException extends RuntimeException {
    public FranchiseNotExistException(String message) {
        super(message);
    }
}
