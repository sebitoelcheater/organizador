package com.example.version2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.controlador.Controlador;
import com.example.controlador.Curso;
import com.example.controlador.Modulo;
import com.example.data.AdapterDatabase;
import com.example.version2.ActividadRamos.MiArrayAdapter;
import com.example.version2.ColorPickerDialog.OnColorChangedListener;



public class ActividadEdicionRamo extends ListActivity implements OnItemClickListener,OnColorChangedListener {
	
	public class MiModuloEditandoArrayAdapter extends ArrayAdapter<Modulo> {

		  private List<Modulo> objects;
		   
		  public MiModuloEditandoArrayAdapter(Context context, int textViewResourceId, List<Modulo> listaModulos) {
				super(context, textViewResourceId, listaModulos);
				this.objects = listaModulos;
			}
		

		  public void agregarModulo(Modulo modulo) {
	            objects.add(modulo);
	        }

	        public void clear() {
	            objects.clear();
	        }
		  @Override
		  public View getView(final int position, View convertView, ViewGroup parent) {
			  	LayoutInflater inflater=getLayoutInflater();
			  	//Recoje la view de la lista
			  	View fila=inflater.inflate(R.layout.item_modulo_editando, parent, false);
			  	//Recoje textview donde va el nombre del ramo
				TextView diaModulo=(TextView)fila.findViewById(R.id.diaModulo);
				TextView salaModulo=(TextView)fila.findViewById(R.id.salaModulo);
				TextView horaInicio=(TextView)fila.findViewById(R.id.horaInicio);
				TextView horaFin=(TextView)fila.findViewById(R.id.horaFin);
				//Le pone el nombre al campo de texto del nombre del ramo
				diaModulo.setText(objects.get(position).obtenerNombreDiaDeLaSemana());
				horaInicio.setText(objects.get(position).obtenerStringInicio());
				horaFin.setText(objects.get(position).obtenerStringFin());
				salaModulo.setText(" "+objects.get(position).obtenerUbicacion());
				
				
				Button boton_editar = (Button)fila.findViewById(R.id.botonEditarModulo);
				boton_editar.setOnClickListener(new View.OnClickListener() {
		             public void onClick(View v) {
		            	 String idModulo = objects.get(position).obtenerId();
			             	Bundle bundle = new Bundle();
			            	bundle.putString("idModulo", idModulo);
			            	bundle.putString("CASO", "EDITAR");
			            	showDialog(obtenerIdUnica(),bundle); //Cuidado con el showdialog....
		            	 
		                
		             }
		         });
				Button boton_eliminar_modulo = (Button)fila.findViewById(R.id.botonEliminarModulo);
				boton_eliminar_modulo.setOnClickListener(new View.OnClickListener() {
		             public void onClick(View v) {
		             	Bundle bundle = new Bundle();
		            	bundle.putString("idModulo", objects.get(position).obtenerId());
		            	bundle.putString("CASO", "ELIMINAR");
		            	showDialog(obtenerIdUnica(),bundle); //Cuidado con el showdialog....


		                 
		             }
		         });
				
			  return fila;
		  }
		} 

	
	  private String idRamoAEditar = null;
		private MiModuloEditandoArrayAdapter adaptador;


		public Curso cursoAEditar;
		public Modulo moduloAEditar;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Recibe el mensaje del inetent
        Intent intent = getIntent();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle(R.string.app_name);
        //Aqu铆 recibe la id (como string) del ramo a editar
        idRamoAEditar = intent.getStringExtra("id");
        
        AdapterDatabase ad = new AdapterDatabase(this);
		
   		Curso c =ad.getRecord(Curso.class, "Cursos", Long.parseLong(idRamoAEditar)); 

        cursoAEditar = c;
       
        String nombreOriginal = cursoAEditar.obtenerNombre();

        ArrayList<Modulo> array_modulos = Controlador.obtenerModulosPorIdCurso(this, idRamoAEditar);

        
        
        setContentView(R.layout.activity_actividad_edicion_ramo);
        /*Spinner numeroDeNotas = (Spinner)findViewById(R.id.spinner1);
        //numeroDeNotas.setAdapter(new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,numeros));
        //ListView listaDeModulos = (ListView) findViewById(R.id.listView1);
        //listaDeModulos.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,modulos));
        //listaDeModulos.setOnItemClickListener(this);
        //TextView textNombreRamo = (TextView) findViewById(R.id.textNombreRamo);
        textNombreRamo.setText(idRamoAEditar);*/
        
        EditText campoTextoNombre = (EditText) findViewById(R.id.nombreRamoAEditar);

        campoTextoNombre.setText(nombreOriginal);
        
        Button botonColor = (Button) findViewById(R.id.botonColor);
        String color = cursoAEditar.obtenerColor();
        botonColor.setBackgroundColor(Color.rgb(Controlador.getRed(color), Controlador.getGreen(color), Controlador.getBlue(color)));
        
        
        adaptador = new MiModuloEditandoArrayAdapter(this, R.layout.item_modulo_editando, array_modulos);
        setListAdapter(adaptador);

        
        
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_actividad_datos_del_ramo, menu);
        return true;
    }*/
public static int obtenerIdUnica(){
    	
    	/*Entrega una id 煤nica. Vital para crear dialogos 煤nicos */
    	Calendar now = Calendar.getInstance();
		String year = String.valueOf(now.get(Calendar.YEAR));
		String month = String.valueOf(now.get(Calendar.MONTH)); // Note: zero based!
		String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH)); //2
		String hour = String.valueOf(now.get(Calendar.HOUR_OF_DAY));//2
		String minute = String.valueOf(now.get(Calendar.MINUTE));//2
		String second = String.valueOf(now.get(Calendar.SECOND));//2
		String millis = String.valueOf(now.get(Calendar.MILLISECOND));//3
		
		//year + month + day +
		//No puede quedar m谩s largo, porque el rango de n
		String idunicaStr = hour + minute + second + millis;
		 int idunicaInt	=  Integer.valueOf(idunicaStr);
		return (idunicaInt);
    }

public void actualizarColor()
{
	Button botonColor = (Button) findViewById(R.id.botonColor);
    String color = cursoAEditar.obtenerColor();
    botonColor.setBackgroundColor(Color.rgb(Controlador.getRed(color), Controlador.getGreen(color), Controlador.getBlue(color)));
    
  	
}
public void actualizarModulos()
	{
	
   	ArrayList<Modulo> nuevo_array_modulos = Controlador.obtenerModulosPorIdCurso(this, idRamoAEditar);

       
       adaptador.clear();
       for(Modulo modulo : nuevo_array_modulos){
       	adaptador.agregarModulo(modulo);
       }
       adaptador.notifyDataSetChanged();
       
	}
    public void guardarCambios(View view) throws Exception
	{
        EditText campoTextoNombre = (EditText) findViewById(R.id.nombreRamoAEditar);
        String nuevoNombre = campoTextoNombre.getText().toString();
    	String nombreOriginal = cursoAEditar.obtenerNombre();
    	if(nuevoNombre.equals("")){Toast.makeText(view.getContext(), "Elija un nombre para el curso", Toast.LENGTH_LONG).show();}
    	else{
		if (nuevoNombre != nombreOriginal){
			cursoAEditar.setNombre(nuevoNombre.trim());
			//cursoAEditar.pull(view.getContext());// REV
			
		}
		setResult(RESULT_OK);
		  finish();}
	}
    
    public void eliminarCurso(View view)
	{
    	Bundle bundle = new Bundle();
    	bundle.putString("CASO", "ELIMINARCURSO");
    	bundle.putString("idCurso", idRamoAEditar);
		showDialog(1,bundle);
	}
    
    public void agregarModulo(View view)
    {
    	
    		Bundle bundle = new Bundle();
    		bundle.putString("CASO", "NUEVO");
        	showDialog(obtenerIdUnica(),bundle);
    	
    	
    }
    
    public void cambiarColor(View view)
    {
    	
    	String color = cursoAEditar.obtenerColor();
    	int intColor = Color.rgb(Controlador.getRed(color), Controlador.getGreen(color), Controlador.getBlue(color));
    	new ColorPickerDialog(this,this,"",intColor,intColor).show();
    }
    
    public void colorChanged(String key, int color) {
    	// TODO Auto-generated method stub
     	cursoAEditar.setColor(Controlador.agregarCeros(3,Color.red(color)));
    	try
    	{
    		cursoAEditar.actualizar(this);
    	}
    	catch (Exception e)
    	{
    		System.out.print("ERROR AL ACTUALIZAR");
    	}
    	actualizarColor();
    }

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
	
protected Dialog onCreateDialog(int id, Bundle b) {
    	
    	final Dialog d = new Dialog(this);
    	String caso = b.getString("CASO"); 
    	/*caso = "ELIMINAR" significa que se desea eliminar el m贸dulo
    	 * caso = "EDITAR" significa que se desea editar el m贸dulo XD*/
    	if (caso == "ELIMINAR") {

    		d.setContentView(R.layout.dialogo_eliminar_modulo);
    		d.setTitle("Desea Eliminar:");
    		Button boton_cancelar_eliminar_modulo = (Button) d.findViewById(R.id.botonCancelarEliminarModulo);
    		Button boton_aceptar_eliminar_modulo = (Button) d.findViewById(R.id.botonAceptarEliminarModulo);
    		String id_modulo = b.getString("idModulo");
            moduloAEditar = new Modulo(this,id_modulo);
            TextView diaModulo=(TextView)d.findViewById(R.id.diaModulo);
			TextView horaInicio=(TextView)d.findViewById(R.id.horaInicio);
			TextView horaFin=(TextView)d.findViewById(R.id.horaFin);
			//TextView textidModulo=(TextView)d.findViewById(R.id.idModulo);
			//TextView textidModulo2=(TextView)d.findViewById(R.id.idModulo2);

			
			//Le pone el nombre al campo de texto del nombre del ramo
			diaModulo.setText(moduloAEditar.obtenerNombreDiaDeLaSemana());
			horaInicio.setText(" "+moduloAEditar.obtenerStringInicio());
			horaFin.setText(moduloAEditar.obtenerStringFin());
			//textidModulo.setText(moduloAEditar.obtenerId());
			//textidModulo2.setText(idModulo2);
            

		
    		boton_cancelar_eliminar_modulo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	/*TODO: Controlador.eliminarModulo(id_modulo);*/
                	
            		d.dismiss(); //Cierra el di谩logo

                	}

    		});
    		
    		boton_aceptar_eliminar_modulo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	
                	/*TODO, recoger en el Buble la id del M贸dulo y eliminar el m贸dulo*
                	 * Adem谩s actualizar la lista de los m贸dulos*/
                	moduloAEditar.borrarModulo(ActividadEdicionRamo.this);
            		actualizarModulos();
            		d.dismiss(); //Cierra el di谩logo

                	}

    		});
    		
    		return d;
    		}
    	else if (caso == "EDITAR") {
    		String []diasDeLaSemana = {"Domingo","Lunes","Martes","Mi閞coles","Jueves","Viernes","S醔ado"};//CORRERSE EN UN INDICE...DOMINGO ==1
    		
    		d.setContentView(R.layout.dialogo_modulo_nuevo);
    		d.setTitle("Editar M骴ulo");
    		Button boton_cancelar = (Button) d.findViewById(R.id.button2);
    		Button boton_aceptar = (Button) d.findViewById(R.id.button1);
    		Spinner spinnerDias = (Spinner) d.findViewById(R.id.spinner1);
    		TimePicker tPInicio = (TimePicker) d.findViewById(R.id.timePicker1);
    		TimePicker tPFin = (TimePicker) d.findViewById(R.id.timePicker2);
    		String id_modulo = b.getString("idModulo");
    		
    		EditText campoTextoLugar = (EditText) d.findViewById(R.id.salaModuloAEditar);

            moduloAEditar = new Modulo(this,id_modulo);
            String salaOriginal = moduloAEditar.obtenerUbicacion();
           campoTextoLugar.setText(salaOriginal);
            //campoTextoLugar.setText(moduloAEditar.obtenerNombreDiaDeLaSemana());

            
    		///Agregando datos
    		spinnerDias.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,diasDeLaSemana));
    		spinnerDias.setSelection(Integer.parseInt(moduloAEditar.obtenerDiaDeLaSemana())-1);
    		tPInicio.setIs24HourView(true);
    		tPInicio.setCurrentHour(moduloAEditar.obtenerInicio().get(Calendar.HOUR_OF_DAY));
    		tPInicio.setCurrentMinute(moduloAEditar.obtenerInicio().get(Calendar.MINUTE));
    		tPFin.setIs24HourView(true);
    		tPFin.setCurrentHour(moduloAEditar.obtenerFin().get(Calendar.HOUR_OF_DAY));
    		tPFin.setCurrentMinute(moduloAEditar.obtenerFin().get(Calendar.MINUTE));
    		///Agregando datos
    		
            
            		
    		boton_cancelar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	/*TODO: Controlador.eliminarModulo(id_modulo);*/
                	
            		d.dismiss(); //Cierra el di谩logo

                	}

    		});
    		
    		boton_aceptar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	
                	/*TODO, recoger en el Buble la id del M贸dulo y eliminar el m贸dulo*
                	 * Adem谩s actualizar la lista de los m贸dulos*/
                	Spinner spinnerDias = (Spinner) d.findViewById(R.id.spinner1);
                	TimePicker tPInicio = (TimePicker) d.findViewById(R.id.timePicker1);
            		TimePicker tPFin = (TimePicker) d.findViewById(R.id.timePicker2);
            		
            		EditText campoTextoLugar = (EditText) d.findViewById(R.id.salaModuloAEditar);

            		
            		String nuevoLugar = campoTextoLugar.getText().toString();
            		//moduloAEditar.establecerNombre(v.getContext(), nuevoLugar);
            		moduloAEditar.setUbicacion(nuevoLugar);
                	moduloAEditar.setDiaDeLaSemana(""+(spinnerDias.getSelectedItemPosition()+1));
                	
                	Calendar inicio = moduloAEditar.obtenerInicio();
                	inicio.set(Calendar.HOUR_OF_DAY, tPInicio.getCurrentHour());
                	inicio.set(Calendar.MINUTE,tPInicio.getCurrentMinute());
                	
                	Calendar fin = moduloAEditar.obtenerFin();
                	fin.set(Calendar.HOUR_OF_DAY, tPFin.getCurrentHour());
                	fin.set(Calendar.MINUTE,tPFin.getCurrentMinute());
                	if(inicio.before(fin))
                	{
                		if(Controlador.puedoCambiarModulo(v.getContext(), spinnerDias.getSelectedItemPosition()+1, inicio, fin,moduloAEditar))
                		{
                			moduloAEditar.setInicio( inicio);
                			moduloAEditar.setFin(fin);
                			actualizarModulos();
                			d.dismiss();
                			//Cierra el di谩logo
                		}
                		else{
                			Toast.makeText(v.getContext(), "Modulo ocupado", Toast.LENGTH_LONG).show();
                		}
                	}
                	else
                	{
                		Toast.makeText(v.getContext(), "Inicio debe ser antes de Fin", Toast.LENGTH_LONG).show();
                	}
                	
                	actualizarModulos();

                	}

    		});
    		
    		
    		//ESTO ES PARA QUE LOS PICKER NO SE PASEN DE LISTOS
    		tPInicio.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){

				public void onTimeChanged(TimePicker view, int hourOfDay,int minute) {
					// TODO Auto-generated method stub
					if(hourOfDay> 22)
						view.setCurrentHour(8);
					if(hourOfDay<8)
						view.setCurrentHour(22);
				}
    			
    			
    		});
    		tPFin.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){

				public void onTimeChanged(TimePicker view, int hourOfDay,
						int minute) {
					// TODO Auto-generated method stub
					if(hourOfDay> 22)
						view.setCurrentHour(8);
					if(hourOfDay<8)
						view.setCurrentHour(22);
				}
    			
    			
    		});
    		//ESTO ES PARA QUE LOS PICKER NO SE PASEN DE LISTOS
    		
    		return d;
    		/*TODO: Crear un layout para la edici贸n de m贸dulo
    		 * recoger todos los datos del m贸dulo
    		 * colocarlos y permitir su edici贸n
    		 * recopilar los cambios
    		 * hacer los cambios a trav茅s del controlador
    		 * actualizar la lista de m贸dulos*/
    		
    		}
    	else if (caso == "NUEVO") {
    		/*TODO: Crear un layout para nuevo modulo
    		 * recopilar los datos del nuevo m贸dulo
    		 * hacer los cambios a trav茅s del controlador
    		 * actualizar la lista de m贸dulos*/
    		String []diasDeLaSemana = {"Domingo","Lunes","Martes","Mi閞coles","Jueves","Viernes","S醔ado"};//CORRERSE EN UN INDICE...DOMINGO ==1
    		
    		d.setContentView(R.layout.dialogo_modulo_nuevo);
    		d.setTitle("Agregar M骴ulo");
    		Button boton_cancelar = (Button) d.findViewById(R.id.button2);
    		Button boton_aceptar = (Button) d.findViewById(R.id.button1);
    		Spinner spinnerDias = (Spinner) d.findViewById(R.id.spinner1);
    		TimePicker tPInicio = (TimePicker) d.findViewById(R.id.timePicker1);
    		TimePicker tPFin = (TimePicker) d.findViewById(R.id.timePicker2);
    		tPInicio.setIs24HourView(true);
    		tPFin.setIs24HourView(true);
            
    		
    		///Agregando datos
    		spinnerDias.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,diasDeLaSemana));
    		tPInicio.setCurrentMinute(0);
    		tPFin.setCurrentMinute(0);
    		int horaDelDia = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    		if(horaDelDia<8 || horaDelDia>22)
    		{
    			tPInicio.setCurrentHour(8);
        		tPFin.setCurrentHour(8);
    		}
    		///Agregando datos
    		
            spinnerDias.setSelection(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1);
            		
    		boton_cancelar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	/*TODO: Controlador.eliminarModulo(id_modulo);*/
                	
            		d.dismiss(); //Cierra el di谩logo

                	}

    		});
    		
    		boton_aceptar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	
                	/*TODO, recoger en el Buble la id del M贸dulo y eliminar el m贸dulo*
                	 * Adem谩s actualizar la lista de los m贸dulos*/
                	Spinner spinnerDias = (Spinner) d.findViewById(R.id.spinner1);
                	TimePicker tPInicio = (TimePicker) d.findViewById(R.id.timePicker1);
            		TimePicker tPFin = (TimePicker) d.findViewById(R.id.timePicker2);
            		Calendar inicio = Calendar.getInstance();
            		inicio.set(Calendar.HOUR_OF_DAY, tPInicio.getCurrentHour());
                	inicio.set(Calendar.MINUTE,tPInicio.getCurrentMinute());
                	
                	Calendar fin = Calendar.getInstance();
                	fin.set(Calendar.HOUR_OF_DAY, tPFin.getCurrentHour());
                	fin.set(Calendar.MINUTE,tPFin.getCurrentMinute());
                	
                	
                	EditText campoTextoLugar = (EditText) d.findViewById(R.id.salaModuloAEditar);

            		String lugar = campoTextoLugar.getText().toString();
            		
            		
            		
                	if(inicio.before(fin))
                	{	
                		if(Controlador.crearNuevoModulo(v.getContext(), 0, Integer.parseInt(cursoAEditar.obtenerId()), spinnerDias.getSelectedItemPosition()+1, inicio, fin, lugar))
                		{
                			
                 
                			actualizarModulos();
                			d.dismiss(); //Cierra el di谩logo
                		}
                		else
                		{
                			Toast.makeText(v.getContext(), "Ya existe un modulo a esta hora", Toast.LENGTH_LONG).show();
                			d.dismiss();
                		}
                	}else{
                		Toast.makeText(v.getContext(), "Inicio debe ser antes de Fin", Toast.LENGTH_LONG).show();
                		
                	}

                	}

    		});
    		
    		//ESTO ES PARA QUE LOS PICKER NO SE PASEN DE LISTOS
    		tPInicio.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){

				public void onTimeChanged(TimePicker view, int hourOfDay,
						int minute) {
					// TODO Auto-generated method stub
					if(hourOfDay> 22)
						view.setCurrentHour(8);
					if(hourOfDay<8)
						view.setCurrentHour(22);
				}
    			
    			
    		});
    		tPFin.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){

				public void onTimeChanged(TimePicker view, int hourOfDay,
						int minute) {
					// TODO Auto-generated method stub
					if(hourOfDay> 22)
						view.setCurrentHour(8);
					if(hourOfDay<8)
						view.setCurrentHour(22);
				}
    			
    			
    		});
    		//ESTO ES PARA QUE LOS PICKER NO SE PASEN DE LISTOS
    		
    		return d;

    		}
    	
    	else if(caso == "ELIMINARCURSO")
    	{
    		d.setContentView(R.layout.dialogo_eliminar_curso);
        		d.setTitle("Desea Eliminar:");
        		
        		Button boton_cancelar_eliminar_modulo = (Button) d.findViewById(R.id.button1);
        		Button boton_aceptar_eliminar_modulo = (Button) d.findViewById(R.id.button2);
        		
        		String id_curso = b.getString("idCurso");
        	    AdapterDatabase ad = new AdapterDatabase(this);
        		
        		Curso c =ad.getRecord(Curso.class, "Cursos", Long.parseLong(id_curso));

                TextView nombreCurso=(TextView)d.findViewById(R.id.nombreCurso);
    			
    			nombreCurso.setText(c.obtenerNombre());

    		
        		boton_cancelar_eliminar_modulo.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	/*TODO: Controlador.eliminarModulo(id_modulo);*/
                    		d.dismiss(); //Cierra el di谩logo

                    	}

        		});
        		
        		boton_aceptar_eliminar_modulo.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	/*TODO: Controlador.eliminarModulo(id_modulo);*/
                        AdapterDatabase ad = new AdapterDatabase(v.getContext());
                		
                		Curso c =ad.getRecord(Curso.class, "Cursos", Long.parseLong(idRamoAEditar));

                    	c.borrarCurso(v.getContext());
                		setResult(RESULT_OK);
                		  finish();
                	
                		d.dismiss(); //Cierra el di谩logo

                    	}

        		});
        		return d;
    	}
    	return d;
    	}

	
	
	
	
	
}