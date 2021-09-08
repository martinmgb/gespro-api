package com.tekimaster.springboot.apirest.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tekimaster.springboot.apirest.service.IFileService;

@Service
public class FileServiceImpl implements IFileService{

	private static final String PATH_UPLOAD = "uploads";

	@Override
	public Path getPath(String nombreArchivo) {
		return Paths.get(PATH_UPLOAD).resolve(nombreArchivo).toAbsolutePath();
	}

	@Override
	public void eliminarArchivo(String nombreArchivo) {
		if(nombreArchivo!=null && nombreArchivo.length()>0) {
			File archivo = getPath(nombreArchivo).toFile();
			if(archivo.exists() && archivo.canRead()) {
				archivo.delete();
			}
		}
	}

	@Override
	public void copiarArchivo(MultipartFile archivo, String nombreArchivo) throws IOException{
		Files.copy(archivo.getInputStream(), getPath(nombreArchivo));
	}

	@Override
	public Resource getRecursoArchivo(String nombreArchivo)  throws MalformedURLException{
		return new UrlResource(getPath(nombreArchivo).toUri());
	}

}
