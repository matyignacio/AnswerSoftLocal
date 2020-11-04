package com.desarrollo.kuky.answersoft.ui;

import android.app.Activity;
import android.content.Intent;
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
        try {
            Cursor c = db.rawQuery("SELECT * FROM usuarios", null);
            if (c.moveToFirst()) { //EL IF ES IGUAL QUE EL ELSE, PORQUE NO MODIFIQUE LAS VALIDACIONES (DEBERIA ELIMINAR TODA LA ESTRUCTURA IF Y DEJAR SOLO EL CUERPO)
                usuario = new Usuario();
                setContentView(R.layout.activity_login);
                // CAPTURAMOS LOS ELEMENTOS
                etNombre = (EditText) findViewById(R.id.etNombre);
                etPass = (EditText) findViewById(R.id.etPass);
                Button bLogin = (Button) findViewById(R.id.bLogin);
                try {
                    Cursor c2 = db.rawQuery("SELECT * FROM configuracion", null);
                    if (c2.moveToFirst()) {
                    } else {
                        Toast.makeText(this, "Por primera vez, debe configurar los datos de sincronizacion", Toast.LENGTH_SHORT).show();
                    }
                    c2.close();
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                c.close();
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
          /*     Intent intent = new Intent(UILogin.this, UIPrincipal.class);
                this.finish();
                startActivity(intent);
                c.close();
                db.close();*/ //ESTO QUEDA COMENTADO PORQUE JUAN ME PIDIO QUE PIDA LOGUEARSE TODAS LAS VECES QUE SE INICIA
            } else {
                usuario = new Usuario();
                setContentView(R.layout.activity_login);
                // CAPTURAMOS LOS ELEMENTOS
                etNombre = (EditText) findViewById(R.id.etNombre);
                etPass = (EditText) findViewById(R.id.etPass);
                Button bLogin = (Button) findViewById(R.id.bLogin);
                try {
                    Cursor c2 = db.rawQuery("SELECT * FROM configuracion", null);
                    if (c2.moveToFirst()) {
                    } else {
                        Toast.makeText(this, "Por primera vez, debe configurar los datos de sincronizacion", Toast.LENGTH_SHORT).show();
                    }
                    c2.close();
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                c.close();
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
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

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
            Intent intent = new Intent(UILogin.this, UIConfiguracion.class);
            startActivity(intent);
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
            if (s.toString().equals("correcto")) {
                Intent intent = new Intent(UILogin.this, UIPrincipal.class);
                startActivity(intent);
                a.finish();
            } else {
                Toast.makeText(a, s, Toast.LENGTH_SHORT).show();
            }

        }
    }


}

