package com.example.version2;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class ViewModuloLista extends RelativeLayout{
	    private TextView nombreSala;
	    private TextView hora;
	    
	    
	    public ViewModuloLista(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        initView(context);
	    }
	    public ViewModuloLista(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        initView(context);
	    }
	    public ViewModuloLista(Context context) {
	        super(context);
	        initView(context);
	    }
	    
	    private void initView(Context context) {
	        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
	        inflater.inflate(R.layout.modulolista, this, true);
	 
	        nombreSala = (TextView) findViewById(R.id.textView1);
	        hora = (TextView) findViewById(R.id.textView3);
	        
	        
	    }
	 
	    public void setHora(CharSequence hora) {
	        this.hora.setText(hora);
	    }
	    public void setNombreSala(CharSequence nombreSala) {
	        this.nombreSala.setText(nombreSala);
	    }
	    
	    public void setColor(int color)
	    {
	    	 setBackgroundColor (color);
	    }
	 }



