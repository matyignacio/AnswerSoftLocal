package com.desarrollo.kuky.answersoft.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.desarrollo.kuky.answersoft.R;
import com.desarrollo.kuky.answersoft.controlador.BaseHelper;
import com.desarrollo.kuky.answersoft.controlador.ConfigaccControlador;
import com.desarrollo.kuky.answersoft.controlador.ProductoControlador;
import com.desarrollo.kuky.answersoft.objetos.Moneda;
import com.desarrollo.kuky.answersoft.objetos.Producto;
import com.desarrollo.kuky.answersoft.util.Util;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.concurrent.Callable;

import static com.desarrollo.kuky.answersoft.util.Util.abrirActivity;

public class UIProductos extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView listaProductos;
    public static Producto p;
    public static Moneda moneda;
    public String busqueda = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProductos);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(UIProductos.this);
                scanIntegrator.initiateScan();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // CAPTURAMOS LOS ELEMENTOS
        listaProductos = (ListView) findViewById(R.id.lvProductos);
        ConfigaccControlador configaccControlador = new ConfigaccControlador();
        configaccControlador.permisosProductos(this);
        // CARGAMOS LOS CAMPOS
        final ProductoControlador productoControlador = new ProductoControlador();
        productoControlador.buscarPorDescripcion(UIProductos.this, listaProductos, busqueda);
        listaProductos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                p = productoControlador.extraerDeLista(position);
                productoControlador.extraerPorId(UIProductos.this, p);
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
        getMenuInflater().inflate(R.menu.productos, menu);
        final MenuItem searchItem = menu.findItem(R.id.buscarproductos);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint(getText(R.string.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                busqueda = query;
                final ProductoControlador productoControlador = new ProductoControlador();
                productoControlador.buscarPorDescripcion(UIProductos.this, listaProductos, query);
                listaProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        p = productoControlador.extraerDeLista(position);
                        productoControlador.extraerPorId(UIProductos.this, p);// Le paso esta actividad y desde el postExecute lanzo
                        // UIProductoSeleccionado
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
//                        busqueda = newText;
//                        final ProductoControlador productoControlador = new ProductoControlador();
//                        productoControlador.buscarPorDescripcion(UIProductos.this, listaProductos, newText);
//                        listaProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                p = productoControlador.extraerDeLista(position);
//                                productoControlador.extraerPorId(UIProductos.this, p);// Le paso esta actividad y desde el postExecute lanzo
//                                // UIProductoSeleccionado
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

        if (id == R.id.presupuestos) {
            abrirActivity(this, UIPresupuestos.class);
        } else if (id == R.id.clientes) {
            abrirActivity(this, UIClientes.class);
        } else if (id == R.id.productos) {

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
                            SQLiteDatabase db = BaseHelper.getInstance(UIProductos.this).getWritableDatabase();
                            db.execSQL("DELETE FROM usuarios");
                            abrirActivity(UIProductos.this, UILogin.class);
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            ProductoControlador productoControlador = new ProductoControlador();
            productoControlador.extraerPorCodBarra(UIProductos.this, p, scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No se pudo leer ningun codigo", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
