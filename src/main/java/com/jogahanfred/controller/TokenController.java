package com.jogahanfred.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tokens")
public class TokenController {
	
	//inyecta tokenServices de la clase configurada
	@Autowired
	private ConsumerTokenServices tokenServices;
		
	//{tokenId:.*}.- cUANDO ES ASI TIENE UN COMPORTAMIENTO ESPECIAL
	//PORQUE VIENE EL TOKEN euudje.eueueue.
	//si esque no se coloca asi el token viajara hasta el primer punto
	@GetMapping("/anular/{tokenId:.*}")
	public void revocarToken(@PathVariable("tokenId") String token) {
		//BUSCA EN LA TABLAS GENERADAS Y LOS ELIMINA
		tokenServices.revokeToken(token);
	}
}
