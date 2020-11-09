package com.desarrollo.kuky.answersoft.ui;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.controlador.BaseHelper;
import com.desarrollo.kuky.answersoft.controlador.UsuarioControlador;
import com.desarrollo.kuky.answersoft.objetos.Usuario;
import com.desarrollo.kuky.answersoft.util.Util;

import static com.desarrollo.kuky.answersoft.util.Util.abrirActivity;
import static com.desarrollo.kuky.answersoft.util.Util.mostrarMensaje;

public class UILogin extends AppCompatActivity {
    public static Usuario usuario;


    // UI references.
    private EditText etNombre;
    private EditText etPass;
    UsuarioControlador uControlador = new UsuarioControlador();
    AttemptLogin attemptLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SQLiteDatabase db = BaseHelper.getInstance(this).getReadableDatabase();
        super.onCreate(savedInstanceState);
        Util util = new Util(this);
        try {
            usuario = new Usuario();
            setContentView(R.layout.activity_login);
            // CAPTURAMOS LOS ELEMENTOS
            etNombre = (EditText) findViewById(R.id.etNombre);
            etPass = (EditText) findViewById(R.id.etPass);
            Button bLogin = (Button) findViewById(R.id.bLogin);
            ////////////////////////////////////////////////////////////////////////////////////////////
            etNombre.setTypeface(util.getTypeface());
            etPass.setTypeface(util.getTypeface());
            bLogin.setTypeface(util.getTypeface());
            try {
                Cursor c2 = db.rawQuery("SELECT * FROM configuracion", null);
                if (c2.moveToFirst()) {
                } else {
                    abrirActivity(this, UIConfiguracion.class);
                    mostrarMensaje(this, "Por primera vez, debe configurar los datos de sincronizacion");
                }
                c2.close();
            } catch (Exception e) {
                mostrarMensaje(this, e.toString());
            }
            db.close();
            etNombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        uControlador.extraerPorNombre(UILogin.this, etNombre.getText());
                    }
                }
            });
            bLogin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
        } catch (Exception e) {
            mostrarMensaje(this, e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.configurar) {
            abrirActivity(UILogin.this, UIConfiguracion.class);
        }
        return super.onOptionsItemSelected(item);
    }

    private void attemptLogin() {
        attemptLogin = new AttemptLogin(this);
        attemptLogin.execute();

    }

    private class AttemptLogin extends AsyncTask<String, String, String> {

        Activity a;
        String nombre, pass, regreso;

        public AttemptLogin(Activity a) {
            this.a = a;
        }

        @Override
        protected void onPreExecute() {
            nombre = String.valueOf(etNombre.getText());
            pass = String.valueOf(etPass.getText());
            uControlador.extraerPorNombre(UILogin.this, etNombre.getText());
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (nombre.equalsIgnoreCase(usuario.getNombre())) {
                    if (pass.equals(usuario.getPass())) {
                        SQLiteDatabase db = BaseHelper.getInstance(a).getWritableDatabase();
                        db.execSQL("DROP TABLE usuarios");
                        db.execSQL(BaseHelper.getInstance(a).getSqlTablaUsuarios());
                        String sql = "INSERT INTO usuarios VALUES " +
                                "('" + usuario.getId() +
                                "','" + usuario.getNombre() +
                                "','" + usuario.getPass() +
                                "')";
                        db.execSQL(sql);
                        db.close();
                        regreso = "correcto";
                    } else {
                        regreso = "La clave no coincide con el usuario";
                    }
                } else {
                    regreso = "La clave no coincide con el usuario";
                }
            } catch (Exception e) {
                regreso = "Error en la conexion";
            }

            return regreso;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("correcto")) {
                abrirActivity(a, UIPrincipal.class);
            } else {
                Toast.makeText(a, s, Toast.LENGTH_SHORT).show();
            }

        }
    }


}

