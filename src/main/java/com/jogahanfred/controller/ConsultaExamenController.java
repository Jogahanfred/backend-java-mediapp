package com.jogahanfred.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jogahanfred.model.ConsultaExamen;
import com.jogahanfred.service.IConsultaExamenService;

@RestController
@RequestMapping("/consultaexamenes")
public class ConsultaExamenController {
	
	@Autowired
	private IConsultaExamenService service;
	
	@GetMapping(value = "/{idConsulta}")
	public ResponseEntity<List<ConsultaExamen>> listar(@PathVariable("idConsulta") Integer idconsulta) {
		List<ConsultaExamen> consultasexamen = new ArrayList<>();
		consultasexamen = service.listarExamenesPorConsulta(idconsulta);
		return new ResponseEntity<List<ConsultaExamen>>(consultasexamen, HttpStatus.OK);
		
		//BD | consulta_examen
		//[1, 1]
		//[1, 2]
		//[1, 3]
		
		//List
		//[new Consulta, new Examen]
		//[new Consulta, new Examen]
		//[new Consulta, new Examen]
	}

}
