package com.btkAkademi.rentACar.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.PromoCodeService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.promoCodeRequests.CreatePromoCodeRequest;
import com.btkAkademi.rentACar.business.requests.promoCodeRequests.UpdatePromoCodeRequest;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessResult;
import com.btkAkademi.rentACar.dataAccess.abstracts.PromoCodeDao;
import com.btkAkademi.rentACar.entities.concretes.PromoCode;

@Service
public class PromoCodeManager implements PromoCodeService{
	private PromoCodeDao promoCodeDao;
	private ModelMapperService modelMapperService;

	@Autowired
	public PromoCodeManager(PromoCodeDao promoCodeDao, ModelMapperService modelMapperService) {
		super();
		this.promoCodeDao = promoCodeDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public Result add(CreatePromoCodeRequest createPromoCodeRequest) {
		Result result = BusinessRules.run(
				promoCodeAlreadyExits(createPromoCodeRequest.getCode())
				
				);
		
		if(result!=null) 
		{
			return result;
			
		}
		
		PromoCode promoCode = this.modelMapperService.forRequest().map(createPromoCodeRequest,PromoCode.class);
		this.promoCodeDao.save(promoCode);
		return new SuccessResult(Messages.promoCodeAdded);
	}

	@Override
	public Result update(UpdatePromoCodeRequest updatePromoCodeRequest) {
		Result result = BusinessRules.run(
				checkPromoCodeIdExists(updatePromoCodeRequest.getPromoCodeId())
				
				);
		
		if(result!=null) 
		{
			return result;
			
		}
		
		PromoCode promoCode = this.modelMapperService.forRequest().map(updatePromoCodeRequest,PromoCode.class);
		this.promoCodeDao.save(promoCode);
		return new SuccessResult(Messages.promoCodeUpdated);
	}
	
	private Result promoCodeAlreadyExits(String promoCode) 
	{
		if(this.promoCodeDao.findByCode(promoCode).getCode()!=null) 
		{
			return new ErrorResult(Messages.promoCodeAlreadyExists);
		}
		return new SuccessResult();
	}
	
	private Result checkPromoCodeIdExists(int id) 
	{
		if(this.promoCodeDao.existsById(id)) 
		{
			return new SuccessResult();
		}
		return new ErrorResult(Messages.promoCodeNotExists);
	}

}
