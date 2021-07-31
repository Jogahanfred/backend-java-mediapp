package com.jogahanfred.service;

import com.jogahanfred.model.Archivo;

public interface IArchivoService {

	//guarda archivo
	int guardar(Archivo archivo);
	//lee archivo
	byte[] leerArchivo(Integer idArchivo);
}
