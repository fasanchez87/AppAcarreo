package com.ingenia.appacarreo.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ingenia.appacarreo.Beans.Carga;
import com.ingenia.appacarreo.Beans.Vehiculo;
import com.ingenia.appacarreo.Helper.gestionSharedPreferences;
import com.ingenia.appacarreo.R;
import com.ingenia.appacarreo.Vars.vars;
import com.ingenia.appacarreo.Volley.ControllerSingleton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Servicio extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener,AdapterView.OnItemSelectedListener
{

    private gestionSharedPreferences sharedPreferences;

    private ArrayList <Carga> listaTiposCarga;
    private ArrayList <String> nombresTipoCarga;


    private ArrayList <Vehiculo> listaTiposVehiculos;
    private ArrayList <String> nombresTiposVehiculos;

    private String direccionTextoClienteOrigen, latitudOrigen,longitudOrigen,
            direccionTextoClienteDestino, latitudDestino, longitudDestino;

    private LinearLayout linearLayoutValorTotalAcarreo, linearLayoutCalcularAcarreo;
    private TextView textViewTotalAcarreo,textViewTotalKilometrosAcarreo;
    EditText editTextCantidadAuxiliares;
    private Button buttonCalcularAcarreo;
    private CheckBox checkEmbalaje;

    private int indicaEmbalaje;

    private String codTipoCarga;
    private String codTipoVehiculo;
    private String cantidadKilometros;
    private String valorTotalAcarreo;
    private String cantidadAuxiliares;

    public String getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(String fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public String getHoraServicio() {
        return horaServicio;
    }

    public void setHoraServicio(String horaServicio) {
        this.horaServicio = horaServicio;
    }

    private String fechaServicio;
    private String horaServicio;

    Spinner spinnerTipoCarga, spinnerTipoVehiculo;

    public vars vars;

    EditText EditTextFechaServicio, EditTextHoraServicio, EditTextDireccionOrigenServicio, EditTextDestinoServicioServicio;

    private MenuItem menuCerrarSesion;
    private MenuItem menuEnviarServicio;

    final NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        indicaEmbalaje = 0; //DEFAULT VALUE

        setContentView(R.layout.activity_servicio);

        codTipoCarga = null;
        codTipoVehiculo = null;
        cantidadKilometros = null;
        valorTotalAcarreo = null;
        cantidadAuxiliares = null;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = new gestionSharedPreferences(getApplicationContext());

        //sharedPreferences.clear();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarServicio);
        setSupportActionBar(toolbar);
      /*  getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowHomeEnabled(false);*/

        vars  = new vars();

        listaTiposCarga = new ArrayList <Carga>();
        nombresTipoCarga = new ArrayList <String>();

        listaTiposVehiculos = new ArrayList <Vehiculo>();
        nombresTiposVehiculos = new ArrayList <String>();


        EditTextFechaServicio = (EditText) findViewById(R.id.editTextFechaServicio);
        EditTextFechaServicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                mostrarCalendarioFechaServicio();

            }
        });

        EditTextHoraServicio = (EditText) findViewById(R.id.editTextHoraServicio);
        EditTextHoraServicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub

                Calendar tDefault = Calendar.getInstance();
                tDefault.set(year, month, day, hour, minute);


                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        Servicio.this,
                        tDefault.get(Calendar.HOUR_OF_DAY),
                        tDefault.get(Calendar.MINUTE),
                        false
                );
                timePickerDialog.setOnCancelListener(Servicio.this);
                timePickerDialog.show(getFragmentManager(), "timePickerDialog");
                timePickerDialog.setTitle("Horario Servicio");

                timePickerDialog.setThemeDark(true);
                timePickerDialog.setCancelable(true);
            }
        });
        EditTextDireccionOrigenServicio = (EditText) findViewById(R.id.editTextDireccionOrigenServicio);
        EditTextDireccionOrigenServicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Servicio.this, MapOrigenCarga.class);
                startActivity(intent);
                //finish();
            }
        });




        EditTextDestinoServicioServicio = (EditText) findViewById(R.id.editTextDestinoServicioServicio);
        EditTextDestinoServicioServicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Servicio.this, MapDestinoCarga.class);
                startActivity(intent);
                //finish();
            }
        });




        spinnerTipoCarga = (Spinner) findViewById(R.id.spinnerTipoCarga);
        spinnerTipoVehiculo = (Spinner) findViewById(R.id.spinnerTipoVehiculo);

        /////

        linearLayoutValorTotalAcarreo = (LinearLayout)findViewById(R.id.linearLayoutValorTotalAcarreo);
        linearLayoutCalcularAcarreo = (LinearLayout)findViewById(R.id.linearLayoutCalcularAcarreo);

        textViewTotalAcarreo = (TextView) findViewById(R.id.textViewTotalAcarreo);
        textViewTotalKilometrosAcarreo = (TextView) findViewById(R.id.textViewTotalKilometrosAcarreo);
        editTextCantidadAuxiliares = (EditText) findViewById(R.id.editTextCantidadAuxiliares);

        checkEmbalaje = (CheckBox) findViewById(R.id.checkEmbalaje);

        buttonCalcularAcarreo = (Button) findViewById(R.id.buttonCalcularAcarreo);
        buttonCalcularAcarreo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                indicaEmbalaje = 0;
                cantidadAuxiliares = "0";
                Log.i("webservice: ","fechaServicio:"+""+fechaServicio);
                Log.i("webservice: ","horaServicio: "+horaServicio);
                Log.i("webservice: ","direccion_real_origen: "+sharedPreferences.getString("direccionTextoClienteOrigen"));
                Log.i("webservice: ","direccion_real_destino: "+sharedPreferences.getString("direccionTextoClienteDestino"));

                Log.i("webservice: ","latitudOrigen: "+latitudOrigen);
                Log.i("webservice: ","longitudOrigen: "+longitudOrigen);
                Log.i("webservice: ","latitudDestino: "+latitudDestino);
                Log.i("webservice: ","longitudDestino: "+longitudDestino);

                Log.i("webservice: ","codTipoCarga: "+codTipoCarga);
                Log.i("webservice: ","codTipoVehiculo: "+codTipoVehiculo);




                if(!editTextCantidadAuxiliares.getText().toString().isEmpty())
                {
                    cantidadAuxiliares = editTextCantidadAuxiliares.getText().toString();
                }

                Log.i("webservice: ","auxiliares_extra: "+cantidadAuxiliares);

                if(checkEmbalaje.isChecked())
                {
                   indicaEmbalaje = 1;
                }

              /*  Log.i("webservice: ","requiere_embalaje: "+indicaEmbalaje);

                Log.i("webservice", "Cantidad Kilometros -> "+":"+CalculationByDistance(4.5619287,
                        -76.0096337, 4.3034047,-75.0361965));*/

                _webServiceCalcularDistancia();

            }
        });



        ///////

        spinnerTipoCarga.setOnItemSelectedListener(this);
        spinnerTipoVehiculo.setOnItemSelectedListener(this);

        if (savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if (extras == null)
            {
                direccionTextoClienteOrigen = null;
                latitudOrigen = null;
                longitudOrigen = null;
                direccionTextoClienteDestino = null;
                latitudDestino = null;
                longitudDestino = null;
            }
            else
            {
                //ORIGEN
                direccionTextoClienteOrigen = extras.getString("direccionTextoClienteOrigen");
                direccionTextoClienteOrigen = sharedPreferences.getString("direccionTextoClienteOrigen");
                latitudOrigen = extras.getString("latitudOrigen");
                longitudOrigen = extras.getString("longitudOrigen");

                //DESTINO
                direccionTextoClienteDestino = extras.getString("direccionTextoClienteDestino");
                latitudDestino = extras.getString("latitudDestino");
                longitudDestino = extras.getString("longitudDestino");


              /*  sharedPreferences.putString("direccionTextoClienteDestino",""+direccionTextoClienteDestino);
                sharedPreferences.putString("latitudDestino",""+latitudDestino);
                sharedPreferences.putString("longitudDestino",""+longitudDestino);*/

                Log.i("Servicio", "direccionTextoClienteOrigen -> "+direccionTextoClienteOrigen);
                Log.i("Servicio", "latitudOrigen -> "+latitudOrigen);
                Log.i("Servicio", "longitudOrigen -> "+longitudOrigen);
                Log.i("Servicio", "longitudDestino -> "+direccionTextoClienteDestino);
                Log.i("Servicio", "longitudDestino -> "+latitudDestino);
                Log.i("Servicio", "longitudDestino -> "+longitudDestino);





            }

            if (sharedPreferences.getBoolean("isSaveInstanceState"))
            {


                direccionTextoClienteOrigen = sharedPreferences.getString("direccionTextoClienteOrigen");

                direccionTextoClienteDestino = sharedPreferences.getString("direccionTextoClienteDestino");


                //EditTextDireccionOrigenServicio.setText("ojo"+direccionTextoClienteOrigen);
                //EditTextFechaServicio.setText(sharedPreferences.getString("fechaServicio"));

                Log.d("savedInstanceState->", "" + "true->"+sharedPreferences.getString("direccionTextoClienteOrigen"));
            }
        }

        else
        {
            //direccionTextoClienteOrigen = (String) savedInstanceState.getSerializable("direccionTextoClienteOrigen");
            latitudOrigen = (String) savedInstanceState.getSerializable("latitudOrigen");
            longitudOrigen = (String) savedInstanceState.getSerializable("longitudOrigen");
        }


        Log.e("peligro","--"+sharedPreferences.getString("direccionTextoClienteOrigen"));
        Log.e("peligro","--"+sharedPreferences.getString("direccionTextoClienteDestino"));

        /*EditTextDireccionOrigenServicio.setText(TextUtils.isEmpty(sharedPreferences.getString("direccionTextoClienteOrigen"))?null:sharedPreferences.getString("direccionTextoClienteOrigen"));
        EditTextDestinoServicioServicio.setText(TextUtils.isEmpty(sharedPreferences.getString("direccionTextoClienteDestino"))?null:sharedPreferences.getString("direccionTextoClienteDestino"));
*/


       /* EditTextDireccionOrigenServicio.setText(TextUtils.isEmpty(sharedPreferences.getString("direccionTextoClienteOrigen"))?null:sharedPreferences.getString("direccionTextoClienteOrigen"));
        EditTextDestinoServicioServicio.setText(TextUtils.isEmpty(sharedPreferences.getString("direccionTextoClienteDestino"))?null:sharedPreferences.getString("direccionTextoClienteDestino"));
*/

        _webServiceGetTipoCarga();
    }

    public void inhabilitarComponentes()
    {
        EditTextFechaServicio.setEnabled(false);
        EditTextHoraServicio.setEnabled(false);
        EditTextDireccionOrigenServicio.setEnabled(false);
        EditTextDestinoServicioServicio.setEnabled(false);

        spinnerTipoCarga.setEnabled(false);
        spinnerTipoVehiculo.setEnabled(false);

        editTextCantidadAuxiliares.setEnabled(false);

        checkEmbalaje.setEnabled(false);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
       /* sharedPreferences.remove("direccionTextoClienteOrigen");
        sharedPreferences.remove("direccionTextoClienteDestino");*/
        EditTextDireccionOrigenServicio.setText(TextUtils.isEmpty(sharedPreferences.getString("direccionTextoClienteOrigen"))?null:sharedPreferences.getString("direccionTextoClienteOrigen"));
        EditTextDestinoServicioServicio.setText(TextUtils.isEmpty(sharedPreferences.getString("direccionTextoClienteDestino"))?null:sharedPreferences.getString("direccionTextoClienteDestino"));
        latitudOrigen = TextUtils.isEmpty(sharedPreferences.getString("latitudOrigen"))?"":sharedPreferences.getString("latitudOrigen");
        longitudOrigen = TextUtils.isEmpty(sharedPreferences.getString("longitudOrigen"))?"":sharedPreferences.getString("longitudOrigen");
        latitudDestino = TextUtils.isEmpty(sharedPreferences.getString("latitudDestino"))?"":sharedPreferences.getString("latitudDestino");
        longitudDestino = TextUtils.isEmpty(sharedPreferences.getString("longitudDestino"))?"":sharedPreferences.getString("longitudDestino");

        linearLayoutCalcularAcarreo.setVisibility(View.VISIBLE);
        linearLayoutValorTotalAcarreo.setVisibility(View.GONE);

        Log.d("onResume", "latitudOrigen: "+latitudOrigen);
        Log.d("onResume", "longitudOrigen: "+longitudOrigen);
        Log.d("onResume", "latitudDestino: "+latitudDestino);
        Log.d("onResume", "longitudDestino: "+longitudDestino);
        Log.d("onResume", "onResume!");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        //Salvamos el estado de la activity.
        super.onSaveInstanceState(savedInstanceState);
        sharedPreferences.putBoolean("isSaveInstanceState", true);



       /* sharedPreferences.remove("direccionTextoClienteOrigen");
        sharedPreferences.remove("direccionTextoClienteDestino");*/
        //ORIGEN
      /*  sharedPreferences.putBoolean("saveInstanceState", true);
        sharedPreferences.putString("direccionTextoClienteOrigen",TextUtils.isEmpty(direccionTextoClienteOrigen)?"":
                direccionTextoClienteOrigen);
        sharedPreferences.putString("latitudOrigen",""+latitudOrigen);
        sharedPreferences.putString("longitudOrigen",""+longitudOrigen);

        //DESTINO
        sharedPreferences.putString("direccionTextoClienteDestino",TextUtils.isEmpty(direccionTextoClienteDestino)?"D":
                direccionTextoClienteDestino);

        Log.d("onSaveInstanceState", "is saved!");
        Log.d("onSaveInstanceState->", "" + "->"+sharedPreferences.getString("direccionTextoClienteOrigen"));


*/
        Log.d("onSaveInstanceState", "is saved!");
    }


    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }




    private int year, month, day, hour, minute, second;


    public void mostrarCalendarioFechaServicio()
    {
        //initDateTimeData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(year, month, day);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),
                cDefault.get(Calendar.DAY_OF_WEEK)
        );

        Calendar cMin = Calendar.getInstance();
        Calendar cMax = Calendar.getInstance();
        cMax.set( cMax.get(Calendar.YEAR), 11, 32 );
        datePickerDialog.setMinDate(cMin);
        //datePickerDialog.setMaxDate(cMax);

        List<Calendar> daysList = new LinkedList<>();
        Calendar[] daysArray;
        Calendar cAux = Calendar.getInstance();

   /*     while( (cAux.getTimeInMillis() <= cMax.getTimeInMillis()) )
        {
            //if( cAux.get( Calendar.DAY_OF_WEEK ) != 3 && cAux.get( Calendar.DAY_OF_WEEK ) != 2 )
            //{
            Calendar c = Calendar.getInstance();

            c.setTimeInMillis( cAux.getTimeInMillis() );
            c.add(Calendar.DATE, 1);//SELECCIONAMOS A PARTIR DE MAÑANA
            daysList.add( c );
            //}

            cAux.setTimeInMillis( cAux.getTimeInMillis() + ( 24 * 60 * 60 * 1000 ) );
        }

        daysArray = new Calendar[ 5 ];

       *//* for( int i = 0; i < daysArray.length; i++ )
        {
            daysArray[i] = daysList.get(i);

        }*//*

        daysArray[0] = daysList.get(0);
        daysArray[1] = daysList.get(1);
        daysArray[2] = daysList.get(2);
        daysArray[3] = daysList.get(3);
        daysArray[4] = daysList.get(4);
*/


        //datePickerDialog.setSelectableDays( daysArray );
        datePickerDialog.setOnCancelListener(this);
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog" );
    }


    @Override
    public void onCancel(DialogInterface dialog)
    {
        if( year == 0 )
        {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            second = c.get(Calendar.SECOND);
        }

       /* Toast.makeText(getActivity(),"AÑO: "+year+" MES: "+month+" DIA: "+day,
                Toast.LENGTH_LONG).show();*/
        year = month = day = hour = minute = second = 0;
    }


    @Override
    public void onDateSet(DatePickerDialog view, int years, int monthOfYear, int dayOfMonth)
    {
        Calendar tDefault = Calendar.getInstance();
        tDefault.set(year, month, day, hour, minute);

        year = years;
        month = monthOfYear;
        day = dayOfMonth;

        this.fechaServicio = year+"-"+(month+1 < 10 ? "0"+(month+1) : month+1)+"-"+(day < 10 ? "0"+day : day);

        EditTextFechaServicio.setText(fechaServicio);
       // sharedPreferences.putString("fechaServicio",""+fechaServicio);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second)
    {
        hour = hourOfDay;
        this.minute = minute;
        this.second = second;
        horaServicio = (hour < 10 ? "0"+hour : hour)+":"+(minute < 10 ? "0"+minute : minute)+":"+"00";
        EditTextHoraServicio.setText(horaServicio);
        //sharedPreferences.putString("horaServicio",""+horaServicio);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_cerrar_sesion).setVisible(true);
        menu.findItem(R.id.action_enviar_servicio).setVisible(false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_servicio, menu);
        // Get dynamic menu item
        menuCerrarSesion = menu.findItem(R.id.action_cerrar_sesion);
        menuEnviarServicio = menu.findItem(R.id.action_enviar_servicio);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_cerrar_sesion:
                AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                builder
                        .setMessage("¿Está seguro de cerrar sesión ahora?").
                        setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {

                            }
                        })
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                sharedPreferences.putBoolean("GuardarSesion",false);
                                sharedPreferences.clear();
                                Intent intent = new Intent(Servicio.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                        }).show();
                return true;

            case R.id.action_enviar_servicio:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Servicio.this);
                builder1
                        .setMessage("¿Está seguro de enviar la solicitud de acarreo ahora?").
                        setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {

                            }
                        })
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                _webServiceRegistroSolicitudServicioAcarreo();
                            }
                        }).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

        Spinner spinners = (Spinner) parent;

        if(spinners.getId() == R.id.spinnerTipoCarga)
        {
            // get spinner value
            Toast.makeText(this, "Id Tipo Carga : " + listaTiposCarga.get(position).getIdTipoCarga(), Toast.LENGTH_LONG).show();
            codTipoCarga = listaTiposCarga.get(position).getIdTipoCarga().toString();
        }

        if(spinners.getId() == R.id.spinnerTipoVehiculo)
        {
            Toast.makeText(this, "Id Tipo Vehiculo : " + listaTiposVehiculos.get(position).getIdVehiculo(), Toast.LENGTH_LONG).show();
            codTipoVehiculo = listaTiposVehiculos.get(position).getIdVehiculo().toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    private void _webServiceGetTipoCarga()
    {

        progressDialog = new ProgressDialog(Servicio.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Cargando configuración, espera un momento ...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        String _urlWebService = vars.ipServer.concat("/tr_panel/wsr/getTipoCarga");

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(_urlWebService,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try
                        {
                            if(response.length() != 0)
                            {
                                //listaTiposCarga.clear();

                                Carga tipoCarga;

                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject carga = (JSONObject) response.get(i);

                                    tipoCarga = new Carga();

                                    tipoCarga.setIdTipoCarga(carga.getString("codigoTipocarg").toString());
                                    tipoCarga.setNombreTipoCarga(carga.getString("nombreTipocarg").toString());

                                    listaTiposCarga.add(tipoCarga);

                                    nombresTipoCarga.add(carga.getString("nombreTipocarg").toString());

                                }

                                ArrayAdapter<String> spinnerAdapterGasto = new ArrayAdapter<String>(Servicio.this,
                                        android.R.layout.simple_spinner_item, nombresTipoCarga);

                                // se agrega estilo de tamaño mediano del item al pulsar
                                spinnerAdapterGasto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                spinnerTipoCarga.setAdapter(spinnerAdapterGasto);

                                _webServiceGetTipoVehiculo();


                            }
                        }

                        catch (JSONException e)
                        {
                            //progressBarLogin.setVisibility(View.GONE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage(e.getMessage().toString())
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();

                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        if (error instanceof TimeoutError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de conexión, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();
                        }

                        else

                        if (error instanceof NoConnectionError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Por favor, conectese a la red.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();

                        }

                        else

                        if (error instanceof AuthFailureError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de autentificación en la red, favor contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();



                        }

                        else

                        if (error instanceof ServerError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error server, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();



                        }

                        else

                        if (error instanceof NetworkError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de red, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();


                        }

                        else

                        if (error instanceof ParseError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de conversión Parser, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();
                        }

                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap <String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("WWW-Authenticate", "xBasic realm=".concat(""));
                return headers;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ControllerSingleton.getInstance().addToReqQueue(jsonObjReq,"");
    }

    private void _webServiceGetTipoVehiculo()
    {
        String _urlWebService = vars.ipServer.concat("/tr_panel/wsr/getTipoVehiculo");

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(_urlWebService,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try
                        {
                            if(response.length() != 0)
                            {

                                Vehiculo tiposVehiculos;

                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject vehiculo = (JSONObject) response.get(i);

                                    tiposVehiculos = new Vehiculo();

                                    tiposVehiculos.setIdVehiculo(vehiculo.getString("codigoVehclase").toString());
                                    tiposVehiculos.setNombreClaseVehiculo(vehiculo.getString("nombreVehclase").toString());

                                    listaTiposVehiculos.add(tiposVehiculos);

                                    nombresTiposVehiculos.add(vehiculo.getString("nombreVehclase").toString());

                                }

                                ArrayAdapter<String> spinnerAdapterVehiculo = new ArrayAdapter<String>(Servicio.this,
                                        android.R.layout.simple_spinner_item, nombresTiposVehiculos);

                                // se agrega estilo de tamaño mediano del item al pulsar
                                spinnerAdapterVehiculo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                spinnerTipoVehiculo.setAdapter(spinnerAdapterVehiculo);

                                progressDialog.dismiss();


                            }
                        }

                        catch (JSONException e)
                        {
                            //progressBarLogin.setVisibility(View.GONE);
                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage(e.getMessage().toString())
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();

                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        if (error instanceof TimeoutError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de conexión, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();
                        }

                        else

                        if (error instanceof NoConnectionError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Por favor, conectese a la red.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();

                        }

                        else

                        if (error instanceof AuthFailureError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de autentificación en la red, favor contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();



                        }

                        else

                        if (error instanceof ServerError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error server, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();



                        }

                        else

                        if (error instanceof NetworkError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de red, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();


                        }

                        else

                        if (error instanceof ParseError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de conversión Parser, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();
                        }

                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap <String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("WWW-Authenticate", "xBasic realm=".concat(""));
                return headers;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ControllerSingleton.getInstance().addToReqQueue(jsonObjReq,"");
    }

    private void _webServiceCalcularDistancia()
    {

        String _urlWebService = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latitudOrigen + "," + longitudOrigen +
                "&destination=" + latitudDestino + "," + longitudDestino+"&mode=driving&language=es";

        //https://maps.googleapis.com/maps/api/directions/json?origin=Chicago,IL&
        // destination=Los+Angeles,CA&waypoints=Joplin,MO|Oklahoma+City,OK&key=YOUR_API_KEY

        progressDialog = new ProgressDialog(Servicio.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Calculando distancia, espera un momento...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        Log.i("webservice",""+_urlWebService);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, _urlWebService, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            String status = response.getString("status");

                            if(status.equals("OK"))
                            {

                                progressDialog.dismiss();
                                JSONArray array = response.getJSONArray("routes");
                                JSONObject routes = array.getJSONObject(0);
                                JSONArray legs = routes.getJSONArray("legs");
                                JSONObject steps = legs.getJSONObject(0);
                                JSONObject distance = steps.getJSONObject("distance");
                                cantidadKilometros = distance.getString("text");
                                cantidadKilometros = cantidadKilometros.replace(" km","");

                                Log.i("webservice",""+cantidadKilometros);

                                cantidadKilometros = cantidadKilometros.replace(".","");


                                _webServiceCalcularTotalAcarreo();


                            }

                            else
                            {
                                if(status.equals("ZERO_RESULTS"))
                                {
                                    Log.i("apigoogle","error");
                                    progressDialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                                    builder
                                            .setMessage("Lo sentimos, no cubrimos hasta la distancia solicitada, pregunta al administrador los posibles destinos.")
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id)
                                                {
                                                    //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                                    //startActivity(intent);
                                                    //finish();
                                                }
                                            }).show();


                                }
                            }
                        }

                        catch (JSONException e)
                        {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage(e.getMessage().toString())
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();

                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        if (error instanceof TimeoutError)
                        {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de conexión, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();
                        }

                        else

                        if (error instanceof NoConnectionError)
                        {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Por favor, conectese a la red.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();

                        }

                        else

                        if (error instanceof AuthFailureError)
                        {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de autentificación en la red, favor contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();



                        }

                        else

                        if (error instanceof ServerError)
                        {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error server, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();



                        }

                        else

                        if (error instanceof NetworkError)
                        {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de red, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();


                        }

                        else

                        if (error instanceof ParseError)
                        {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de conversión Parser, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();
                        }

                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap <String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("WWW-Authenticate", "xBasic realm=".concat(""));
                return headers;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ControllerSingleton.getInstance().addToReqQueue(jsonObjReq,"");
    }

    private void _webServiceCalcularTotalAcarreo()
    {

        String _urlWebService = vars.ipServer.concat("/tr_panel/wsr/getTotalServicio");

        progressDialog = new ProgressDialog(Servicio.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Calculando el valor de acarreo, espera un momento...");
        progressDialog.show();
        progressDialog.setCancelable(false);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, _urlWebService, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            String status = response.getString("status");

                            if(status.equals("ok"))
                            {

                                JSONArray array = response.getJSONArray("result");
                                JSONObject routes = array.getJSONObject(4);
                                valorTotalAcarreo = routes.getString("valor");
                                Log.i("valorTotalAcarreo",""+routes.toString());

                                menuEnviarServicio.setVisible(true);
                                progressDialog.dismiss();

                                linearLayoutCalcularAcarreo.setVisibility(View.GONE);
                                textViewTotalAcarreo.setText("$"+nf.format(Integer.parseInt(valorTotalAcarreo)));
                                textViewTotalKilometrosAcarreo.setText(cantidadKilometros+" KM");
                                linearLayoutValorTotalAcarreo.setVisibility(View.VISIBLE);

                                /*JSONArray array = response.getJSONArray("routes");
                                JSONObject routes = array.getJSONObject(0);
                                JSONArray legs = routes.getJSONArray("legs");
                                JSONObject steps = legs.getJSONObject(0);
                                JSONObject distance = steps.getJSONObject("distance");
                                cantidadKilometros = distance.getString("text");
                                cantidadKilometros = cantidadKilometros.replace(" km","");*/



                            }

                            else
                            {
                                if(status.equals("false"))
                                {
                                    progressDialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                                    builder
                                            .setMessage("Error calculando el valor, contacta al administrador del App.")
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id)
                                                {
                                                    //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                                    //startActivity(intent);
                                                    //finish();
                                                }
                                            }).show();
                                }
                            }
                        }

                        catch (JSONException e)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage(e.getMessage().toString())
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();

                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        if (error instanceof TimeoutError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de conexión, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();
                        }

                        else

                        if (error instanceof NoConnectionError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Por favor, conectese a la red.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();

                        }

                        else

                        if (error instanceof AuthFailureError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de autentificación en la red, favor contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();



                        }

                        else

                        if (error instanceof ServerError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error server, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();



                        }

                        else

                        if (error instanceof NetworkError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de red, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();


                        }

                        else

                        if (error instanceof ParseError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de conversión Parser, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();
                        }

                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap <String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("WWW-Authenticate", "xBasic realm=".concat(""));
                headers.put("cantidadKm", cantidadKilometros);
                headers.put("cantidadAuxiliares", cantidadAuxiliares);
                headers.put("indicaEmbalaje", ""+indicaEmbalaje);
                return headers;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ControllerSingleton.getInstance().addToReqQueue(jsonObjReq,"");
    }

    private void _webServiceRegistroSolicitudServicioAcarreo()
    {
       String _urlWebService = vars.ipServer.concat("/tr_panel/wsr/realizarPedido");

        progressDialog = new ProgressDialog(Servicio.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Enviando solicitud de servicio de carga, espera un momento...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, _urlWebService, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            Boolean status = response.getBoolean("result");

                            if(status)
                            {
                                progressDialog.dismiss();

                                AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                                builder
                                .setTitle("SOLICITUD ENVIADA")
                                .setMessage("Su solicitud de servicio ha sido enviada con exito, pronto se le asignara una orden de servicio.")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id)
                                    {


                                        Log.i("enviarservicio","Token: "+sharedPreferences.getString("MyToken"));
                                        Log.i("enviarservicio","Cedula: "+sharedPreferences.getString("codigoTercerox"));
                                        Log.i("enviarservicio","Fecha: "+fechaServicio);
                                        Log.i("enviarservicio","Hora: "+horaServicio);
                                        Log.i("enviarservicio","Direccion Origen: "+EditTextDireccionOrigenServicio.getText().toString());
                                        Log.i("enviarservicio","Latitud Origen: "+latitudOrigen);
                                        Log.i("enviarservicio","Longitud Origen: "+longitudOrigen);
                                        Log.i("enviarservicio","Direccion Destino: "+EditTextDestinoServicioServicio.getText().toString());
                                        Log.i("enviarservicio","Latitud Destino: "+latitudDestino);
                                        Log.i("enviarservicio","Longitud Destino: "+longitudDestino);
                                        Log.i("enviarservicio","Codigo tipo carga: "+codTipoCarga);
                                        Log.i("enviarservicio","Codigo tipo vehiculo: "+codTipoVehiculo);
                                        Log.i("enviarservicio","# Kilometros: "+cantidadKilometros);//KMS
                                        Log.i("enviarservicio","Cantidad Auxiliares: "+cantidadAuxiliares);
                                        Log.i("enviarservicio","Indica Emabalaje: "+indicaEmbalaje);


                                        finish();
                                        startActivity(getIntent());
                                    }
                                }).show().setCancelable(false);
                            }

                            else
                            {
                                if(status.equals("false"))
                                {
                                    String message = "Error enviando la solicitud.";

                                    //progressBarLogin.setVisibility(View.GONE);
                                    progressDialog.dismiss();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                                    builder
                                            .setMessage(message)
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id)
                                                {

                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                            }).show().setCancelable(false);
                                }
                            }
                        }

                        catch (JSONException e)
                        {
                           // progressBarLogin.setVisibility(View.GONE);

                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage(e.getMessage().toString())
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();

                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        progressDialog.dismiss();
                        if (error instanceof TimeoutError)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de conexión, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();
                        }

                        else

                        if (error instanceof NoConnectionError)
                        {

                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Por favor, conectese a la red.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();

                        }

                        else

                        if (error instanceof AuthFailureError)
                        {

                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de autentificación en la red, favor contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();



                        }

                        else

                        if (error instanceof ServerError)
                        {

                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error server, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();



                        }

                        else

                        if (error instanceof NetworkError)
                        {

                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de red, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();


                        }

                        else

                        if (error instanceof ParseError)
                        {

                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                            builder
                                    .setMessage("Error de conversión Parser, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();
                        }

                        //progressBarLogin.setVisibility(View.GONE);
                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap <String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("WWW-Authenticate", "xBasic realm=".concat(""));
                headers.put("MyToken", sharedPreferences.getString("MyToken"));
                headers.put("codigoTercerox", sharedPreferences.getString("codigoTercerox"));
                headers.put("fechaxInicioxx", fechaServicio);
                headers.put("horaxxInicioxx", horaServicio);
                headers.put("direccCarguexx", EditTextDireccionOrigenServicio.getText().toString());
                headers.put("latituCarguexx", latitudOrigen);
                headers.put("longitCarguexx", longitudOrigen);
                headers.put("direccDescargu", EditTextDestinoServicioServicio.getText().toString());
                headers.put("latituDescargu", latitudDestino);
                headers.put("longitDescargu", longitudDestino);
                headers.put("codigoTipocarg", codTipoCarga);
                headers.put("codigoVehclase", codTipoVehiculo);
                headers.put("cantidPedidoxx", cantidadKilometros);//KMS
                headers.put("cantidAuxiliar", cantidadAuxiliares);
                headers.put("indicaEmbalaje", ""+indicaEmbalaje);
                return headers;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ControllerSingleton.getInstance().addToReqQueue(jsonObjReq,"");
    }
}
