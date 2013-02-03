package com.example.data;

import java.util.*;

public class Tabla {

	public String nombre;
	public String nombreClaseRepresentante;
	/**
	 * En campos[0] esta el nombre de la variable y en campos[1] esta el tipo y
	 * en [2] el resto de palabras
	 */
	public ArrayList<String[]> campos = new ArrayList<String[]>();
	private String[] keys;

	public int numCampos = 0;

	/**
	 * Nota: En este caso (iidC integer primary key autoincrement, idC integer,
	 * iidP integer, title VARCHAR not null, comentable integer, color VARCHAR
	 * not null);"); El tipo del primer id es integer primary key autoincrement
	 * 
	 * @param nombre
	 * @param campos
	 * @param tipoCampos
	 * @throws Exception
	 */

	public Tabla(String nombre,String claseRepresentante, String sqliteCode) {
		this.nombre = nombre;
		this.nombreClaseRepresentante=claseRepresentante;
		String[] codeSplit = sqliteCode.split(",");
		setKeys(new String[codeSplit.length]);
		for (int i = 0; i < codeSplit.length; i++) {
			String[] txt = codeSplit[i].trim().split(" ");
			String nombreCampo = txt[0];
			getKeys()[i] = nombreCampo;
			String tipoCampo = txt[1];
			String resto = "";

			if (txt.length > 2) {
				for (int k = 2; k < txt.length; k++) {
					resto += txt[k] + " ";
				}

				resto = resto.trim();
			}

			this.campos.add(new String[] { nombreCampo, tipoCampo, resto });
			this.numCampos++;
		}

	}

	public String getNombreLlave() {
		return this.campos.get(0)[0];
	}

	public String getNombreLlaveMaster() {
		return this.campos.get(1)[0];
	}

	public String[] getKeys() {
		return keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}
}
