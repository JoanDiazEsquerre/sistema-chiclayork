package com.model.aldasa.util;

public enum TipoPlatoType {
	
	ENTRADA("Entrada"),MENU("Menu"), CARTA("Carta"), BEBIDA("Bebida"), PLATO_BANDERA("Plato Bandera"), GUARNICIONES("Guarniciones"), POSTRES("Postres"), OTROS("Otros");
	
	private String nombre;
	
	private TipoPlatoType(String nombre) {
		this.nombre=nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
}
