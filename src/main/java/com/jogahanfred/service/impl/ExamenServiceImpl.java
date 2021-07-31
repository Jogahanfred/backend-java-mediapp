package com.jogahanfred.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jogahanfred.model.Examen;
import com.jogahanfred.repo.IExamenRepo;
import com.jogahanfred.repo.IGenericRepo;
import com.jogahanfred.service.IExamenService;

@Service
public class ExamenServiceImpl extends CRUDImpl<Examen, Integer> implements IExamenService {

	@Autowired
	private IExamenRepo repo;

	@Override
	protected IGenericRepo<Examen, Integer> getRepo() {
		return repo;
	}

}
