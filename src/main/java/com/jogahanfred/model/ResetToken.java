package com.jogahanfred.model;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ResetToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	//TOKEN GENERADO EN STRING , UNIQUE= TRUE PARA QUE NO SE REPITA
	@Column(nullable = false, unique = true)
	private String token;
	
	//UNION 1 A 1 CON LA CLASE USUARIO
	@OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "id_usuario")
	private Usuario user;
	
	//EXPIRACION OBLIGATORIA
	@Column(nullable = false)
	private LocalDateTime expiracion;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public LocalDateTime getExpiracion() {
		return expiracion;
	}

	public void setExpiracion(LocalDateTime expiracion) {
		this.expiracion = expiracion;
	}
	
	//SETTER PARA ENVIAR MINUTOS Y VER CUANDO EXPIRA
	public void setExpiracion(int minutos) {	
		LocalDateTime hoy = LocalDateTime.now();//FECHA-HORA ACTUAL
		LocalDateTime exp = hoy.plusMinutes(minutos);//+ LOS MINUTOS INGRESADOS
		this.expiracion = exp;//GUARDA ASI EN BD LA NUEVA EXPIRACION
	}
	
	//METODO PARA VALIDAR SI YA EXPIRO TOKEN
	public boolean estaExpirado() {
		//VALIDA SI LA FECHA ACTUAL ES DESPUES DE LA FECHA DE EXPIRACION
		return LocalDateTime.now().isAfter(this.expiracion);
	}

}
