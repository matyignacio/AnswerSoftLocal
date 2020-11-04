package com.desarrollo.kuky.answersoft.controlador;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kuky on 15/10/16.
 */
public class BaseHelper extends SQLiteOpenHelper {
    private static BaseHelper sInstance;

    private static final String DATABASE_NAME = "AnswerSoft";
    private static final int DATABASE_VERSION = 1;

    public static synchronized BaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new BaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private BaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlTablaConfiguracion);
        sqLiteDatabase.execSQL(sqlTablaUsuarios);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    String sqlTablaClientes = "CREATE TABLE IF NOT EXISTS clientes (" +
            "IDCLIENTE integer default NULL," +
            "RAZONSOC varchar(30) default NULL, " +
            "DOMICILIO varchar(40) default NULL," +
            "TELEFONO varchar(25) default NULL," +
            "TIPORESP varchar(3) default NULL," +
            "NROCUIT varchar(11) default NULL" +
            ")";

    String sqlTablaProductos = "CREATE TABLE IF NOT EXISTS productos (" +
            "IDPRODUCTO integer default NULL," +
            "DESCRIP varchar(40) default NULL, " +
            "STOCK decimal(20,4) default NULL," +
            "PRECVENTA decimal(15,4) default NULL," +
            "CODALTERN varchar(20) default NULL" +
            ")";
    String sqlTablaConfiguracion = "CREATE TABLE IF NOT EXISTS configuracion (" +
            "direcionip varchar(30) default NULL," +
            "puerto varchar(7) default NULL, " +
            "nombrebase varchar(30) default NULL," +
            "user varchar(15) default NULL," +
            "pass varchar(18) default NULL" +
            ")";

    String sqlTablaUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
            "`IDUSUARIO` varchar(6) default NULL," +
            "`NOMBRE` varchar(30) default NULL, " +
            "`pass` varchar(50) default NULL" +
            ")";

    public String getSqlTablaUsuarios() {
        return sqlTablaUsuarios;
    }

    public String getSqlTablaClientes() {
        return sqlTablaClientes;
    }

    public String getSqlTablaProductos() {
        return sqlTablaProductos;
    }

    public String getSqlTablaConfiguracion() {
        return sqlTablaConfiguracion;
    }
}