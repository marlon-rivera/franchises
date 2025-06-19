package co.com.nequi.franchising.api.exception;

public record ExceptionResponse(
        String timestamp,
        int status,
        String message,
        String path
) {}