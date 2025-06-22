package co.com.nequi.franchising.model.exceptions;

public class BranchNotExistException extends RuntimeException {
    public BranchNotExistException(String message) {
        super(message);
    }
}
