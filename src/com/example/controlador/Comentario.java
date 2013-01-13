package com.example.controlador;

import android.content.Context;

import com.example.data.AdapterDatabase;

public class Comentario extends Modelo {
	
//	Comentarios:
//		iidCom, idCom, iidH, fecha, comentario
	public static String nombreTabla="Comentarios";
	public static String[] keys;
	
	public Comentario()
	{
		
	}
	
	public Comentario(Context context, String idCom,
			String iidH, String fecha, String comentario)
	{
		AdapterDatabase db = new AdapterDatabase(context);
		String[] params = new String[] { idCom, iidH, fecha, comentario };
		db.insertRecord("Comentarios", params) ;

		
	}

	@Override
	public void setData(String id, Object[] params) {
		// TODO Auto-generated method stub
		
		this.NombreTabla = nombreTabla;
		for (int i = 0; i < keys.length; i++) {
			this.params.put(this.keys[i], (String) params[i]);
		}
		
	}
	
	public static void setKeys(String[] _keys)
	{
		keys=_keys;
	}
	

}
