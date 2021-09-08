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

import com.tekimaster.springboot.apirest.models.entity.Insumo;
import com.tekimaster.springboot.apirest.models.entity.Producto;
import com.tekimaster.springboot.apirest.service.IProductoService;
import com.tekimaster.springboot.apirest.util.OperacionesUtil;

@CrossOrigin({"*"})
@RestController
@RequestMapping("/api")
public class ProductoRestController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductoRestController.class);

	@Autowired
	private IProductoService productoService;

	@GetMapping("/productos")
	public List<Producto> index(){
		return productoService.findAll();
	}
	
	@GetMapping("/productos/tipo/{tipo}")
	public List<Producto> productosByTipo(@PathVariable Integer tipo){
		return productoService.findByTipoOrderByNombreAsc(tipo);
	}
	
	@GetMapping("/productos/page/{page}")
	public Page<Producto> index(@PathVariable Integer page){
		return productoService.findAll(PageRequest.of(page, 20)); 
	}
	
	@GetMapping("/productos/nombre/{nombre}/page/{page}")
	public Page<Producto> index(@PathVariable String nombre, @PathVariable Integer page){
		return productoService.findByNombre(nombre, PageRequest.of(page, 20)); 
	}
	
	@GetMapping("/productos/{id}")
	public ResponseEntity<?> show(@PathVariable Long id){
		Producto producto = null;
		Map<String, String> response = new HashMap<String, String>();
		try {
			producto = productoService.findById(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al consultar producto "+id+" en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, String>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(producto==null) {
			response.put("mensaje", "El producto "+id+" no existe en base de datos");
			return new ResponseEntity<Map<String, String>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Producto>(productoService.findById(id), HttpStatus.OK);
	}
	
	@PostMapping("/productos")
	public ResponseEntity<?> create(@Valid @RequestBody Producto producto, BindingResult result){
		Producto productoNew = null;
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
			producto.setTipo(OperacionesUtil.getTipoProducto(producto));
			productoNew = productoService.save(producto);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar producto en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Producto ha sido creado con éxito!");
		response.put("data", productoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/productos/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Producto producto, BindingResult result, @PathVariable Long id){
		Producto productoActual = productoService.findById(id);
		Producto productoUpdated = null;
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '"+ err.getField()+"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(producto==null) {
			response.put("mensaje", "No se pudo editar, producto "+id+" no existe en base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			productoActual.setNombre(producto.getNombre());
			productoActual.setPorcentajeUtilidadDetal(producto.getPorcentajeUtilidadDetal());
			productoActual.setPorcentajeUtilidadMayor(producto.getPorcentajeUtilidadMayor());
			productoActual.setDetalleInsumos(producto.getDetalleInsumos());
			productoActual.setDetalleProductos(producto.getDetalleProductos());
			productoActual.setTipo(OperacionesUtil.getTipoProducto(producto));
			productoUpdated =  productoService.save(productoActual);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar producto en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Producto actualizado con éxito!");
		response.put("data", productoUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/productos/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			productoService.delete(id);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar producto en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Producto eliminado con éxito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
}
