package com.ingenia.appacarreo.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ingenia.appacarreo.Activity.ConfirmarServicioConductor;
import com.ingenia.appacarreo.Activity.Login;
import com.ingenia.appacarreo.Adapters.ServiciosDisponiblesAdapter;
import com.ingenia.appacarreo.Beans.Servicio;
import com.ingenia.appacarreo.Helper.gestionSharedPreferences;
import com.ingenia.appacarreo.R;
import com.ingenia.appacarreo.Vars.vars;
import com.ingenia.appacarreo.Volley.ControllerSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ServicioDisponibleConductor extends Fragment
{

    private MenuItem menuCerrarSesion;
    private gestionSharedPreferences sharedPreferences;
    private ArrayList<Servicio> serviciosDisponibles;
    private RecyclerView recyclerViewServiciosDisponibles;
    private ServiciosDisponiblesAdapter mAdapter;
    private ProgressDialog progressDialog;
    private ImageView imagenSinServicios;
    public Button ButtonconfirmarServicio;

    final NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);


    public vars vars;

    public ServicioDisponibleConductor()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sharedPreferences = new gestionSharedPreferences(getActivity().getApplicationContext());
        serviciosDisponibles = new ArrayList<Servicio>();

        vars = new vars();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conductor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewServiciosDisponibles = (RecyclerView) this.getActivity().findViewById(R.id.recycler_view_servicios_disponibles);
        imagenSinServicios = (ImageView) getActivity().findViewById(R.id.imagenSinServiciosSolicitados);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewServiciosDisponibles.setLayoutManager(layoutManager);
        mAdapter = new ServiciosDisponiblesAdapter(serviciosDisponibles);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        recyclerViewServiciosDisponibles.setLayoutManager(mLayoutManager);
        recyclerViewServiciosDisponibles.setItemAnimator(new DefaultItemAnimator());
        //recyclerViewServiciosDisponibles.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL));
        recyclerViewServiciosDisponibles.setAdapter(mAdapter);

        recyclerViewServiciosDisponibles.addOnItemTouchListener(new RecyclerTouchListener(this.getActivity(),
                recyclerViewServiciosDisponibles, new ClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                Servicio solicitudServicio = serviciosDisponibles.get(position);
                Intent intent = new Intent(ServicioDisponibleConductor.this.getActivity(), ConfirmarServicioConductor.class);
                intent.putExtra("direccCarguexx", solicitudServicio.getDireccionCargue());
                intent.putExtra("fechaSolicitud", solicitudServicio.getFecha());
                intent.putExtra("cantidConfirma", solicitudServicio.getDistancia());
                intent.putExtra("tarifaTotalxxx", solicitudServicio.getTarifa());
                intent.putExtra("cantidAuxiliar", solicitudServicio.getAuxiliares());
                intent.putExtra("indicaEmbalaje", solicitudServicio.getRequiereEmbalaje());
                intent.putExtra("codigoPediresu", solicitudServicio.getIdServicio());
                intent.putExtra("latituCarguexx", solicitudServicio.getLatitudOrigen());
                intent.putExtra("longitCarguexx", solicitudServicio.getLongitudOrigen());
                //intent.putExtra("direccDescargu", solicitudServicio.getIndicaAgenda());
                intent.putExtra("latituDescargu", solicitudServicio.getLatitudDestino());
                intent.putExtra("longitDescargu", solicitudServicio.getLongitudDestino());

                //DATOS CLIENTE QUE SOLICITA SERVICIO
                intent.putExtra("codigoClientex",solicitudServicio.getCodigoCliente());
                intent.putExtra("emailxTercerox",solicitudServicio.getEmailCliente());

                //Toast.makeText(getActivity(),"Codigo Cliente: "+solicitudServicio.getCodigoCliente()+"  :  "+"Email: "+solicitudServicio.getEmailCliente(),Toast.LENGTH_LONG).show();

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



       /* _webServiceObtenerSolicitudesdisponibles();

        mAdapter.notifyDataSetChanged();*/


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_cerrar_sesion).setVisible(true);
        menu.findItem(R.id.action_enviar_servicio).setVisible(false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_servicio, menu);
        // Get dynamic menu item
        menuCerrarSesion = menu.findItem(R.id.action_cerrar_sesion);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_cerrar_sesion:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                                Intent intent = new Intent(getActivity(), Login.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public interface ClickListener
    {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener
    {

        private GestureDetector gestureDetector;
        private ServicioDisponibleConductor.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ServicioDisponibleConductor.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e)
        {
        }




        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
        {

        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        _webServiceObtenerSolicitudesdisponibles();

        mAdapter.notifyDataSetChanged();


    }

    private void _webServiceObtenerSolicitudesdisponibles()
    {
        serviciosDisponibles.clear();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Consultando servicios disponibles, espera un momento...");
        progressDialog.show();
        progressDialog.setCancelable(false);
//        buttonSeleccionarServicios.setVisibility(View.GONE);

       final String _urlWebService = vars.ipServer.concat("/tr_panel/wsr/getPedidos");

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(_urlWebService,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try
                        {

                            Log.i("respon", "xxxx"+String.valueOf(response));

                            if(response.get(0).equals(0))
                            {
                                progressDialog.dismiss();
                                recyclerViewServiciosDisponibles.setVisibility(View.GONE);
                                imagenSinServicios.setVisibility(View.VISIBLE);
                            }

                            else
                            {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject jsonObject = (JSONObject) response.get(i);

                                    Servicio servicio = new Servicio();
                                    servicio.setIdServicio(jsonObject.getString("codigoPediresu"));
                                    servicio.setDireccionCargue(jsonObject.getString("direccCarguexx"));
                                    servicio.setDireccionDescargue(jsonObject.getString("direccDescargu"));
                                    servicio.setFecha(jsonObject.getString("fechaxInicioxx") + " " + jsonObject.getString("horaxxInicioxx"));
                                    servicio.setDistancia(jsonObject.getString("cantidConfirma") + " KM APROX.");
                                    servicio.setTarifa("$"+nf.format(Integer.parseInt(jsonObject.getString("tarifaTotalxxx"))));
                                    servicio.setRequiereEmbalaje(jsonObject.getString("indicaEmbalaje").equals("1") ? "SI" : "NO");
                                    servicio.setAuxiliares(jsonObject.getString("cantidAuxiliar"));

                                    servicio.setLatitudOrigen(jsonObject.getString("latituCarguexx"));
                                    servicio.setLongitudOrigen(jsonObject.getString("longitCarguexx"));
                                    servicio.setLatitudDestino(jsonObject.getString("latituDescargu"));
                                    servicio.setLongitudDestino(jsonObject.getString("longitDescargu"));

                                    //DATOS CLIENTE
                                    servicio.setCodigoCliente(jsonObject.getString("codigoClientex"));
                                    servicio.setEmailCliente(jsonObject.getString("emailxTercerox"));

                                    serviciosDisponibles.add(servicio);
                                }

                                recyclerViewServiciosDisponibles.setVisibility(View.VISIBLE);
                                imagenSinServicios.setVisibility(View.GONE);

                                mAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }


                        }
                        catch (JSONException e)
                        {

                            progressDialog.dismiss();                            // buttonSeleccionarServicios.setVisibility(View.GONE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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


                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        if (error instanceof TimeoutError)
                        {

                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
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
                headers.put("codigoVehclase", sharedPreferences.getString("codigoVehclase"));
                return headers;
            }
        };

        ControllerSingleton.getInstance().addToReqQueue(jsonObjReq, "");
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
