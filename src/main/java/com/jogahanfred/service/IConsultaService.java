package com.jogahanfred.service;

import java.time.LocalDateTime;
import java.util.List;

import com.jogahanfred.dto.ConsultaListaExamenDTO;
import com.jogahanfred.dto.ConsultaResumenDTO;
import com.jogahanfred.dto.FiltroConsultaDTO;
import com.jogahanfred.model.Consulta;

public interface IConsultaService extends ICRUD<Consulta, Integer> {

	public Consulta registrarTransaccional(ConsultaListaExamenDTO dto);

	// Puedo manda (String dni, String nombreCompleto), pero nos apoyaremos de un
	// DTO

	List<Consulta> buscar(FiltroConsultaDTO filtro);

	List<Consulta> buscarFecha(LocalDateTime fecha);

	List<ConsultaResumenDTO> listarResumen();
	
	//devolvera arreglo de byte para que el front reconstruya el pdf
	byte[] generarReporte();
}

