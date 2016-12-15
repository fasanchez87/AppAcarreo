package com.ingenia.appacarreo.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ingenia.appacarreo.R;
import com.ingenia.appacarreo.Vars.vars;
import com.ingenia.appacarreo.Volley.ControllerSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by davidcaicedo on 26/06/16.
 */
public class Registro extends AppCompatActivity
{
    String url, usuario, clave;
    EditText txtCodigoTercerox, txtNombreUsuario, txtApellidoUsuario, txtEmailUsuario,
            txtCelularUsuario, txtPassUsuario;

    ProgressBar progressBarRegistro;

    Button botonRegistro;
    vars vars;

    //int codigoUsuario, celularUsuario, codigoSeguridad;
    private String codigoTercerox, nombreUsuario, apellidoUsuario, emailUsuario, passUsuario, celularUsuario;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_registro);

        progressBarRegistro = (ProgressBar) findViewById(R.id.progressBarRegistro);


        vars = new vars();

        txtCodigoTercerox = (EditText) findViewById(R.id.txtCodigoTercerox);
        txtNombreUsuario = (EditText) findViewById(R.id.txtNombreUsuario);
        txtApellidoUsuario = (EditText) findViewById(R.id.txtApellidoUsuario);
        txtEmailUsuario = (EditText) findViewById(R.id.txtEmailUsuario);
        txtCelularUsuario = (EditText) findViewById(R.id.txtCelularUsuario);
        txtPassUsuario = (EditText) findViewById(R.id.txtPassUsuario);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        botonRegistro = (Button) findViewById(R.id.btnAceptar);
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Registrar();
            }
        });


    }

    public void Registrar()
    {
        progressBarRegistro.setVisibility(View.VISIBLE);

        codigoTercerox = txtCodigoTercerox.getText().toString().trim();
        nombreUsuario = txtNombreUsuario.getText().toString().trim();
        apellidoUsuario = txtApellidoUsuario.getText().toString().trim();
        emailUsuario = txtEmailUsuario.getText().toString().trim();
        passUsuario = txtPassUsuario.getText().toString().trim();
        celularUsuario = txtCelularUsuario.getText().toString().trim();

        if (codigoTercerox.equals(""))
        {
            Toast.makeText(Registro.this, "El campo USUARIO es obligatorio", Toast.LENGTH_SHORT).show();
            progressBarRegistro.setVisibility(View.GONE);
            return;
        }
        if (nombreUsuario.equals(""))
        {
            Toast.makeText(Registro.this, "El campo NOMBRE es obligatorio", Toast.LENGTH_SHORT).show();
            progressBarRegistro.setVisibility(View.GONE);
            return;
        }
        if (apellidoUsuario.equals(""))
        {
            Toast.makeText(Registro.this, "El campo APELLIDO es obligatorio", Toast.LENGTH_SHORT).show();
            progressBarRegistro.setVisibility(View.GONE);
            return;
        }
        if (emailUsuario.equals(""))
        {
            Toast.makeText(Registro.this, "El campo EMAIL es obligatorio", Toast.LENGTH_SHORT).show();
            progressBarRegistro.setVisibility(View.GONE);
            return;
        }
        if (celularUsuario.equals(""))
        {
            Toast.makeText(Registro.this, "El campo CELULAR es obligatorio", Toast.LENGTH_SHORT).show();
            progressBarRegistro.setVisibility(View.GONE);
            return;
        }
        if (passUsuario.equals(""))
        {
            Toast.makeText(Registro.this, "El campo CONTRASEÑA es obligatorio", Toast.LENGTH_SHORT).show();
            progressBarRegistro.setVisibility(View.GONE);
            return;
        }

/*

        BaseDatos baseDatos = new BaseDatos( getApplicationContext() );

        Usuario miUsuario = new Usuario( 1, codigoTercerox, nombreUsuario, apellidoUsuario, emailUsuario, celularUsuario, passUsuario );
        baseDatos.registrarUsuario( miUsuario );
        finish();
*/

        _webServiceRegistroCliente();

    }


private void _webServiceRegistroCliente()
{
    String _urlWebService = vars.ipServer.concat("/tr_panel/wsr/insertarCliente");




    progressBarRegistro.setVisibility(View.VISIBLE);

    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, _urlWebService, null,
            new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    try
                    {


                        Log.i("registro","codigoTercerox: "+codigoTercerox);
                        Log.i("registro","nombreUsuario: "+nombreUsuario);
                        Log.i("registro","apellidoUsuario: "+apellidoUsuario);
                        Log.i("registro","emailUsuario: "+emailUsuario);
                        Log.i("registro","passUsuario: "+passUsuario);
                        Log.i("registro","celularUsuario: "+celularUsuario);
                        Log.i("registro","result"+response);
                        boolean result = response.getBoolean("result");

                        if(result)
                        {
                            progressBarRegistro.setVisibility(View.GONE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                            builder
                                    .setTitle("REGISTRO CLIENTE")
                                    .setMessage("El registro de Cliente ha sido exitoso, proceda al ingreso del App.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            //Intent intent = new Intent(Registro.this, Login.class);
                                           // startActivity(intent);
                                            finish();
                                        }
                                    }).show().setCancelable(false);
                        }

                        else
                        {

                                progressBarRegistro.setVisibility(View.GONE);

                                AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                                builder
                                        .setTitle("REGISTRO CLIENTE")
                                        .setMessage("Error al registrarse, por favor contacte al Administrador del App.")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                Intent intent = new Intent(Registro.this, Login.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).show().setCancelable(false);

                        }
                    }

                    catch (JSONException e)
                    {
                        // progressBarLogin.setVisibility(View.GONE);

                        progressBarRegistro.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
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

                    progressBarRegistro.setVisibility(View.GONE);
                    if (error instanceof TimeoutError)
                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
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

                        progressBarRegistro.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
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

                        progressBarRegistro.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
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

                        progressBarRegistro.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
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

                        progressBarRegistro.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
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

                        progressBarRegistro.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
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
        public java.util.Map<String, String> getHeaders() throws AuthFailureError
        {
            HashMap<String, String> headers = new HashMap <String, String>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            headers.put("WWW-Authenticate", "xBasic realm=".concat(""));
            headers.put("codigoTercerox", codigoTercerox);
            headers.put("nombreTercerox", nombreUsuario);
            headers.put("apell1Tercerox", apellidoUsuario);
            headers.put("emailxTercerox", emailUsuario);
            headers.put("movilxTercerox", celularUsuario);
            headers.put("clavexUsuariox", passUsuario);
            return headers;
        }

    };

    jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    ControllerSingleton.getInstance().addToReqQueue(jsonObjReq,"");
}
}
