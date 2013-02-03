package com.example.controlador;

import java.util.ArrayList;

import android.content.Context;

import com.example.data.AdapterDatabase;

public class Profesor extends Modelo {

	// Profesores:
	// iidP, idM, usuario, contrasena, nombre, apellido
	private static String nombreTabla = "Profesores";
	private static String[] keys;

	public static String getNombreTabla() {
		return nombreTabla;
	}

	public static String[] getKeys() {
		return keys;
	}

	public Profesor() {
		this.NombreTabla = nombreTabla;
	}

	public Profesor(Context context, int idM, String usuario,String contrasena, String nombre, String apellido) throws Exception {
		super(nombreTabla,AdapterDatabase.tablas.get(nombreTabla).getKeys(),	new String[] {"" + idM, usuario, contrasena, nombre, apellido});
		this.NombreTabla = nombreTabla;
	}

	@Override
	public void setData( Object[] params) {

		if(params.length==keys.length)
		{
			for (int i = 0; i < keys.length; i++) {
				this.params.put(this.keys[i], params[i]);
			}
		}
		else
		{
			for (int i = 1; i < keys.length; i++) {
				this.params.put(this.keys[i], params[i-1]);
			}
			
		}
	}

	public static void setKeys(String[] keys, String nombreTabla) {
		Profesor.keys = keys;
		Profesor.nombreTabla = nombreTabla;
	}
	
	public static ArrayList<Profesor> getIidPwhereIdP(Context context, String idM) // DEPRECATED???
	{
		AdapterDatabase ad = new AdapterDatabase(context);

		ArrayList<Profesor> p = ad.getRecordWhere(Profesor.class, nombreTabla,new String[] { "idM" }, new String[] { idM }, null,null, null, null);
		return p;
	}
	
	public String getIidP() {
		return (String) this.params.get("iidP");
	}

	public String getAccion() {
		
		return this.params.get("accion").toString();
	}

	@Override
	public String getIid() {
		if(params.containsKey(keys[0]))
			return (String) this.params.get(keys[0]);
		return null;
	}
	

}
