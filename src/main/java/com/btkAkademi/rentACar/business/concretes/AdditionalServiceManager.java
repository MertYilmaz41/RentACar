package com.btkAkademi.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.IAdditionalService;
import com.btkAkademi.rentACar.business.abstracts.RentalService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.dtos.AdditionalServiceListDto;
import com.btkAkademi.rentACar.business.requests.additionalServiceRequests.CreateAdditionalServiceRequest;
import com.btkAkademi.rentACar.business.requests.additionalServiceRequests.UpdateAdditionalServiceRequest;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorDataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessResult;
import com.btkAkademi.rentACar.dataAccess.abstracts.AdditionalServiceDao;
import com.btkAkademi.rentACar.entities.concretes.AdditionalService;



@Service
public class AdditionalServiceManager implements IAdditionalService{
	private AdditionalServiceDao additionalServiceDao;
	private ModelMapperService modelMapperService;
	private RentalService rentalService;

	@Autowired
	public AdditionalServiceManager(AdditionalServiceDao additionalServiceDao, ModelMapperService modelMapperService,@Lazy RentalService rentalService) {
		super();
		this.additionalServiceDao = additionalServiceDao;
		this.modelMapperService = modelMapperService;
		this.rentalService = rentalService;
	}

	@Override
	public DataResult<List<AdditionalServiceListDto>> getAll() {
		List<AdditionalService> carList = this.additionalServiceDao.findAll();
		List<AdditionalServiceListDto> response = carList.stream()
				.map(car->modelMapperService.forDto()
				.map(car, AdditionalServiceListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<AdditionalServiceListDto>>(response);
	}
	
	@Override
	public DataResult<List<AdditionalServiceListDto>> getAllByRentalId(int rentalId) {
		List<AdditionalService> additionalServiceList = this.additionalServiceDao.getAllByRentalId(rentalId);
		List<AdditionalServiceListDto> response = additionalServiceList.stream()
				.map(additionalService -> modelMapperService.forDto()
						.map(additionalService, AdditionalServiceListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<AdditionalServiceListDto>>(response);
	}

	
	@Override
	public Result add(CreateAdditionalServiceRequest createAdditionalServiceRequest) {
		Result result = BusinessRules.run(
				checkIfRentalExists(createAdditionalServiceRequest.getRentalId())
				
				);
		
		if(result!=null) 
		{
			return result;
		}
		
		AdditionalService additionalService = this.modelMapperService.forRequest().map(createAdditionalServiceRequest, AdditionalService.class);
		this.additionalServiceDao.save(additionalService);
		return new SuccessResult(Messages.additionalServiceAdded);
		
	}

	@Override
	public Result update(UpdateAdditionalServiceRequest updateAdditionalServiceRequest) {
		
		Result result = BusinessRules.run(
				checkIfRentalExists(updateAdditionalServiceRequest.getRentalId())
				);
		
		if(result!=null) 
		{
			return result;
			
			
		}
		AdditionalService additionalService = this.modelMapperService.forRequest().map(updateAdditionalServiceRequest, AdditionalService.class);
		this.additionalServiceDao.save(additionalService);
		return new SuccessResult(Messages.additionalServiceUpdated);
	}
	
	@Override
	public Result delete(int id) {
		if(this.additionalServiceDao.existsById(id)) 
		{
			this.additionalServiceDao.deleteById(id);
			return new SuccessResult(Messages.additionalServiceDeleted);
		}
		else 
		{
			return new ErrorResult();
		}
	}

	
	@Override
	public DataResult<AdditionalServiceListDto> getByAdditionalServiceId(int additionalServiceId) {
		
		if (additionalServiceDao.existsById(additionalServiceId)) {
			AdditionalService additionalService = this.additionalServiceDao.findById(additionalServiceId).get();
			AdditionalServiceListDto response = modelMapperService.forDto().map(additionalService, AdditionalServiceListDto.class);
			return new SuccessDataResult<AdditionalServiceListDto>(response);
		} else
			return new ErrorDataResult<>();
	}
	
	//private Result checkIfAdditionalServiceExists(String additionalServiceName) 
	//{
		//if(this.additionalServiceDao.findByName(additionalServiceName)!=null) 
		//{
			//return new ErrorResult(Messages.additionalServiceExists);
		//}
		//return new SuccessResult();
	//}
	
	private Result checkIfRentalExists(int rentalId) 
	{
		if(this.rentalService.getRentalById(rentalId)!=null) 
		{
			return new SuccessResult();
		}
		return new ErrorResult();
	}







}
