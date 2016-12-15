package com.ingenia.appacarreo.Beans;

/**
 * Created by davidcaicedo on 29/06/16.
 */
public class Usuario
{
    private int codigoUsuario;
    private String codigoTercerox;
    String nombreUsuario;
    String apellidoUsuario;
    String emailUsuario;
    String celularUsuario;
    String passUsuario;

    public Usuario()
    {
    }

    public Usuario(int codigoUsuario, String codigoTercerox, String nombreUsuario, String apellidoUsuario,
                   String emailUsuario, String celularUsuario, String passUsuario )
    {
        super();
        this.codigoUsuario = codigoUsuario;
        this.codigoTercerox = codigoTercerox;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.emailUsuario = emailUsuario;
        this.celularUsuario = celularUsuario;
        this.passUsuario = passUsuario;
    }

    // Metodos Get
    public int getCodigoUsuario()
    {
        return this.codigoUsuario;
    }
    public String getCodigoTercerox()
    {
        return this.codigoTercerox;
    }
    public String getNombreUsuario()
    {
        return this.nombreUsuario;
    }
    public String getApellidoUsuario()
    {
        return this.apellidoUsuario;
    }
    public String getEmailUsuario()
    {
        return this.emailUsuario;
    }
    public String getCelularUsuario()
    {
        return this.celularUsuario;
    }
    public String getPassUsuario()
    {
        return this.passUsuario;
    }

    // Metodos Set
    public void setCodigoUsuario( int codigoUsuario )
    {
        this.codigoUsuario = codigoUsuario;
    }
    public void setCodigoTercerox( String codigoTercerox )
    {
        this.codigoTercerox = codigoTercerox;
    }
    public void setNombreUsuario( String nombreUsuario )
    {
        this.nombreUsuario = nombreUsuario;
    }
    public void setApellidoUsuario( String apellidoUsuario ){ this.apellidoUsuario = apellidoUsuario; }
    public void setEmailUsuarioUsuario( String emailUsuario )
    {
        this.emailUsuario = emailUsuario;
    }
    public void setCelularUsuario( String celularUsuario )
    {
        this.celularUsuario = celularUsuario;
    }
    public void setPassUsuario( String passUsuario )
    {
        this.passUsuario = passUsuario;
    }

}