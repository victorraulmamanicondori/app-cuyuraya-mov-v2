package com.eas.app.api.request;

public class AsignarMedidorRequest {
    private String codigoMedidor;
    private String dni;

    public AsignarMedidorRequest() {}

    public AsignarMedidorRequest(String codigoMedidor, String dni) {
        this.codigoMedidor = codigoMedidor;
        this.dni = dni;
    }

    public String getCodigoMedidor() {
        return codigoMedidor;
    }

    public void setCodigoMedidor(String codigoMedidor) {
        this.codigoMedidor = codigoMedidor;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
