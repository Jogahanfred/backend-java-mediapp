package com.jogahanfred.controller;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jogahanfred.dto.ConsultaListaExamenDTO;
import com.jogahanfred.dto.ConsultaResumenDTO;
import com.jogahanfred.dto.FiltroConsultaDTO;
import com.jogahanfred.exception.ModeloNotFoundException;
import com.jogahanfred.model.Archivo;
import com.jogahanfred.model.Consulta;
import com.jogahanfred.service.IArchivoService;
import com.jogahanfred.service.IConsultaService;

//HATEOAS
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

	@Autowired
	private IConsultaService consultaService;
	
	@Autowired
	private IArchivoService archivoService;

	@GetMapping
	public ResponseEntity<List<Consulta>> listar() throws Exception {
		List<Consulta> lista = consultaService.listar();
		return new ResponseEntity<List<Consulta>>(lista, HttpStatus.OK);

	}

	// @Valid para que se respete los constrains
	// con @PATHVARIBLE recepcionas el id que viene con la url
	@GetMapping("/{id}")
	public ResponseEntity<Consulta> listarPorId(@PathVariable("id") Integer id) throws Exception {
		Consulta consulta = consultaService.listarPorId(id);
		if (consulta == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}

		return new ResponseEntity<Consulta>(consulta, HttpStatus.OK);

	}

	// Forma 3, nivel 3 Richardson / para mostrar un bloque de localizacion href
	@GetMapping("/hateoas/{id}")
	public EntityModel<Consulta> listarPorIdHateoas(@PathVariable("id") Integer id) throws Exception {
		Consulta consulta = consultaService.listarPorId(id);
		if (consulta == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}

		EntityModel<Consulta> recurso = EntityModel.of(consulta);
		//THIS.CLASS() SE REFIERE A LA CLASE QUE HACE REFERENCIA, EN ESTE CASO A MEDICOCONTROLLER.
		//LUEGO LISTARPORID(ID) SE REFIERE AL METODO LISTAR... Y LA DEVOLUCION DE ESTE
		//TODO ELLO SE GUARDA EN LINK 
		// localhost:8080/consultas/18
		//HATEOAS
		//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
		//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
		//PARA EVITAR ESTAR LLAMANDO "WebMvcLinkBuilder.methodOn"
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).listarPorId(id));

		recurso.add(link.withRel("consulta-recurso"));
		return recurso;

	}

	// (JACKSON)con @REQUESTBODY serializa el JSON a Objeto java
	/*
	 * Forma 1,nivel 1 Richardson / sin la localizacion
	 * 
	 * @PostMapping public ResponseEntity<Consulta> registrar(@Valid @RequestBody
	 * Consulta p) throws Exception { Consulta consulta =
	 * consultaService.registrar(p); return new ResponseEntity<Consulta>(consulta,
	 * HttpStatus.CREATED); }
	 */
	// Forma 2, nivel 2 Richardson / con la localizacion
	@PostMapping
	public ResponseEntity<Consulta> registrar(@Valid @RequestBody ConsultaListaExamenDTO c) throws Exception {
		Consulta consulta = consultaService.registrarTransaccional(c);
		// Esto "ServletUriComponentsBuilder.fromCurrentRequest()" obtiene la url de la
		// peticion actual.
		// ".PATH" es para agregar el id a la url actual, id que se obtiene del modelo
		// PACIENTE que esta ingresando
		// localhost:8080/examenes({2}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(consulta.getIdConsulta()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping
	public ResponseEntity<Consulta> modificar(@Valid @RequestBody Consulta c) throws Exception {
		Consulta consulta = consultaService.modificar(c);
		return new ResponseEntity<Consulta>(consulta, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception {
		Consulta consulta = consultaService.listarPorId(id);
		if (consulta == null) {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}
		consultaService.eliminar(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

	}
	
	//Cuando los parametros vienen en String en la url /buscar?fecha='05/07/1996', se utiliza el @RequestParam 

	@GetMapping("/buscar")	
	public ResponseEntity<List<Consulta>> buscarFecha(@RequestParam(value = "fecha")  String fecha) {		
		List<Consulta> consultas = new ArrayList<>();	
		consultas = consultaService.buscarFecha(LocalDateTime.parse(fecha));						
		return new ResponseEntity<List<Consulta>>(consultas, HttpStatus.OK);
	}
	
	@PostMapping("/buscar/otros")
	public ResponseEntity<List<Consulta>> buscarOtro(@RequestBody FiltroConsultaDTO filtro) {		
		List<Consulta> consultas = new ArrayList<>();
		
		consultas = consultaService.buscar(filtro);			
		
		return new ResponseEntity<List<Consulta>>(consultas, HttpStatus.OK);
	}
	
	@GetMapping(value ="/listarResumen")
	public ResponseEntity<List<ConsultaResumenDTO>> listarResumen(){
		List<ConsultaResumenDTO> consultas = new ArrayList<>();
		consultas = consultaService.listarResumen();
		return new ResponseEntity<List<ConsultaResumenDTO>>(consultas,HttpStatus.OK);
	}
	
	//MediaType.APPLICATION_OCTET_STREAM_VALUE .- recibe y retorna todo binarios "application/octect-str"
	@GetMapping(value = "/generarReporte", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> generarReporte() {
		byte[] data = null;
		data = consultaService.generarReporte();
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}

	//solo espera un archivo
	//MULTIPART_FORM_DATA_VALUE.- ya que vendra un archivo --@RequestParam("adjunto") lo que vendra del front
	@PostMapping(value = "/guardarArchivo", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	//adjunto .- este keyword tiene que hacer math con lo que viene del front
	public ResponseEntity<Integer> guardarArchivo(@RequestParam("adjunto") MultipartFile file) throws IOException {
		// cuando venga mas info como un formulario @RequestPart("medico") Medico medico
		
		//La carga de archivos se puede trabajar en la nube con amazon, cloudinary etc,e implica que se agrege la libreria en el POM
		//para eso enviamos el file a sus servidores y ellos nos devuelven una url en formato string y nosotro guardamos el string
		//String url = aws.sendfile(file) y con la url guardamos en BD
		int rpta = 0;
		
		Archivo ar = new Archivo();
		ar.setFiletype(file.getContentType());//obtenemos si es jpg o png etc y seteamos a la clase archivo
		ar.setFilename(file.getOriginalFilename());//obtenemos el nombre del archivo y seteamos a la clase archivo
		ar.setValue(file.getBytes());//obtenemos el arreglo de bytes y seteamos a la clase archivo
		
		rpta = archivoService.guardar(ar);

		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}
	 
	@GetMapping(value = "/leerArchivo/{idArchivo}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> leerArchivo(@PathVariable("idArchivo") Integer idArchivo) throws IOException {
				
		byte[] arr = archivoService.leerArchivo(idArchivo); 

		return new ResponseEntity<byte[]>(arr, HttpStatus.OK);
	}
}

