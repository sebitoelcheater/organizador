package com.example.controlador;

import android.content.Context;

import com.example.data.AdapterDatabase;

public class Profesor {

	// Profesores:
	// iidP, idP, usuario, contrasena, nombre, apellido
	
	public Profesor(){
		
	}
	
	public Profesor(Context context, int idC, int iidP, String nombre,
			String comentable, String color) throws Exception {

		super(nombreTabla, AdapterDatabase.tablas.get(nombreTabla).keys,
				new String[] { "" + idC, "" + iidP, nombre, comentable, color });

		keys = AdapterDatabase.tablas.get(nombreTabla).keys;
		AdapterDatabase db = new AdapterDatabase(context);
		db.insertRecord(nombreTabla, new String[] { "" + idC, "" + iidP,
				nombre, comentable, color });

	}



}
