package com.btkAkademi.rentACar.core.utilities.adapters;

import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessResult;

import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.services.IsBankService;

@Service
public class IsBankServiceAdapter implements BankAdapterService{

	@Override
	public Result checkIfLimitIsEnough(String cardNo, String day, String mounth, String cVV, double amount) {
		IsBankService isBankService = new IsBankService();
		if( isBankService.isLimitExists(cardNo,day,mounth,cVV,amount)) {
			return new SuccessResult();
		}else {	
			return new ErrorResult(Messages.limitNotEnough);
			}
	}






}
