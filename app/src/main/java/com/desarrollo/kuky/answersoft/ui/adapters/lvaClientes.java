package com.desarrollo.kuky.answersoft.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.objetos.Cliente;
import com.desarrollo.kuky.answersoft.util.Util;

import java.util.ArrayList;

public class lvaClientes extends BaseAdapter {
    // Declare Variables
    Context context;
    ArrayList<Cliente> clientes;
    private final Typeface tf;
    LayoutInflater inflater;

    public lvaClientes(Context context, ArrayList<Cliente> clientes) {
        this.context = context;
        this.clientes = clientes;
        Util util = new Util(context);
        this.tf = util.getTypeface();
    }

    @Override
    public int getCount() {
        return clientes.size();
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
        TextView tvNombreCliente, tvDomicilioCLiente;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.sli_clientes, parent, false);

        // Locate the TextViews in listview_item.xml
        tvNombreCliente = (TextView) itemView.findViewById(R.id.tvDescripcionCliente);
        tvDomicilioCLiente = (TextView) itemView.findViewById(R.id.tvDomicilioCliente);
        // Capture position and set to the TextViews
        tvNombreCliente.setText(clientes.get(position).getRazonSocial());
        tvNombreCliente.setTypeface(tf);
        tvDomicilioCLiente.setText(clientes.get(position).getDomicilio());
        tvDomicilioCLiente.setTypeface(tf);

        return itemView;
    }
}
