package com.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.controlador.Controlador;
import com.example.controlador.Curso;
import com.example.controlador.Modulo;
import com.example.data.AdapterDatabase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class Server extends Activity {

	public void comentar (View view, int i, String comentario) throws Exception {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        comentario=comentario.replaceAll(" ", "%20");
        HttpPost httppost = new HttpPost("http://www.cheaper.cl/android/comentar.php?ramo="+i+"&comentario="+comentario+"");

        try {
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            
            
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        	e.printStackTrace();
        	throw e;
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	e.printStackTrace();
        	throw e;
        } 
	}
	
	public void comentar2(View view, Curso c,Modulo m, String comentario)
	{
		int idH = Integer.parseInt(m.obtenerIdMaster());
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        comentario=comentario.replaceAll(" ", "%20");
        HttpPost httppost = new HttpPost("http://www.cheaper.cl/android/funciones/comentar.php?idH="+idH+"&comentario="+comentario+"");
        try {
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
		//Hola seba esto es para que guardes el nuevo método que considerara el Curso asociado y el Modulo asociado(teniendo los objetos tienes todos los dato asociados a ellos... si necesitas ayuda me dices o revisas la documentacion del controlador)
		
	}
	
	public String getInternetData(String URL) throws Exception {
		BufferedReader in = null;
		String data = null;
		try{
			HttpClient client = new DefaultHttpClient();
			URI website = new URI(URL);
			HttpGet request = new HttpGet();
			request.setURI(website);
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String l = "";
			String nl = System.getProperty("line.separator");
			while ((l = in.readLine()) !=null){
				sb.append(l + nl);
			}
			in.close();
			data = sb.toString();
			return data;
		}finally{
			if (in != null){
				try{
					in.close();
					return data;
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public ArrayList<JSONObject> getCursoFromDatabase(String idC,String elemento) throws Exception{
		String URL = "http://www.cheaper.cl/android/suscribir.php?id="+idC+"";
		String JSONChain = getInternetData(URL);
		stringToJSON a = new stringToJSON();
		ArrayList<JSONObject> arreglo = a.getArray(JSONChain,elemento);
		return arreglo;
	}
	
	public boolean suscribirCurso (String id, Context ctx) throws Exception{
		
		boolean b = true;
		ArrayList<JSONObject> Profesor = getCursoFromDatabase(id,"Profesores");
		JSONObject Curso = getCursoFromDatabase(id,"Cursos").get(0);
		ArrayList<JSONObject> Horarios = getCursoFromDatabase(id,"Horarios");
		ArrayList<JSONObject> Comentarios = getCursoFromDatabase(id,"Comentarios");
		
		if(Profesor == null && Curso == null && Comentarios == null)
		{	
			throw new NoExisteCursoException("No existe CUrso");
		}
		String iidC=null,iidP = null,iidH=null,iidCom=null;
		
		for (int i = 0; i < Profesor.size(); i++) {
			String idP = Profesor.get(i).getString("idP");
			String usuario = Profesor.get(i).getString("usuario");
			String contrasena = Profesor.get(i).getString("contrasena");
			String nombre = Profesor.get(i).getString("nombre");
			String apellido = Profesor.get(i).getString("apellido");
			
			iidP = Controlador.insertarProfesor(ctx, idP, usuario, contrasena, nombre, apellido);
			// introducir nuevo profesor (si no est‡ introducido). Lo obtengo a partir de un for, pero es claro que arrojar‡ s—lo un elemento
        }
		
		{
			String idC = Curso.getString("idC");
			String idP = Curso.getString("idP");
			String titulo = arreglarCotejamiento(Curso.getString("titulo")).trim();
			String comentable = Curso.getString("comentable");
			String color = Curso.getString("color");
        	Curso c = Controlador.crearNuevoCurso(ctx, Integer.parseInt(idC), Integer.parseInt(iidP==null?"0":iidP), titulo, comentable.equals("1"),color);
			if(c!=null)
				iidC = c.obtenerId();
        	// introducir nuevo curso con funciones hechas por Ariel, con los par‡metros declarados en este for. Lo mismo para profe,horarios y comentarios
		}
		if(Horarios!= null){
		for (int i = 0; i < Horarios.size(); i++) {
			String idH=Horarios.get(i).getString("idH");
			String idC=Horarios.get(i).getString("idC");
			String dds=Horarios.get(i).getString("dds");
			String inicio=Horarios.get(i).getString("inicio");
			String fin=Horarios.get(i).getString("fin");
			String ubicacion=arreglarCotejamiento(Horarios.get(i).getString("ubicacion"));
			
			
			inicio = arreglaLo(inicio);
			fin = arreglaLo(fin);
			
			
			SimpleDateFormat formato = new SimpleDateFormat("HHmmss");
			Date a = new Date();
			Calendar cInicio = new GregorianCalendar();
			try
			{
				a = formato.parse(inicio);
				cInicio.setTime(a);
			}catch(ParseException e){}
			
			formato = new SimpleDateFormat("HHmmss");
			a = new Date();
			Calendar cFin = new GregorianCalendar();
			
			try
			{
				a = formato.parse(fin);
				cFin.setTime(a);
			}catch(ParseException e){}
        	
			boolean pudeCrearlo = Controlador.crearNuevoModulo(ctx, Integer.parseInt(idH), Integer.parseInt(iidC==null?"0":iidC), Integer.parseInt(dds), cInicio, cFin, ubicacion);
			
			
			if(!pudeCrearlo)
			{
				b = false;
			}
			// introducir nuevo horarios 
        }
		
		}
		/*
		for (int i = 0; i < Comentarios.size(); i++) {
			String idCom = Comentarios.get(i).getString("idCom");
			String idH = Comentarios.get(i).getString("idH");
			String fecha = Comentarios.get(i).getString("fecha");
			String comentario = Comentarios.get(i).getString("comentario");
			iidCom = Controlador.insertarComentario(ctx,idCom, iidH==null?"0":iidH,fecha,comentario);
        	// introducir nuevo comentarios 
        }
        */
		return b;
		// forma de obtener el campo "name" del usuario de idP 1 Profesor.get(1).getString("name");
		
	}
	
	private String[] getParamsCurso(JSONObject Curso)
	{
		try{
		String idC = Curso.getString("idC");
		String idP = Curso.getString("idP");
		String titulo = arreglarCotejamiento(Curso.getString("titulo")).trim();
		String comentable = Curso.getString("comentable");
		String color = Curso.getString("color");
		return new String[]{idC,idP,titulo,comentable,color};
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("getParamsCurso recibio algo mal en server.java");
			return null;
			
		}
		
	}
	
	public boolean actualizarCurso (String id, Context ctx) //retorna si hay un tope en la nueva edicion
		throws Exception{
		
		/*
		 * Hay que re-hacer este metodo haciendo que use los metodos de AdapterDatabase: deleteRecord, updateRecord e insertRecord
		 * 
		 */
		
	//	boolean delete=ad.deleteRecord("Curso", Long.parseLong(id));
		boolean suscrito=false;
		//if(delete==true)
		AdapterDatabase ad = new AdapterDatabase(this);
		String[] params = getParamsCurso(Integer.parseInt(id));
		ad.updateRecord("Cursos",params);
		
		return suscrito;
		
		
		
//		boolean b = true;
//		ArrayList<JSONObject> Profesor = getCursoFromDatabase(id,"Profesores");
//		ArrayList<JSONObject> Curso = getCursoFromDatabase(id,"Cursos");
//		ArrayList<JSONObject> Horarios = getCursoFromDatabase(id,"Horarios");
//		ArrayList<JSONObject> Comentarios = getCursoFromDatabase(id,"Comentarios");
//		
//		if(Profesor == null && Curso == null && Comentarios == null)
//		{	
//			
//			throw new NoExisteCursoException("No existe CUrso");
//			
//		}
//		String iidC=null,iidP = null,iidH=null,iidCom=null;
//		
//		
//		for (int i = 0; i < Profesor.size(); i++) {
//			String idP = Profesor.get(i).getString("idP");
//			String usuario = Profesor.get(i).getString("usuario");
//			String contrasena = Profesor.get(i).getString("contrasena");
//			String nombre = Profesor.get(i).getString("nombre");
//			String apellido = Profesor.get(i).getString("apellido");
//			iidP = Controlador.insertarProfesor(ctx, idP, usuario, contrasena, nombre, apellido);
//			// introducir nuevo profesor (si no est‡ introducido). Lo obtengo a partir de un for, pero es claro que arrojar‡ s—lo un elemento
//        }
//		
//		for (int i = 0; i < Curso.size(); i++) {
//			String idC = Curso.get(i).getString("idC");
//			String idP = Curso.get(i).getString("idP");
//			String titulo = arreglarCotejamiento(Curso.get(i).getString("titulo")).trim();
//			String comentable = Curso.get(i).getString("comentable");
//			String color = Curso.get(i).getString("color");
//        	Curso c = Controlador.crearNuevoCurso(ctx, Integer.parseInt(idC), Integer.parseInt(iidP==null?"0":iidP), titulo, comentable.equals("1"),color);
//			if(c!=null)
//				iidC = c.obtenerId();
//        	// introducir nuevo curso con funciones hechas por Ariel, con los par‡metros declarados en este for. Lo mismo para profe,horarios y comentarios
//        }
//		if(Horarios != null)
//		{	
//		for (int i = 0; i < Horarios.size(); i++) {
//			String idH=Horarios.get(i).getString("idH");
//			String idC=Horarios.get(i).getString("idC");
//			String dds=Horarios.get(i).getString("dds");
//			String inicio=Horarios.get(i).getString("inicio");
//			String fin=Horarios.get(i).getString("fin");
//			String ubicacion=arreglarCotejamiento(Horarios.get(i).getString("ubicacion"));
//			
//			
//			inicio = arreglaLo(inicio);
//			fin = arreglaLo(fin);
//			
//			SimpleDateFormat formato = new SimpleDateFormat("HHmmss");
//			Date a = new Date();
//			Calendar cInicio = new GregorianCalendar();
//			try
//			{
//				a = formato.parse(inicio);
//				cInicio.setTime(a);
//			}catch(ParseException e){}
//			
//			formato = new SimpleDateFormat("HHmmss");
//			a = new Date();
//			Calendar cFin = new GregorianCalendar();
//			
//			try
//			{
//				a = formato.parse(fin);
//				cFin.setTime(a);
//			}catch(ParseException e){}
//        	int x=Integer.parseInt(iidC==null?"0":iidC);
//			if(!Controlador.crearNuevoModulo(ctx, Integer.parseInt(idH), Integer.parseInt(iidC==null?"0":iidC), Integer.parseInt(dds), cInicio, cFin, ubicacion))
//				b = false;
//			// introducir nuevo horarios 
//        }}
//		/*
//		for (int i = 0; i < Comentarios.size(); i++) {
//			String idCom = Comentarios.get(i).getString("idCom");
//			String idH = Comentarios.get(i).getString("idH");
//			String fecha = Comentarios.get(i).getString("fecha");
//			String comentario = Comentarios.get(i).getString("comentario");
//			iidCom = Controlador.insertarComentario(ctx,idCom, iidH==null?"0":iidH,fecha,comentario);
//        	// introducir nuevo comentarios 
//        }*/
//		return b;
		// forma de obtener el campo "name" del usuario de idP 1 Profesor.get(1).getString("name");
		
	}

	private String arreglaLo(String horaSeba) {
		// TODO Auto-generated method stub
		String arreglado = "";
		int zHoraSeba = Integer.parseInt(horaSeba);
		
		int horas = zHoraSeba/10000;
		int minutos = (zHoraSeba/100)%100;
		int segundos = zHoraSeba%10;
		
		arreglado = agregarCeros(2,horas)+agregarCeros(2,minutos)+agregarCeros(2,segundos);
		
		return arreglado;
	}

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
	public class NoExisteCursoException extends Exception {

		public NoExisteCursoException(String msg) {

		super(msg);
		} 
		}
	
	private String arreglarCotejamiento(String variable)
	{
		variable = variable.replace("&aacute;", "á");
		variable = variable.replace("&eacute;", "é");
		variable = variable.replace("&iacute;", "í");
		variable = variable.replace("&oacute;", "ó");
		variable = variable.replace("&uacute;", "ú");
		variable = variable.replace("&Aacute;", "�?");
		variable = variable.replace("&Eacute;", "É");
		variable = variable.replace("&Iacute;", "�?");
		variable = variable.replace("&Oacute;", "Ó");
		variable = variable.replace("&Uacute;", "Ú");
		variable = variable.replace("&ntilde;", "ñ");
		variable = variable.replace("&Ntilde;", "Ñ");
		return variable;
	}
	
}