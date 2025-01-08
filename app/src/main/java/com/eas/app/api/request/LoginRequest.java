/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

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
