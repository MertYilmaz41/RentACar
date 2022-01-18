package com.btkAkademi.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.CarService;
import com.btkAkademi.rentACar.business.abstracts.CorporateCustomerInvoiceService;
import com.btkAkademi.rentACar.business.abstracts.CorporateCustomerService;
import com.btkAkademi.rentACar.business.abstracts.IAdditionalService;
import com.btkAkademi.rentACar.business.abstracts.PaymentService;
import com.btkAkademi.rentACar.business.abstracts.RentalService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.dtos.AdditionalServiceListDto;
import com.btkAkademi.rentACar.business.dtos.CarListDto;
import com.btkAkademi.rentACar.business.dtos.CorporateCustomerInvoiceDto;
import com.btkAkademi.rentACar.business.dtos.CorporateListDto;
import com.btkAkademi.rentACar.business.dtos.PaymentListDto;
import com.btkAkademi.rentACar.business.dtos.RentalListDto;
import com.btkAkademi.rentACar.business.requests.corporateCustomerInvoiceRequests.CreateCorporateCustomerInvoiceRequest;
import com.btkAkademi.rentACar.business.requests.corporateCustomerInvoiceRequests.UpdateCorporateCustomerInvoiceRequest;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessResult;
import com.btkAkademi.rentACar.dataAccess.abstracts.CorporateCustomerInvoiceDao;
import com.btkAkademi.rentACar.entities.concretes.CorporateCustomerInvoice;


@Service
public class CorporateCustomerInvoiceManager implements CorporateCustomerInvoiceService{
	private CorporateCustomerInvoiceDao corporateCustomerInvoiceDao;
	private ModelMapperService modelMapperService;
	private CorporateCustomerService corporateCustomerService;
	private CarService carService;
	private RentalService rentalService;
	private PaymentService paymentService;
	private IAdditionalService iAdditionalService;
	

	@Autowired
	public CorporateCustomerInvoiceManager
		(
			CorporateCustomerInvoiceDao corporateCustomerInvoiceDao,
			ModelMapperService modelMapperService,
			CorporateCustomerService corporateCustomerService,
			CarService carService, 
			RentalService rentalService, 
			PaymentService paymentService,
			IAdditionalService iAdditionalService
		)
	
	{
		super();
		this.corporateCustomerInvoiceDao = corporateCustomerInvoiceDao;
		this.modelMapperService = modelMapperService;
		this.corporateCustomerService = corporateCustomerService;
		this.carService = carService;
		this.rentalService = rentalService;
		this.paymentService = paymentService;
		this.iAdditionalService = iAdditionalService;
	}


	@Override
	public DataResult<CorporateCustomerInvoiceDto> getInvoiceForCorporateCustomer(int rentalId)
	{
		CorporateCustomerInvoice corporateCustomerInvoice = corporateCustomerInvoiceDao.findByRentalId(rentalId);
		RentalListDto rental = rentalService.getRentalById(rentalId).getData();
		CorporateListDto customer = corporateCustomerService.getByCorporateCustomerId(rental.getCustomerId()).getData();
		List<AdditionalServiceListDto> additionalServices = iAdditionalService.getAllByRentalId(rentalId).getData();
		CarListDto car = carService.getByCarId(rental.getCarId()).getData();
		List<PaymentListDto> payments = paymentService.getAllByRentalId(rentalId).getData();
		double totalPrice = 0;
		for(PaymentListDto payment:payments) {
			totalPrice+=payment.getTotalPaymentAmount();
		}
		
		CorporateCustomerInvoiceDto responseCustomerDto = CorporateCustomerInvoiceDto.builder()
				.id(corporateCustomerInvoice.getId())
				.companyName(customer.getCompanyName())
				.taxNumber(customer.getTaxNumber())
				.email(customer.getEmail())
				.totalPrice(totalPrice)
				.rentDate(rental.getRentDate())
				.returnedDate(rental.getReturnDate())
				.creationDate(corporateCustomerInvoice.getCreationDate())
				.additonalServices(additionalServices)
				.build();
		return new SuccessDataResult<CorporateCustomerInvoiceDto>(responseCustomerDto);
	}
	

	@Override
	public DataResult<List<CorporateCustomerInvoiceDto>> getAll() {
		List<CorporateCustomerInvoice> corporateInvoiceList = this.corporateCustomerInvoiceDao.findAll();
		List<CorporateCustomerInvoiceDto> response = corporateInvoiceList.
				stream()
				.map(corporateInvoice -> modelMapperService
				.forDto().map(corporateInvoice, CorporateCustomerInvoiceDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<CorporateCustomerInvoiceDto>>(response);
	}
	
	@Override
	public Result add(CreateCorporateCustomerInvoiceRequest createCorporateCustomerInvoiceRequest) {
		Result result = BusinessRules.run
				(
				checkIfInvoiceAlreadyExist(createCorporateCustomerInvoiceRequest.getRentalId())
				
				);
		
		if(result!=null) 
		{
			return result;
		}
		CorporateCustomerInvoice corporateCustomerInvoice = this.modelMapperService.forRequest()
				.map(createCorporateCustomerInvoiceRequest, CorporateCustomerInvoice.class);
		
		this.corporateCustomerInvoiceDao.save(corporateCustomerInvoice);
		return new SuccessResult(Messages.corporateCustomerInvoiceAdded);
	}
	
	@Override
	public Result update(UpdateCorporateCustomerInvoiceRequest updateCorporateCustomerInvoiceRequest) {
		Result result = BusinessRules.run(
				checkIfInvoiceExists(updateCorporateCustomerInvoiceRequest.getCorporateCustomerInvoiceId())
				);
		
		if(result!=null) 
		{
			return result;
		}
		
		CorporateCustomerInvoice corporateCustomerInvoice = this.modelMapperService
				.forRequest()
				.map(updateCorporateCustomerInvoiceRequest, CorporateCustomerInvoice.class);
		this.corporateCustomerInvoiceDao.save(corporateCustomerInvoice);
		
		return new SuccessResult(Messages.corporateCustomerInvoiceUpdated);
	}
	
	@Override
	public Result delete(int id) {
	
		if(this.corporateCustomerInvoiceDao.existsById(id)) 
		{
			this.corporateCustomerInvoiceDao.deleteById(id);
			return new SuccessResult(Messages.corporateCustomerInvoiceDeleted);
		}
		else 
		{
			return new ErrorResult();
		}

	}
	
	private Result checkIfInvoiceAlreadyExist(int rentalId) 
	{
		if(this.corporateCustomerInvoiceDao.existsById(rentalId)) 
		{
			return new ErrorResult(Messages.invoiceAlreadyExists);
		}
		return new SuccessResult();
	}
	
	private Result checkIfInvoiceExists(int id) 
	{
		if(this.corporateCustomerInvoiceDao.existsById(id))
		{
			return new SuccessResult();
		}
		return new ErrorResult(Messages.corporateCustomerInvoiceIdNotExists);
	}














}
