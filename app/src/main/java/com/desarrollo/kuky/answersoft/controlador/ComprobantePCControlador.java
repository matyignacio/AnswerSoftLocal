package com.desarrollo.kuky.answersoft.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.desarrollo.kuky.answersoft.objetos.ComprobantePC;
import com.desarrollo.kuky.answersoft.ui.adapters.lvaPresupuestos;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.desarrollo.kuky.answersoft.util.Util.RellenarConCeros;

public class ComprobantePCControlador {
    ProgressDialog pDialog;
    private ArrayList<ComprobantePC> comprobantesXC = new ArrayList<>();


    private class BuscarPorCliente extends AsyncTask<String, Float, String> {
        Activity a;
        ListView l;
        String razonSocial;

        public BuscarPorCliente(Activity a, ListView l, String razonSocial) {
            this.a = a;
            this.l = l;
            this.razonSocial = razonSocial;
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
            String retorno = "No se pudo conectar a la dase de datos";
            int limit = 10000, NROPTOVTA = 0;
            String comprobante = "";
            comprobantesXC = new ArrayList<>();
            ////////////////////////////////////////////////////////////////////////////////////
            SQLiteDatabase db = BaseHelper.getInstance(a).getReadableDatabase();
            Cursor c = db.rawQuery("SELECT NROPTOVTA,limite_presupuestos FROM parametros", null);
            if (c.moveToFirst()) {
                NROPTOVTA = (int) c.getFloat(0);
                limit = c.getInt(1);
            }
            comprobante = RellenarConCeros(4, String.valueOf(NROPTOVTA)) + "%";
            c.close();
            db.close();
            ////////////////////////////////////////////////////////////////////////////////////
            conn = (Connection) Conexion.GetConnection(a);
            if (conn != null) {
                if (razonSocial.equals("")) {
                    try {
                        String consultaSql = "SELECT NROCOMP, CLIENTE, FECHA, TOTAL, VENDEDOR, idt" +
                                " FROM comprobantes_p_c" +
                                " WHERE NROCOMP like ?" +
                                " ORDER BY idt DESC" +
                                " LIMIT ?";
                        ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                        ps.setString(1, comprobante);
                        ps.setInt(2, limit);
                        ps.executeQuery();
                        rs = ps.getResultSet();
                        while (rs.next()) {
                            ComprobantePC comprobantePC = new ComprobantePC();
                            comprobantePC.setNROCOMP(rs.getString(1));
                            comprobantePC.setCLIENTE(rs.getString(2));
                            comprobantePC.setFECHA(rs.getString(3));
                            comprobantePC.setTOTAL(rs.getFloat(4));
                            comprobantePC.setVENDEDOR(rs.getString(5));
                            comprobantePC.setId(rs.getInt(6));
                            comprobantesXC.add(comprobantePC);
                        }
                        rs.close();
                        ps.close();
                        conn.close();
                        retorno = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return e.toString();
                    }
                } else {
                    try {
                        conn = (Connection) Conexion.GetConnection(a);
                        String consultaSql = "SELECT NROCOMP, CLIENTE, FECHA, TOTAL, VENDEDOR, idt" +
                                " FROM comprobantes_p_c" +
                                " WHERE CLIENTE LIKE ?" +
                                " ORDER BY idt DESC" +
                                " LIMIT 10";
                        ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                        ps.setString(1, "%" + razonSocial + "%");
                        ps.executeQuery();
                        rs = ps.getResultSet();
                        while (rs.next()) {
                            ComprobantePC comprobantePC = new ComprobantePC();
                            comprobantePC.setNROCOMP(rs.getString(1));
                            comprobantePC.setCLIENTE(rs.getString(2));
                            comprobantePC.setFECHA(rs.getString(3));
                            comprobantePC.setTOTAL(rs.getFloat(4));
                            comprobantePC.setVENDEDOR(rs.getString(5));
                            comprobantePC.setId(rs.getInt(6));
                            comprobantesXC.add(comprobantePC);
                        }
                        rs.close();
                        ps.close();
                        conn.close();
                        retorno = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return e.toString();
                    }
                }
            }
            return retorno;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s.equals("")) {
                lvaPresupuestos adaptador = new lvaPresupuestos(a, comprobantesXC);
                l.setAdapter(adaptador);
            } else {
                Toast.makeText(a, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void buscarPorCliente(Activity a, ListView l, String descripcion) {
        BuscarPorCliente buscarPorCliente = new BuscarPorCliente(a, l, descripcion);
        buscarPorCliente.execute();
    }

    public ComprobantePC extraerDeLista(int posicion) {
        return comprobantesXC.get(posicion);
    }

}
