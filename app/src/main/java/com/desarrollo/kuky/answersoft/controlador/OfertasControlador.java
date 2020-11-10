package com.desarrollo.kuky.answersoft.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.desarrollo.kuky.answersoft.objetos.Producto;
import com.desarrollo.kuky.answersoft.util.Util;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.SQLException;

import static com.desarrollo.kuky.answersoft.R.style.b_primary;
import static com.desarrollo.kuky.answersoft.R.style.b_primary_disabled;

/**
 * Created by Kuky on 19/11/2016.
 */

public class OfertasControlador {
    ProgressDialog pDialog;
    private Producto p;
    private Insertar insertar;

    private class Insertar extends AsyncTask<String, Float, String> {
        Activity a;
        Producto p;
        Button b;

        public Insertar(Activity a, Producto p, Button b) {
            this.a = a;
            this.p = p;
            this.b = b;
        }

        @Override
        protected void onPreExecute() {
            b.setTextAppearance(a, b_primary_disabled);
            Util util = new Util(a);
            b.setTypeface(util.getTypeface());
            pDialog = new ProgressDialog(a);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Estableciendo conexion...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn;
            PreparedStatement ps;
            String retorno = "No se pudo conectar a la dase de datos";
            try {
                conn = (Connection) Conexion.GetConnection(a);
                if (conn != null) {
                    Log.d("Conectado", "se conecto con exito");
                    String consultaSql = "DELETE FROM ofertas WHERE IDPRODUCTO=?";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.setString(1, Util.RellenarConCeros(6, String.valueOf(p.getIdProducto())));
                    ps.execute();
                    consultaSql = "INSERT INTO  ofertas (IDPRODUCTO, DESCRIP, BARRA, PRECIO) VALUES (?,?,?,?)";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.setString(1, Util.RellenarConCeros(6, String.valueOf(p.getIdProducto())));
                    ps.setString(2, p.getDescripcion());
                    ps.setString(3, p.getCodAlternativo());
                    ps.setFloat(4, p.getPrecioVenta());
                    ps.executeUpdate();
                    ps.close();
                    conn.close();
                    retorno = "Se inserto correctamente";
                }
                return retorno;
            } catch (SQLException e) {
                e.printStackTrace();
                return "Hubo un error al insertar etiqueta";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            Toast.makeText(a, s, Toast.LENGTH_SHORT).show();
            b.setTextAppearance(a, b_primary);
            Util util = new Util(a);
            b.setTypeface(util.getTypeface());
        }
    }

    public void insertar(Activity a, Producto p, Button b) {
        insertar = new Insertar(a, p, b);
        insertar.execute();
    }
}
