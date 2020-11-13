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
import com.desarrollo.kuky.answersoft.controlador.ClienteControlador;
import com.desarrollo.kuky.answersoft.controlador.ConfigaccControlador;
import com.desarrollo.kuky.answersoft.objetos.Cliente;
import com.desarrollo.kuky.answersoft.util.Util;

import java.util.concurrent.Callable;

import static com.desarrollo.kuky.answersoft.util.Util.abrirActivity;


public class UIClientes extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView lvClientes;
    public static Cliente c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarClientes);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // CAPTURAMOS LOS ELEMENTOS
        lvClientes = (ListView) findViewById(R.id.lvClientes);
        // CARGAMOS LOS CAMPOS
        final ClienteControlador clienteControlador = new ClienteControlador();
        clienteControlador.extraerTodos(this, lvClientes);
        lvClientes.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                c = clienteControlador.extraerDeLista(position);
                clienteControlador.extraerPorId(UIClientes.this, c);
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
        getMenuInflater().inflate(R.menu.clientes, menu);
        final MenuItem searchItem = menu.findItem(R.id.buscarclientes);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint(getText(R.string.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                final ClienteControlador clienteControlador = new ClienteControlador();
                clienteControlador.buscarPorRazonSocial(UIClientes.this, lvClientes, query);
                lvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        c = clienteControlador.extraerDeLista(position);
                        clienteControlador.extraerPorId(UIClientes.this, c);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
//                Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
//                Runnable workRunnable = null;
//                handler.removeCallbacks(workRunnable);
//                workRunnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        final ClienteControlador clienteControlador = new ClienteControlador();
//                        clienteControlador.buscarPorRazonSocial(UIClientes.this, lvClientes, newText);
//                        lvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                c = clienteControlador.extraerDeLista(position);
//                                clienteControlador.extraerPorId(UIClientes.this, c);
//                            }
//                        });
//                    }
//                };
//                handler.postDelayed(workRunnable, DELAY /*delay*/);
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

        if (id == R.id.clientes) {
        } else if (id == R.id.presupuestos) {
            abrirActivity(this, UIPresupuestos.class);
        } else if (id == R.id.productos) {
            abrirActivity(this, UIProductos.class);
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
                            SQLiteDatabase db = BaseHelper.getInstance(UIClientes.this).getWritableDatabase();
                            db.execSQL("DELETE FROM usuarios");
                            abrirActivity(UIClientes.this, UILogin.class);
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
