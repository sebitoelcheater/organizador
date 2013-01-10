package com.example.data;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import com.example.controlador.Curso;
import com.example.controlador.Modelo;
import com.example.controlador.Modulo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AdapterDatabase {

	public static Map<String,Tabla> tablas;

    private static final String TAG = "AdapterCursos";
    private static final String DATABASE_NAME= "OrganizadorDB";

    private static final int DATABASE_VERSION = 9;


    private final Context context;    

    private DatabaseHelper DBHelper;
    
    public static void crearTablas()
    {
    	tablas = new HashMap<String, Tabla>(4);//si se va a agregar una tabla aumentar este 4
    	tablas.put("Cursos",new Tabla("Cursos","iidC integer primary key autoincrement, idC integer, iidP integer, title VARCHAR not null, comentable integer, color VARCHAR not null"));
    	tablas.put("Horarios",new Tabla("Horarios","iidH integer primary key autoincrement, idH integer, iidC integer, dds integer, inicio date, fin date, ubicacion VARCHAR"));
    	tablas.put("Profesores",new Tabla("Profesores","iidP integer primary key autoincrement, idP integer, usuario VARCHAR, contrasena VARCHAR, nombre VARCHAR, apellido VARCHAR"));
    	tablas.put("Comentarios",new Tabla("Comentarios","iidCom integer primary key autoincrement, idCom integer, iidH integer, fecha date, comentario text"));
    	Curso.setKeys(tablas.get("Cursos").keys);
    	Modulo.setKeys(tablas.get("Horarios").keys);
    }
    
    public AdapterDatabase(Context ctx)
    {
    	
    	
    	this.context = ctx;
        DBHelper = new DatabaseHelper(context,tablas);
    }
    
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
    	Map<String,Tabla> tablas;
    	
        DatabaseHelper(Context context, Map<String,Tabla> t) 
        {
           super(context, DATABASE_NAME, null, DATABASE_VERSION);
       		tablas=t;
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
        	        	
        	try {
        		
        		int size = tablas.size();
        		String[] keys = new String[size];
        		tablas.keySet().toArray(keys);
        		
	        	for(int i=0;i<size;i++) 
	        	{
	        		Tabla tActual = tablas.get(keys[i]);
	        		int sizeTabla = tActual.campos.size();
	                String TABLE_CREATE="create table if not exists "+tActual.nombre+" (";
	                for(int k=0;k<sizeTabla;k++)
	                {
	                	TABLE_CREATE+= tActual.campos.get(k)[0]+" "+tActual.campos.get(k)[1]+" "+tActual.campos.get(k)[2];
	                	
	                	if(k==sizeTabla-1)
	                	{
	                		TABLE_CREATE+=");";
	                	}
	                	else
	                	{
	                		TABLE_CREATE+=", ";
	                		
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
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
          
            int size=tablas.size();
            for(int i=0;i<size;i++)
            {
            db.execSQL("DROP TABLE IF EXISTS "+tablas.get(i).nombre);
            }
            onCreate(db);
        }
    }    

    
    /**
     * Inserta una nueva fila a la tabla, NOTA: NO INGRESAR EL ID AUTOGENERABLE 
     * @param nombreTabla
     * @param params son los parametros de la fila. Para mas informacion ver OrdenParametros.txt
     * @return
     */
    public long insertRecord(String nombreTabla,String[] params )
    {
    	Tabla t = tablas.get(nombreTabla);
    	String[] nombresCampos=getNombresCampos(nombreTabla);
   
        ContentValues initialValues = new ContentValues();
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        
        for(int i=0;i<nombresCampos.length-1;i++)
        {
       		initialValues.put(nombresCampos[i+1],params[i]);
       	        	 
        }
      	long ret =db.insert(nombreTabla, null, initialValues);
    	db.close() ; DBHelper.close();
        return ret;  	
    }
   
    /**
     * Borra la fila rowId de la tabla nombreTabla
     * @param nombreTabla
     * @param rowId
     * @return
     */
    public boolean deleteRecord(String nombreTabla,long rowId) 
    {
    	String nombreLlave = tablas.get(nombreTabla).getNombreLlave();
    	SQLiteDatabase db = DBHelper.getWritableDatabase();
    	boolean ret = db.delete(nombreTabla, nombreLlave + "=" + rowId, null)>0;
    	db.close() ; DBHelper.close();
        return ret;
    }
    
    
    //---retrieves all the records---
    public <T extends Modelo> ArrayList<T> getAllRecords(Class<T> _class,String nombreTabla) 
    {
    	SQLiteDatabase db = DBHelper.getWritableDatabase();
    	String[] nombreCampos=getNombresCampos(nombreTabla);
    	Cursor ret = db.query(nombreTabla,nombreCampos,  null, null, null, null, null);
    	ArrayList<T> instancias= obtenerInstancias(_class,nombreTabla, ret);
    	
    	db.close() ; DBHelper.close(); ret.close();
    	return instancias;
    }
    

    /**
     * Obtiene los ids del cursor indicado
     * .
     * 
     * @param <T>
     * @param <T>
     * @param <T>
     * @param ret
     * @return
     */
    private  <T extends Modelo> ArrayList<T> obtenerInstancias(Class<T> _class,String nombreTabla, Cursor ret)
    
    {
    		SQLiteDatabase db = DBHelper.getWritableDatabase();
    		ArrayList<T> arrayList=new ArrayList<T>();
			
	    	if (ret.moveToFirst())
	        {
	            do {
	            	String id = ret.getString(0);
	            	T instancia = getInstance(_class);
	            	//c = mcursor
	            	
	            	int numColumn=ret.getColumnCount();
	            	Object[] params = new Object[numColumn];
	            	for(int i=0;i<numColumn;i++)
	            	{
	            		params[i]=ret.getString(i);
	            	}
	            	
	            	instancia.setData(id, params);
	                arrayList.add(instancia);
	                
	            } while (ret.moveToNext());
	        }
	    	
	    	db.close();
	    	DBHelper.close();
	    	ret.close();
	    	
	    	return arrayList;
    }

    //---retrieves a particular record---
    public <T extends Modelo> T getRecord(Class<T> _class, String nombreTabla, long rowId) throws SQLException 
    {
    	SQLiteDatabase db = DBHelper.getWritableDatabase();
    	Tabla t = tablas.get(nombreTabla);
    	Cursor mCursor = db.query(nombreTabla, getNombresCampos(nombreTabla), t.getNombreLlave()+ "=" + rowId, null, null, null, null, null);
    	if (mCursor != null) {
    		
    		mCursor.moveToFirst();
               }
    	ArrayList<T> listaDeUnaInstancia = obtenerInstancias(_class,nombreTabla, mCursor);
    	
    	db.close() ; DBHelper.close(); mCursor.close();
    	if(listaDeUnaInstancia.size()!=0)
    	{
    		return listaDeUnaInstancia.get(0);
    	}
    	return null;
    }
    
    /**
     * 
     * @param <T>
     * @param nombreTabla
     * @param nombreCampo
     * @param valorCampo
     * @return
     */
    public <T extends Modelo> ArrayList<T> getRecordWhere(Class<T> _class,String nombreTabla, String[] camposIguales,String[] valoresIguales, String[] camposIntervalo, String[] minIntervalo, String[] maxIntervalo,String ordenadoPor)
    {
    	SQLiteDatabase db = DBHelper.getWritableDatabase();
    	String SQLCODE=generarCodigoIgual(camposIguales,valoresIguales);
    	
    	
    	SQLCODE = SQLCODE+"AND "+generarCodigoIntervalo(minIntervalo,camposIntervalo,maxIntervalo);
    	if(SQLCODE.trim().endsWith("AND"))
    	{
    		SQLCODE=SQLCODE.trim().substring(0,SQLCODE.length()-4);
    	}
	    System.out.println("\n SQLCODE: "+SQLCODE);
    	Cursor mCursor = db.query(nombreTabla, getNombresCampos(nombreTabla),SQLCODE, null, null, null,ordenadoPor);
	    	
	    	if (mCursor != null) //HASTA ACA VOY BIEN 1
	    	{
	    		
	    		mCursor.moveToFirst();
	              
	    	}
	    
	    	ArrayList<T> listaInstancias = obtenerInstancias(_class,nombreTabla, mCursor);
	    	
	    	
	    	db.close() ; 
	    	DBHelper.close();
	    	mCursor.close();
	    	
	    	return listaInstancias;
    }
    private String generarCodigoIgual(String[] campos, String[] valores)
    {
    	if(campos!=null)
    	{
	    	String txt="";
	    	for(int i=0;i<campos.length;i++)
	    	{
	    		txt+=campos[i]+"= '"+valores[i]+"' AND ";
	    	}
	    	
	    	return txt.substring(0,txt.length()-4);
    	}
    	else
    	{
    		return "";
    	}
    }
    
    private String generarCodigoIntervalo(String[] min, String[] campos, String[] max)
    {
    	if(campos!=null&&(max!=null||min!=null))
    	{
	    	String txt="";
	    	
	    	for(int i=0;i<campos.length;i++)
	    	{
	    		if(min!=null&&min[i]!=null)
	    			txt+="'"+min[i]+"'<= "+campos[i]+" AND";
	    		if(max!=null&&max[i]!=null)
	    			txt+=" "+campos[i]+" <= '"+max[i]+"' AND";
	    	}
	    	
	    	System.out.println("codigoIntervalo= "+ txt.substring(0, txt.length()-4));
	    	return txt.substring(0, txt.length()-4);
    	
    	}
    	else
    	{
    		return "";
    	}
    	
    	
    }
    //---updates a record---
    /**
     * 
     * @param nombreTabla
     * @param params NOTA : params[0] siempre será el id
     * Para informacion sobre el orden de los parametros, ver OrdenParametros.txt
     * @return
     */
    public boolean updateRecord(String nombreTabla, String[] params) 
    { //updateRecord("Cursos",new String[]{4,"2","Negro",...}
    	SQLiteDatabase db = DBHelper.getWritableDatabase();
    	Tabla t = tablas.get(nombreTabla);
    	int size = t.campos.size();
    	ArrayList<String[]> campos = t.campos;
        ContentValues args = new ContentValues();
        for(int i=0;i<size;i++)
        {
        	args.put(campos.get(i)[0],params[i]);
        }
        boolean ret = db.update(nombreTabla, args, campos.get(0)[0] + "=" + params[0], null) > 0;
        db.close() ;
        DBHelper.close();
        
        return ret;
    }
    
    public void close() 
    {
        DBHelper.close();
    }
    
    public ArrayList<Modulo> comprobarTopeHorario(String diaDeLaSemana,String inicio, String fin)
    {
    	String fInicio = "'"+inicio+"'";
    	String fFin = "'"+fin+"'";
    	Tabla t = tablas.get("Horarios");
    	int size = t.campos.size();
    	
    	String KEY_INICIO="inicio";
    	String KEY_FIN = "fin";
    	SQLiteDatabase db = DBHelper.getWritableDatabase();
    	Cursor mCursor =
                 db.query(true, "Horarios", getNombresCampos("Horarios"), 
                 "dds" + "=" + diaDeLaSemana+ " AND (("+KEY_INICIO+"<"+fInicio+" AND "+ KEY_FIN+">"+fFin+") OR ("+KEY_INICIO+">="+fInicio+" AND "+ KEY_INICIO+"<"+fFin+") OR ("+KEY_FIN+">"+fInicio+" AND "+ KEY_FIN+"<="+fFin+")"+")", null, null, null, null, null);
         if (mCursor != null) {
             mCursor.moveToFirst();
         }
         
         ArrayList<Modulo> modulos = obtenerInstancias(Modulo.class,"Horarios", mCursor);
         
         
         db.close() ; DBHelper.close(); mCursor.close();
         return modulos;
    }

    public String[] getNombresCampos(String nombreTabla)
    {
    	System.out.println("\nNombre de Tabla: "+nombreTabla);
 
    	Tabla t = tablas.get(nombreTabla);
    	int size = t.campos.size();
    	String[] hashNombresCampos = new String[size];
    	
    	for(int i=0;i<hashNombresCampos.length;i++)
    	{
    		hashNombresCampos[i]=t.campos.get(i)[0];
    	}
    	return hashNombresCampos;
    	
    
    }
    
    public ArrayList<Modulo> getRecordPorCursoHORARIOS(String idCurso)
    {
    	String[] campos=getNombresCampos("Horarios");
    	SQLiteDatabase db = DBHelper.getWritableDatabase();
    	 Cursor mCursor =
                 db.query(true, "Horarios", campos, 
                 "iidC" + "=" + idCurso, null, null, null, null, null);
         if (mCursor != null) {
             mCursor.moveToFirst();
         }
         
         ArrayList<Modulo> ret = obtenerInstancias(Modulo.class,"Horarios", mCursor);
                
         
         
         db.close() ; DBHelper.close(); mCursor.close();
         return ret;
    }
    
    public ArrayList<Modulo> getModulosSiguientesHORARIOS(String diaDeLaSemana,String hora, String limit) {
  		// TODO Auto-generated method stub
    	SQLiteDatabase db = DBHelper.getWritableDatabase();
    	Cursor mCursor = db.query("Horarios", null, "("+"dds"+"="+diaDeLaSemana+" and " +"inicio"+">='"+hora+"')"+" or " + "dds"+">="+diaDeLaSemana, null, null, null, "dds"+","+"inicio",limit);
    	ArrayList<Modulo> ret = obtenerInstancias(Modulo.class,"Horarios", mCursor);
    	
    	db.close() ; DBHelper.close(); mCursor.close();
      	return ret;
  	}
  
    private static <D> D getInstance(Class<D> _class)
    {
        try
        {
            return _class.newInstance();
        }
        catch (Exception _ex)
        {
            _ex.printStackTrace();
        }
        return null;
    }
}
