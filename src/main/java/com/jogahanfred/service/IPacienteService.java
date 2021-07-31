package com.jogahanfred.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; 
import com.jogahanfred.model.Paciente;

public interface IPacienteService extends ICRUD<Paciente, Integer> {

	Page<Paciente> listarPageable(Pageable pageable);
 
	Paciente buscarPorDni(String dni);
}
