package com.desarrollo.kuky.answersoft.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.controlador.BaseHelper;
import com.desarrollo.kuky.answersoft.util.Util;

public class UIConfiguracion extends AppCompatActivity {
    EditText etDireccionIP, etPuerto, etNombreBase, etUserBase, etPassBase;
    Button bAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        // CAPTURAMOS LOS ELEMENTOS
        etDireccionIP = (EditText) findViewById(R.id.etDireccionIP);
        etPuerto = (EditText) findViewById(R.id.etPuerto);
        etNombreBase = (EditText) findViewById(R.id.etNombreBase);
        etUserBase = (EditText) findViewById(R.id.etUserBase);
        etPassBase = (EditText) findViewById(R.id.etPassBase);
        bAceptar = (Button) findViewById(R.id.bAceptar);
        // SETEAMOS TYPEFACE
        Util util = new Util(this);
        etDireccionIP.setTypeface(util.getTypeface());
        etPuerto.setTypeface(util.getTypeface());
        etNombreBase.setTypeface(util.getTypeface());
        etUserBase.setTypeface(util.getTypeface());
        etPassBase.setTypeface(util.getTypeface());
        bAceptar.setTypeface(util.getTypeface());
        // CARGAMOS LOS CAMPOS
        SQLiteDatabase db = BaseHelper.getInstance(this).getReadableDatabase();
        try {
            Cursor c = db.rawQuery("SELECT * FROM configuracion", null);
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    etDireccionIP.setText(c.getString(0));
                    etPuerto.setText(c.getString(1));
                    etNombreBase.setText(c.getString(2));
                    etUserBase.setText(c.getString(3));
                    etPassBase.setText(c.getString(4));
                }
            }
            db.close();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void insertarConfiguracion(View view) {
        if (etDireccionIP.getText().toString().equals("")) {
            Toast.makeText(this, "Debe llenar el campo direccion IP", Toast.LENGTH_SHORT).show();
        } else {
            if (etPuerto.getText().toString().equals("")) {
                Toast.makeText(this, "Debe llenar el campo puerto", Toast.LENGTH_SHORT).show();
            } else {
                if (etNombreBase.getText().toString().equals("")) {
                    Toast.makeText(this, "Debe ingresar el nombre de la base", Toast.LENGTH_SHORT).show();
                } else {
                    if (etUserBase.getText().toString().equals("")) {
                        Toast.makeText(this, "Debe ingresar el usuario ", Toast.LENGTH_SHORT).show();
                    } else {
                        SQLiteDatabase db = BaseHelper.getInstance(this).getWritableDatabase();
                        db.execSQL("DROP TABLE configuracion");
                        db.execSQL(BaseHelper.getInstance(this).getSqlTablaConfiguracion());
                        String sql = "INSERT INTO configuracion VALUES " +
                                "('" + etDireccionIP.getText() +
                                "','" + etPuerto.getText() +
                                "','" + etNombreBase.getText() +
                                "','" + etUserBase.getText() +
                                "','" + etPassBase.getText() +
                                "')";
                        db.execSQL(sql);
                        db.close();
                        Toast.makeText(this, "Configuracion asignada con exito!", Toast.LENGTH_SHORT).show();
                        this.finish();
                    }
                }
            }
        }
    }

}
