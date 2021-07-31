package com.jogahanfred.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jogahanfred.exception.ModeloNotFoundException;
import com.jogahanfred.model.Paciente;
import com.jogahanfred.service.IPacienteService;

//HATEOAS
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

	@Autowired
	private IPacienteService pacienteService;

	@GetMapping
	public ResponseEntity<List<Paciente>> listar() throws Exception {
		List<Paciente> lista = pacienteService.listar();
		return new ResponseEntity<List<Paciente>>(lista, HttpStatus.OK);

	}

	// @Valid para que se respete los constrains
	// con @PATHVARIBLE recepcionas el id que viene con la url
	@GetMapping("/{id}")
	public ResponseEntity<Paciente> listarPorId(@PathVariable("id") Integer id) throws Exception {
		Paciente paciente = pacienteService.listarPorId(id);
		if (paciente == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}

		return new ResponseEntity<Paciente>(paciente, HttpStatus.OK);

	}

	// Forma 3, nivel 3 Richardson / para mostrar un bloque de localizacion href
	@GetMapping("/hateoas/{id}")
	public EntityModel<Paciente> listarPorIdHateoas(@PathVariable("id") Integer id) throws Exception {
		Paciente paciente = pacienteService.listarPorId(id);
		if (paciente == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}

		EntityModel<Paciente> recurso = EntityModel.of(paciente);
		//THIS.CLASS() SE REFIERE A LA CLASE QUE HACE REFERENCIA, EN ESTE CASO A PACIENTECONTROLLER.
		//LUEGO LISTARPORID(ID) SE REFIERE AL METODO LISTAR... Y LA DEVOLUCION DE ESTE
		//TODO ELLO SE GUARDA EN LINK 
		// localhost:8080/pacientes/18
		//HATEOAS
		//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
		//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
		//PARA EVITAR ESTAR LLAMANDO "WebMvcLinkBuilder.methodOn"
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).listarPorId(id));

		recurso.add(link.withRel("paciente-recurso"));
		return recurso;

	}

	// (JACKSON)con @REQUESTBODY serializa el JSON a Objeto java
	/*
	 * Forma 1,nivel 1 Richardson / sin la localizacion
	 * 
	 * @PostMapping public ResponseEntity<Paciente> registrar(@Valid @RequestBody
	 * Paciente p) throws Exception { Paciente paciente =
	 * pacienteService.registrar(p); return new ResponseEntity<Paciente>(paciente,
	 * HttpStatus.CREATED); }
	 */
	// Forma 2, nivel 2 Richardson / con la localizacion
	@PostMapping
	public ResponseEntity<Paciente> registrar(@Valid @RequestBody Paciente p) throws Exception {
		Paciente paciente = pacienteService.registrar(p);
		// Esto "ServletUriComponentsBuilder.fromCurrentRequest()" obtiene la url de la
		// peticion actual.
		// ".PATH" es para agregar el id a la url actual, id que se obtiene del modelo
		// PACIENTE que esta ingresando
		// localhost:8080/pacientes({2}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(paciente.getIdPaciente()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping
	public ResponseEntity<Paciente> modificar(@Valid @RequestBody Paciente p) throws Exception {
		Paciente paciente = pacienteService.modificar(p);
		return new ResponseEntity<Paciente>(paciente, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception {
		Paciente paciente = pacienteService.listarPorId(id);
		if (paciente == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}
		pacienteService.eliminar(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

	}
	
	@GetMapping("/pageable")
	public ResponseEntity<Page<Paciente>> listarPageable(Pageable pageable) throws Exception{
		Page<Paciente> pacientes = pacienteService.listarPageable(pageable);
		return new ResponseEntity<Page<Paciente>>(pacientes, HttpStatus.OK);
	}
	
	@GetMapping("/buscarPorDni/{dni}")
	public ResponseEntity<Paciente> listarPorDni(@PathVariable("dni") String dni) throws Exception {
		Paciente paciente = pacienteService.buscarPorDni(dni);
		if (paciente == null) {
			throw new ModeloNotFoundException("DNI no encontrado: " + dni);
		}

		return new ResponseEntity<Paciente>(paciente, HttpStatus.OK);

	}

}
