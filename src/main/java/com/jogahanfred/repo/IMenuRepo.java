 package com.jogahanfred.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jogahanfred.model.Menu;

public interface IMenuRepo extends IGenericRepo<Menu, Integer>{

	@Query(value="select m.* from menu_rol mr inner join usuario_rol ur on ur.id_rol = mr.id_rol "
			+ "inner join menu m on m.id_menu = mr.id_menu inner join usuario u on u.id_usuario = ur.id_usuario "
			+ "where u.nombre = :nombre", 
			nativeQuery = true)
	//Por defecto devolvia arreglo de Object[], pero como menu tiene la estructura
	List<Menu> listarMenuPorUsuario(@Param("nombre") String nombre);

}
