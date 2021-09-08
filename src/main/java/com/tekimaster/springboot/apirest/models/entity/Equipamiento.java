package com.tekimaster.springboot.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tekimaster.springboot.apirest.util.Constante;

@Entity
@Table(name = "equipamientos")
public class Equipamiento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Size(min = 4, max = 40)
	@Column(nullable = false)
	private String nombre;
	
	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_equipamiento_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private TipoEquipamiento tipoEquipamiento;
	
	@NotNull
	@Column(nullable = false)
	private Double costo;
	
	@NotNull
	@Column(nullable = false)
	private Double vidaUtil;
	
	@NotNull
	@Column(nullable = false)
	private Double stock;
	
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;
	
	@Column(name = "updated_at")
	@Temporal(TemporalType.DATE)
	private Date updatedAt;
	
	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public TipoEquipamiento getTipoEquipamiento() {
		return tipoEquipamiento;
	}

	public void setTipoEquipamiento(TipoEquipamiento tipoEquipamiento) {
		this.tipoEquipamiento = tipoEquipamiento;
	}

	public Double getVidaUtil() {
		return vidaUtil;
	}

	public void setVidaUtil(Double vidaUtil) {
		this.vidaUtil = vidaUtil;
	}

	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}
	
	public Double getDepreciacionAnual() {
		return this.costo/this.vidaUtil;
	}
	
	public Double getDepreciacionMensual() {
		return this.getDepreciacionAnual()/Constante.DOCE_MESES;
	}

	public Double getDepreciacionDiaria() {
		return this.getDepreciacionMensual()/Constante.TREINTA_DIAS;
	}
	
	public Double getDepreciacionHora() {
		return this.getDepreciacionDiaria()/Constante.SIETE_HORAS;
	}
	
}
