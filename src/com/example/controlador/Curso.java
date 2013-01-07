package com.example.controlador;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.NoHttpResponseException;

import android.content.Context;
import android.database.Cursor;
import android.widget.EditText;
import android.widget.Toast;

import com.example.data.*;
import com.example.server.server;
import com.example.server.server.NoExisteCursoException;
import com.example.version2.ActividadRamos;
import com.example.version2.R;
public class Curso extends Modelo
{

	private String id=null; //iidC
	private String nombre=null;
	private String idC=null; //idC
	private String idP; 
	private boolean comentable=false;
	private String color=null;
	private static String nombreTabla="Cursos";

	public Curso(){}
	
	public String obtenerId()
	{
		return id;
	}
	
	public String obtenerNombre()
	{
		return nombre;
	}
	
	public String obtenerIdMaster()
	{
		return idC;
	}
	
	public String obtenerIdP()
	{
		return idP;
	}
	
	public boolean obtenerComentable()
	{
		return comentable;
	}
	

		public boolean esEditable()
		{
			return !(esDescargado());
		}
		
		public boolean esDescargado()
		{
			return !(idC.equals("0"));
		}
		
		public String obtenerColor()
		{
			return color;
		}
	//METODOS DE OBTENCION
		
	//METODOS DE SETEO DEL OBJETO(NO DB)
		public void setId(String id)
		{
			this.id = id;
		}
		
		public void setNombre(String nombre)
		{
			
			this.nombre = nombre;
			
		}
		
		public void setIdMaster(String idMaster)
		{
			this.idC = idMaster;
		}
		
		public void setComentable(boolean comentable)
		{
			this.comentable = comentable;
		}
		
		public void setIdP(String idP)
		{
			this.idP = idP;
		}
		
		public void setColor(String string) {
			// TODO Auto-generated method stub
			color = string;
		}
	
		public boolean actualizar(Context context) throws UnknownHostException, NoHttpResponseException, NoExisteCursoException 
		{
			boolean b = true;
			if(esDescargado())
			{
				//ACTUALIZAR UN RAMO
				AdapterDatabase ad = new AdapterDatabase(context);
	        		
	        	Curso cursoActualizando =ad.getRecord(Curso.class, "Cursos", Long.parseLong(id));
				ArrayList<Modulo> modulosDelCurso = Controlador.obtenerModulosPorIdCurso(context, id);
				cursoActualizando.borrarCurso(context);
				
				try {
					server s = new server();
					
    				b = s.actualizarCurso(idC, context);
    			} 
    			catch(UnknownHostException uhe){
    				//REPONER LOS CURSOS
    				String ii = Controlador.crearNuevoCurso(context, Integer.parseInt(idC), Integer.parseInt(idP), nombre, comentable, color).obtenerId();
    				for(Modulo m : modulosDelCurso)
    				{
    					Controlador.crearNuevoModulo(context, Integer.parseInt(m.obtenerIdMaster()), Integer.parseInt(ii), Integer.parseInt(m.obtenerDiaDeLaSemana()), m.obtenerInicio(), m.obtenerFin(), m.obtenerUbicacion());
    				}
    				throw uhe;
    			}catch(NoHttpResponseException nhre)
    			{
    				String ii = Controlador.crearNuevoCurso(context, Integer.parseInt(idC), Integer.parseInt(idP), nombre, comentable, color).obtenerId();
    				for(Modulo m : modulosDelCurso)
    				{
    					Controlador.crearNuevoModulo(context, Integer.parseInt(m.obtenerIdMaster()), Integer.parseInt(ii), Integer.parseInt(m.obtenerDiaDeLaSemana()), m.obtenerInicio(), m.obtenerFin(), m.obtenerUbicacion());
    				}
    				throw nhre;
    			}
				catch(NoExisteCursoException nece)
    			{
    				//REPONER LOS CURSOS
    				String ii = Controlador.crearNuevoCurso(context, Integer.parseInt(idC), Integer.parseInt(idP), nombre, comentable, color).obtenerId();
    				for(Modulo m : modulosDelCurso)
    				{
    					Controlador.crearNuevoModulo(context, Integer.parseInt(m.obtenerIdMaster()), Integer.parseInt(ii), Integer.parseInt(m.obtenerDiaDeLaSemana()), m.obtenerInicio(), m.obtenerFin(), m.obtenerUbicacion());
    				}
    				throw nece;
    				
    			} 
    			catch (Exception e) {
				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
				
				
			}

			return b;
		}
		
		public boolean borrarCurso(Context context)  //DEPRECATED ???(DEBERIA IR EN EL CONTROLADOR?)
		{
			AdapterDatabase db = new AdapterDatabase(context);
			
					
			
			if(db.deleteRecord(nombreTabla, Long.parseLong(this.id)))
			{	
				
				return true;
			}
			 
			
			return false;
		}
	// METODOS DE SETEO DE LA BASE DE DATOS Y OBJETO

		@Override
		public void setData(String id, Object[] params) {
			
//			this.id = id;
//			private String id; //iidC
//			private String idC;
//			private String idP;
//			private String nombre;
//			private boolean comentable;
//			private String color;
//			private static String nombreTabla="Cursos";
			
//"iidC integer primary key autoincrement, idC integer, iidP integer, title VARCHAR not null, comentable integer, color VARCHAR not null"));
			this.id=(String) params[0];
			idC=(String) params[1];
			idP=(String) params[2];
			nombre=(String) params[3];
			comentable=Integer.parseInt((String) params[4])==1;
			color=(String) params[5];
		}
}
