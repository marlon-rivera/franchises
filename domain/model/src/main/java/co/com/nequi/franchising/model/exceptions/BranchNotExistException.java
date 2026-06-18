package co.com.nequi.franchising.model.exceptions;

public class BranchNotExistException extends BusinessException {
    public BranchNotExistException(String message) {
        super(message, 404);
    }
}
