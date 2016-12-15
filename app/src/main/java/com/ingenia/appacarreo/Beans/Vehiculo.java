package com.ingenia.appacarreo.Beans;

/**
 * Created by FABiO on 25/11/2016.
 */

public class Vehiculo
{
    private String idVehiculo;
    private String nombreClaseVehiculo;

    public Vehiculo()
    {

    }

    public String getNombreClaseVehiculo() {
        return nombreClaseVehiculo;
    }

    public void setNombreClaseVehiculo(String nombreClaseVehiculo) {
        this.nombreClaseVehiculo = nombreClaseVehiculo;
    }

    public String getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(String idVehiculo) {
        this.idVehiculo = idVehiculo;
    }



}
