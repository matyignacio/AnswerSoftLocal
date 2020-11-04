package com.desarrollo.kuky.answersoft.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.desarrollo.kuky.answersoft.objetos.Cliente;
import com.desarrollo.kuky.answersoft.ui.UIClienteSeleccionado;
import com.desarrollo.kuky.answersoft.ui.adapters.lvaClientes;
import com.desarrollo.kuky.answersoft.util.Util;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by kuky on 15/10/16.
 */
public class ClienteControlador {
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private ProgressDialog pDialog;
    private ExtraerPorId extraerPorId;
    private BuscarPorDescripcion buscarPorDescripcion;
    private ExtraerTodos extraerTodos;

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
            pDialog.setMessage("Procesando clientes...");
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
            int cantidadRegistros, i = 0;
            try {
                conn = (Connection) Conexion.GetConnection(a);
                String consultaSql = "SELECT IDCLIENTE,RAZONSOC,DOMICILIO FROM clientes WHERE VER=1 ORDER BY RAZONSOC";
                ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                ps.execute();
                rs = ps.getResultSet();
                cantidadRegistros = Util.obtenerCantidadRegistros(rs);
                do {
                    Cliente cliente = new Cliente();
                    cliente.setRazonSocial(rs.getString(2));
                    cliente.setIdCliente(rs.getInt(1));
                    cliente.setDomicilio(rs.getString(3));
                    clientes.add(cliente);
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
                lvaClientes adaptador = new lvaClientes(a, clientes);
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

    public void extraerTodos(final Activity a, ListView l) {
        extraerTodos = new ExtraerTodos(a, l);
        extraerTodos.execute();
    }

    private class ExtraerPorId extends AsyncTask<String, Float, String> {
        Activity a;
        Cliente c;

        public ExtraerPorId(Activity a, Cliente c) {
            this.a = a;
            this.c = c;
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
            try {
                conn = (Connection) Conexion.GetConnection(a);
                String consultaSql = "SELECT TELEFONO, TIPORESP, NROCUIT FROM clientes WHERE IDCLIENTE=?";
                ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                ps.setInt(1, c.getIdCliente());
                ps.executeQuery();
                rs = ps.getResultSet();
                while (rs.next()) {
                    c.setTelefono(rs.getString(1));
                    c.setTipoResponsabilidad(rs.getString(2));
                    c.setNumeroCuit(rs.getString(3));
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
                Intent intent = new Intent(a, UIClienteSeleccionado.class);
                a.startActivity(intent);
            } else {
                Toast.makeText(a, s.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void extraerPorId(Activity a, Cliente c) {
        extraerPorId = new ExtraerPorId(a, c);
        extraerPorId.execute();
    }

    private class BuscarPorDescripcion extends AsyncTask<String, Float, String> {
        Activity a;
        ListView l;
        String razonSocial;

        public BuscarPorDescripcion(Activity a, ListView l, String razonSocial) {
            this.a = a;
            this.l = l;
            this.razonSocial = razonSocial;
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            clientes = new ArrayList<>();
            if (razonSocial.equals("")) {
                try {
                    conn = (Connection) Conexion.GetConnection(a);
                    String consultaSql = "SELECT IDCLIENTE,RAZONSOC,DOMICILIO FROM clientes WHERE VER=1 ORDER BY RAZONSOC";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.execute();
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        Cliente c = new Cliente();
                        c.setIdCliente(rs.getInt(1));
                        c.setRazonSocial(rs.getString(2));
                        c.setDomicilio(rs.getString(3));
                        clientes.add(c);
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
                    String consultaSql = "SELECT IDCLIENTE,RAZONSOC,DOMICILIO FROM clientes WHERE VER = 1 AND RAZONSOC like ? ORDER BY RAZONSOC";
                    ps = (PreparedStatement) conn.prepareStatement(consultaSql);
                    ps.setString(1, "%" + razonSocial + "%");
                    ps.executeQuery();
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        Cliente c = new Cliente();
                        c.setIdCliente(rs.getInt(1));
                        c.setRazonSocial(rs.getString(2));
                        c.setDomicilio(rs.getString(3));
                        clientes.add(c);
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
            if (s.toString().toString().equals("")) {
                lvaClientes adaptador = new lvaClientes(a, clientes);
                l.setAdapter(adaptador);
            } else {
                Toast.makeText(a, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void buscarPorRazonSocial(Activity a, ListView l, String descripcion) {
        buscarPorDescripcion = new BuscarPorDescripcion(a, l, descripcion);
        buscarPorDescripcion.execute();
    }


    public Cliente extraerDeLista(int posicion) {
        return clientes.get(posicion);
    }
}
