package com.tekimaster.springboot.apirest.models.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tekimaster.springboot.apirest.util.Constante;

@Entity
@Table(name = "productos")
public class Producto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Producto() {
		this.detalleInsumos = new ArrayList<InsumoProducto>();
		this.detalleEquipamientos = new ArrayList<EquipamientoProducto>();
		this.detalleProductos = new ArrayList<ProductoProducto>();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Size(min = 4, max = 40)
	@Column(nullable = false)
	private String nombre;
	
	@Column
	private Integer tipo;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "producto_padre_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<ProductoProducto> detalleProductos = new ArrayList<ProductoProducto>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "producto_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<InsumoProducto> detalleInsumos;
	
	@NotNull
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "producto_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<EquipamientoProducto> detalleEquipamientos;
	
	@Column(name = "porcentaje_utilidad_detal")
	private Double porcentajeUtilidadDetal;
	
	@Column(name = "porcentaje_utilidad_mayor")
	private Double porcentajeUtilidadMayor;
	
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
	
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = new Date();
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

	public List<InsumoProducto> getDetalleInsumos() {
		return detalleInsumos;
	}

	public void setDetalleInsumos(List<InsumoProducto> detalleInsumos) {
		this.detalleInsumos = detalleInsumos;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Double getPorcentajeUtilidadDetal() {
		return porcentajeUtilidadDetal;
	}

	public void setPorcentajeUtilidadDetal(Double porcentajeUtilidadDetal) {
		this.porcentajeUtilidadDetal = porcentajeUtilidadDetal;
	}

	public Double getPorcentajeUtilidadMayor() {
		return porcentajeUtilidadMayor;
	}

	public void setPorcentajeUtilidadMayor(Double porcentajeUtilidadMayor) {
		this.porcentajeUtilidadMayor = porcentajeUtilidadMayor;
	}
	
	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public List<ProductoProducto> getDetalleProductos() {
		return detalleProductos;
	}

	public void setDetalleProductos(List<ProductoProducto> detalleProductos) {
		this.detalleProductos = detalleProductos;
	}

	public List<EquipamientoProducto> getDetalleEquipamientos() {
		return detalleEquipamientos;
	}

	public void setDetalleEquipamientos(List<EquipamientoProducto> detalleEquipamientos) {
		this.detalleEquipamientos = detalleEquipamientos;
	}

	/*TODO Falta calcular los costos directos*/
	
	public Double getCostoTotal() {
		Double costoTotal = Double.valueOf(0.00);
		costoTotal += getCostoProductos();
		costoTotal += getCostoInsumos();
		return costoTotal;
	}
	
	private Double getCostoInsumos() {
		Double costo = Double.valueOf(0.00);
		for (InsumoProducto insumoProducto : detalleInsumos) {
			costo += insumoProducto.getImporte();
		}
		return costo;
	}

	private Double getCostoProductos() {
		Double costo = Double.valueOf(0.00);
		for (ProductoProducto producto : detalleProductos) {
			costo += producto.getCantidad()*producto.getProducto().getCostoTotal();
		}
		return costo;
	}
	
	public Double getUtilidadDetal() {
		Double utilidad = Double.valueOf(0.00);
		utilidad += getUtilidadProductosDetal();
		utilidad += getUtilidadInsumosDetal();
		return utilidad;
	}

	private Double getUtilidadInsumosDetal() {
		return getCostoInsumos()*(getPorcentajeUtilidadDetal()/Constante.DOUBLE_100);
	}

	private Double getUtilidadProductosDetal() {
		Double utilidad = Double.valueOf(0.00);
		for (ProductoProducto producto : detalleProductos) {
			utilidad += producto.getCantidad()*producto.getProducto().getUtilidadDetal();
		}
		return utilidad;
	}
	
	public Double getUtilidadMayor() {
		Double utilidad = Double.valueOf(0.00);
		utilidad += getUtilidadProductosMayor();
		utilidad += getUtilidadInsumosMayor();
		return utilidad;
	}

	private Double getUtilidadInsumosMayor() {
		return getCostoInsumos()*(getPorcentajeUtilidadMayor()/Constante.DOUBLE_100);
	}

	private Double getUtilidadProductosMayor() {
		Double utilidad = Double.valueOf(0.00);
		for (ProductoProducto producto : detalleProductos) {
			utilidad += producto.getCantidad()*producto.getProducto().getUtilidadMayor();
		}
		return utilidad;
	}
	
	public Double getIvaDetal() {
		Double iva = Double.valueOf(0.00);
		iva += (getCostoTotal()+getUtilidadDetal())*(Constante.UNIDAD_TIPO_DOUBLE+(Constante.IVA/Constante.DOUBLE_100));
		return iva;
	}
	
	public Double getIvaMayor() {
		Double iva = Double.valueOf(0.00);
		iva += (getCostoTotal()+getUtilidadMayor())*(Constante.UNIDAD_TIPO_DOUBLE+(Constante.IVA/Constante.DOUBLE_100));
		return iva;
	}

	public BigDecimal getPrecioDetal() {
		BigDecimal precio = BigDecimal.valueOf(getCostoInsumos()
				*(Constante.UNIDAD_TIPO_DOUBLE+(getPorcentajeUtilidadDetal()/Constante.DOUBLE_100))
				*(Constante.UNIDAD_TIPO_DOUBLE+(Constante.IVA/Constante.DOUBLE_100))
				+getPrecioAlDetalProductos());
		return precio.setScale(-2, RoundingMode.HALF_UP);
	}

	private Double getPrecioAlDetalProductos() {
		Double precio = Double.valueOf(0.00);
		for (ProductoProducto producto : detalleProductos) {
			precio += producto.getProducto().getPrecioDetal().doubleValue()*producto.getCantidad();
		}
		return precio;
	}
	
	private Double getPrecioAlMayorProductos() {
		Double precio = Double.valueOf(0.00);
		for (ProductoProducto producto : detalleProductos) {
			precio += producto.getProducto().getPrecioMayor().doubleValue()*producto.getCantidad();
		}
		return precio;
	}

	public BigDecimal getPrecioMayor() {
		BigDecimal precio = BigDecimal.valueOf(getCostoInsumos()
				*(Constante.UNIDAD_TIPO_DOUBLE+(getPorcentajeUtilidadMayor()/Constante.DOUBLE_100))
				*(Constante.UNIDAD_TIPO_DOUBLE+(Constante.IVA/Constante.DOUBLE_100))
				+getPrecioAlMayorProductos());
		return precio.setScale(-2, RoundingMode.HALF_UP);
	}
	
	
}
