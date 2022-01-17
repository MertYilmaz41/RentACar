package com.btkAkademi.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.CarDamageService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.dtos.CarDamageListDto;
import com.btkAkademi.rentACar.business.requests.carDamageRequests.CreateCarDamageRequest;
import com.btkAkademi.rentACar.business.requests.carDamageRequests.UpdateCarDamageRequest;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorDataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessResult;
import com.btkAkademi.rentACar.dataAccess.abstracts.CarDamageDao;
import com.btkAkademi.rentACar.entities.concretes.CarDamage;

@Service
public class CarDamageManager implements CarDamageService{
	private CarDamageDao carDamageDao;
	private ModelMapperService modelMapperService;

	@Autowired
	public CarDamageManager(CarDamageDao carDamageDao, ModelMapperService modelMapperService) {
		super();
		this.carDamageDao = carDamageDao;
		this.modelMapperService = modelMapperService;
	}
	
	@Override
	public DataResult<List<CarDamageListDto>> getAll() {
		List<CarDamage> carList = this.carDamageDao.findAll();
		List<CarDamageListDto> response = carList.stream()
				.map(car -> modelMapperService.forDto()
				.map(car, CarDamageListDto.class))	
				.collect(Collectors.toList());
		return new SuccessDataResult<List<CarDamageListDto>>(response);
	}

	@Override
	public Result add(CreateCarDamageRequest createCarDamageRequest) {
		CarDamage carDamage = this.modelMapperService.forRequest().map(createCarDamageRequest, CarDamage.class);
		this.carDamageDao.save(carDamage);
		return new SuccessResult(Messages.carDamageAdded);
		
	}


	@Override
	public Result update(UpdateCarDamageRequest updateCarDamageRequest) {
		
		Result result = BusinessRules.run(
				checkIfCarDamageIdExist(updateCarDamageRequest.getCarDamageId())
				
				);
		
		if(result!=null) 
		{
			return result;
				
		}
		
		CarDamage carDamage = this.modelMapperService.forRequest().map(updateCarDamageRequest, CarDamage.class);
		this.carDamageDao.save(carDamage);
		return new SuccessResult(Messages.carDamageUpdated);
	}
	
	@Override
	public Result delete(int id) {
		if(this.carDamageDao.existsById(id)) 
		{
			this.carDamageDao.deleteById(id);
			return new SuccessResult(Messages.carDamageDeleted);
		}
		else 
		{
			return new ErrorResult();
		}
	}
	
	@Override
	public DataResult<CarDamageListDto> getByCarDamageId(int carDamageId) {
		if (carDamageDao.existsById(carDamageId)) {
			CarDamage carDamage = this.carDamageDao.findById(carDamageId).get();
			CarDamageListDto response = modelMapperService.forDto().map(carDamage, CarDamageListDto.class);
			return new SuccessDataResult<CarDamageListDto>(response);
		} else
			return new ErrorDataResult<>();
	}

	
	private Result checkIfCarDamageIdExist(int id) 
	{
		if(!this.carDamageDao.existsById(id))
		{
			return new ErrorResult(Messages.CarDamageIdNotExists);
		}
		return new SuccessResult();
	}




}
