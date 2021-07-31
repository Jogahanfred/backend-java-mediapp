package com.jogahanfred.controller;
 

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; 
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
import com.jogahanfred.model.Signo;
import com.jogahanfred.service.ISignoService;

@RestController
@RequestMapping("/signos")
public class SignoController {
	
	@Autowired
	private ISignoService signoService;
	
	@GetMapping
	public ResponseEntity<List<Signo>> listar() throws Exception {
		List<Signo> lista = signoService.listar();
		return new ResponseEntity<List<Signo>>(lista, HttpStatus.OK);

	}
 
	@GetMapping("/{id}")
	public ResponseEntity<Signo> listarPorId(@PathVariable("id") Integer id) throws Exception {
		Signo signo = signoService.listarPorId(id);
		if (signo == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}

		return new ResponseEntity<Signo>(signo, HttpStatus.OK);

	}

	@PostMapping
	public ResponseEntity<Signo> registrar(@Valid @RequestBody Signo p) throws Exception {
		Signo signo = signoService.registrar(p); 
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(signo.getIdSigno()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping
	public ResponseEntity<Signo> modificar(@Valid @RequestBody Signo p) throws Exception {
		Signo signo = signoService.modificar(p);
		return new ResponseEntity<Signo>(signo, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception {
		Signo signoPaciente = signoService.listarPorId(id);
		if (signoPaciente == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}
		signoService.eliminar(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

	}
	
	@GetMapping("/pageable")
	public ResponseEntity<Page<Signo>> listarPageable(Pageable pageable) throws Exception{
		Page<Signo> signo = signoService.listarPageable(pageable);
		return new ResponseEntity<Page<Signo>>(signo, HttpStatus.OK);
	}

}

	 
