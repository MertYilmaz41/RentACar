package com.btkAkademi.rentACar.core.utilities.adapters.CorporateCustomer;

import com.btkAkademi.rentACar.core.utilities.results.DataResult;

public interface CorporateCustomerFindexScoreAdapter {
	DataResult<Integer> checkFindexScore(String taxNumber);
}
