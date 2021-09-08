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

import com.tekimaster.springboot.apirest.models.entity.TipoInsumo;
import com.tekimaster.springboot.apirest.models.entity.UnidadMedida;
import com.tekimaster.springboot.apirest.service.IParametroService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class ParametrosRestController {
	
	private static final Logger log = LoggerFactory.getLogger(ParametrosRestController.class);

	@Autowired
	private IParametroService parametroService;
	
	/*****************************************TIPO_DE_INSUMO ***********************************/
	
	@GetMapping("/parametros/tiposInsumo")
	public List<TipoInsumo> index(){
		return parametroService.findAllTiposInsumo();
	}
	
	@GetMapping("/parametros/tiposInsumo/page/{page}")
	public Page<TipoInsumo> index(@PathVariable Integer page){
		return parametroService.findAllTiposInsumo(PageRequest.of(page, 20)); 
	}
	
	@GetMapping("/parametros/tiposInsumo/{id}")
	public ResponseEntity<?> show(@PathVariable Long id){
		TipoInsumo tipoInsumo = null;
		Map<String, String> response = new HashMap<String, String>();
		try {
			tipoInsumo = parametroService.findByIdTipoInsumo(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al consultar tipo de insumo "+id+" en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, String>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(tipoInsumo==null) {
			response.put("mensaje", "El tipo de insumo "+id+" no existe en base de datos");
			return new ResponseEntity<Map<String, String>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<TipoInsumo>(parametroService.findByIdTipoInsumo(id), HttpStatus.OK);
	}
	
	@PostMapping("/parametros/tiposInsumo")
	public ResponseEntity<?> create(@Valid @RequestBody TipoInsumo tipoInsumo, BindingResult result){
		TipoInsumo tipoInsumoNew = null;
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
			tipoInsumoNew = parametroService.saveTipoInsumo(tipoInsumo);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar tipo de insumo en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Tipo de insumo ha sido creado con éxito!");
		response.put("data", tipoInsumoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/parametros/tiposInsumo/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody TipoInsumo tipoInsumo, BindingResult result, @PathVariable Long id){
		TipoInsumo tipoInsumoActual = parametroService.findByIdTipoInsumo(id);
		TipoInsumo tipoInsumoUpdated = null;
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '"+ err.getField()+"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(tipoInsumoActual==null) {
			response.put("mensaje", "No se pudo editar, tipo de insumo "+id+" no existe en base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			tipoInsumoActual.setNombre(tipoInsumo.getNombre());
			tipoInsumoUpdated =  parametroService.saveTipoInsumo(tipoInsumoActual);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar tipo de insumo en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Tipo de insumo actualizado con éxito!");
		response.put("data", tipoInsumoUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/parametros/tiposInsumo/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			
			TipoInsumo tipoInsumo = parametroService.findByIdTipoInsumo(id);
			
			parametroService.deleteTipoInsumo(id);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar tipo de insumo en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Tipo de insumo eliminado con éxito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	/*****************************************UNIDAD_DE_MEDIDA ***********************************/
	
	@GetMapping("/parametros/unidadesMedida")
	public List<UnidadMedida> indexUnidadMedida(){
		return parametroService.findAllUnidadesMedida();
	}
	
	@GetMapping("/parametros/unidadesMedida/page/{page}")
	public Page<UnidadMedida> indexUnidadMedida(@PathVariable Integer page){
		return parametroService.findAllUnidadesMedida(PageRequest.of(page, 20)); 
	}
	
	@GetMapping("/parametros/unidadesMedida/{id}")
	public ResponseEntity<?> showUnidadMedida(@PathVariable Long id){
		UnidadMedida unidadMedida = null;
		Map<String, String> response = new HashMap<String, String>();
		try {
			unidadMedida = parametroService.findByIdUnidadMedida(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al consultar unidad de medida "+id+" en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, String>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(unidadMedida==null) {
			response.put("mensaje", "La unidad de medida "+id+" no existe en base de datos");
			return new ResponseEntity<Map<String, String>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UnidadMedida>(parametroService.findByIdUnidadMedida(id), HttpStatus.OK);
	}
	
	@PostMapping("/parametros/unidadesMedida")
	public ResponseEntity<?> createUnidadMedida(@Valid @RequestBody UnidadMedida unidadMedida, BindingResult result){
		UnidadMedida unidadMedidaNew = null;
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
			unidadMedidaNew = parametroService.saveUnidadMedida(unidadMedida);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar unidad de medida en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Unidad de medida ha sido creada con éxito!");
		response.put("data", unidadMedidaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/parametros/unidadesMedida/{id}")
	public ResponseEntity<?> updateUnidadMedida(@Valid @RequestBody UnidadMedida unidadMedida, BindingResult result, @PathVariable Long id){
		UnidadMedida unidadMedidaActual = parametroService.findByIdUnidadMedida(id);
		UnidadMedida unidadMedidaUpdated = null;
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '"+ err.getField()+"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(unidadMedidaActual==null) {
			response.put("mensaje", "No se pudo editar, unidad de medida "+id+" no existe en base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			unidadMedidaActual.setNombre(unidadMedida.getNombre());
			unidadMedidaActual.setNombreAbreviado(unidadMedida.getNombreAbreviado());
			unidadMedidaUpdated =  parametroService.saveUnidadMedida(unidadMedidaActual);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar unidad de medida en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Unidad de medida actualizada con éxito!");
		response.put("data", unidadMedidaUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/parametros/unidadesMedida/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> deleteUnidadMedida(@PathVariable Long id){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			
			UnidadMedida unidadMedida = parametroService.findByIdUnidadMedida(id);
			
			parametroService.deleteUnidadMedida(id);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar unidad de medida en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Unidad de medida eliminada con éxito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
/*****************************************TIPO_DE_EQUIPAMIENTO ***********************************/
/*	
	@GetMapping("/parametros/tiposEquipamiento")
	public List<TipoInsumo> indexTiposEquipamiento(){
		return parametroService.findAllTiposInsumo();
	}
	
	@GetMapping("/parametros/tiposInsumo/page/{page}")
	public Page<TipoInsumo> index(@PathVariable Integer page){
		return parametroService.findAllTiposInsumo(PageRequest.of(page, 20)); 
	}
	
	@GetMapping("/parametros/tiposInsumo/{id}")
	public ResponseEntity<?> show(@PathVariable Long id){
		TipoInsumo tipoInsumo = null;
		Map<String, String> response = new HashMap<String, String>();
		try {
			tipoInsumo = parametroService.findByIdTipoInsumo(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al consultar tipo de insumo "+id+" en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, String>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(tipoInsumo==null) {
			response.put("mensaje", "El tipo de insumo "+id+" no existe en base de datos");
			return new ResponseEntity<Map<String, String>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<TipoInsumo>(parametroService.findByIdTipoInsumo(id), HttpStatus.OK);
	}
	
	@PostMapping("/parametros/tiposInsumo")
	public ResponseEntity<?> create(@Valid @RequestBody TipoInsumo tipoInsumo, BindingResult result){
		TipoInsumo tipoInsumoNew = null;
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
			tipoInsumoNew = parametroService.saveTipoInsumo(tipoInsumo);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar tipo de insumo en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Tipo de insumo ha sido creado con éxito!");
		response.put("data", tipoInsumoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/parametros/tiposInsumo/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody TipoInsumo tipoInsumo, BindingResult result, @PathVariable Long id){
		TipoInsumo tipoInsumoActual = parametroService.findByIdTipoInsumo(id);
		TipoInsumo tipoInsumoUpdated = null;
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '"+ err.getField()+"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(tipoInsumoActual==null) {
			response.put("mensaje", "No se pudo editar, tipo de insumo "+id+" no existe en base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			tipoInsumoActual.setNombre(tipoInsumo.getNombre());
			tipoInsumoUpdated =  parametroService.saveTipoInsumo(tipoInsumoActual);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar tipo de insumo en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Tipo de insumo actualizado con éxito!");
		response.put("data", tipoInsumoUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/parametros/tiposInsumo/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			
			TipoInsumo tipoInsumo = parametroService.findByIdTipoInsumo(id);
			
			parametroService.deleteTipoInsumo(id);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar tipo de insumo en base de datos");
			response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Tipo de insumo eliminado con éxito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
*/
}
