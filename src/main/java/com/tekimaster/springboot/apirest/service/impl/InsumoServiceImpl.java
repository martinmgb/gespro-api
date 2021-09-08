package com.tekimaster.springboot.apirest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tekimaster.springboot.apirest.dao.IInsumoDao;
import com.tekimaster.springboot.apirest.models.entity.Insumo;
import com.tekimaster.springboot.apirest.models.entity.TipoInsumo;
import com.tekimaster.springboot.apirest.service.IInsumoService;

@Service
public class InsumoServiceImpl implements IInsumoService {
	
	@Autowired
	private IInsumoDao insumoDao;
	
	/*Services de Insumo*/
	
	@Transactional(readOnly = true)
	public List<Insumo> findAll() {
		return insumoDao.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<Insumo> findAll(Pageable pageable) {
		return insumoDao.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public List<Insumo> findAllByOrderByNombreAsc() {
		return insumoDao.findAllByOrderByNombreAsc();
	}
	
	@Transactional(readOnly = true)
	public Page<Insumo> findByNombre(String nombre, Pageable pageable) {
		return insumoDao.findByNombreContains(nombre, pageable);
	}

	@Transactional(readOnly = true)
	public Insumo findById(Long id) {
		return insumoDao.findById(id).orElse(null);
	}
	
	@Override
	public List<Insumo> findByTipoInsumo(TipoInsumo tipoInsumo) {
		return insumoDao.findByTipoInsumo(tipoInsumo);
	}
	
	@Transactional
	public Insumo save(Insumo insumo) {
		return insumoDao.save(insumo);
	}
	
	@Transactional
	public void delete(Long id) {
		insumoDao.deleteById(id);
	}
	
}
