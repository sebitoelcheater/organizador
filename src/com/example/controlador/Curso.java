package com.example.controlador;

import java.util.ArrayList;
import android.content.Context;
import com.example.data.*;
import com.example.server.Server;

public class Curso extends Modelo {

	// 0 1 2 3 4 5
	// private static String[] keys = new
	// String[]{"iidC","nombre","idM","idP","comentable","color"};
	private static String[] keys;

	public static String[] getKeys() {
		return keys;
	}

	private static String nombreTabla = "Cursos";

	public static String getNombreTabla() {
		return nombreTabla;
	}

	/**
	 * No usar
	 */
	public Curso() {
		this.NombreTabla = nombreTabla;
	}

	public Curso(Context context, int idM, int iidP, String nombre,	String comentable, String color) {

		super(nombreTabla, AdapterDatabase.tablas.get(nombreTabla).getKeys(),
				new String[] { "" + idM, "" + iidP, nombre, comentable, color });
		this.NombreTabla = nombreTabla;

	}
	
	

	@Override
	public String getIid() {
		if(params.containsKey(keys[0]))
			return (String) this.params.get(keys[0]);
		return null;
	}

	public String getNombre() {
		return (String) this.params.get("titulo");
	}

	public String getIdMaster() {
		return (String) this.params.get("idM");
	}

	public String getIdP() {
		return (String) this.params.get("idP");
	}

	public String getComentable() {
		return (String) this.params.get("comentable");
	}

	public boolean esEditable() {
		return !(esDescargado());
	}

	public boolean esDescargado() {
		return !this.params.get("idM").equals("0");
	}

	public String getColor() {
		return (String) this.params.get("color");
	}

	public boolean actualizar(Context context) throws Exception {
		Server s = new Server();
		boolean b = s.actualizarCurso(getIdMaster(), context);// seba va a
																// arreglar
		return b;
	}

	public boolean borrarCurso(Context context) // DEPRECATED ???(DEBERIA IR EN
												// EL CONTROLADOR?)
	{
		AdapterDatabase db = new AdapterDatabase(context);

		if (db.deleteRecord(nombreTabla, Long.parseLong(getIid()))) {
			return true;
		}

		return false;
	}

	public boolean existeCursoComentable(Context context) {
		AdapterDatabase db = new AdapterDatabase(context);

		boolean b = db.getRecordWhere(Curso.class, "Cursos",
				new String[] { "idM" }, new String[] { getIdMaster() }, null,
				null, null, null).size() == 0;

		return !b;

	}

	public static Curso getCurso(Context context, String id) // DEPRECATED???
	{
		AdapterDatabase ad = new AdapterDatabase(context);

		Curso c = ad.getRecord(Curso.class, nombreTabla, Long.parseLong(id));
		return c;
	}
	
	public static Curso getCursoWhereIdC(Context context, String idM) // DEPRECATED???
	{
		AdapterDatabase ad = new AdapterDatabase(context);

		ArrayList<Curso> c = ad.getRecordWhere(Curso.class, nombreTabla, new String[] { "idM" }, new String[] { idM }, null, null,null, null);
		return c.get(0);
	}

	public static ArrayList<Curso> getCursosOrdenados(Context context)// por
																		// esEditable
																		// ==
																		// idM=0
	{
		AdapterDatabase db = new AdapterDatabase(context);
		ArrayList<Curso> cursos1 = db.getRecordWhere(Curso.class, nombreTabla,
				new String[] { "comentable" }, new String[] { "0" }, null, null,
				null, null);
		ArrayList<Curso> cursos2 = db.getRecordWhere(Curso.class, nombreTabla,
				new String[] { "comentable" }, new String[] { "1" }, null, null,
				null, null);
		ArrayList<Curso> ordenados = new ArrayList<Curso>();
		ordenados.addAll(cursos1);
		ordenados.addAll(cursos2);
		return ordenados;
	}

	public static ArrayList<Curso> getCursosComentables(Context context) {

		AdapterDatabase db = new AdapterDatabase(context);
		ArrayList<Curso> cursos = db.getRecordWhere(Curso.class, nombreTabla,
				new String[] { keys[4] }, new String[] { "1" }, null, null,
				null, null);

		return cursos;

	}

	public static ArrayList<Curso> getCursosEditables(Context context) {
		// TODO Auto-generated method stub //idM=0

		AdapterDatabase db = new AdapterDatabase(context);

		return db.getRecordWhere(Curso.class, nombreTabla,
				new String[] { keys[2] }, new String[] { "0" }, null, null,
				null, null);

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

	public static void setKeys(String[] keys, String nombreTabla) {

		Curso.keys = keys;
		Curso.nombreTabla = nombreTabla;
	}
	
	public boolean actualizar(Context ctx, String color, String titulo, boolean comentable) {

		
		String[] params = (String[]) this.params.values().toArray();

		boolean actualizar = false;

		if (this.getColor() != color || this.getNombre() != titulo || this.getComentable() != Integer.toString(Functions.booleanToInt(comentable)) ) {
			actualizar = true;
			params[2] = "" + titulo;
			params[3] = "" + comentable;
			params[4] = "" + color;
		}

		if (actualizar) {
			AdapterDatabase ad = new AdapterDatabase(ctx);
			ad.updateRecord(nombreTabla, params);

		}

		return actualizar;

	}

	public ArrayList<Modulo> getModulos(Context ctx, String ordenadoPor)
	{
		AdapterDatabase ad = new AdapterDatabase(ctx);
		return ad.getRecordWhere(Modulo.class,"Horarios", new String[]{"iidC"}, new String[]{this.getIid()}, null, null, null, ordenadoPor);
		
		
	}
	
	public ArrayList<Profesor> getProfesores(Context ctx, String ordenadoPor)
	{
		AdapterDatabase ad = new AdapterDatabase(ctx);
		return ad.getRecordWhere(Profesor.class,"Profesores", new String[]{"iidC"}, new String[]{this.getIid()}, null, null, null, ordenadoPor);
		
	}
	
	public String getAccion()
	{
		return this.params.get("accion").toString();
	}
	
	public static boolean existeCursoWhereIdC(Context context, String idM) {
		AdapterDatabase db = new AdapterDatabase(context);

		boolean b = db.getRecordWhere(Curso.class, nombreTabla,
				new String[] { "idM" }, new String[] { idM }, null,
				null, null, null).size() == 0;

		return b;

	}

	public String getAccionProfesores(Context ctx) {
		// TODO Auto-generated method stub
		AdapterDatabase ad = new AdapterDatabase(ctx);
		
		return ad.getExtremeRecord(Profesor.class, "Profesores", "accion", true, "iidC", this.getIid()).getAccion();
	}

	public String getAccionHorarios(Context ctx) {
		
		AdapterDatabase ad = new AdapterDatabase(ctx);
		
		return ad.getExtremeRecord(Modulo.class, "Horarios", "accion", true, "iidC", this.getIid()).getAccion();
	}
}
