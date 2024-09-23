package com.eas.app.model;

public class Sector {
    private Integer idSector;
    private String nombre;
    private String codigoComunidadCampesina;

    public Integer getIdSector() {
        return idSector;
    }

    public void setIdSector(Integer idSector) {
        this.idSector = idSector;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoComunidadCampesina() {
        return codigoComunidadCampesina;
    }

    public void setCodigoComunidadCampesina(String codigoComunidadCampesina) {
        this.codigoComunidadCampesina = codigoComunidadCampesina;
    }
}
