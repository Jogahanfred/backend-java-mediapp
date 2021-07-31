package com.jogahanfred.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

@RestController
@RequestMapping("/utils")
public class UtilController {

//Se inyecta para poder manipular el beans LocalResolver de la clase MessageConfig	
	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private HttpServletResponse httpServletResponse;

	// Cambiar idioma
	@GetMapping("/locale/{loc}")
	public ResponseEntity<Void> changeLocale(@PathVariable("loc") String loc) {
		Locale userlLocale = null;

		switch (loc) {
		case "en":
			userlLocale = Locale.ENGLISH;
			break;
		case "fr":
			userlLocale = Locale.FRENCH;
			break;
		default:
			userlLocale = Locale.ROOT;
			break;
		}
		localeResolver.setLocale(httpServletRequest, httpServletResponse, userlLocale);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
