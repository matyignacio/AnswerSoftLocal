package com.desarrollo.kuky.answersoft.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.controlador.OfertasControlador;
import com.desarrollo.kuky.answersoft.controlador.ProductoControlador;
import com.desarrollo.kuky.answersoft.util.Util;

import static com.desarrollo.kuky.answersoft.util.Util.abrirActivity;

public class UIProductoSeleccionado extends AppCompatActivity {
    TextView tvTitulo;
    EditText etStock, etPrecioVenta, etCodigoBarra;
    Button bGuardar, bEtiqueta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util util = new Util(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_seleccionado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle(UIProductos.p.getDescripcion()); LO DEJO DE USAR PARA PERSONALIZAR EL TITULO
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // sacamos el titulo por defecto para usar nuestro tv
        // CAPTURAMOS LOS ELEMENTOS
        tvTitulo = (TextView) findViewById(R.id.tbProductoSeleccionado);
        etStock = (EditText) findViewById(R.id.etStock);
        etPrecioVenta = (EditText) findViewById(R.id.etPrecioVenta);
        etCodigoBarra = (EditText) findViewById(R.id.etCodigoBarra);
        bGuardar = (Button) findViewById(R.id.bGuardar);
        bEtiqueta = (Button) findViewById(R.id.bEtiqueta);
        // SETEAMOS TYPEFACE
        etStock.setTypeface(util.getTypeface());
        etCodigoBarra.setTypeface(util.getTypeface());
        etPrecioVenta.setTypeface(util.getTypeface());
        bGuardar.setTypeface(util.getTypeface());
        bEtiqueta.setTypeface(util.getTypeface());
        // CARGAMOS LOS CAMPOS
        float stock = 0, precioVenta = 0;
        stock = Util.RedondearFloat(UIProductos.p.getStock(), 2);
        tvTitulo.setText(UIProductos.p.getDescripcion());
        etStock.setText(String.valueOf(stock));
        etStock.setSelectAllOnFocus(true);
        precioVenta = UIProductos.p.getPrecioVenta();
        etPrecioVenta.setText(String.valueOf(Util.RedondearFloat(precioVenta, 2)));
        etPrecioVenta.setSelectAllOnFocus(true);
        etCodigoBarra.setText(UIProductos.p.getCodAlternativo());
        for (int i = 0; i < UIProductos.configaccs.size(); i++) {
            if (UIProductos.configaccs.get(i).getItemidh().equals(Util.NO_MODIFICA_PRECIO)) {
                etPrecioVenta.setEnabled(false);
            }
            if (UIProductos.configaccs.get(i).getItemidh().equals(Util.NO_MODIFICA_STOCK)) {
                etStock.setEnabled(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        abrirActivity(this, UIProductos.class);
    }

    public void Guardar(View view) {
        ProductoControlador pControlador = new ProductoControlador();
        if (etStock.getText().toString().equals("")) {
            Toast.makeText(this, "Debe llenar el campo Stock", Toast.LENGTH_SHORT).show();
        } else {
            if (etPrecioVenta.getText().toString().equals("")) {
                Toast.makeText(this, "Debe llenar el campo Precio de venta", Toast.LENGTH_SHORT).show();
            } else {
                UIProductos.p.setPrecioVenta(Float.parseFloat(etPrecioVenta.getText().toString()));
                UIProductos.p.setStock(Float.parseFloat(etStock.getText().toString()));
                if (!etCodigoBarra.getText().equals("")) {
                    UIProductos.p.setCodAlternativo(etCodigoBarra.getText().toString());
                }
                try {
                    pControlador.update(this, UIProductos.p, bGuardar);
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void InsertarEtiqueta(View view) {
        OfertasControlador oControlador = new OfertasControlador();
        if (etStock.getText().toString().equals("")) {
            Toast.makeText(this, "Debe llenar el campo Stock", Toast.LENGTH_SHORT).show();
        } else {
            try {
                oControlador.insertar(this, UIProductos.p, bEtiqueta);
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
