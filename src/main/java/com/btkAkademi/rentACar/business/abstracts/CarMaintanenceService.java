package com.btkAkademi.rentACar.business.abstracts;

import java.util.List;

import com.btkAkademi.rentACar.business.dtos.CarMaintanenceListDto;
import com.btkAkademi.rentACar.business.requests.carInServiceRequests.CreateCarMaintenanceRequest;
import com.btkAkademi.rentACar.business.requests.carInServiceRequests.UpdateCarMaintenanceRequest;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;




public interface CarMaintanenceService {
	
	DataResult<List<CarMaintanenceListDto>> getAll();
	Result add(CreateCarMaintenanceRequest createCarMaintenanceRequest);
	Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest);
	Result delete(int id);
	DataResult<CarMaintanenceListDto> getByCarMaintanenceId(int carMaintanenceId);
	boolean checkIfCarIsInMaintanance (int carId) ;
}
