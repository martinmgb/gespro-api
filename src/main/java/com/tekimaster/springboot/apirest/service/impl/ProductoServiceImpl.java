package com.tekimaster.springboot.apirest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tekimaster.springboot.apirest.dao.IProductoDao;
import com.tekimaster.springboot.apirest.models.entity.Producto;
import com.tekimaster.springboot.apirest.service.IProductoService;

@Service
public class ProductoServiceImpl implements IProductoService {
	
	@Autowired
	private IProductoDao productoDao;

	@Transactional(readOnly = true)
	public List<Producto> findAll() {
		return productoDao.findAll();
	}
	
	@Transactional(readOnly = true)
	public List<Producto> findByTipoOrderByNombreAsc(Integer tipo) {
		return productoDao.findByTipoOrderByNombreAsc(tipo);
	}

	@Transactional(readOnly = true)
	public Page<Producto> findAll(Pageable pageable) {
		return productoDao.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public Page<Producto> findByNombre(String nombre, Pageable pageable) {
		return productoDao.findByNombreContains(nombre, pageable);
	}
	
	@Transactional(readOnly = true)
	public Producto findById(Long id) {
		return productoDao.findById(id).orElse(null);
	}

	@Transactional
	public Producto save(Producto producto) {
		return productoDao.save(producto);
	}

	@Transactional
	public void delete(Long id) {
		productoDao.deleteById(id);
	}

}
