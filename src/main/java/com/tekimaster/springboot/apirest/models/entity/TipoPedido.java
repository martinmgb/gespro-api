package com.tekimaster.springboot.apirest.models.entity;

public enum TipoPedido {
	DETAL(0, "DETAL"), 
	MAYOR(1,"MAYOR");
	
	private Integer id;
	private String nombre;
	
	private TipoPedido(Integer id, String nombre) {
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
