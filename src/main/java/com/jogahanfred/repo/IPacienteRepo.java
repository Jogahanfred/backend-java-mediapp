package com.jogahanfred.repo;
  

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jogahanfred.model.Paciente;

public interface IPacienteRepo extends IGenericRepo<Paciente, Integer>{

	@Query("FROM Paciente p WHERE p.dni = :dni")
	Paciente buscarPorDni(@Param("dni") String dni);
}
