package com.desarrollo.kuky.answersoft.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.Editable;
import android.widget.Toast;

import com.desarrollo.kuky.answersoft.ui.UILogin;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kuky on 25/03/2017.
 */

public class UsuarioControlador {
    private ProgressDialog pDialog;
    UsuarioPorNombre usuarioPorNombre;


    private class UsuarioPorNombre extends AsyncTask<String, Float, String> {
        Activity a;
        Editable nombre;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(a);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Estableciendo conexion...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        public UsuarioPorNombre(Activity a, Editable nombre) {
            this.a = a;
            this.nombre = nombre;
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            String retorno = "No se pudo conectar a la dase de datos";
            try {
                conn = (Connection) Conexion.GetConnection(a);
                if (conn != null) {
                    String consultaSql = "SELECT IDUSUARIO, NOMBRE, PASSWORD FROM usuarios WHERE NOMBRE like '" + nombre + "';";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.execute();
                    rs = ps.getResultSet();
                    if (rs.next()) {
                        UILogin.usuario.setId(rs.getString(1));
                        UILogin.usuario.setNombre(rs.getString(2));
                        UILogin.usuario.setPass(rs.getString(3));
                    } else {
                        UILogin.usuario.setNombre(null);
                    }
                    rs.close();
                    ps.close();
                    conn.close();
                    if (UILogin.usuario.getNombre() == null) {
                        retorno = "El nombre de usuario es inexistente";
                    } else {
                        retorno = "";
                    }
                }
                return retorno;
            } catch (SQLException e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s.equals("")) {
                //Toast.makeText(a, s, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(a, s.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void extraerPorNombre(Activity a, Editable descripcion) {
        usuarioPorNombre = new UsuarioPorNombre(a, descripcion);
        usuarioPorNombre.execute();
    }

}
