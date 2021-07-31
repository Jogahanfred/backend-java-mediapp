package com.jogahanfred.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {

	public boolean tieneAcceso(String path) {
		
		boolean rpta = false;

		String metodoRol = "";
		//Valida lo que venga en el parametro desde el controller
		switch (path) {
		case "listar":
			metodoRol = "ADMIN";
			break;

		case "listarId":
			metodoRol = "ADMIN,USER,DBA";
			break;
		}
		
		String metodoRoles[] = metodoRol.split(","); //guardamos los roles separando de la coma
       //informacion que brinda el token
		Authentication usuarioLogueado = SecurityContextHolder.getContext().getAuthentication();

		System.out.println(usuarioLogueado.getName());
		//iteramos los GrantedAuthority que tiene el usuario logeado
		for (GrantedAuthority auth : usuarioLogueado.getAuthorities()) {
			String rolUser = auth.getAuthority();//obtenemos el nombre de rol
			System.out.println(rolUser);
			//si el rol que viene en el parametro es igual al que tiene el usuario del token
			for (String rolMet : metodoRoles) {
				if (rolUser.equalsIgnoreCase(rolMet)) {
					rpta = true;
				}
			}
		}
		
		return rpta;
	}
}
