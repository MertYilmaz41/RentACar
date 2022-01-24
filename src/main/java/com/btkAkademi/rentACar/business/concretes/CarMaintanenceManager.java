package com.btkAkademi.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.entities.concretes.CarMaintanence;
import com.btkAkademi.rentACar.business.abstracts.CarMaintanenceService;
import com.btkAkademi.rentACar.business.abstracts.CarService;
import com.btkAkademi.rentACar.business.abstracts.RentalService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.dtos.CarMaintanenceListDto;
import com.btkAkademi.rentACar.business.requests.carInServiceRequests.CreateCarMaintenanceRequest;
import com.btkAkademi.rentACar.business.requests.carInServiceRequests.UpdateCarMaintenanceRequest;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorDataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessResult;
import com.btkAkademi.rentACar.dataAccess.abstracts.CarMaintenanceDao;


@Service
public class CarMaintanenceManager implements CarMaintanenceService{
	
	private CarMaintenanceDao carMaintenanceDao;
	private ModelMapperService modelMapperService;
	private RentalService rentalService;
	private CarService carService;

	@Autowired
	public CarMaintanenceManager(CarMaintenanceDao carMaintenanceDao, ModelMapperService modelMapperService, @Lazy RentalService rentalService,CarService carService) {
		super();
		this.carMaintenanceDao = carMaintenanceDao;
		this.modelMapperService = modelMapperService;
		this.rentalService = rentalService;
		this.carService = carService;
	}

	@Override
	public DataResult<List<CarMaintanenceListDto>> getAll() {
		List<CarMaintanence> carMaintananceList = this.carMaintenanceDao.findAll();
		List<CarMaintanenceListDto> response = carMaintananceList.stream()
				.map(carMaintanance->modelMapperService.forDto()
				.map(carMaintanance, CarMaintanenceListDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<CarMaintanenceListDto>>(response);
	}
	

	@Override
	public Result add(CreateCarMaintenanceRequest createCarMaintananceRequest) {
		Result result = BusinessRules.run(				
				checkIfCarIsExists(createCarMaintananceRequest.getCarId()),
				//checkIfCarIsRented(createCarMaintananceRequest.getCarId()),
				checkIfMaintanenceEndTimeNull(createCarMaintananceRequest.getCarId())
				);		
		
		if(result!=null) 
		{			
			return result;
		}
		
		CarMaintanence carMaintanance = this.modelMapperService.forRequest()
				.map(createCarMaintananceRequest,CarMaintanence.class);
		carMaintanance.setId(0);
		System.out.println(carMaintanance.getId());
	
		this.carMaintenanceDao.save(carMaintanance);		
		return new SuccessResult(Messages.carMaintanenceAdded);
	}
	
	@Override
	public Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest) {
		
		Result result = BusinessRules.run(
				checkIfCarMaintananceIdExist(updateCarMaintenanceRequest.getCarMaintenanceId())
				);
		
		if(result!=null) 
		{
			return result;
			
		}
		
		CarMaintanence carMaintanence = this.modelMapperService.forRequest().map(updateCarMaintenanceRequest, CarMaintanence.class);
		this.carMaintenanceDao.save(carMaintanence);
		return new SuccessResult(Messages.carMaintananceUpdated);
	}
	
	@Override
	public Result delete(int id) {
		if(this.carMaintenanceDao.existsById(id)) 
		{
			this.carMaintenanceDao.deleteById(id);
			return new SuccessResult(Messages.carMaintananceDeleted);
		}
		else 
		{
			return new ErrorResult();
		}
	}
	
	@Override
	public DataResult<CarMaintanenceListDto> getByCarMaintanenceId(int carMaintanenceId) {
		if (carMaintenanceDao.existsById(carMaintanenceId)) {
			CarMaintanence carMaintanence = this.carMaintenanceDao.findById(carMaintanenceId).get();
			CarMaintanenceListDto response = modelMapperService.forDto().map(carMaintanence, CarMaintanenceListDto.class);
			return new SuccessDataResult<CarMaintanenceListDto>(response);
		} else
			return new ErrorDataResult<>();
	}


	@Override
	public boolean checkIfCarIsInMaintanance(int carId) {
		if(carMaintenanceDao.findByCarIdAndMaintenanceEndTimeIsNull(carId)!=null) {
			return true;
		}
		else return false;
	}

	private Result checkIfCarIsExists(int carId) {
		if(carService.getByCarId(carId).isSuccess()) {
			return new SuccessResult();
		}
		else return new ErrorResult(Messages.carIsNotExists);
	}
	
	private Result checkIfCarIsRented(int carId) {
		if(rentalService.getRentalById(carId)!=null)
		{
			return new ErrorResult(Messages.carRented);
		}
		else return new SuccessResult();
	}
	
	private Result checkIfMaintanenceEndTimeNull(int carId) 
	{
		if(this.carMaintenanceDao.findByCarIdAndMaintenanceEndTimeIsNull(carId)!=null) 
		{
			return new ErrorResult(Messages.carInMaintanance);
		}
		return new SuccessResult();
	}
	
	private Result checkIfCarMaintananceIdExist(int id) 
	{
		if(!this.carMaintenanceDao.existsById(id))
		{
			return new ErrorResult(Messages.carMaintananceIdNotExists);
		}
		return new SuccessResult();
	}






	//
}
