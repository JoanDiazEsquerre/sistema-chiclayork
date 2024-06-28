package com.model.aldasa.util;

public enum EstadoProspeccion {

	EN_SEGUIMIENTO("En seguimiento"),TERMINADO("Terminado");
	
	private String name;
	
	private EstadoProspeccion(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
}
