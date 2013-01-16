package com.example.controlador;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

	}

	public Modulo(Context context, int idH, int iidC, int inicio,
			int fin, String ubicacion) throws Exception {
		super(nombreTabla, getKeys(), new Object[] { idH, iidC, inicio,
				fin, ubicacion });
		
	}

	/**
	 * RE-HACER METODO CON getRecordWhere
	 * 
	 * @param context
	 * @param idCurso
	 * @return
	 */
	static public ArrayList<Modulo> getModulosPorIdCurso(Context context,Curso c) {
		AdapterDatabase db = new AdapterDatabase(context);
		ArrayList<Modulo> modulos = db.getRecordWhere(Modulo.class, nombreTabla,
				new String[] { "idC" },
				new String[] { c.getId() }, null,
				null, null, null);

		return modulos;
		
//		AdapterDatabase db = new AdapterDatabase(context);
//
//		ArrayList<Modulo> modulos = db.getRecordPorCursoHORARIOS(idCurso);
//
//		return modulos;
	}

	static public Modulo ultimoModulo(Context context, Curso c) {

		AdapterDatabase db = new AdapterDatabase(context);
		ArrayList<Modulo> modulos = db.getRecordWhere(Modulo.class, nombreTabla,
				new String[] { "idC" },
				new String[] { c.getId() }, null,
				null, null, "inicio");
		
		int tamanoModulos=modulos.size();
		if (tamanoModulos!=0){
			return modulos.get(tamanoModulos-1);
		}
		else{
			return null;
		}
	}

	static public boolean puedoCambiarModulo(Context context, int dds,
			Calendar inicio, Calendar fin, Modulo moduloAEditar) {
		AdapterDatabase db = new AdapterDatabase(context);
		String stringInicio = agregarCeros(2, inicio.get(Calendar.HOUR_OF_DAY))
				+ ":" + agregarCeros(2, inicio.get(Calendar.MINUTE));
		String stringFin = agregarCeros(2, fin.get(Calendar.HOUR_OF_DAY)) + ":"
				+ agregarCeros(2, fin.get(Calendar.MINUTE));

		;
		ArrayList<Modulo> c = comprobarTopeHorario(context, dds + "",
				stringInicio, stringFin);

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
			Context context, int dia) // INEFICIENTE!!!!, el dia es de 1 a 7
										// siendo 1 el domingo
	{
		AdapterDatabase db = new AdapterDatabase(context);
		ArrayList<Modulo> modulosNoOrdenados = db.getAllRecords(Modulo.class,
				nombreTabla);
		ArrayList<Modulo> modulos = new ArrayList<Modulo>();
		for (Modulo modulo : modulosNoOrdenados) {

			if (modulo.getDiaDeLaSemana().equals(dia + ""))
				modulos.add(modulo);
		}

		quicksortModulo(modulos);
		return modulos;
	}

	/***
	 * Ordena segun hora de inicio
	 * 
	 * @param x
	 */
	public static void quicksortModulo(ArrayList<Modulo> x) {
		quicksortRecursivo(x, 0, x.size() - 1);
	}

	private static void quicksortRecursivo(ArrayList<Modulo> x, int lo, int ho) {
		int l = lo, h = ho;
		Modulo mid, t;

		if (ho > lo) {
			mid = x.get((lo + ho) / 2);
			while (l < h) {
				while ((l < ho)
						&& (x.get(l).getInicio()
								.compareTo(mid.getInicio()) < 0))
					++l;
				while ((h > lo)
						&& (x.get(h).getInicio()
								.compareTo(mid.getInicio()) > 0))
					--h;
				if (l <= h) {
					t = x.get(l);
					x.set(l, x.get(h));
					x.set(h, t);
					++l;
					--h;
				}
			}

			if (lo < h)
				quicksortRecursivo(x, lo, h);
			if (l < ho)
				quicksortRecursivo(x, l, ho);
		}
	}

	static public ArrayList<Modulo> getModulosDelDia(Context context,
			Calendar hoydia) // INEFICIENTE!!!!
	{
		ArrayList<Modulo> modulos;
		AdapterDatabase db = new AdapterDatabase(context);

		// ArrayList<Modulo> modulosNoOrdenados =
		// db.getAllRecords(Modulo.class,nombreTabla);
		//
		// for(Modulo modulo: modulosNoOrdenados)
		// {
		// if
		// (modulo.getDiaDeLaSemana().equals(hoydia.get(Calendar.DAY_OF_WEEK)+""))
		// modulos.add(modulo);
		//
		// }
		//
		modulos = db.getRecordWhere(Modulo.class, nombreTabla,
				new String[] { "dds" },
				new String[] { "" + hoydia.get(Calendar.DAY_OF_WEEK) }, null,
				null, null, null);

		return modulos;
	}
	 //ACA HAY UN DDS !
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
				new String[] { "comentable" },
				new String[] { "1" }, null,
				null, null, null);
		
		ArrayList<Modulo> modulos = new ArrayList<Modulo>();
		
		for (Curso curso: cursos){
			String idC = curso.getId();
			
			ArrayList<Modulo> modulo = db.getRecordWhere(Modulo.class, nombreTabla,
					new String[] { "idC" },
					new String[] { idC }, 
					new String[] { "fin" },
					new String[] { Integer.toString(hoyMinutos) },
					new String[] { Integer.toString(hoyMinutos+horas*60) },
					null);
			
			modulos.addAll(modulo);
		}
		return modulos;
		
//		ArrayList<Modulo> posiblesModulos = getModulosDelDia(context, ahora);
//		ArrayList<Modulo> posibles2Modulos = new ArrayList<Modulo>();
//
//		for (Modulo m : posiblesModulos) {
//			AdapterDatabase ad = new AdapterDatabase(context);
//
//			Curso c = ad.getRecord(Curso.class, Curso.getNombreTabla(),
//					Long.parseLong(m.getIdCurso()));
//			if (c.getComentable().equals("1")) {
//				posibles2Modulos.add(m);
//			}
//
//		}
//
//		posiblesModulos = new ArrayList<Modulo>();
//		for (Modulo m : posibles2Modulos) {
//			Calendar fin = m.getFin();
//			Calendar plazoMaximo = (Calendar) fin.clone();
//			plazoMaximo.add(Calendar.HOUR, horas);
//			Calendar aahora = (Calendar) fin.clone();
//			aahora.set(Calendar.HOUR_OF_DAY, ahora.get(Calendar.HOUR_OF_DAY));
//			aahora.set(Calendar.MINUTE, ahora.get(Calendar.MINUTE));
//
//			if (aahora.before(plazoMaximo) && aahora.after(fin))
//				posiblesModulos.add(m);
//		}
//		return posiblesModulos;
	}

	/**
	 * RE-HACER METODO CON getRecordWhere
	 * 
	 * @param context
	 * @param ahora
	 * @param minutos
	 * @return
	 */
	static public ArrayList<Modulo> getLosModulosAnterioresFin(
			Context context, Calendar momento) // TERMINAR
	{
				
		AdapterDatabase db = new AdapterDatabase(context);
		
		ArrayList<Modulo> modulo = db.getRecordWhere(Modulo.class, nombreTabla,
				null,
				null, 
				new String[] { "fin" },
				new String[] { "0" },
				new String[] { Integer.toString(Functions.getEnMinutos(momento)) },
				null);
		
		return modulo;
		
		
		
//		ArrayList<Modulo> modulos = new ArrayList<Modulo>();
//		Calendar antes = ((Calendar) ahora.clone());
//		antes.add(Calendar.MINUTE, -minutos);
//
//		String stringAhora = agregarCeros(2, ahora.get(Calendar.HOUR_OF_DAY))
//				+ ":" + agregarCeros(2, ahora.get(Calendar.MINUTE));
//		String stringAntes = agregarCeros(2, antes.get(Calendar.HOUR_OF_DAY))
//				+ ":" + agregarCeros(2, antes.get(Calendar.MINUTE));
//
//		AdapterDatabase db = new AdapterDatabase(context);
//		;
//		if (antes.get(Calendar.DAY_OF_WEEK) == ahora.get(Calendar.DAY_OF_WEEK)) {
//			ArrayList<Modulo> c = db.getRecordWhere(Modulo.class, "Horarios",
//					new String[] { "dds" },
//					new String[] { ahora.get(Calendar.DAY_OF_WEEK) + "" },
//					new String[] { "fin" }, new String[] { stringAntes },
//					new String[] { stringAhora }, "fin");
//
//		} else {
//			ArrayList<Modulo> c = db.getRecordWhere(Modulo.class, nombreTabla,
//					new String[] { "dds" },
//					new String[] { ahora.get(Calendar.DAY_OF_WEEK) + "" },
//					new String[] { "fin" }, new String[] { "00:00" },
//					new String[] { stringAhora }, "fin");
//
//			c = db.getRecordWhere(Modulo.class, nombreTabla,
//					new String[] { "dds" },
//					new String[] { ahora.get(Calendar.DAY_OF_WEEK) + "" },
//					new String[] { "fin" }, new String[] { stringAntes },
//					new String[] { "23:59" }, "fin");
//
//		}
//
//		ArrayList<Modulo> modulosComentables = new ArrayList<Modulo>();// AQUI
//																		// ESTA
//		for (Modulo m : modulos) {
//			AdapterDatabase ad = new AdapterDatabase(context);
//
//			Curso c = ad.getRecord(Curso.class, Curso.getNombreTabla(),
//					Long.parseLong(m.getIdCurso()));
//			if (c.getComentable().equals("1")) {
//				modulosComentables.add(m);
//			}
//		}
//
//		return modulosComentables;
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
		
		ArrayList<Modulo> modulo = db.getRecordWhere(Modulo.class, nombreTabla,
				null,
				null, 
				new String[] { "inicio" },
				new String[] { "0" },
				new String[] { Integer.toString(Functions.getEnMinutos(momento)) },
				null);
		
		return modulo;
		
		
//		ArrayList<Modulo> modulos = new ArrayList<Modulo>();
//		Calendar despues = ((Calendar) ahora.clone());
//		despues.add(Calendar.MINUTE, minutos);
//
//		String stringAhora = agregarCeros(2, ahora.get(Calendar.HOUR_OF_DAY))
//				+ ":" + agregarCeros(2, ahora.get(Calendar.MINUTE));
//		String stringDespues = agregarCeros(2,
//				despues.get(Calendar.HOUR_OF_DAY))
//				+ ":" + agregarCeros(2, despues.get(Calendar.MINUTE));
//
//		AdapterDatabase db = new AdapterDatabase(context);
//
//		if (despues.get(Calendar.DAY_OF_WEEK) == ahora
//				.get(Calendar.DAY_OF_WEEK)) {
//			ArrayList<Modulo> c = db.getRecordWhere(Modulo.class, nombreTabla,
//					new String[] { "dds" },
//					new String[] { ahora.get(Calendar.DAY_OF_WEEK) + "" },
//					new String[] { "inicio" }, new String[] { stringAhora },
//					new String[] { stringDespues }, "inicio");
//
//		} else {
//			ArrayList<Modulo> c = db.getRecordWhere(Modulo.class, nombreTabla,
//					new String[] { "dds" },
//					new String[] { ahora.get(Calendar.DAY_OF_WEEK) + "" },
//					new String[] { "inicio" }, new String[] { stringAhora },
//					new String[] { "23:59" }, "inicio");
//
//			c = db.getRecordWhere(Modulo.class, nombreTabla,
//					new String[] { "dds" },
//					new String[] { ahora.get(Calendar.DAY_OF_WEEK) + "" },
//					new String[] { "inicio" }, new String[] { "00:00" },
//					new String[] { stringDespues }, "inicio");
//
//		}
//
//		return modulos;
	}

	static public ArrayList<Modulo> getLosSiguientesModulosDelDia(
			Context context, Calendar ahora, int largo) {

		String hora = agregarCeros(2, ahora.get(Calendar.HOUR_OF_DAY)) + ":"
				+ agregarCeros(2, ahora.get(Calendar.MINUTE));
		AdapterDatabase db = new AdapterDatabase(context);

		ArrayList<Modulo> c = db.getRecordWhere(Modulo.class, nombreTabla,
				new String[] { "dds" },
				new String[] { "" + ahora.get(Calendar.DAY_OF_WEEK) },
				new String[] { "inicio" }, new String[] { hora }, null, largo
						+ "");

		return c;
	}

	static public ArrayList<Modulo> getLosSiguientesModulos(
			Context context, Calendar ahora, int largo) {

		String hora = agregarCeros(2, ahora.get(Calendar.HOUR_OF_DAY)) + ":"
				+ agregarCeros(2, ahora.get(Calendar.MINUTE));
		AdapterDatabase db = new AdapterDatabase(context);

		ArrayList<Modulo> modulosMismoDia = db.getRecordWhere(Modulo.class,
				nombreTabla, new String[] { "dds" },
				new String[] { ahora.get(Calendar.DAY_OF_WEEK) + "" },
				new String[] { "inicio" }, new String[] { hora }, null, null);
		ArrayList<Modulo> modulosDiasSiguientes = db.getRecordWhere(
				Modulo.class, nombreTabla, null, null, new String[] { "dds" },
				new String[] { ahora.get(Calendar.DAY_OF_WEEK) + "" }, null,
				null);

		ArrayList<Modulo> modulos = new ArrayList<Modulo>();
		if (modulosMismoDia != null)
			modulos.addAll(modulosMismoDia);

		if (modulosDiasSiguientes != null)
			modulos.addAll(modulosDiasSiguientes);

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
	public static ArrayList<Modulo> comprobarTopeHorario(Context ctx,
			String diaDeLaSemana, String inicio, String fin) {
		AdapterDatabase ad = new AdapterDatabase(ctx);

		// String fInicio = "'"+inicio+"'";
		// String fFin = "'"+fin+"'";
		// String KEY_INICIO="inicio";
		// String KEY_FIN = "fin";
		//
		// Cursor mCursor =
		// db.query(true, nombreTabla, getNombresCampos("Horarios"),
		// "dds" + "=" + diaDeLaSemana+
		// " AND (("+KEY_INICIO+"<"+fInicio+" AND "+
		// KEY_FIN+">"+fFin+") OR ("+KEY_INICIO+">="+fInicio+" AND "+
		// KEY_INICIO+"<"+fFin+") OR ("+KEY_FIN+">"+fInicio+" AND "+
		// KEY_FIN+"<="+fFin+")"+")", null, null, null, null, null);
		// if (mCursor != null) {
		// mCursor.moveToFirst();
		// }
		// db.close() ; DBHelper.close(); mCursor.close();
		ArrayList<Modulo> modulos = ad
				.getRecordWhere(Modulo.class, nombreTabla,
						new String[] { "dds" }, new String[] { diaDeLaSemana },
						new String[] { "inicio", "fin" }, new String[] {
								inicio, null }, new String[] { null, fin },
						null);

		return modulos;
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

	public String getDiaDeLaSemana() {
		return (String) this.params.get(getKeys()[3]);
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

	public Calendar getInicio() {
		return (Calendar) ((Calendar) this.params.get(getKeys()[4])).clone();
	}

	public Calendar getFin() {
		return (Calendar) ((Calendar) this.params.get(getKeys()[5])).clone();
	}

	public String getStringInicio() {
		Calendar inicio = getInicio();
		return agregarCeros(2, inicio.get(Calendar.HOUR_OF_DAY)) + ":"
				+ agregarCeros(2, inicio.get(Calendar.MINUTE));
	}

	public String getStringFin() {
		Calendar fin = getFin();
		return agregarCeros(2, fin.get(Calendar.HOUR_OF_DAY)) + ":"
				+ agregarCeros(2, fin.get(Calendar.MINUTE));
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

	/**
	 * Ver si es necesario este metodo
	 * 
	 * @param ctx
	 * @param diaDeLaSemana
	 * @param hora
	 * @param limit
	 * @return
	 */
	public static ArrayList<Modulo> getModulosSiguientesHORARIOS(Context ctx,
			String diaDeLaSemana, String hora, String limit) {
		// // TODO Auto-generated method stub
		// AdapterDatabase ad = new AdapterDatabase(ctx);
		//
		// ArrayList<Modulo> modulos = ad.getRecordWhere(Modulo.class,
		// nombreTabla, new String[]{"dds"}, new String[]{diaDeLaSemana],
		// camposIntervalo, minIntervalo, maxIntervalo, ordenadoPor)
		// Cursor mCursor = db.query("Horarios", null,
		// "("+"dds"+"="+diaDeLaSemana+" and " +"inicio"+">='"+hora+"')"+" or "
		// + "dds"+">="+diaDeLaSemana, null, null, null,
		// "dds"+","+"inicio",limit);
		// ArrayList<Modulo> ret = getInstancias(Modulo.class,"Horarios",
		// mCursor);
		//
		// db.close() ; DBHelper.close(); mCursor.close();
		// return ret;

		return null;
	}

}