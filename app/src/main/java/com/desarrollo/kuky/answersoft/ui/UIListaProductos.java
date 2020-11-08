package com.desarrollo.kuky.answersoft.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.controlador.ProductoControlador;
import com.desarrollo.kuky.answersoft.objetos.Producto;

import static com.desarrollo.kuky.answersoft.ui.UIPresupuesto.producto;

public class UIListaProductos extends AppCompatActivity {
    private ListView listaProductos;
    private EditText buscarProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);
        // CAPTURAMOS LOS ELEMENTOS
        listaProductos = (ListView) findViewById(R.id.listaProductos);
        buscarProducto = (EditText) findViewById(R.id.etBuscarProducto);
        ////////////////////////////////////////////////////////////////
        listaProductos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ////////////////////////////////////////////////////////////////
        producto = new Producto();
        cargarProductos();
        buscarProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                cargarProductos();
            }
        });
    }

    @Override
    public void onBackPressed() {
        UIPresupuesto.fromLista = 0;
        this.finish();
    }

    public void cargarProductos() {
        final ProductoControlador productoControlador = new ProductoControlador();
        productoControlador.buscarPorDescripcionPresupuesto(UIListaProductos.this, listaProductos, buscarProducto.getText().toString());
        listaProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UIPresupuesto.fromLista = 1;
                producto = productoControlador.extraerDeLista(position);
                productoControlador.extraer(UIListaProductos.this, producto, UIClientes.c.getCodLista());
                UIListaProductos.this.finish();
            }
        });

    }
}