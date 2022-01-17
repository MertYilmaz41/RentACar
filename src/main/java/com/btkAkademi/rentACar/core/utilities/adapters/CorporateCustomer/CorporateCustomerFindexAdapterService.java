package com.btkAkademi.rentACar.core.utilities.adapters.CorporateCustomer;

import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.services.CorporateCustomerFindexCheckService;


@Service
public class CorporateCustomerFindexAdapterService implements CorporateCustomerFindexScoreAdapter{


	@Override
	public DataResult<Integer> checkFindexScore(String taxNumber) {
		CorporateCustomerFindexCheckService corporateCustomerFindexCheckService = new CorporateCustomerFindexCheckService();
	
		return new SuccessDataResult<Integer>(corporateCustomerFindexCheckService.calculateFindexScoreEnough(taxNumber));
	}


}
