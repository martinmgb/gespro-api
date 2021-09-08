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
import com.tekimaster.springboot.apirest.service.IInsumoService;

@CrossOrigin({"*"})
@RestController
@RequestMapping("/api")
public class InsumoRestController {
	
	private static final Logger log = LoggerFactory.getLogger(InsumoRestController.class);

	@Autowired
	private IInsumoService insumoService;

	@GetMapping("/insumos")
	public List<Insumo> index(){
		return insumoService.findAll();
	}
	
	@GetMapping("/insumos/orderByNombre")
	public List<Insumo> findAllByOrderByNombreAsc(){
		return insumoService.findAllByOrderByNombreAsc();
	}
	
	@GetMapping("/insumos/page/{page}")
	public Page<Insumo> index(@PathVariable Integer page){
		return insumoService.findAll(PageRequest.of(page, 20)); 
	}
	
	@GetMapping("/insumos/nombre/{nombre}/page/{page}")
	public Page<Insumo> index(@PathVariable String nombre, @PathVariable Integer page){
		return insumoService.findByNombre(nombre, PageRequest.of(page, 20)); 
	}
	
	@GetMapping("/insumos/{id}")
	public ResponseEntity<?> show(@PathVariable Long id){
		Insumo insumo = null;
		Map<String, String> response = new HashMap<String, String>();
		try {
			insumo = insumoService.findById(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al consultar insumo "+id+" en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, String>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(insumo==null) {
			response.put("mensaje", "El insumo "+id+" no existe en base de datos");
			return new ResponseEntity<Map<String, String>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Insumo>(insumoService.findById(id), HttpStatus.OK);
	}
	
	@PostMapping("/insumos")
	public ResponseEntity<?> create(@Valid @RequestBody Insumo insumo, BindingResult result){
		Insumo insumoNew = null;
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
			insumoNew = insumoService.save(insumo);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar insumo en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Insumo ha sido creado con éxito!");
		response.put("data", insumoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/insumos/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Insumo insumo, BindingResult result, @PathVariable Long id){
		Insumo insumoActual = insumoService.findById(id);
		Insumo insumoUpdated = null;
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '"+ err.getField()+"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(insumo==null) {
			response.put("mensaje", "No se pudo editar, insumo "+id+" no existe en base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			insumoActual.setNombre(insumo.getNombre());
			insumoActual.setCosto(insumo.getCosto());
			insumoActual.setStock(insumo.getStock());
			insumoActual.setTipoInsumo(insumo.getTipoInsumo());
			insumoActual.setUnidadMedida(insumo.getUnidadMedida());
			insumoUpdated =  insumoService.save(insumoActual);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar insumo en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Insumo actualizado con éxito!");
		response.put("data", insumoUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/insumos/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			
			Insumo insumo = insumoService.findById(id);
			
			insumoService.delete(id);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar insumo en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Insumo eliminado con éxito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
}
