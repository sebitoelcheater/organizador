package com.example.version2;

import com.example.version2.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class DibujarLineas extends Activity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dibujar_lineas);

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout1);
		Lienzo fondo = new Lienzo(this);
		linearLayout.addView(fondo);
	}

	class Lienzo extends View {

		public Lienzo(Context context) {
			super(context);
		}

		protected void onDraw(Canvas canvas) {
			canvas.drawRGB(255, 255, 255);
			int ancho = canvas.getWidth();
			int alto = canvas.getHeight();

			Paint pincel1 = new Paint();
			pincel1.setARGB(255, 255, 0, 0);
			canvas.drawLine(70, 0, 70, alto, pincel1);
			canvas.drawLine(73, 0, 73, alto, pincel1);
			pincel1.setARGB(255, 0, 0, 0);
			int cantLineas = alto / 30 - 2;
			for (int fila = 0; fila < cantLineas; fila++) {
				canvas.drawLine(0, fila * 30 + 60, ancho, fila * 30 + 60,
						pincel1);
			}
		}
	}

}
