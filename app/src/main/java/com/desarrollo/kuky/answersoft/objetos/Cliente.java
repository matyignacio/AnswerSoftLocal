package com.desarrollo.kuky.answersoft.objetos;

/**
 * Created by kuky on 15/10/16.
 */
public class Cliente {
    private int idCliente;
    private String razonSocial;
    private String domicilio;
    private String telefono;
    private String tipoResponsabilidad;
    private String numeroCuit;

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoResponsabilidad() {
        return tipoResponsabilidad;
    }

    public void setTipoResponsabilidad(String tipoResponsabilidad) {
        this.tipoResponsabilidad = tipoResponsabilidad;
    }

    public String getNumeroCuit() {
        return numeroCuit;
    }

    public void setNumeroCuit(String numeroCuit) {
        this.numeroCuit = numeroCuit;
    }

    @Override
    public String toString() {
        return getRazonSocial().toString();
    }
}
