package com.desarrollo.kuky.answersoft.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.desarrollo.kuky.answersoft.ui.UILogin;
import com.desarrollo.kuky.answersoft.ui.UIParametros;
import com.desarrollo.kuky.answersoft.ui.UIPrincipal;
import com.desarrollo.kuky.answersoft.ui.UIProductoSeleccionado;
import com.desarrollo.kuky.answersoft.util.Util;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import static com.desarrollo.kuky.answersoft.util.Util.abrirActivity;
import static com.desarrollo.kuky.answersoft.util.Util.mostrarMensaje;

/**
 * Created by Kuky on 25/03/2017.
 */

public class ConfigaccControlador {
    private ProgressDialog pDialog;

    private class PermisosProductos extends AsyncTask<String, Float, String> {
        Activity a;
        String user;
        String permiso;

        public PermisosProductos(Activity a) {
            this.a = a;
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(a);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Estableciendo conexion...");
            pDialog.setCancelable(false);
            pDialog.show();
            SQLiteDatabase db = BaseHelper.getInstance(a).getReadableDatabase();
            try {
                Cursor c = db.rawQuery("SELECT IDUSUARIO FROM usuarios", null);
                if (c.moveToNext()) {
                    user = "USR_" + c.getString(0);
                }
            } catch (Exception e) {
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            permiso = "N"; //inicializamos con el permiso negado
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            String retorno = "No se pudo conectar a la dase de datos";
            try {
                conn = (Connection) Conexion.GetConnection(a);
                if (conn != null) {
                    String consultaSql = "SELECT " + user +
                            " FROM `configacc`" +
                            " WHERE `HIJO` = 203.0000";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.execute();
                    rs = ps.getResultSet();
                    if (rs.next()) {
                        permiso = rs.getString(1);
                    }
                    rs.close();
                    ps.close();
                    conn.close();
                    retorno = "";
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
                if (permiso.equals("S")) {
                    UIProductoSeleccionado.permisoStock = 1;
                } else {
                    UIProductoSeleccionado.permisoStock = 0;
                }
            } else {
                mostrarMensaje(a, s);
            }
        }
    }

    public void permisosProductos(Activity a) {
        PermisosProductos permisosProductos = new PermisosProductos(a);
        permisosProductos.execute();
    }

    private class PermisosParametros extends AsyncTask<String, Float, String> {
        Activity a;
        String user;
        String permiso;

        public PermisosParametros(Activity a) {
            this.a = a;
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(a);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Estableciendo conexion...");
            pDialog.setCancelable(false);
            pDialog.show();
            SQLiteDatabase db = BaseHelper.getInstance(a).getReadableDatabase();
            try {
                Cursor c = db.rawQuery("SELECT IDUSUARIO FROM usuarios", null);
                if (c.moveToNext()) {
                    user = "USR_" + c.getString(0);
                }
            } catch (Exception e) {
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            permiso = "N"; //inicializamos con el permiso negado
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            String retorno = "No se pudo conectar a la dase de datos";
            try {
                conn = (Connection) Conexion.GetConnection(a);
                if (conn != null) {
                    String consultaSql = "SELECT " + user +
                            " FROM `configacc`" +
                            " WHERE `HIJO` = 144.0000";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.execute();
                    rs = ps.getResultSet();
                    if (rs.next()) {
                        permiso = rs.getString(1);
                    }
                    rs.close();
                    ps.close();
                    conn.close();
                    retorno = "";
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
                if (permiso.equals("S")) {
                    abrirActivity(a, UIParametros.class);
                } else {
                    Util.createCustomDialog(a,
                            "NO TIENE PERMISOS",
                            "¿Que desea hacer?",
                            "CERRAR SESION",
                            "VOLVER AL INICIO",
                            new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    ///////////////////////////////////////////////////////////////////////////////////////
                                    SQLiteDatabase db = BaseHelper.getInstance(a).getWritableDatabase();
                                    db.execSQL("DELETE FROM usuarios");
                                    abrirActivity(a, UILogin.class);
                                    return null;
                                }
                            },
                            new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    abrirActivity(a, UIPrincipal.class);
                                    return null;
                                }
                            }).show();
                }
            } else {
                mostrarMensaje(a, s);
            }
        }
    }

    public void permisosParametros(Activity a) {
        PermisosParametros permisosParametros = new PermisosParametros(a);
        permisosParametros.execute();
    }

}
