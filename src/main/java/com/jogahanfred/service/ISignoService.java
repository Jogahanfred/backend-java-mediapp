package com.jogahanfred.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
 
import com.jogahanfred.model.Signo;

public interface ISignoService extends ICRUD<Signo, Integer> {


	Page<Signo> listarPageable(Pageable pageable);
}
