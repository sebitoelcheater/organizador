/*
 * package com.example.deprecated;
 * 
 * import java.util.ArrayList; import java.util.Calendar; import
 * java.util.HashMap; import java.util.List; import java.util.Map;
 * 
 * import com.example.controlador.Controlador; import
 * com.example.controlador.Curso; import com.example.controlador.Modulo;
 * 
 * import android.os.Bundle; import android.app.Activity; import
 * android.content.Context; import android.content.pm.ActivityInfo; import
 * android.graphics.Color; import android.util.DisplayMetrics; import
 * android.util.Log; import android.view.Display; import android.view.Menu;
 * import android.view.View; import android.view.ViewGroup; import
 * android.view.ViewGroup.LayoutParams; import android.widget.AbsoluteLayout;
 * import android.widget.Button; import android.widget.LinearLayout; import
 * android.widget.SimpleAdapter; import android.widget.TextView; import
 * android.view.WindowManager;
 * 
 * //ESTO TRABAJA EN PIXELES
 * 
 * public class ActividadHorarioSemanal extends Activity {
 * 
 * @Override public void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState);
 * setContentView(R.layout.activity_actividad_horario_semanal);
 * //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
 * 
 * rellenarModulos();
 * 
 * 
 * 
 * 
 * }
 * 
 * 
 * private void rellenarModulos() { // TODO Auto-generated method stub Display
 * display = ((WindowManager)
 * getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); float scale =
 * getApplicationContext().getResources().getDisplayMetrics().density;
 * DisplayMetrics metrics = getResources().getDisplayMetrics(); double
 * factorMaDPI = 4.0/(3.0); double factorDPIaPXL = 1;
 * //getWindowManager().getDefaultDisplay().getMetrics(metrics);
 * 
 * switch(metrics.densityDpi)
 * 
 * {
 * 
 * case DisplayMetrics.DENSITY_HIGH: //HDPI
 * 
 * factorDPIaPXL *= 3.0/2.0; break;
 * 
 * case DisplayMetrics.DENSITY_MEDIUM: //MDPI
 * 
 * 
 * factorDPIaPXL *= 4.0/4.0;
 * 
 * break;
 * 
 * 
 * 
 * case DisplayMetrics.DENSITY_LOW: //LDPI
 * 
 * 
 * factorDPIaPXL *= 3.0/4.0;
 * 
 * break;
 * 
 * 
 * }
 * 
 * double factor = factorMaDPI*factorDPIaPXL;
 * 
 * Log.d("FACTOR",""+factor); //LA RELACION ES 80dp corresponde a 60 minutos
 * 
 * int enQueVoy = 480; LinearLayout l = (LinearLayout)
 * findViewById(R.id.LinearLayoutLunes); ArrayList<Modulo> modulosDia =
 * Controlador.obtenerModulosSegunDia(this, 2);
 * 
 * for(Modulo m : modulosDia) { Curso curso = new
 * Curso(this,m.obtenerIdCurso());
 * 
 * Calendar inicio = m.obtenerInicio(); int minutos =
 * inicio.get(Calendar.HOUR_OF_DAY)*60+inicio.get(Calendar.MINUTE); Calendar fin
 * = m.obtenerFin(); int duracionEnMinutos =
 * fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE) - minutos;
 * if(enQueVoy<minutos) //Y si enQueVoy=>minutos(ESTO NO DEBERIA PASAR....) {
 * l.addView(new TextView(this),new
 * LayoutParams(LayoutParams.FILL_PARENT,(int)(factor*(minutos-enQueVoy))));
 * enQueVoy+= minutos-enQueVoy; }
 * 
 * ViewModulo a = new ViewModulo(this);
 * a.setHoraInicio(m.obtenerStringInicio()); a.setHoraFin(m.obtenerStringFin());
 * a.setSala(m.obtenerNombre());
 * 
 * a.setNombre(curso.obtenerNombre().substring(0,3)); String color =
 * curso.obtenerColor(); a.setColor(Color.rgb(Controlador.getRed(color),
 * Controlador.getGreen(color), Controlador.getBlue(color))); l.addView(a,new
 * LayoutParams(LayoutParams.FILL_PARENT,(int)(factor*duracionEnMinutos)));
 * enQueVoy+=duracionEnMinutos; }
 * 
 * //EDITANDO
 * 
 * enQueVoy = 480; l = (LinearLayout) findViewById(R.id.LinearLayoutMartes);
 * modulosDia = Controlador.obtenerModulosSegunDia(this, 3); for(Modulo m :
 * modulosDia) { Curso curso = new Curso(this,m.obtenerIdCurso());
 * 
 * Calendar inicio = m.obtenerInicio(); int minutos =
 * inicio.get(Calendar.HOUR_OF_DAY)*60+inicio.get(Calendar.MINUTE); Calendar fin
 * = m.obtenerFin(); int duracionEnMinutos =
 * fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE) - minutos;
 * if(enQueVoy<minutos) //Y si enQueVoy=>minutos(ESTO NO DEBERIA PASAR....) {
 * l.addView(new TextView(this),new
 * LayoutParams(LayoutParams.FILL_PARENT,(int)(factor*(minutos-enQueVoy))));
 * enQueVoy+= minutos-enQueVoy; }
 * 
 * ViewModulo a = new ViewModulo(this);
 * a.setHoraInicio(m.obtenerStringInicio()); a.setHoraFin(m.obtenerStringFin());
 * a.setSala(m.obtenerNombre());
 * 
 * a.setNombre(curso.obtenerNombre().substring(0,3)); String color =
 * curso.obtenerColor(); a.setColor(Color.rgb(Controlador.getRed(color),
 * Controlador.getGreen(color), Controlador.getBlue(color))); l.addView(a,new
 * LayoutParams(LayoutParams.FILL_PARENT,(int)(factor*duracionEnMinutos)));
 * enQueVoy+=duracionEnMinutos; }
 * 
 * //EDITANDO
 * 
 * 
 * 
 * 
 * enQueVoy = 480; l = (LinearLayout) findViewById(R.id.LinearLayoutMiercoles);
 * modulosDia = Controlador.obtenerModulosSegunDia(this, 4); for(Modulo m :
 * modulosDia) {
 * 
 * Curso curso = new Curso(this,m.obtenerIdCurso());
 * 
 * Calendar inicio = m.obtenerInicio(); int minutos =
 * inicio.get(Calendar.HOUR_OF_DAY)*60+inicio.get(Calendar.MINUTE); Calendar fin
 * = m.obtenerFin(); int duracionEnMinutos =
 * fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE) - minutos;
 * if(enQueVoy<minutos) //Y si enQueVoy=>minutos(ESTO NO DEBERIA PASAR....) {
 * l.addView(new TextView(this),new
 * LayoutParams(LayoutParams.FILL_PARENT,(int)(factor*(minutos-enQueVoy))));
 * enQueVoy+= minutos-enQueVoy; }
 * 
 * ViewModulo a = new ViewModulo(this);
 * a.setHoraInicio(m.obtenerStringInicio()); a.setHoraFin(m.obtenerStringFin());
 * a.setSala(m.obtenerNombre());
 * 
 * a.setNombre(curso.obtenerNombre().substring(0,3)); String color =
 * curso.obtenerColor(); a.setColor(Color.rgb(Controlador.getRed(color),
 * Controlador.getGreen(color), Controlador.getBlue(color))); l.addView(a,new
 * LayoutParams(LayoutParams.FILL_PARENT,(int)(factor*duracionEnMinutos)));
 * enQueVoy+=duracionEnMinutos; }
 * 
 * enQueVoy = 480; l = (LinearLayout) findViewById(R.id.LinearLayoutJueves);
 * modulosDia = Controlador.obtenerModulosSegunDia(this, 5); for(Modulo m :
 * modulosDia) { Curso curso = new Curso(this,m.obtenerIdCurso());
 * 
 * Calendar inicio = m.obtenerInicio(); int minutos =
 * inicio.get(Calendar.HOUR_OF_DAY)*60+inicio.get(Calendar.MINUTE); Calendar fin
 * = m.obtenerFin(); int duracionEnMinutos =
 * fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE) - minutos;
 * if(enQueVoy<minutos) //Y si enQueVoy=>minutos(ESTO NO DEBERIA PASAR....) {
 * l.addView(new TextView(this),new
 * LayoutParams(LayoutParams.FILL_PARENT,(int)(factor*(minutos-enQueVoy))));
 * enQueVoy+= minutos-enQueVoy; }
 * 
 * ViewModulo a = new ViewModulo(this);
 * a.setHoraInicio(m.obtenerStringInicio()); a.setHoraFin(m.obtenerStringFin());
 * a.setSala(m.obtenerNombre());
 * 
 * a.setNombre(curso.obtenerNombre().substring(0,3)); String color =
 * curso.obtenerColor(); a.setColor(Color.rgb(Controlador.getRed(color),
 * Controlador.getGreen(color), Controlador.getBlue(color))); l.addView(a,new
 * LayoutParams(LayoutParams.FILL_PARENT,(int)(factor*duracionEnMinutos)));
 * enQueVoy+=duracionEnMinutos; }
 * 
 * enQueVoy = 480; l = (LinearLayout) findViewById(R.id.LinearLayoutViernes);
 * modulosDia = Controlador.obtenerModulosSegunDia(this, 6); for(Modulo m :
 * modulosDia) {
 * 
 * Curso curso = new Curso(this,m.obtenerIdCurso());
 * 
 * Calendar inicio = m.obtenerInicio(); int minutos =
 * inicio.get(Calendar.HOUR_OF_DAY)*60+inicio.get(Calendar.MINUTE); Calendar fin
 * = m.obtenerFin(); int duracionEnMinutos =
 * fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE) - minutos;
 * if(enQueVoy<minutos) //Y si enQueVoy=>minutos(ESTO NO DEBERIA PASAR....) {
 * l.addView(new TextView(this),new
 * LayoutParams(LayoutParams.FILL_PARENT,(int)(factor*(minutos-enQueVoy))));
 * enQueVoy+= minutos-enQueVoy; }
 * 
 * ViewModulo a = new ViewModulo(this);
 * a.setHoraInicio(m.obtenerStringInicio()); a.setHoraFin(m.obtenerStringFin());
 * a.setSala(m.obtenerNombre());
 * 
 * a.setNombre(curso.obtenerNombre().substring(0,3)); String color =
 * curso.obtenerColor(); a.setColor(Color.rgb(Controlador.getRed(color),
 * Controlador.getGreen(color), Controlador.getBlue(color))); l.addView(a,new
 * LayoutParams(LayoutParams.FILL_PARENT,(int)(factor*duracionEnMinutos)));
 * enQueVoy+=duracionEnMinutos; }
 * 
 * enQueVoy = 480; l = (LinearLayout) findViewById(R.id.LinearLayoutSabado);
 * modulosDia = Controlador.obtenerModulosSegunDia(this, 7); for(Modulo m :
 * modulosDia) {
 * 
 * Curso curso = new Curso(this,m.obtenerIdCurso());
 * 
 * Calendar inicio = m.obtenerInicio(); int minutos =
 * inicio.get(Calendar.HOUR_OF_DAY)*60+inicio.get(Calendar.MINUTE); Calendar fin
 * = m.obtenerFin(); int duracionEnMinutos =
 * fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE) - minutos;
 * if(enQueVoy<minutos) //Y si enQueVoy=>minutos(ESTO NO DEBERIA PASAR....) {
 * l.addView(new TextView(this),new
 * LayoutParams(LayoutParams.FILL_PARENT,(int)(factor*(minutos-enQueVoy))));
 * enQueVoy+= minutos-enQueVoy; }
 * 
 * ViewModulo a = new ViewModulo(this);
 * a.setHoraInicio(m.obtenerStringInicio()); a.setHoraFin(m.obtenerStringFin());
 * a.setSala(m.obtenerNombre());
 * 
 * a.setNombre(curso.obtenerNombre().substring(0,3)); String color =
 * curso.obtenerColor(); a.setColor(Color.rgb(Controlador.getRed(color),
 * Controlador.getGreen(color), Controlador.getBlue(color))); l.addView(a,new
 * LayoutParams(LayoutParams.FILL_PARENT,(int)(factor*duracionEnMinutos)));
 * enQueVoy+=duracionEnMinutos; }
 * 
 * enQueVoy = 480; l = (LinearLayout) findViewById(R.id.LinearLayoutDomingo);
 * modulosDia = Controlador.obtenerModulosSegunDia(this, 1); for(Modulo m :
 * modulosDia) {
 * 
 * Curso curso = new Curso(this,m.obtenerIdCurso());
 * 
 * Calendar inicio = m.obtenerInicio(); int minutos =
 * inicio.get(Calendar.HOUR_OF_DAY)*60+inicio.get(Calendar.MINUTE); Calendar fin
 * = m.obtenerFin(); int duracionEnMinutos =
 * fin.get(Calendar.HOUR_OF_DAY)*60+fin.get(Calendar.MINUTE) - minutos;
 * if(enQueVoy<minutos) //Y si enQueVoy=>minutos(ESTO NO DEBERIA PASAR....) {
 * l.addView(new TextView(this),new
 * LayoutParams(LayoutParams.FILL_PARENT,(int)(factor*(minutos-enQueVoy))));
 * enQueVoy+= minutos-enQueVoy; }
 * 
 * ViewModulo a = new ViewModulo(this);
 * a.setHoraInicio(m.obtenerStringInicio()); a.setHoraFin(m.obtenerStringFin());
 * a.setSala(m.obtenerNombre());
 * 
 * a.setNombre(curso.obtenerNombre().substring(0,3)); String color =
 * curso.obtenerColor(); a.setColor(Color.rgb(Controlador.getRed(color),
 * Controlador.getGreen(color), Controlador.getBlue(color))); l.addView(a,new
 * LayoutParams(LayoutParams.FILL_PARENT,(int)(factor*duracionEnMinutos)));
 * enQueVoy+=duracionEnMinutos; }
 * 
 * }
 * 
 * @Override public boolean onCreateOptionsMenu(Menu menu) {
 * getMenuInflater().inflate(R.menu.activity_actividad_horario_semanal, menu);
 * return true; }
 * 
 * }
 */