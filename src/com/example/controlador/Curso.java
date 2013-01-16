package com.example.controlador;

import java.util.ArrayList;
import android.content.Context;
import com.example.data.*;
import com.example.server.Server;

public class Curso extends Modelo {

	// 0 1 2 3 4 5
	// private static String[] keys = new
	// String[]{"iidC","nombre","idC","idP","comentable","color"};
	private static String[] keys;

	static String[] getKeys() {
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

	public Curso(Context context, int idC, int iidP, String nombre,
			String comentable, String color) throws Exception {

		super(nombreTabla, AdapterDatabase.tablas.get(nombreTabla).keys,
				new String[] { "" + idC, "" + iidP, nombre, comentable, color });
		this.NombreTabla = nombreTabla;

	}

	public String getId() {
		return (String) this.params.get("iidC");
	}

	public String getNombre() {
		return (String) this.params.get("nombre");
	}

	public String getIdMaster() {
		return (String) this.params.get("idC");
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
		return this.params.get("idC").equals("0");
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

		if (db.deleteRecord(nombreTabla, Long.parseLong(getId()))) {
			return true;
		}

		return false;
	}

	public boolean existeCursoComentable(Context context) {
		AdapterDatabase db = new AdapterDatabase(context);

		boolean b = db.getRecordWhere(Curso.class, "Cursos",
				new String[] { "idC" }, new String[] { getIdMaster() }, null,
				null, null, null).size() == 0;

		return !b;

	}

	public static Curso getCurso(Context context, String id) // DEPRECATED???
	{
		AdapterDatabase ad = new AdapterDatabase(context);

		Curso c = ad.getRecord(Curso.class, nombreTabla, Long.parseLong(id));
		return c;
	}

	public static ArrayList<Curso> getCursosOrdenados(Context context)// por
																		// esEditable
																		// ==
																		// idC=0
	{
		AdapterDatabase db = new AdapterDatabase(context);
		ArrayList<Curso> cursos1 = db.getRecordWhere(Curso.class, nombreTabla,
				new String[] { keys[2] }, new String[] { "0" }, null, null,
				null, null);
		ArrayList<Curso> cursos2 = db.getRecordWhere(Curso.class, nombreTabla,
				new String[] { keys[2] }, new String[] { "1" }, null, null,
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
		// TODO Auto-generated method stub //idC=0

		AdapterDatabase db = new AdapterDatabase(context);

		return db.getRecordWhere(Curso.class, nombreTabla,
				new String[] { keys[2] }, new String[] { "0" }, null, null,
				null, null);

	}

	@Override
	public void setData(String id, Object[] params) {
		this.NombreTabla = nombreTabla;
		for (int i = 0; i < keys.length; i++) {
			this.params.put(this.keys[i], (String) params[i]);
		}

	}

	public static void setKeys(String[] keys, String nombreTabla) {

		Curso.keys = keys;
		Curso.nombreTabla = nombreTabla;
	}

}
