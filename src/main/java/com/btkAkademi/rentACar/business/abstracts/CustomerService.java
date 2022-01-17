package com.btkAkademi.rentACar.business.abstracts;

import java.util.List;

import com.btkAkademi.rentACar.business.dtos.CustomerListDto;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.entities.concretes.Customer;


public interface CustomerService {
	DataResult<List<CustomerListDto>> getAll();
	DataResult<CustomerListDto> getCustomerDtoById(int id);
	DataResult<Customer> findCustomerById(int id);
	
	
}
