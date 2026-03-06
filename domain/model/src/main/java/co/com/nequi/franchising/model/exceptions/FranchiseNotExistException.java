package co.com.nequi.franchising.model.exceptions;

public class FranchiseNotExistException extends BusinessException {
    public FranchiseNotExistException(String message) {
        super(message, 404);
    }
}
