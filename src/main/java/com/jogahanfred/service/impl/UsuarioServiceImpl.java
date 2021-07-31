package com.jogahanfred.service.impl;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import com.jogahanfred.model.Usuario;
import com.jogahanfred.repo.IUsuarioRepo;
@Service
public class UsuarioServiceImpl implements UserDetailsService{
	@Autowired
	private IUsuarioRepo repo;	
	@Override//Vendra el usuario del front y devolvera a la CLASE CONFIGURACION LA INFORMACION DE ESTE METODO 
	//PARA QUE SPRING SEPA EL USUARIO Y ROLES QUE TIENE
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//Busco con el servicio si existe el usuario
		Usuario usuario = repo.findOneByUsername(username);
		//Muestro msj si no existe
		if(usuario == null) {
			throw new UsernameNotFoundException(String.format("Usuario no existe", username));
		}
		//Spring security no manejas los roles como cadenas de texto
		//Los roles se tienen que guardar en una clase GrantedAuthority segun spring.
		List<GrantedAuthority> roles = new ArrayList<>();
		//Aqui obtengo los roles, y para cada rol lo guardo en la implementacion SimpleGrantedAuthority 
		usuario.getRoles().forEach(rol -> {
			roles.add(new SimpleGrantedAuthority(rol.getNombre()));
		});
		//generamos una clase de spring security, y le pasamos informacion que nos solicita
		UserDetails ud = new User(usuario.getUsername(), usuario.getPassword(), usuario.isEnabled(), true, true, true, roles);
		return ud;
	}
}
