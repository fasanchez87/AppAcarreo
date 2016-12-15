package com.ingenia.appacarreo.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.ingenia.appacarreo.Permission.PermissionUtils;
import com.ingenia.appacarreo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.ingenia.appacarreo.Helper.gestionSharedPreferences;



public class MapOrigenCarga extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, OnPermissionCallback {

    private GoogleMap mGoogleMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private PermissionHelper permissionHelper;
    private boolean isSingle;

    private Geocoder geocoder;

    private boolean mTimerIsRunning;

    private gestionSharedPreferences sharedPreferences;

    Button buttonCargaOrigen;

    private String messageAlert = "";
    private boolean isMoving = false;


    private String[] neededPermission;
    String direccionesObtenidas;

    private double longitude, latitude;
    Location location;
    LocationManager lm;

    AutoCompleteTextView autoCompleteTextDireccionOrigen;
    List<android.location.Address> direcciones;

    ProgressBar progressBarEsperaDireccionOrigen;


    private android.support.v7.app.AlertDialog builder;

    private String direccionTextoClienteOrigen, latitudOrigen, longitudOrigen;


    private final static String SINGLE_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;

    private final static String[] MULTI_PERMISSIONS = new String[]
            {
                    Manifest.permission.ACCESS_FINE_LOCATION
                    //Manifest.permission.READ_PHONE_STATE
            };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_origen_carga);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCargaOrigen);
        setSupportActionBar(toolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedPreferences = new gestionSharedPreferences(getApplicationContext());

        mTimerIsRunning = false;
        latitudOrigen = null;
        longitudOrigen = null;


        autoCompleteTextDireccionOrigen=(AutoCompleteTextView) findViewById(R.id.autoCompleteEditTextDireccionOrigen);

        progressBarEsperaDireccionOrigen = (ProgressBar) findViewById(R.id.progressBarEsperaDireccionOrigen);
        buttonCargaOrigen = (Button) findViewById(R.id.buttonCargaOrigen);
        buttonCargaOrigen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                direccionTextoClienteOrigen = TextUtils.isEmpty(autoCompleteTextDireccionOrigen.getText().toString())?"":
                        autoCompleteTextDireccionOrigen.getText().toString();

                //Intent intent = new Intent(MapOrigenCarga.this, Servicio.class);
               /* intent.putExtra("direccionTextoClienteOrigen", direccionTextoClienteOrigen);
                intent.putExtra("latitudOrigen", latitudOrigen);
                intent.putExtra("longitudOrigen", longitudOrigen);
                Log.i("Origen", " : " +direccionTextoClienteOrigen );
                Log.i("Origen", " : " +latitudOrigen );*/
                sharedPreferences.putString("direccionTextoClienteOrigen",direccionTextoClienteOrigen);
                sharedPreferences.putString("latitudOrigen",latitudOrigen);
                sharedPreferences.putString("longitudOrigen",longitudOrigen);
                //startActivity(intent);
                finish();

            }
        });


    }


    private void enableMyLocation()
    {
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);

        }

        if (mGoogleMap != null)
        {
            progressBarEsperaDireccionOrigen.setVisibility(View.VISIBLE);
            mGoogleMap.setMyLocationEnabled(true);

            LatLng bogota = new LatLng(4.712473308884156, -74.07213594764471);
            //mGoogleMap.addMarker(new MarkerOptions().position(bogota).title("Marker in Bogota").draggable(true));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener()
            {
                @Override
                public void onCameraMoveStarted(int i)
                {
                /*mDragTimer.start();
                mTimerIsRunning = true;*/
                    mTimerIsRunning = true;
                    isMoving = true;
                    progressBarEsperaDireccionOrigen.setVisibility(View.GONE);
                    Log.i("onCameraMoveStarted", " : " + i);
                    Log.i("onCameraMoveStarted", " : " + i);

                }
            });

            mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener()
            {
                //Se llama cuando el movimiento de la cámara ha terminado, no hay animaciones en espera y
                // el usuario ha dejado de interactuar con el mapa.
                @Override
                public void onCameraIdle()
                {
                    // Cleaning all the markers.
                    if (mGoogleMap != null)
                    {
                        mGoogleMap.clear();

                    }
                    isMoving = false;
                    progressBarEsperaDireccionOrigen.setVisibility(View.VISIBLE);

                    Log.i("setOnCameraIdleListener", "centerLat: " + mGoogleMap.getCameraPosition().target.latitude);
                    Log.i("setOnCameraIdleListener", "centerLong: " + mGoogleMap.getCameraPosition().target.longitude);
                    latitudOrigen = ""+mGoogleMap.getCameraPosition().target.latitude;
                    longitudOrigen = ""+mGoogleMap.getCameraPosition().target.longitude;
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            autoCompleteTextDireccionOrigen.setText(""+getCompleteAddressString(mGoogleMap.getCameraPosition().target.latitude,
                                    mGoogleMap.getCameraPosition().target.longitude));
                            autoCompleteTextDireccionOrigen.setSelection(autoCompleteTextDireccionOrigen.getText().length());

                            new Handler().postDelayed(new Runnable()
                            {
                                public void run() {

                                    if (!isMoving)
                                    {
                                /*circleFrameLayout.setBackground(mDrawable);
                                textView.setVisibility(View.VISIBLE);*/
                                        progressBarEsperaDireccionOrigen.setVisibility(View.GONE);
                                    }
                                }
                            }, 1500);
                        }
                    });



                }
            });



                    //mPosition = mGoogleMap.getCameraPosition().target;
                    //mZoom = mGoogleMap.getCameraPosition().zoom;





            //mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(mGoogleMap.getCameraPosition().zoom - 0.5f));
        }
    }


    private String getCompleteAddressString(double latitude, double longitude)
    {
        String strAdd = "";
        geocoder = new Geocoder(this, Locale.getDefault());
        try
        {
            direcciones = geocoder.getFromLocation(latitude, longitude, 1);

            if (direcciones != null)
            {
                direccionesObtenidas = direcciones.get(0).getAddressLine(0);
                //StringBuilder strReturnedAddress = new StringBuilder("");
/*
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++)
                {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }*/
               // strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + direccionesObtenidas.toString());
            }
            else
            {
                Log.w("My Current loction address", "No Address returned!");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return direccionesObtenidas;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            permissionHelper = PermissionHelper.getInstance(this);
            isSingle = true;
            permissionHelper
                    .setForceAccepting(true) // default is false. its here so you know that it exists.
                    .request(isSingle ? SINGLE_PERMISSION : MULTI_PERMISSIONS);
        }

        else
        {

            enableMyLocation();
        }

        // Add a marker in Sydney and move the camera
     /*   LatLng bogota = new LatLng(4.6482837, -74.072092);
        mGoogleMap.addMarker(new MarkerOptions().position(bogota).title("Marker in Bogota").draggable(true));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
*/
    /*googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener()
    {
        @Override
        public void onCameraChange(CameraPosition cameraPosition)
        {

            Log.i("point","centerLat: "+cameraPosition.target.latitude);
            Log.i("point","centerLong: "+cameraPosition.target.longitude);
        }
    });*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onPermissionGranted(@NonNull String[] permissionName)
    {
        Log.i("onPermissionGranted", "Permission(s) " + Arrays.toString(permissionName) + " Granted");
        // Enable the my location layer if the permission has been granted.
       enableMyLocation();
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName)
    {
        Log.i("onPermissionDeclined", "Permission(s) " + Arrays.toString(permissionName) + " Declined");

    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName)
    {
        Log.i("onPermissionPreGranted", "Permission( " + permissionsName + " ) preGranted");
        enableMyLocation();
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName)
    {
        Log.i("NeedExplanation", "Permission( " + permissionName + " ) needs Explanation");
        if (!isSingle)
        {
            neededPermission = PermissionHelper.declinedPermissions(this, MULTI_PERMISSIONS);
            StringBuilder builder = new StringBuilder(neededPermission.length);
            if (neededPermission.length > 0)
            {
                for (String permission : neededPermission)
                {
                    builder.append(permission).append("\n");
                }
            }

            android.support.v7.app.AlertDialog alert = getAlertDialog(neededPermission, builder.toString());
            if (!alert.isShowing())
            {
                alert.show();
            }
        }
        else
        {
            getAlertDialog(permissionName).show();
        }
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName)
    {
        //result.setText("Permission " + permissionName + " can only be granted from SettingsScreen");
        Log.i("ReallyDeclined", "Permission " + permissionName + " can only be granted from settingsScreen");
        /** you can call  {@link PermissionHelper#openSettingsScreen(Context)} to open the settings screen */
        getAlertDialog(permissionName).show();
    /*new AlertDialog.Builder(Inicio.this)
            .setMessage(R.string.location_permission_denied)
            .setPositiveButton(android.R.string.ok, null)
            .create();*/
    }

    @Override
    public void onNoPermissionNeeded()
    {
        Log.i("onNoPermissionNeeded", "Permission(s) not needed");
    }

    public android.support.v7.app.AlertDialog getAlertDialog(final String[] permissions, final String permissionName)
    {
        if (builder == null)
        {
            builder = new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("Permission Needs Explanation").setCancelable(false)
                    .create();
        }
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Request", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionHelper.requestAfterExplanation(permissions);
                finish();
            }
        });
        builder.setMessage("Permissions need explanation (" + permissionName + ")");
        return builder;
    }

    public android.support.v7.app.AlertDialog getAlertDialog(final String permission)
    {
        if (builder == null)
        {
            builder = new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("Habilitar Permiso").setCancelable(false)
                    .create();
        }
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Entiendo", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                permissionHelper.requestAfterExplanation(permission);
                openSettingsScreen(MapOrigenCarga.this);
            }
        });

        if(permission.equals("android.permission.ACCESS_FINE_LOCATION"))
        {
            messageAlert = "FastTrack necesita que apruebe el permiso de Geolocalización, a continuación será dirigido a ajustes de Aplicación "
                    +"y active el permiso de Ubicación.";
        }

        if(permission.equals("android.permission.READ_PHONE_STATE"))
        {
            messageAlert = "FastTrack necesita que apruebe el permiso de Acceso al télefono, a continuación será dirigido a ajustes de Aplicación "
                    +"y active el permiso de Télefono y Almacenamiento.";
        }

        builder.setMessage(messageAlert);
        return builder;
    }

    public static void openSettingsScreen(@NonNull Context context)
    {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + context.getPackageName());
        intent.setData(uri);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        permissionHelper.onActivityForResult(requestCode);
    }
}