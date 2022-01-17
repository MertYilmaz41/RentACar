package com.btkAkademi.rentACar.business.abstracts;

import java.util.List;

import com.btkAkademi.rentACar.business.dtos.AdditionalServiceListDto;
import com.btkAkademi.rentACar.business.requests.additionalServiceRequests.CreateAdditionalServiceRequest;
import com.btkAkademi.rentACar.business.requests.additionalServiceRequests.UpdateAdditionalServiceRequest;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;



public interface IAdditionalService {
	DataResult<List<AdditionalServiceListDto>> getAll();
	DataResult<List<AdditionalServiceListDto>> getAllByRentalId(int rentalId);
	Result add(CreateAdditionalServiceRequest createAdditionalServiceRequest);
	Result update(UpdateAdditionalServiceRequest updateAdditionalServiceRequest);
	Result delete(int id);
	DataResult<AdditionalServiceListDto> getByAdditionalServiceId(int additionalServiceId);
}
