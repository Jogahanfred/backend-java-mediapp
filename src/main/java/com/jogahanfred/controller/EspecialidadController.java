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
import com.jogahanfred.model.Especialidad;
import com.jogahanfred.service.IEspecialidadService;

//HATEOAS
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadController {

	@Autowired
	private IEspecialidadService especialidadService;

	@GetMapping
	public ResponseEntity<List<Especialidad>> listar() throws Exception {
		List<Especialidad> lista = especialidadService.listar();
		return new ResponseEntity<List<Especialidad>>(lista, HttpStatus.OK);

	}

	// @Valid para que se respete los constrains
	// con @PATHVARIBLE recepcionas el id que viene con la url
	@GetMapping("/{id}")
	public ResponseEntity<Especialidad> listarPorId(@PathVariable("id") Integer id) throws Exception {
		Especialidad especialidad = especialidadService.listarPorId(id);
		if (especialidad == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}

		return new ResponseEntity<Especialidad>(especialidad, HttpStatus.OK);

	}

	// Forma 3, nivel 3 Richardson / para mostrar un bloque de localizacion href
	@GetMapping("/hateoas/{id}")
	public EntityModel<Especialidad> listarPorIdHateoas(@PathVariable("id") Integer id) throws Exception {
		Especialidad especialidad = especialidadService.listarPorId(id);
		if (especialidad == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}

		EntityModel<Especialidad> recurso = EntityModel.of(especialidad);
		//THIS.CLASS() SE REFIERE A LA CLASE QUE HACE REFERENCIA, EN ESTE CASO A MEDICOCONTROLLER.
		//LUEGO LISTARPORID(ID) SE REFIERE AL METODO LISTAR... Y LA DEVOLUCION DE ESTE
		//TODO ELLO SE GUARDA EN LINK 
		// localhost:8080/especialidades/18
		//HATEOAS
		//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
		//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
		//PARA EVITAR ESTAR LLAMANDO "WebMvcLinkBuilder.methodOn"
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).listarPorId(id));

		recurso.add(link.withRel("especialidad-recurso"));
		return recurso;

	}

	// (JACKSON)con @REQUESTBODY serializa el JSON a Objeto java
	/*
	 * Forma 1,nivel 1 Richardson / sin la localizacion
	 * 
	 * @PostMapping public ResponseEntity<Especialidad> registrar(@Valid @RequestBody
	 * Especialidad p) throws Exception { Especialidad especialidad =
	 * especialidadService.registrar(p); return new ResponseEntity<Especialidad>(especialidad,
	 * HttpStatus.CREATED); }
	 */
	// Forma 2, nivel 2 Richardson / con la localizacion
	@PostMapping
	public ResponseEntity<Especialidad> registrar(@Valid @RequestBody Especialidad e) throws Exception {
		Especialidad especialidad = especialidadService.registrar(e);
		// Esto "ServletUriComponentsBuilder.fromCurrentRequest()" obtiene la url de la
		// peticion actual.
		// ".PATH" es para agregar el id a la url actual, id que se obtiene del modelo
		// PACIENTE que esta ingresando
		// localhost:8080/especialidades({2}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(especialidad.getIdEspecialidad()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping
	public ResponseEntity<Especialidad> modificar(@Valid @RequestBody Especialidad e) throws Exception {
		Especialidad especialidad = especialidadService.modificar(e);
		return new ResponseEntity<Especialidad>(especialidad, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception {
		Especialidad especialidad = especialidadService.listarPorId(id);
		if (especialidad == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}
		especialidadService.eliminar(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

	}

}
