package com.tekimaster.springboot.apirest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tekimaster.springboot.apirest.models.entity.EstadoPedido;
import com.tekimaster.springboot.apirest.models.entity.Pedido;
import com.tekimaster.springboot.apirest.service.IPedidoService;
import com.tekimaster.springboot.apirest.util.Constante;
import com.tekimaster.springboot.apirest.util.OperacionesUtil;

@CrossOrigin({"*"})
@RestController
@RequestMapping("/api")
public class PedidoRestController {
	
	private static final Logger log = LoggerFactory.getLogger(PedidoRestController.class);

	@Autowired
	private IPedidoService pedidoService;

	@GetMapping("/pedidos")
	public List<Pedido> index(){
		return pedidoService.findAll();
	}
	
	@GetMapping("/pedidos/page/{page}")
	public Page<Pedido> index(@PathVariable Integer page){
		return pedidoService.findAll(PageRequest.of(page, 20, Sort.by("id").descending()));
	}
	
	@GetMapping("/pedidos/{id}")
	public ResponseEntity<?> show(@PathVariable Long id){
		Pedido pedido = null;
		Map<String, String> response = new HashMap<String, String>();
		try {
			pedido = pedidoService.findById(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al consultar pedido "+id+" en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, String>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(pedido==null) {
			response.put("mensaje", "El pedido "+id+" no existe en base de datos");
			return new ResponseEntity<Map<String, String>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Pedido>(pedidoService.findById(id), HttpStatus.OK);
	}
	
	@PostMapping("/pedidos")
	public ResponseEntity<?> create(@Valid @RequestBody Pedido pedido, BindingResult result){
		Pedido pedidoNew = null;
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(result.hasErrors()) {
			
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '"+ err.getField()+"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			pedidoNew = pedidoService.save(pedido);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar pedido en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Pedido ha sido creado con éxito!");
		response.put("data", pedidoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/pedidos/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Pedido pedido, BindingResult result, @PathVariable Long id){
		Pedido pedidoActual = pedidoService.findById(id);
		Pedido pedidoUpdated = null;
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '"+ err.getField()+"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(pedido==null) {
			response.put("mensaje", "No se pudo editar, pedido "+id+" no existe en base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			pedidoActual.setCliente(pedido.getCliente());
			pedidoActual.setDetalleProductos(pedido.getDetalleProductos());
			pedidoActual.setDetalleInsumos(pedido.getDetalleInsumos());
			pedidoActual.setPorcentajeDescuento(pedido.getPorcentajeDescuento());
			pedidoActual.setTipoPedido(pedido.getTipoPedido());
			pedidoActual.setFechaEntrega(pedido.getFechaEntrega());
			pedidoUpdated =  pedidoService.save(pedidoActual);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar pedido en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Pedido actualizado con éxito!");
		response.put("data", pedidoUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/pedidos/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			pedidoService.delete(id);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar pedido en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Pedido eliminado con éxito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@PutMapping("/pedidos/{accion}/{id}")
	public ResponseEntity<?> confirmar(@PathVariable String accion, @PathVariable Long id){
		Pedido pedidoActual = pedidoService.findById(id);
		Pedido pedidoUpdated = null;
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			EstadoPedido estadoPedido = getEstadoByAccion(accion);
			if(estadoPedido!=null) {
				pedidoActual.setEstadoPedido(estadoPedido);
				pedidoUpdated =  pedidoService.save(pedidoActual);
			}else {
				response.put("mensaje", "Acción no reconocida!");
				response.put("error", "Acción no reconocida!");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al modificar pedido en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Pedido ha sido modificado con éxito!");
		response.put("data", pedidoUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	private EstadoPedido getEstadoByAccion(String accion) {
		switch (accion) {
			case Constante.ACCION_CONFIRMAR: 
				return new EstadoPedido(Constante.PENDIENTE);
			
			case Constante.ACCION_REALIZAR: 
				return new EstadoPedido(Constante.REALIZADO);
			
			case Constante.ACCION_FINALIZAR: 
				return new EstadoPedido(Constante.FINALIZADO);
			
			case Constante.ACCION_CANCELAR: 
				return new EstadoPedido(Constante.CANCELADO);
			
			default:
				return null;
			
		}
	}
	
}
