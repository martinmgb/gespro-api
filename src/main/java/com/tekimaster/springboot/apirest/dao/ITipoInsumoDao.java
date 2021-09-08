package com.tekimaster.springboot.apirest.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tekimaster.springboot.apirest.models.entity.TipoInsumo;

public interface ITipoInsumoDao extends JpaRepository<TipoInsumo, Long> {
	
}
