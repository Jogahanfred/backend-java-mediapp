package com.jogahanfred.service.impl;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.jogahanfred.dto.ConsultaListaExamenDTO;
import com.jogahanfred.dto.ConsultaResumenDTO;
import com.jogahanfred.dto.FiltroConsultaDTO;
import com.jogahanfred.model.Consulta;
import com.jogahanfred.repo.IConsultaExamenRepo;
import com.jogahanfred.repo.IConsultaRepo;
import com.jogahanfred.repo.IGenericRepo;
import com.jogahanfred.service.IConsultaService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ConsultaServiceImpl extends CRUDImpl<Consulta, Integer> implements IConsultaService {

	@Autowired
	private IConsultaRepo repo;

	@Autowired
	private IConsultaExamenRepo ceRepo;

	@Override
	protected IGenericRepo<Consulta, Integer> getRepo() {
		return repo;
	}

	// Si algo falla automaticamente acciona el ROLLBACK
	@Transactional
	@Override
	public Consulta registrarTransaccional(ConsultaListaExamenDTO dto) {
		// INSERTAR CONSULTA -> OBTENER PK
		// INSERTAR DETALLE CONSULTA -> USANDO LA PK PREVIA
		dto.getConsulta().getDetalleConsulta().forEach(det -> det.setConsulta(dto.getConsulta()));

		repo.save(dto.getConsulta());
		// Cuando inserto un objeto, su llave primaria esta en 0
		// Despues de insertar el objeto, su llave se establece con un id incremental
		// que le da la BD
		dto.getLstExamen().forEach(ex -> ceRepo.registrar(dto.getConsulta().getIdConsulta(), ex.getIdExamen()));
		return dto.getConsulta();
		/*
		 * List<DetalleConsulta> listaDetalle = consulta.getDetalleConsulta();
		 * for(DetalleConsulta det : listaDetalle) { det.setConsulta(consulta); }
		 */

	}

	@Override
	public List<Consulta> buscar(FiltroConsultaDTO filtro) {
		return repo.buscar(filtro.getDni(), filtro.getNombreCompleto());
	}

	//(fecha, fecha.plusDays(1)) plusDay de la fecha que viene +1
	@Override
	public List<Consulta> buscarFecha(LocalDateTime fecha) {
		return repo.buscarFecha(fecha, fecha.plusDays(1));
	}

	@Override
	public List<ConsultaResumenDTO> listarResumen() {
		//List<Object[]>
		//cantidad fecha
		//[1  , "15/05/2021"]
		//[3  , "11/02/2021"]
		//[5  , "12/09/2021"]
		//Creamos la lista vacia que guardara la info
		//Luego del repo iteramos y colocamos las posiciones al arreglo
		//Final guardamos el objeto listeado en la nueva lista 
		List<ConsultaResumenDTO> consultas = new ArrayList<>();
		repo.listarResumen().forEach(x ->{
			ConsultaResumenDTO cr = new ConsultaResumenDTO();
			//cr.setCantidad(x[0]); Hasta aqui esta devolviendo los valores peroen object , tenemos que castear
			cr.setCantidad(Integer.parseInt(String.valueOf(x[0])));
			cr.setFecha(String.valueOf(x[1]));
			consultas.add(cr);
		});
		
		return consultas;
		
	}

	@Override
	public byte[] generarReporte() {
		//instancia un array vacio
		byte[] data = null;
		//Se tiene que hacer con map<string,object> porque asi lo recibira jasper
		//con el map poblaremos a los parametros del reporte
		//txt_titulo <- parametro keyword
		//Prueba de titulo <- contenido
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("txt_titulo", "Prueba de titulo");

		try {
			//file.getPath() .- url del reporte, referencia de donde se encuentra el reporte
			//new JRBeanCollectionDataSource(this.listarResumen() .- metodo con la clase DTO
			File file = new ClassPathResource("/reports/consultas.jasper").getFile();
			///file.getPath() .- indica cual es la url del reporte
			///parametros.- los parametros
			
			///JRBeanCollectionDataSource.- para que jasper lo pueda interpretar
			///new JRBeanCollectionDataSource(this.listarResumen()).- coleccion que es necesaria para iterar los field de report
			///los atributos de la clase tienen que tener el mismo nombre a los field del reporte 
			JasperPrint print = JasperFillManager.fillReport(file.getPath(), parametros, 
															 new JRBeanCollectionDataSource(this.listarResumen()));
			//JasperExportManager.exportReportToPdf.- Aqui retorna un arreglo de byte
			data = JasperExportManager.exportReportToPdf(print);
			// mitocode jasperreports | excel, pdf, ppt, word, csv
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}	

}
