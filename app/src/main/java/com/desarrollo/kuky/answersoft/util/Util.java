package com.desarrollo.kuky.answersoft.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.controlador.BaseHelper;
import com.desarrollo.kuky.answersoft.ui.UILogin;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kuky on 14/11/2016.
 */

public class Util {
    Context context;
    public static String NO_MODIFICA_PRECIO = "1";
    public static String NO_MODIFICA_STOCK = "2";
    public static String PREFIJO_USER = "USR_";
    public static String DIRECCION_IP = "192.168.1.102";
    public static String PUERTO = "3306";
    public static String NOMBRE_BASE = "ubeer";
    public static String USER_BD = "kuky";
    public static String PASS_BD = "kuky";
    public static String BASE_DATOS = "jdbc:mysql://" + DIRECCION_IP + ":" + PUERTO + "/" + NOMBRE_BASE;

    //public static String BASE_DATOS="jdbc:mysql://192.168.0.2:3306/answer_fact25";
    //public static String USER_BD="root";
    //public static String PASS_BD="";

    public Util(Context context) {
        this.context = context;
    }

    public Typeface getTypeface() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/CaviarDreams_Bold.ttf");
    }

    public static int obtenerCantidadRegistros(ResultSet rs) throws SQLException {
        int i = 0;
        try {
            while (rs.next()) {
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        rs.first();
        return i;
    }

    public static String RellenarConCeros(int largo, String ARellenar) {
        String cadena = null;
        int cantDiferencia = largo - ARellenar.length();
        String diferencia = "";
        for (int i = 0; i < cantDiferencia; i++) {
            diferencia += "0";
        }
        cadena = diferencia + ARellenar;
        return cadena;
    }

    public static float RedondearFloat(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        float floatRedondeado = Float.parseFloat(String.valueOf(bd));
        return floatRedondeado;
    }

    public static void showDialogCerrarSesion(final Activity a) {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(a);
        View promptView = layoutInflater.inflate(R.layout.dialog_cerrar_sesion, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(a);
        alertDialogBuilder.setView(promptView);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SQLiteDatabase db = BaseHelper.getInstance(a).getWritableDatabase();
                        db.execSQL("DELETE FROM usuarios");
                        Intent intent = new Intent(a, UILogin.class);
                        a.startActivity(intent);
                        a.finish();
                    }
                })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public static void setDireccionIp(String direccionIp) {
        DIRECCION_IP = direccionIp;
    }

    public static void setPUERTO(String PUERTO) {
        Util.PUERTO = PUERTO;
    }

    public static void setNombreBase(String nombreBase) {
        NOMBRE_BASE = nombreBase;
    }

    public static void setUserBd(String userBd) {
        USER_BD = userBd;
    }

    public static void setPassBd(String passBd) {
        PASS_BD = passBd;
    }
}
