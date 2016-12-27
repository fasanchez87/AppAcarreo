package com.ingenia.appacarreo.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ingenia.appacarreo.Fragments.HistorialServicioCliente;
import com.ingenia.appacarreo.Fragments.HistorialServicioConductor;
import com.ingenia.appacarreo.Fragments.ServicioDisponibleConductor;
import com.ingenia.appacarreo.Fragments.ServiciosAgendadosConductor;
import com.ingenia.appacarreo.Fragments.SolicitarServicioCliente;
import com.ingenia.appacarreo.Helper.gestionSharedPreferences;
import com.ingenia.appacarreo.R;

public class Inicio extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private NavigationView navigationView;
    private gestionSharedPreferences sharedPreferences;
    private String tipoUsuario;
    private TextView textViewnameUser;
    private TextView textViewemailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = new gestionSharedPreferences(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarInicio);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navigationView.getMenu();

        //MenuItem item = menu.findItem(R.id.nav_activar_geolocalizacion);

        textViewnameUser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.NombreUserHeaderNavGestion);
        textViewemailUser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.EmailHeaderNavGestion);

        textViewnameUser.setText(""+sharedPreferences.getString("nombreUsuario"));
        textViewemailUser.setText("" + sharedPreferences.getString("emailxTercerox"));

        navigationView.setNavigationItemSelectedListener(this);

        //VALIDAMOS EL TIPO DE CONDUCTOR: 1: CONDUCTOR; 0:CLIENTE
        tipoUsuario = sharedPreferences.getString("indicaConducto");

        if(tipoUsuario.equals("0"))//ES CLIENTE
        {
            menu.getItem(0).getSubMenu().getItem(2).setVisible(false);
            menu.getItem(0).getSubMenu().getItem(3).setVisible(false);
            menu.getItem(0).getSubMenu().getItem(4).setVisible(false);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, new SolicitarServicioCliente());
            fragmentTransaction.commit();
            setTitle(navigationView.getMenu().getItem(0).getSubMenu().getItem(0).getTitle());
            navigationView.setCheckedItem(R.id.nav_solicitar_servicio);

        }

        if(tipoUsuario.equals("1"))//ES CONDUCTOR
        {
            menu.getItem(0).getSubMenu().getItem(0).setVisible(false);
            menu.getItem(0).getSubMenu().getItem(1).setVisible(false);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, new ServicioDisponibleConductor());
            fragmentTransaction.commit();
            setTitle(navigationView.getMenu().getItem(0).getSubMenu().getItem(2).getTitle());
            navigationView.setCheckedItem(R.id.nav_servicios_disponibles);
        }




        //navigationView.getMenu().getItem(1).getSubMenu().getItem(0).setChecked(true);
        //Titulo en Toolbar.



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        Class fragmentClass;

        switch (item.getItemId())
        {

            case R.id.nav_solicitar_servicio:
                fragmentClass = SolicitarServicioCliente.class;
                break;

            case R.id.nav_historial_servicio_cliente:
                fragmentClass = HistorialServicioCliente.class;
                break;


            case R.id.nav_servicios_disponibles:
                //progressBar.setVisibility(View.VISIBLE);
                fragmentClass = ServicioDisponibleConductor.class;
                break;

            case R.id.nav_historial_servicio_conductor:
                //progressBar.setVisibility(View.VISIBLE);
                fragmentClass = HistorialServicioConductor.class;
                break;

            case R.id.nav_servicios_agendados_conductor:
                //progressBar.setVisibility(View.VISIBLE);
                fragmentClass = ServiciosAgendadosConductor.class;
                break;

            /*

            case R.id.nav_soporte:
                fragmentClass = Soporte.class;
                break;

            case R.id.nav_terminos_condiciones:
                fragmentClass = Terminos.class;
                break;

            case R.id.nav_servicios_agendados:
                if (tipoUsuario.equals("C"))
                {
                    fragmentClass = AgendamientoServicioCliente.class;
                }
                else
                {
                    fragmentClass = AgendamientoServicioEsteticista.class;
                }

                break;

            case R.id.nav_configuracion:

                if (tipoUsuario.equals("E"))
                {
                    fragmentClass = DatosUsuario.class;
                }

                else
                {
                    fragmentClass = Configuracion.class;
                }

                break;*/

            case R.id.nav_cerrar_sesion:

                AlertDialog.Builder builder = new AlertDialog.Builder(Inicio.this);
                builder
                        .setMessage("¿Deseas cerrar sesión? Se eliminaran los datos de ingreso.")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {

                               // _webServiceCerrarSesionChangeStateOnLine(sharedPreferences.getString("serialUsuario"));
                                sharedPreferences.putBoolean("GuardarSesion",false);
                                sharedPreferences.clear();
                               // stopService(new Intent(getBaseContext(), ServiceActualizarUbicacionProveedor.class));
                                Intent intent = new Intent(Inicio.this, Login.class);
                                startActivity(intent);
                                finish();
                                //overridePendingTransition(R.anim.left_out, R.anim.left_in);
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {

                                if(tipoUsuario.equals("0"))//ES CLIENTE
                                {
                                    setTitle("Solicitar Servicio");
                                    navigationView.setCheckedItem(R.id.nav_solicitar_servicio);
                                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.frame_container, new SolicitarServicioCliente());
                                    fragmentTransaction.commit();
                                }
                                else
                                {
                                    /*setTitle("Servicios Disponibles");//ES CONDUCTOR
                                    navigationView.setCheckedItem(R.id.nav_servicios_disponibles);

                                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.frame_container, new ());
                                    fragmentTransaction.commit();*/
                                }



                            }
                        }).setCancelable(false).show();

            default:

                if(tipoUsuario.equals("0"))//CLIENTE
                {
                    fragmentClass = SolicitarServicioCliente.class;
                }
                else
                {
                    fragmentClass = ServicioDisponibleConductor.class;
                }

        }

        try
        {
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();

        item.setChecked(true);
        setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }
}
