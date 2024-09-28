package com.eas.app.api.request;

public class ResetearContrasenaRequest {
    private String dni;

    public ResetearContrasenaRequest(String dni) {
        this.dni = dni;
    }

    public String getDni() {
        return dni;
    }
}
