package com.example.version2;


import twitter4j.auth.RequestToken;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlador.*;
import com.example.data.*;
import com.example.server.server;
public class ActividadFeedback extends Activity {
	 private ArrayList<String> cursos;
	 private ArrayList<Curso> cursosComentables;
	 Modulo aFB;
	 private String stringComentario;
	 private TweetHelper tweet_hlp;
	 private int TWITTER_AUTH=1;
	 private RequestToken mRequestToken = null;
	 
	
	 @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        tweet_hlp = new TweetHelper(this);
	     
       setContentView(R.layout.activity_actividad_feedback);
        setTitle("Organizador");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle b = getIntent().getExtras();
        aFB = new Modulo(this,getIntent().getStringExtra("ID"));
        
        //cargarDatos();
        TextView nombreCurso=(TextView) findViewById(R.id.nombreCusoAFeedbackear);  
        AdapterDatabase ad = new AdapterDatabase(this);
		
		Curso c =ad.getRecord(Curso.class, "Cursos", Long.parseLong(aFB.obtenerIdCurso()));
		
        nombreCurso.setText(c.obtenerNombre());
        
        //Spinner numeroDeNotas = (Spinner)findViewById(R.id.spinner1);
        //numeroDeNotas.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,cursos));
    }
	 

	 EditText comentario;
	 
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
    private void cargarDatos() {
    	
		cursosComentables = Controlador.obtenerCursosComentables(this); // ACA DEBERIA IR UN METODO LLAMADO OBTENERCURSOSCOMENTABLES... NO OLVIDAR PEDIRMELO!
    	cursos = new ArrayList<String>();
		for(Curso c : cursosComentables)
    		cursos.add(c.obtenerNombre());
    }
    
    public void enviarComentario(View view)
    {
    	//Hola, soy Seba. Capturar spinner
    	//Spinner mySpinner = (Spinner)findViewById(R.id.spinner1);
        AdapterDatabase ad = new AdapterDatabase(this);
		
   		Curso c =ad.getRecord(Curso.class, "Cursos", Long.parseLong(aFB.obtenerIdCurso()));

    	//Capturar comentario
    	comentario = (EditText) findViewById(R.id.editText1);
    	stringComentario = comentario.getText().toString();
    	if(!stringComentario.trim().equals("")){
    	//Crear objeto de la clase post y posteriormente ejecutar m�todo comentar
    	server objeto1 = new server();
    	
    	try{
    		
    		objeto1.comentar2(view,c,Controlador.ultimoModulo(this,c),stringComentario);
    		
    		//Gracias por el feedback
        	Toast toast = Toast.makeText(getApplicationContext(), "Gracias por tu feedback!", Toast.LENGTH_SHORT);
        	toast.show();
        	showDialog(obtenerIdUnica(), new Bundle());
        	
    	}catch(Exception e){ Toast.makeText(view.getContext(), "Error :Curso ya no existe o no tienes acceso a Internet", Toast.LENGTH_LONG).show();}	
    	}
    	
    	 
    	
    }
    
    protected Dialog onCreateDialog(int id, Bundle b)
	{
		
		final Dialog d = new Dialog(this);
		d.setContentView(R.layout.dialogo_twitter);
		d.setTitle("�Desea twittear su feedback?");
		Button botonSi = (Button)d.findViewById(R.id.buttonSi);
		Button botonNo = (Button)d.findViewById(R.id.buttonNo);
		botonSi.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
            	/*TODO: Controlador.eliminarModulo(id_modulo);*/
            	try {
					//validamos login de nuevo
            		    
            		validar_login();
					// obtenemos texto de componente de texto
					// enviamos tweet y obtenemos estado de envio
            		boolean twt_snd_status = tweet_hlp.Send_Tweet(formatearComentario(stringComentario));
					if (twt_snd_status)// OK enviado
					{ // cambiamos imagen de estad a twitter_OK
						Show_Toast("Tu tweet ha sido enviado");
						d.dismiss();
						finish();

					} else {
						// cambiamos imagen de estad a twitter_fial
						Show_Toast("Hubo un Error no se pudo enviar tu tweet.");
						
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					//System.out.println(e.getMessage());
					e.printStackTrace();
				}
        		//d.dismiss(); //Cierra el diálogo
        		//finish();
            	}
			
			 

		});
		
		botonNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	/*TODO: Controlador.eliminarModulo(id_modulo);*/
            	    
            	   
        			
            	    d.dismiss();
        			finish();
            	}

		});
		
		return d;
		
	}
    
    
   
	
    
    protected String formatearComentario(String stringComentario2) {
		// TODO Auto-generated method stub
        AdapterDatabase ad = new AdapterDatabase(this);
		
   		Curso c =ad.getRecord(Curso.class, "Cursos", Long.parseLong(aFB.obtenerIdCurso()));

		return "FeedBack de " +c.obtenerNombre()+", \""+stringComentario2 +"\""+"-Desde Organizador";
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
					Intent i = new Intent(ActividadFeedback.this, TwitterWebActivity.class);
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


}
