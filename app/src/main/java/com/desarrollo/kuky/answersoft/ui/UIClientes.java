package com.desarrollo.kuky.answersoft.ui;

import android.content.Intent;
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
import com.desarrollo.kuky.answersoft.controlador.ClienteControlador;
import com.desarrollo.kuky.answersoft.controlador.ConfigaccControlador;
import com.desarrollo.kuky.answersoft.objetos.Cliente;
import com.desarrollo.kuky.answersoft.util.Util;


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
            Intent intent = new Intent(this, UIPrincipal.class);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.clientes, menu);
        final MenuItem searchItem = menu.findItem(R.id.buscarclientes);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //permite modificar el hint que el EditText muestra por defecto
        //searchView.setQueryHint(getText(R.string.search));
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
                final ClienteControlador clienteControlador = new ClienteControlador();
                clienteControlador.buscarPorRazonSocial(UIClientes.this, lvClientes, newText);
                lvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        c = clienteControlador.extraerDeLista(position);
                        clienteControlador.extraerPorId(UIClientes.this, c);
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

        if (id == R.id.clientes) {
        } else if (id == R.id.productos) {
            ConfigaccControlador configaccControlador = new ConfigaccControlador();
            configaccControlador.permisosProductos(this);
        } else if (id == R.id.cerrarSesion) {
            Util.showDialogCerrarSesion(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
