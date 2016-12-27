package com.ingenia.appacarreo.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.ingenia.appacarreo.Helper.gestionSharedPreferences;
import com.ingenia.appacarreo.R;
import com.ingenia.appacarreo.Vars.vars;
import com.ingenia.appacarreo.Volley.ControllerSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity
{

    public vars vars;

    private gestionSharedPreferences sharedPreferences;
    private TextView editTextEmail, editTextPassword, textRegistro;
    private Button buttonIniciarSesion;
    private ProgressBar progressBarLogin;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutClave;

    private Boolean saveLogin;

    private String emailUser,claveUser,_urlWebService;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLogin);

        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        vars  = new vars();
        sharedPreferences = new gestionSharedPreferences(getApplicationContext());

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        textInputLayoutClave = (TextInputLayout) findViewById(R.id.input_layout_password);

        editTextEmail = (TextView) findViewById(R.id.editTextEmail);
        editTextPassword = (TextView) findViewById(R.id.textViewPassword);

        textRegistro = (TextView) findViewById(R.id.textViewRegistroLogin);

        textRegistro.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(Login.this, Registro.class);
                startActivity(intent);
                // overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);

        buttonIniciarSesion = (Button) findViewById(R.id.buttonIniciarSesion);
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                progressBarLogin.setVisibility(View.VISIBLE);
                Login();
            }
        });

        editTextEmail.addTextChangedListener(new RevisorText(editTextEmail));
        editTextPassword.addTextChangedListener(new RevisorText(editTextPassword));

        saveLogin = sharedPreferences.getBoolean("GuardarSesion");

        if (saveLogin == true)
        {
            cargarActivitys();
            editTextEmail.setText(sharedPreferences.getString("email"));
            editTextPassword.setText(sharedPreferences.getString("clave"));
            //recordarClave.setChecked(true);
        }

    }

    private void Login()
    {
        if (!validateEmail())
        {
            return;
        }

        if (!validatePassword())
        {
            return;
        }

        emailUser = editTextEmail.getText().toString();
        claveUser = editTextPassword.getText().toString();

        _webServiceLogin(emailUser, claveUser);

    }

    public void cargarActivitys()
    {
       /* if(sharedPreferences.getString("indicaConducto").equals("0"))
        {
            Intent intent = new Intent(Login.this, Servicio.class);
            startActivity(intent);
            sharedPreferences.putBoolean("GuardarSesion", true);
            Login.this.finish();
        }
        else

        if(sharedPreferences.getString("indicaConducto").equals("1"))
        {
            Intent intent = new Intent(Login.this, ServicioDisponibleConductor.class);
            startActivity(intent);
            sharedPreferences.putBoolean("GuardarSesion", true);
            Login.this.finish();
        }
*/
        Intent intent = new Intent(Login.this, Inicio.class);
        startActivity(intent);
        sharedPreferences.putBoolean("GuardarSesion", true);
        Login.this.finish();

    }


    private boolean validateEmail()
    {
        String email = editTextEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email))
        {
            textInputLayoutEmail.setError("Digite email correctamente");//cambiar a edittext en register!!
            requestFocus(editTextEmail);
            return false;
        }

        else
        {
            textInputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword()
    {
        if (editTextPassword.getText().toString().trim().isEmpty())
        {
            textInputLayoutClave.setError("Digite su password, no puede estar vacio");
            requestFocus(editTextPassword);
            return false;
        }

        else
        {
            textInputLayoutClave.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email)
    {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view)
    {
        if (view.requestFocus())
        {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class RevisorText implements TextWatcher
    {

        private View view;

        private RevisorText(View view)
        {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        public void afterTextChanged(Editable editable)
        {
            switch (view.getId())
            {
                case R.id.input_layout_email:
                    validateEmail();
                    break;
                case R.id.input_layout_password:
                    validatePassword();
                    break;

            }
        }
    }

    private void _webServiceLogin(final String emailUser , final String claveUser)
    {
        _urlWebService = vars.ipServer.concat("/tr_panel/wsr/login");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, _urlWebService, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            Boolean status = response.getBoolean("status");
                            JSONObject usuario;

                            if(status)
                            {

                               /* String nombre = response.getString("nombreUsuariox");
                                String apellidos = response.getString("apellidosUsuario");
                                String nombreUsuario = (nombre+" "+apellidos);
                                String emailUser = response.getString("emailUsuario");
                                String tipoUsuario = response.getString("tipoUsuario");
                                String serialUsuario = response.getString("serialUsuario");
                                String token = response.getString("MyToken");

                                sharedPreferences.putString("nombreUsuario",nombreUsuario);
                                sharedPreferences.putString("emailUser",emailUser);
                                sharedPreferences.putString("MyToken",token);*/

                                progressBarLogin.setVisibility(View.GONE);

                                usuario = response.getJSONObject("usuario");

                                //DATOS DEL USUARIO EN BD LOCAL
                                sharedPreferences.putString("codigoTercerox",usuario.getString("codigoTercerox"));
                                sharedPreferences.putString("emailxTercerox",usuario.getString("emailxTercerox"));
                                sharedPreferences.putString("nombreUsuario",usuario.getString("nombre"));
                                sharedPreferences.putString("indicaClientex",usuario.getString("indicaClientex"));
                                sharedPreferences.putString("indicaConducto",usuario.getString("indicaConducto"));
                                sharedPreferences.putString("codigoVehclase",usuario.getString("codigoVehclase"));
                                sharedPreferences.putString("numeroPlacaxxx",usuario.getString("numeroPlacaxxx"));
                                sharedPreferences.putString("MyToken",usuario.getString("MyToken"));

                                Log.i("MyToken",""+sharedPreferences.getString("MyToken"));
                                Log.i("MyToken","codigoTercerox"+sharedPreferences.getString("codigoTercerox"));
                                Log.i("codigoVehclase","codigoVehclase: "+sharedPreferences.getString("codigoVehclase"));


                                //AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                //builder
                                //      .setMessage(message)
                                //    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                //  {
                                //    @Override
                                //  public void onClick(DialogInterface dialog, int id)
                                //{

                                /*if(sharedPreferences.getString("indicaConducto").equals("0"))
                                {
                                    Intent intent = new Intent(Login.this, Servicio.class);
                                    startActivity(intent);
                                    sharedPreferences.putBoolean("GuardarSesion", true);
                                    Login.this.finish();
                                }

                                else

                                if(sharedPreferences.getString("indicaConducto").equals("1"))
                                {
                                    Intent intent = new Intent(Login.this, ServicioDisponibleConductor.class);
                                    startActivity(intent);
                                    sharedPreferences.putBoolean("GuardarSesion", true);
                                    Login.this.finish();
                                }*/

                                Intent intent = new Intent(Login.this, Inicio.class);
                                startActivity(intent);
                                sharedPreferences.putBoolean("GuardarSesion", true);
                                Login.this.finish();



                            }

                            else
                            {

                                    String message = response.getString("message");

                                    progressBarLogin.setVisibility(View.GONE);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                    builder
                                            .setMessage(message)
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id)
                                                {

                                                }
                                            }).show();

                            }
                        }

                        catch (JSONException e)
                        {
                            progressBarLogin.setVisibility(View.GONE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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

                        progressBarLogin.setVisibility(View.GONE);
                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap <String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("WWW-Authenticate", "xBasic realm=".concat(""));
                headers.put("user", emailUser);
                headers.put("pass", claveUser);
                return headers;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ControllerSingleton.getInstance().addToReqQueue(jsonObjReq,"");
    }

}
