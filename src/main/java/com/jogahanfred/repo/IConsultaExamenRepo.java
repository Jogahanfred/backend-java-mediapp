package com.jogahanfred.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jogahanfred.model.ConsultaExamen;

//@Repository
public interface IConsultaExamenRepo extends IGenericRepo<ConsultaExamen, Integer> {

	// SQL | DML DATA MANIPULATION LANGUAGE INSERT UPDATE DELETE -- true para
	// indicar que es query nativo
	//Cuando hay una anotacion con @Query debe ir con la anotacion @Modifying
	@Modifying
	@Query(value = "INSERT INTO tbl_consulta_examen(id_examen,id_consulta) VALUES (:idExamen, :idConsulta)", nativeQuery = true)
	Integer registrar(@Param("idConsulta") Integer idConsulta, @Param("idExamen") Integer idExamen);

	@Query("FROM ConsultaExamen ce where ce.consulta.idConsulta = :idConsulta")
	List<ConsultaExamen> listarExamenesPorConsulta(@Param("idConsulta") Integer idconsulta);
}
