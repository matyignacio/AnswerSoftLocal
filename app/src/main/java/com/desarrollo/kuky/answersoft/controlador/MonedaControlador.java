package com.desarrollo.kuky.answersoft.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.desarrollo.kuky.answersoft.objetos.Moneda;
import com.desarrollo.kuky.answersoft.objetos.Producto;
import com.desarrollo.kuky.answersoft.ui.UIProductos;
import com.desarrollo.kuky.answersoft.ui.adapters.lvaProductos;
import com.desarrollo.kuky.answersoft.util.Util;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Kuky on 18/04/2017.
 */

public class MonedaControlador {
    MonedaPorCodigo monedaPorCodigo;
    MonedaPorCodigoPorDescripcion monedaPorCodigoPorDescripcion;

    private class MonedaPorCodigo extends AsyncTask<String, Float, String> {
        Activity a;
        ListView l;
        ProgressDialog pDialog;
        ArrayList<Producto> productos;
        String stringReturn = "";

        @Override
        protected void onPreExecute() {
        }

        public MonedaPorCodigo(Activity a, ListView l, ProgressDialog pDialog, ArrayList<Producto> productos) {
            this.a = a;
            this.l = l;
            this.pDialog = pDialog;
            this.productos = productos;
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            for (int i = 0; i < productos.size(); i++) {
                UIProductos.moneda = new Moneda();
                try {
                    conn = (Connection) Conexion.GetConnection(a);
                    String consultaSql = "SELECT VALOR FROM moneda WHERE CODIGO=?";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.setFloat(1, productos.get(i).getMoneda());
                    ps.execute();
                    rs = ps.getResultSet();
                    if (rs.next()) {
                        UIProductos.moneda.setCodigo(productos.get(i).getIdProducto());
                        UIProductos.moneda.setValor(rs.getFloat(1));
                    } else {
                        UIProductos.moneda.setCodigo(0);
                    }
                    rs.close();
                    ps.close();
                    conn.close();
                    if (UIProductos.moneda.getCodigo() == 0) {
                        stringReturn = "No se pudo encontrar la moneda";
                    } else {
                        productos.get(i).setPrecioVenta(Util.RedondearFloat(productos.get(i).getPrecioVenta() * UIProductos.moneda.getValor(), 2));
                        stringReturn = "";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    stringReturn = e.toString();
                }
            }
            return stringReturn;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s.equals("")) {
                lvaProductos adaptador = new lvaProductos(a, productos);
                l.setAdapter(adaptador);
            } else {
                Toast.makeText(a, s.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void MonedaPorCodigo(Activity a, ListView l, ProgressDialog pDialog, ArrayList<Producto> productos) {
        monedaPorCodigo = new MonedaPorCodigo(a, l, pDialog, productos);
        monedaPorCodigo.execute();
    }

    private class MonedaPorCodigoPorDescripcion extends AsyncTask<String, Float, String> {
        Activity a;
        ListView l;
        ArrayList<Producto> productos;
        String stringReturn = "";

        @Override
        protected void onPreExecute() {
        }

        public MonedaPorCodigoPorDescripcion(Activity a, ListView l, ArrayList<Producto> productos) {
            this.a = a;
            this.l = l;
            this.productos = productos;
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn;
            PreparedStatement psMoneda;
            ResultSet rsmoneda;
            for (int i = 0; i < productos.size(); i++) {
                UIProductos.moneda = new Moneda();
                try {
                    conn = (Connection) Conexion.GetConnection(a);
                    String consultaSqlMoneda = "SELECT VALOR FROM moneda WHERE CODIGO=?";
                    psMoneda = (PreparedStatement) conn.prepareStatement(consultaSqlMoneda);
                    psMoneda.setFloat(1, productos.get(i).getMoneda());
                    psMoneda.execute();
                    rsmoneda = psMoneda.getResultSet();
                    if (rsmoneda.next()) {
                        UIProductos.moneda.setCodigo(productos.get(i).getIdProducto());
                        UIProductos.moneda.setValor(rsmoneda.getFloat(1));
                    } else {
                        UIProductos.moneda.setCodigo(0);
                    }
                    rsmoneda.close();
                    psMoneda.close();
                    conn.close();
                    if (UIProductos.moneda.getCodigo() == 0) {
                        stringReturn = "No se pudo encontrar la moneda";
                    } else {
                        productos.get(i).setPrecioVenta(Util.RedondearFloat(productos.get(i).getPrecioVenta() * UIProductos.moneda.getValor(), 2));
                        stringReturn = "";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    stringReturn = e.toString();
                }
            }
            return stringReturn;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("")) {
                lvaProductos adaptador = new lvaProductos(a, productos);
                l.setAdapter(adaptador);
            } else {
                Toast.makeText(a, s.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void MonedaPorCodigoPorDescripcion(Activity a, ListView l, ArrayList<Producto> productos) {
        monedaPorCodigoPorDescripcion = new MonedaPorCodigoPorDescripcion(a, l, productos);
        monedaPorCodigoPorDescripcion.execute();
    }


}
