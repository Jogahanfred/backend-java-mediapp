package com.jogahanfred.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jogahanfred.model.ConsultaExamen;
import com.jogahanfred.repo.IConsultaExamenRepo;
import com.jogahanfred.service.IConsultaExamenService; 

@Service
public class ConsultaExamenServiceImpl implements IConsultaExamenService{

	@Autowired
	private IConsultaExamenRepo repo;
	
	@Override
	public List<ConsultaExamen> listarExamenesPorConsulta(Integer idConsulta) {
		return repo.listarExamenesPorConsulta(idConsulta);
	}

}
