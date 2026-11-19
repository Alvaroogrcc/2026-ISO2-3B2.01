package es.sescam.cliente.controller;

import es.sescam.cliente.service.ApiClient;
import es.sescam.cliente.view.PacienteView;

public class PacienteController {

    private final ApiClient apiClient;
    private final PacienteView view;

    public PacienteController(ApiClient apiClient, PacienteView view) {
        this.apiClient = apiClient;
        this.view = view;
    }

    public ApiClient getApiClient() { return apiClient; }
    public PacienteView getView() { return view; }
}
