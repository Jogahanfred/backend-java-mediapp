package com.jogahanfred.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.jogahanfred.model.Medico;
import com.jogahanfred.service.IMedicoService;

//HATEOAS
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

	@Autowired
	private IMedicoService medicoService;
	//Dinamicamente
	//@PreAuthorize("@authServiceImpl.tieneAcceso('listar')")//le hago una inyeccion mandando parametros
	//@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping
	public ResponseEntity<List<Medico>> listar() throws Exception {
		List<Medico> lista = medicoService.listar();
		return new ResponseEntity<List<Medico>>(lista, HttpStatus.OK);

	}

	// @Valid para que se respete los constrains
	// con @PATHVARIBLE recepcionas el id que viene con la url
	@GetMapping("/{id}")
	public ResponseEntity<Medico> listarPorId(@PathVariable("id") Integer id) throws Exception {
		Medico medico = medicoService.listarPorId(id);
		if (medico == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}

		return new ResponseEntity<Medico>(medico, HttpStatus.OK);

	}

	// Forma 3, nivel 3 Richardson / para mostrar un bloque de localizacion href
	@GetMapping("/hateoas/{id}")
	public EntityModel<Medico> listarPorIdHateoas(@PathVariable("id") Integer id) throws Exception {
		Medico medico = medicoService.listarPorId(id);
		if (medico == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}

		EntityModel<Medico> recurso = EntityModel.of(medico);
		//THIS.CLASS() SE REFIERE A LA CLASE QUE HACE REFERENCIA, EN ESTE CASO A MEDICOCONTROLLER.
		//LUEGO LISTARPORID(ID) SE REFIERE AL METODO LISTAR... Y LA DEVOLUCION DE ESTE
		//TODO ELLO SE GUARDA EN LINK 
		// localhost:8080/medicos/18
		//HATEOAS
		//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
		//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
		//PARA EVITAR ESTAR LLAMANDO "WebMvcLinkBuilder.methodOn"
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).listarPorId(id));

		recurso.add(link.withRel("medico-recurso"));
		return recurso;

	}

	// (JACKSON)con @REQUESTBODY serializa el JSON a Objeto java
	/*
	 * Forma 1,nivel 1 Richardson / sin la localizacion
	 * 
	 * @PostMapping public ResponseEntity<Medico> registrar(@Valid @RequestBody
	 * Medico p) throws Exception { Medico medico =
	 * medicoService.registrar(p); return new ResponseEntity<Medico>(medico,
	 * HttpStatus.CREATED); }
	 */
	// Forma 2, nivel 2 Richardson / con la localizacion
	@PostMapping
	public ResponseEntity<Medico> registrar(@Valid @RequestBody Medico m) throws Exception {
		Medico medico = medicoService.registrar(m);
		// Esto "ServletUriComponentsBuilder.fromCurrentRequest()" obtiene la url de la
		// peticion actual.
		// ".PATH" es para agregar el id a la url actual, id que se obtiene del modelo
		// PACIENTE que esta ingresando
		// localhost:8080/medicos({2}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(medico.getIdMedico()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping
	public ResponseEntity<Medico> modificar(@Valid @RequestBody Medico m) throws Exception {
		Medico medico = medicoService.modificar(m);
		return new ResponseEntity<Medico>(medico, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception {
		Medico medico = medicoService.listarPorId(id);
		if (medico == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}
		medicoService.eliminar(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

	}

}
