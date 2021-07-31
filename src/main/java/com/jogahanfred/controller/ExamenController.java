package com.jogahanfred.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.jogahanfred.model.Examen;
import com.jogahanfred.service.IExamenService;

//HATEOAS
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/examenes")
public class ExamenController {

	@Autowired
	private IExamenService examenService;

	@GetMapping
	public ResponseEntity<List<Examen>> listar() throws Exception {
		List<Examen> lista = examenService.listar();
		return new ResponseEntity<List<Examen>>(lista, HttpStatus.OK);

	}

	// @Valid para que se respete los constrains
	// con @PATHVARIBLE recepcionas el id que viene con la url
	@GetMapping("/{id}")
	public ResponseEntity<Examen> listarPorId(@PathVariable("id") Integer id) throws Exception {
		Examen examen = examenService.listarPorId(id);
		if (examen == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}

		return new ResponseEntity<Examen>(examen, HttpStatus.OK);

	}

	// Forma 3, nivel 3 Richardson / para mostrar un bloque de localizacion href
	@GetMapping("/hateoas/{id}")
	public EntityModel<Examen> listarPorIdHateoas(@PathVariable("id") Integer id) throws Exception {
		Examen examen = examenService.listarPorId(id);
		if (examen == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}

		EntityModel<Examen> recurso = EntityModel.of(examen);
		//THIS.CLASS() SE REFIERE A LA CLASE QUE HACE REFERENCIA, EN ESTE CASO A MEDICOCONTROLLER.
		//LUEGO LISTARPORID(ID) SE REFIERE AL METODO LISTAR... Y LA DEVOLUCION DE ESTE
		//TODO ELLO SE GUARDA EN LINK 
		// localhost:8080/examenes/18
		//HATEOAS
		//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
		//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
		//PARA EVITAR ESTAR LLAMANDO "WebMvcLinkBuilder.methodOn"
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).listarPorId(id));

		recurso.add(link.withRel("examen-recurso"));
		return recurso;

	}

	// (JACKSON)con @REQUESTBODY serializa el JSON a Objeto java
	/*
	 * Forma 1,nivel 1 Richardson / sin la localizacion
	 * 
	 * @PostMapping public ResponseEntity<Examen> registrar(@Valid @RequestBody
	 * Examen p) throws Exception { Examen examen =
	 * examenService.registrar(p); return new ResponseEntity<Examen>(examen,
	 * HttpStatus.CREATED); }
	 */
	// Forma 2, nivel 2 Richardson / con la localizacion
	@PostMapping
	public ResponseEntity<Examen> registrar(@Valid @RequestBody Examen e) throws Exception {
		Examen examen = examenService.registrar(e);
		// Esto "ServletUriComponentsBuilder.fromCurrentRequest()" obtiene la url de la
		// peticion actual.
		// ".PATH" es para agregar el id a la url actual, id que se obtiene del modelo
		// PACIENTE que esta ingresando
		// localhost:8080/examenes({2}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(examen.getIdExamen()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping
	public ResponseEntity<Examen> modificar(@Valid @RequestBody Examen e) throws Exception {
		Examen examen = examenService.modificar(e);
		return new ResponseEntity<Examen>(examen, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception {
		Examen examen = examenService.listarPorId(id);
		if (examen == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}
		examenService.eliminar(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

	}

}
