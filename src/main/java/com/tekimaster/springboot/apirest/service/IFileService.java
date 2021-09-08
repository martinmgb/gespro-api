package com.tekimaster.springboot.apirest.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
	
	public Path getPath(String nombreArchivo);
	
	public void eliminarArchivo(String nombreArchivo);
	
	public void copiarArchivo(MultipartFile archivo, String nombreArchivo) throws IOException;
	
	public Resource getRecursoArchivo(String nombreArchivo) throws MalformedURLException;
}
