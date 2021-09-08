package com.tekimaster.springboot.apirest.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tekimaster.springboot.apirest.models.entity.Pedido;

public interface IPedidoDao extends JpaRepository<Pedido, Long> {
	
}
