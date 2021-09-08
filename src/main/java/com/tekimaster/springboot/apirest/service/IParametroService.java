package com.tekimaster.springboot.apirest.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tekimaster.springboot.apirest.models.entity.TipoInsumo;
import com.tekimaster.springboot.apirest.models.entity.UnidadMedida;

public interface IParametroService {
	
	/*Services de TipoInsumo*/
	
	public List<TipoInsumo> findAllTiposInsumo();
	
	public Page<TipoInsumo> findAllTiposInsumo(Pageable pageable);
	
	public TipoInsumo findByIdTipoInsumo(Long id);
	
	public TipoInsumo saveTipoInsumo(TipoInsumo tipoInsumo);
	
	public void deleteTipoInsumo(Long id);
	
	/*Services de UnidadMedida*/
	
	public List<UnidadMedida> findAllUnidadesMedida();
	
	public Page<UnidadMedida> findAllUnidadesMedida(Pageable pageable);
	
	public UnidadMedida findByIdUnidadMedida(Long id);
	
	public UnidadMedida saveUnidadMedida(UnidadMedida unidadMedida);
	
	public void deleteUnidadMedida(Long id);
}
