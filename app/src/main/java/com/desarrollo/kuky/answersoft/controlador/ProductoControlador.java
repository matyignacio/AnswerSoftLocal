package com.desarrollo.kuky.answersoft.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.desarrollo.kuky.answersoft.objetos.Moneda;
import com.desarrollo.kuky.answersoft.objetos.Producto;
import com.desarrollo.kuky.answersoft.ui.UIClientes;
import com.desarrollo.kuky.answersoft.ui.UIProductoSeleccionado;
import com.desarrollo.kuky.answersoft.ui.UIProductos;
import com.desarrollo.kuky.answersoft.ui.adapters.lvaListadoProductos;
import com.desarrollo.kuky.answersoft.ui.adapters.lvaProductos;
import com.desarrollo.kuky.answersoft.util.Util;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.desarrollo.kuky.answersoft.R.style.b_primary;
import static com.desarrollo.kuky.answersoft.R.style.b_primary_disabled;
import static com.desarrollo.kuky.answersoft.ui.UIPresupuesto.cargarVistas;
import static com.desarrollo.kuky.answersoft.ui.UIPresupuesto.producto;
import static com.desarrollo.kuky.answersoft.util.Util.abrirActivity;
import static com.desarrollo.kuky.answersoft.util.Util.mostrarMensaje;

/**
 * Created by kuky on 19/10/16.
 */
public class ProductoControlador {
    private ProgressDialog pDialog;
    private ExtraerTodos extraerTodos;
    private Update update;
    private BuscarPorDescripcion buscarPorDescripcion;
    private BuscarPorDescripcionPresupuesto buscarPorDescripcionPresupuesto;
    private ExtraerPorId extraerPorId;
    private Extraer extraer;
    private ExtraerPorCodAltern extraerPorCodAltern;
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
            String retorno = "No se pudo conectar a la dase de datos";
            int cantidadRegistros, i = 0, limit = 1000;
            try {
                ////////////////////////////////////////////////////////////////////////////////////
                SQLiteDatabase db = BaseHelper.getInstance(a).getReadableDatabase();
                Cursor c = db.rawQuery("SELECT limite_productos FROM parametros", null);
                if (c.moveToFirst()) {
                    limit = c.getInt(0);
                }
                c.close();
                db.close();
                ////////////////////////////////////////////////////////////////////////////////////
                conn = (Connection) Conexion.GetConnection(a);
                if (conn != null) {
                    String consultaSql = "select p.IDPRODUCTO, p.DESCRIP, (p.PRECVENTA*m.VALOR), p.CODALTERN FROM producto_0 p, moneda m WHERE p.PRECIOS=m.CODIGO AND p.VER=1 ORDER BY p.DESCRIP LIMIT ?";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.setInt(1, limit);
                    ps.executeQuery();
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
                lvaProductos adaptador = new lvaProductos(a, productos);
                l.setAdapter(adaptador);
            } else {
                mostrarMensaje(a, s);
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
            String retorno = "No se pudo conectar a la dase de datos";
            try {
                conn = (Connection) Conexion.GetConnection(a);
                if (conn != null) {
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
                    retorno = "";
                }
                return retorno;
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error de conexion";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s.equals("")) {
                abrirActivity(a, UIProductoSeleccionado.class);
            } else {
                mostrarMensaje(a, s);
            }
        }
    }

    public void extraerPorId(Activity a, Producto p) {
        extraerPorId = new ExtraerPorId(a, p);
        extraerPorId.execute();
    }


    private class Extraer extends AsyncTask<String, Float, String> {
        Activity a;
        Producto p;
        String codLista;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(a);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Estableciendo conexion...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        public Extraer(Activity a, Producto p, String codLista) {
            this.a = a;
            this.p = p;
            this.codLista = codLista;
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
                    if (codLista.equals("0000")) {
                        String consultaSql = "SELECT STOCK,DESCRIP,PRECVENTA,CODALTERN FROM producto_0 WHERE IDPRODUCTO=?";
                        ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                        ps.setInt(1, producto.getIdProducto());
                        ps.executeQuery();
                        rs = ps.getResultSet();
                        while (rs.next()) {
                            producto.setStock(rs.getFloat(1));
                            producto.setDescripcion(rs.getString(2));
                            producto.setPrecioVenta(rs.getFloat(3));
                            producto.setCodAlternativo(rs.getString(4));
                        }
                    } else {
                        String listaCodigo = "LIST_" + codLista;
                        String consultaSql = "SELECT STOCK,DESCRIP," + listaCodigo + ",CODALTERN FROM producto_0 WHERE IDPRODUCTO=?";
                        ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                        ps.setInt(1, producto.getIdProducto());
                        ps.executeQuery();
                        rs = ps.getResultSet();
                        while (rs.next()) {
                            producto.setStock(rs.getFloat(1));
                            producto.setDescripcion(rs.getString(2));
                            producto.setPrecioVenta(rs.getFloat(3));
                            producto.setCodAlternativo(rs.getString(4));
                        }
                    }
                    rs.close();
                    ps.close();
                    conn.close();
                    retorno = "";
                }
                return retorno;
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error de conexion";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s.equals("")) {
                cargarVistas(a);
            } else {
                mostrarMensaje(a, s);
            }
        }
    }

    public void extraer(Activity a, Producto p, String codLista) {
        extraer = new Extraer(a, p, codLista);
        extraer.execute();
    }


    private class ExtraerPorCodAltern extends AsyncTask<String, Float, String> {
        Activity a;
        String codAltern;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(a);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Estableciendo conexion...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        public ExtraerPorCodAltern(Activity a, String codAltern) {
            this.a = a;
            this.codAltern = codAltern;
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            String retorno = "No se pudo conectar a la dase de datos";
            producto = new Producto();
            try {
                conn = (Connection) Conexion.GetConnection(a);
                if (conn != null) {
                    String consultaSql = "select IDPRODUCTO from producto_0 where CODALTERN like ?";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.setString(1, codAltern);
                    ps.executeQuery();
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        producto.setIdProducto(rs.getInt(1));
                    }
                    rs.close();
                    ps.close();
                    conn.close();
                    retorno = "";
                }
                return retorno;
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error de conexion";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s.equals("")) {
                extraer(a, producto, UIClientes.c.getCodLista());
            } else {
                mostrarMensaje(a, s);
            }
        }
    }

    public void extraerPorCodAltern(Activity a, String codAltern) {
        extraerPorCodAltern = new ExtraerPorCodAltern(a, codAltern);
        extraerPorCodAltern.execute();
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
            regreso = "No se pudo conectar a la dase de datos";
            try {
                conn = (Connection) Conexion.GetConnection(a);
                if (conn != null) {
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
                abrirActivity(a, UIProductoSeleccionado.class);
            } else {
                mostrarMensaje(a, s);
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
            b.setTextAppearance(a, b_primary_disabled);
            Util util = new Util(a);
            b.setTypeface(util.getTypeface());
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
                    retorno = "Se actualizo el producto correctamente";
                }
                return retorno;
            } catch (SQLException e) {
                e.printStackTrace();
                return "Hubo un error en la actualizacion de producto";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            mostrarMensaje(a, s);
            b.setTextAppearance(a, b_primary);
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
            String retorno = "No se pudo conectar a la dase de datos";
            int limit = 10000;
            productos = new ArrayList<>();
            ////////////////////////////////////////////////////////////////////////////////////
            SQLiteDatabase db = BaseHelper.getInstance(a).getReadableDatabase();
            Cursor c = db.rawQuery("SELECT limite_productos FROM parametros", null);
            if (c.moveToFirst()) {
                limit = c.getInt(0);
            }
            c.close();
            db.close();
            ////////////////////////////////////////////////////////////////////////////////////
            if (descripcion.equals("")) {
                try {
                    conn = (Connection) Conexion.GetConnection(a);
                    if (conn != null) {
                        String consultaSql = "select p.IDPRODUCTO, p.DESCRIP, (p.PRECVENTA*m.VALOR), p.CODALTERN FROM producto_0 p, moneda m WHERE p.PRECIOS=m.CODIGO AND p.VER=1 ORDER BY p.DESCRIP LIMIT ?";
                        ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                        ps.setInt(1, limit);
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
                        retorno = "";
                    }
                    return retorno;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            } else {
                try {
                    conn = (Connection) Conexion.GetConnection(a);
                    if (conn != null) {
                        String consultaSql = "select p.IDPRODUCTO, p.DESCRIP, (p.PRECVENTA*m.VALOR), p.CODALTERN FROM producto_0 p, moneda m WHERE p.PRECIOS=m.CODIGO AND p.VER=1 AND p.DESCRIP like ? ORDER BY p.DESCRIP LIMIT ?";
                        ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                        ps.setString(1, "%" + descripcion + "%");
                        ps.setInt(2, limit);
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
                        retorno = "";
                    }
                    return retorno;
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
                mostrarMensaje(a, s);
            }
        }
    }

    public void buscarPorDescripcion(Activity a, ListView l, String descripcion) {
        buscarPorDescripcion = new BuscarPorDescripcion(a, l, descripcion);
        buscarPorDescripcion.execute();
    }


    private class BuscarPorDescripcionPresupuesto extends AsyncTask<String, Float, String> {
        Activity a;
        ListView l;
        String descripcion;

        public BuscarPorDescripcionPresupuesto(Activity a, ListView l, String descripcion) {
            this.a = a;
            this.l = l;
            this.descripcion = descripcion;
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            String retorno = "No se pudo conectar a la dase de datos";
            int limit = 10000;
            productos = new ArrayList<>();
            ////////////////////////////////////////////////////////////////////////////////////
            SQLiteDatabase db = BaseHelper.getInstance(a).getReadableDatabase();
            Cursor c = db.rawQuery("SELECT limite_productos FROM parametros", null);
            if (c.moveToFirst()) {
                limit = c.getInt(0);
            }
            c.close();
            db.close();
            ////////////////////////////////////////////////////////////////////////////////////
            if (descripcion.equals("")) {
                try {
                    conn = (Connection) Conexion.GetConnection(a);
                    if (conn != null) {
                        String consultaSql = "select p.IDPRODUCTO, p.DESCRIP, (p.PRECVENTA*m.VALOR), p.CODALTERN FROM producto_0 p, moneda m WHERE p.PRECIOS=m.CODIGO AND p.VER=1 ORDER BY p.DESCRIP LIMIT ?";
                        ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                        ps.setInt(1, limit);
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
                        retorno = "";
                    }
                    return retorno;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            } else {
                try {
                    conn = (Connection) Conexion.GetConnection(a);
                    if (conn != null) {

                        String consultaSql = "select p.IDPRODUCTO, p.DESCRIP, (p.PRECVENTA*m.VALOR), p.CODALTERN FROM producto_0 p, moneda m WHERE p.PRECIOS=m.CODIGO AND p.VER=1 AND p.DESCRIP like ? ORDER BY p.DESCRIP LIMIT ?";
                        ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                        ps.setString(1, "%" + descripcion + "%");
                        ps.setInt(2, limit);
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
                        retorno = "";
                    }
                    return retorno;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("")) {
                lvaListadoProductos adaptador = new lvaListadoProductos(a, productos);
                l.setAdapter(adaptador);
            } else {
                mostrarMensaje(a, s);
            }
        }
    }

    public void buscarPorDescripcionPresupuesto(Activity a, ListView l, String descripcion) {
        buscarPorDescripcionPresupuesto = new BuscarPorDescripcionPresupuesto(a, l, descripcion);
        buscarPorDescripcionPresupuesto.execute();
    }


    public Producto extraerDeLista(int posicion) {
        return productos.get(posicion);
    }

}
