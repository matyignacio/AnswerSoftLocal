package com.desarrollo.kuky.answersoft.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.desarrollo.kuky.answersoft.objetos.ComprobantePD;
import com.desarrollo.kuky.answersoft.ui.UIPresupuestoSeleccionado;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.desarrollo.kuky.answersoft.ui.UIPresupuestoSeleccionado.comprobantesPD;
import static com.desarrollo.kuky.answersoft.util.Util.abrirActivity;
import static com.desarrollo.kuky.answersoft.util.Util.mostrarMensaje;

public class ComprobantePDControlador {
    private ProgressDialog pDialog;
    private ExtraerPorNumComp extraerPorNumComp;


    private class ExtraerPorNumComp extends AsyncTask<String, Float, String> {
        Activity a;
        String comprobante;

        public ExtraerPorNumComp(Activity a, String comprobante) {
            this.a = a;
            this.comprobante = comprobante;
        }

        @Override
        protected void onPreExecute() {
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
            ResultSet rs;
            comprobantesPD = new ArrayList<>();
            try {
                conn = (Connection) Conexion.GetConnection(a);
                String consultaSql = "SELECT DESCRIPPROD, CANT, SUBTOT, NROCOMP" +
                        " FROM comprobantes_p_d" +
                        " WHERE NROCOMP = ?";
                ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                ps.setString(1, comprobante);
                ps.executeQuery();
                rs = ps.getResultSet();
                while (rs.next()) {
                    ComprobantePD comprobantePD = new ComprobantePD();
                    comprobantePD.setDESCRIPPROD(rs.getString(1));
                    comprobantePD.setCANT(rs.getFloat(2));
                    comprobantePD.setSUBTOT(rs.getFloat(3));
                    comprobantePD.setNROCOMP(rs.getString(4));
                    comprobantesPD.add(comprobantePD);
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
                abrirActivity(a, UIPresupuestoSeleccionado.class);
            } else {
                mostrarMensaje(a, s);
            }
        }
    }

    public void extraerPorNumComp(Activity a, String comprobante) {
        extraerPorNumComp = new ExtraerPorNumComp(a, comprobante);
        extraerPorNumComp.execute();
    }

}
