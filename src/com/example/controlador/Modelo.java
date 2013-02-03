package com.example.controlador;

import java.util.HashMap;
import java.util.LinkedHashMap;

import android.content.Context;

import com.example.data.AdapterDatabase;

public abstract class Modelo {// EX ISeteable

	protected String NombreTabla;
	protected LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();

	protected Modelo() {
		NombreTabla = null;
		
	}


	protected Modelo(String nombreTabla, String[] keys, Object[] values)
			 {
		if (keys.length -1 != values.length) {
			System.out.println("Modelo/constructor largo de llaves -1 != a de valores");
		}

		for (int i = 1; i < keys.length; i++) {
			params.put(keys[i], values[i-1]);
		}

		if (keys.length-1 != params.size()) {
			System.out.println("Modelo/constructor HashMap mato parametros");
		}

		this.NombreTabla = nombreTabla; 

	}

	public boolean save(Context ctx) {// Recordar que Modulo hace override de
										// este metodo
		AdapterDatabase ad = new AdapterDatabase(ctx);
		
		int id =(int) ad.insertRecord(NombreTabla, this.params.values().toArray());
		
		if (id > 0)
		{
			this.setParam(AdapterDatabase.getKeys(NombreTabla)[0], ""+id);
			return true;
		}
		return false;
	}

	/**
	 * Si el numero de parametros es igual o mayor al numero de keys, se tomaran en cuenta los primeros y se colocará el id interno, si no lo es, no se tomara el id interno (en este caso
	 * deben haber exactamente keys-1 parametros)
	 * @param params
	 */
	public abstract void setData(Object[] params);
	public boolean estaRegistrado(Context ctx)
	{
		try
		{
			AdapterDatabase ad = new AdapterDatabase(ctx);
			Object c =ad.getRecordIdMaster(this.getClass(), NombreTabla, (String)params.get(params.keySet().toArray()[1].toString()));
			if(c==null)
				return false;
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	protected void setParam(String key,String value)
	{
		params.put(key, value);
	
	}
	/**
	 * Retorna el valor correspondiente a esta key. Si no existe retorna null
	 * @param key
	 * @return
	 */
	public String getParam(String key)
	{
		try
		{
			return  params.get(key).toString();
		}catch(Exception e)
		{
			return null;
		}
	}
	/**
	 * Retorna su iid, o null si no esta.
	 * @return
	 */
	public abstract String getIid();
	
}
