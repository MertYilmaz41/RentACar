package com.btkAkademi.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.CityService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.dtos.CityListDto;
import com.btkAkademi.rentACar.business.requests.cityRequests.CreateCityRequest;
import com.btkAkademi.rentACar.business.requests.cityRequests.UpdateCityRequest;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorDataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessResult;
import com.btkAkademi.rentACar.dataAccess.abstracts.CityDao;
import com.btkAkademi.rentACar.entities.concretes.City;

@Service
public class CityManager implements CityService{
	
	private CityDao cityDao;
	private ModelMapperService modelMapperService;

	@Autowired
	public CityManager(CityDao cityDao, ModelMapperService modelMapperService) {
		super();
		this.cityDao = cityDao;
		this.modelMapperService = modelMapperService;
	}

	
	@Override
	public DataResult<List<CityListDto>> getAll() {
		List<City> carList = this.cityDao.findAll();
		List<CityListDto> response = carList.stream()
				.map(city->modelMapperService.forDto()
				.map(city, CityListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<CityListDto>>(response);
	
	}
	
	@Override
	public Result add(CreateCityRequest createCityRequest) {
		
		Result result = BusinessRules.run();
		
		if(result!=null) 
		{
			return result;
				
		}
		
		City city = this.modelMapperService.forRequest().map(createCityRequest, City.class);
		this.cityDao.save(city);
		return new SuccessResult(Messages.cityAdded);
	
	}

	@Override
	public Result update(UpdateCityRequest updateCityRequest) {
		
		Result result = BusinessRules.run(
				checkIfCityIdExist(updateCityRequest.getCityId()))
				;
		
		if(result!=null) 
		{
			return result;
				
		}
		
		City city = this.modelMapperService.forRequest().map(updateCityRequest, City.class);
		this.cityDao.save(city);
		return new SuccessResult(Messages.cityUpdated);
	}
	
	@Override
	public Result delete(int id) {
		if(this.cityDao.existsById(id)) 
		{
			this.cityDao.deleteById(id);
			return new SuccessResult(Messages.cityDeleted);
		}
		else 
		{
			return new ErrorResult();
		}
	}
	
	@Override
	public DataResult<CityListDto> getByCityId(int cityId) {
		if (cityDao.existsById(cityId)) {
			City city = this.cityDao.findById(cityId).get();
			CityListDto response = modelMapperService.forDto().map(city, CityListDto.class);
			return new SuccessDataResult<CityListDto>(response);
		} else
			return new ErrorDataResult<>();
	
	}
	
	private Result checkIfCityIdExist(int id) 
	{
		if(!this.cityDao.existsById(id))
		{
			return new ErrorResult(Messages.cityIdNotExists);
		}
		return new SuccessResult();
	}













}
