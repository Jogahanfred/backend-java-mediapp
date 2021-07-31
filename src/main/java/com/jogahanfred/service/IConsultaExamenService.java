package com.jogahanfred.service;

import java.util.List;

import com.jogahanfred.model.ConsultaExamen;

public interface IConsultaExamenService {

	List<ConsultaExamen> listarExamenesPorConsulta(Integer idconsulta);
}
