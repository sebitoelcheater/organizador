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

	private static String[] keys = new String[]{"iidC","nombre","idC","idP","comentable","color"};
	
	private static String nombreTabla="Cursos";

	public Curso()
	{
		
		
	}
	
	
	public String obtenerId()
	{
		return (String)this.params.get("iidC");
	}
	
	public String obtenerNombre()
	{
		return (String)this.params.get("nombre");
	}
	
	public String obtenerIdMaster()
	{
		return (String)this.params.get("idC");
	}
	
	public String obtenerIdP()
	{
		return (String)this.params.get("idP");
	}
	
	public String obtenerComentable()
	{
		return (String)this.params.get("comentable");
	}
	

		public boolean esEditable()
		{
			return !(esDescargado());
		}
		
		public boolean esDescargado()
		{
			return this.params.get("idC").equals("0");
		}
		
		public String obtenerColor()
		{
			return (String)this.params.get("color");
		}
	//METODOS DE OBTENCION
		
	//METODOS DE SETEO DEL OBJETO(NO DB)

		public boolean actualizar(Context context) throws UnknownHostException, NoHttpResponseException, NoExisteCursoException 
		{
			server s = new server();
			boolean b = s.actualizarCurso(obtenerIdMaster(), context);//seba va a arreglar 
    		return b;
		}
		
		public boolean borrarCurso(Context context)  //DEPRECATED ???(DEBERIA IR EN EL CONTROLADOR?)
		{
			AdapterDatabase db = new AdapterDatabase(context);
			
					
			
			if(db.deleteRecord(nombreTabla, Long.parseLong(obtenerId())))
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
			this.NombreTabla=nombreTabla;
			for(int i=0;i<keys.length;i++)
			{
				this.params.put(this.keys[i], (String)params[i]);
			}
			
		}
}
