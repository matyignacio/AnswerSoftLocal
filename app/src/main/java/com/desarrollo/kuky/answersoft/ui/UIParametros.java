package com.desarrollo.kuky.answersoft.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.controlador.BaseHelper;
import com.desarrollo.kuky.answersoft.util.Util;

import java.util.ArrayList;

import static com.desarrollo.kuky.answersoft.util.Util.EXITOSO;
import static com.desarrollo.kuky.answersoft.util.Util.abrirActivity;
import static com.desarrollo.kuky.answersoft.util.Util.mostrarMensaje;
import static com.desarrollo.kuky.answersoft.util.Util.validarCampos;

public class UIParametros extends AppCompatActivity {
    TextView tvTitulo;
    EditText etVendedor, etPuntoVenta, etLimiteClientes, etLimiteProductos, etLimitePresupuestos;
    Button bAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametros);
        // CAPTURAMOS LOS ELEMENTOS
        tvTitulo = (TextView) findViewById(R.id.tvTitulo);
        etVendedor = (EditText) findViewById(R.id.etVendedor);
        etPuntoVenta = (EditText) findViewById(R.id.etPuntoVenta);
        etLimiteProductos = (EditText) findViewById(R.id.etLimiteProductos);
        etLimitePresupuestos = (EditText) findViewById(R.id.etLimitePresupuestos);
        etLimiteClientes = (EditText) findViewById(R.id.etLimiteClientes);
        bAceptar = (Button) findViewById(R.id.bAceptar);
        // SETEAMOS TYPEFACE
        Util util = new Util(this);
        tvTitulo.setTypeface(util.getTypeface());
        etVendedor.setTypeface(util.getTypeface());
        etPuntoVenta.setTypeface(util.getTypeface());
        etLimiteProductos.setTypeface(util.getTypeface());
        etLimitePresupuestos.setTypeface(util.getTypeface());
        etLimiteClientes.setTypeface(util.getTypeface());
        bAceptar.setTypeface(util.getTypeface());
        // CARGAMOS LOS CAMPOS
        SQLiteDatabase db = BaseHelper.getInstance(this).getReadableDatabase();
        try {
            Cursor c = db.rawQuery("SELECT * FROM parametros", null);
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    etVendedor.setText(c.getString(0));
                    etPuntoVenta.setText(c.getString(1));
                    etLimiteClientes.setText(c.getString(2));
                    etLimiteProductos.setText(c.getString(3));
                    etLimitePresupuestos.setText(c.getString(4));
                }
            }
            c.close();
        } catch (Exception e) {
            mostrarMensaje(this, e.toString());
        }
        db.close();
    }

    @Override
    public void onBackPressed() {
        abrirActivity(this, UIPrincipal.class);
    }

    public void guardarParametros(View view) {
        ArrayList<EditText> inputs = new ArrayList<>();
        inputs.add(etVendedor);
        inputs.add(etPuntoVenta);
        inputs.add(etLimiteClientes);
        inputs.add(etLimiteProductos);
        inputs.add(etLimitePresupuestos);
        if (validarCampos(this, inputs) == EXITOSO) {
            SQLiteDatabase db = BaseHelper.getInstance(this).getWritableDatabase();
            db.execSQL("DROP TABLE parametros");
            db.execSQL(BaseHelper.getInstance(this).getSqlTablaParametros());
            String sql = "INSERT INTO parametros VALUES " +
                    "('" + etVendedor.getText() +
                    "','" + etPuntoVenta.getText() +
                    "'," + etLimiteClientes.getText() +
                    "," + etLimiteProductos.getText() +
                    "," + etLimitePresupuestos.getText() +
                    ")";
            db.execSQL(sql);
            db.close();
            mostrarMensaje(this, "Parametros guardados con exito!");
            abrirActivity(this, UIPrincipal.class);
        }
    }
}