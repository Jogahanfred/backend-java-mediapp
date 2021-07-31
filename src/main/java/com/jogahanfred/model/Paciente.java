package com.jogahanfred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
 
//Info para el Swagger
@Schema(description = "Paciente Model")
@Entity
@Table(name = "tbl_paciente")
public class Paciente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idPaciente;

	//@JsonProperty("xyz") -- como un alias para el JSON
	@Schema(description = "Nombre del Paciente")
	@NotNull
	//@Size(min = 3, message = "Nombres es minimo 3 caracteres")
	//PARA MOSTRAR EN OTRO IDIOMAS
	@Size(min = 3, message = "{nombres.size}")
	@Column(name = "nombres", nullable = false, length = 70)
	private String nombres;

	@NotEmpty
	@Size(min = 3, message = "{apellidos.size}")
	@Column(name = "apellidos", nullable = false, length = 70)
	private String apellidos;

	// @Max @Min es para Integer::::@Size es para cadena
	@Size(min = 8, max = 8, message = "DNI debe tener 8 digitos")
	@Column(name = "dni", nullable = false, length = 9, unique = true)
	private String dni;

	@Size(min = 3, max = 150, message = "Dirección debe tener minimo 3 caracteres")
	@Column(name = "direccion", nullable = true, length = 150)
	private String direccion;

	@Size(min = 9, max = 9, message = "Telefono debe tener 9 digitos")
	@Column(name = "telefono", nullable = true, length = 9)
	private String telefono;
	
	@Email( message = "E-mail formato incorrecto")
	//@Pattern(regexp ="")--para darle un patron a el email
	@Column(name = "email", nullable = true, length = 55)
	private String email;

	public Integer getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(Integer idPaciente) {
		this.idPaciente = idPaciente;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	

	
}
