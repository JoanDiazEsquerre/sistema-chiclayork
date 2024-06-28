package com.model.aldasa.util;

public enum EstadoAtencionMesaType {
	
	COBRADO("Cobrado"),ELIMINADO("Eliminado"), OCUPADO("Ocupado");
	
	private String name;
	
	private EstadoAtencionMesaType(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
}
