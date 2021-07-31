package com.jogahanfred.service;

import com.jogahanfred.model.ResetToken;

public interface IResetTokenService {

	//buscar un token
	ResetToken findByToken(String token);
	
	void guardar(ResetToken token);
	
	void eliminar(ResetToken token);

}
