/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

package com.eas.app.api.request;

public class UbigeoRequest {
    private String codigoDistrito;
    private String codigoCentroPoblado;
    private String codigoComunidadCampesina;
    private String codigoComunidadNativa;

    public String getCodigoDistrito() {
        return codigoDistrito;
    }

    public void setCodigoDistrito(String codigoDistrito) {
        this.codigoDistrito = codigoDistrito;
    }

    public String getCodigoCentroPoblado() {
        return codigoCentroPoblado;
    }

    public void setCodigoCentroPoblado(String codigoCentroPoblado) {
        this.codigoCentroPoblado = codigoCentroPoblado;
    }

    public String getCodigoComunidadCampesina() {
        return codigoComunidadCampesina;
    }

    public void setCodigoComunidadCampesina(String codigoComunidadCampesina) {
        this.codigoComunidadCampesina = codigoComunidadCampesina;
    }

    public String getCodigoComunidadNativa() {
        return codigoComunidadNativa;
    }

    public void setCodigoComunidadNativa(String codigoComunidadNativa) {
        this.codigoComunidadNativa = codigoComunidadNativa;
    }
}
