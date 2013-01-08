package com.example.controlador;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import android.annotation.SuppressLint;
import android.content.Context;
import com.example.data.*;
public class Modulo extends Modelo
{								// 0      1     2      3      4       5       6
	String[] keys = new String[]{"iidH","idH","iidC","dds","inicio","fin","ubicación"};
	String nombreTabla = "Horarios";
//	private String id=null;
//	private String idH=null;
//	private String idCurso=null;
//	private String ubicacion=null;
//	private String diaDeLaSemana=null;
//	private Calendar inicio=null;
//	private Calendar fin=null;
//	
	public Modulo()
	{
		
	}
	
	public String obtenerId()
	{
		return (String)this.params.get(keys[0]);
	}
	public String getDiaDeLaSemana()
	{
		//return diaDeLaSemana;
		return (String)this.params.get(keys[3]);
	}

			
	public String obtenerIdMaster()
	{
		//return idH;
		return (String)this.params.get(keys[1]);
	}
			
	public String obtenerIdCurso()
	{
		return (String)this.params.get(keys[2]);
	}
			
	public String obtenerUbicacion()
	{
		return (String)this.params.get(keys[6]);
	}
			
	public String obtenerDiaDeLaSemana()
	{
		return (String)this.params.get(keys[3]);
	}
			
	public String obtenerNombreDiaDeLaSemana() 
	{
		String diaDeLaSemana = obtenerDiaDeLaSemana();
		if (diaDeLaSemana.equals("2")){
			return "Lunes";
		}
		else if (diaDeLaSemana.equals("3")){
					return "Martes";
					
				}
				else if (diaDeLaSemana.equals("4")){
					return "Miércoles";
					
				}
				else if (diaDeLaSemana.equals("5")){
					return "Jueves";
				}
				else if (diaDeLaSemana.equals("6")){
					return "Viernes";
					
				}
				else if (diaDeLaSemana.equals("7")){
					return "Sábado";
					
				}
				else if (diaDeLaSemana.equals("1")){
					return "Domingo";
					
				}
				else {
					return "DIA "+diaDeLaSemana+" ";
				}
			}
			
			public Calendar obtenerInicio()
			{
				return (Calendar) ((Calendar)this.params.get(keys[4])).clone();
			}
			
			public Calendar obtenerFin()
			{
				return (Calendar) ((Calendar)this.params.get(keys[5])).clone();
			}
			
			public String obtenerStringInicio() {
				Calendar inicio= obtenerInicio();
				return  agregarCeros(2,inicio.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,inicio.get(Calendar.MINUTE));
			}
			
			public String obtenerStringFin() {
				Calendar fin = obtenerFin();
				return agregarCeros(2,fin.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,fin.get(Calendar.MINUTE));
			}
		//METODOS DE OBTENCION
		
		//METODOS DE SETEO DEL OBJETO(NO DB)

	
			public boolean borrarModulo(Context context) //DEPRECATED ???(DEBERIA IR EN EL CONTROLADOR?)
			{
				AdapterDatabase db = new AdapterDatabase(context);
				
				if(db.deleteRecord("Horarios",Long.parseLong(obtenerId())))
				{	
					
					return true;
				}
				
				return false;
			}
		// METODOS DE SETEO DE LA BASE DE DATOS Y OBJETO
			
			private static String agregarCeros(int n, int i) { 
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
			
			
		@SuppressLint("SimpleDateFormat")
		@Override
		public void setData(String id, Object[] params) {
			this.NombreTabla=nombreTabla;
			for(int i=0;i<keys.length;i++)
			{
				this.params.put(this.keys[i], (String)params[i]);
			}
			
				
				
			}

}