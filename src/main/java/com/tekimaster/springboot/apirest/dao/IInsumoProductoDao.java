package com.tekimaster.springboot.apirest.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tekimaster.springboot.apirest.models.entity.InsumoProducto;

public interface IInsumoProductoDao extends JpaRepository<InsumoProducto, Long> {
	
}
