package com.jogahanfred.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jogahanfred.model.ResetToken;
import com.jogahanfred.model.Usuario;
import com.jogahanfred.service.ILoginService;
import com.jogahanfred.service.IResetTokenService;
import com.jogahanfred.util.EmailUtil;
import com.jogahanfred.util.Mail;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private ILoginService service;
	@Autowired	
	private IResetTokenService tokenService;
	@Autowired
	private EmailUtil emailUtil;
	
	//Recibimos el correo
	@PostMapping(value = "/enviarCorreo", consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Integer> enviarCorreo(@RequestBody String correo) throws Exception {
		int rpta = 0;
		
		//verificamos si el correo registrado como usuario existe en BD y es correcto
		Usuario us = service.verificarNombreUsuario(correo);
		if(us != null && us.getIdUsuario() > 0) {
			ResetToken token = new ResetToken();//GENERAMOS UNA INSTANCIA
			//UUID.randomUUID() .- GENERA CADENA ALEATORIA Y CONVIERTE A STRING
			token.setToken(UUID.randomUUID().toString());
			token.setUser(us);
			token.setExpiracion(10);
			//GUARDAMOS TOKEN
			tokenService.guardar(token);

			//INSTANCIAMOS LA CLASE MAIL
			Mail mail = new Mail();
			//COLOCAMOS EL EMAIL EMISOR 
			mail.setFrom("anderssonml05071996@gmail.com");
			//COLOCAMOS EMAIL RECEPTOR :: CORREO ENVIAR DEL FRONT
			mail.setTo(us.getUsername());
			//ASUNTO
			mail.setSubject("RESTABLECER CONTRASEÑA  MEDIAPP");
			
			//MAPA QUE ESTA EN LA CLASE MAIL
			//AQUI IRA EL USUARIO Y LA URL QUE MODIFICARA EL PASSWORD
			Map<String, Object> model = new HashMap<>();
			//URL + EL TOKEN GENERADO
			String url = "http://localhost:4200/recuperar/" + token.getToken();
			//USUARIO
			model.put("user", token.getUser().getUsername());
			//TOKEN
			model.put("resetUrl", url);
			mail.setModel(model);
			
			//Se utiliza el metodo enviar correo si todo esta bien 
			emailUtil.enviarMail(mail);
			
			rpta = 1;	
			System.out.println(rpta);
		}
		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}
	
	//verificamos el token 
	@GetMapping(value = "/restablecer/verificar/{token}")
	public ResponseEntity<Integer> verificarToken(@PathVariable("token") String token) {
		int rpta = 0;//expirado
		try {
			if (token != null && !token.isEmpty()) {
				//si el token existe lo busca y valida
				ResetToken rt = tokenService.findByToken(token);
				if (rt != null && rt.getId() > 0) {
					if (!rt.estaExpirado()) {
						rpta = 1;// no esta expirado
					}
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<Integer>(rpta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}
	
	//restablecemos el token
	@PostMapping(value = "/restablecer/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> restablecerClave(@PathVariable("token") String token, @RequestBody String clave) {		
		try {
			ResetToken rt = tokenService.findByToken(token);		
			//cambiarmos la clave
			service.cambiarClave(clave, rt.getUser().getUsername());
			//eliminamos la solicitud del token
			tokenService.eliminar(rt);
		} catch (Exception e) {
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
}
