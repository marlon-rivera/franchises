package co.com.nequi.franchising.model.exceptions;

public class BranchCreationException extends BusinessException {
    public BranchCreationException(String message) {
        super(message, 400);
    }
}
