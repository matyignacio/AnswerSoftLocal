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
        sqLiteDatabase.execSQL(sqlTablaParametros);
        sqLiteDatabase.execSQL(sqlTablaPedidoLocal);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Loop through each version when an upgrade occurs.
        for (int version = oldVersion + 1; version <= newVersion; version++) {
            switch (version) {
                case 2:
                    // Apply changes made in version 2
                    /*db.execSQL(
                            "ALTER TABLE " +
                                    TABLE_PRODUCTS +
                                    " ADD COLUMN " +
                                    COLUMN_DESCRIPTION +
                                    " TEXT;"
                    );*/
                    break;

                case 3:
                    // Apply changes made in version 3
                    /*db.execSQL(CREATE_TABLE_TRANSACTION);*/
                    break;
            }
        }
    }

    String sqlTablaParametros = "CREATE TABLE IF NOT EXISTS parametros (" +
            "`IDVENDEDOR` varchar(6) default NULL," +
            "`NROPTOVTA` decimal(10,4) default NULL," +
            "limite_clientes int(11) default NULL, " +
            "limite_productos int(11) default NULL" +
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
    public static String sqlTablaPedidoLocal = "CREATE TABLE PedidoLocal (" +
            "idP INTEGER, " +
            "cantidad decimal(10,4) default NULL, " +
            "DESCRIPPROD2 varchar(40)," +
            "PUNIT2 decimal(15,4)," +
            "SUBTOT2 decimal(15,4)," +
            "CODALTERN varchar(20) default NULL," +
            "id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ")";

    public String getSqlTablaUsuarios() {
        return sqlTablaUsuarios;
    }

    public String getSqlTablaConfiguracion() {
        return sqlTablaConfiguracion;
    }

    public String getSqlTablaParametros() {
        return sqlTablaParametros;
    }

    public static String getSqlTablaPedidoLocal() {
        return sqlTablaPedidoLocal;
    }
}