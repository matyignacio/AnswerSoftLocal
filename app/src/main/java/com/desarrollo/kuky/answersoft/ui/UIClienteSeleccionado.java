package com.desarrollo.kuky.answersoft.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.util.Util;

public class UIClienteSeleccionado extends AppCompatActivity {

    private TextView etDireccionCliente, etTelefono, etTipoResponsabilidad, etCuit, tvTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util util = new Util(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_seleccionado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProductos);
        //toolbar.setTitle(UIClientes.c.toString()); LO DEJO DE USAR PARA PERSONALIZAR EL TITULO
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // sacamos el titulo por defecto para usar nuestro tv
        // CAPTURAMOS LOS ELEMENTOS
        tvTitulo = (TextView) findViewById(R.id.tbClienteSeleccionado);
        etDireccionCliente = (TextView) findViewById(R.id.etDireccionCliente);
        etTelefono = (TextView) findViewById(R.id.etTelefonoCliente);
        etTipoResponsabilidad = (TextView) findViewById(R.id.etTipoResponsabilidad);
        etCuit = (TextView) findViewById(R.id.etCuit);
        // SETEAMOS TYPEFACE
        etTelefono.setTypeface(util.getTypeface());
        etDireccionCliente.setTypeface(util.getTypeface());
        etTipoResponsabilidad.setTypeface(util.getTypeface());
        etCuit.setTypeface(util.getTypeface());
        // CARGAMOS LOS CAMPOS
        tvTitulo.setText(UIClientes.c.getRazonSocial());
        etTelefono.setText(UIClientes.c.getTelefono());
        etTipoResponsabilidad.setText(String.valueOf(UIClientes.c.getTipoResponsabilidad()));
        etDireccionCliente.setText(UIClientes.c.getDomicilio());
        etCuit.setText(String.valueOf(UIClientes.c.getNumeroCuit()));
    }
}
