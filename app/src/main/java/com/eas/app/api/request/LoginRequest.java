package com.eas.app.api.request;

public class LoginRequest {
    private String dni;
    private String clave;

    // Constructor
    public LoginRequest(String dni, String clave) {
        this.dni = dni;
        this.clave = clave;
    }

    // Getters
    public String getDni() {
        return dni;
    }

    public String getClave() {
        return clave;
    }

    // Setters
    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
