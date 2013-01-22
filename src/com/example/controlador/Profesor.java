package com.example.controlador;

import java.util.ArrayList;

import android.content.Context;

import com.example.data.AdapterDatabase;

public class Profesor extends Modelo {

	// Profesores:
	// iidP, idP, usuario, contrasena, nombre, apellido
	private static String nombreTabla = "Profesores";
	private static String[] keys;

	public Profesor() {
		this.NombreTabla = nombreTabla;
	}

	public Profesor(Context context, int idP, String usuario,String contrasena, String nombre, String apellido) throws Exception {
		super(nombreTabla,AdapterDatabase.tablas.get(nombreTabla).keys,	new String[] {"" + idP, usuario, contrasena, nombre, apellido});
		this.NombreTabla = nombreTabla;
	}

	@Override
	public void setData(String id, Object[] params) {
		this.NombreTabla = nombreTabla;
		for (int i = 1; i <= keys.length; i++) {
			this.params.put(this.keys[i-1], (String) params[i-1]);
		}

	}

	public static void setKeys(String[] keys, String nombreTabla) {
		Profesor.keys = keys;
		Profesor.nombreTabla = nombreTabla;
	}
	
	public static ArrayList<Profesor> getIidPwhereIdP(Context context, String idP) // DEPRECATED???
	{
		AdapterDatabase ad = new AdapterDatabase(context);

		ArrayList<Profesor> p = ad.getRecordWhere(Profesor.class, nombreTabla,new String[] { "idP" }, new String[] { idP }, null,null, null, null);
		return p;
	}
	
	public String getIidP() {
		return (String) this.params.get("iidP");
	}
	

}
