package com.example.controlador;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import android.annotation.SuppressLint;
import android.content.Context;
import com.example.data.*;
public class Modulo extends Modelo
{
	private String id=null;
	private String idH=null;
	private String idCurso=null;
	private String ubicacion=null;
	private String diaDeLaSemana=null;
	private Calendar inicio=null;
	private Calendar fin=null;
	
	public Modulo()
	{
		
	}
	
	public Modulo(Context context,String id)
	{
		this.id=id;
		
	}
	public String obtenerId()
	{
				return id;
	}
	public String getDiaDeLaSemana()
	{
		return diaDeLaSemana;
	}

			
	public String obtenerIdMaster()
	{
		return idH;
	}
			
	public String obtenerIdCurso()
	{
		return idCurso;
	}
			
	public String obtenerUbicacion()
	{
		return ubicacion;
	}
			
	public String obtenerDiaDeLaSemana()
	{
		return diaDeLaSemana;
	}
			
	public String obtenerNombreDiaDeLaSemana() 
	{
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
				return (Calendar) inicio.clone();
			}
			
			public Calendar obtenerFin()
			{
				return (Calendar) fin.clone();
			}
			
			public String obtenerStringInicio() {
				return  agregarCeros(2,inicio.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,inicio.get(Calendar.MINUTE));
			}
			
			public String obtenerStringFin() {
				return agregarCeros(2,fin.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,fin.get(Calendar.MINUTE));
			}
		//METODOS DE OBTENCION
		
		//METODOS DE SETEO DEL OBJETO(NO DB)
			public void setDiaDeLaSemana(String diaDeLaSemana) 
			{
				this.diaDeLaSemana = diaDeLaSemana;				
			}

			public void setId(String id)
			{
				this.id = id;
			}
			
			public void setIdMaster(String idMaster)
			{
				this.idH = idMaster;
			}
			
			public void setIdCrso(String idCurso)
			{
				this.idCurso = idCurso;
			}
			
			public void setUbicacion(String nombre)
			{
				this.ubicacion = nombre;
			}
			
			public void setInicio(Calendar inicio)
			{
				this.inicio = (Calendar) inicio.clone();
			}
			
			public void setFin(Calendar fin)
			{
				this.fin = (Calendar) fin.clone();
			}
	
			public boolean borrarModulo(Context context) //DEPRECATED ???(DEBERIA IR EN EL CONTROLADOR?)
			{
				AdapterDatabase db = new AdapterDatabase(context);
				
				if(db.deleteRecord("Horarios",Long.parseLong(this.id)))
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
				// TODO Auto-generated method stub
			

// iidH integer primary key autoincrement, idH integer, iidC integer, dds integer, inicio date, fin date, ubicacion VARCHAR"));
		    	
				Date a = new Date();
				Date b = new Date();
				inicio = new GregorianCalendar();
				fin = new GregorianCalendar();
				SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
				
				this.id=(String)params[0]; 
				idH=(String)params[1]; 
				idCurso=(String)params[2];
				diaDeLaSemana=(String)params[3];
				try {
					a = formato.parse((String) params[4]);
					b = formato.parse((String)params[5]);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				inicio.setTime(a);
				fin.setTime(b);
				ubicacion=(String)params[6];
			
				
				
			}

}