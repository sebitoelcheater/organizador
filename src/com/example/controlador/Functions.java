package com.example.controlador;

import java.util.Calendar;

public class Functions {

	public static int minutosDeUnDia = 60 * 24;

	public static int getRed(String color) {
		return Integer.parseInt(color.substring(0, 3));
	}

	public static int getGreen(String color) {
		return Integer.parseInt(color.substring(4, 7));
	}

	public static int getBlue(String color) {
		return Integer.parseInt(color.substring(8, 11));
	}

	public static String agregarCeros(int n, int i) {
		// TODO Auto-generated method stub
		String numero = i + "";
		int longitud = n - numero.length();
		if (numero.length() < n) {
			for (int j = 0; j < longitud; ++j) {
				numero = "0" + numero;
			}
		}

		return numero;
	}

/**
 * Obtiene un int del 0 al 6, que representa lunes,martes,.....,domingo	
 * @param minutos
 * @return
 */
	public static int getDia(int minutos) {
		return minutos / minutosDeUnDia;
	}

	/**
	 * Obtiene la hora del dia en minutos(considerando HH:MM, considera tanto HH como MM), tomando como referencia las 00:00
	 * @param minutos
	 * @return
	 */
	public static int getHoraDelDiaEnMinutos(int minutos) {
		return minutos % minutosDeUnDia;
	}

	/**
	 * Si consideramos HH:MM, esto devuelve HH
	 * @param minutos
	 * @return
	 */
	public static int getHoraDelDia(int minutos) {
		return getHoraDelDiaEnMinutos(minutos) / 60;
	}

	/**
	 * Si consideramos HH:MM, esto devuelve MM
	 * @param minutos
	 * @return
	 */
	public static int getMinutosDelDia(int minutos) {
		return getHoraDelDiaEnMinutos(minutos) % 60;
	}

	/**
	 * Obtiene el momento actual considerando el dia de la semana, la hora y los minutos en sistema de referencia de los minutos, tomando como 0 el lunes a las 00:00
	 * @return
	 */
	public static int getAhoraEnMinutos() {
		int dia = traducirDias(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		int hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int minutos = Calendar.getInstance().get(Calendar.MINUTE);
		return dia * 24 * 60 + hora * 60 + minutos;

	}

	
	private static int traducirDias(int USDia) {
		int dia = USDia;
		dia -= 2;

		if (dia == -1) {
			dia = 6;
		}
		return dia;

	}

	/**
	 * Obtiene la fecha calendar en sistema de minutos semanales
	 * @param calendar
	 * @return
	 */
	public static int getEnMinutos(Calendar calendar) {
		int dia = traducirDias(calendar.get(Calendar.DAY_OF_WEEK));
		int hora = calendar.get(Calendar.HOUR_OF_DAY);
		int minutos = calendar.get(Calendar.MINUTE);
		return dia * 24 * 60 + hora * 60 + minutos;
	}

	/**
 * Retorna XX:YY
 * @param minutos
 * @return
 */
	public static String getHoraYMinutos(int minutos){
		return ""+getHoraDelDia(minutos)+":"+getMinutosDelDia(minutos);
	}
	
	public static int booleanToInt(boolean b) {
		if (b==true){
			return 1;
		}
		else if (b==false){
			return 0;
		}
		return (Integer) null;
	}
	
	public static boolean intToBoolean(int i) {
		if (i==1){
			return true;
		}
		else if (i==0){
			return false;
		}
		return false;
	}
	
	 public static int dateToMin(int dia,int hora,int minuto) {
		 int inicio = dia*Functions.minutosDeUnDia + hora*60 + minuto;
		 return inicio;
	 }
	 
	 public static String[] minToDate (int min){
		 int dia = getDia(min);
		 int hhmm = getHoraDelDia(min);
		 return new String[] {dayToDia(dia),""+hhmm};
	 }
	 
	 public static String dayToDia(int dia) {
			if (dia==0) {
				return "Lunes";
			} else if (dia==0) {
				return "Martes";

			} else if (dia==0) {
				return "Miercoles";

			} else if (dia==3) {
				return "Jueves";
			} else if (dia==4) {
				return "Viernes";

			} else if (dia==5) {
				return "Sabado";

			} else if (dia==6) {
				return "Domingo";

			} else {
				return "DIA " + dia + " ";
			}
		}
	 
	 public static Class<?> getClass(String nombreTabla)
	 {
		 if(nombreTabla.equals("Cursos"))
		 {
			 return Curso.class;
		 }else if(nombreTabla.equals("Horarios"))
		 {
			return Modulo.class; 
		 }else if(nombreTabla.equals("Profesores"))
		 {
			 return Profesor.class;
		 }else if(nombreTabla.equals("Comentarios"))
		 {
			 return Comentario.class;
		 }
		 
		 return null;
	 }

}
