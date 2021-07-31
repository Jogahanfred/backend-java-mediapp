package com.jogahanfred.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jogahanfred.model.Medico;
import com.jogahanfred.repo.IGenericRepo;
import com.jogahanfred.repo.IMedicoRepo;
import com.jogahanfred.service.IMedicoService;

@Service
public class MedicoServiceImpl extends CRUDImpl<Medico, Integer> implements IMedicoService {

	@Autowired
	private IMedicoRepo repo;

	@Override
	protected IGenericRepo<Medico, Integer> getRepo() {
		return repo;
	}

}
