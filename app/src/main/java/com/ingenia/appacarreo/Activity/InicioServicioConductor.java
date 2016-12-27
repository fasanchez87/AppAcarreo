package com.ingenia.appacarreo.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ingenia.appacarreo.Helper.gestionSharedPreferences;
import com.ingenia.appacarreo.R;
import com.ingenia.appacarreo.Vars.vars;

import java.util.ArrayList;
import java.util.List;


public class InicioServicioConductor extends AppCompatActivity implements LocationListener,
        GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, OnPermissionCallback {

    private GoogleMap mGoogleMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private PermissionHelper permissionHelper;
    private boolean isSingle;

    private ProgressDialog progressDialog;

    private Button buttonIniciarServicioConductor;


    private com.ingenia.appacarreo.Helper.gestionSharedPreferences sharedPreferences;

    public vars vars;

    private Geocoder geocoder;

    private boolean mTimerIsRunning;

    Button buttonCargaDestino;

    private String messageAlert = "";
    private boolean isMoving = false;


    private String[] neededPermission;
    String direccionesObtenidas;

    private int height = 200;
    private int width = 200;

    private double longitude, latitude;
    Location location;
    LocationManager lm;

    AutoCompleteTextView autoCompleteTextDireccionDestino;
    List<Address> direcciones;

    ProgressBar progressBarEsperaDireccionDestino;


    private android.support.v7.app.AlertDialog builder;

    private String direccionTextoClienteDestino, latitudDestino, longitudDestino;



    private String direccCarguexx, fechaSolicitud, cantidConfirma, tarifaTotalxxx, cantidAuxiliar, indicaEmbalaje, codigoPediresu,
            latituCarguexx, longitCarguexx, latituDescargu, longitDescargu, codigoClientex,emailxTercerox, movilxTercerox,nombreTercerox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_servicio_conductor);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarInicioServicioConductorToolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapInicioServicioConductor);
        mapFragment.getMapAsync(this);

        sharedPreferences = new gestionSharedPreferences(getApplicationContext());

        vars  = new vars();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                direccCarguexx = null;
                fechaSolicitud = null;
                cantidConfirma = null;
                tarifaTotalxxx = null;
                cantidAuxiliar = null;
                indicaEmbalaje = null;
                codigoPediresu = null;
                latituCarguexx = null;
                longitCarguexx = null;
                latituDescargu = null;
                longitDescargu = null;
                //DATOS CLIENTE.
                codigoClientex = null;
                emailxTercerox = null;
                movilxTercerox = null;
                nombreTercerox = null;

            } else {
                direccCarguexx = extras.getString("direccCarguexx");
                fechaSolicitud = extras.getString("fechaSolicitud");
                cantidConfirma = extras.getString("cantidConfirma");
                tarifaTotalxxx = extras.getString("tarifaTotalxxx");
                cantidAuxiliar = extras.getString("cantidAuxiliar");
                indicaEmbalaje = extras.getString("indicaEmbalaje");
                codigoPediresu = extras.getString("codigoPediresu");
                latituCarguexx = extras.getString("latituCarguexx");
                longitCarguexx = extras.getString("longitCarguexx");
                latituDescargu = extras.getString("latituDescargu");
                longitDescargu = extras.getString("longitDescargu");
                //DATOS CLIENTE.
                codigoClientex = extras.getString("codigoClientex");
                emailxTercerox = extras.getString("emailxTercerox");
                movilxTercerox = extras.getString("movilxTercerox");
                nombreTercerox = extras.getString("nombreTercerox");

                Log.i("detalle", "direccCarguexx -> " + direccCarguexx);
                Log.i("detalle", "fechaSolicitud -> " + fechaSolicitud);
                Log.i("detalle", "cantidConfirma -> " + cantidConfirma);
                Log.i("detalle", "tarifaTotalxxx -> " + tarifaTotalxxx);
                Log.i("detalle", "cantidAuxiliar -> " + cantidAuxiliar);
                Log.i("detalle", "indicaEmbalaje -> " + indicaEmbalaje);
                Log.i("detalle", "codigoPediresu -> " + codigoPediresu);

                Log.i("detalle", "latituCarguexx -> " + latituCarguexx);
                Log.i("detalle", "longitCarguexx -> " + longitCarguexx);
                Log.i("detalle", "latituDescargu -> " + latituDescargu);
                Log.i("detalle", "longitDescargu -> " + longitDescargu);
            }


        }

        buttonIniciarServicioConductor = (Button) findViewById(R.id.buttonIniciarServicioConductor);
        buttonIniciarServicioConductor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //EJECUTAMOS ORDEN DE CARGUE
                AlertDialog.Builder builder = new AlertDialog.Builder(InicioServicioConductor.this);
                builder
                        .setMessage("¿Está seguro de aceptar la orden de servicio?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                               // _webServiceIniciarServicio();

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {

                    }
                }).setCancelable(false).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);


        //mGoogleMap.addMarker(new MarkerOptions().position(bogota).title("Marker in Bogota").draggable(true));


        //MARKET ORIGEN

        Log.i("detalle", "latituCarguexx -> " + latituCarguexx);
        Log.i("detalle", "longitCarguexx -> " + longitCarguexx);
        Log.i("detalle", "latituDescargu -> " + latituDescargu);
        Log.i("detalle", "longitDescargu -> " + longitDescargu);

        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        LatLng ORIGEN = new LatLng(Double.parseDouble(latituCarguexx), Double.parseDouble(longitCarguexx));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(ORIGEN));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        mGoogleMap.addMarker(new MarkerOptions().position(ORIGEN).title(nombreTercerox).draggable(true).snippet(direccCarguexx+"\n"+movilxTercerox).
                icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.rgb(41,114,102));
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setGravity(Gravity.CENTER);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });


        //_webServiceCalcularRuta();


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {

    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {

    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {

    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {

    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {

    }

    @Override
    public void onNoPermissionNeeded()
    {

    }
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

   /* private void _webServiceCalcularRuta()
    {

        progressDialog = new ProgressDialog(ConfirmarServicioConductor.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Calculando ruta, espera un momento...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        String _urlWebService = "http://maps.googleapis.com/maps/api/directions/json?origin="+latituCarguexx+","+
                longitCarguexx + "&destination="+latituDescargu+","+longitDescargu+"&sensor=false";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, _urlWebService, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {


                            Log.i("response", "sdsd"+String.valueOf(response));

                            //Tranform the string into a json object
                            JSONArray routeArray = response.getJSONArray("routes");
                            JSONObject routes = routeArray.getJSONObject(0);
                            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                            String encodedString = overviewPolylines.getString("points");
                            List<LatLng> list = decodePoly(encodedString);
                            Polyline line = mGoogleMap.addPolyline(new PolylineOptions()
                                    .addAll(list)
                                    .width(20)
                                    .color(Color.parseColor("#FF0080"))//Google maps blue color
                                    .geodesic(true)
                            );

                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                            LatLng DESTINO = new LatLng(Double.parseDouble(latituDescargu), Double.parseDouble(longitDescargu));
                            mGoogleMap.addMarker(new MarkerOptions().position(DESTINO).title("Dirección Descargue").draggable(true).snippet(direccCarguexx).
                                    icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));


                            LatLng ORIGEN = new LatLng(Double.parseDouble(latituCarguexx), Double.parseDouble(longitCarguexx));
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(ORIGEN));
                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                            mGoogleMap.addMarker(new MarkerOptions().position(ORIGEN).title("Dirección Cargue").draggable(true).snippet(direccCarguexx).
                                    icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));





           *//*
           for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                .width(2)
                .color(Color.BLUE).geodesic(true));
            }
           *//*

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Log.i("response", "sdsd"+String.valueOf(response));



                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                            builder
                                    .setMessage(e.getMessage().toString())
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();

                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError) {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                            builder
                                    .setMessage("Error de conexión, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();
                        } else if (error instanceof NoConnectionError) {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                            builder
                                    .setMessage("Por favor, conectese a la red.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();

                        } else if (error instanceof AuthFailureError) {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                            builder
                                    .setMessage("Error de autentificación en la red, favor contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();


                        } else if (error instanceof ServerError) {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                            builder
                                    .setMessage("Error server, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();


                        } else if (error instanceof NetworkError) {


                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                            builder
                                    .setMessage("Error de red, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();


                        } else if (error instanceof ParseError) {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                            builder
                                    .setMessage("Error de conversión Parser, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();
                        }

                        ///progressBarLogin.setVisibility(View.GONE);
                    }
                })

        {
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("WWW-Authenticate", "xBasic realm=".concat(""));
                return headers;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ControllerSingleton.getInstance().addToReqQueue(jsonObjReq,"");


    }*/

   /* private void _webServiceConfirmarServicio()
    {
        progressDialog = new ProgressDialog(ConfirmarServicioConductor.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Confirmando el servicio, por favor espera un momento...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        Log.i("prueba"+"","codigoTercerox: "+codigoClientex);
        Log.i("prueba"+"","emailxTercerox: "+emailxTercerox);
        Log.i("prueba"+"","codigoPediresu: "+codigoPediresu);
        Log.i("prueba"+"","codigoConducto: "+sharedPreferences.getString("codigoTercerox"));
        Log.i("prueba"+"","numeroPlacaxxx: "+sharedPreferences.getString("numeroPlacaxxx"));


        String _urlWebService = vars.ipServer.concat("/tr_panel/wsr/ordenCargue");

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
                                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                                builder
                                        .setMessage("La orden de servicio ha sido registrada éxitosamente.")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                                //startActivity(intent);
                                                finish();
                                            }
                                        }).setCancelable(false).show();
                            }

                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                                builder
                                        .setMessage("Error registrando la orden de servicio, favor comuniquese con el area de soporte de AppCarreos")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                                //startActivity(intent);
                                                finish();
                                            }
                                        }).setCancelable(false).show();
                            }

                        }

                        catch (JSONException e)
                        {
                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                            builder
                                    .setMessage(e.getMessage().toString())
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();

                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError) {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                            builder
                                    .setMessage("Error de conexión, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();
                        } else if (error instanceof NoConnectionError) {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                            builder
                                    .setMessage("Por favor, conectese a la red.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();

                        } else if (error instanceof AuthFailureError) {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                            builder
                                    .setMessage("Error de autentificación en la red, favor contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();


                        } else if (error instanceof ServerError) {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                            builder
                                    .setMessage("Error server, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();


                        } else if (error instanceof NetworkError) {


                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                            builder
                                    .setMessage("Error de red, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();


                        } else if (error instanceof ParseError) {

                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarServicioConductor.this);
                            builder
                                    .setMessage("Error de conversión Parser, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Intent intent = new Intent(Pago.this.getApplicationContext(), Registro.class);
                                            //startActivity(intent);
                                            //finish();
                                        }
                                    }).show();
                        }

                        ///progressBarLogin.setVisibility(View.GONE);
                    }
                })

        {
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("WWW-Authenticate", "xBasic realm=".concat(""));
                headers.put("codigoTercerox", codigoClientex);
                headers.put("emailxTercerox", emailxTercerox);
                headers.put("codigoPediresu", codigoPediresu);
                headers.put("codigoConducto", sharedPreferences.getString("codigoTercerox"));
                headers.put("numeroPlacaxxx", sharedPreferences.getString("numeroPlacaxxx"));
                return headers;


            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ControllerSingleton.getInstance().addToReqQueue(jsonObjReq,"");


    }*/
}
