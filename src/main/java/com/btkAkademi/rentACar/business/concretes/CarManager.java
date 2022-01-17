package com.btkAkademi.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.CarService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.dtos.CarListDto;
import com.btkAkademi.rentACar.business.requests.carRequests.CreateCarRequest;
import com.btkAkademi.rentACar.business.requests.carRequests.UpdateCarRequest;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorDataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessResult;
import com.btkAkademi.rentACar.dataAccess.abstracts.CarDao;
import com.btkAkademi.rentACar.entities.concretes.Car;



@Service
public class CarManager implements CarService{

	private CarDao carDao;
	private ModelMapperService modelMapperService;
	
	
	@Autowired
	public CarManager(CarDao carDao, ModelMapperService modelMapperService) {
		this.carDao = carDao;
		this.modelMapperService = modelMapperService;
	}


	@Override
	public DataResult<List<CarListDto>> getAll() {
		List<Car> carList = this.carDao.findAll();
		List<CarListDto> response = carList.stream()
				.map(car->modelMapperService.forDto()
				.map(car, CarListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<CarListDto>>(response);
		
	}


	@Override
	public Result add(CreateCarRequest createCarRequest) {
		
		Result result = BusinessRules.run();
		
		if(result!=null) 
		{
			return result;
			
		}
		
		Car car = this.modelMapperService.forRequest().map(createCarRequest,Car.class);
		this.carDao.save(car);
		return new SuccessResult(Messages.carAdded);
		
	}


	@Override
	public Result update(UpdateCarRequest updateCarRequest) {
		
		Result result = BusinessRules.run(
				checkIfCarIdExist(updateCarRequest.getCarId())
				);
		
		if(result!=null) 
		{
			return result;
			
			
		}
		
		Car car = this.modelMapperService.forRequest().map(updateCarRequest, Car.class);
		this.carDao.save(car);
		return new SuccessResult(Messages.carUpdated);
		
	}
	
	@Override
	public Result delete(int id) {
		if(this.carDao.existsById(id)) 
		{
			this.carDao.deleteById(id);
			return new SuccessResult(Messages.carDeleted);
		}
		else 
		{
			return new ErrorResult();
		}
	}

	@Override
	public DataResult<CarListDto> getByCarId(int carId) {
		if (carDao.existsById(carId)) {
			Car car = this.carDao.findById(carId).get();
			CarListDto response = modelMapperService.forDto().map(car, CarListDto.class);
			return new SuccessDataResult<CarListDto>(response);
		} else
			return new ErrorDataResult<>();
		
	}
	
	private Result checkIfCarIdExist(int id) 
	{
		if(!this.carDao.existsById(id))
		{
			return new ErrorResult(Messages.carIdNotExists);
		}
		return new SuccessResult();
	}



	
	//private Result checkIfCarExist(int id) 
	//{
		//if(this.carDao.existsById(id)) 
		//{
			//return new ErrorResult(Messages.carAdded);
		//}
		//return new SuccessResult();
	//}

}
