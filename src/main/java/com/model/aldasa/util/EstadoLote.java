package com.model.aldasa.util;

public enum EstadoLote {
	
	DISPONIBLE("Disponible"),SEPARADO("Separado"), VENDIDO("Vendido"),INACTIVO("Inactivo");
	
	private String name;
	
	private EstadoLote(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
}
