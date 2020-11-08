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

import java.util.ArrayList;

/**
 * Created by kuky on 25/04/2016.
 */
public class lvaListadoProductos extends BaseAdapter {
    // Declare Variables
    Context context;
    ArrayList<Producto> productos;
    private final Typeface tf;
    LayoutInflater inflater;

    public lvaListadoProductos(Context context, ArrayList<Producto> productos) {
        this.context = context;
        this.productos = productos;
        this.tf = Typeface.createFromAsset(context.getAssets(), "fonts/CaviarDreams_Bold.ttf");
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
        TextView tvNombreProducto, tvCodigo, tvStockProducto, tvPrecioProducto;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.sli_listado_productos, parent, false);

        // Locate the TextViews in listview_item.xml
        tvCodigo = (TextView) itemView.findViewById(R.id.tvCodigo);
        tvNombreProducto = (TextView) itemView.findViewById(R.id.tvNombreProducto);
        tvStockProducto = (TextView) itemView.findViewById(R.id.tvStockProducto);
        tvPrecioProducto = (TextView) itemView.findViewById(R.id.tvPrecioProducto);

        tvCodigo.setTypeface(tf);
        tvNombreProducto.setTypeface(tf);
        tvStockProducto.setTypeface(tf);
        tvPrecioProducto.setTypeface(tf);

        // Capture position and set to the TextViews

        tvCodigo.setText(productos.get(position).getCodAlternativo());
        tvNombreProducto.setText(productos.get(position).getDescripcion());
        tvStockProducto.setText(String.valueOf(productos.get(position).getStock()));
        tvPrecioProducto.setText("$ " + productos.get(position).getPrecioVenta());


        return itemView;
    }
}