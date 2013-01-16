package com.example.controlador;

import java.util.HashMap;

import android.content.Context;

import com.example.data.AdapterDatabase;

public abstract class Modelo {// EX ISeteable

	protected String NombreTabla;
	protected HashMap<String, Object> params = new HashMap<String, Object>();

	protected Modelo() {
		NombreTabla = null;
		
	}

	protected Modelo(String nombreTabla, String[] keys, Object[] values)
			 {
		if (keys.length != values.length) {
			System.out.println(
					"Modelo/constructor largo de llaves != a de valores");
		}

		for (int i = 0; i < keys.length; i++) {
			params.put(keys[i], values[i]);
		}

		if (keys.length != params.size()) {
			System.out.println("Modelo/constructor HashMap mato parametros");
		}

		this.NombreTabla = nombreTabla;

	}

	public boolean save(Context ctx) {// Recordar que Modulo hace override de
										// este metodo
		AdapterDatabase ad = new AdapterDatabase(ctx);
		if (ad.insertRecord(NombreTabla, this.params.values().toArray()) > 0)
			return true;
		return false;
	}

	public abstract void setData(String id, Object[] params);

}
