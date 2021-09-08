package com.tekimaster.springboot.apirest.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tekimaster.springboot.apirest.models.entity.Insumo;
import com.tekimaster.springboot.apirest.models.entity.TipoInsumo;

public interface IInsumoService {
	
	/*Services de Insumo*/
	
	public List<Insumo> findAll();
	
	public Page<Insumo> findAll(Pageable pageable);
	
	public List<Insumo> findAllByOrderByNombreAsc();
	
	public Page<Insumo> findByNombre(String nombre, Pageable pageable);
	
	public List<Insumo> findByTipoInsumo(TipoInsumo tipoInsumo);
	
	public Insumo findById(Long id);
	
	public Insumo save(Insumo insumo);
	
	public void delete(Long id);
}
