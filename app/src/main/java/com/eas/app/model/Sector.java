/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

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
