package com.example.version2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.controlador.Controlador;
import com.example.controlador.Curso;
import com.example.controlador.DisplaySupport;
import com.example.controlador.Modulo;
import com.example.controlador.TweetHelper;
import com.example.data.AdapterDatabase;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.WindowManager;

//ESTO TRABAJA EN PIXELES

public class ActividadHorarioSemanal2 extends Activity implements OnClickListener {

	
	private Modulo moduloAEditar;
	private TweetHelper tweet_hlp;
	 private int TWITTER_AUTH=1;
	 
	 @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweet_hlp = new TweetHelper(this);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FrameLayout fl = new FrameLayout(this);  
        fl.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        setContentView(fl);
        
        dibujaLasLineas(fl);	
		fl.addView(new ViewEstatica(this));
		rellenarModulos();

		
		
		
		
       
        
        
        
    }
	 private static final int SHARE = Menu.FIRST+1;
	    
	    
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {

	        super.onCreateOptionsMenu(menu);
	        /*menu.add(Menu.NONE, NEW_ITEM, 1, R.string.menu_newitem).setIcon(
	                android.R.drawable.ic_menu_add);
	        menu.add(Menu.NONE, RESET_ALL, 2, R.string.menu_resetall).setIcon(
	                android.R.drawable.ic_menu_revert);
	        menu.add(Menu.NONE, SHARE, 3, R.string.menu_share).setIcon(
	                android.R.drawable.ic_menu_share);
	        menu.add(Menu.NONE, PREFERENCES, 4, R.string.menu_preferences).setIcon(
	                android.R.drawable.ic_menu_preferences);*/
	        menu.add(Menu.NONE,SHARE,5,"Compartir!").setIcon(R.drawable.ic_twitter);
	        return true;
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {

	        switch (item.getItemId()) {

	        /*case NEW_ITEM:
	            Intent call = new Intent(TallyphantActivity.this, ItemEdit.class);
	            startActivityForResult(call, REQUEST_EDITITEM);
	            return true;

	        case RESET_ALL:
	            db.resetAll();
	            remoteNotifyAll();
	            updateItemList();
	            return true;

	        case SHARE:
	            Intent i = new Intent(android.content.Intent.ACTION_SEND);
	            i.setType("text/plain");
	            i.putExtra(Intent.EXTRA_SUBJECT, R.string.share_subject);
	            Vector<DB.Item> items = db.getItems();
	            String msg = "";
	            for (DB.Item titem : items)
	                msg += titem.getFormatted() + "\n";
	            i.putExtra(Intent.EXTRA_TEXT, msg);
	            startActivity(Intent.createChooser(i,
	                    getString(R.string.share_title)));
	            return true;

	        case PREFERENCES:
	            Intent prefs = new Intent(getBaseContext(), Preferences.class);
	            startActivityForResult(prefs, REQUEST_PREFS);
	            return true;*/

	       
	            
	        case SHARE:
	        	 LayoutInflater lis = LayoutInflater.from(this);
	             View view1 = lis.inflate(R.layout.compartir, null);

	             // Fill in the version...
	            
	             Builder p1 = new AlertDialog.Builder(this).setView(view1);
	             final AlertDialog alrt1 = p1.create();
	             alrt1.setIcon(R.drawable.ic_twitter);
	             alrt1.setTitle("COMPARTIR MI HORARIO");//ACA HACER EL SWICTH SEGUN LA ACTIVIDAD
	             alrt1.setButton(AlertDialog.BUTTON_NEUTRAL,
	                     "COMPARTIR",
	                     new DialogInterface.OnClickListener() {
	                         public void onClick(DialogInterface dialog,
	                                 int whichButton) {
	                        	 try {
	             					//validamos login de nuevo
	                         		    
	                         		validar_login();
	             					// obtenemos texto de componente de texto
	             					// enviamos tweet y obtenemos estado de envio
	                         		boolean twt_snd_status = tweet_hlp.Send_Tweet(formatearComentario());
	             					if (twt_snd_status)// OK enviado
	             					{ // cambiamos imagen de estad a twitter_OK
	             						Show_Toast("Tu tweet ha sido enviado");
	             						
	             					} else {
	             						// cambiamos imagen de estad a twitter_fial
	             						Show_Toast("Hubo un Error no se pudo enviar tu tweet.");
	             						
	             					}

	             				} catch (Exception e) {
	             					// TODO Auto-generated catch block
	             					//System.out.println(e.getMessage());
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
    
   
	    protected String formatearComentario() {
			// TODO Auto-generated method stub
			//TRABAJO
	    	
	    	String a = "";
	    	ArrayList<Modulo> modulos = Controlador.obtenerLosSiguientesModulosDelDia(this, Calendar.getInstance(), 5);
	    	if(modulos.size()!=0){
    		a = "Lo que me queda por hacer hoy "+ stringDDS(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))+ " : ";
	    	for(Modulo m : modulos)
	    	{
	    		AdapterDatabase ad = new AdapterDatabase(this);
	    		Curso curso =ad.getRecord(Curso.class, "Cursos", Long.parseLong(m.obtenerIdCurso()));
	    		a+= " "+curso.obtenerNombre()+"("+m.obtenerStringInicio() +"-"+m.obtenerStringFin() +")" +" ";
	    	}}
	    	else{a="Terminaron las clases por hoy";}
	    	a+= "- Desde Organizador";
	    	return a;
	    	
		}

		public void Show_Toast(String txt){
			
			Toast.makeText(this,txt, Toast.LENGTH_SHORT).show();
		}
	    
	    public void validar_login(){
			//Verificar si existen claves alamacenadas en el telefono si no llamar al webview.
					if(!tweet_hlp.verify_logindata())
					{
						Show_Toast("No se han encontrado claves en el telefono las descargaremos.");
						//creamos intent y pasamos get_AuthenticationURL
						Intent i = new Intent(ActividadHorarioSemanal2.this, TwitterWebActivity.class);
						i.putExtra("URL", tweet_hlp.get_AuthenticationURL());
						startActivityForResult(i, TWITTER_AUTH);
						
					} 
		}
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			//si el codigo obtenido es ok
			if (resultCode == Activity.RESULT_OK) {
				// Obtenemos oauth_verifier pasado por el webview
				String oauthVerifier = (String) data.getExtras().get("oauth_verifier");
				Log.e("oauthVerifier ->", oauthVerifier);
				// Grabamos el valor de oauthVerifier en el shared preferences
				tweet_hlp.Store_OAuth_verifier(oauthVerifier);
				
			}

		}

	    
    private void dibujaLasLineas(FrameLayout fl) {
		// TODO Auto-generated method stub
    	LinearLayout ln = new LinearLayout(this);
        ln.addView(new Lienzo(this));
        fl.addView(ln);
		
	}


	private void rellenarModulos()  //SOLO MODULOS>= 2hrs tienen sala, por un asunto estetico
    {
	   	// TODO Auto-generated method stub
    	//LA RELACION ES 25dp  corresponde a 60 minutos 
    	double factor= (185.0/500.0);
    	int enQueVoy = 480;
    	int minutosFinAnterior=480;
    	LinearLayout l = (LinearLayout) findViewById(R.id.LinearLayoutLunes);
    	ArrayList<Modulo> modulosDia = Controlador.obtenerModulosSegunDiaOrdenadosSegunInicio(this, 2);
		
    	
    	//EDITANDO
    	for(Modulo m : modulosDia)
		{
    	
    		AdapterDatabase ad = new AdapterDatabase(this);
    		Curso curso =ad.getRecord(Curso.class, "Cursos", Long.parseLong(m.obtenerIdCurso()));
    		
    		Calendar inicio = m.obtenerInicio();
			int minutosInicio = inicio.get(Calendar.HOUR_OF_DAY)*60+inicio.get(Calendar.MINUTE);
			Calendar fin = m.obtenerFin();
			int duracionEnMinutos = fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE) - minutosInicio;
			
			
			
			if(minutosFinAnterior<minutosInicio)  //Y si enQueVoy=>minutos(ESTO NO DEBERIA PASAR....)
			{
				
				
				l.addView(new TextView(this),new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*(minutosInicio-minutosFinAnterior)))));
	
			}
			minutosFinAnterior=fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE);
				
			ViewModulo a = new ViewModulo(this);
			//a.setHoraInicio(m.obtenerStringInicio());
			//a.setHoraFin(m.obtenerStringFin());
			Curso curso2 =ad.getRecord(Curso.class, "Cursos", Long.parseLong(m.obtenerIdCurso()));
			if(curso2.esEditable()){
				
			a.setOnClickListener(this); 
			a.setClickable(true);
			a.setModulo(m);}
			
			
			if(duracionEnMinutos>=120)  a.setSala(m.obtenerUbicacion());
			
			if(duracionEnMinutos>=45) a.setNombre(curso.obtenerNombre().substring(0,3));
			
			String color = curso.obtenerColor();
			a.setColor(Color.rgb(Controlador.getRed(color), Controlador.getGreen(color), Controlador.getBlue(color)));
			l.addView(a,new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*duracionEnMinutos))));
			enQueVoy+=duracionEnMinutos;
		}
		
		//EDITANDO
		
    	enQueVoy = 480;
		l = (LinearLayout) findViewById(R.id.LinearLayoutMartes);
		modulosDia = Controlador.obtenerModulosSegunDiaOrdenadosSegunInicio(this, 3);
		for(Modulo m : modulosDia)
		{
			AdapterDatabase ad = new AdapterDatabase(this);
    		Curso curso =ad.getRecord(Curso.class, "Cursos", Long.parseLong(m.obtenerIdCurso()));
			
    		Calendar inicio = m.obtenerInicio();
			int minutos = inicio.get(Calendar.HOUR_OF_DAY)*60+inicio.get(Calendar.MINUTE);
			Calendar fin = m.obtenerFin();
			int duracionEnMinutos = fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE) - minutos;
			if(enQueVoy<minutos)  //Y si enQueVoy=>minutos(ESTO NO DEBERIA PASAR....)
			{
				l.addView(new TextView(this),new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*(minutos-enQueVoy)))));
				enQueVoy+= minutos-enQueVoy;
			}
				
			ViewModulo a = new ViewModulo(this);
			//a.setHoraInicio(m.obtenerStringInicio());
			//a.setHoraFin(m.obtenerStringFin());
			
	
    		Curso curso2 =ad.getRecord(Curso.class, "Cursos", Long.parseLong(m.obtenerIdCurso()));
			
			if(curso2.esEditable()){
				a.setOnClickListener(this);
				a.setClickable(true);
				a.setModulo(m);}
			
			if(duracionEnMinutos>=120)  a.setSala(m.obtenerUbicacion());
			
			if(duracionEnMinutos>=45) a.setNombre(curso.obtenerNombre().substring(0,3));
			String color = curso.obtenerColor();
			a.setColor(Color.rgb(Controlador.getRed(color), Controlador.getGreen(color), Controlador.getBlue(color)));
			l.addView(a,new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*duracionEnMinutos))));
			enQueVoy+=duracionEnMinutos;
		}
		
		//EDITANDO
		
		
		
		
		enQueVoy = 480;
		l = (LinearLayout) findViewById(R.id.LinearLayoutMiercoles);
		modulosDia = Controlador.obtenerModulosSegunDiaOrdenadosSegunInicio(this, 4);
		for(Modulo m : modulosDia)
		{
			
			AdapterDatabase ad = new AdapterDatabase(this);
    		Curso curso =ad.getRecord(Curso.class, "Cursos", Long.parseLong(m.obtenerIdCurso())); 
    		
    		Calendar inicio = m.obtenerInicio();
			int minutos = inicio.get(Calendar.HOUR_OF_DAY)*60+inicio.get(Calendar.MINUTE);
			Calendar fin = m.obtenerFin();
			int duracionEnMinutos = fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE) - minutos;
			if(enQueVoy<minutos)  //Y si enQueVoy=>minutos(ESTO NO DEBERIA PASAR....)
			{
				l.addView(new TextView(this),new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*(minutos-enQueVoy)))));
				enQueVoy+= minutos-enQueVoy;
			}
				
			ViewModulo a = new ViewModulo(this);
			//a.setHoraInicio(m.obtenerStringInicio());
			//a.setHoraFin(m.obtenerStringFin());
			if(curso.esEditable()){
				a.setOnClickListener(this);
				a.setClickable(true);
				a.setModulo(m);}
			
			
			if(duracionEnMinutos>=120)  a.setSala(m.obtenerUbicacion());
			
			if(duracionEnMinutos>=45) a.setNombre(curso.obtenerNombre().substring(0,3));
			String color = curso.obtenerColor();
			a.setColor(Color.rgb(Controlador.getRed(color), Controlador.getGreen(color), Controlador.getBlue(color)));
			l.addView(a,new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*duracionEnMinutos))));
			enQueVoy+=duracionEnMinutos;
		}
		
		enQueVoy = 480;
		l = (LinearLayout) findViewById(R.id.LinearLayoutJueves);
		modulosDia = Controlador.obtenerModulosSegunDiaOrdenadosSegunInicio(this, 5);
		for(Modulo m : modulosDia)
		{
			AdapterDatabase ad = new AdapterDatabase(this);
    		Curso curso =ad.getRecord(Curso.class, "Cursos", Long.parseLong(m.obtenerIdCurso()));
    		
    		Calendar inicio = m.obtenerInicio();
			int minutos = inicio.get(Calendar.HOUR_OF_DAY)*60+inicio.get(Calendar.MINUTE);
			Calendar fin = m.obtenerFin();
			int duracionEnMinutos = fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE) - minutos;
			if(enQueVoy<minutos)  //Y si enQueVoy=>minutos(ESTO NO DEBERIA PASAR....)
			{
				l.addView(new TextView(this),new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*(minutos-enQueVoy)))));
				enQueVoy+= minutos-enQueVoy;
			}
				
			ViewModulo a = new ViewModulo(this);
			//a.setHoraInicio(m.obtenerStringInicio());
			//a.setHoraFin(m.obtenerStringFin());
			if(curso.esEditable()){
				a.setOnClickListener(this);
				a.setClickable(true);
				a.setModulo(m);}
			
			
			if(duracionEnMinutos>=120)  a.setSala(m.obtenerUbicacion());
			
			if(duracionEnMinutos>=45) a.setNombre(curso.obtenerNombre().substring(0,3));
			String color = curso.obtenerColor();
			a.setColor(Color.rgb(Controlador.getRed(color), Controlador.getGreen(color), Controlador.getBlue(color)));
			l.addView(a,new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*duracionEnMinutos))));
			enQueVoy+=duracionEnMinutos;
		}
		
		enQueVoy = 480;
		l = (LinearLayout) findViewById(R.id.LinearLayoutViernes);
		modulosDia = Controlador.obtenerModulosSegunDiaOrdenadosSegunInicio(this, 6);
		for(Modulo m : modulosDia)
		{
			
			AdapterDatabase ad = new AdapterDatabase(this);
    		Curso curso =ad.getRecord(Curso.class, "Cursos", Long.parseLong(m.obtenerIdCurso()));
    		
    		Calendar inicio = m.obtenerInicio();
			int minutos = inicio.get(Calendar.HOUR_OF_DAY)*60+inicio.get(Calendar.MINUTE);
			Calendar fin = m.obtenerFin();
			int duracionEnMinutos = fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE) - minutos;
			if(enQueVoy<minutos)  //Y si enQueVoy=>minutos(ESTO NO DEBERIA PASAR....)
			{
				l.addView(new TextView(this),new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*(minutos-enQueVoy)))));
				enQueVoy+= minutos-enQueVoy;
			}
				
			ViewModulo a = new ViewModulo(this);
			//a.setHoraInicio(m.obtenerStringInicio());
			//a.setHoraFin(m.obtenerStringFin());
			
			if(curso.esEditable()){
				a.setOnClickListener(this);
				a.setClickable(true);
				a.setModulo(m);}
			
			if(duracionEnMinutos>=120)  a.setSala(m.obtenerUbicacion());
			
			if(duracionEnMinutos>=45) a.setNombre(curso.obtenerNombre().substring(0,3));
			String color = curso.obtenerColor();
			a.setColor(Color.rgb(Controlador.getRed(color), Controlador.getGreen(color), Controlador.getBlue(color)));
			l.addView(a,new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*duracionEnMinutos))));
			enQueVoy+=duracionEnMinutos;
		}
		
		enQueVoy = 480;
		l = (LinearLayout) findViewById(R.id.LinearLayoutSabado);
		modulosDia = Controlador.obtenerModulosSegunDiaOrdenadosSegunInicio(this, 7);
		for(Modulo m : modulosDia)
		{
			
			AdapterDatabase ad = new AdapterDatabase(this);
    		Curso curso =ad.getRecord(Curso.class, "Cursos", Long.parseLong(m.obtenerIdCurso()));
    		
    		Calendar inicio = m.obtenerInicio();
			int minutos = inicio.get(Calendar.HOUR_OF_DAY)*60+inicio.get(Calendar.MINUTE);
			Calendar fin = m.obtenerFin();
			int duracionEnMinutos = fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE) - minutos;
			if(enQueVoy<minutos)  //Y si enQueVoy=>minutos(ESTO NO DEBERIA PASAR....)
			{
				l.addView(new TextView(this),new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*(minutos-enQueVoy)))));
				enQueVoy+= minutos-enQueVoy;
			}
				
			ViewModulo a = new ViewModulo(this);
			//a.setHoraInicio(m.obtenerStringInicio());
			//a.setHoraFin(m.obtenerStringFin());
			if(curso.esEditable()){
				a.setOnClickListener(this);
				a.setClickable(true);
				a.setModulo(m);}
			
			
			if(duracionEnMinutos>=120)  a.setSala(m.obtenerUbicacion());
			
			if(duracionEnMinutos>=45) a.setNombre(curso.obtenerNombre().substring(0,3));
			String color = curso.obtenerColor();
			a.setColor(Color.rgb(Controlador.getRed(color), Controlador.getGreen(color), Controlador.getBlue(color)));
			l.addView(a,new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*duracionEnMinutos))));
			enQueVoy+=duracionEnMinutos;
		}
		
		enQueVoy = 480;
		l = (LinearLayout) findViewById(R.id.LinearLayoutDomingo);
		modulosDia = Controlador.obtenerModulosSegunDiaOrdenadosSegunInicio(this, 1);
		for(Modulo m : modulosDia)
		{
			
			AdapterDatabase ad = new AdapterDatabase(this);
    		Curso curso =ad.getRecord(Curso.class, "Cursos", Long.parseLong(m.obtenerIdCurso()));
    		
    		Calendar inicio = m.obtenerInicio();
			int minutos = inicio.get(Calendar.HOUR_OF_DAY)*60+inicio.get(Calendar.MINUTE);
			Calendar fin = m.obtenerFin();
			int duracionEnMinutos = fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE) - minutos;
			if(enQueVoy<minutos)  //Y si enQueVoy=>minutos(ESTO NO DEBERIA PASAR....)
			{
				l.addView(new TextView(this),new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*(minutos-enQueVoy)))));
				enQueVoy+= minutos-enQueVoy;
			}
				
			ViewModulo a = new ViewModulo(this);
			//a.setHoraInicio(m.obtenerStringInicio());
			//a.setHoraFin(m.obtenerStringFin());
			if(curso.esEditable()){
				a.setOnClickListener(this);
				a.setClickable(true);
				a.setModulo(m);}
			
			
			if(duracionEnMinutos>=120)  a.setSala(m.obtenerUbicacion());
			
			
			if(duracionEnMinutos>=45) a.setNombre(curso.obtenerNombre().substring(0,3));
			String color = curso.obtenerColor();
			a.setColor(Color.rgb(Controlador.getRed(color), Controlador.getGreen(color), Controlador.getBlue(color)));
			l.addView(a,new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*duracionEnMinutos))));
			enQueVoy+=duracionEnMinutos;
		}
		
	}


	public void onClick(View v) {
		// TODO Auto-generated method stub
		moduloAEditar = ((ViewModulo)v).getModulo();
		Bundle b = new Bundle();
		showDialog(obtenerIdUnica(),b);
		
	}
	
	
	protected Dialog onCreateDialog(int id, Bundle b)
	{
		
		final Dialog d = new Dialog(this);
    	
		
				String []diasDeLaSemana = {"Domingo","Lunes","Martes","Mi�rcoles","Jueves","Viernes","S�bado"};//CORRERSE EN UN INDICE...DOMINGO ==1
				AdapterDatabase ad = new AdapterDatabase(this);
	    		Curso curso =ad.getRecord(Curso.class, "Cursos", Long.parseLong(moduloAEditar.obtenerIdCurso()));
				
		d.setContentView(R.layout.dialogo_modulo_bkn);
		d.setTitle(curso.obtenerNombre());
		Button boton_cancelar = (Button) d.findViewById(R.id.button2);
		Button boton_aceptar = (Button) d.findViewById(R.id.button1);
		Button boton_eliminar = (Button)  d.findViewById(R.id.button3);
		Spinner spinnerDias = (Spinner) d.findViewById(R.id.spinner1);
		TimePicker tPInicio = (TimePicker) d.findViewById(R.id.timePicker1);
		TimePicker tPFin = (TimePicker) d.findViewById(R.id.timePicker2);
		
		EditText campoTextoLugar = (EditText) d.findViewById(R.id.salaModuloAEditar);

        String salaOriginal = moduloAEditar.obtenerUbicacion();
       campoTextoLugar.setText(salaOriginal);
        //campoTextoLugar.setText(moduloAEditar.obtenerNombreDiaDeLaSemana());

        
		///Agregando datos
		spinnerDias.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,diasDeLaSemana));
		spinnerDias.setSelection(Integer.parseInt(moduloAEditar.obtenerDiaDeLaSemana())-1);
		tPInicio.setIs24HourView(true);
		tPInicio.setCurrentHour(moduloAEditar.obtenerInicio().get(Calendar.HOUR_OF_DAY));
		tPInicio.setCurrentMinute(moduloAEditar.obtenerInicio().get(Calendar.MINUTE));
		tPFin.setIs24HourView(true);
		tPFin.setCurrentHour(moduloAEditar.obtenerFin().get(Calendar.HOUR_OF_DAY));
		tPFin.setCurrentMinute(moduloAEditar.obtenerFin().get(Calendar.MINUTE));
		///Agregando datos
		
        
        		
		boton_cancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	/*TODO: Controlador.eliminarModulo(id_modulo);*/
            	
        		d.dismiss(); //Cierra el diálogo

            	}

		});
		
		boton_aceptar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	/*TODO, recoger en el Buble la id del Módulo y eliminar el módulo*
            	 * Además actualizar la lista de los módulos*/
            	Spinner spinnerDias = (Spinner) d.findViewById(R.id.spinner1);
            	TimePicker tPInicio = (TimePicker) d.findViewById(R.id.timePicker1);
        		TimePicker tPFin = (TimePicker) d.findViewById(R.id.timePicker2);
        		
        		EditText campoTextoLugar = (EditText) d.findViewById(R.id.salaModuloAEditar);

        		
        		String nuevoLugar = campoTextoLugar.getText().toString();
        		moduloAEditar.setUbicacion( nuevoLugar);
        		
            	moduloAEditar.setDiaDeLaSemana(""+(spinnerDias.getSelectedItemPosition()+1));
            	Calendar inicio = moduloAEditar.obtenerInicio();
            	inicio.set(Calendar.HOUR_OF_DAY, tPInicio.getCurrentHour());
            	inicio.set(Calendar.MINUTE,tPInicio.getCurrentMinute());
            	
            	Calendar fin = moduloAEditar.obtenerFin();
            	fin.set(Calendar.HOUR_OF_DAY, tPFin.getCurrentHour());
            	fin.set(Calendar.MINUTE,tPFin.getCurrentMinute());
            	if(inicio.before(fin))
            	{
            		if(Controlador.puedoCambiarModulo(v.getContext(), spinnerDias.getSelectedItemPosition()+1, inicio, fin,moduloAEditar))
            		{
            			moduloAEditar.setInicio( inicio);
            			moduloAEditar.setFin(fin);
            			actualizarModulos();
            			d.dismiss();
            			//Cierra el diálogo
            		}
            		else{
            			Toast.makeText(v.getContext(), "Modulo ocupado", Toast.LENGTH_LONG).show();
            		}
            	}
            	else
            	{
            		Toast.makeText(v.getContext(), "Inicio debe ser antes de Fin", Toast.LENGTH_LONG).show();
            	}

            	}

		});
		
		boton_eliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	/*TODO: Controlador.eliminarModulo(id_modulo);*/
            	moduloAEditar.borrarModulo(v.getContext());
            	actualizarModulos();
            	d.dismiss();
        		
            	}

		});
		
		
		//ESTO ES PARA QUE LOS PICKER NO SE PASEN DE LISTOS
		tPInicio.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){

			public void onTimeChanged(TimePicker view, int hourOfDay,int minute) {
				// TODO Auto-generated method stub
				if(hourOfDay> 22)
					view.setCurrentHour(8);
				if(hourOfDay<8)
					view.setCurrentHour(22);
			}
			
			
		});
		tPFin.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){

			public void onTimeChanged(TimePicker view, int hourOfDay,
					int minute) {
				// TODO Auto-generated method stub
				if(hourOfDay> 22)
					view.setCurrentHour(8);
				if(hourOfDay<8)
					view.setCurrentHour(22);
			}
			
			
		});
		//ESTO ES PARA QUE LOS PICKER NO SE PASEN DE LISTOS
		
		return d;
		/*TODO: Crear un layout para la edición de módulo
		 * recoger todos los datos del módulo
		 * colocarlos y permitir su edición
		 * recopilar los cambios
		 * hacer los cambios a través del controlador
		 * actualizar la lista de módulos*/
		
		

	}
	
	private String stringDDS(int dia)
	{
		if (dia==2){
			return "Lunes";
		}
		else if (dia==3){
			return "Martes";
			
		}
		else if (dia==4){
			return "Mi�rcoles";
			
		}
		else if (dia==5){
			return "Jueves";
		}
		else if (dia==6){
			return "Viernes";
			
		}
		else if (dia ==7){
			return "S�bado";
			
		}
		else if (dia==1){
			return "Domingo";
			
		}
		else {
			return "DIA "+dia+" ";
		}
	}

public static int obtenerIdUnica(){
    	
    	/*Entrega una id única. Vital para crear dialogos únicos */
    	Calendar now = Calendar.getInstance();
		String year = String.valueOf(now.get(Calendar.YEAR));
		String month = String.valueOf(now.get(Calendar.MONTH)); // Note: zero based!
		String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH)); //2
		String hour = String.valueOf(now.get(Calendar.HOUR_OF_DAY));//2
		String minute = String.valueOf(now.get(Calendar.MINUTE));//2
		String second = String.valueOf(now.get(Calendar.SECOND));//2
		String millis = String.valueOf(now.get(Calendar.MILLISECOND));//3
		
		//year + month + day +
		//No puede quedar más largo, porque el rango de n
		String idunicaStr = hour + minute + second + millis;
		 int idunicaInt	=  Integer.valueOf(idunicaStr);
		return (idunicaInt);
    }

	protected void actualizarModulos() {
		// TODO Auto-generated method stub
		FrameLayout fl = new FrameLayout(this);  
	        fl.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	        setContentView(fl);
	        
	        dibujaLasLineas(fl);	
			fl.addView(new ViewEstatica(this));
			rellenarModulos();
	}

	 /*
	  * Lo siguiente no tiene que estar, para que mande el menú de la actividad
	  * que está como arriba de ella (awsome activity)
	  * 
	 @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.activity_actividad_configuracion, menu);
       return true;
   }
      
      */
}

class Lienzo extends View {

    public Lienzo(Context context) {
        super(context);
    }
    
    @SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
    	
    	/*
    	
    			l.addView(new TextView(this),new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*(minutos-enQueVoy)))));
				enQueVoy+= minutos-enQueVoy;
			}
				
			ViewModulo a = new ViewModulo(this);
			a.setHoraInicio(m.obtenerStringInicio());
			a.setHoraFin(m.obtenerStringFin());
			a.setSala(m.obtenerNombre());
			
			a.setNombre(curso.obtenerNombre().substring(0,3));
			String color = curso.obtenerColor();
			a.setColor(Color.rgb(Controlador.getRed(color), Controlador.getGreen(color), Controlador.getBlue(color)));
			l.addView(a,new LayoutParams(LayoutParams.FILL_PARENT,DisplaySupport.dipToPx(getApplicationContext(),(int)(factor*duracionEnMinutos))));
			enQueVoy+=duracionEnMinutos;
		}*/
    	//
    	int dato0 = DisplaySupport.dipToPx(this.getContext(),365);
    	int dato1 = DisplaySupport.dipToPx(this.getContext(),50);
    	int dato2 = DisplaySupport.dipToPx(this.getContext(),53);
    	int dato3 = DisplaySupport.dipToPx(this.getContext(),35);		
    	int dato4 = DisplaySupport.dipToPx(this.getContext(),22);//TAMA�O DE UNA HORA
    	
    	canvas.drawRGB(255,255,255);
        int ancho=canvas.getWidth();
        int alto=canvas.getHeight();
        
        Paint pincel1=new Paint();
        pincel1.setARGB(255,255,0,0);            
        canvas.drawLine(dato1, 0, dato1, dato0, pincel1);
        canvas.drawLine(dato2, 0, dato2, dato0, pincel1);
        pincel1.setARGB(255,0,0,0);
        int cantLineas=16;
        for(int fila=0;fila<cantLineas;fila++) {
        	canvas.drawLine(0, fila*dato4+dato3, ancho, fila*dato4+dato3, pincel1);
        }
    }}
