package com.example.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.controlador.Comentario;
import com.example.controlador.Curso;
import com.example.controlador.Modelo;
import com.example.controlador.Modulo;
import com.example.controlador.Profesor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AdapterDatabase {

	public static Map<String, Tabla> tablas;

	private static final String TAG = "AdapterCursos";

	private static final String DATABASE_NAME = "OrganizadorDB";

	private static final int DATABASE_VERSION = 9;

	private final Context context;

	private DatabaseHelper DBHelper;

	/**
	 * Metodo encargado de iniciar las tablas. De aqui se obtiene toda la
	 * informacion para las clases y demases. Si se quiere agregar una tabla,
	 * colocar codigo aca
	 */
	public static void crearTablas() {
		tablas = new HashMap<String, Tabla>(4);// si se va a agregar una tabla
												// aumentar este 4
		String cursos = "Cursos";
		tablas.put(
				cursos,
				new Tabla(
						cursos,
						"iidC integer primary key autoincrement, idC integer, iidP integer, title VARCHAR not null, comentable integer, color VARCHAR not null, accion integer"));

		String horarios = "Horarios";
		tablas.put(
				horarios,
				new Tabla(

						horarios,
						"iidH integer primary key autoincrement, idH integer, iidC integer, inicio integer, fin integer, ubicacion VARCHAR, accion integer"));

		String profesores = "Profesores";

		tablas.put(
				profesores,
				new Tabla(
						profesores,
						"iidP integer primary key autoincrement, idP integer, usuario VARCHAR, contrasena VARCHAR, nombre VARCHAR, apellido VARCHAR, accion integer"));

		String comentarios = "Comentarios";
		tablas.put(
				comentarios,
				new Tabla(
						comentarios,
						"iidCom integer primary key autoincrement, idCom integer, iidH integer, fecha date, comentario text"));

		Curso.setKeys(tablas.get(cursos).keys, cursos);
		Modulo.setKeys(tablas.get(horarios).keys, horarios);
		Profesor.setKeys(tablas.get(profesores).keys, profesores);
		Comentario.setKeys(tablas.get(comentarios).keys, comentarios);
	}

	public AdapterDatabase(Context ctx) {

		this.context = ctx;
		DBHelper = new DatabaseHelper(context, tablas);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		Map<String, Tabla> tablas;

		DatabaseHelper(Context context, Map<String, Tabla> t) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			tablas = t;
		}

		
		@Override
		public void onCreate(SQLiteDatabase db) {

			try {

				int size = tablas.size();
				String[] keys = new String[size];
				tablas.keySet().toArray(keys);

				for (int i = 0; i < size; i++) {
					Tabla tActual = tablas.get(keys[i]);
					int sizeTabla = tActual.campos.size();
					String TABLE_CREATE = "create table if not exists "
							+ tActual.nombre + " (";
					for (int k = 0; k < sizeTabla; k++) {
						TABLE_CREATE += tActual.campos.get(k)[0] + " "
								+ tActual.campos.get(k)[1] + " "
								+ tActual.campos.get(k)[2];

						if (k == sizeTabla - 1) {
							TABLE_CREATE += ");";
						} else {
							TABLE_CREATE += ", ";

						}

					}
					System.out.println(TABLE_CREATE);
					db.execSQL(TABLE_CREATE.trim());

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");

			int size = tablas.size();
			for (int i = 0; i < size; i++) {
				db.execSQL("DROP TABLE IF EXISTS " + tablas.get(i).nombre);
			}
			onCreate(db);
		}
	}

	/**
	 * Inserta una nueva fila a la tabla, NOTA: NO INGRESAR EL ID AUTOGENERABLE
	 * 
	 * @param nombreTabla
	 * @param params
	 *            son los parametros de la fila. Para mas informacion ver
	 *            OrdenParametros.txt
	 * @return
	 */
	public long insertRecord(String nombreTabla, Object[] params) {
		String[] nombresCampos = getNombresCampos(nombreTabla);

		ContentValues initialValues = new ContentValues();
		SQLiteDatabase db = DBHelper.getWritableDatabase();
		
		for (int i = 1; i < nombresCampos.length; i++) {
			initialValues.put(nombresCampos[i], params[i-1].toString());
		}
		
		long ret = db.insert(nombreTabla, null, initialValues);
		db.close();
		DBHelper.close();
		return ret;
	}

	/**
	 * Borra la fila rowId de la tabla nombreTabla
	 * 
	 * @param nombreTabla
	 * @param rowId
	 * @return
	 */
	public boolean deleteRecord(String nombreTabla, long rowId) {
		String nombreLlave = tablas.get(nombreTabla).getNombreLlave();
		SQLiteDatabase db = DBHelper.getWritableDatabase();
		boolean ret = db.delete(nombreTabla, nombreLlave + "=" + rowId, null) > 0;
		db.close();
		DBHelper.close();
		return ret;
	}

	/**
	 * Obtiene todos los objetos(filas) que esten en la tabla indicada
	 * 
	 * @param _class
	 * @param nombreTabla
	 * @return
	 */
	public <T extends Modelo> ArrayList<T> getAllRecords(Class<T> _class,
			String nombreTabla) {
		SQLiteDatabase db = DBHelper.getWritableDatabase();
		String[] nombreCampos = getNombresCampos(nombreTabla);
		Cursor ret = db.query(nombreTabla, nombreCampos, null, null, null,
				null, null);
		ArrayList<T> instancias = obtenerInstancias(_class, nombreTabla, ret);

		db.close();
		DBHelper.close();
		ret.close();
		return instancias;
	}

	/**
	 * Obtiene objetos creados con los parametros de las filas apuntadas por el
	 * cursor
	 * 
	 * @param <T>
	 * @param ret
	 * @return
	 */
	private <T extends Modelo> ArrayList<T> obtenerInstancias(Class<T> _class, String nombreTabla, Cursor ret)
	{
		
		ArrayList<T> arrayList = new ArrayList<T>();

		if (ret.moveToFirst()) {
			do {
				String id = ret.getString(0);
				T instancia = getInstance(_class);
				// c = mcursor

				int numColumn = ret.getColumnCount();
				Object[] params = new Object[numColumn];
				for (int i = 0; i < numColumn; i++) {
					params[i] = ret.getString(i);
				}

				instancia.setData(id, params);
				arrayList.add(instancia);

			} while (ret.moveToNext());
		}

		return arrayList;
	}

	/**
	 * Obtiene el objeto cuyo id=rowId de nombreTabla
	 * 
	 * @param _class
	 * @param nombreTabla
	 * @param rowId
	 * @return
	 * @throws SQLException
	 */
	public <T extends Modelo> T getRecord(Class<T> _class, String nombreTabla,
			long rowId) throws SQLException {
		SQLiteDatabase db = DBHelper.getWritableDatabase();
		Tabla t = tablas.get(nombreTabla);
		Cursor mCursor = db.query(nombreTabla, getNombresCampos(nombreTabla),
				t.getNombreLlave() + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {

			mCursor.moveToFirst();
		}
		ArrayList<T> listaDeUnaInstancia = obtenerInstancias(_class,
				nombreTabla, mCursor);

		db.close();
		DBHelper.close();
		mCursor.close();
		if (listaDeUnaInstancia.size() != 0) {
			return listaDeUnaInstancia.get(0);
		}
		return null;
	}
	/**
	 * Obtiene Record segun el idMaster
	 * @param _class
	 * @param nombreTabla
	 * @param rowId
	 * @return
	 * @throws SQLException
	 */
	public <T extends Modelo> T getRecordIdMaster(Class<T> _class, String nombreTabla,	long rowId) throws SQLException {
		SQLiteDatabase db = DBHelper.getWritableDatabase();
		Tabla t = tablas.get(nombreTabla);
		Cursor mCursor = db.query(nombreTabla, getNombresCampos(nombreTabla),
				t.getNombreLlaveMaster() + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {

			mCursor.moveToFirst();
		}
		ArrayList<T> listaDeUnaInstancia = obtenerInstancias(_class,
				nombreTabla, mCursor);

		db.close();
		DBHelper.close();
		mCursor.close();
		if (listaDeUnaInstancia.size() != 0) {
			return listaDeUnaInstancia.get(0);
		}
		return null;
	}

	/**
	 * Obtiene todos los objetos(filas) que cumplan con las condiciones
	 * especificadas con los parametros. NOTA: El orden en que se pongan los
	 * campos debe ser consistente con el orden de los valores. NOTA: Si no se
	 * quiere un limite max, colocar null. Es valido colocar null, o new
	 * String[]{null}, o new String[]{"2",null}. En el ultimo caso indica que el
	 * rango del primer campo tiene max, pero el del segundo no
	 * 
	 * @param _class
	 *            Clase que representa a la tabla
	 * 
	 * @param nombreTabla
	 *            Nombre de la tabla
	 * 
	 * @param camposIguales
	 *            Campos que tienen una restriccion de igualdad. por ejemplo si
	 *            queremos todos los objetos en que "dds" = 3, aqui va new
	 *            String[]{"dds"}
	 * @param valoresIguales
	 *            Valores que toman los campos señalados en camposIguales. En el
	 *            ejemplo , iria new String[]{"3"}
	 * @param camposIntervalo
	 *            Campos que tienen una restriccion de igualdad, por ejemplo
	 *            3<="dds"<=5. INCLUYE EXTREMOS
	 * @param minIntervalo
	 *            Valores minimo, en el caso anterior es new String[]{"3"}
	 * @param maxIntervalo
	 *            Valores maximos, en el caso anterior es new String[]{"5"}
	 * @param ordenadoPor
	 *            Campo segun el cual se quieren ordenar los parametros (NO
	 *            PROBADO)
	 * @return Un ArrayList de objetos que cumplen con lo pedido
	 */
	public <T extends Modelo> ArrayList<T> getRecordWhere(Class<T> _class,
			String nombreTabla, String[] camposIguales,
			String[] valoresIguales, String[] camposIntervalo,
			String[] minIntervalo, String[] maxIntervalo, String ordenadoPor) {
		
		SQLiteDatabase db = DBHelper.getWritableDatabase();
		String SQLCODE = generarCodigoIgual(camposIguales, valoresIguales);

		SQLCODE = SQLCODE+ "AND "+ generarCodigoIntervalo(minIntervalo, camposIntervalo, maxIntervalo);
		if (SQLCODE.trim().endsWith("AND")) {
			SQLCODE = SQLCODE.trim().substring(0, SQLCODE.length() - 4);
		}
		if(SQLCODE.trim().startsWith("AND")){
			SQLCODE = SQLCODE.trim().substring(3, SQLCODE.length());
		}
		System.out.println("\n SQLCODE: " + SQLCODE);
		Cursor mCursor = db.query(nombreTabla, getNombresCampos(nombreTabla),SQLCODE.trim(), null, null, null, ordenadoPor);

		if (mCursor != null) 
			mCursor.moveToFirst();

		
		
		ArrayList<T> listaInstancias = obtenerInstancias(_class, nombreTabla,mCursor);
		mCursor.close();
		db.close();
		DBHelper.close();
		

		return listaInstancias;
	}

	/**
	 * Crea el codigo SQL que se encarga de pedir que campos[i] =valores[i] para
	 * todo i
	 * 
	 * @param campos
	 * @param valores
	 * @return
	 */
	private String generarCodigoIgual(String[] campos, String[] valores) {
		if (campos != null) {
			String txt = "";
			for (int i = 0; i < campos.length; i++) {
				if(valores[i]!=null)
					txt += campos[i] + "= '" + valores[i] + "' AND ";
			}

			return txt.substring(0, txt.length() - 4);
		} else {
			return "";
		}
	}

	/**
	 * Crea el codigo SQL que se encarga de pedir que min[i]<=campos[i]<=max[i]
	 * para todo i
	 * 
	 * @param min
	 * @param campos
	 * @param max
	 * @return
	 */
	private String generarCodigoIntervalo(String[] min, String[] campos,
			String[] max) {
		if (campos != null && (max != null || min != null)) {
			String txt = "";

			for (int i = 0; i < campos.length; i++) {
				if (min != null && min[i] != null)
					txt += "'" + min[i] + "'<= " + campos[i] + " AND";
				if (max != null && max[i] != null)
					txt += " " + campos[i] + " <= '" + max[i] + "' AND";
			}

			System.out.println("codigoIntervalo= "+ txt.substring(0, txt.length() - 4));
			return txt.substring(0, txt.length() - 4);

		} else {
			return "";
		}

	}

	// ---updates a record---
	/**
	 * Actualiza una fila de la tabla con los datos de params. En params[0]
	 * siempre esta el id Unico !
	 * 
	 * @param nombreTabla
	 * @param params
	 *            NOTA : params[0] siempre será el id Para informacion sobre el
	 *            orden de los parametros, ver OrdenParametros.txt
	 * @return
	 */
	public boolean updateRecord(String nombreTabla, String[] params) { // updateRecord("Cursos",new
																		// String[]{4,"2","Negro",...}
		SQLiteDatabase db = DBHelper.getWritableDatabase();
		Tabla t = tablas.get(nombreTabla);
		int size = t.campos.size();
		ArrayList<String[]> campos = t.campos;
		ContentValues args = new ContentValues();
		for (int i = 0; i < size; i++) {
			args.put(campos.get(i)[0], params[i]);
		}
		boolean ret = db.update(nombreTabla, args, campos.get(0)[0] + "="
				+ params[0], null) > 0;
		db.close();
		DBHelper.close();

		return ret;
	}

	/**
	 * Obtiene los nombres de las columnas de la tabla indicada
	 * 
	 * @param nombreTabla
	 * @return
	 */
	public String[] getNombresCampos(String nombreTabla) {
		System.out.println("\nNombre de Tabla: " + nombreTabla);

		Tabla t = tablas.get(nombreTabla);
		int size = t.campos.size();
		String[] hashNombresCampos = new String[size];

		for (int i = 0; i < hashNombresCampos.length; i++) {
			hashNombresCampos[i] = t.campos.get(i)[0];
		}
		return hashNombresCampos;

	}

	/**
	 * Obtiene un objeto creado con el constructor vacio de la clase indicada
	 * 
	 * @param _class
	 * @return
	 */
	private static <D> D getInstance(Class<D> _class) {
		try {
			return _class.newInstance();
		} catch (Exception _ex) {
			_ex.printStackTrace();
		}
		return null;
	}
	
	public static String[] getKeys(String nombreTabla)
	{
		return AdapterDatabase.tablas.get(nombreTabla).keys;
	}
}
