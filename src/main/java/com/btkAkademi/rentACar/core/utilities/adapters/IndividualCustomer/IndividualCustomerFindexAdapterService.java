package com.btkAkademi.rentACar.core.utilities.adapters.IndividualCustomer;

import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.services.IndividualCustomerFindexCheckService;


@Service
public class IndividualCustomerFindexAdapterService implements IndividualCustomerFindexScoreAdapterService{

	@Override
	public DataResult<Integer> getFindexScoreOfIndividualCustomer(String tcNo) {
		IndividualCustomerFindexCheckService individualCustomerFindexCheckService = new IndividualCustomerFindexCheckService();
		
		return new SuccessDataResult<Integer>(individualCustomerFindexCheckService.calculateFindexScoreEnough(tcNo));
		
	}





}
