package co.com.nequi.franchising.model.exceptions;

public abstract class BusinessException extends RuntimeException {

    private final int httpStatus;

    protected BusinessException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
