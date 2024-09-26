package com.eas.app.api.request;

public class LecturaActualRequest {
    private String codigoMedidor;
    private String lecturaActual;

    public LecturaActualRequest() {}

    public LecturaActualRequest(String codigoMedidor, String lecturaActual) {
        this.codigoMedidor = codigoMedidor;
        this.lecturaActual = lecturaActual;
    }

    public String getCodigoMedidor() {
        return codigoMedidor;
    }

    public void setCodigoMedidor(String codigoMedidor) {
        this.codigoMedidor = codigoMedidor;
    }

    public String getLecturaActual() {
        return lecturaActual;
    }

    public void setLecturaActual(String lecturaActual) {
        this.lecturaActual = lecturaActual;
    }
}
