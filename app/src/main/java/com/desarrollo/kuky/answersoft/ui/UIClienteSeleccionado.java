package com.desarrollo.kuky.answersoft.ui;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.controlador.BaseHelper;
import com.desarrollo.kuky.answersoft.util.Util;

import static com.desarrollo.kuky.answersoft.controlador.BaseHelper.sqlTablaPedidoLocal;
import static com.desarrollo.kuky.answersoft.util.Util.abrirActivity;

public class UIClienteSeleccionado extends AppCompatActivity {

    private TextView etDireccionCliente, etTelefono, etTipoResponsabilidad, etCuit, tvTitulo;
    private Button bGenerarPresupuesto;

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
        bGenerarPresupuesto = (Button) findViewById(R.id.bGenerarPresupuesto);
        // SETEAMOS TYPEFACE
        etTelefono.setTypeface(util.getTypeface());
        etDireccionCliente.setTypeface(util.getTypeface());
        etTipoResponsabilidad.setTypeface(util.getTypeface());
        etCuit.setTypeface(util.getTypeface());
        bGenerarPresupuesto.setTypeface(util.getTypeface());
        // CARGAMOS LOS CAMPOS
        tvTitulo.setText(UIClientes.c.getRazonSocial());
        etTelefono.setText(UIClientes.c.getTelefono());
        etTipoResponsabilidad.setText(String.valueOf(UIClientes.c.getTipoResponsabilidad()));
        etDireccionCliente.setText(UIClientes.c.getDomicilio());
        etCuit.setText(String.valueOf(UIClientes.c.getNumeroCuit()));
        bGenerarPresupuesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = BaseHelper.getInstance(UIClienteSeleccionado.this).getWritableDatabase();
                db.execSQL("DROP TABLE PedidoLocal");
                db.execSQL(sqlTablaPedidoLocal);
                abrirActivity(UIClienteSeleccionado.this, UIPresupuesto.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        abrirActivity(this, UIClientes.class);
    }
}
