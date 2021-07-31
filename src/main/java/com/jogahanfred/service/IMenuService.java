package com.jogahanfred.service;

import java.util.List;

import com.jogahanfred.model.Menu;

public interface IMenuService extends ICRUD<Menu, Integer>{
	
	//Metodo para obtener las opciones del usuario		
	List<Menu> listarMenuPorUsuario(String nombre);

}
