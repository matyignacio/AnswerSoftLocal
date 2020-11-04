package com.desarrollo.kuky.answersoft.ui;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.controlador.BaseHelper;
import com.desarrollo.kuky.answersoft.controlador.ConfigaccControlador;
import com.desarrollo.kuky.answersoft.util.Util;

public class UIPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvBienvenidoUsuario;

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
        tvBienvenidoUsuario.setTypeface(util.getTypeface());
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SQLiteDatabase db = BaseHelper.getInstance(this).getReadableDatabase();
        try {
            Cursor c = db.rawQuery("SELECT NOMBRE FROM usuarios", null);
            while (c.moveToNext()) {
                tvBienvenidoUsuario.setText("Bienvenido " + c.getString(0) + "!");
            }
            c.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            SQLiteDatabase db = BaseHelper.getInstance(this).getReadableDatabase();
            try {
                Cursor c = db.rawQuery("SELECT * FROM usuarios", null);
                if (c.moveToFirst()) {
                    c.close();
                    db.close();
                    Intent setIntent = new Intent(Intent.ACTION_MAIN);
                    setIntent.addCategory(Intent.CATEGORY_HOME);
                    setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(setIntent);
                    super.onBackPressed();
                }
            } catch (Exception e) {

            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int bandera = 0;


        if (id == R.id.clientes) {
            ConfigaccControlador configaccControlador = new ConfigaccControlador();
            configaccControlador.permisosClientes(UIPrincipal.this);
        } else if (id == R.id.productos) {
            ConfigaccControlador configaccControlador = new ConfigaccControlador();
            configaccControlador.permisosProductos(UIPrincipal.this);
        } else if (id == R.id.cerrarSesion) {
            Util.showDialogCerrarSesion(this);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}