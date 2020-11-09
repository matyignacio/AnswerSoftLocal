package com.desarrollo.kuky.answersoft.ui;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.controlador.BaseHelper;
import com.desarrollo.kuky.answersoft.controlador.ComprobantePCControlador;
import com.desarrollo.kuky.answersoft.controlador.ComprobantePDControlador;
import com.desarrollo.kuky.answersoft.controlador.ConfigaccControlador;
import com.desarrollo.kuky.answersoft.util.Util;

import java.util.concurrent.Callable;

import static com.desarrollo.kuky.answersoft.util.Util.abrirActivity;


public class UIPresupuestos extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView lvPresupuestos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuestos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPresupuestos);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // CAPTURAMOS LOS ELEMENTOS
        lvPresupuestos = (ListView) findViewById(R.id.lvPresupuestos);
        // CARGAMOS LOS CAMPOS
        final ComprobantePCControlador comprobantePCControlador = new ComprobantePCControlador();
        comprobantePCControlador.buscarPorCliente(this, lvPresupuestos, "");
        lvPresupuestos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvPresupuestos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ComprobantePDControlador comprobantePDControlador = new ComprobantePDControlador();
                comprobantePDControlador.extraerPorNumComp(UIPresupuestos.this, comprobantePCControlador.extraerDeLista(position).getNROCOMP());
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            abrirActivity(this, UIPrincipal.class);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.presupuestos, menu);
        final MenuItem searchItem = menu.findItem(R.id.buscarpresupustos);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint(getText(R.string.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*Toast.makeText(UIClientes.this, "Submmited", Toast.LENGTH_SHORT).show();
                //se oculta el EditText
                searchView.setQuery("", false);
                searchView.setIconified(true);*/
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ComprobantePCControlador comprobantePCControlador = new ComprobantePCControlador();
                comprobantePCControlador.buscarPorCliente(UIPresupuestos.this, lvPresupuestos, newText);
                lvPresupuestos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                lvPresupuestos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ComprobantePDControlador comprobantePDControlador = new ComprobantePDControlador();
                        comprobantePDControlador.extraerPorNumComp(UIPresupuestos.this, comprobantePCControlador.extraerDeLista(position).getNROCOMP());
                    }
                });
                return true;
            }
        });
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.presupuestos) {

        } else if (id == R.id.clientes) {
            ConfigaccControlador configaccControlador = new ConfigaccControlador();
            configaccControlador.permisosClientes(this);
        } else if (id == R.id.productos) {
            ConfigaccControlador configaccControlador = new ConfigaccControlador();
            configaccControlador.permisosProductos(this);
        } else if (id == R.id.parametros) {
            abrirActivity(this, UIParametros.class);
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
                            SQLiteDatabase db = BaseHelper.getInstance(UIPresupuestos.this).getWritableDatabase();
                            db.execSQL("DELETE FROM usuarios");
                            abrirActivity(UIPresupuestos.this, UILogin.class);
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
