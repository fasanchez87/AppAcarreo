package com.ingenia.appacarreo.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ingenia.appacarreo.Beans.Servicio;
import com.ingenia.appacarreo.R;

import java.util.List;

/**
 * Created by FABiO on 16/12/2016.
 */

public class ServiciosAgendadosAdapter extends RecyclerView.Adapter <ServiciosAgendadosAdapter.MyViewHolder>
{
    private List<Servicio> solicitudServicioList;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView codigoSolicitud,telefonoCliente, direccionSolicitudOrigen,direccionSolicitudDestino, fechaSolicitud, distancia, tarifa, auxiliares, requiereEmbalaje;
        public Button ButtonconfirmarServicio;

        public MyViewHolder(View view)
        {
            super(view);

            codigoSolicitud = (TextView) view.findViewById(R.id.textViewCodigoSolicitudServicioAgendado);
            telefonoCliente = (TextView) view.findViewById(R.id.textViewTelefonoClienteServicioAgendado);
            direccionSolicitudOrigen = (TextView) view.findViewById(R.id.textViewDireccionOrigenServicioAgendado);
            direccionSolicitudDestino = (TextView) view.findViewById(R.id.textViewDireccionDestinoServicioAgendado);
            fechaSolicitud = (TextView) view.findViewById(R.id.textViewFechaServicioAgendado);
            distancia = (TextView) view.findViewById(R.id.textViewDistanciaServicioAgendado);
            tarifa = (TextView) view.findViewById(R.id.textViewTarifaServicioAgendado);
            requiereEmbalaje = (TextView) view.findViewById(R.id.textViewRequiereEmbalajeServicioAgendado);
            auxiliares = (TextView) view.findViewById(R.id.textViewCantidadAuxiliaresServicioAgendado);
            ButtonconfirmarServicio = (Button) view.findViewById(R.id.buttonIniciarServicioAgendado);

        }
    }

    public ServiciosAgendadosAdapter(List<Servicio> serviciosList)
    {
        this.solicitudServicioList = serviciosList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.servicio_agendado_row, parent, false);

        return new MyViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {

        final Servicio solicitudServicio = solicitudServicioList.get(position);
        holder.codigoSolicitud.setText(solicitudServicio.getIdServicio());
        holder.telefonoCliente.setText(solicitudServicio.getTelefonoCliente());
        holder.direccionSolicitudOrigen.setText(solicitudServicio.getDireccionCargue());
        holder.direccionSolicitudDestino.setText(solicitudServicio.getDireccionDescargue());
        holder.fechaSolicitud.setText(solicitudServicio.getFecha());
        holder.distancia.setText(solicitudServicio.getDistancia());
        holder.tarifa.setText(solicitudServicio.getTarifa());
        holder.requiereEmbalaje.setText(solicitudServicio.getRequiereEmbalaje());
        holder.auxiliares.setText(solicitudServicio.getAuxiliares());

    }

    @Override
    public int getItemCount()
    {
        return solicitudServicioList.size();
    }

    public List<Servicio> getSolicitudServicioList()
    {
        return solicitudServicioList;
    }
}
