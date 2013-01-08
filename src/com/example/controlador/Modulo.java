package com.example.controlador;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import android.annotation.SuppressLint;
import android.content.Context;
import com.example.data.*;
public class Modulo extends Modelo
{								// 0      1     2      3      4       5       6
	static String[] keys = new String[]{"iidH","idH","iidC","dds","inicio","fin","ubicación"};
	static String nombreTabla = "Horarios";
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
	
	public Modulo(Context context,int idH,int iidC,int dds, Calendar inicio, Calendar fin, String ubicacion) throws Exception
	{
		super(nombreTabla, keys, new Object [] {idH, iidC, dds, inicio, fin, ubicacion});
		AdapterDatabase db = new AdapterDatabase(context);
		
		String stringInicio = agregarCeros(2,inicio.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,inicio.get(Calendar.MINUTE));
		String stringFin = agregarCeros(2,fin.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,fin.get(Calendar.MINUTE));
		;        
		
		boolean b = db.comprobarTopeHorario(dds+"",stringInicio,stringFin).size()==0;
		if(b)
			db.insertRecord(nombreTabla,new String[]{""+idH,""+iidC,dds+"",stringInicio, stringFin, ubicacion});        
	    
		}
	
	static public ArrayList<Modulo> obtenerModulosPorIdCurso(Context context,String idCurso)
	{
		ArrayList<Modulo> modulos = new ArrayList<Modulo>();
		AdapterDatabase db = new AdapterDatabase(context);
		;
		ArrayList<Modulo> c = db.getRecordPorCursoHORARIOS(idCurso);
		 
				
		return modulos;
	} 
	
	static public Modulo ultimoModulo(Context context,Curso c) 
	{
		
		Modulo ultimoModulo = null;    
	    Modulo primerModulo;
		Modulo anterior = ultimoModulo;
		
		Calendar ahora = Calendar.getInstance();
		
		ArrayList<Modulo> modulosDeC = obtenerModulosPorIdCurso(context,c.obtenerId());
		if(modulosDeC.size()>=1)
		{
			ArrayList<Modulo> anteriores = new ArrayList<Modulo>();
			ArrayList<Modulo> posteriores = new ArrayList<Modulo>();
			ultimoModulo = modulosDeC.get(0);
			primerModulo = ultimoModulo;
			for(Modulo m:modulosDeC)
			{
				if(m.obtenerInicio().after(ultimoModulo.obtenerInicio()))
					ultimoModulo = m;
				
				if(m.obtenerInicio().before(primerModulo.obtenerInicio()))
					primerModulo = m;
				
				if(m.obtenerInicio().get(Calendar.DAY_OF_WEEK)>=ahora.get(Calendar.DAY_OF_WEEK))
				{
					if(m.obtenerInicio().get(Calendar.HOUR_OF_DAY)>=ahora.get(Calendar.HOUR_OF_DAY))
					{
						if(m.obtenerInicio().get(Calendar.MINUTE)>=ahora.get(Calendar.MINUTE))
						{
							anteriores.add(m);
						}
						else
						{
							posteriores.add(m);
						}
					}
					else
					{
						posteriores.add(m);
					}
				}
				else
				{
					posteriores.add(m);
				}
			}
		
			if(anteriores.size()>=1)
			{	
				anterior = anteriores.get(0);
				for(Modulo a : anteriores)
				{
					if(a.obtenerFin().after(anterior))
						anterior = a;
				}
			}	
			else
			{
				anterior = posteriores.get(0);
				for(Modulo a : posteriores)
				{
					if(a.obtenerFin().before(anterior))
						anterior = a;
				}
			}
			
		}	
		return anterior;
	}
	
	static public boolean crearNuevoModulo(Context context,int idH,int iidC,int diaDeLaSemana, Calendar inicio, Calendar fin, String nombre)
	{
		
		AdapterDatabase db = new AdapterDatabase(context);
		String stringInicio = agregarCeros(2,inicio.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,inicio.get(Calendar.MINUTE));
		String stringFin = agregarCeros(2,fin.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,fin.get(Calendar.MINUTE));
		;        
		
		boolean b = db.comprobarTopeHorario(diaDeLaSemana+"",stringInicio,stringFin).size()==0;
		if(b)
			db.insertRecord("Horarios",new String[]{""+idH,""+iidC,diaDeLaSemana+"",stringInicio, stringFin, nombre});        
	    
		
		
		return b;
		
	}	
	
	static public boolean puedoCambiarModulo(Context context,int dds, Calendar inicio,Calendar fin, Modulo moduloAEditar)
	{
		AdapterDatabase db = new AdapterDatabase(context);
		String stringInicio = agregarCeros(2,inicio.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,inicio.get(Calendar.MINUTE));
		String stringFin = agregarCeros(2,fin.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,fin.get(Calendar.MINUTE));
		
		;        
		ArrayList<Modulo> c =db.comprobarTopeHorario(dds+"",stringInicio,stringFin);
		
		boolean b = c.size()==0;
		if((!b) && c.size()==1)
			if((moduloAEditar.obtenerId().equals("iidH")))
				b = true;
		
		return b;
	}
	
	static public ArrayList<Modulo> obtenerModulos(Context context)
	{
		AdapterDatabase db = new AdapterDatabase(context);
		ArrayList<Modulo> modulos =db.getAllRecords(Modulo.class,"Horarios");
		
		return modulos;
	}
	
	static public ArrayList<Modulo> obtenerModulosSegunDiaOrdenadosSegunInicio(Context context, int dia) //INEFICIENTE!!!!, el dia es de 1 a 7 siendo 1 el domingo
	{
		AdapterDatabase db = new AdapterDatabase(context);
		ArrayList<Modulo> modulosNoOrdenados =  db.getAllRecords(Modulo.class,"Horarios");
		ArrayList<Modulo> modulos = new ArrayList<Modulo>();
		     for(Modulo modulo : modulosNoOrdenados)
		     {
		    	
		          if (modulo.obtenerDiaDeLaSemana().equals(dia+""))
		        	modulos.add(modulo);
		     }
		     
		     quicksortModulo(modulos);	 
		return modulos;
	}
	/***
	 * Ordena segun hora de inicio
	 * @param x
	 */
	public static void quicksortModulo(ArrayList<Modulo> x)
	{
		quicksortRecursivo(x, 0, x.size()-1);
	}
	
	private static void quicksortRecursivo(ArrayList<Modulo> x,int lo,int ho)
	  {
	    int  l=lo, h=ho;
	    Modulo mid,t;
	 
	    if(ho>lo)
	    {
	      mid=x.get((lo+ho)/2);
	      while(l<h)
	      {
	        while((l<ho)&&(x.get(l).obtenerInicio().compareTo(mid.obtenerInicio())<0))  ++l;
	        while((h>lo)&&(x.get(h).obtenerInicio().compareTo(mid.obtenerInicio())>0))  --h;
	        if(l<=h)
	        {
	          t    = x.get(l);
	          x.set(l, x.get(h));
	          x.set(h, t);
	          ++l;
	          --h;
	        }
	      }
	 
	      if(lo<h) quicksortRecursivo(x,lo,h);
	      if(l<ho) quicksortRecursivo(x,l,ho);
	    }
	  }
	
	static public ArrayList<Modulo> obtenerModulosDelDia(Context context, Calendar hoydia) //INEFICIENTE!!!!
	{
		ArrayList<Modulo> modulos = new ArrayList<Modulo>();
		AdapterDatabase db = new AdapterDatabase(context);
		
		ArrayList<Modulo> modulosNoOrdenados = db.getAllRecords(Modulo.class,"Horarios");
		
		for(Modulo modulo: modulosNoOrdenados)
		{
			if (modulo.obtenerDiaDeLaSemana().equals(hoydia.get(Calendar.DAY_OF_WEEK)+""))
	        	modulos.add(modulo);
			
		}
		
				
		return modulos;
	}
	
	public static ArrayList<Modulo> obtenerLosFeedBackeables(
			Context context, Calendar ahora) {
		// TODO Auto-generated method stub
		int horas = 3;//Lo que espera el feedBack
		
		
		ArrayList<Modulo> posiblesModulos = obtenerModulosDelDia(context,ahora);
		ArrayList<Modulo> posibles2Modulos = new ArrayList<Modulo>();
		
		
		
		
		for (Modulo m : posiblesModulos)
		{
			AdapterDatabase ad = new AdapterDatabase(context);
       		
       		Curso c =ad.getRecord(Curso.class, "Cursos", Long.parseLong(m.obtenerIdCurso()));
			if(c.obtenerComentable().equals("1"))
			{
				posibles2Modulos.add(m);
			}
		
		}
		
		
		posiblesModulos =  new ArrayList<Modulo>();
		for(Modulo m : posibles2Modulos)
		{
			Calendar fin = m.obtenerFin();
			Calendar plazoMaximo = (Calendar) fin.clone();
			plazoMaximo.add(Calendar.HOUR, horas);
			Calendar aahora = (Calendar) fin.clone();
			aahora.set(Calendar.HOUR_OF_DAY, ahora.get(Calendar.HOUR_OF_DAY));
			aahora.set(Calendar.MINUTE, ahora.get(Calendar.MINUTE));
				
			if(aahora.before(plazoMaximo) && aahora.after(fin))
				posiblesModulos.add(m);
		}
		return posiblesModulos;
	}
	
	
	
	static public ArrayList<Modulo> obtenerLosModulosAnterioresFin(Context context,Calendar ahora, int minutos) //TERMINAR
	{
		ArrayList<Modulo> modulos = new ArrayList<Modulo>();
		Calendar antes = ((Calendar)ahora.clone());
		antes.add(Calendar.MINUTE,-minutos);
		
		String stringAhora = agregarCeros(2,ahora.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,ahora.get(Calendar.MINUTE));
		String stringAntes = agregarCeros(2,antes.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,antes.get(Calendar.MINUTE));
		
		AdapterDatabase db = new AdapterDatabase(context);
		;
		if(antes.get(Calendar.DAY_OF_WEEK)== ahora.get(Calendar.DAY_OF_WEEK))
		{
			ArrayList<Modulo> c = db.getRecordWhere(Modulo.class,"Horarios",new String[]{"dds"},new String[]{ahora.get(Calendar.DAY_OF_WEEK)+""},new String[]{"fin"},new String[]{stringAntes},new String[]{stringAhora},"fin");
	
		}	
		else
		{
			ArrayList<Modulo> c = db.getRecordWhere(Modulo.class,"Horarios",new String[]{"dds"},new String[]{ahora.get(Calendar.DAY_OF_WEEK)+""},new String[]{"fin"},new String[]{"00:00"},new String[]{stringAhora},"fin");
					
			c = db.getRecordWhere(Modulo.class,"Horarios",new String[]{"dds"},new String[]{ahora.get(Calendar.DAY_OF_WEEK)+""},new String[]{"fin"},new String[]{stringAntes},new String[]{"23:59"},"fin");
					
		}
		
		
		ArrayList<Modulo> modulosComentables = new ArrayList<Modulo>();//AQUI ESTA
		for(Modulo m: modulos )
		{
			   AdapterDatabase ad = new AdapterDatabase(context);
       		
       		Curso c =ad.getRecord(Curso.class, "Cursos", Long.parseLong(m.obtenerIdCurso()));
			if(c.obtenerComentable().equals("1"))
			{
				modulosComentables.add(m);
			}
		}
		
		return modulosComentables;
	}
	
	static public ArrayList<Modulo> obtenerLosModulosProximosInicio(Context context,Calendar ahora, int minutos) //TERMINAR
	{
		ArrayList<Modulo> modulos = new ArrayList<Modulo>();
		Calendar despues = ((Calendar)ahora.clone());
		despues.add(Calendar.MINUTE,minutos);
		
		String stringAhora = agregarCeros(2,ahora.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,ahora.get(Calendar.MINUTE));
		String stringDespues = agregarCeros(2,despues.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,despues.get(Calendar.MINUTE));
		
		AdapterDatabase db = new AdapterDatabase(context);
		;
		if(despues.get(Calendar.DAY_OF_WEEK)== ahora.get(Calendar.DAY_OF_WEEK))
		{
			ArrayList<Modulo> c = db.getRecordWhere(Modulo.class,"Horarios",new String[]{"dds"},new String[]{ahora.get(Calendar.DAY_OF_WEEK)+""},new String[]{"inicio"},new String[]{stringAhora},new String[]{stringDespues},"inicio");
			
			
		}	
		else
		{
			ArrayList<Modulo> c = db.getRecordWhere(Modulo.class,"Horarios",new String[]{"dds"},new String[]{ahora.get(Calendar.DAY_OF_WEEK)+""},new String[]{"inicio"},new String[]{stringAhora},new String[]{"23:59"},"inicio");
			
			
			c = db.getRecordWhere(Modulo.class,"Horarios",new String[]{"dds"},new String[]{ahora.get(Calendar.DAY_OF_WEEK)+""},new String[]{"inicio"},new String[]{"00:00"},new String[]{stringDespues},"inicio");
		
				
		}
		
		
		return modulos;
	}
	
	static public ArrayList<Modulo> obtenerLosSiguientesModulosDelDia(Context context, Calendar ahora, int largo)
	{
		ArrayList<Modulo> modulos  = new ArrayList<Modulo>();
		String hora = agregarCeros(2,ahora.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,ahora.get(Calendar.MINUTE));
		AdapterDatabase db = new AdapterDatabase(context);

		ArrayList<Modulo> c = db.getRecordWhere(Modulo.class,"Horarios", new String[]{"dds"}, new String[]{""+ahora.get(Calendar.DAY_OF_WEEK)}, new String[]{"inicio"}, new String[]{hora}, null, largo+"");
		
				
		return modulos; 
	}
	
	static public ArrayList<Modulo> obtenerLosSiguientesModulos(Context context, Calendar ahora, int largo)
	{
		
		String hora = agregarCeros(2,ahora.get(Calendar.HOUR_OF_DAY))+":"+agregarCeros(2,ahora.get(Calendar.MINUTE));
		AdapterDatabase db = new AdapterDatabase(context);
		
		
		ArrayList<Modulo> modulos = db.getModulosSiguientesHORARIOS(ahora.get(Calendar.DAY_OF_WEEK)+"",hora,largo+"");
		
		
		
		return modulos;
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