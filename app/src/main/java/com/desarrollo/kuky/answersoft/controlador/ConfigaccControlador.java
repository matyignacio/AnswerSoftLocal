package com.desarrollo.kuky.answersoft.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.desarrollo.kuky.answersoft.objetos.Configacc;
import com.desarrollo.kuky.answersoft.ui.UIClientes;
import com.desarrollo.kuky.answersoft.ui.UIProductos;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Kuky on 25/03/2017.
 */

public class ConfigaccControlador {
    private ProgressDialog pDialog;
    public PermisosClientes permisosClientes;
    public PermisosProductos permisosProductos;
    public EditarProductos editarProductos;
    Configacc configacc;

    private class PermisosClientes extends AsyncTask<String, Float, String> {
        Activity a;
        String user;
        ArrayList<Configacc> configaccs;

        public PermisosClientes(Activity a) {
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
            configaccs = new ArrayList<>();
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            try {
                conn = (Connection) Conexion.GetConnection(a);
                String consultaSql = "Select MENU,INDICE,ITEM, OPCIONES, ITEMID, HIJO \n" +
                        "    From CONFIGACC " +
                        "    WHERE TIPO = 'M' " +
                        "    and menu='mnmantenimiento' " +
                        "    and indice=8 " +
                        "    and " + user + " ='S'" +
                        "    Order by orden";
                ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                ps.execute();
                rs = ps.getResultSet();
                if (rs.next()) {
                    configacc = new Configacc();
                    configacc.setMenu(rs.getString(1));
                    configacc.setIndice(rs.getFloat(2));
                    configacc.setItem(rs.getString(3));
                    configacc.setOpciones(rs.getString(4));
                    configacc.setItemid(rs.getString(5));
                    configacc.setHijo(rs.getFloat(6));
                    configaccs.add(configacc);
                }
                rs.close();
                ps.close();
                conn.close();
                return "";
            } catch (SQLException e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s.equals("")) {
                int bandera = 0;
                for (int i = 0; i < configaccs.size(); i++) {
                    if (configaccs.get(i).getIndice() == 8) {
                        bandera = 1;
                        Intent intent = new Intent(a, UIClientes.class);
                        a.startActivity(intent);
                        a.finish();
                    }
                }
                if (bandera == 0) {
                    Toast.makeText(a, "Ud no tiene permisos para acceder a esta seccion.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(a, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void permisosClientes(Activity a) {
        permisosClientes = new PermisosClientes(a);
        permisosClientes.execute();
    }

    private class PermisosProductos extends AsyncTask<String, Float, String> {
        Activity a;
        String user;
        ArrayList<Configacc> configaccs;

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
            configaccs = new ArrayList<>();
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            try {
                conn = (Connection) Conexion.GetConnection(a);
                String consultaSql = "Select MENU,INDICE,ITEM, OPCIONES, ITEMID, HIJO \n" +
                        "    From CONFIGACC " +
                        "    WHERE TIPO = 'M' " +
                        "    and menu='mnmantenimiento' " +
                        "    and indice = 0 " +
                        "    and " + user + " ='S'" +
                        "    Order by orden";
                ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                ps.execute();
                rs = ps.getResultSet();
                if (rs.next()) {
                    configacc = new Configacc();
                    configacc.setMenu(rs.getString(1));
                    configacc.setIndice(rs.getFloat(2));
                    configacc.setItem(rs.getString(3));
                    configacc.setOpciones(rs.getString(4));
                    configacc.setItemid(rs.getString(5));
                    configacc.setHijo(rs.getFloat(6));
                    configaccs.add(configacc);
                }
                rs.close();
                ps.close();
                conn.close();
                return "";
            } catch (SQLException e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s.equals("")) {
                int bandera = 0;
                for (int i = 0; i < configaccs.size(); i++) {
                    if (configaccs.get(i).getIndice() == 0) {
                        bandera = 1;
                        Intent intent = new Intent(a, UIProductos.class);
                        a.startActivity(intent);
                        a.finish();
                    }
                }
                if (bandera == 0) {
                    Toast.makeText(a, "Ud no tiene permisos para acceder a esta seccion.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(a, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void permisosProductos(Activity a) {
        permisosProductos = new PermisosProductos(a);
        permisosProductos.execute();
    }

    private class EditarProductos extends AsyncTask<String, Float, String> {
        Activity a;
        String user;

        public EditarProductos(Activity a) {
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
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            UIProductos.configaccs = new ArrayList<>();
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            try {
                conn = (Connection) Conexion.GetConnection(a);
                String consultaSql = "Select ITEM, OPCIONES, ITEMID, ITEMIDH From configacc" +
                        "    WHERE TIPO = 'F'" +
                        "    and ITEMID = 'FrmABMProductos'" +
                        "    and " + user + " ='N'";
                ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {//SIEMPRE ES WHILE CUANDO SE ESPERA 2 O MAS REGISTROS (Y METERLOS EN UN ARRAYLIST)
                    configacc = new Configacc();
                    configacc.setItem(rs.getString(1));
                    configacc.setOpciones(rs.getString(2));
                    configacc.setItemid(rs.getString(3));
                    configacc.setItemidh(rs.getString(4));
                    UIProductos.configaccs.add(configacc);
                }
                rs.close();
                ps.close();
                conn.close();
                return "";
            } catch (SQLException e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s.equals("")) {
                //Toast.makeText(a, "Pasa por aca", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(a, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void editarProductos(Activity a) {
        editarProductos = new EditarProductos(a);
        editarProductos.execute();
    }

}
