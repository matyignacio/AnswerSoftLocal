package com.desarrollo.kuky.answersoft.controlador;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Kuky on 15/11/2016.
 */

public class Conexion {

    //public static String BASE_DATOS="jdbc:mysql://192.168.0.2:3306/answer_fact25";
    //public static String BASE_DATOS="jdbc:mysql://192.168.1.101:3306/answer_fact";

    public static Connection GetConnection(Activity a) {
        Connection conexion = null;
        String direccionIP, puerto, nombreBase, user, pass;
        SQLiteDatabase db = BaseHelper.getInstance(a).getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM configuracion", null);
        if (c.moveToFirst()) {
            do {
                direccionIP = c.getString(0);
                puerto = c.getString(1);
                nombreBase = c.getString(2);
                user = c.getString(3);
                pass = c.getString(4);
            } while (c.moveToNext());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conexion = DriverManager.getConnection(
                        "jdbc:mysql://" + direccionIP + ":" + puerto + "/" + nombreBase, user, pass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                conexion = null;
            } catch (SQLException e) {
                e.printStackTrace();
                conexion = null;
            }
        } else {
            Toast.makeText(a, "Debe configurar la conexion al servidor", Toast.LENGTH_SHORT).show();
        }
        c.close();
        db.close();
        return conexion;
    }

    /*public static Connection GetConnection(Activity a) {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://192.168.1.101:3306/answer_fact", "root", "4F737D");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            conexion = null;
        } catch (SQLException e) {
            e.printStackTrace();
            conexion = null;
        } finally {
            return conexion;
        }
    }*/
}
