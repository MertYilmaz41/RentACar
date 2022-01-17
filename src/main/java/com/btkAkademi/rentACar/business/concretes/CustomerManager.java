package com.btkAkademi.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.CustomerService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.dtos.CustomerListDto;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorDataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.dataAccess.abstracts.CustomerDao;
import com.btkAkademi.rentACar.entities.concretes.Customer;

@Service
public class CustomerManager implements CustomerService{
	
	private CustomerDao customerDao;
	private ModelMapperService modelMapperService;
	
	
	@Autowired
	public CustomerManager(CustomerDao customerDao) {
		super();
		this.customerDao = customerDao;
	}
	
	@Override
	public DataResult<List<CustomerListDto>> getAll() {
		List<Customer> customerList = this.customerDao.findAll();
		List<CustomerListDto> response = customerList.stream()
				.map(customer -> modelMapperService.forDto()
				.map(customer, CustomerListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<CustomerListDto>>(response);
	}

	
	@Override
	public DataResult<CustomerListDto> getCustomerDtoById(int id) {
		if (customerDao.existsById(id)) {
			Customer customer = this.customerDao.findById(id).get();
			CustomerListDto response = modelMapperService.forDto().map(customer, CustomerListDto.class);
			return new SuccessDataResult<CustomerListDto>(response);
		} else
			return new ErrorDataResult<>();
				
	}

	@Override
	public DataResult<Customer> findCustomerById(int id) 
	{
		if(!customerDao.existsById(id)) 
		{
			return new ErrorDataResult<Customer>(customerDao.findById(id).get(), Messages.customerNotFound); 
		}
		return new SuccessDataResult<Customer>();
	}



}
