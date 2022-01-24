package com.btkAkademi.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.CarService;
import com.btkAkademi.rentACar.business.abstracts.RentalService;
import com.btkAkademi.rentACar.business.abstracts.SegmentService;
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
	private SegmentService segmentService;
	private RentalService rentalService;
	
	
	@Autowired
	public CarManager(CarDao carDao, 
			ModelMapperService modelMapperService, 
			SegmentService segmentService, 
			@Lazy RentalService rentalService) {
		this.carDao = carDao;
		this.modelMapperService = modelMapperService;
		this.segmentService = segmentService;
		this.rentalService = rentalService;
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
		
		Result result = BusinessRules.run(
				checkIfSegmentIdExists(createCarRequest.getSegmentId())
			
				);
		
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
		
		
		Result result = BusinessRules.run(
				checkIfCarIsInRental(id)
				
				);
		
		
		if(result!=null) 
		{
			return result;
		}
		
		
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
		if(this.carDao.existsById(id))
		{
			return new SuccessResult();	
		}
		
		return new ErrorResult(Messages.carIdNotExists);
	}


	@Override
	public DataResult<List<Integer>> findAvailableCarsBySegmentId(int segmentId) {
		if(carDao.findAvailableCarBySegment(segmentId).size()<1) {
			return new ErrorDataResult<List<Integer>>();
		}else return new SuccessDataResult<List<Integer>>(carDao.findAvailableCarBySegment(segmentId));
	}
	
	private Result checkIfSegmentIdExists(int id) 
	{	
		if(segmentService.getBySegmentId(id)!=null) 
		{
			return new SuccessResult();
		}
		return new ErrorResult(Messages.segmentIdNotExists);
	}

	
	private Result checkIfCarIsInRental(int carId) 
	{
		if(this.rentalService.getRentalById(carId)!=null)
		{
			return new ErrorResult(Messages.carIsInRental);
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
