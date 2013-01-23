//SEBASTIAN
package com.example.frontend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.controlador.Functions;
import com.example.controlador.Curso;
import com.example.controlador.Modulo;
import com.example.frontend.ActividadEdicionRamo.MiModuloEditandoArrayAdapter;
import com.example.version2.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;

public class ActividadFeedBackear extends Activity implements
		OnItemClickListener {

	public class MiModuloFeedBackeandoArrayAdapter extends ArrayAdapter<Modulo> {

		private List<Modulo> modulosPorFeedBackear;

		public MiModuloFeedBackeandoArrayAdapter(Context context,
				int textViewResourceId, List<Modulo> listaModulos) {
			super(context, textViewResourceId, listaModulos);
			this.modulosPorFeedBackear = listaModulos;
		}

		public void agregarModulo(Modulo modulo) {
			modulosPorFeedBackear.add(modulo);
		}

		public void clear() {
			modulosPorFeedBackear.clear();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			int horas = 3;
			LayoutInflater inflater = getLayoutInflater();

			View fila = inflater.inflate(R.layout.feedbackeo, parent, false);

			TextView nombre = (TextView) fila.findViewById(R.id.textView1);
			TextView fecha = (TextView) fila.findViewById(R.id.textView2);
			TextView deadLine = (TextView) fila.findViewById(R.id.textView3);
			// Chronometer
			// cronometro=(Chronometer)fila.findViewById(R.id.chronometer1);
			try {

				Curso c = Curso.getCurso(convertView.getContext(),modulosPorFeedBackear.get(position).getIdCurso());

				String nombreCurso = c.getNombre();

				nombre.setText(nombreCurso);

				Modulo m = modulosPorFeedBackear.get(position);
				Calendar ahora = Calendar.getInstance();
				int fin = m.getFin();

				Calendar clon = (Calendar) ahora.clone();
				clon.set(Calendar.HOUR_OF_DAY, Functions.getHoraDelDia(fin)
						+ horas);
				clon.set(Calendar.MINUTE, Functions.getMinutosDelDia(fin));

				long diferencia = clon.getTimeInMillis()
						- ahora.getTimeInMillis();
				long difS = diferencia / 1000;

				int horass = ((int) difS) / 3600;
				int minutos = ((int) (difS - horass * 3600)) / 60;
				/*
				 * Calendar cDiferencia = (Calendar) clon.clone();
				 * cDiferencia.setTimeInMillis(diferencia);
				 */

				String Tempo = agregarCeros(2, horass) + ":"
						+ agregarCeros(2, minutos);

				deadLine.setText("Te quedan " + Tempo + " para FeedBackear");
			} catch (NullPointerException e) {
			}

			// fecha.setText(modulosPorFeedBackear.get(position).getNombreDiaDeLaSemana()+" "+modulosPorFeedBackear.get(position).getStringInicio()+" - "+modulosPorFeedBackear.get(position).getStringFin());

			// ACA VA EL MANEJO DEL CRONOMETRO
			// cronometro.start();

			// ESTO ESTA EN TRANCE MIENTRAS BUSCO UN CRONOMETRO
			// ACA DEBERIAN IR LOS LISTENER

			return fila;
		}

		private String agregarCeros(int n, int i) {
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
	}

	private MiModuloFeedBackeandoArrayAdapter adaptador;
	ArrayList<Modulo> modulosAFeedBackear;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actividad_feed_backear);

		ListView lista = (ListView) findViewById(R.id.listView1);

		modulosAFeedBackear = Modulo.getLosFeedBackeables(this);

		lista.setAdapter(new MiModuloFeedBackeandoArrayAdapter(this,
				R.layout.item_modulo_editando, modulosAFeedBackear));

		lista.setOnItemClickListener(this);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.activity_actividad_feed_backear, menu);
	 * return true; }
	 */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, ActividadFeedback.class);
		intent.putExtra("ID", modulosAFeedBackear.get(arg2).getId());

		startActivity(intent);
	}
}
