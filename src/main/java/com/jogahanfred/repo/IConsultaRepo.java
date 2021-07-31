package com.jogahanfred.repo;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.jogahanfred.model.Consulta;
//@Repository
public interface IConsultaRepo extends IGenericRepo<Consulta, Integer> {
	// Angular : jaime
	// BD: jaime
	@Query("FROM Consulta c WHERE c.paciente.dni = :dni OR LOWER(c.paciente.nombres) "
			+ "LIKE %:nombreCompleto% OR LOWER(c.paciente.apellidos) "
			+ "LIKE %:nombreCompleto%")
	List<Consulta> buscar(@Param("dni") String dni, @Param("nombreCompleto") String nombreCompleto);

	// >= 22-05-21 //< 23-05-21
	// SELECT new com.mitocode.model.Consulta(c.paciete, c.medico)
	@Query("FROM Consulta c WHERE c.fecha BETWEEN :fechaConsulta AND :fechaSgte")
	List<Consulta> buscarFecha(@Param("fechaConsulta") LocalDateTime fechaConsulta,@Param("fechaSgte") LocalDateTime fechaSgte);

	//PROCEDURE SIEMPRE DEVOLVERA UNA LISTA DE OBJETOS
	@Query(value="select * from fn_listarresumen()", nativeQuery = true)
	List<Object[]> listarResumen();
}
