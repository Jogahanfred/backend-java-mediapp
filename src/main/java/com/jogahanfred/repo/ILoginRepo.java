package com.jogahanfred.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.jogahanfred.model.Usuario;

public interface ILoginRepo extends IGenericRepo<Usuario, Integer>{

	//VERIFICAMOS EL USUARIO
	@Query("FROM Usuario us where us.username =:usuario")
	Usuario verificarNombreUsuario(@Param("usuario")String usuario);
	
	//ACTUALIZAR LA CLAVE 
	@Transactional
	@Modifying
	@Query("UPDATE Usuario us SET us.password =:clave WHERE us.username =:nombre")
	void cambiarClave(@Param("clave") String clave, @Param("nombre") String nombre);
	
}
