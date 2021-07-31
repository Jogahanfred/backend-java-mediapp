package com.jogahanfred.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jogahanfred.model.Paciente;
import com.jogahanfred.repo.IGenericRepo;
import com.jogahanfred.repo.IPacienteRepo;
import com.jogahanfred.service.IPacienteService;

@Service
public class PacienteServiceImpl extends CRUDImpl<Paciente, Integer> implements IPacienteService {

	@Autowired
	private IPacienteRepo repo;

	@Override
	protected IGenericRepo<Paciente, Integer> getRepo() {
		return repo;
	}
	
	@Override
	public Page<Paciente> listarPageable(Pageable pageable) {
		return repo.findAll(pageable);
	}
 
	
	@Override
	public Paciente buscarPorDni(String dni) {
		return repo.buscarPorDni(dni);
	}

}
