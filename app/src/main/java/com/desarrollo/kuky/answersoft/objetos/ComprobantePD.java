package com.desarrollo.kuky.answersoft.objetos;

public class ComprobantePD {
    private String NROCOMP;
    private String IDPRODUCTO;
    private String DESCRIPPROD;
    private float CANT;
    private float PUNIT;
    private float SUBTOT;
    private int DEPOSITO;
    private String PRODUCTO;

    public String getNROCOMP() {
        return NROCOMP;
    }

    public void setNROCOMP(String NROCOMP) {
        this.NROCOMP = NROCOMP;
    }

    public String getIDPRODUCTO() {
        return IDPRODUCTO;
    }

    public void setIDPRODUCTO(String IDPRODUCTO) {
        this.IDPRODUCTO = IDPRODUCTO;
    }

    public String getDESCRIPPROD() {
        return DESCRIPPROD;
    }

    public void setDESCRIPPROD(String DESCRIPPROD) {
        this.DESCRIPPROD = DESCRIPPROD;
    }

    public float getCANT() {
        return CANT;
    }

    public void setCANT(float CANT) {
        this.CANT = CANT;
    }

    public float getPUNIT() {
        return PUNIT;
    }

    public void setPUNIT(float PUNIT) {
        this.PUNIT = PUNIT;
    }

    public float getSUBTOT() {
        return SUBTOT;
    }

    public void setSUBTOT(float SUBTOT) {
        this.SUBTOT = SUBTOT;
    }

    public int getDEPOSITO() {
        return DEPOSITO;
    }

    public void setDEPOSITO(int DEPOSITO) {
        this.DEPOSITO = DEPOSITO;
    }

    public String getPRODUCTO() {
        return PRODUCTO;
    }

    public void setPRODUCTO(String PRODUCTO) {
        this.PRODUCTO = PRODUCTO;
    }
}
