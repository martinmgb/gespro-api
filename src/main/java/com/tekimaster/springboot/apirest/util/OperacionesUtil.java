package com.tekimaster.springboot.apirest.util;

import com.tekimaster.springboot.apirest.models.entity.Pedido;
import com.tekimaster.springboot.apirest.models.entity.Producto;
import com.tekimaster.springboot.apirest.models.entity.TipoPedido;
import com.tekimaster.springboot.apirest.models.entity.TipoProducto;

public class OperacionesUtil {

	public static Integer getTipoProducto(Producto producto) {
		if(producto.getDetalleProductos().size()>0) {
			return TipoProducto.COMBINADO.getId();
		}
		return TipoProducto.BASE.getId();
	}

	public static TipoPedido getTipoPedido(Pedido pedido) {
		if(pedido.getTipoPedido().getId().equals(TipoPedido.DETAL.getId())) {
			return TipoPedido.DETAL;
		}
		return TipoPedido.MAYOR;
	}
	 
}
