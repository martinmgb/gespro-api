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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tekimaster.springboot.apirest.util.Constante;

@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Pedido() {
		this.detalleInsumos = new ArrayList<InsumoPedido>();
		this.detalleProductos = new ArrayList<ProductoPedido>();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Cliente cliente;
	
	@Column(nullable = false)
	private TipoPedido tipoPedido;

	@Column(name = "fecha_entrega")
	@Temporal(TemporalType.DATE)
	private Date fechaEntrega;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estado_pedido_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private EstadoPedido estadoPedido;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "pedido_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<ProductoPedido> detalleProductos = new ArrayList<ProductoPedido>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "pedido_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<InsumoPedido> detalleInsumos;
	
	@Column(name = "porcentaje_descuento")
	private Double porcentajeDescuento;
	
	@Column(name = "costo")
	private Double costo;
	
	@Column(name = "utilidad")
	private Double utilidad;
	
	@Column(name = "iva")
	private Double iva;
	
	@Column(name = "precio")
	private Double precio;
	
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;
	
	@Column(name = "updated_at")
	@Temporal(TemporalType.DATE)
	private Date updatedAt;
	
	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
		this.estadoPedido = new EstadoPedido(Constante.POR_CONFIRMAR);
	}
	
	@PreUpdate
	public void PreUpdate() {
		this.updatedAt = new Date();
		this.costo = getCostoTotal();
		if(this.tipoPedido.getId().equals(TipoPedido.DETAL.getId())) {
			this.utilidad = getUtilidadDetal();
			this.iva = getIvaDetal();
			this.precio = getPrecioDetal().doubleValue();
		}else {
			this.utilidad = getUtilidadMayor();
			this.iva = getIvaMayor();
			this.precio = getPrecioMayor().doubleValue();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public List<InsumoPedido> getDetalleInsumos() {
		return detalleInsumos;
	}

	public void setDetalleInsumos(List<InsumoPedido> detalleInsumos) {
		this.detalleInsumos = detalleInsumos;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<ProductoPedido> getDetalleProductos() {
		return detalleProductos;
	}

	public void setDetalleProductos(List<ProductoPedido> detalleProductos) {
		this.detalleProductos = detalleProductos;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Double getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	public void setPorcentajeDescuento(Double porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}
	
	public TipoPedido getTipoPedido() {
		return tipoPedido;
	}

	public void setTipoPedido(TipoPedido tipoPedido) {
		this.tipoPedido = tipoPedido;
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public Double getUtilidad() {
		return utilidad;
	}

	public void setUtilidad(Double utilidad) {
		this.utilidad = utilidad;
	}

	public Double getIva() {
		return iva;
	}

	public void setIva(Double iva) {
		this.iva = iva;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Date getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public EstadoPedido getEstadoPedido() {
		return estadoPedido;
	}

	public void setEstadoPedido(EstadoPedido estadoPedido) {
		this.estadoPedido = estadoPedido;
	}

	public Double getCostoTotal() {
		Double costoTotal = Double.valueOf(0.00);
		costoTotal += getCostoProductos();
		costoTotal += getCostoInsumos();
		return costoTotal;
	}
	
	private Double getCostoInsumos() {
		Double costo = Double.valueOf(0.00);
		for (InsumoPedido insumoProducto : detalleInsumos) {
			costo += insumoProducto.getImporte();
		}
		return costo;
	}

	private Double getCostoProductos() {
		Double costo = Double.valueOf(0.00);
		for (ProductoPedido producto : detalleProductos) {
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
		return getCostoInsumos()*(Constante.PORCENTAJE_UTILIDAD_DETAL_DEFAULT/Constante.DOUBLE_100);
	}

	private Double getUtilidadProductosDetal() {
		Double utilidad = Double.valueOf(0.00);
		for (ProductoPedido producto : detalleProductos) {
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
		return getCostoInsumos()*(Constante.PORCENTAJE_UTILIDAD_MAYOR_DEFAULT/Constante.DOUBLE_100);
	}

	private Double getUtilidadProductosMayor() {
		Double utilidad = Double.valueOf(0.00);
		for (ProductoPedido producto : detalleProductos) {
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
		BigDecimal precio = BigDecimal.valueOf(
				(getCostoInsumos()+getUtilidadInsumosDetal()
				+getCostoProductos()+getUtilidadProductosDetal())
						*(Constante.UNIDAD_TIPO_DOUBLE+(Constante.IVA/Constante.DOUBLE_100)));
		if(this.porcentajeDescuento!=null) {
			precio = precio.multiply(BigDecimal.valueOf(Constante.UNIDAD_TIPO_DOUBLE-(this.porcentajeDescuento/Constante.DOUBLE_100)));
		}
		return precio.setScale(-2, RoundingMode.HALF_UP);
	}

	private Double getPrecioAlDetalProductos() {
		Double precio = Double.valueOf(0.00);
		for (ProductoPedido producto : detalleProductos) {
			precio += producto.getCantidad()*producto.getProducto().getPrecioDetal().doubleValue();
		}
		return precio;
	}
	
	private Double getPrecioAlMayorProductos() {
		Double precio = Double.valueOf(0.00);
		for (ProductoPedido producto : detalleProductos) {
			precio += producto.getCantidad()*producto.getProducto().getPrecioMayor().doubleValue();
		}
		return precio;
	}

	public BigDecimal getPrecioMayor() {
		BigDecimal precio = BigDecimal.valueOf(
				(getCostoInsumos()+getUtilidadInsumosMayor())
				*(Constante.UNIDAD_TIPO_DOUBLE+(Constante.IVA/Constante.DOUBLE_100))
				+getPrecioAlMayorProductos());
		if(getPorcentajeDescuento()!=null && getPorcentajeDescuento()>0) {
			precio = precio.multiply(BigDecimal.valueOf(Constante.UNIDAD_TIPO_DOUBLE-(getPorcentajeDescuento()/Constante.DOUBLE_100)));
		}
		return precio.setScale(-2, RoundingMode.HALF_UP);
	}
	
	
}
