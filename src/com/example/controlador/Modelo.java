package com.example.controlador;


public abstract class Modelo {//EX ISeteable

	protected String nombreTabla;
	
	public abstract void setData(String id,Object[] params);
	
}
