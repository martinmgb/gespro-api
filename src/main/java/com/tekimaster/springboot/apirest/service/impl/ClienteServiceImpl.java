package com.tekimaster.springboot.apirest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tekimaster.springboot.apirest.dao.IClienteDao;
import com.tekimaster.springboot.apirest.models.entity.Cliente;
import com.tekimaster.springboot.apirest.models.entity.Region;
import com.tekimaster.springboot.apirest.service.IClienteService;

@Service
public class ClienteServiceImpl implements IClienteService {
	
	@Autowired
	private IClienteDao clienteDao;

	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	@Transactional
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Transactional
	public Cliente save(Cliente cliente) {
		return clienteDao.save(cliente);
	}

	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

	@Override
	public List<Region> getRegiones() {
		return clienteDao.getRegiones();
	}

}
