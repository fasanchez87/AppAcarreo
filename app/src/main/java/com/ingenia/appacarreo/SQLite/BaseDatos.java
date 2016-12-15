package com.ingenia.appacarreo.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ingenia.appacarreo.Beans.Usuario;

import java.util.Vector;

/*
 * Created by davidcaicedo on 26/06/16.
 */

public class BaseDatos extends SQLiteOpenHelper
{
    public static final int bdVersion = 1;
    public static final String bdNombre = "appCarreos.db";

    private static final String sqlCrearTabla  =
            "CREATE TABLE tbl_usuario " +
                    "( codigoUsuario integer primary key autoincrement, "
                    + "codigoTercerox text,"
                    + "nombreUsuario text,"
                    + "apellidoUsuario text,"
                    + "emailUsuario text,"
                    + "celularUsuario text,"
                    + "passUsuario text"
                    + ");";

    public BaseDatos(Context context )
    {
        super(context, bdNombre, null, bdVersion );
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL( sqlCrearTabla );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    public void registrarUsuario( Usuario miUsuario )
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            int inscodigo = miUsuario.getCodigoUsuario();
            String inscodigoTercerox = miUsuario.getCodigoTercerox();
            String insnombreUsuario = miUsuario.getNombreUsuario();
            String insapellidoUsuario = miUsuario.getApellidoUsuario();
            String insemailUsuario = miUsuario.getEmailUsuario();
            String inscelularUsuario = miUsuario.getCelularUsuario();
            String inspassUsuario = miUsuario.getPassUsuario();

            values.put( "codigoUsuario", inscodigo );
            values.put( "codigoTercerox", inscodigoTercerox );
            values.put( "nombreUsuario", insnombreUsuario );
            values.put( "apellidoUsuario", insapellidoUsuario );
            values.put( "emailUsuario", insemailUsuario );
            values.put( "celularUsuario", inscelularUsuario );
            values.put( "passUsuario", inspassUsuario );

            // Devuelve 1 si se hizo el registro
            long x = db.insert( "tbl_usuario", null, values );
            db.close();

            /*
            String sql = "INSERT INTO tbl_usuario " +
                    "( "+
                        " codigoUsuario, codigoTercerox, nombreUsuario" +
                        " apellidoUsuario, emailUsuario, celularUsuario," +
                        " passUsuario " +
                    ")" +
                    "VALUES" +
                    "(" +
                        inscodigo + ", " + "'" + inscodigoTercerox +"', " + "'" + insnombreUsuario +"', " +
                        "'" + insapellidoUsuario +"', " + "'" + insemailUsuario +"', " + "'" + inscelularUsuario +"', " +
                        "'" + inspassUsuario +"' " +
                    ");";
            /*
            codigoUsuario integer primary key autoincrement, "
                    + "codigoTercerox text,"
                    + "nombreUsuario text,"
                    + "apellidoUsuario text,"
                    + "emailUsuario text,"
                    + "celularUsuario text,"
                    + "passUsuario text"
             *

          //  db.execSQL( sql );
            db.execSQL( "INSERT INTO tbl_usuario (codigoUsuario,codigoTercerox,nombreUsuario) VALUES ( 2,'David','Caicedo') ");
            //db.execSQL( "INSERT INTO tbl_usuario codigoUsuario" );*/

        }
        catch ( Exception excepcion )
        {
            excepcion.printStackTrace();
        }
    }

    public Vector<Usuario> obtenerUsuarios()
    {
        Vector<Usuario> vectorUsuarios = new Vector<Usuario>();

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM tbl_usuario;";
        Cursor cursor = db.rawQuery( sql, null );

        if ( cursor.moveToFirst() )
        {
            //Recorremos el cursor hasta que no haya más registros
            do
            {
                int codigoUsuario = cursor.getInt(0);
                String codigoTercerox = cursor.getString(1);
                String nombreUsuario = cursor.getString(2);
                String apellidoUsuario = cursor.getString(3);
                String emailUsuario = cursor.getString(4);
                String celularUsuario = cursor.getString(5);
                String passUsuario = cursor.getString(6);

                Usuario miUsuario = new Usuario( codigoUsuario, codigoTercerox, nombreUsuario, apellidoUsuario, emailUsuario, celularUsuario, passUsuario );

                vectorUsuarios.add( miUsuario );
            }
            while( cursor.moveToNext() );
        }

        db.close();

        return vectorUsuarios;
    }

    public void vaciarTablaUsuarios()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "DELETE FROM tbl_usuario";
        db.execSQL( sql );

        db.close();
    }
}
