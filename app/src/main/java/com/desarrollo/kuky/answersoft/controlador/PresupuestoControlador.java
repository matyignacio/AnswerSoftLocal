package com.desarrollo.kuky.answersoft.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Button;

import com.desarrollo.kuky.answersoft.ui.UIClientes;
import com.desarrollo.kuky.answersoft.ui.UIPrincipal;
import com.desarrollo.kuky.answersoft.util.Util;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.desarrollo.kuky.answersoft.R.style.b_primary;
import static com.desarrollo.kuky.answersoft.R.style.b_primary_disabled;
import static com.desarrollo.kuky.answersoft.util.Util.RellenarConCeros;
import static com.desarrollo.kuky.answersoft.util.Util.abrirActivity;
import static com.desarrollo.kuky.answersoft.util.Util.mostrarMensaje;
import static com.desarrollo.kuky.answersoft.util.Util.mostrarMensajeLog;

public class PresupuestoControlador {

    private ProgressDialog pDialog;
    private GuardarPresupuestoXC guardarPresupuestoXC;
    private GuardarPresupuestoXD guardarPresupuestoXD;


    private class GuardarPresupuestoXC extends AsyncTask<String, Float, String> {
        Activity a;
        Button b;
        Float total;
        int vidvendedor, vdeposito, PuntodeVenta, IDPLANILLA, NROPRESUP;

        public GuardarPresupuestoXC(Activity a, Button b, Float total, int vidvendedor, int vdeposito, int PuntodeVenta, int IDPLANILLA, int NROPRESUP) {
            this.a = a;
            this.b = b;
            this.total = total;
            this.vidvendedor = vidvendedor;
            this.vdeposito = vdeposito;
            this.PuntodeVenta = PuntodeVenta;
            this.IDPLANILLA = IDPLANILLA;
            this.NROPRESUP = NROPRESUP;
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(a);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Guardando presupuesto...");
            pDialog.setCancelable(false);
            pDialog.show();
            b.setTextAppearance(a, b_primary_disabled);
            Util util = new Util(a);
            b.setTypeface(util.getTypeface());
        }

        @Override
        protected String doInBackground(String... strings) {
            String retorno = "";
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            try {
                String fecha = Util.obtenerHora();
                String vnombrevendedor = "";
                SQLiteDatabase db = BaseHelper.getInstance(a).getWritableDatabase();
                Cursor c = db.rawQuery("SELECT IDVENDEDOR,NROPTOVTA FROM parametros", null);
                if (c.moveToFirst()) {
                    vidvendedor = c.getInt(0);
                    PuntodeVenta = (int) c.getFloat(1);
                }
                conn = (Connection) Conexion.GetConnection(a);
                if (conn != null) {
                    mostrarMensajeLog(a, "se conecto con exito");
                    ////////////////////////////////////////////////////////////////////////////////
                    String consultaSql = "SELECT AYNVEN FROM vendedor WHERE IDVENDEDOR=?";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.setInt(1, vidvendedor);
                    ps.execute();
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        vnombrevendedor = rs.getString(1);
                    }
                    ////////////////////////////////////////////////////////////////////////////////
                    consultaSql = "SELECT IDPLANILLA FROM planilla WHERE FECCIERRE = '0000-00-00 00:00:00' OR FECCIERRE = ''";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.execute();
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        IDPLANILLA = (int) rs.getFloat(1);
                    }
                    ////////////////////////////////////////////////////////////////////////////////
                    consultaSql = "SELECT NROPRESUP FROM config WHERE NROPTOVTA = ?";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.setInt(1, PuntodeVenta);
                    ps.execute();
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        NROPRESUP = (int) rs.getFloat(1);
                    }
                    NROPRESUP = NROPRESUP + 1;
                    ////////////////////////////////////////////////////////////////////////////////
                    consultaSql = "INSERT INTO `comprobantes_p_c` (TIPODOC,LETRA,COMPORIGINAL,NROCOMP,IDPLANILLA," +
                            " IDCLIENTE,IDVENDEDOR,FECHA,HORA,TIPOREM,GRAVADO,IVAINSC,IVANOINSC,CLIENTE,VENDEDOR," +
                            " TOTAL) VALUES " +
                            " ('PRS', 'P', 'X', ?, ?, ?, ?, ?, ?, '', 0, 0, 0, ?, ?, ?)";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.setString(1, (Util.RellenarConCeros(4, String.valueOf(PuntodeVenta)) +
                            Util.RellenarConCeros(8, String.valueOf(NROPRESUP))));
                    ps.setString(2, String.valueOf(IDPLANILLA));
                    ps.setString(3, RellenarConCeros(6, String.valueOf(UIClientes.c.getIdCliente())));
                    ps.setString(4, RellenarConCeros(6, String.valueOf(vidvendedor)));
                    ps.setString(5, fecha);
                    ps.setString(6, fecha);
                    ps.setString(7, UIClientes.c.getRazonSocial());
                    ps.setString(8, vnombrevendedor);
                    ps.setFloat(9, total);
                    ps.execute();
                    ps.close();
                    conn.close();
                    retorno = "Se está guardando el presupuesto...";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                retorno = "Hubo un error al insertar comprobante_p_c";
            }
            return retorno;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s.equals("Se está guardando el presupuesto...")) {
                guardarPresupuestoXD(a, b, total, vidvendedor, vdeposito, PuntodeVenta, IDPLANILLA, NROPRESUP);
            } else {
                mostrarMensaje(a, s);
            }
        }
    }

    public void guardarPresupuestoXC(Activity a, Button b, Float total, int vidvendedor, int vdeposito, int PuntodeVenta, int IDPLANILLA, int NROPRESUP) {
        guardarPresupuestoXC = new GuardarPresupuestoXC(a, b, total, vidvendedor, vdeposito, PuntodeVenta, IDPLANILLA, NROPRESUP);
        guardarPresupuestoXC.execute();
    }


    private class GuardarPresupuestoXD extends AsyncTask<String, Float, String> {
        Activity a;
        Button b;
        Float total;
        int vidvendedor, vdeposito, PuntodeVenta, IDPLANILLA, NROPRESUP;

        public GuardarPresupuestoXD(Activity a, Button b, Float total, int vidvendedor, int vdeposito, int PuntodeVenta, int IDPLANILLA, int NROPRESUP) {
            this.a = a;
            this.b = b;
            this.total = total;
            this.vidvendedor = vidvendedor;
            this.vdeposito = vdeposito;
            this.PuntodeVenta = PuntodeVenta;
            this.IDPLANILLA = IDPLANILLA;
            this.NROPRESUP = NROPRESUP;
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(a);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Guardando presupuesto...");
            pDialog.setCancelable(false);
            pDialog.show();
            b.setTextAppearance(a, b_primary_disabled);
            Util util = new Util(a);
            b.setTypeface(util.getTypeface());
        }

        @Override
        protected String doInBackground(String... strings) {
            String retorno = "";
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            String consultaSql;
            try {
                conn = (Connection) Conexion.GetConnection(a);
                if (conn != null) {
                    mostrarMensajeLog(a, "se conecto con exito");
                    SQLiteDatabase db = BaseHelper.getInstance(a).getWritableDatabase();
                    Cursor c = db.rawQuery("SELECT * FROM PedidoLocal", null);
                    if (c.moveToFirst()) {
                        do {
                            consultaSql = "INSERT INTO `comprobantes_p_d` (NROCOMP,IDPRODUCTO,DESCRIPPROD,CANT,PUNIT,SUBTOT," +
                                    " DEPOSITO,PRODUCTO) VALUES " +
                                    "( ?, ?, ?, ?, ?, ?, ?, ?)";
                            ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                            ps.setString(1, (Util.RellenarConCeros(4, String.valueOf(PuntodeVenta)) +
                                    Util.RellenarConCeros(8, String.valueOf(NROPRESUP))));
                            ps.setString(2, c.getString(5));
                            ps.setString(3, c.getString(2));
                            ps.setFloat(4, c.getFloat(1));
                            ps.setFloat(5, c.getFloat(3));
                            ps.setFloat(6, c.getFloat(4));
                            ps.setInt(7, vdeposito);
                            ps.setString(8, Util.RellenarConCeros(6, String.valueOf(c.getInt(0))));
                            ps.execute();
                        } while (c.moveToNext());
                    }
                    consultaSql = "UPDATE config SET NROPRESUP =? WHERE NROPTOVTA = ?";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.setInt(1, NROPRESUP);
                    ps.setInt(2, PuntodeVenta);
                    ps.execute();
                    ////////////////////////////////////////////////////////////////////////////////
                    ps.close();
                    conn.close();
                    retorno = "Se guardó el presupuesto correctamente";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                retorno = "Hubo un error al insertar comprobante_p_d";
            }
            return retorno;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s.equals("Se guardó el presupuesto correctamente")) {
                b.setTextAppearance(a, b_primary);
                Util util = new Util(a);
                b.setTypeface(util.getTypeface());
                abrirActivity(a, UIPrincipal.class);
                mostrarMensaje(a, s);
            } else {
                mostrarMensaje(a, s);
            }
        }
    }

    public void guardarPresupuestoXD(Activity a, Button b, Float total, int vidvendedor, int vdeposito, int PuntodeVenta, int IDPLANILLA, int NROPRESUP) {
        guardarPresupuestoXD = new GuardarPresupuestoXD(a, b, total, vidvendedor, vdeposito, PuntodeVenta, IDPLANILLA, NROPRESUP);
        guardarPresupuestoXD.execute();
    }
}
