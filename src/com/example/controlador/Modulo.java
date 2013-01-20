package com.example.controlador;

import java.util.*;
import android.content.Context;
import android.widget.Toast;

import com.example.data.*;

public class Modulo extends Modelo {
	// 0 1 2 3 4 5 6
	private static String[] keys;

	// iidH, idH, iidC, dds, inicio, fin, ubicaci—n
	static String[] getKeys() {
		return keys;
	}

	public static void setKeys(String[] keys) {
		Modulo.keys = keys;
	}

	static String nombreTabla = "Horarios";

	// private String id=null;
	// private String idH=null;
	// private String idCurso=null;
	// private String ubicacion=null;
	// private String diaDeLaSemana=null;
	// private Calendar inicio=null;
	// private Calendar fin=null;
	//
	public Modulo() {

		this.NombreTabla = nombreTabla;
	}

	public Modulo(Context context, int idH, int iidC, int inicio, int fin, String ubicacion) throws Exception {
		super(nombreTabla, getKeys(), new Object[] { idH, iidC, inicio, fin, ubicacion });

		this.NombreTabla = nombreTabla;

	}

	@Override
	public boolean save(Context ctx) {
		AdapterDatabase db = new AdapterDatabase(ctx);

		boolean b = getModulosEntreInicioYFin(ctx, this.getInicio(),this.getFin()).size() == 0;
		if (b)
			db.insertRecord(nombreTabla, this.params.values().toArray());

		return b;
	}

	/**
	 * RE-HACER METODO CON getRecordWhere
	 * 
	 * @param context
	 * @param idCurso
	 * @return
	 */
	static public ArrayList<Modulo> getModulosPorIdCurso(Context context,
			Curso c) {
		AdapterDatabase db = new AdapterDatabase(context);
		ArrayList<Modulo> modulos = db.getRecordWhere(Modulo.class,
				nombreTabla, new String[] { "idC" },
				new String[] { c.getId() }, null, null, null, null);

		return modulos;

		// AdapterDatabase db = new AdapterDatabase(context);
		//
		// ArrayList<Modulo> modulos = db.getRecordPorCursoHORARIOS(idCurso);
		//
		// return modulos;
	}

	/** Seba lo hace **/
	static public Modulo ultimoModulo(Context context, Curso c) {

		AdapterDatabase db = new AdapterDatabase(context);
		ArrayList<Modulo> modulos = db.getRecordWhere(Modulo.class,
				nombreTabla, new String[] { "idC" },
				new String[] { c.getId() }, null, null, null, "inicio");

		int tamanoModulos = modulos.size();
		if (tamanoModulos != 0) {
			return modulos.get(tamanoModulos - 1);
		} else {
			return null;
		}

	}

	static public boolean puedoCambiarModulo(Context context, int inicio,
			int fin, Modulo moduloAEditar) {

		ArrayList<Modulo> c = getModulosEntreInicioYFin(context, inicio, fin);

		boolean b = c.size() == 0;
		if ((!b) && c.size() == 1)
			if ((moduloAEditar.getId().equals(getKeys()[0])))
				b = true;

		return b;
	}

	static public ArrayList<Modulo> getModulos(Context context) {
		AdapterDatabase db = new AdapterDatabase(context);
		ArrayList<Modulo> modulos = db.getAllRecords(Modulo.class, nombreTabla);

		return modulos;
	}

	static public ArrayList<Modulo> getModulosSegunDiaOrdenadosSegunInicio(
			Context context, int dia) 
	{
		AdapterDatabase db = new AdapterDatabase(context);

		ArrayList<Modulo> modulosOrdenados = db.getRecordWhere(Modulo.class,
				nombreTabla, null, null, new String[] { "inicio", "fin" },
				new String[] { dia * Functions.minutosDeUnDia + "",
						dia * Functions.minutosDeUnDia + "" }, new String[] {
						(dia + 1) * Functions.minutosDeUnDia + "",
						(dia + 1) * Functions.minutosDeUnDia + "" }, "inicio");

		return modulosOrdenados;

	}
	
	public static Modulo getModulo(Context context, String id) // DEPRECATED???
	{
		AdapterDatabase ad = new AdapterDatabase(context);

		Modulo m = ad.getRecord(Modulo.class, nombreTabla, Long.parseLong(id));
		return m;
	}

	/***
	 * Ordena segun hora de inicio
	 * 
	 * @param x
	 */

	static public ArrayList<Modulo> getModulosDelDia(Context context, int hoydia) // INEFICIENTE!!!!
	{
		return getModulosSegunDiaOrdenadosSegunInicio(context, hoydia);
	}

	// ACA HAY UN DDS !
	/**
	 * RE-HACER METODO CON getRecordWhere
	 * 
	 * @param context
	 * @param ahora
	 * @return
	 */
	public static ArrayList<Modulo> getLosFeedBackeables(Context context) {
		// TODO Auto-generated method stub
		int horas = 3;// Lo que espera el feedBack
		int hoyMinutos = Functions.getAhoraEnMinutos();

		AdapterDatabase db = new AdapterDatabase(context);
		ArrayList<Curso> cursos = db.getRecordWhere(Curso.class, "Cursos",
				new String[] { "comentable" }, new String[] { "1" }, null,
				null, null, null);

		ArrayList<Modulo> modulos = new ArrayList<Modulo>();

		for (Curso curso : cursos) {
			String idC = curso.getId();

			ArrayList<Modulo> modulo = db.getRecordWhere(Modulo.class,
					nombreTabla, new String[] { "idC" }, new String[] { idC },
					new String[] { "fin" },
					new String[] { Integer.toString(hoyMinutos) },
					new String[] { Integer.toString(hoyMinutos + horas * 60) },
					null);

			modulos.addAll(modulo);
		}
		return modulos;

		// ArrayList<Modulo> posiblesModulos = getModulosDelDia(context, ahora);
		// ArrayList<Modulo> posibles2Modulos = new ArrayList<Modulo>();
		//
		// for (Modulo m : posiblesModulos) {
		// AdapterDatabase ad = new AdapterDatabase(context);
		//
		// Curso c = ad.getRecord(Curso.class, Curso.getNombreTabla(),
		// Long.parseLong(m.getIdCurso()));
		// if (c.getComentable().equals("1")) {
		// posibles2Modulos.add(m);
		// }
		//
		// }
		//
		// posiblesModulos = new ArrayList<Modulo>();
		// for (Modulo m : posibles2Modulos) {
		// Calendar fin = m.getFin();
		// Calendar plazoMaximo = (Calendar) fin.clone();
		// plazoMaximo.add(Calendar.HOUR, horas);
		// Calendar aahora = (Calendar) fin.clone();
		// aahora.set(Calendar.HOUR_OF_DAY, ahora.get(Calendar.HOUR_OF_DAY));
		// aahora.set(Calendar.MINUTE, ahora.get(Calendar.MINUTE));
		//
		// if (aahora.before(plazoMaximo) && aahora.after(fin))
		// posiblesModulos.add(m);
		// }
		// return posiblesModulos;
	}

	/**
	 * RE-HACER METODO CON getRecordWhere
	 * 
	 * @param context
	 * @param ahora
	 * @param minutos
	 * @return
	 */
	static public ArrayList<Modulo> getLosModulosAnterioresFin(Context context,
			Calendar momento) // TERMINAR
	{

		AdapterDatabase db = new AdapterDatabase(context);

		ArrayList<Modulo> modulo = db
				.getRecordWhere(Modulo.class, nombreTabla, null, null,
						new String[] { "fin" }, new String[] { "0" },
						new String[] { Integer.toString(Functions
								.getEnMinutos(momento)) }, null);

		return modulo;

	}

	/**
	 * RE-HACER METODO CON getRecordWhere
	 * 
	 * @param context
	 * @param ahora
	 * @param minutos
	 * @return
	 */
	static public ArrayList<Modulo> getLosModulosProximosInicio(
			Context context, Calendar momento) // TERMINAR
	{

		AdapterDatabase db = new AdapterDatabase(context);

		ArrayList<Modulo> modulo = db
				.getRecordWhere(Modulo.class, nombreTabla, null, null,
						new String[] { "inicio" }, new String[] { "0" },
						new String[] { Integer.toString(Functions
								.getEnMinutos(momento)) }, null);

		return modulo;

	}

	static public ArrayList<Modulo> getLosSiguientesModulosDelDia(
			Context context, int minAhora, int largo) {

		AdapterDatabase db = new AdapterDatabase(context);
		ArrayList<Modulo> c = db.getRecordWhere(Modulo.class, nombreTabla,
				null, null, new String[] { "inicio" }, new String[] { minAhora
						+ "" }, null, "inicio");

		return c;
	}
	
	static public ArrayList<Modulo> getLosSiguientesModulos(
			Context context, int ahoraEnMinutos) {

		AdapterDatabase db = new AdapterDatabase(context);

		ArrayList<Modulo> modulos = db.getRecordWhere(Modulo.class,
				nombreTabla, null, null, new String[] { "inicio" },
				new String[] { ahoraEnMinutos + "" }, null, "inicio");

		return modulos;
	}

	/**
	 * Cambiar el nombre de este metodo a "existen modulos entre inicio y fin"
	 * 
	 * @param ctx
	 * @param diaDeLaSemana
	 * @param inicio
	 * @param fin
	 * @return
	 */
	public static ArrayList<Modulo> getModulosEntreInicioYFin(Context ctx,
			int inicio, int fin) {
		AdapterDatabase ad = new AdapterDatabase(ctx);

		ArrayList<Modulo> modulos = ad.getRecordWhere(Modulo.class,
				nombreTabla, null, null, new String[] { "inicio", "fin" },
				new String[] { "" + inicio, null }, new String[] { null,
						"" + fin }, null);

		return modulos;
	}

	
	public boolean actualizar(Context ctx, int minInicio, int minFin,
			String ubicacion) {

		String[] params = (String[]) this.params.values().toArray();

		boolean actualizar = false;

		if (this.getInicio() != minInicio || this.getFin() != minFin) {
			if (minInicio < minFin) {
				if (Modulo.puedoCambiarModulo(ctx, minInicio, minFin, this)) {
					actualizar = true;
					params[3] = "" + minInicio;
					params[4] = "" + minFin;

				}
			}

		}

		if (!this.getUbicacion().equals(ubicacion)) {
			actualizar = true;
			params[5] = ubicacion;
		}

		if (actualizar) {
			AdapterDatabase ad = new AdapterDatabase(ctx);
			ad.updateRecord(nombreTabla, params);

		}

		return actualizar;

	}

	public String getId() {
		return (String) this.params.get(getKeys()[0]);
	}

	public String getDiaDeLaSemana() {
		// return diaDeLaSemana;
		return (String) this.params.get(getKeys()[3]);
	}

	public String getIdMaster() {
		// return idH;
		return (String) this.params.get(getKeys()[1]);
	}

	public String getIdCurso() {
		return (String) this.params.get(getKeys()[2]);
	}

	public String getUbicacion() {
		return (String) this.params.get(getKeys()[6]);
	}

	public String getNombreDiaDeLaSemana() {
		String diaDeLaSemana = getDiaDeLaSemana();
		if (diaDeLaSemana.equals("2")) {
			return "Lunes";
		} else if (diaDeLaSemana.equals("3")) {
			return "Martes";

		} else if (diaDeLaSemana.equals("4")) {
			return "Miércoles";

		} else if (diaDeLaSemana.equals("5")) {
			return "Jueves";
		} else if (diaDeLaSemana.equals("6")) {
			return "Viernes";

		} else if (diaDeLaSemana.equals("7")) {
			return "Sábado";

		} else if (diaDeLaSemana.equals("1")) {
			return "Domingo";

		} else {
			return "DIA " + diaDeLaSemana + " ";
		}
	}

	public int getInicio() {
		return Integer.parseInt(this.params.get("inicio").toString());
	}

	public int getFin() {
		return Integer.parseInt(this.params.get("fin").toString());
	}

	// METODOS DE OBTENCION

	// METODOS DE SETEO DEL OBJETO(NO DB)

	public boolean borrarModulo(Context context) // DEPRECATED ???(DEBERIA IR EN
													// EL CONTROLADOR?)
	{
		AdapterDatabase db = new AdapterDatabase(context);

		if (db.deleteRecord(nombreTabla, Long.parseLong(getId()))) {

			return true;
		}

		return false;
	}

	// METODOS DE SETEO DE LA BASE DE DATOS Y OBJETO

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

	public static void setKeys(String[] keys, String nombreTabla) {
		Modulo.keys = keys;
		Modulo.nombreTabla = nombreTabla;
	}

	@Override
	public void setData(String id, Object[] params) {

		for (int i = 0; i < keys.length; i++) {
			this.params.put(this.keys[i], params[i]);
		}

	}
}