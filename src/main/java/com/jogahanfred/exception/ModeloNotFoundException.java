package com.jogahanfred.exception;


//@ResponseStatus(HttpStatus.NOT_FOUND)
public class ModeloNotFoundException extends RuntimeException{
 

	public ModeloNotFoundException(String message) {
		super(message); 
	}

	 
}		
