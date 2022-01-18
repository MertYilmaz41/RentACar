package com.btkAkademi.rentACar.business.concretes;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.CarService;
import com.btkAkademi.rentACar.business.abstracts.CustomerCardDetailService;
import com.btkAkademi.rentACar.business.abstracts.IAdditionalService;
import com.btkAkademi.rentACar.business.abstracts.PaymentService;
import com.btkAkademi.rentACar.business.abstracts.PromoCodeService;
import com.btkAkademi.rentACar.business.abstracts.RentalService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.dtos.AdditionalServiceListDto;
import com.btkAkademi.rentACar.business.dtos.PaymentListDto;
import com.btkAkademi.rentACar.business.dtos.PromoCodeListDto;
import com.btkAkademi.rentACar.business.dtos.RentalListDto;
import com.btkAkademi.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
import com.btkAkademi.rentACar.business.requests.paymentRequests.UpdatePaymentRequest;
import com.btkAkademi.rentACar.core.utilities.adapters.BankAdapterService;
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
import com.btkAkademi.rentACar.dataAccess.abstracts.PaymentDao;
import com.btkAkademi.rentACar.entities.concretes.Payment;


@Service
public class PaymentManager implements PaymentService{

	private PaymentDao paymentDao;
	private ModelMapperService modelMapperService;
	private RentalService rentalService;
	private CarService carService;
	private IAdditionalService iAdditionalService;
	private BankAdapterService bankAdapterService;
	private PromoCodeService promoCodeService;
	
	@Autowired
	public PaymentManager(
			PaymentDao paymentDao,
			ModelMapperService modelMapperService, 
			@Lazy RentalService rentalService,
			CarService carService, 
			IAdditionalService iAdditionalService, 
			BankAdapterService bankAdapterService,
			CustomerCardDetailService customerPaymentDetailService,
			CorporateCustomerFindexScoreAdapter corporateCustomerFindexScoreAdapter,
			IndividualCustomerFindexScoreAdapterService individualCustomerFindexScoreAdapterService,
			PromoCodeService promoCodeService
			) 
	{
		super();
		this.paymentDao = paymentDao;
		this.modelMapperService = modelMapperService;
		this.rentalService = rentalService;
		this.carService = carService;
		this.iAdditionalService = iAdditionalService;
		this.bankAdapterService = bankAdapterService;
		this.promoCodeService = promoCodeService;
	}

	@Override
	public DataResult<List<PaymentListDto>> getAll() {
		List<Payment> paymentList = this.paymentDao.findAll();
		List<PaymentListDto> response = paymentList.stream()
				.map(payment -> modelMapperService.forDto()
				.map(payment, PaymentListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<PaymentListDto>>(response);
	}
	
	@Override
	public DataResult<List<PaymentListDto>> getAllByRentalId(int id) {
		List<Payment> paymentList = this.paymentDao.getAllByRentalId(id); 
		List<PaymentListDto> response = paymentList.stream()
				.map(payment -> modelMapperService.forDto()
				.map(payment, PaymentListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<>(response);
	}
	
	@Override
	public Result add(CreatePaymentRequest createPaymentRequest) {
		
		Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest, Payment.class);
		
		int rentalId= createPaymentRequest.getRentalId();
		
		
		RentalListDto rental = rentalService.getRentalById(rentalId).getData();
		
		
		double totalPrice = totalPriceCalculator(rental);
		
		payment.setTotalPaymentAmount(totalPrice);
		
		Result result = BusinessRules.run(bankAdapterService.checkIfLimitIsEnough(
				createPaymentRequest.getCardNo(),
				createPaymentRequest.getDay(), 
				createPaymentRequest.getMonth(), 
				createPaymentRequest.getCvv(),
				totalPrice));
		
		if(result != null) 
		{
			return result;
		}
		
		payment.setId(0);
		
		this.paymentDao.save(payment);
		
		return new SuccessResult(Messages.paymentAdded);
		
	}


	@Override
	public Result update(UpdatePaymentRequest updatePaymentRequest) {
		
		Result result = BusinessRules.run(
				checkIfPaymentIdExist(updatePaymentRequest.getPaymentId())
				);
		
		if(result!=null) 
		{
			return result;
			
		}
		Payment payment = this.modelMapperService.forRequest().map(updatePaymentRequest, Payment.class);
		this.paymentDao.save(payment);
		return new SuccessResult(Messages.paymentUpdated);
	}
	
	@Override
	public Result delete(int id) {
		if(this.paymentDao.existsById(id)) 
		{
			this.paymentDao.deleteById(id);
			return new SuccessResult(Messages.paymentDeleted);
		}
		else 
		{
			return new ErrorResult();
		}
	}
	
	@Override
	public DataResult<PaymentListDto> getByPaymentId(int paymentId) {
		if (paymentDao.existsById(paymentId)) {
			Payment payment = this.paymentDao.findById(paymentId).get();
			PaymentListDto response = modelMapperService.forDto().map(payment, PaymentListDto.class);
			return new SuccessDataResult<PaymentListDto>(response);
		} 
		else
			return new ErrorDataResult<>();
	}
	
	private Result checkIfPaymentIdExist(int id) 
	{
		if(!this.paymentDao.existsById(id))
		{
			return new ErrorResult(Messages.paymentIdNotExists);
		}
		return new SuccessResult();
	}

	private double totalPriceCalculator(RentalListDto rentalListDto) {
		
		double totalPrice = 0.0;

		
		long days = ChronoUnit.DAYS.between( rentalListDto.getRentDate() , rentalListDto.getReturnDate()) ;
	
		
		if(days==0) days=1;
		
		totalPrice+=days* carService.getByCarId(rentalListDto.getCarId()).getData().getDailyPrice();
		
		if(rentalListDto.getPromoCodeId() !=0) 
		{
			PromoCodeListDto promoCode = this.promoCodeService.getById(rentalListDto.getPromoCodeId()).getData();
			if(!promoCode.getCodeEndDate().isAfter(rentalListDto.getReturnDate())) 
			{
				double discountRate = 0;
				discountRate = promoCode.getDiscountRate();
				totalPrice = totalPrice - (totalPrice * discountRate);
			}
		}
		
		List<AdditionalServiceListDto> services = iAdditionalService.getAllByRentalId(rentalListDto.getId()).getData();
		
		for(AdditionalServiceListDto additionalService : services) {
			
			totalPrice+=additionalService.getPrice();
			
		}
		System.out.println(totalPrice);
		return totalPrice;
	}




}
