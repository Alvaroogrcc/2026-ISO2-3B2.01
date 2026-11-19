package es.sescam.cliente.controller;

import es.sescam.cliente.service.ApiClient;
import es.sescam.cliente.view.CitaView;

public class CitaController {

    private final ApiClient apiClient;
    private final CitaView view;

    public CitaController(ApiClient apiClient, CitaView view) {
        this.apiClient = apiClient;
        this.view = view;
    }

    public ApiClient getApiClient() { return apiClient; }
    public CitaView getView() { return view; }
}
