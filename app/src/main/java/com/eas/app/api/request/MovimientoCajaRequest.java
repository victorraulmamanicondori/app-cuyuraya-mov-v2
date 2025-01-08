/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

package com.eas.app.api.request;

public class MovimientoCajaRequest {
    private String idTipoMovimiento;
    private String monto;
    private String descripcion;

    public MovimientoCajaRequest() {}

    public MovimientoCajaRequest(String idTipoMovimiento, String monto, String descripcion) {
        this.idTipoMovimiento = idTipoMovimiento;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    public String getIdTipoMovimiento() {
        return idTipoMovimiento;
    }

    public void setIdTipoMovimiento(String idTipoMovimiento) {
        this.idTipoMovimiento = idTipoMovimiento;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
