package com.eas.app.api.response;

public class LecturaActualResponse {
    private Integer idLectura;
    private Integer idMedidor;
    private Double lecturaActual;
    private Double lecturaAnterior;
    private Double m3Consumido;
    private Integer idTarifa;
    private Double montoPagar;
    private Double montoMulta;
    private String numeroRecibo;
    private String fechaLimitePago;
    private String fechaCorte;
    private String fechaLectura;
    private String comentario;
    private String estado;
    private String fecCreacion;
    private String fecActualizacion;

    public Integer getIdLectura() {
        return idLectura;
    }

    public void setIdLectura(Integer idLectura) {
        this.idLectura = idLectura;
    }

    public Integer getIdMedidor() {
        return idMedidor;
    }

    public void setIdMedidor(Integer idMedidor) {
        this.idMedidor = idMedidor;
    }

    public Double getLecturaActual() {
        return lecturaActual;
    }

    public void setLecturaActual(Double lecturaActual) {
        this.lecturaActual = lecturaActual;
    }

    public Double getLecturaAnterior() {
        return lecturaAnterior;
    }

    public void setLecturaAnterior(Double lecturaAnterior) {
        this.lecturaAnterior = lecturaAnterior;
    }

    public Double getM3Consumido() {
        return m3Consumido;
    }

    public void setM3Consumido(Double m3Consumido) {
        this.m3Consumido = m3Consumido;
    }

    public Integer getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Integer idTarifa) {
        this.idTarifa = idTarifa;
    }

    public Double getMontoPagar() {
        return montoPagar;
    }

    public void setMontoPagar(Double montoPagar) {
        this.montoPagar = montoPagar;
    }

    public Double getMontoMulta() {
        return montoMulta;
    }

    public void setMontoMulta(Double montoMulta) {
        this.montoMulta = montoMulta;
    }

    public String getNumeroRecibo() {
        return numeroRecibo;
    }

    public void setNumeroRecibo(String numeroRecibo) {
        this.numeroRecibo = numeroRecibo;
    }

    public String getFechaLimitePago() {
        return fechaLimitePago;
    }

    public void setFechaLimitePago(String fechaLimitePago) {
        this.fechaLimitePago = fechaLimitePago;
    }

    public String getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(String fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    public String getFechaLectura() {
        return fechaLectura;
    }

    public void setFechaLectura(String fechaLectura) {
        this.fechaLectura = fechaLectura;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(String fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public String getFecActualizacion() {
        return fecActualizacion;
    }

    public void setFecActualizacion(String fecActualizacion) {
        this.fecActualizacion = fecActualizacion;
    }
}
