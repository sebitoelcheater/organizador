package com.example.version2;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class ViewEstatica extends RelativeLayout{
	     
	    public ViewEstatica(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        initView(context);
	    }
	    public ViewEstatica(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        initView(context);
	    }
	    public ViewEstatica(Context context) {
	        super(context);
	        initView(context);
	    }
	    
	    private void initView(Context context) {
	        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
	        inflater.inflate(R.layout.activity_actividad_horario_semanal, this, true);
	        
	    }
	 
	 	 }



