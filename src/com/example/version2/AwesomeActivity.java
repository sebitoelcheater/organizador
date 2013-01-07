package com.example.version2;

import java.util.Calendar;

import com.example.controlador.Controlador;
import com.example.controlador.Curso;
import com.example.data.AdapterDatabase;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Adil Soomro
 *
 */
@SuppressWarnings("deprecation")
public class AwesomeActivity extends TabActivity implements TabHost.OnTabChangeListener {
	TabHost tabHost;
	/** Called when the activity is first created. */
	private PendingIntent pendingIntent;
	int tab = 0;
	public void onCreate(Bundle savedInstanceState) { // SEBA... NO FUNCIONAN LAS NOTIFICACIONES
		super.onCreate(savedInstanceState);
		AdapterDatabase.crearTablas();
		setContentView(R.layout.main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		tabHost = getTabHost();
		setTabs();
		tabHost.setOnTabChangedListener(this);
		
		
		activarNotificaciones();
		if(Controlador.obtenerCursos(this).size()==0)
		{
			Toast.makeText(this, "¿No sabes que hacer?", Toast.LENGTH_LONG).show();
			Toast.makeText(this, "Presiona el botón Menú!", Toast.LENGTH_LONG).show();
			Toast.makeText(this, "Y visita nuestro sitio web", Toast.LENGTH_LONG).show();
		}
	}
	private void setTabs() 
	{
		addTab("Hoy", R.drawable.tab_hoy, ActividadHorario.class,"HORARIO");
		addTab("Horario", R.drawable.tab_horario, ActividadHorarioSemanal2.class,"SEMANAL");
		addTab("Feedback", R.drawable.tab_feedback, ActividadFeedBackear.class,"FEEDBACK");
		addTab("Ajustes", R.drawable.tab_preferencias, ActividadRamos.class,"CONFIG");
	}
	private void addTab(String labelId, int drawableId, Class<?> c,String nombreTab)
	{
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec(nombreTab);	
		
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);		
		spec.setIndicator(tabIndicator);
		spec.setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(spec);
	
	}
	
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		if(tabId.equals("FEEDBACK") && Controlador.obtenerLosFeedBackeables(this,Calendar.getInstance()).size()==0)
		{
			tabHost.setCurrentTab(tab);
			Toast.makeText(this, "No hay cursos para FeedBackear ahora", Toast.LENGTH_LONG).show();
		}
		else
		{
			tab = tabHost.getCurrentTab();
		}
	}
	
	   /* private static final int NEW_ITEM = Menu.FIRST;
    private static final int RESET_ALL = Menu.FIRST + 1;
    private static final int SHARE = Menu.FIRST + 2;
    private static final int PREFERENCES = Menu.FIRST + 3;*/
    //private static final int ABOUT = Menu.FIRST + 4;
    private static final int ABOUT = Menu.FIRST;
    
    //NUEVO
    
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
        menu.add(Menu.NONE, ABOUT, 5, R.string.menu_about).setIcon(
                android.R.drawable.ic_menu_help);
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
                                    .parse(getString(R.string.URL));
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
    
    private void activarNotificaciones() {
		// TODO Auto-generated method stub
    	int comprobacionIntervaloSegundos = 30;//pensar esto mejor
    	
		   Intent myIntent = new Intent(AwesomeActivity.this, alarmChecker.class);
		   pendingIntent = PendingIntent.getService(AwesomeActivity.this, 0, myIntent, 0);

		   AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

		   Calendar calendar = Calendar.getInstance();
		   calendar.setTimeInMillis(System.currentTimeMillis());
		   calendar.add(Calendar.SECOND, 10);
		   alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), comprobacionIntervaloSegundos * 1000, pendingIntent);
 
	
		   
		    
		   
	}
}