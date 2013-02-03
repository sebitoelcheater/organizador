//SEBASTIAN
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

import com.example.controlador.Functions;
import com.example.controlador.Curso;
import com.example.controlador.Modulo;
import com.example.version2.R;
import com.example.version2.ActividadRamos.MiArrayAdapter;
import com.example.version2.ColorPickerDialog.OnColorChangedListener;

public class ActividadEdicionRamo extends ListActivity implements
		OnItemClickListener, OnColorChangedListener {

	public class MiModuloEditandoArrayAdapter extends ArrayAdapter<Modulo> {

		private List<Modulo> objects;

		public MiModuloEditandoArrayAdapter(Context context,
				int textViewResourceId, List<Modulo> listaModulos) {
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			// Recoje la view de la lista
			View fila = inflater.inflate(R.layout.item_modulo_editando, parent,
					false);
			// Recoje textview donde va el nombre del ramo
			TextView diaModulo = (TextView) fila.findViewById(R.id.diaModulo);
			TextView salaModulo = (TextView) fila.findViewById(R.id.salaModulo);
			TextView horaInicio = (TextView) fila.findViewById(R.id.horaInicio);
			TextView horaFin = (TextView) fila.findViewById(R.id.horaFin);
			// Le pone el nombre al campo de texto del nombre del ramo
			diaModulo.setText(objects.get(position).getNombreDiaDeLaSemana());
			horaInicio.setText(Functions.getHoraYMinutos(objects.get(position).getInicio()));
			horaFin.setText(Functions.getHoraYMinutos(objects.get(position).getFin()));
			salaModulo.setText(" " + objects.get(position).getUbicacion());

			Button boton_editar = (Button) fila
					.findViewById(R.id.botonEditarModulo);
			boton_editar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String idModulo = objects.get(position).getId();
					Bundle bundle = new Bundle();
					bundle.putString("idModulo", idModulo);
					bundle.putString("CASO", "EDITAR");
					showDialog(getIdUnica(), bundle); // Cuidado con el
															// showdialog....

				}
			});
			Button boton_eliminar_modulo = (Button) fila
					.findViewById(R.id.botonEliminarModulo);
			boton_eliminar_modulo
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							Bundle bundle = new Bundle();
							bundle.putString("idModulo", objects.get(position)
									.getId());
							bundle.putString("CASO", "ELIMINAR");
							showDialog(getIdUnica(), bundle); // Cuidado con
																	// el
																	// showdialog....

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
		// Recibe el mensaje del inetent
		Intent intent = getIntent();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setTitle(R.string.app_name);
		// Aquí recibe la id (como string) del ramo a editar
		idRamoAEditar = intent.getStringExtra("id");

		Curso c = Curso.getCurso(this, idRamoAEditar);

		cursoAEditar = c;

		String nombreOriginal = cursoAEditar.getNombre();

		ArrayList<Modulo> array_modulos = Modulo.getModulosPorIdCurso(
				this, c);

		setContentView(R.layout.activity_actividad_edicion_ramo);
		/*
		 * Spinner numeroDeNotas = (Spinner)findViewById(R.id.spinner1);
		 * //numeroDeNotas.setAdapter(new
		 * ArrayAdapter<Integer>(this,android.R.layout
		 * .simple_spinner_item,numeros)); //ListView listaDeModulos =
		 * (ListView) findViewById(R.id.listView1);
		 * //listaDeModulos.setAdapter(new
		 * ArrayAdapter<String>(this,android.R.layout
		 * .simple_expandable_list_item_1,modulos));
		 * //listaDeModulos.setOnItemClickListener(this); //TextView
		 * textNombreRamo = (TextView) findViewById(R.id.textNombreRamo);
		 * textNombreRamo.setText(idRamoAEditar);
		 */

		EditText campoTextoNombre = (EditText) findViewById(R.id.nombreRamoAEditar);

		campoTextoNombre.setText(nombreOriginal);

		Button botonColor = (Button) findViewById(R.id.botonColor);
		String color = cursoAEditar.getColor();
		botonColor.setBackgroundColor(Color.rgb(Functions.getRed(color),
				Functions.getGreen(color), Functions.getBlue(color)));

		adaptador = new MiModuloEditandoArrayAdapter(this,
				R.layout.item_modulo_editando, array_modulos);
		setListAdapter(adaptador);

	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.activity_actividad_datos_del_ramo,
	 * menu); return true; }
	 */
	public static int getIdUnica() {

		/* Entrega una id única. Vital para crear dialogos únicos */
		Calendar now = Calendar.getInstance();
		String year = String.valueOf(now.get(Calendar.YEAR));
		String month = String.valueOf(now.get(Calendar.MONTH)); // Note: zero
																// based!
		String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH)); // 2
		String hour = String.valueOf(now.get(Calendar.HOUR_OF_DAY));// 2
		String minute = String.valueOf(now.get(Calendar.MINUTE));// 2
		String second = String.valueOf(now.get(Calendar.SECOND));// 2
		String millis = String.valueOf(now.get(Calendar.MILLISECOND));// 3

		// year + month + day +
		// No puede quedar más largo, porque el rango de n
		String idunicaStr = hour + minute + second + millis;
		int idunicaInt = Integer.valueOf(idunicaStr);
		return (idunicaInt);
	}

	public void actualizarColor() {
		Button botonColor = (Button) findViewById(R.id.botonColor);
		String color = cursoAEditar.getColor();
		botonColor.setBackgroundColor(Color.rgb(Functions.getRed(color),
				Functions.getGreen(color), Functions.getBlue(color)));

	}

	public void actualizarModulos() {
		
		Curso c = Curso.getCurso(this,idRamoAEditar);

		ArrayList<Modulo> nuevo_array_modulos = Modulo
				.getModulosPorIdCurso(this, c);

		adaptador.clear();
		for (Modulo modulo : nuevo_array_modulos) {
			adaptador.agregarModulo(modulo);
		}
		adaptador.notifyDataSetChanged();

	}

	public void guardarCambios(View view) throws Exception {
		EditText campoTextoNombre = (EditText) findViewById(R.id.nombreRamoAEditar);
		String nuevoNombre = campoTextoNombre.getText().toString();
		String nombreOriginal = cursoAEditar.getNombre();
		String colorOriginal = cursoAEditar.getColor();
		String comentableOriginal = cursoAEditar.getComentable();
		if (nuevoNombre.equals("")) {
			Toast.makeText(view.getContext(), "Elija un nombre para el curso",
					Toast.LENGTH_LONG).show();
		} else {
			if (nuevoNombre != nombreOriginal) {
				cursoAEditar.actualizar(this, colorOriginal, nuevoNombre.trim(), Functions.intToBoolean(Integer.parseInt(comentableOriginal)));
				// cursoAEditar.pull(view.getContext());// REV

			}
			setResult(RESULT_OK);
			finish();
		}
	}

	public void eliminarCurso(View view) {
		Bundle bundle = new Bundle();
		bundle.putString("CASO", "ELIMINARCURSO");
		bundle.putString("idCurso", idRamoAEditar);
		showDialog(1, bundle);
	}

	public void agregarModulo(View view) {

		Bundle bundle = new Bundle();
		bundle.putString("CASO", "NUEVO");
		showDialog(getIdUnica(), bundle);

	}

	public void cambiarColor(View view) {

		String color = cursoAEditar.getColor();
		int intColor = Color.rgb(Functions.getRed(color),
				Functions.getGreen(color), Functions.getBlue(color));
		new ColorPickerDialog(this, this, "", intColor, intColor).show();
	}

	public void colorChanged(String key, int color) {
		// TODO Auto-generated method stub
		String nombreOriginal = cursoAEditar.getNombre();
		String colorOriginal = cursoAEditar.getColor();
		String comentableOriginal = cursoAEditar.getComentable();
		cursoAEditar.actualizar(this, Functions.agregarCeros(3, Color.red(color)), nombreOriginal, Functions.intToBoolean(Integer.parseInt(comentableOriginal)));
		try {
			cursoAEditar.actualizar(this);
		} catch (Exception e) {
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
		/*
		 * caso = "ELIMINAR" significa que se desea eliminar el módulo caso =
		 * "EDITAR" significa que se desea editar el módulo XD
		 */
		if (caso == "ELIMINAR") {

			d.setContentView(R.layout.dialogo_eliminar_modulo);
			d.setTitle("Desea Eliminar:");
			Button boton_cancelar_eliminar_modulo = (Button) d
					.findViewById(R.id.botonCancelarEliminarModulo);
			Button boton_aceptar_eliminar_modulo = (Button) d
					.findViewById(R.id.botonAceptarEliminarModulo);
			String id_modulo = b.getString("idModulo");
			
			final Modulo moduloAEditar = Modulo.getModulo(this,id_modulo);
			
			TextView diaModulo = (TextView) d.findViewById(R.id.diaModulo);
			TextView horaInicio = (TextView) d.findViewById(R.id.horaInicio);
			TextView horaFin = (TextView) d.findViewById(R.id.horaFin);
			// TextView textidModulo=(TextView)d.findViewById(R.id.idModulo);
			// TextView textidModulo2=(TextView)d.findViewById(R.id.idModulo2);

			// Le pone el nombre al campo de texto del nombre del ramo
			diaModulo.setText(moduloAEditar.getNombreDiaDeLaSemana());
			horaInicio.setText(Functions.getHoraYMinutos(moduloAEditar.getInicio()));
			horaFin.setText(Functions.getHoraYMinutos(moduloAEditar.getFin()));
			// textidModulo.setText(moduloAEditar.getId());
			// textidModulo2.setText(idModulo2);

			boton_cancelar_eliminar_modulo
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							/* TODO: Controlador.eliminarModulo(id_modulo); */

							d.dismiss(); // Cierra el diálogo

						}

					});

			boton_aceptar_eliminar_modulo
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {

							/*
							 * TODO, recoger en el Buble la id del Módulo y
							 * eliminar el módulo* Además actualizar la lista
							 * de los módulos
							 */
							moduloAEditar.borrarModulo(ActividadEdicionRamo.this);
							actualizarModulos();
							d.dismiss(); // Cierra el diálogo

						}

					});

			return d;
		} else if (caso == "EDITAR") {
			String[] diasDeLaSemana = { "Domingo", "Lunes", "Martes",
					"Mi�rcoles", "Jueves", "Viernes", "S�bado" };// CORRERSE EN
																	// UN
																	// INDICE...DOMINGO
																	// ==1

			d.setContentView(R.layout.dialogo_modulo_nuevo);
			d.setTitle("Editar M�dulo");
			Button boton_cancelar = (Button) d.findViewById(R.id.button2);
			Button boton_aceptar = (Button) d.findViewById(R.id.button1);
			Spinner spinnerDias = (Spinner) d.findViewById(R.id.spinner1);
			TimePicker tPInicio = (TimePicker) d.findViewById(R.id.timePicker1);
			TimePicker tPFin = (TimePicker) d.findViewById(R.id.timePicker2);
			String id_modulo = b.getString("idModulo");

			EditText campoTextoLugar = (EditText) d
					.findViewById(R.id.salaModuloAEditar);
			
			final Modulo moduloAEditar = Modulo.getModulo(this,id_modulo);
			String salaOriginal = moduloAEditar.getUbicacion();
			campoTextoLugar.setText(salaOriginal);
			// campoTextoLugar.setText(moduloAEditar.getNombreDiaDeLaSemana());

			// /Agregando datos
			spinnerDias.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, diasDeLaSemana));
			System.out.println("holi"+moduloAEditar.getDiaDeLaSemana());
			spinnerDias.setSelection(Integer.parseInt(moduloAEditar.getDiaDeLaSemana()) - 1);
			tPInicio.setIs24HourView(true);
			tPInicio.setCurrentHour(Functions.getHoraDelDia((moduloAEditar.getInicio())));
			tPInicio.setCurrentMinute(Functions.getMinutosDelDia(((moduloAEditar.getInicio()))));
			tPFin.setIs24HourView(true);
			tPFin.setCurrentHour(Functions.getHoraDelDia((moduloAEditar.getFin())));
			tPFin.setCurrentMinute(Functions.getMinutosDelDia(((moduloAEditar.getFin()))));
			// /Agregando datos

			boton_cancelar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					/* TODO: Controlador.eliminarModulo(id_modulo); */

					d.dismiss(); // Cierra el diálogo

				}

			});

			boton_aceptar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					/*
					 * TODO, recoger en el Buble la id del Módulo y eliminar el
					 * módulo* Además actualizar la lista de los módulos
					 */
					Spinner spinnerDias = (Spinner) d
							.findViewById(R.id.spinner1);
					TimePicker tPInicio = (TimePicker) d
							.findViewById(R.id.timePicker1);
					TimePicker tPFin = (TimePicker) d
							.findViewById(R.id.timePicker2);

					EditText campoTextoLugar = (EditText) d
							.findViewById(R.id.salaModuloAEditar);

					String nuevoLugar = campoTextoLugar.getText().toString();
					// moduloAEditar.establecerNombre(v.getContext(),
					// nuevoLugar);
					

					int inicio = Functions.dateToMin(spinnerDias.getSelectedItemPosition() + 1,tPInicio.getCurrentHour(),tPInicio.getCurrentMinute());
					int fin = Functions.dateToMin(spinnerDias.getSelectedItemPosition() + 1,tPFin.getCurrentHour(),tPFin.getCurrentMinute());
					
					if (inicio<fin) {
						if (Modulo.puedoCambiarModulo(v.getContext(),inicio, fin, moduloAEditar)) {
							moduloAEditar.actualizar(v.getContext(), inicio, fin, nuevoLugar);
							d.dismiss();
							// Cierra el diálogo
						} else {
							Toast.makeText(v.getContext(), "Modulo ocupado",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(v.getContext(),
								"Inicio debe ser antes de Fin",
								Toast.LENGTH_LONG).show();
					}

					actualizarModulos();

				}

			});

			// ESTO ES PARA QUE LOS PICKER NO SE PASEN DE LISTOS
			tPInicio.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

				public void onTimeChanged(TimePicker view, int hourOfDay,
						int minute) {
					// TODO Auto-generated method stub
					if (hourOfDay > 22)
						view.setCurrentHour(8);
					if (hourOfDay < 8)
						view.setCurrentHour(22);
				}

			});
			tPFin.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

				public void onTimeChanged(TimePicker view, int hourOfDay,
						int minute) {
					// TODO Auto-generated method stub
					if (hourOfDay > 22)
						view.setCurrentHour(8);
					if (hourOfDay < 8)
						view.setCurrentHour(22);
				}

			});
			// ESTO ES PARA QUE LOS PICKER NO SE PASEN DE LISTOS

			return d;
			/*
			 * TODO: Crear un layout para la edición de módulo recoger todos
			 * los datos del módulo colocarlos y permitir su edición recopilar
			 * los cambios hacer los cambios a través del controlador
			 * actualizar la lista de módulos
			 */

		} else if (caso == "NUEVO") {
			/*
			 * TODO: Crear un layout para nuevo modulo recopilar los datos del
			 * nuevo módulo hacer los cambios a través del controlador
			 * actualizar la lista de módulos
			 */
			String[] diasDeLaSemana = { "Domingo", "Lunes", "Martes",
					"Mi�rcoles", "Jueves", "Viernes", "S�bado" };// CORRERSE EN
																	// UN
																	// INDICE...DOMINGO
																	// ==1

			d.setContentView(R.layout.dialogo_modulo_nuevo);
			d.setTitle("Agregar M�dulo");
			Button boton_cancelar = (Button) d.findViewById(R.id.button2);
			Button boton_aceptar = (Button) d.findViewById(R.id.button1);
			Spinner spinnerDias = (Spinner) d.findViewById(R.id.spinner1);
			TimePicker tPInicio = (TimePicker) d.findViewById(R.id.timePicker1);
			TimePicker tPFin = (TimePicker) d.findViewById(R.id.timePicker2);
			tPInicio.setIs24HourView(true);
			tPFin.setIs24HourView(true);

			// /Agregando datos
			spinnerDias.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, diasDeLaSemana));
			tPInicio.setCurrentMinute(0);
			tPFin.setCurrentMinute(0);
			int horaDelDia = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			if (horaDelDia < 8 || horaDelDia > 22) {
				tPInicio.setCurrentHour(8);
				tPFin.setCurrentHour(8);
			}
			// /Agregando datos

			spinnerDias.setSelection(Calendar.getInstance().get(
					Calendar.DAY_OF_WEEK) - 1);

			boton_cancelar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					/* TODO: Controlador.eliminarModulo(id_modulo); */

					d.dismiss(); // Cierra el diálogo

				}

			});

			boton_aceptar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					/*
					 * TODO, recoger en el Buble la id del Módulo y eliminar el
					 * módulo* Además actualizar la lista de los módulos
					 */
					Spinner spinnerDias = (Spinner) d
							.findViewById(R.id.spinner1);
					TimePicker tPInicio = (TimePicker) d
							.findViewById(R.id.timePicker1);
					TimePicker tPFin = (TimePicker) d
							.findViewById(R.id.timePicker2);
					
					int inicio = Functions.dateToMin(spinnerDias.getSelectedItemPosition() + 1,tPInicio.getCurrentHour(),tPInicio.getCurrentMinute());
					int fin = Functions.dateToMin(spinnerDias.getSelectedItemPosition() + 1,tPFin.getCurrentHour(),tPFin.getCurrentMinute());


					EditText campoTextoLugar = (EditText) d
							.findViewById(R.id.salaModuloAEditar);

					String lugar = campoTextoLugar.getText().toString();

					if (inicio<fin) {
						
						Modulo m = null;
						try {
							m = new Modulo(v.getContext(), 0,Integer.parseInt(cursoAEditar.getIid()), inicio, fin, lugar);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // gonza esto no lo resolv�!
						if (m.save(v.getContext())) {

							actualizarModulos();
							d.dismiss(); // Cierra el diálogo
						} else {
							Toast.makeText(v.getContext(),
									"Ya existe un modulo a esta hora",
									Toast.LENGTH_LONG).show();
							d.dismiss();
						}
					} else {
						Toast.makeText(v.getContext(),
								"Inicio debe ser antes de Fin",
								Toast.LENGTH_LONG).show();

					}

				}

			});

			// ESTO ES PARA QUE LOS PICKER NO SE PASEN DE LISTOS
			tPInicio.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

				public void onTimeChanged(TimePicker view, int hourOfDay,
						int minute) {
					// TODO Auto-generated method stub
					if (hourOfDay > 22)
						view.setCurrentHour(8);
					if (hourOfDay < 8)
						view.setCurrentHour(22);
				}

			});
			tPFin.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

				public void onTimeChanged(TimePicker view, int hourOfDay,
						int minute) {
					// TODO Auto-generated method stub
					if (hourOfDay > 22)
						view.setCurrentHour(8);
					if (hourOfDay < 8)
						view.setCurrentHour(22);
				}

			});
			// ESTO ES PARA QUE LOS PICKER NO SE PASEN DE LISTOS

			return d;

		}

		else if (caso == "ELIMINARCURSO") {
			d.setContentView(R.layout.dialogo_eliminar_curso);
			d.setTitle("Desea Eliminar:");

			Button boton_cancelar_eliminar_modulo = (Button) d
					.findViewById(R.id.button1);
			Button boton_aceptar_eliminar_modulo = (Button) d
					.findViewById(R.id.button2);

			String id_curso = b.getString("idCurso");

			Curso c = Curso.getCurso(this, id_curso);

			TextView nombreCurso = (TextView) d.findViewById(R.id.nombreCurso);

			nombreCurso.setText(c.getNombre());

			boton_cancelar_eliminar_modulo
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							/* TODO: Controlador.eliminarModulo(id_modulo); */
							d.dismiss(); // Cierra el diálogo

						}

					});

			boton_aceptar_eliminar_modulo
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							/* TODO: Controlador.eliminarModulo(id_modulo); */

							Curso c = Curso.getCurso(v.getContext(),idRamoAEditar);

							c.borrarCurso(v.getContext());
							setResult(RESULT_OK);
							finish();

							d.dismiss(); // Cierra el diálogo

						}

					});
			return d;
		}
		return d;
	}

}