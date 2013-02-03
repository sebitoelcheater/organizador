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
		this.NombreTabla = nombreTabla;
	}
	
	public Comentario(Context context, String idCom,
			String iidH, String fecha, String comentario) throws Exception
	{
		super(nombreTabla,keys,new String[] { idCom, iidH, fecha, comentario });
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
	
	public static void setKeys(String[] keys,String nombreTabla)
	{
		Comentario.keys = keys;
		Comentario.nombreTabla=nombreTabla;
	}

	public static String getNombreTabla() {
		// TODO Auto-generated method stub
		return nombreTabla;
	}

	@Override
	public String getIid() {
		if(params.containsKey(keys[0]))
			return (String) this.params.get(keys[0]);
		return null;
	}

}
