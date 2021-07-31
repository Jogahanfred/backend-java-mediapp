package com.jogahanfred.service;

import com.jogahanfred.model.Usuario;

public interface ILoginService {

	//verificamos si existe el usuario
	Usuario verificarNombreUsuario(String usuario);
	void cambiarClave(String clave, String nombre);
}
