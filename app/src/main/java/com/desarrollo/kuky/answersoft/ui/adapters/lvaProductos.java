package com.desarrollo.kuky.answersoft.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.objetos.Producto;
import com.desarrollo.kuky.answersoft.util.Util;

import java.util.ArrayList;

public class lvaProductos extends BaseAdapter {
    // Declare Variables
    Context context;
    ArrayList<Producto> productos;
    private final Typeface tf;
    LayoutInflater inflater;

    public lvaProductos(Context context, ArrayList<Producto> productos) {
        this.context = context;
        this.productos = productos;
        Util util = new Util(context);
        this.tf = util.getTypeface();
    }

    @Override
    public int getCount() {
        return productos.size();
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
        TextView tvProducto, tvPrecioVenta;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.sli_productos, parent, false);

        // Locate the TextViews in listview_item.xml
        tvProducto = (TextView) itemView.findViewById(R.id.tvProducto);
        tvPrecioVenta = (TextView) itemView.findViewById(R.id.etPrecioVenta);

        // Capture position and set to the TextViews
        tvProducto.setText(productos.get(position).toString());
        tvProducto.setTypeface(tf);
        float precioVenta;
        precioVenta = Util.RedondearFloat(productos.get(position).getPrecioVenta(), 2);
        tvPrecioVenta.setText(String.valueOf(precioVenta));
        tvPrecioVenta.setTypeface(tf);

        return itemView;
    }
}
