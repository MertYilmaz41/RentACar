package com.btkAkademi.rentACar.core.utilities.adapters.IndividualCustomer;

import com.btkAkademi.rentACar.core.utilities.results.DataResult;

public interface IndividualCustomerFindexScoreAdapterService {
	DataResult<Integer> getFindexScoreOfIndividualCustomer(String tcNo);
}
