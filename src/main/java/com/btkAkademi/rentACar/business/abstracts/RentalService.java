package com.btkAkademi.rentACar.business.abstracts;

import java.util.List;

import com.btkAkademi.rentACar.business.dtos.RentalListDto;
import com.btkAkademi.rentACar.business.requests.rentalRequests.CreateRentalRequestForCorporate;
import com.btkAkademi.rentACar.business.requests.rentalRequests.CreateRentalRequestForIndividual;
import com.btkAkademi.rentACar.business.requests.rentalRequests.UpdateRentalRequest;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;


public interface RentalService {
	DataResult<List<RentalListDto>> getAll();
	Result add(CreateRentalRequestForIndividual createRentalForIndividualRequest);	
	Result add(CreateRentalRequestForCorporate createRentalRequestForCorporate);
	Result update(UpdateRentalRequest updateRentalRequest);
	Result delete(int id);
	DataResult<RentalListDto> getRentalById(int id);
	boolean isCarRented(int carId);
	
	
	

}
