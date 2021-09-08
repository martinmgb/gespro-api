package com.tekimaster.springboot.apirest.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tekimaster.springboot.apirest.models.entity.UnidadMedida;

public interface IUnidadMedidaDao extends JpaRepository<UnidadMedida, Long> {

}
