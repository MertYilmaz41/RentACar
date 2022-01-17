package com.btkAkademi.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.CorporateCustomerService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.dtos.CorporateListDto;
import com.btkAkademi.rentACar.business.requests.corporateRequests.CreateCorporateRequest;
import com.btkAkademi.rentACar.business.requests.corporateRequests.UpdateCorporateRequest;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorDataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessResult;
import com.btkAkademi.rentACar.dataAccess.abstracts.CorporateCustomerDao;
import com.btkAkademi.rentACar.entities.concretes.CorporateCustomer;

@Service
public class CorporateCustomerManager implements CorporateCustomerService{
	private CorporateCustomerDao corporateCustomerDao;
	private ModelMapperService modelMapperService;
	
	@Autowired
	public CorporateCustomerManager(CorporateCustomerDao corporateCustomerDao, ModelMapperService modelMapperService) {
		this.corporateCustomerDao = corporateCustomerDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<CorporateListDto>> getAll() {
		List<CorporateCustomer> corporateCustomerList = this.corporateCustomerDao.findAll();
		List<CorporateListDto> response = corporateCustomerList.stream()
				.map(corporate -> modelMapperService.forDto()
				.map(corporate, CorporateListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<CorporateListDto>>(response);
	}

	
	@Override
	public Result add(CreateCorporateRequest createCorporateRequest) {
		Result result =  BusinessRules.run(
				checkIfEmailExist(createCorporateRequest.getEmail()),
				checkIfCompanyNameExist(createCorporateRequest.getCompanyName())
				);
		
		if(result!=null) 
		{
			return result;
		}
		
		CorporateCustomer corporateCustomer = this.modelMapperService.forRequest().map(createCorporateRequest, CorporateCustomer.class);
		this.corporateCustomerDao.save(corporateCustomer);
		return new SuccessResult(Messages.corporateCustomerAdded);
	}
	
	@Override
	public Result update(UpdateCorporateRequest updateCorporateRequest) {
		
		Result result = BusinessRules.run(
				checkIfCorporateCustomerIdExist(updateCorporateRequest.getCorporateId()),
				checkIfCompanyNameExist(updateCorporateRequest.getCompanyName()),
				checkIfEmailExist(updateCorporateRequest.getEmail())
				);
		
		if(result!=null) 
		{
			return result;
			
		}
		
		CorporateCustomer corporateCustomer = this.modelMapperService.forRequest().map(updateCorporateRequest, CorporateCustomer.class);
		this.corporateCustomerDao.save(corporateCustomer);
		return new SuccessResult(Messages.corporateCustomerUpdated);
	}
	
	@Override
	public Result delete(int id) {
		if(this.corporateCustomerDao.existsById(id)) 
		{
			this.corporateCustomerDao.deleteById(id);
			return new SuccessResult(Messages.corporateCustomerDeleted);
		}
		else 
		{
			return new ErrorResult();
		}
	}
	
	@Override
	public DataResult<CorporateListDto> getByCorporateCustomerId(int corporateCustomerId) {
		if (corporateCustomerDao.existsById(corporateCustomerId)) {
			CorporateCustomer corporateCustomer = this.corporateCustomerDao.findById(corporateCustomerId).get();
			CorporateListDto response = modelMapperService.forDto().map(corporateCustomer, CorporateListDto.class);
			return new SuccessDataResult<CorporateListDto>(response);
		} else
			return new ErrorDataResult<>();
	}

	
	private Result checkIfEmailExist(String email) 
	{
		if(this.corporateCustomerDao.findByEmail(email)!=null) 
		{
			return new ErrorResult(Messages.emailExist);
		}
		return new SuccessResult();
	}
	
	private Result checkIfCompanyNameExist(String companyName) 
	{
		if(this.corporateCustomerDao.findByCompanyName(companyName)!=null) 
		{
			return new ErrorResult(Messages.companyNameExists);
		}
		return new SuccessResult();
	}
	
	private Result checkIfCorporateCustomerIdExist(int id) 
	{
		if(!this.corporateCustomerDao.existsById(id))
		{
			return new ErrorResult(Messages.corporateCustomerIdNotExists);
		}
		return new SuccessResult();
	}







}
