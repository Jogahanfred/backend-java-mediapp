package com.jogahanfred.repo;

import com.jogahanfred.model.ResetToken;

public interface IResetTokenRepo extends IGenericRepo<ResetToken, Integer>{
	
	//from ResetToken rt where rt.token = :? --> Buscamos por TOKEN
	ResetToken findByToken(String token);

}
