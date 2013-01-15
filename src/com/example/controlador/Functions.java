package com.example.controlador;

public class Functions {

	public static int minutosDelDia = 60*24;
	
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

}
