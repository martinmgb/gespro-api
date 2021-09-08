package com.tekimaster.springboot.apirest.models.entity;

public enum TipoProducto {
	BASE(0, "BASE"), 
	COMBINADO(1,"COMBINADO");
	
	private Integer id;
	private String nombre;
	
	private TipoProducto(Integer id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}
