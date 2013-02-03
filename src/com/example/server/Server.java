//SEBASTIAN
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
import java.util.LinkedHashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.controlador.*;
import com.example.data.AdapterDatabase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class Server extends Activity {
 
	public void comentar(View view, int i, String comentario) throws Exception {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		comentario = comentario.replaceAll(" ", "%20");
		HttpPost httppost = new HttpPost("http://www.cheaper.cl/android/funciones/comentar.php?ramo=" + i	+ "&comentario=" + comentario + "");

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

	public void comentar2(View view, Curso c, Modulo m, String comentario) {
		int idH = Integer.parseInt(m.getIdMaster());
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		comentario = comentario.replaceAll(" ", "%20");
		HttpPost httppost = new HttpPost(
				"http://www.cheaper.cl/android/funciones/comentar.php?idH="
						+ idH + "&comentario=" + comentario + "");
		try {
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		// Hola seba esto es para que guardes el nuevo método que considerara el
		// Curso asociado y el Modulo asociado(teniendo los objetos tienes todos
		// los dato asociados a ellos... si necesitas ayuda me dices o revisas
		// la documentacion del controlador)

	}

	public String getInternetData(String URL) throws Exception {
		BufferedReader in = null;
		String data = null;
		try {
			HttpClient client = new DefaultHttpClient();
			URI website = new URI(URL);
			HttpGet request = new HttpGet();
			request.setURI(website);
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String l = "";
			String nl = System.getProperty("line.separator");
			while ((l = in.readLine()) != null) {
				sb.append(l + nl);
			}
			in.close();
			data = sb.toString();
			return data;
		} finally {
			if (in != null) {
				try {
					in.close();
					return data;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ArrayList<JSONObject> getRecordFromDatabase(String idC,String nombreTabla) throws Exception {
		String URL = "http://www.cheaper.cl/android/funciones/suscribir.php?id=" + idC + "";
		String JSONChain = getInternetData(URL);
		
		ArrayList<JSONObject> arreglo = stringToJSON.getArray(JSONChain, nombreTabla);
		return arreglo;
	}

	public boolean suscribirCurso(String id, Context ctx) throws Exception {

//		boolean b = true;
//		ArrayList<JSONObject> jsonProfesor = getRecordFromDatabase(id, "Profesores");
//		ArrayList<JSONObject> jsonCurso = getRecordFromDatabase(id, "Cursos");
//		ArrayList<JSONObject> Horarios = getRecordFromDatabase(id, "Horarios");
//		ArrayList<JSONObject> Comentarios = getRecordFromDatabase(id,"Comentarios");
//
//		ArrayList<ArrayList<JSONObject>> jsonTablesData = new ArrayList<ArrayList<JSONObject>>();
//		
		
		String[] nombresTablas = new String[]{Profesor.getNombreTabla(),Curso.getNombreTabla(),Modulo.getNombreTabla(),Comentario.getNombreTabla()};// REPARAR ESTO ! ! !
		LinkedHashMap<String,Modelo[]> objetosSuscritos = new LinkedHashMap<String, Modelo[]>(); 
		boolean valido=true;
		for(String nombreTabla : nombresTablas)
		{
			ArrayList<JSONObject> jsonTabla = getRecordFromDatabase(id, nombreTabla);
			if(jsonTabla==null)
				continue; 
			String[] keys = AdapterDatabase.getKeys(nombreTabla);
			String nombreClase=AdapterDatabase.getNombreClaseFromTabla(nombreTabla);
			Class<?> clase = Class.forName(nombreClase);
			Modelo[] objetos = new Modelo[jsonTabla.size()];
			int j=0;
			for(JSONObject js : jsonTabla)
			{
				Object[] values = new String[keys.length-1];
				
				for(int i=1;i<keys.length;i++)
				{
					if(keys[i].startsWith("ii"))
					{
						values[i-1]=AdapterDatabase.findIidWithIdM(ctx,keys[i],objetosSuscritos)+"";
					}
					else
					{
						values[i-1] = js.getString(keys[i]);
					}
					
				}
				Modelo a =(Modelo) clase.newInstance();
				a.setData(values);
				if(!a.estaRegistrado(ctx))
				{
					objetos[j++]=a;
					if(!a.save(ctx))
					{
						valido = false;
						break;
						
					}
				}
			}

			objetosSuscritos.put(nombreTabla, objetos);
			if(!valido)
				break;
			
			
		}
		
		if(!valido)
		{
			borrarElementos(objetosSuscritos);
			return true;
		}
		
		return false;
		

	}

	private void borrarElementos(LinkedHashMap<String, Modelo[]> objetosSuscritos) {
		String[] nombreTablas = objetosSuscritos.keySet().toArray(new String[0]);
		AdapterDatabase ad = new AdapterDatabase(this);
		for(String nombreTabla : nombreTablas)
		{
			Modelo[] objetosTabla = objetosSuscritos.get(nombreTabla);
			
			for(Modelo m :objetosTabla)
			{
				String id = m.getIid();
				if(id!=null)
				{
					ad.deleteRecord(nombreTabla,Long.parseLong(id));
				}
				
				
			}
		}
	}

	private String[] getParamsCurso(JSONObject Curso) {
		try {
			String idC = Curso.getString("idC");
			String idP = Curso.getString("idP");
			String titulo = arreglarCotejamiento(Curso.getString("titulo"))
					.trim();
			String comentable = Curso.getString("comentable");
			String color = Curso.getString("color");
			return new String[] { idC, idP, titulo, comentable, color };
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("getParamsCurso recibio algo mal en server.java");
			return null;

		}

	}

	public boolean actualizarCurso(String id, Context ctx)
	{// retorna si hay un
															// tope en la nueva
		System.out.println("ACTUALIZARCURSO NO IMPLEMENTADO (Server)");
		
	return false;
		
//		boolean suscrito = false;
//		// if(delete==true)
//		AdapterDatabase ad = new AdapterDatabase(this);
//		String[] params = getParamsCurso(getRecordFromDatabase(id,"Curso")); 
//		ad.updateRecord("Cursos", params);
//
//		return suscrito;
/**
		// boolean b = true;
		// ArrayList<JSONObject> Profesor =
		// getRecordFromDatabase(id,"Profesores");
		// ArrayList<JSONObject> Curso = getRecordFromDatabase(id,"Cursos");
		// ArrayList<JSONObject> Horarios = getRecordFromDatabase(id,"Horarios");
		// ArrayList<JSONObject> Comentarios =
		// getRecordFromDatabase(id,"Comentarios");
		//
		// if(Profesor == null && Curso == null && Comentarios == null)
		// {
		//
		// throw new NoExisteCursoException("No existe CUrso");
		//
		// }
		// String iidC=null,iidP = null,iidH=null,iidCom=null;
		//
		//
		// for (int i = 0; i < Profesor.size(); i++) {
		// String idP = Profesor.get(i).getString("idP");
		// String usuario = Profesor.get(i).getString("usuario");
		// String contrasena = Profesor.get(i).getString("contrasena");
		// String nombre = Profesor.get(i).getString("nombre");
		// String apellido = Profesor.get(i).getString("apellido");
		// iidP = Controlador.insertarProfesor(ctx, idP, usuario, contrasena,
		// nombre, apellido);
		 * 
		 */
		// // introducir nuevo profesor (si no est‡ introducido). Lo obtengo a
		// partir de un for, pero es claro que arrojar‡ s—lo un elemento
		// }
		//
		// for (int i = 0; i < Curso.size(); i++) {
		// String idC = Curso.get(i).getString("idC");
		// String idP = Curso.get(i).getString("idP");
		// String titulo =
		// arreglarCotejamiento(Curso.get(i).getString("titulo")).trim();
		// String comentable = Curso.get(i).getString("comentable");
		// String color = Curso.get(i).getString("color");
		// Curso c = Controlador.crearNuevoCurso(ctx, Integer.parseInt(idC),
		// Integer.parseInt(iidP==null?"0":iidP), titulo,
		// comentable.equals("1"),color);
		// if(c!=null)
		// iidC = c.getId();
		// // introducir nuevo curso con funciones hechas por Ariel, con los
		// par‡metros declarados en este for. Lo mismo para profe,horarios y
		// comentarios
		// }
		// if(Horarios != null)
		// {
		// for (int i = 0; i < Horarios.size(); i++) {
		// String idH=Horarios.get(i).getString("idH");
		// String idC=Horarios.get(i).getString("idC");
		// String dds=Horarios.get(i).getString("dds");
		// String inicio=Horarios.get(i).getString("inicio");
		// String fin=Horarios.get(i).getString("fin");
		// String
		// ubicacion=arreglarCotejamiento(Horarios.get(i).getString("ubicacion"));
		//
		//
		// inicio = arreglaLo(inicio);
		// fin = arreglaLo(fin);
		//
		// SimpleDateFormat formato = new SimpleDateFormat("HHmmss");
		// Date a = new Date();
		// Calendar cInicio = new GregorianCalendar();
		// try
		// {
		// a = formato.parse(inicio);
		// cInicio.setTime(a);
		// }catch(ParseException e){}
		//
		// formato = new SimpleDateFormat("HHmmss");
		// a = new Date();
		// Calendar cFin = new GregorianCalendar();
		//
		// try
		// {
		// a = formato.parse(fin);
		// cFin.setTime(a);
		// }catch(ParseException e){}
		// int x=Integer.parseInt(iidC==null?"0":iidC);
		// if(!Controlador.crearNuevoModulo(ctx, Integer.parseInt(idH),
		// Integer.parseInt(iidC==null?"0":iidC), Integer.parseInt(dds),
		// cInicio, cFin, ubicacion))
		// b = false;
		// // introducir nuevo horarios
		// }}
		// /*
		// for (int i = 0; i < Comentarios.size(); i++) {
		// String idCom = Comentarios.get(i).getString("idCom");
		// String idH = Comentarios.get(i).getString("idH");
		// String fecha = Comentarios.get(i).getString("fecha");
		// String comentario = Comentarios.get(i).getString("comentario");
		// iidCom = Controlador.insertarComentario(ctx,idCom,
		// iidH==null?"0":iidH,fecha,comentario);
		// // introducir nuevo comentarios
		// }*/
		// return b;
		// forma de get el campo "name" del usuario de idP 1
		// Profesor.get(1).getString("name");

	}

	private String arreglaLo(String horaSeba) {
		// TODO Auto-generated method stub
		String arreglado = "";
		int zHoraSeba = Integer.parseInt(horaSeba);

		int horas = zHoraSeba / 10000;
		int minutos = (zHoraSeba / 100) % 100;
		int segundos = zHoraSeba % 10;

		arreglado = agregarCeros(2, horas) + agregarCeros(2, minutos)
				+ agregarCeros(2, segundos);

		return arreglado;
	}

	private static String agregarCeros(int n, int i) {
		// TODO Auto-generated method stub
		String numero = i + "";
		int longitud = n - numero.length();
		if (numero.length() < n) {
			for (int j = 0; j < longitud; ++j) {
				numero = "0" + numero;
			}
		}

		return numero;
	}

	public class NoExisteCursoException extends Exception {

		public NoExisteCursoException(String msg) {

			super(msg);
		}
	}

	private String arreglarCotejamiento(String variable) {
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

	public boolean pullNewData(Context ctx)
	{
		
		
		
		//String URL = "http://www.cheaper.cl/android/funciones/suscribir.php?json="+reunirIDsActualizar(ctx);
 		String JSONRequest = "{'Horarios':[[{'id':1},{'id':2}],[],[]],'Cursos':[[][][]]}"; //getInternetData(URL);
//		
 		if(JSONRequest==null)
			return true;
 		AdapterDatabase ad = new AdapterDatabase(ctx);
 		String[] nombreTablas = (String[])AdapterDatabase.tablas.keySet().toArray();
 		
 	
 		for( String nombreTabla : nombreTablas)
 		{
 			ArrayList<JSONObject> arreglo = stringToJSON.getArray(JSONRequest, nombreTabla);
 			if(arreglo==null)
 				continue;
 			
 			String[] keys=AdapterDatabase.getKeys(nombreTabla);
 			int size=arreglo.size();//en 0 deben venir los ids que quiero borrar, en 1 los actualizar, y en 2 agregar
 			
 			
 			JSONObject ja = arreglo.get(0);
 			
 			
 			return true;
 				
 			

 		}
	
 		return true;
		
		
		
	}
	
	private String reunirIDsActualizar(Context ctx)
	{
		
//		ArrayList<Curso> cursos = Curso.getAllRecords();
//		
//		JSONObject jsonChain = new JSONObject();
//		for(Curso c : cursos)
//		{
//			String idM = c.getIdMaster();
//			String accionCurso = c.getAccion();
//			ArrayList<Modulo> ms = c.getModulos("accion");
//			String accionModulo=ms.get(ms.size()-1).getAccion();
//			
//			ArrayList<Profesor> ps = c.getProfesores("accion");
//			String accionProfesor = ps.get(ps.size()-1).getAccion();
//						
//			JSONObject jo = new JSONObject();
//			jo.put("accionModulo", accionModulo);
//			jo.put("accionProfesor", accionProfesor);
//			jo.put("accionCurso", accionCurso);
//			
//			jsonChain.put(idM,jo);
//		}
		
//		return jsonChain.toString();
	
		ArrayList<Curso> cursos = Curso.getCursosOrdenados(ctx);
		
		JSONObject jsonChain = new JSONObject();
		for(Curso c : cursos)
		{
			String idM = c.getIdMaster();
			
			JSONObject jo = new JSONObject();
			String accionProfesor = c.getAccionProfesores(ctx);
			String accionModulo = c.getAccionHorarios(ctx);
			String accionCursos = c.getAccion();
			
			try {
				jo.put("accionModulo", accionModulo);
				jo.put("accionProfesor", accionProfesor);
				jo.put("accionCurso", accionCursos);
				jsonChain.put(idM,jo);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		return jsonChain.toString();
		
	}
}