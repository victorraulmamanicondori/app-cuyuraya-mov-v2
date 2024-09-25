package com.eas.app.model;

import java.util.Objects;

public class Usuario {
    private Integer id;
    private String dni;
    private String nombres;
    private String paterno;
    private String materno;
    private String telefono;
    private String direccion;
    private String numeroContrato;
    private String codigoDepartamento;
    private String codigoProvincia;
    private String codigoDistrito;
    private String codigoCentroPoblado;
    private String codigoComunidadCampesina;
    private String codigoComunidadNativa;
    private String clave;
    private String estado;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public String getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(String codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public String getCodigoProvincia() {
        return codigoProvincia;
    }

    public void setCodigoProvincia(String codigoProvincia) {
        this.codigoProvincia = codigoProvincia;
    }

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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(dni, usuario.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dni);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", nombres='" + nombres + '\'' +
                ", paterno='" + paterno + '\'' +
                ", materno='" + materno + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", numeroContrato='" + numeroContrato + '\'' +
                ", codigoDepartamento='" + codigoDepartamento + '\'' +
                ", codigoProvincia='" + codigoProvincia + '\'' +
                ", codigoDistrito='" + codigoDistrito + '\'' +
                ", codigoCentroPoblado='" + codigoCentroPoblado + '\'' +
                ", codigoComunidadCampesina='" + codigoComunidadCampesina + '\'' +
                ", codigoComunidadNativa='" + codigoComunidadNativa + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
