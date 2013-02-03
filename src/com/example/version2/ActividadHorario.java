//SEBASTIAN
package com.example.version2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlador.*;
import com.example.data.*;
import com.example.version2.R;

public class ActividadHorario extends Activity { // PONER COLORES :)

	int hoy;
	int viendo;
	private TweetHelper tweet_hlp;
	private int TWITTER_AUTH = 1;
	private AdapterDatabase ad;

	// String []diasDeLaSemana =
	// {"Domingo","Lunes","Martes","Miercoles","Jueves","Viernes","Sabado"};
	ArrayList<HashMap<String, String>> array_modulos = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ad = new AdapterDatabase(this);
		tweet_hlp = new TweetHelper(this);
		setContentView(R.layout.activity_actividad_horario);
		setTitle("Organizador");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Calendar c = Calendar.getInstance();
		hoy = c.get(Calendar.DAY_OF_WEEK);
		viendo = hoy;
		/*
		 * antigua funcionalidad Spinner numeroDeNotas =
		 * (Spinner)findViewById(R.id.spinner1); numeroDeNotas.setAdapter(new
		 * ArrayAdapter
		 * <String>(this,android.R.layout.simple_spinner_item,diasDeLaSemana));
		 */

		// Esto puede ser un actualizable
		/*
		 * ListView modulos = (ListView)findViewById(R.id.listView1);
		 * modulos.setAdapter(new
		 * ArrayAdapter<Integer>(this,android.R.layout.simple_list_item_1
		 * ,array_modulos));
		 */
		generaModulos();
		// Button ver = (Button)findViewById(R.id.button1);
		// ver.setOnClickListener(this);

		// Hasta aca voy bien 5

	}

	private static final int SHARE = Menu.FIRST + 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		/*
		 * menu.add(Menu.NONE, NEW_ITEM, 1, R.string.menu_newitem).setIcon(
		 * android.R.drawable.ic_menu_add); menu.add(Menu.NONE, RESET_ALL, 2,
		 * R.string.menu_resetall).setIcon( android.R.drawable.ic_menu_revert);
		 * menu.add(Menu.NONE, SHARE, 3, R.string.menu_share).setIcon(
		 * android.R.drawable.ic_menu_share); menu.add(Menu.NONE, PREFERENCES,
		 * 4, R.string.menu_preferences).setIcon(
		 * android.R.drawable.ic_menu_preferences);
		 */
		menu.add(Menu.NONE, SHARE, 5, "Compartir!").setIcon(
				R.drawable.ic_twitter);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		/*
		 * case NEW_ITEM: Intent call = new Intent(TallyphantActivity.this,
		 * ItemEdit.class); startActivityForResult(call, REQUEST_EDITITEM);
		 * return true;
		 * 
		 * case RESET_ALL: db.resetAll(); remoteNotifyAll(); updateItemList();
		 * return true;
		 * 
		 * case SHARE: Intent i = new
		 * Intent(android.content.Intent.ACTION_SEND); i.setType("text/plain");
		 * i.putExtra(Intent.EXTRA_SUBJECT, R.string.share_subject);
		 * Vector<DB.Item> items = db.getItems(); String msg = ""; for (DB.Item
		 * titem : items) msg += titem.getFormatted() + "\n";
		 * i.putExtra(Intent.EXTRA_TEXT, msg);
		 * startActivity(Intent.createChooser(i,
		 * getString(R.string.share_title))); return true;
		 * 
		 * case PREFERENCES: Intent prefs = new Intent(getBaseContext(),
		 * Preferences.class); startActivityForResult(prefs, REQUEST_PREFS);
		 * return true;
		 */

		case SHARE:
			LayoutInflater lis = LayoutInflater.from(this);
			View view1 = lis.inflate(R.layout.compartir, null);

			// Fill in the version...

			Builder p1 = new AlertDialog.Builder(this).setView(view1);
			final AlertDialog alrt1 = p1.create();
			alrt1.setIcon(R.drawable.ic_twitter);
			alrt1.setTitle("COMPARTIR MI HORARIO");// ACA HACER EL SWICTH SEGUN
													// LA ACTIVIDAD
			alrt1.setButton(AlertDialog.BUTTON_NEUTRAL, "COMPARTIR",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {
								// validamos login de nuevo
								validar_login();
								// obtenemos texto de componente de texto
								// enviamos tweet y obtenemos estado de envio
								boolean twt_snd_status = tweet_hlp
										.Send_Tweet(formatearComentario());
								if (twt_snd_status)// OK enviado
								{ // cambiamos imagen de estad a twitter_OK
									Show_Toast("Tu tweet ha sido enviado");

								} else {
									// cambiamos imagen de estad a twitter_fial
									Show_Toast("Hubo un Error no se pudo enviar tu tweet.");

								}

							} catch (Exception e) {
								// TODO Auto-generated catch block
								// System.out.println(e.getMessage());
								e.printStackTrace();
							}
						}
					});
			alrt1.setButton(AlertDialog.BUTTON_NEGATIVE, "Salir",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});
			alrt1.show();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.activity_actividad_horario, menu);
	 * return true; }
	 */

	/*
	 * public void onClick(View v) { // TODO Auto-generated method stub
	 * //generaModulos(numeroDeNotas.getSelectedItemPosition());
	 * generaModulos();
	 * 
	 * }
	 */
	/*
	 * METODO ANTIGUO private void generaModulos(int selectedItemPosition) { //
	 * TODO Auto-generated method stub
	 * 
	 * ArrayList<Modulo> modulos = Controlador.getModulosSegunDia(this,
	 * selectedItemPosition+1); this.array_modulos= new ArrayList<String>();
	 * for(Modulo m : modulos) array_modulos.add(m.getNombre());
	 * 
	 * 
	 * }
	 */

	protected String formatearComentario() {
		// TODO Auto-generated method stub

		// TRABAJO
		String a = "";
		if (hoy != viendo) {
			a = "Mira mi Horario de " + stringDDS(viendo) + " : ";
			ArrayList<Modulo> modulos = Modulo
					.getModulosSegunDiaOrdenadosSegunInicio(this, viendo);
			for (Modulo m : modulos) {

				Curso c = ad.getRecord(Curso.class, "Cursos",
						Long.parseLong(m.getIdCurso()));

				a += " " + c.getNombre() + "(" + m.getInicio()
						+ "-" + m.getFin() + ")" + " ";
			}
		} else {
			ArrayList<Modulo> modulos = Modulo
					.getLosSiguientesModulosDelDia(this,Functions.getAhoraEnMinutos(), 5);
			if (modulos.size() != 0) {
				a = "Lo que me queda por hacer hoy "
						+ stringDDS(Calendar.getInstance().get(
								Calendar.DAY_OF_WEEK)) + " : ";
				for (Modulo m : modulos) {

					Curso c = ad.getRecord(Curso.class, "Cursos",
							Long.parseLong(m.getIdCurso()));

					a += " " + c.getNombre() + "("
							+ m.getInicio() + "-"
							+ m.getFin() + ")" + " ";
				}
			} else {
				a = "Terminaron las clases por hoy";
			}
		}
		a += "- Desde Organizador";
		return a;
	}

	public void Show_Toast(String txt) {

		Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
	}

	public void validar_login() {
		// Verificar si existen claves alamacenadas en el telefono si no llamar
		// al webview.
		if (!tweet_hlp.verify_logindata()) {
			Show_Toast("No se han encontrado claves en el telefono las descargaremos.");
			// creamos intent y pasamos get_AuthenticationURL
			Intent i = new Intent(ActividadHorario.this,
					TwitterWebActivity.class);
			i.putExtra("URL", tweet_hlp.get_AuthenticationURL());
			startActivityForResult(i, TWITTER_AUTH);

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// si el codigo obtenido es ok
		if (resultCode == Activity.RESULT_OK) {
			// Obtenemos oauth_verifier pasado por el webview
			String oauthVerifier = (String) data.getExtras().get(
					"oauth_verifier");
			Log.e("oauthVerifier ->", oauthVerifier);
			// Grabamos el valor de oauthVerifier en el shared preferences
			tweet_hlp.Store_OAuth_verifier(oauthVerifier);

		}

	}

	private void generaModulos() {
		// TODO Auto-generated method stub
		ArrayList<Modulo> modulos;
		TextView titulo = (TextView) findViewById(R.id.textView1);
		if (hoy == viendo) {
			modulos = Modulo.getLosSiguientesModulosDelDia(this,Functions.getAhoraEnMinutos(), 5);// HASTA ACA VOY BIEN 3
			titulo.setText("Siguientes clases(HOY)");
		}

		else {
			modulos = Modulo.getModulosSegunDiaOrdenadosSegunInicio(
					this, viendo);
			titulo.setText(stringDDS(viendo));
		}

		this.array_modulos = new ArrayList<HashMap<String, String>>();
		for (Modulo m : modulos) {

			Curso c = ad.getRecord(Curso.class, "Cursos",
					Long.parseLong(m.getIdCurso()));

			HashMap<String, String> mapa = new HashMap<String, String>();
			if (m.getUbicacion().toUpperCase()
					.equals(c.getNombre().toUpperCase())) // ARREGLAR
																// ESTO!!!!!
				mapa.put("Nombre", m.getUbicacion());
			else
				mapa.put("Nombre",
						c.getNombre() + " - " + m.getUbicacion());
			mapa.put("Hora", Functions.getHoraYMinutos(m.getInicio()));
			array_modulos.add(mapa);
		}
		ListView listaModulos = (ListView) findViewById(R.id.listView1);
		listaModulos.setAdapter(new SimpleAdapter(this, array_modulos,
				android.R.layout.simple_list_item_2, new String[] { "Nombre",
						"Hora" }, new int[] { android.R.id.text1,
						android.R.id.text2 }));

	}

	public void verSiguiente(View v) {
		if (++viendo > 7)
			viendo = 1;

		generaModulos();
	}

	public void verAnterior(View v) {
		if (--viendo < 1)
			viendo = 7;

		generaModulos();
	}

	public void verHoy(View v) {
		viendo = hoy;
		generaModulos();
	}

	private String stringDDS(int dia) {
		if (dia == 2) {
			return "Lunes";
		} else if (dia == 3) {
			return "Martes";

		} else if (dia == 4) {
			return "Miércoles";

		} else if (dia == 5) {
			return "Jueves";
		} else if (dia == 6) {
			return "Viernes";

		} else if (dia == 7) {
			return "Sábado";

		} else if (dia == 1) {
			return "Domingo";

		} else {
			return "DIA " + dia + " ";
		}
	}
}
