/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

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
