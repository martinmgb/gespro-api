package com.tekimaster.springboot.apirest.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tekimaster.springboot.apirest.models.entity.Producto;

public interface IProductoService {
	
	/*Services de Producto*/
	
	public List<Producto> findAll();
	
	public List<Producto> findByTipoOrderByNombreAsc(Integer tipo);
	
	public Page<Producto> findAll(Pageable pageable);
	
	public Page<Producto> findByNombre(String nombre, Pageable pageable);
	
	public Producto findById(Long id);
	
	public Producto save(Producto tipoInsumo);
	
	public void delete(Long id);
	
}
