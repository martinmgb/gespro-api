package com.tekimaster.springboot.apirest.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tekimaster.springboot.apirest.models.entity.Insumo;
import com.tekimaster.springboot.apirest.models.entity.TipoInsumo;

public interface IInsumoDao extends JpaRepository<Insumo, Long> {
	
	public List<Insumo> findByTipoInsumo(TipoInsumo tipoInsumo);
	
	public Page<Insumo> findByNombreContains(String nombre, Pageable pageable);
	
	public List<Insumo> findAllByOrderByNombreAsc();
	
}
