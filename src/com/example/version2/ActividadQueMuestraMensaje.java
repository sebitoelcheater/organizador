package com.example.version2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;
import com.example.controlador.*;
import com.example.data.*;
import com.example.version2.R;

public class ActividadQueMuestraMensaje extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actividad_que_muestra_mensaje);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// Recibe el mensaje del inetent
		Intent intent = getIntent();
		String mensaje = intent
				.getStringExtra(ActividadPrincipal.MENSAJE_EXTRA);

		// Crea el TextView
		TextView textView = new TextView(this);
		textView.setTextSize(40);
		textView.setText(mensaje);

		// Deja el Text view en layout de la actividad
		setContentView(textView);

	}

}
