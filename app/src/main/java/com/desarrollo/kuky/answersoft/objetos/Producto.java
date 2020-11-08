package com.desarrollo.kuky.answersoft.objetos;

/**
 * Created by kuky on 15/10/16.
 */
public class Producto {
    private int idProducto;
    private String descripcion;
    private float stock;
    private float precioVenta;
    private String codAlternativo;
    private int moneda;
    private float listaCodigo;

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getStock() {
        return stock;
    }

    public void setStock(float stock) {
        this.stock = stock;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getCodAlternativo() {
        return codAlternativo;
    }

    public void setCodAlternativo(String codAlternativo) {
        this.codAlternativo = codAlternativo;
    }

    public int getMoneda() {
        return moneda;
    }

    public void setMoneda(int moneda) {
        this.moneda = moneda;
    }

    public float getListaCodigo() {
        return listaCodigo;
    }

    public void setListaCodigo(float listaCodigo) {
        this.listaCodigo = listaCodigo;
    }

    @Override
    public String toString() {
        return this.getDescripcion().toString();
    }
}
