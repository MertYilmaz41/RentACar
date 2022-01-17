package com.btkAkademi.rentACar.business.concretes;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.IndividualCustomerService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.dtos.IndividualCustomerListDto;
import com.btkAkademi.rentACar.business.requests.individualCustomerRequests.CreateIndividualCustomerRequest;
import com.btkAkademi.rentACar.business.requests.individualCustomerRequests.UpdateIndividualCustomerRequest;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorDataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessResult;
import com.btkAkademi.rentACar.dataAccess.abstracts.IndividualCustomerDao;
import com.btkAkademi.rentACar.entities.concretes.IndividualCustomer;

@Service
public class IndividualCustomerManager implements IndividualCustomerService{
	private static final int ageLimit=18;
	
	private IndividualCustomerDao individualCustomerDao;
	private ModelMapperService modelMapperService;
	
	@Autowired
	public IndividualCustomerManager(IndividualCustomerDao individualCustomerDao
			,ModelMapperService modelMapperService) 
	{
		this.individualCustomerDao = individualCustomerDao;
		this.modelMapperService = modelMapperService;
	}
	
	@Override
	public DataResult<List<IndividualCustomerListDto>> getAll() {
		List<IndividualCustomer> individualCustomerList = this.individualCustomerDao.findAll();
		List<IndividualCustomerListDto> response = individualCustomerList.stream()
				.map(individualCustomer->modelMapperService.forDto()
				.map(individualCustomer, IndividualCustomerListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<IndividualCustomerListDto>>(response);
	}
	

	@Override
	public Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest) {
		Result result = BusinessRules.run(
				checkIfEmailExist(createIndividualCustomerRequest.getEmail()),
				checkIsIndividualCustomerUnderage(createIndividualCustomerRequest.getBirthDate())
				);
		
		if (result !=null) 
		{
			return result;
		}
		IndividualCustomer individualCustomer = modelMapperService.forRequest().map(createIndividualCustomerRequest, IndividualCustomer.class);
		this.individualCustomerDao.save(individualCustomer);
		return new SuccessResult(Messages.individualCustomerAdded);
	}
	
	
	@Override
	public Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest) {
		
		Result result = BusinessRules.run(
				checkIfIndividualCustomerIdExist(updateIndividualCustomerRequest.getIndividualCustomerId()),
				checkIfEmailExist(updateIndividualCustomerRequest.getEmail())
				
				);
		
		if(result!=null) 
		{
			return result;
			
		}
		
		IndividualCustomer individualCustomer = this.modelMapperService.forRequest().map(updateIndividualCustomerRequest, IndividualCustomer.class);
		this.individualCustomerDao.save(individualCustomer);
		return new SuccessResult(Messages.individualCustomerUpdated);
	}
	
	@Override
	public Result delete(int id) {
		if(this.individualCustomerDao.existsById(id)) 
		{
			this.individualCustomerDao.deleteById(id);
			return new SuccessResult(Messages.individualCustomerDeleted);
		}
		else 
		{
			return new ErrorResult();
		}
	}
	
	@Override
	public DataResult<IndividualCustomerListDto> getByIndividualCustomerId(int individualCustomerId) {
		if (individualCustomerDao.existsById(individualCustomerId)) {
			IndividualCustomer individualCustomer = this.individualCustomerDao.findById(individualCustomerId).get();
			IndividualCustomerListDto response = modelMapperService.forDto().map(individualCustomer, IndividualCustomerListDto.class);
			return new SuccessDataResult<IndividualCustomerListDto>(response);
		} else
			return new ErrorDataResult<>();
	}

	
	private Result checkIfEmailExist(String email) 
	{
		if(this.individualCustomerDao.findByEmail(email)!=null) 
		{
			return new ErrorResult(Messages.emailExist);
		}
		
		return new SuccessResult();
	}
	
	
	private Result checkIsIndividualCustomerUnderage(LocalDate birthDate) 
	{
		int Age = Period.between(birthDate, LocalDate.now()).getYears();
		if(Age<ageLimit ) {
			return new ErrorResult(Messages.ageNotInLimit);
		}
		return new SuccessResult();
	}
	
	
	private Result checkIfIndividualCustomerIdExist(int id) 
	{
		if(!this.individualCustomerDao.existsById(id))
		{
			return new ErrorResult(Messages.individualCustomerIdNotExists);
		}
		return new SuccessResult();
	}







}
