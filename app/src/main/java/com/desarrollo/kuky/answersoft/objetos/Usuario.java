package com.desarrollo.kuky.answersoft.objetos;

/**
 * Created by Kuky on 25/03/2017.
 */

public class Usuario {
    private String id;
    private String nombre;
    private String pass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
