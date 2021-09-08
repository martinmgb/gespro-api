package com.tekimaster.springboot.apirest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tekimaster.springboot.apirest.dao.IPedidoDao;
import com.tekimaster.springboot.apirest.models.entity.Pedido;
import com.tekimaster.springboot.apirest.service.IPedidoService;

@Service
public class PedidoServiceImpl implements IPedidoService {
	
	@Autowired
	private IPedidoDao pedidoDao;

	@Transactional(readOnly = true)
	public List<Pedido> findAll() {
		return pedidoDao.findAll();
	}

	@Transactional(readOnly = true)
	public Page<Pedido> findAll(Pageable pageable) {
		return pedidoDao.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public Pedido findById(Long id) {
		return pedidoDao.findById(id).orElse(null);
	}

	@Transactional
	public Pedido save(Pedido pedido) {
		return pedidoDao.save(pedido);
	}

	@Transactional
	public void delete(Long id) {
		pedidoDao.deleteById(id);
	}

}
