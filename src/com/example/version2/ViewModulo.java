package com.example.version2;

import com.example.controlador.Modulo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewModulo extends RelativeLayout {
	private TextView horaInicio;
	private TextView nombre;
	private TextView sala;
	private TextView horaFin;
	private Modulo modulo;

	public ViewModulo(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public ViewModulo(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ViewModulo(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		inflater.inflate(R.layout.modulo, this, true);

		nombre = (TextView) findViewById(R.id.textView2);
		sala = (TextView) findViewById(R.id.textView3);

	}

	public void setNombre(CharSequence nombre) {
		this.nombre.setText(nombre);
	}

	public void setSala(CharSequence sala) {
		this.sala.setText(sala);
	}

	public void setModulo(Modulo m) {
		modulo = m;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setColor(int color) {
		setBackgroundColor(color);
	}
}
