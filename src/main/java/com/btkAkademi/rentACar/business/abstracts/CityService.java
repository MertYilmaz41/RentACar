package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;

import java.util.List;

import com.btkAkademi.rentACar.business.dtos.CityListDto;
import com.btkAkademi.rentACar.business.requests.cityRequests.CreateCityRequest;
import com.btkAkademi.rentACar.business.requests.cityRequests.UpdateCityRequest;

public interface CityService {
	DataResult<List<CityListDto>> getAll();
	Result add(CreateCityRequest createCityRequest);
	Result update(UpdateCityRequest updateCityRequest);
	Result delete(int id);
	DataResult<CityListDto> getByCityId(int cityId);
	
}
