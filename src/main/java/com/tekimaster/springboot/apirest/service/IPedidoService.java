package com.tekimaster.springboot.apirest.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tekimaster.springboot.apirest.models.entity.Pedido;

public interface IPedidoService {
	
	/*Services de Pedido*/
	
	public List<Pedido> findAll();
	
	public Page<Pedido> findAll(Pageable pageable);
	
	public Pedido findById(Long id);
	
	public Pedido save(Pedido pedido);
	
	public void delete(Long id);
	
}
