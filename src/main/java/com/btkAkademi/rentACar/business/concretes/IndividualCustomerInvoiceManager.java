package com.btkAkademi.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.CarService;
import com.btkAkademi.rentACar.business.abstracts.IAdditionalService;
import com.btkAkademi.rentACar.business.abstracts.IndividualCustomerInvoiceService;
import com.btkAkademi.rentACar.business.abstracts.IndividualCustomerService;
import com.btkAkademi.rentACar.business.abstracts.PaymentService;
import com.btkAkademi.rentACar.business.abstracts.RentalService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.dtos.AdditionalServiceListDto;
import com.btkAkademi.rentACar.business.dtos.CarListDto;
import com.btkAkademi.rentACar.business.dtos.IndividualCustomerInvoiceDto;
import com.btkAkademi.rentACar.business.dtos.IndividualCustomerListDto;
import com.btkAkademi.rentACar.business.dtos.PaymentListDto;
import com.btkAkademi.rentACar.business.dtos.RentalListDto;
import com.btkAkademi.rentACar.business.requests.individualCustomerInvoiceRequests.CreateIndividualCustomerInvoiceRequest;
import com.btkAkademi.rentACar.business.requests.individualCustomerInvoiceRequests.UpdateIndividualCustomerInvoiceRequest;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessResult;
import com.btkAkademi.rentACar.dataAccess.abstracts.IndividualCustomerInvoiceDao;
import com.btkAkademi.rentACar.entities.concretes.IndividualCustomerInvoice;

@Service
public class IndividualCustomerInvoiceManager implements IndividualCustomerInvoiceService{
	private IndividualCustomerInvoiceDao individualCustomerInvoiceDao;
	private ModelMapperService modelMapperService;
	private IndividualCustomerService individualCustomerService;
	private CarService carService;
	private RentalService rentalService;
	private PaymentService paymentService;
	private IAdditionalService iAdditionalService;
	

	@Autowired
	public IndividualCustomerInvoiceManager(
			IndividualCustomerInvoiceDao individualCustomerInvoiceDao,
			ModelMapperService modelMapperService,
			IndividualCustomerService individualCustomerService,
			CarService carService, 
			RentalService rentalService,
			PaymentService paymentService,
			IAdditionalService iAdditionalService) 
	{
		super();
		this.individualCustomerInvoiceDao = individualCustomerInvoiceDao;
		this.modelMapperService = modelMapperService;
		this.individualCustomerService = individualCustomerService;
		this.carService = carService;
		this.rentalService = rentalService;
		this.paymentService = paymentService;
		this.iAdditionalService = iAdditionalService;
	}

	
	@Override
	public DataResult<IndividualCustomerInvoiceDto> getInvoiceForIndividualCustomer(int rentalId) 
	{
		IndividualCustomerInvoice individualCustomerInvoice = individualCustomerInvoiceDao.findByRentalId(rentalId);
		RentalListDto rental = rentalService.getRentalById(rentalId).getData();
		IndividualCustomerListDto customer = individualCustomerService.getByIndividualCustomerId(rental.getCustomerId()).getData();
		List<AdditionalServiceListDto> additionalServices = iAdditionalService.getAllByRentalId(rentalId).getData();
		CarListDto car = carService.getByCarId(rental.getCarId()).getData();
		List<PaymentListDto> payments = paymentService.getAllByRentalId(rentalId).getData();
		double totalPrice = 0;
		for(PaymentListDto payment:payments) {
			totalPrice+=payment.getTotalPaymentAmount();
		}
		
		IndividualCustomerInvoiceDto responseCustomerDto = IndividualCustomerInvoiceDto.builder()
				.id(individualCustomerInvoice.getId())
				.firstName(customer.getFirstName())
				.lastName(customer.getLastName())
				.tcNo(customer.getTcNo())
				.email(customer.getEmail())
				.totalPrice(totalPrice)
				.rentDate(rental.getRentDate())
				.returnedDate(rental.getReturnDate())
				.creationDate(individualCustomerInvoice.getCreationDate())
				.additonalServices(additionalServices)
				.build();
		return new SuccessDataResult<IndividualCustomerInvoiceDto>(responseCustomerDto);
		
	}
	
	@Override
	public DataResult<List<IndividualCustomerInvoiceDto>> getAll() {
		List<IndividualCustomerInvoice> individualInvoiceList = this.individualCustomerInvoiceDao.findAll();
		List<IndividualCustomerInvoiceDto> response = individualInvoiceList.
				stream()
				.map(individualInvoice -> modelMapperService
				.forDto().map(individualInvoice, IndividualCustomerInvoiceDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<IndividualCustomerInvoiceDto>>(response);
	}

	@Override
	public Result add(CreateIndividualCustomerInvoiceRequest createIndividualCustomerInvoiceRequest) {
		Result result = BusinessRules.run(checkIfInvoiceAlreadyExist(createIndividualCustomerInvoiceRequest.getRentalId()));
		
		if(result!=null) 
		{
			return result;
		}
		IndividualCustomerInvoice individualCustomerInvoice = this.modelMapperService.forRequest()
			.map(createIndividualCustomerInvoiceRequest,IndividualCustomerInvoice.class);
		
		this.individualCustomerInvoiceDao.save(individualCustomerInvoice);	
		return new SuccessResult(Messages.invoiceAdded);
	}

	@Override
	public Result update(UpdateIndividualCustomerInvoiceRequest updateIndividualCustomerInvoiceRequest) {
		Result result = BusinessRules.run(
				checkIfInvoiceExists(updateIndividualCustomerInvoiceRequest.getIndividualCustomerInvoiceId())
				);
		if(result!=null) 
		{ 
			return result;
		}
		
		IndividualCustomerInvoice individualCustomerInvoice = this.modelMapperService
				.forRequest()
				.map(updateIndividualCustomerInvoiceRequest, IndividualCustomerInvoice.class);
		this.individualCustomerInvoiceDao.save(individualCustomerInvoice);
		return new SuccessResult(Messages.individualCustomerInvoiceUpdated);
	}
	
	@Override
	public Result delete(int id) {
		if(this.individualCustomerInvoiceDao.existsById(id)) 
		{
			this.individualCustomerInvoiceDao.deleteById(id);
			return new SuccessResult(Messages.individualCustomerInvoiceDeleted);
		}
		else 
		{
			return new ErrorResult();
		}
	}
	
	private Result checkIfInvoiceAlreadyExist(int rentalId) 
	{
		if(this.individualCustomerInvoiceDao.existsById(rentalId)) 
		{
			return new ErrorResult(Messages.invoiceAlreadyExists);
		}
		return new SuccessResult();
	}
	
	private Result checkIfInvoiceExists(int id) 
	{
		if(this.individualCustomerInvoiceDao.existsById(id))
		{
			return new SuccessResult();
		}
		return new ErrorResult(Messages.individualCustomerInvoiceIdNotExists);
	}











}
