package com.desarrollo.kuky.answersoft.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.objetos.ComprobantePC;
import com.desarrollo.kuky.answersoft.util.Util;

import java.util.ArrayList;

public class lvaPresupuestos extends BaseAdapter {
    // Declare Variables
    Context context;
    ArrayList<ComprobantePC> comprobantesXC;
    private final Typeface tf;
    LayoutInflater inflater;

    public lvaPresupuestos(Context context, ArrayList<ComprobantePC> comprobantesXC) {
        this.context = context;
        this.comprobantesXC = comprobantesXC;
        Util util = new Util(context);
        this.tf = util.getTypeface();
    }

    @Override
    public int getCount() {
        return comprobantesXC.size();
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
        TextView tvNumeroComprobante, tvNombreCliente, tvTotal;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.sli_presupuestos, parent, false);

        // Locate the TextViews in listview_item.xml
        tvNumeroComprobante = (TextView) itemView.findViewById(R.id.tvNumeroComprobante);
        tvNombreCliente = (TextView) itemView.findViewById(R.id.tvNombreCliente);
        tvTotal = (TextView) itemView.findViewById(R.id.tvTotal);
        ///////////////////////////////////////////////////////////
        tvNombreCliente.setTypeface(tf);
        tvNumeroComprobante.setTypeface(tf);
        tvTotal.setTypeface(tf);
        // Capture position and set to the TextViews
        tvNombreCliente.setText(comprobantesXC.get(position).getCLIENTE());
        tvNumeroComprobante.setText(comprobantesXC.get(position).getNROCOMP());
        tvTotal.setText("$ " + comprobantesXC.get(position).getTOTAL());

        return itemView;
    }
}
