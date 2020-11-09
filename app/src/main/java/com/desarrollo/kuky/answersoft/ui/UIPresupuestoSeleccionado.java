package com.desarrollo.kuky.answersoft.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.objetos.ComprobantePD;
import com.desarrollo.kuky.answersoft.ui.adapters.lvaPresupuestoRealizado;
import com.desarrollo.kuky.answersoft.util.Util;

import java.util.ArrayList;

import static com.desarrollo.kuky.answersoft.util.Util.abrirActivity;

public class UIPresupuestoSeleccionado extends AppCompatActivity {
    public static ArrayList<ComprobantePD> comprobantesPD;
    private ListView listaPedido;
    private TextView tvPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util util = new Util(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto_seleccionado);
        tvPedidos = (TextView) findViewById(R.id.tvPedidos);
        ////////////////////////////////////////////////////////////////////////////////////////////
        tvPedidos.setTypeface(util.getTypeface());
        ////////////////////////////////////////////////////////////////////////////////////////////
        tvPedidos.setText("Presupuesto: " + comprobantesPD.get(0).getNROCOMP());
        listaPedido = (ListView) findViewById(R.id.lvPresupuestoRealizado);
        lvaPresupuestoRealizado adapterPedido = new lvaPresupuestoRealizado(this);
        listaPedido.setAdapter(adapterPedido);
    }

    @Override
    public void onBackPressed() {
        abrirActivity(this, UIPresupuestos.class);
    }
}