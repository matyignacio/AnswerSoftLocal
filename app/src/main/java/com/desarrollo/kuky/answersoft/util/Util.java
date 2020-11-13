package com.desarrollo.kuky.answersoft.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.desarrollo.kuky.answersoft.R;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Created by Kuky on 14/11/2016.
 */

public class Util {
    Context context;
    public static final int ERROR = 0;
    public static final int EXITOSO = 1;
    public static String PRODUCTO_SIN_CODIGO = "-";
    public static String NO_MODIFICA_PRECIO = "1";
    public static String NO_MODIFICA_STOCK = "2";
    public static String PREFIJO_USER = "USR_";

    //public static String BASE_DATOS="jdbc:mysql://192.168.0.2:3306/answer_fact25";
    //public static String USER_BD="root";
    //public static String PASS_BD="";

    public Util(Context context) {
        this.context = context;
    }

    public Typeface getTypeface() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/CaviarDreams_Bold.ttf");
    }

    public static void abrirActivity(Activity a, Class destino) {
        Intent intent = new Intent(a, destino);
        a.startActivity(intent);
        a.finish();
    }

    public static void abrirActivityWithoutClose(Activity a, Class destino) {
        Intent intent = new Intent(a, destino);
        a.startActivity(intent);
    }

    public static void abrirActivityWithBundle(Activity a, Class destino, Bundle mBundle) {
        /** COMO SE USA
         *             Bundle mBundle = new Bundle();
         *             mBundle.putInt("key", value); (Pueden ser ints, Strings, etc)
         *             abrirActivityWithBundle(this, ActivityDestino.class, mBundle);
         * */
        Intent intent = new Intent(a, destino);
        intent.putExtras(mBundle);
        a.startActivity(intent);
        a.finish();
    }

    public static void mostrarMensaje(Context c, String mensaje) {
        Toast.makeText(c, mensaje, Toast.LENGTH_LONG).show();
        Log.e("MOSTRARMENSAJE:::", mensaje);
    }

    public static void mostrarMensajeLog(Context c, String mensaje) {
        Log.e("MOSTRARMENSAJE:::", mensaje);
    }

    public static android.app.AlertDialog createCustomDialog(Activity a,
                                                             String titulo, String cuerpo,
                                                             String mensajeSi, String mensajeNo,
                                                             final Callable<Void> methodAcept, final Callable<Void> methodCancel) {
        final android.app.AlertDialog alertDialog;
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(a);
        // Get the layout inflater
        LayoutInflater inflater = a.getLayoutInflater();
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.custom_dialog, null);
        //builder.setView(inflater.inflate(R.layout.dialog_signin, null))
        TextView tvTitulo = (TextView) v.findViewById(R.id.tvTitulo);
        TextView tvCuerpo = (TextView) v.findViewById(R.id.tvCuerpo);
        Button bAceptar = (Button) v.findViewById(R.id.bAceptar);
        Button bCancelar = (Button) v.findViewById(R.id.bCancelar);
        tvTitulo.setText(titulo);
        tvCuerpo.setText(cuerpo);
        bAceptar.setText(mensajeSi);
        bCancelar.setText(mensajeNo);
        builder.setView(v);
        alertDialog = builder.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        // Add action buttons
        bAceptar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v12) {
                        // Aceptar
                        try {
                            methodAcept.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        alertDialog.dismiss();
                    }
                }
        );
        bCancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v1) {
                        try {
                            methodCancel.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        alertDialog.dismiss();
                    }
                }
        );
        return alertDialog;
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

    public static String obtenerHora() {
        Date d = new Date();
        int ano = d.getYear() + 1900;
        int mes = d.getMonth() + 1;
        return ano + "/" + mes + "/" + d.getDate()
                + " " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds();
    }

    public static int validarCampos(Activity a, ArrayList<EditText> inputs) {
        int i;
        for (i = 0; i < inputs.size(); i++) {
            if (inputs.get(i).getText().toString().equals("")) {
                mostrarMensaje(a, "Debe llenar todos los campos");
                inputs.get(i).requestFocus();
                return ERROR;
            }
        }
        return EXITOSO;
    }

    public static void disableInput(Activity a, EditText editText) {
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextIsSelectable(true);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setTextIsSelectable(false);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return true;  // Blocks input from hardware keyboards.
            }
        });
    }
}
