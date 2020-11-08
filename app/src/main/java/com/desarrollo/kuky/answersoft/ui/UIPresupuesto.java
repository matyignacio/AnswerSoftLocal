package com.desarrollo.kuky.answersoft.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.controlador.BaseHelper;
import com.desarrollo.kuky.answersoft.controlador.PresupuestoControlador;
import com.desarrollo.kuky.answersoft.controlador.ProductoControlador;
import com.desarrollo.kuky.answersoft.objetos.Presupuesto;
import com.desarrollo.kuky.answersoft.objetos.Producto;
import com.desarrollo.kuky.answersoft.ui.adapters.lvaPresupuestoLocal;
import com.desarrollo.kuky.answersoft.util.Util;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import static com.desarrollo.kuky.answersoft.controlador.BaseHelper.sqlTablaPedidoLocal;
import static com.desarrollo.kuky.answersoft.util.Util.PRODUCTO_SIN_CODIGO;
import static com.desarrollo.kuky.answersoft.util.Util.abrirActivity;
import static com.desarrollo.kuky.answersoft.util.Util.abrirActivityWithoutClose;
import static com.desarrollo.kuky.answersoft.util.Util.mostrarMensaje;
import static com.desarrollo.kuky.answersoft.util.Util.mostrarMensajeLog;

public class UIPresupuesto extends AppCompatActivity {
    float subTotal;
    float Total;
    String fecha;
    String[] arregloCantidad, arregloNombreProducto, arregloPrecio, arregloIdProducto;
    ListView listaItems;
    public static TextView tvPrecioProducto, tvdescProducto, tvTotal;
    public static EditText cantProducto, acListaProductos;
    public static int fromLista = 0;
    public static Producto producto;
    ArrayList<Presupuesto> presupuestos;
    Button listarProducto, bAgregar, bGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto);
        subTotal = 0;
        Total = 0;
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        acListaProductos = (EditText) findViewById(R.id.etCodigoProducto);
        tvPrecioProducto = (TextView) findViewById(R.id.tvPrecioProducto);
        tvdescProducto = (TextView) findViewById(R.id.tvdescProducto);
        cantProducto = (EditText) findViewById(R.id.etCantidad);
        listarProducto = (Button) findViewById(R.id.listarProductos);
        bGuardar = (Button) findViewById(R.id.bGuardar);
        bAgregar = (Button) findViewById(R.id.bAgregar);
        ////////////////////////////////////////////////////////////////////////////////////////////
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams_Bold.ttf");
        tvPrecioProducto.setTypeface(typeface);
        tvdescProducto.setTypeface(typeface);
        tvTotal.setTypeface(typeface);
        cantProducto.setTypeface(typeface);
        acListaProductos.setTypeface(typeface);
        bAgregar.setTypeface(typeface);
        bGuardar.setTypeface(typeface);
        listarProducto.setTypeface(typeface);
        ////////////////////////////////////////////////////////////////////////////////////////////
        listaItems = (ListView) findViewById(R.id.listaItems);
        acListaProductos.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!acListaProductos.getText().toString().equals("") &&
                            !PRODUCTO_SIN_CODIGO.equals(acListaProductos.getText().toString())
                            && fromLista == 0) {
                        producto = new Producto();
                        ProductoControlador productoControlador = new ProductoControlador();
                        productoControlador.extraerPorCodAltern(UIPresupuesto.this, acListaProductos.getText().toString());
                    }
                }
            }
        });
        /*ESTE ONCHANGE LO REHABILITO PARA PODER ASIGNAR EL FOCO A LA CANTIDAD DE PRODUCTOS*/
        tvdescProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                cantProducto.requestFocus();
            }
        });
        listarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActivityWithoutClose(UIPresupuesto.this, UIListaProductos.class);
            }
        });
        bGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.createCustomDialog(UIPresupuesto.this,
                        "¿Esta seguro que desea guardar el pedido?",
                        "",
                        "SI, GUARDAR",
                        "CANCELAR",
                        new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                int vidvendedor = 0, vdeposito = 0, PuntodeVenta = 0, IDPLANILLA = 0, NROPRESUP = 0;
                                if (Total != 0) {
                                    PresupuestoControlador presupuestoControlador = new PresupuestoControlador();
                                    presupuestoControlador.guardarPresupuestoXC(UIPresupuesto.this, bGuardar, Total,
                                            vidvendedor, vdeposito, PuntodeVenta, IDPLANILLA, NROPRESUP);
                                }
                                return null;
                            }
                        },
                        new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                return null;
                            }
                        }).show();
            }
        });
        seleccionarItem();
    }


    @Override
    public void onBackPressed() {
        final SQLiteDatabase db = BaseHelper.getInstance(this).getWritableDatabase();
        final Cursor c = db.rawQuery("SELECT * FROM PedidoLocal", null);
        if (c != null && c.getCount() != 0) {
            Util.createCustomDialog(this,
                    "¿Tiene productos pendientes, quiere abandonar el presupuesto?",
                    "",
                    "SI, SALIR",
                    "CANCELAR",
                    new Callable<Void>() {
                        @Override
                        public Void call() {
                            db.execSQL("DROP TABLE PedidoLocal");
                            db.execSQL(sqlTablaPedidoLocal);
                            c.close();
                            db.close();
                            abrirActivity(UIPresupuesto.this, UIClienteSeleccionado.class);
                            return null;
                        }
                    },
                    new Callable<Void>() {
                        @Override
                        public Void call() {
                            c.close();
                            db.close();
                            return null;
                        }
                    }).show();
        } else {
            abrirActivity(UIPresupuesto.this, UIClienteSeleccionado.class);
        }
    }

    public static void cargarVistas(Activity a) {
        try {
            // aca evaluo si el producto tiene codigo de barra. En caso de no tenerlo, se asigna un guion
            // al campo "codigo producto" para poder saltear la validacion y poder agregarlo al pedido
            if (producto.getIdProducto() > 0) {
                if (!producto.getCodAlternativo().equals("")) {
                    acListaProductos.setText(producto.getCodAlternativo());
                } else {
                    acListaProductos.setText(PRODUCTO_SIN_CODIGO);
                }
                tvdescProducto.setText(producto.getDescripcion());
                tvPrecioProducto.setText("$" + producto.getPrecioVenta());
                fromLista = 0;
            } else {
                acListaProductos.setText("");
                tvdescProducto.setText("");
                tvPrecioProducto.setText("");
                mostrarMensaje(a, "No existe un producto con ese codigo");
            }
        } catch (Exception e) {
            mostrarMensajeLog(a, e.toString() + " - No se pudo cargar la vista");
        }
    }


    public void agregarItem(View view) {
        float cantidadProductos;
        SQLiteDatabase db = BaseHelper.getInstance(this).getWritableDatabase();
        try {
            cantidadProductos = Float.parseFloat(cantProducto.getText().toString());
        } catch (Exception e) {
            cantidadProductos = 0;
        }
        if (!acListaProductos.getText().toString().equals("") &&
                !tvdescProducto.getText().toString().equals("") &&
                cantidadProductos > 0) {
            if (db != null) {
                subTotal = cantidadProductos * producto.getPrecioVenta();
                String sql = "INSERT INTO PedidoLocal (idP , cantidad , DESCRIPPROD2 ,CODALTERN, PUNIT2 ,SUBTOT2 ) VALUES" +
                        "(" + producto.getIdProducto() + "," +
                        cantidadProductos + ",'" +
                        producto.getDescripcion() + "','" +
                        producto.getCodAlternativo() + "'," +
                        producto.getPrecioVenta() + "," +
                        Util.RedondearFloat(subTotal, 2) + ")";
                db.execSQL(sql);
            }
            cargarItems();
            producto = new Producto(); // RESETEAMOS EL ID DEL PRODUCTO
        } else {
            mostrarMensaje(this, "El producto no existe");
        }
        db.close();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cantProducto.getWindowToken(), 0);
        cantProducto.setText("");
        acListaProductos.setText("");
        tvdescProducto.setText("");
        tvPrecioProducto.setText("$0");
        acListaProductos.requestFocus();
        seleccionarItem();
    }

    public void cargarItems() {
        presupuestos = new ArrayList<>();
        Presupuesto presupuesto;
        Total = 0;
        SQLiteDatabase db = BaseHelper.getInstance(this).getReadableDatabase();
        if (db != null) {
            try {
                Cursor c = db.rawQuery("select cantidad,DESCRIPPROD2,SUBTOT2,id from PedidoLocal", null);
                if (c.moveToFirst()) {
                    do {
                        presupuesto = new Presupuesto();
                        presupuesto.setCantidad(c.getFloat(0));
                        presupuesto.setNombre(c.getString(1));
                        presupuesto.setPrecio(c.getFloat(2));
                        presupuesto.setId(c.getInt(3));
                        presupuestos.add(presupuesto);
                        Total = Total + Util.RedondearFloat(c.getFloat(2), 2);
                    } while (c.moveToNext());
                }
                c.close();
            } catch (Exception e) {
                mostrarMensaje(this, "No se pudo cargar los items");
            }
            tvTotal.setText("Total \n$" + Util.RedondearFloat(Total, 2));
            lvaPresupuestoLocal adaptador = new lvaPresupuestoLocal(this, presupuestos);
            listaItems.setAdapter(adaptador);
        }
    }

    public void seleccionarItem() {
        listaItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //Agrego la logica para resaltar el renglon seleccionado ///////////////////////////
                view.setBackgroundResource(R.color.backgroundDisabled);
                ////////////////////////////////////////////////////////////////////////////////////
                view.setSelected(true);
                Util.createCustomDialog(UIPresupuesto.this,
                        "ATENCIÓN",
                        "¿Esta seguro que desea quitar " + presupuestos.get(position).getNombre() + "?",
                        "SI, ELIMINAR",
                        "CANCELAR",
                        new Callable<Void>() {
                            @Override
                            public Void call() {
                                ///////////////////////////////////////////////////////////////////////////////////////
                                SQLiteDatabase db = BaseHelper.getInstance(UIPresupuesto.this).getReadableDatabase();
                                int id = presupuestos.get(position).getId();
                                if (db != null) {
                                    long res = db.delete("PedidoLocal", "id=" + id, null);//BORRAR
                                    if (res > 0) {
                                        mostrarMensaje(UIPresupuesto.this, "Producto Eliminado");
                                        cargarItems();
                                    }
                                }
                                return null;
                            }
                        },
                        new Callable<Void>() {
                            @Override
                            public Void call() {
                                cargarItems();
                                return null;
                            }
                        }).show();
                return true;
            }
        });
    }
}