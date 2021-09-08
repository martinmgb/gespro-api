package com.tekimaster.springboot.apirest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tekimaster.springboot.apirest.dao.ITipoInsumoDao;
import com.tekimaster.springboot.apirest.dao.IUnidadMedidaDao;
import com.tekimaster.springboot.apirest.models.entity.TipoInsumo;
import com.tekimaster.springboot.apirest.models.entity.UnidadMedida;
import com.tekimaster.springboot.apirest.service.IParametroService;

@Service
public class ParametroServiceImpl implements IParametroService {
	
	@Autowired
	private ITipoInsumoDao tipoInsumoDao;
	@Autowired
	private IUnidadMedidaDao unidadMedidaDao;
	
	/*Services de TipoInsumo*/
	
	@Transactional(readOnly = true)
	public List<TipoInsumo> findAllTiposInsumo() {
		return tipoInsumoDao.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<TipoInsumo> findAllTiposInsumo(Pageable pageable) {
		return tipoInsumoDao.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public TipoInsumo findByIdTipoInsumo(Long id) {
		return tipoInsumoDao.findById(id).orElse(null);
	}
	
	@Transactional
	public TipoInsumo saveTipoInsumo(TipoInsumo tipoInsumo) {
		return tipoInsumoDao.save(tipoInsumo);
	}
	
	@Transactional
	public void deleteTipoInsumo(Long id) {
		tipoInsumoDao.deleteById(id);
	}
	
	/*Services de UnidadMedida*/

	@Transactional(readOnly = true)
	public List<UnidadMedida> findAllUnidadesMedida() {
		return unidadMedidaDao.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<UnidadMedida> findAllUnidadesMedida(Pageable pageable) {
		return unidadMedidaDao.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public UnidadMedida findByIdUnidadMedida(Long id) {
		return unidadMedidaDao.findById(id).orElse(null);
	}

	@Transactional
	public UnidadMedida saveUnidadMedida(UnidadMedida unidadMedida) {
		return unidadMedidaDao.save(unidadMedida);
	}

	@Transactional
	public void deleteUnidadMedida(Long id) {
		unidadMedidaDao.deleteById(id);
	}

}
