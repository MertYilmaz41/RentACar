package com.btkAkademi.rentACar.core.utilities.adapters;

import com.btkAkademi.rentACar.core.utilities.results.Result;

public interface BankAdapterService {
	Result checkIfLimitIsEnough(String cardNo,String day,String year,String cVV,double amount);
}
