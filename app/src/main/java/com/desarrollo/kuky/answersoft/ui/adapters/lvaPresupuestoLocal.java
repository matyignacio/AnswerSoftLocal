package com.desarrollo.kuky.answersoft.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.objetos.Presupuesto;

import java.util.ArrayList;

/**
 * Created by kuky on 25/04/2016.
 */
public class lvaPresupuestoLocal extends BaseAdapter {
    // Declare Variables
    Context context;
    private final Typeface tf;
    LayoutInflater inflater;
    ArrayList<Presupuesto> presupuestos;

    public lvaPresupuestoLocal(Context context, ArrayList<Presupuesto> presupuestos) {
        this.context = context;
        this.tf = Typeface.createFromAsset(context.getAssets(), "fonts/CaviarDreams_Bold.ttf");
        this.presupuestos = presupuestos;
    }

    @Override
    public int getCount() {
        return presupuestos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView tvNombreProducto, tvPrecioProducto, tvCantidadProducto;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.sli_presupuesto_local, parent, false);

        // Locate the TextViews in listview_item.xml
        tvCantidadProducto = (TextView) itemView.findViewById(R.id.tvCantidadProducto);
        tvPrecioProducto = (TextView) itemView.findViewById(R.id.tvPrecioProducto);
        tvNombreProducto = (TextView) itemView.findViewById(R.id.tvNombreProducto);

        // Capture position and set to the TextViews

        tvCantidadProducto.setText(String.valueOf(presupuestos.get(position).getCantidad()));
        tvPrecioProducto.setText("$ " + presupuestos.get(position).getPrecio());
        tvNombreProducto.setText(presupuestos.get(position).getNombre());
        tvCantidadProducto.setTypeface(tf);
        tvPrecioProducto.setTypeface(tf);
        tvNombreProducto.setTypeface(tf);

        return itemView;
    }
}