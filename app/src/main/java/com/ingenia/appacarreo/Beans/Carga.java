package com.ingenia.appacarreo.Beans;

/**
 * Created by FABiO on 25/11/2016.
 */

public class Carga
{

    private String idTipoCarga;
    private String nombreTipoCarga;

    public Carga()
    {

    }

    public Carga(String idTipoCarga, String nombreTipoCarga)
    {
        this.idTipoCarga = idTipoCarga;
        this.nombreTipoCarga = nombreTipoCarga;
    }

    public String getNombreTipoCarga()
    {
        return nombreTipoCarga;
    }

    public void setNombreTipoCarga(String nombreTipoCarga)
    {
        this.nombreTipoCarga = nombreTipoCarga;
    }

    public String getIdTipoCarga()
    {
        return idTipoCarga;
    }

    public void setIdTipoCarga(String idTipoCarga)
    {
        this.idTipoCarga = idTipoCarga;
    }


}
