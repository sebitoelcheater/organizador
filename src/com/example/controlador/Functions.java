package com.example.controlador;

import java.util.Calendar;

public class Functions {

	public static int minutosDeUnDia = 60*24;
	
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

	public static int getDia(int minutos){
		return minutos/minutosDeUnDia;
	}
	
	public static int getHoraDelDiaEnMinutos(int minutos)
	{
		return minutos%minutosDeUnDia;
	}
	
	public static int getHoraDelDia(int minutos)
	{
		return getHoraDelDiaEnMinutos(minutos)/60;
	}
	
	public static int getMinutosDelDia(int minutos)
	{
		return getHoraDelDiaEnMinutos(minutos)%60;
	}

	public static int getAhoraEnMinutos()
	{
		int dia = traducirDias(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		int hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int minutos = Calendar.getInstance().get(Calendar.MINUTE);
		return dia*24*60+hora*60+minutos;
		
		
	}

	private static int traducirDias(int USDia)
	{
		int dia = USDia;
		dia-=2;
		
		if(dia==-1)
		{
			dia=6;
		}
		return dia;
		
	}
}
