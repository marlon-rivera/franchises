package co.com.nequi.franchising.model.exceptions;

public class FranchiseCreationException extends BusinessException {

    public FranchiseCreationException(String message) {
        super(message, 400);
    }

}
