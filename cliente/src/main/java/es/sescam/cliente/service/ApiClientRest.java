package es.sescam.cliente.service;

import java.util.Objects;

public class ApiClientRest implements ApiClient {

    private final String baseUrl;

    public ApiClientRest(String baseUrl) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }
}
