package com.example.version2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;

import com.example.controlador.Functions;
import com.example.controlador.Curso;
import com.example.controlador.Modulo;
import com.example.data.AdapterDatabase;
import com.example.version2.ActividadRamos.MiArrayAdapter;

public class ActividadDatosDelRamo extends ListActivity implements
		OnItemClickListener {
	private static final int REQUEST_EDITAR_O_AGREGAR = 0;
	private String idRamoAEditar = null;

	public class MiModuloArrayAdapter extends ArrayAdapter<Modulo> {

		private List<Modulo> objects;

		public MiModuloArrayAdapter(Context context, int textViewResourceId,
				List<Modulo> listaModulos) {
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
			View fila = inflater.inflate(R.layout.item_modulo, parent, false);
			// Recoje textview donde va el nombre del ramo
			TextView diaModulo = (TextView) fila.findViewById(R.id.diaModulo);
			TextView salaModulo = (TextView) fila.findViewById(R.id.salaModulo);

			TextView horaInicio = (TextView) fila.findViewById(R.id.horaInicio);
			TextView horaFin = (TextView) fila.findViewById(R.id.horaFin);
			// Le pone el nombre al campo de texto del nombre del ramo
			diaModulo.setText(objects.get(position)
					.obtenerNombreDiaDeLaSemana());
			horaInicio.setText(objects.get(position).obtenerStringInicio());
			horaFin.setText(objects.get(position).obtenerStringFin());
			salaModulo.setText(" " + objects.get(position).obtenerUbicacion());

			return fila;
		}
	}

	private MiModuloArrayAdapter adaptador;

	public Curso cursoAVer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// Recibe el mensaje del inetent
		Intent intent = getIntent();
		// Aquí recibe la id (como string) del ramo a editar
		idRamoAEditar = intent.getStringExtra("id");

		AdapterDatabase ad = new AdapterDatabase(this);

		Curso cursoAVer = ad.getRecord(Curso.class, "Cursos",
				Long.parseLong(idRamoAEditar));

		String nombreOriginal = cursoAVer.obtenerNombre();
		ArrayList<Modulo> array_modulos = Functions.obtenerModulosPorIdCurso(
				this, idRamoAEditar);

		setContentView(R.layout.activity_actividad_datos_del_ramo);
		Button botonColor = (Button) findViewById(R.id.button2);
		String color = cursoAVer.obtenerColor();
		int intColor = Color.rgb(Functions.getRed(color),
				Functions.getGreen(color), Functions.getBlue(color));
		botonColor.setBackgroundColor(intColor);
		botonColor.setClickable(false);

		Button boton_editar_curso = (Button) findViewById(R.id.botonEditarEsteCurso);

		if (!cursoAVer.esEditable()) {
			boton_editar_curso.setVisibility(View.INVISIBLE);
		}
		;
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

		TextView campoTextoNombre = (TextView) findViewById(R.id.nombreRamoAVer);

		campoTextoNombre.setText(nombreOriginal);

		adaptador = new MiModuloArrayAdapter(this, R.layout.item_modulo,
				array_modulos);
		setListAdapter(adaptador);

	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.activity_actividad_datos_del_ramo,
	 * menu); return true; }
	 */

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	public void editarRamo(View view) {
		Intent intentEditarRamo = new Intent(this, ActividadEdicionRamo.class);
		intentEditarRamo.putExtra("id", cursoAVer.obtenerId());
		startActivityForResult(intentEditarRamo, REQUEST_EDITAR_O_AGREGAR);
	}

	public void actualizarDatosRamo() {
		/*
		 * Una vez modificados los datos a partir de esta actividad, es
		 * necesario actualizar los datos del Ramo
		 */
		// Primero actualiza el nombre

		AdapterDatabase ad = new AdapterDatabase(this);

		cursoAVer = ad.getRecord(Curso.class, "Cursos",
				Long.parseLong(idRamoAEditar));

		String nombreOriginal = cursoAVer.obtenerNombre(); // Re-obtiene el
															// nombre del ramo
		/*
		 * Este método se utiliza gracias a que obtenerNombre() se conecta con
		 * la base de datos la cual ya está actualizada
		 */
		TextView campoTextoNombre = (TextView) findViewById(R.id.nombreRamoAVer);
		campoTextoNombre.setText(nombreOriginal);

		ArrayList<Modulo> nuevo_array_modulos = Functions
				.obtenerModulosPorIdCurso(this, idRamoAEditar);

		adaptador.clear();
		for (Modulo modulo : nuevo_array_modulos) {
			adaptador.agregarModulo(modulo);
		}
		adaptador.notifyDataSetChanged();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		/*
		 * Cuando se llega de un intent, se devuelve un "resultado". Según este
		 * resultado se realizan varias acciones. En este caso el único
		 * resultado por ahora es un OK para EDITAR O AGREGAR un Curso y en este
		 * caso, actualiza la Lista de Ramos
		 */
		case REQUEST_EDITAR_O_AGREGAR:
			if (resultCode == RESULT_OK) {
				// Método necesario para actualizar vizualmente la lista de los
				// Ramos
				actualizarDatosRamo();
				getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			}
		}
	}

	public void eliminarCurso(View v) {
		Bundle bundle = new Bundle();
		bundle.putString("idCurso", idRamoAEditar);
		showDialog(1, bundle);
	}

	protected Dialog onCreateDialog(int id, Bundle b) {

		final Dialog d = new Dialog(this);
		d.setContentView(R.layout.dialogo_eliminar_curso);
		d.setTitle("Desea Eliminar:");

		Button boton_cancelar_eliminar_modulo = (Button) d
				.findViewById(R.id.button1);
		Button boton_aceptar_eliminar_modulo = (Button) d
				.findViewById(R.id.button2);

		String id_curso = b.getString("idCurso");
		AdapterDatabase ad = new AdapterDatabase(this);

		Curso cursoAEditar = ad.getRecord(Curso.class, "Cursos",
				Long.parseLong(id_curso));

		TextView nombreCurso = (TextView) d.findViewById(R.id.nombreCurso);

		nombreCurso.setText(cursoAEditar.obtenerNombre());

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
						AdapterDatabase ad = new AdapterDatabase(v.getContext());

						Curso c = ad.getRecord(Curso.class, "Cursos",
								Long.parseLong(idRamoAEditar));
						c.borrarCurso(v.getContext());
						setResult(RESULT_OK);
						finish();

						d.dismiss(); // Cierra el diálogo

					}

				});
		return d;

	}
}