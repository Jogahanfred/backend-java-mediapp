package com.jogahanfred.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jogahanfred.model.Especialidad;
import com.jogahanfred.repo.IEspecialidadRepo;
import com.jogahanfred.repo.IGenericRepo;
import com.jogahanfred.service.IEspecialidadService; 

@Service
public class EspecialidadServiceImpl extends CRUDImpl<Especialidad, Integer> implements IEspecialidadService {

	@Autowired
	private IEspecialidadRepo repo;

	@Override
	protected IGenericRepo<Especialidad, Integer> getRepo() {
		return repo;
	}

}
