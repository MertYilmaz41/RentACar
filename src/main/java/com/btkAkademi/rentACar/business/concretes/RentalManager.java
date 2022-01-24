package com.btkAkademi.rentACar.business.concretes;



import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.CarMaintanenceService;
import com.btkAkademi.rentACar.business.abstracts.CarService;
import com.btkAkademi.rentACar.business.abstracts.CorporateCustomerService;
import com.btkAkademi.rentACar.business.abstracts.CustomerService;
import com.btkAkademi.rentACar.business.abstracts.IndividualCustomerService;
import com.btkAkademi.rentACar.business.abstracts.PaymentService;
import com.btkAkademi.rentACar.business.abstracts.RentalService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.dtos.CarListDto;
import com.btkAkademi.rentACar.business.dtos.RentalListDto;
import com.btkAkademi.rentACar.business.requests.rentalRequests.CreateRentalRequestForCorporate;
import com.btkAkademi.rentACar.business.requests.rentalRequests.CreateRentalRequestForIndividual;
import com.btkAkademi.rentACar.business.requests.rentalRequests.UpdateRentalRequest;
import com.btkAkademi.rentACar.core.utilities.adapters.CorporateCustomer.CorporateCustomerFindexScoreAdapter;
import com.btkAkademi.rentACar.core.utilities.adapters.IndividualCustomer.IndividualCustomerFindexScoreAdapterService;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorDataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessResult;
import com.btkAkademi.rentACar.dataAccess.abstracts.RentalDao;
import com.btkAkademi.rentACar.entities.concretes.Rental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class RentalManager implements RentalService{
	private RentalDao rentalDao;
	private ModelMapperService modelMapperService;
	private CarService carService;
	private CustomerService customerService;
	private CorporateCustomerService corporateCustomerService;
	private CarMaintanenceService carMaintanenceService;
	private CorporateCustomerFindexScoreAdapter corporateCustomerFindexScoreAdapter;
	private IndividualCustomerFindexScoreAdapterService individualCustomerFindexScoreAdapterService;
	private IndividualCustomerService individualCustomerService;
	
	@Autowired
	public RentalManager(
			RentalDao rentalDao,
			ModelMapperService modelMapperService,
			CarService carService,
			CustomerService customerService, 
			CorporateCustomerService corporateCustomerService,
			CarMaintanenceService carMaintanenceService,
			PaymentService paymentService,
			CorporateCustomerFindexScoreAdapter corporateCustomerFindexScoreAdapter,
			IndividualCustomerFindexScoreAdapterService individualCustomerFindexScoreAdapterService,
			IndividualCustomerService individualCustomerService
			) 
	{
		super();
		this.rentalDao = rentalDao;
		this.modelMapperService = modelMapperService;
		this.customerService = customerService;
		this.carMaintanenceService = carMaintanenceService;
		this.corporateCustomerFindexScoreAdapter = corporateCustomerFindexScoreAdapter;
		this.individualCustomerFindexScoreAdapterService = individualCustomerFindexScoreAdapterService;
		this.corporateCustomerService = corporateCustomerService;
		this.carService = carService;
	}

	
	@Override
	public DataResult<List<RentalListDto>> getAll(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);	
		List<Rental> rentalList = this.rentalDao.findAll(pageable).getContent();
		List<RentalListDto> response = rentalList.stream()
				.map(rental->modelMapperService.forDto()
				.map(rental, RentalListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<RentalListDto>>(response);
	}
	

	@Override
	public Result add(CreateRentalRequestForIndividual createRentalForIndividualRequest) {
		
		if (!checkIfCarInMaintanance(createRentalForIndividualRequest.getCarId()).isSuccess() 
				|| !checkIfRentalIdExist(createRentalForIndividualRequest.getCarId()).isSuccess()) 
		{
			CarListDto car = findAvailableCar(carService.getByCarId(createRentalForIndividualRequest.getCarId()).getData().getSegmentId()).getData();
			
			if(car!=null) 
			{
				createRentalForIndividualRequest.setCarId(car.getId());
			}
			else 
			return new ErrorResult(Messages.noAvailableCarInThisSegment);
		}
		
		
		
		
		Result result = BusinessRules.run(
				checkIfCustomerExist(createRentalForIndividualRequest.getIndividualCustomerId()),
				checkIfCarInMaintanance(createRentalForIndividualRequest.getCarId()),
				checkIfIndividualCustomerHasEnoughCreditScore(
					individualCustomerService.getByIndividualCustomerId(createRentalForIndividualRequest.getIndividualCustomerId()).getData().getTcNo(),
					carService.getByCarId(createRentalForIndividualRequest.getCarId()).getData().getFindexScore())	,
				checkIfCustomerAgeIsEnough(createRentalForIndividualRequest.getIndividualCustomerId(), createRentalForIndividualRequest.getCarId())
				//checkIfCarIdExists(createRentalForIndividualRequest.getCarId())
		
			
				);

		if (result != null) {
			return result;
		}

		Rental rental = this.modelMapperService.forRequest().map(createRentalForIndividualRequest, Rental.class);
		
		this.rentalDao.save(rental);
		return new SuccessResult(Messages.rentalAdded);
	}
	
	@Override
	public Result add(CreateRentalRequestForCorporate createRentalRequestForCorporate) {
		Result result = BusinessRules.run(
				checkIfCustomerExist(createRentalRequestForCorporate.getCorporateCustomerId()),
				checkIfCarInMaintanance(createRentalRequestForCorporate.getCarId()),				
				checkIfCorporateCustomerHasEnoughCreditScore(
					corporateCustomerService.getByCorporateCustomerId(createRentalRequestForCorporate.getCorporateCustomerId()).getData().getTaxNumber(),
					carService.getByCarId(createRentalRequestForCorporate.getCarId()).getData().getFindexScore()),
				checkIfCarIdExists(createRentalRequestForCorporate.getCarId())
			
				);

		if (result != null) {
			return result;
		}

		Rental rental = this.modelMapperService.forRequest().map(createRentalRequestForCorporate, Rental.class);
		
		this.rentalDao.save(rental);
		return new SuccessResult(Messages.rentalAdded);
	}

	
	@Override
	public Result update(UpdateRentalRequest updateRentalRequest) {
		
		Result result = BusinessRules.run(
				checkIfRentalIdExist(updateRentalRequest.getRentalId())
				
				);
		
		if(result!=null) 
		{
			return result;
			
		}
		Rental rental = this.modelMapperService.forRequest().map(updateRentalRequest, Rental.class);
		this.rentalDao.save(rental);
		return new SuccessResult(Messages.rentalUpdated);
	}
	
	@Override
	public Result delete(int id) {
		if(this.rentalDao.existsById(id)) 
		{
			this.rentalDao.deleteById(id);
			return new SuccessResult(Messages.rentalDeleted);
		}
		else 
		{
			return new ErrorResult();
		}
	}
	
	@Override
	public DataResult<RentalListDto> getRentalById(int id) {
		if (rentalDao.existsById(id)) {
			Rental rental= this.rentalDao.findById(id).get();
			RentalListDto response = modelMapperService.forDto().map(rental, RentalListDto.class);
			return new SuccessDataResult<RentalListDto>(response);
		} else
			return new ErrorDataResult<>();
	}
	
	
	@Override
	public boolean isCarRented(int carId) {
		if (rentalDao.findByCarIdAndReturnDateIsNull(carId) != null) {
			return true;
		} else
			return false;
	}
	
	
	private Result checkIfCustomerExist(int customerId) {
		if (!customerService.findCustomerById(customerId).isSuccess()) {
			return new ErrorResult(Messages.customerNotFound);
		}

		return new SuccessResult();
	}
	
	
	private Result checkIfCarInMaintanance(int carId) {
		if (carMaintanenceService.checkIfCarIsInMaintanance(carId)) {
			return new ErrorResult(Messages.carInMaintanance);
		}
		return new SuccessResult();
	}
	
	private Result checkIfRentalIdExist(int id) 
	{
		if(!this.rentalDao.existsById(id))
		{
			return new ErrorResult(Messages.rentalIdNotExists);
		}
		return new SuccessResult();
	}
	
	private Result checkIfCorporateCustomerHasEnoughCreditScore(String taxNumber,int minCreditScore ) 
	{
		System.out.println("min :"+minCreditScore);
		if(corporateCustomerFindexScoreAdapter.checkFindexScore(taxNumber).getData()>=minCreditScore) 
		{
			return new SuccessResult();
		}
		else return new ErrorResult(Messages.lowCreditScore);		
		
	}
	
	private Result checkIfIndividualCustomerHasEnoughCreditScore(String tcNo,int minCreditScore ) 
	{
		System.out.println("min :"+minCreditScore);
		if(individualCustomerFindexScoreAdapterService.getFindexScoreOfIndividualCustomer(tcNo).getData()>=minCreditScore) 
		{
			return new SuccessResult();
		}
		else return new ErrorResult(Messages.lowCreditScore);

	}
	
	private Result checkIfCustomerAgeIsEnough(int customerId, int carId) {
		
		int age = Period.between(individualCustomerService.getByIndividualCustomerId(customerId).getData().getBirthDate(), LocalDate.now()).getYears();
		int minAge = carService.getByCarId(carId).getData().getMinAge();
		if(age<minAge) {
			return new ErrorResult(Messages.ageIsNotEnough);
		}
		return new SuccessResult();
		
	}
	
	private DataResult<CarListDto> findAvailableCar(int SegmentId) {
		if(carService.findAvailableCarsBySegmentId(SegmentId).isSuccess()) {
			CarListDto car = carService.getByCarId(carService.findAvailableCarsBySegmentId(SegmentId).getData().get(0)).getData();
			return new SuccessDataResult<CarListDto>(car);
		}else return new ErrorDataResult<CarListDto>();
	}

	private Result checkIfCarIdExists(int carId) 
	{
		
		if(this.carService.getByCarId(carId)!=null) 
		{
			return new ErrorResult(Messages.carIsInRental);
		}
		
		return new SuccessResult();
	}
}
