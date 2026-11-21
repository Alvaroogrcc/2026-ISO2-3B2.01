package es.sescam.cliente.service.error;

import java.time.Instant;
import java.util.Objects;

/**
 * Contrato básico para representar errores devueltos o detectados por las APIs cliente.
 */
public class ApiError {

    private final String code;
    private final String message;
    private final int status;
    private final Instant timestamp;

    public ApiError(String code, String message, int status) {
        this(code, message, status, Instant.now());
    }

    public ApiError(String code, String message, int status, Instant timestamp) {
        this.code = Objects.requireNonNull(code, "code");
        this.message = Objects.requireNonNull(message, "message");
        this.status = status;
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp");
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", timestamp=" + timestamp +
                '}';
    }
}
