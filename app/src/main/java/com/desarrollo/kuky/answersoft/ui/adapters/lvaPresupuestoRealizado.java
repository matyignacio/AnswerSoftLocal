package com.desarrollo.kuky.answersoft.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.desarrollo.kuky.answersoft.R;

import static com.desarrollo.kuky.answersoft.ui.UIPresupuestoSeleccionado.comprobantesPD;


/**
 * Created by kuky on 25/04/2016.
 */
public class lvaPresupuestoRealizado extends BaseAdapter {
    // Declare Variables
    Context context;
    private final Typeface tf;
    LayoutInflater inflater;

    public lvaPresupuestoRealizado(Context context) {
        this.context = context;
        this.tf = Typeface.createFromAsset(context.getAssets(), "fonts/CaviarDreams_Bold.ttf");
    }

    @Override
    public int getCount() {
        return comprobantesPD.size();
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
        TextView tvCantidad, tvSubTotal, tvDescripcion;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.sli_presupuesto_realizado, parent, false);

        // Locate the TextViews in listview_item.xml
        tvCantidad = (TextView) itemView.findViewById(R.id.tvCantidadProducto);
        tvDescripcion = (TextView) itemView.findViewById(R.id.tvNombreProducto);
        tvSubTotal = (TextView) itemView.findViewById(R.id.tvPrecioProducto);
        tvCantidad.setTypeface(tf);
        tvDescripcion.setTypeface(tf);
        tvSubTotal.setTypeface(tf);

        tvCantidad.setText(String.valueOf(comprobantesPD.get(position).getCANT()));
        tvDescripcion.setText(comprobantesPD.get(position).getDESCRIPPROD());
        tvSubTotal.setText("$ " + comprobantesPD.get(position).getSUBTOT());

        return itemView;
    }
}