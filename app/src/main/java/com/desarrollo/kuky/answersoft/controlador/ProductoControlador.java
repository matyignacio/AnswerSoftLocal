package com.desarrollo.kuky.answersoft.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.desarrollo.kuky.answersoft.objetos.Moneda;
import com.desarrollo.kuky.answersoft.objetos.Producto;
import com.desarrollo.kuky.answersoft.ui.UIProductoSeleccionado;
import com.desarrollo.kuky.answersoft.ui.UIProductos;
import com.desarrollo.kuky.answersoft.ui.adapters.lvaProductos;
import com.desarrollo.kuky.answersoft.util.Util;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.desarrollo.kuky.answersoft.R.style.botonRojoBlanco;
import static com.desarrollo.kuky.answersoft.R.style.botonRojoNegro;

/**
 * Created by kuky on 19/10/16.
 */
public class ProductoControlador {
    private ProgressDialog pDialog;
    private ExtraerTodos extraerTodos;
    private Update update;
    private BuscarPorDescripcion buscarPorDescripcion;
    private ExtraerPorId extraerPorId;
    private ExtraerPorCodBarra extraerPorCodBarra;
    private ArrayList<Producto> productos = new ArrayList<>();

    private class ExtraerTodos extends AsyncTask<String, Float, String> {
        Activity a;
        ListView l;

        public ExtraerTodos(Activity a, ListView l) {
            this.a = a;
            this.l = l;
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(a);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setMessage("Procesando productos...");
            pDialog.setProgressNumberFormat(null);
            pDialog.setProgressPercentFormat(null);
            pDialog.setCancelable(true);
            pDialog.setMax(100);
            pDialog.show();
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    ExtraerTodos.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            PreparedStatement psMoneda;
            ResultSet rsmoneda;
            int cantidadRegistros, i = 0;
            try {
                conn = (Connection) Conexion.GetConnection(a);
                String consultaSql = "select p.IDPRODUCTO, p.DESCRIP, (p.PRECVENTA*m.VALOR), p.CODALTERN FROM producto_0 p, moneda m WHERE p.PRECIOS=m.CODIGO AND p.VER=1 ORDER BY p.DESCRIP";
                ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                ps.execute();
                rs = ps.getResultSet();
                cantidadRegistros = Util.obtenerCantidadRegistros(rs);
                do {
                    Producto p = new Producto();
                    UIProductos.moneda = new Moneda();
                    p.setIdProducto(rs.getInt(1));
                    p.setDescripcion(rs.getString(2));
                    p.setPrecioVenta(rs.getFloat(3));
                    p.setCodAlternativo(rs.getString(4));
                    productos.add(p);
                    i++;
                    publishProgress((float) (i * 100 / cantidadRegistros));
                } while (rs.next());
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
                lvaProductos adaptador = new lvaProductos(a, productos);
                l.setAdapter(adaptador);
            } else {
                Toast.makeText(a, s, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            int progreso = (int) values[0].floatValue();
            pDialog.setProgress(progreso);
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(a, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
        }


    }

    public void extraerTodos(Activity a, ListView l) {
        extraerTodos = new ExtraerTodos(a, l);
        extraerTodos.execute();
    }


    private class ExtraerPorId extends AsyncTask<String, Float, String> {
        Activity a;
        Producto p;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(a);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Estableciendo conexion...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        public ExtraerPorId(Activity a, Producto p) {
            this.a = a;
            this.p = p;
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            try {
                conn = (Connection) Conexion.GetConnection(a);
                String consultaSql = "SELECT STOCK FROM producto_0 WHERE IDPRODUCTO=?";
                ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                ps.setInt(1, p.getIdProducto());
                ps.executeQuery();
                rs = ps.getResultSet();
                while (rs.next()) {
                    p.setStock(rs.getFloat(1));
                }
                rs.close();
                ps.close();
                conn.close();
                return "";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error de conexion";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s.equals("")) {
                Intent intent = new Intent(a, UIProductoSeleccionado.class);
                a.startActivity(intent);
            } else {
                Toast.makeText(a, s.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void extraerPorId(Activity a, Producto p) {
        extraerPorId = new ExtraerPorId(a, p);
        extraerPorId.execute();
    }

    private class ExtraerPorCodBarra extends AsyncTask<String, Float, String> {
        Activity a;
        Producto p;
        String codBarra;
        String regreso = "";

        public ExtraerPorCodBarra(Activity a, Producto p, String codBarra) {
            this.a = a;
            this.p = p;
            this.codBarra = codBarra;
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            try {
                conn = (Connection) Conexion.GetConnection(a);
                String consultaSql = "SELECT p.IDPRODUCTO, p.DESCRIP, (p.PRECVENTA*m.VALOR), p.STOCK FROM producto_0 p, moneda m WHERE p.PRECIOS=m.CODIGO AND CODALTERN like ? ";
                ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                ps.setString(1, "%" + codBarra + "%");
                ps.executeQuery();
                rs = ps.getResultSet();
                int cantidadRegistros = Util.obtenerCantidadRegistros(rs);
                if (cantidadRegistros > 0) {
                    do {
                        p = new Producto();
                        p.setIdProducto(rs.getInt(1));
                        p.setDescripcion(rs.getString(2));
                        p.setPrecioVenta(rs.getFloat(3));
                        p.setStock(rs.getFloat(4));
                        p.setCodAlternativo(codBarra);
                    } while (rs.next());
                    rs.close();
                    ps.close();
                    conn.close();
                    regreso = "";
                } else {
                    regreso = "No se encontro ningun producto con ese codigo";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                regreso = "Error de conexion";
            }
            return regreso;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("")) {
                UIProductos.p = p;
                Intent intent = new Intent(a, UIProductoSeleccionado.class);
                a.startActivity(intent);
            } else {
                Toast.makeText(a, s.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void extraerPorCodBarra(Activity a, Producto p, String codBarra) {
        extraerPorCodBarra = new ExtraerPorCodBarra(a, p, codBarra);
        extraerPorCodBarra.execute();
    }

    private class Update extends AsyncTask<String, Float, String> {
        Activity a;
        Producto p;
        Button b;

        public Update(Activity a, Producto p, Button b) {
            this.a = a;
            this.p = p;
            this.b = b;
        }

        @Override
        protected void onPreExecute() {
            b.setTextAppearance(a, botonRojoNegro);
            Util util = new Util(a);
            b.setTypeface(util.getTypeface());
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn;
            PreparedStatement ps;
            try {
                conn = (Connection) Conexion.GetConnection(a);
                Log.d("Conectado", "se conecto con exito");
                String consultaSql = "UPDATE producto_0 p " +
                        "LEFT JOIN moneda m ON p.PRECIOS = m.CODIGO " +
                        "SET p.STOCK=?, p.CODALTERN=?,p.PRECVENTA = FORMAT(? / m.VALOR ,4) " +
                        "WHERE IDPRODUCTO=?";
                ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                ps.setFloat(1, p.getStock());
                ps.setString(2, p.getCodAlternativo());
                ps.setFloat(3, p.getPrecioVenta());
                ps.setInt(4, p.getIdProducto());
                ps.executeUpdate();
                ps.close();
                conn.close();
                return "Se actualizo el producto correctamente";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Hubo un error en la actualizacion de producto";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(a, s, Toast.LENGTH_SHORT).show();
            b.setTextAppearance(a, botonRojoBlanco);
            Util util = new Util(a);
            b.setTypeface(util.getTypeface());
        }
    }

    public void update(Activity a, Producto p, Button b) {
        update = new Update(a, p, b);
        update.execute();
    }

    private class BuscarPorDescripcion extends AsyncTask<String, Float, String> {
        Activity a;
        ListView l;
        String descripcion;

        public BuscarPorDescripcion(Activity a, ListView l, String descripcion) {
            this.a = a;
            this.l = l;
            this.descripcion = descripcion;
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            PreparedStatement psMoneda;
            ResultSet rsmoneda;
            productos = new ArrayList<>();
            if (descripcion.equals("")) {
                try {
                    conn = (Connection) Conexion.GetConnection(a);
                    String consultaSql = "select p.IDPRODUCTO, p.DESCRIP, (p.PRECVENTA*m.VALOR), p.CODALTERN FROM producto_0 p, moneda m WHERE p.PRECIOS=m.CODIGO AND p.VER=1 ORDER BY p.DESCRIP";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.execute();
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        Producto p = new Producto();
                        UIProductos.moneda = new Moneda();
                        p.setIdProducto(rs.getInt(1));
                        p.setDescripcion(rs.getString(2));
                        p.setPrecioVenta(rs.getFloat(3));
                        p.setCodAlternativo(rs.getString(4));
                        productos.add(p);
                    }
                    rs.close();
                    ps.close();
                    conn.close();
                    return "";
                } catch (SQLException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            } else {
                try {
                    conn = (Connection) Conexion.GetConnection(a);
                    String consultaSql = "select p.IDPRODUCTO, p.DESCRIP, (p.PRECVENTA*m.VALOR), p.CODALTERN FROM producto_0 p, moneda m WHERE p.PRECIOS=m.CODIGO AND p.VER=1 AND p.DESCRIP like ? ORDER BY p.DESCRIP";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.setString(1, "%" + descripcion + "%");
                    ps.executeQuery();
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        Producto p = new Producto();
                        UIProductos.moneda = new Moneda();
                        p.setIdProducto(rs.getInt(1));
                        p.setDescripcion(rs.getString(2));
                        p.setPrecioVenta(rs.getFloat(3));
                        p.setCodAlternativo(rs.getString(4));
                        productos.add(p);
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
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("")) {
                lvaProductos adaptador = new lvaProductos(a, productos);
                l.setAdapter(adaptador);
            } else {
                Toast.makeText(a, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void buscarPorDescripcion(Activity a, ListView l, String descripcion) {
        buscarPorDescripcion = new BuscarPorDescripcion(a, l, descripcion);
        buscarPorDescripcion.execute();
    }


    public Producto extraerDeLista(int posicion) {
        return productos.get(posicion);
    }

}
