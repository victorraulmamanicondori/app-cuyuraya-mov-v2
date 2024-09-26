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
