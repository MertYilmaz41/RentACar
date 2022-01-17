package com.btkAkademi.rentACar.business.abstracts;

import java.util.List;

import com.btkAkademi.rentACar.business.dtos.CorporateListDto;
import com.btkAkademi.rentACar.business.requests.corporateRequests.CreateCorporateRequest;
import com.btkAkademi.rentACar.business.requests.corporateRequests.UpdateCorporateRequest;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;



public interface CorporateCustomerService {
	DataResult<List<CorporateListDto>> getAll();
	Result add(CreateCorporateRequest createCorporateRequest);
	Result update(UpdateCorporateRequest updateCorporateRequest);
	Result delete(int id);
	DataResult<CorporateListDto> getByCorporateCustomerId(int corporateCustomerId);
}
