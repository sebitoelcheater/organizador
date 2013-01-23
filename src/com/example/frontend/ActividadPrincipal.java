package com.example.frontend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlador.*;
import com.example.version2.R;

public class ActividadPrincipal extends Activity {
	public final static String MENSAJE_EXTRA = "com.example.version0.MESSAGE";
	private PendingIntent pendingIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actividad_principal);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		/*
		 * ArrayList<Curso> cursos = Controlador.obtenerCursos(this); for(Curso
		 * c: cursos) { c.borrarCurso(this); }
		 */

		activarNotificaciones();

	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.activity_principal, menu); return true;
	 * }
	 */

	// Metodo ejecutado al presionar el boton de "Envíalo!".
	public void enviarMensaje(View view) {
		Intent intent = new Intent(this, ActividadQueMuestraMensaje.class);

		// En este caso se crea un objeto llamado campotexto que se relaciona
		// con el campo de texto. No se sabe el id, por lo que se recupera,
		// usando
		// El "nombre clave" edit_message que se le definió en
		// activity_principal.xml
		EditText campoTexto = (EditText) findViewById(R.id.edit_message);

		// Recupera el valor del objeto campoTexto, y lo pasa a String
		String texto = campoTexto.getText().toString();

		// Necesario para la comunicación entre Activities
		intent.putExtra(MENSAJE_EXTRA, texto);

		startActivity(intent);
	}

	public void verRamos(View view) {
		Intent intent = new Intent(this, ActividadRamos.class);

		// Necesario para la comunicación entre Activities
		intent.putExtra(MENSAJE_EXTRA, true);

		startActivity(intent);
	}

	public void verHorario(View view) {
		Intent intent = new Intent(this, ActividadHorario.class);

		// Necesario para la comunicación entre Activities
		intent.putExtra(MENSAJE_EXTRA, true);

		startActivity(intent);
	}

	/*
	 * public void verNotas (View view) { Intent intent = new
	 * Intent(this,ActividadNotas2.class);
	 * 
	 * //Necesario para la comunicación entre Activities
	 * intent.putExtra(MENSAJE_EXTRA,true);
	 * 
	 * 
	 * startActivity(intent); }
	 */

	public void verFormularioFeedback(View view) {
		Intent intent = new Intent(this, ActividadFeedback.class);

		// Necesario para la comunicación entre Activities
		intent.putExtra(MENSAJE_EXTRA, true);

		startActivity(intent);
	}

	private void activarNotificaciones() {
		// TODO Auto-generated method stub
		int comprobacionIntervaloSegundos = 360;// pensar esto mejor

		Intent myIntent = new Intent(ActividadPrincipal.this,
				alarmChecker.class);
		pendingIntent = PendingIntent.getService(ActividadPrincipal.this, 0,
				myIntent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 10);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(),
				comprobacionIntervaloSegundos * 1000, pendingIntent);

	}

	/*
	 * private static final int NEW_ITEM = Menu.FIRST; private static final int
	 * RESET_ALL = Menu.FIRST + 1; private static final int SHARE = Menu.FIRST +
	 * 2; private static final int PREFERENCES = Menu.FIRST + 3;
	 */
	// private static final int ABOUT = Menu.FIRST + 4;
	private static final int ABOUT = Menu.FIRST;

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
		menu.add(Menu.NONE, ABOUT, 5, R.string.menu_about).setIcon(
				android.R.drawable.ic_menu_help);
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

		case ABOUT:
			LayoutInflater li = LayoutInflater.from(this);
			View view = li.inflate(R.layout.about, null);

			// Fill in the version...
			TextView tv = (TextView) view.findViewById(R.id.version);
			PackageManager pm = getPackageManager();
			try {
				PackageInfo pi = pm.getPackageInfo(getApplicationContext()
						.getPackageName(), 0);
				tv.setText(pi.versionName);
			} catch (Exception e) {
			}

			Builder p = new AlertDialog.Builder(this).setView(view);
			final AlertDialog alrt = p.create();
			alrt.setIcon(R.drawable.ic_launcher);
			alrt.setTitle(getString(R.string.about_title));
			alrt.setButton(AlertDialog.BUTTON_NEUTRAL,
					getString(R.string.about_website),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Uri uri = Uri
									.parse("http://www.cheaper.cl/android");
							startActivity(new Intent(Intent.ACTION_VIEW, uri));
						}
					});
			alrt.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});
			alrt.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
