package com.ingenia.appacarreo.Beans;

/**
 * Created by FABiO on 16/12/2016.
 */

public class Servicio
{

    private String direccionCargue;

    public String getDireccionDescargue() {
        return direccionDescargue;
    }

    public void setDireccionDescargue(String direccionDescargue) {
        this.direccionDescargue = direccionDescargue;
    }

    public String getDireccionCargue() {
        return direccionCargue;
    }

    public void setDireccionCargue(String direccionCargue) {
        this.direccionCargue = direccionCargue;
    }

    private String direccionDescargue;
    private String fecha;
    private String distancia;
    private String tarifa;
    private String auxiliares;
    private String requiereEmbalaje;
    private String latitudOrigen;

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    private String nombreCliente;

    private String codigoCliente;

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    private String telefonoCliente;

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    private String emailCliente;

    public String getLatitudOrigen() {
        return latitudOrigen;
    }

    public void setLatitudOrigen(String latitudOrigen) {
        this.latitudOrigen = latitudOrigen;
    }

    public String getLongitudOrigen() {
        return longitudOrigen;
    }

    public void setLongitudOrigen(String longitudOrigen) {
        this.longitudOrigen = longitudOrigen;
    }

    public String getLatitudDestino() {
        return latitudDestino;
    }

    public void setLatitudDestino(String latitudDestino) {
        this.latitudDestino = latitudDestino;
    }

    public String getLongitudDestino() {
        return longitudDestino;
    }

    public void setLongitudDestino(String longitudDestino) {
        this.longitudDestino = longitudDestino;
    }

    private String longitudOrigen;
    private String latitudDestino;
    private String longitudDestino;



    private String idServicio;


    public Servicio()
    {

    }

    public Servicio(String direccionCargue, String direccionDescargue, String fecha, String hora, String distancia,
                    String tarifa, String auxiliares, String requiereEmbalaje, String latOrigen,
                    String lonOrigen, String latDestino, String lonDestino)
    {
        this.direccionCargue = direccionCargue;
        this.direccionDescargue = direccionDescargue;
        this.fecha = fecha;
        this.distancia = distancia;
        this.tarifa = tarifa;
        this.auxiliares = auxiliares;
        this.requiereEmbalaje = requiereEmbalaje;
        this.latitudOrigen = latOrigen;
        this.longitudOrigen = lonOrigen;
        this.latitudDestino = latDestino;
        this.longitudDestino = lonDestino;

    }



    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }



    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    public String getAuxiliares() {
        return auxiliares;
    }

    public void setAuxiliares(String auxiliares) {
        this.auxiliares = auxiliares;
    }

    public String getRequiereEmbalaje() {
        return requiereEmbalaje;
    }

    public void setRequiereEmbalaje(String requiereEmbalaje) {
        this.requiereEmbalaje = requiereEmbalaje;
    }

}
