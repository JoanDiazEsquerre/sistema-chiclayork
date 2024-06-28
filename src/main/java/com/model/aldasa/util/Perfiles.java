package com.model.aldasa.util;

public enum Perfiles {
	
	ADMINISTRADOR("ADMINISTRADOR",1),
	MESERO("MESERO",2),
	CHEF("CHEF",3),
	CAJERO("CAJERO", 4);
	
	private String name;
	private int id;
	
	private Perfiles(String name,int id) {
		this.name=name;
		this.id=id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

}
