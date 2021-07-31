package com.jogahanfred.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jogahanfred.model.Signo;
import com.jogahanfred.repo.IGenericRepo;
import com.jogahanfred.repo.ISignoRepo;
import com.jogahanfred.service.ISignoService;

@Service
public class SignoServiceImpl extends CRUDImpl<Signo, Integer> implements ISignoService {

	@Autowired
	private ISignoRepo repo;
	
	@Override
	protected IGenericRepo<Signo, Integer> getRepo() {
		return repo;
	}

	@Override
	public Page<Signo> listarPageable(Pageable pageable) {
		return repo.findAll(pageable);
	}

}
