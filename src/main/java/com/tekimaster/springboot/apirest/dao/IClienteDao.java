package com.tekimaster.springboot.apirest.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tekimaster.springboot.apirest.models.entity.Cliente;
import com.tekimaster.springboot.apirest.models.entity.Region;

public interface IClienteDao extends JpaRepository<Cliente, Long> {
	
	@Query("from Region")
	public List<Region> getRegiones();
}
