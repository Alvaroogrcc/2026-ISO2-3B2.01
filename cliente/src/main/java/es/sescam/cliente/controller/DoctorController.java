package es.sescam.cliente.controller;

import es.sescam.cliente.service.ApiClient;
import es.sescam.cliente.view.DoctorView;

public class DoctorController {

    private final ApiClient apiClient;
    private final DoctorView view;

    public DoctorController(ApiClient apiClient, DoctorView view) {
        this.apiClient = apiClient;
        this.view = view;
    }

    public ApiClient getApiClient() { return apiClient; }
    public DoctorView getView() { return view; }
}
