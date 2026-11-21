package es.sescam.cliente.service.error;

import java.util.Objects;

/**
 * Excepción base para errores al consumir las APIs por contrato.
 */
public class ApiException extends RuntimeException {

    private final ApiError error;

    public ApiException(ApiError error) {
        super(Objects.requireNonNull(error, "error").getMessage());
        this.error = error;
    }

    public ApiException(ApiError error, Throwable cause) {
        super(Objects.requireNonNull(error, "error").getMessage(), cause);
        this.error = error;
    }

    public ApiError getError() {
        return error;
    }
}
