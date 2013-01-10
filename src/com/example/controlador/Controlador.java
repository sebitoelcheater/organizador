package com.example.controlador;

import android.content.Context;

import com.example.data.*;

public class Controlador  //NOTA: REVISAR BIEN LOS METODOS DEL CONTROLADOR....PENSAR BIEN EN SU VERDADERA FUNCIONALIDAD!
{

	static public String insertarProfesor(Context context,String idP,String usuario,String contrasena,String nombre, String apellido)//DEPRECATED, despues se usara el objeto profesor
	{
		AdapterDatabase db = new AdapterDatabase(context);
		;
		String[] params = new String[]{""+idP,usuario,contrasena,nombre,apellido};
	    String id = db.insertRecord("Profesores",params)+"";    
	    
		return id;
	}
	
	static public String insertarComentario(Context context,String idCom, String iidH, String fecha,String comentario)//CUANDO MANEJE ESTO COMO OBJETO PUEDO INTERPRETAR EL fecha COMO UN CALENDAR :)
	{
		AdapterDatabase db = new AdapterDatabase(context);
		        
		String[] params = new String[]{idCom,iidH,fecha,comentario};
		String id = db.insertRecord("Comentarios",params)+"";
	    
		return id;
	}
	

	
	public static int getRed(String color)
	{
		return Integer.parseInt(color.substring(0, 3));
	}
	
	public static int getGreen(String color)
	{
		return Integer.parseInt(color.substring(4, 7));
	}
	
	public static int getBlue(String color)
	{
		return Integer.parseInt(color.substring(8, 11));
	}
	
	public static String agregarCeros(int n, int i) {
		// TODO Auto-generated method stub
		String numero = i+"";
		int longitud = n-numero.length();
		if(numero.length()<n)
		{
			for(int j =0; j<longitud;++j)
			{
				numero = "0"+numero;
			}	
		}
		
		return numero;
	}

}
