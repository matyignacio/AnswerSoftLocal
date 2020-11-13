package com.desarrollo.kuky.answersoft.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.controlador.BaseHelper;
import com.desarrollo.kuky.answersoft.controlador.ConfigaccControlador;
import com.desarrollo.kuky.answersoft.util.Util;

import java.util.concurrent.Callable;

import static com.desarrollo.kuky.answersoft.util.Util.abrirActivity;
import static com.desarrollo.kuky.answersoft.util.Util.mostrarMensaje;

public class UIPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvBienvenidoUsuario;
    Button bClientes, bProductos, bPresupuestos;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util util = new Util(this);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        tvBienvenidoUsuario = (TextView) findViewById(R.id.tvBienvenidoUsuario);
        bClientes = (Button) findViewById(R.id.bClientes);
        bProductos = (Button) findViewById(R.id.bProductos);
        bPresupuestos = (Button) findViewById(R.id.bPresupuestos);
        ////////////////////////////////////////////////////////////////////////////////////////////
        tvBienvenidoUsuario.setTypeface(util.getTypeface());
        bClientes.setTypeface(util.getTypeface());
        bProductos.setTypeface(util.getTypeface());
        bPresupuestos.setTypeface(util.getTypeface());
        ////////////////////////////////////////////////////////////////////////////////////////////
        bClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActivity(UIPrincipal.this, UIClientes.class);
            }
        });
        bProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActivity(UIPrincipal.this, UIProductos.class);
            }
        });
        bPresupuestos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActivity(UIPrincipal.this, UIPresupuestos.class);
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SQLiteDatabase db = BaseHelper.getInstance(this).getReadableDatabase();
        try {
            Cursor c2 = db.rawQuery("SELECT * FROM parametros", null);
            if (c2.moveToFirst()) {
            } else {
                ConfigaccControlador configaccControlador = new ConfigaccControlador();
                configaccControlador.permisosParametros(UIPrincipal.this);
                mostrarMensaje(this, "Es necesario configurar los parametros");
            }
            c2.close();
        } catch (Exception e) {
            mostrarMensaje(this, e.toString());
        }
        try {
            Cursor c = db.rawQuery("SELECT NOMBRE FROM usuarios", null);
            while (c.moveToNext()) {
                tvBienvenidoUsuario.setText("Bienvenido " + c.getString(0) + "!");
            }
            c.close();
            db.close();
        } catch (Exception e) {
            mostrarMensaje(this, e.toString());
        }
        db.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.finish();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.presupuestos) {
            abrirActivity(UIPrincipal.this, UIPresupuestos.class);
        } else if (id == R.id.clientes) {
            abrirActivity(UIPrincipal.this, UIClientes.class);
        } else if (id == R.id.productos) {
            abrirActivity(UIPrincipal.this, UIProductos.class);
        } else if (id == R.id.parametros) {
            ConfigaccControlador configaccControlador = new ConfigaccControlador();
            configaccControlador.permisosParametros(this);
        } else if (id == R.id.cerrarSesion) {
            Util.createCustomDialog(this,
                    "¿Esta seguro que desea cerrar sesion?",
                    "",
                    "SI, CERRAR SESION",
                    "CANCELAR",
                    new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            ///////////////////////////////////////////////////////////////////////////////////////
                            SQLiteDatabase db = BaseHelper.getInstance(UIPrincipal.this).getWritableDatabase();
                            db.execSQL("DELETE FROM usuarios");
                            abrirActivity(UIPrincipal.this, UILogin.class);
                            return null;
                        }
                    },
                    new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            return null;
                        }
                    }).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}