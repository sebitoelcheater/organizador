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

//                                                0       1       2     3       4           5
	//private static String[] keys = new String[]{"iidC","nombre","idC","idP","comentable","color"};
	private static String[] keys;

	static String[] getKeys() {
		return keys;
	}

	public static void setKeys(String[] keys) {
		Curso.keys = keys;
	}
	
	private static String nombreTabla="Cursos";
	public static String getNombreTabla()
	{
		return nombreTabla;
	}
	/**
	 * No lo usi conchetumare!
	 */
	public Curso()
	{
		
		
	}
	
	public Curso(Context context,int idC, int iidP, String nombre,String comentable,String color) throws Exception //CUANDO ESTO CRESCA NO OLVIDAR AGREGAR ACA NUEVAS CARACTERISTICAS
	{
		
		super(nombreTabla,AdapterDatabase.tablas.get(nombreTabla).keys,new String[]{""+idC,""+iidP,nombre,comentable,color});
		keys=AdapterDatabase.tablas.get(nombreTabla).keys;
		AdapterDatabase db = new AdapterDatabase(context);
		db.insertRecord(nombreTabla,new String[]{""+idC,""+iidP,nombre,comentable,color});
	    db.close();
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

	public boolean actualizar(Context context) throws Exception 
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

	public boolean existeCursoComentable(Context context)// Le das el idmaster y te dice si ya lo tienes en ls base de datos interna :)
	{
		AdapterDatabase db = new AdapterDatabase(context);
		
	    boolean b=db.getRecordWhere(Curso.class,"Cursos",new String[]{ "idC"},new String[]{obtenerIdMaster()},null,null,null,null).size()==0; 
		    
	    return !b;
			
	}
		
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
		
	static public Curso obtenerCurso(Context context, String id) //DEPRECATED???
	{
		AdapterDatabase ad = new AdapterDatabase(context);
  		
   		Curso c =ad.getRecord(Curso.class, nombreTabla, Long.parseLong(id));
		return c;
	}
		
	static public ArrayList<Curso> obtenerCursosOrdenados(Context context)//por esEditable == idC=0
	{
		AdapterDatabase db = new AdapterDatabase(context);
		ArrayList<Curso> cursos1 = db.getRecordWhere(Curso.class, nombreTabla, new String[]{keys[2]},new String[]{"0"} , null, null, null, null);
		ArrayList<Curso> cursos2 = db.getRecordWhere(Curso.class, nombreTabla, new String[]{keys[2]},new String[]{"1"} , null, null, null, null);
		ArrayList<Curso> ordenados = new ArrayList<Curso>();
		ordenados.addAll(cursos1);
		ordenados.addAll(cursos2);
		return ordenados;
	}

		static public ArrayList<Curso> obtenerCursosComentables(Context context)
		{
			
			AdapterDatabase db = new AdapterDatabase(context);
			ArrayList<Curso> cursos = db.getRecordWhere(Curso.class, nombreTabla, new String[]{keys[4]}, new String[]{"1"}, null, null, null, null);
		
	        return cursos;
			
		}
		public static ArrayList<Curso> obtenerCursosEditables(Context context) {
			// TODO Auto-generated method stub //idC=0
			
			AdapterDatabase db = new AdapterDatabase(context);
			
			return db.getRecordWhere(Curso.class, nombreTabla, new String[]{keys[2]}, new String[]{"0"}, null, null, null, null);
			
		}
		
}
