package com.tekimaster.springboot.apirest.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tekimaster.springboot.apirest.models.entity.Producto;

public interface IProductoDao extends JpaRepository<Producto, Long> {
	
	public List<Producto> findByTipoOrderByNombreAsc(Integer tipo);
	
	public Page<Producto> findByNombreContains(String nombre, Pageable pageable);
}
