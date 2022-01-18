package com.btkAkademi.rentACar.business.concretes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.PromoCodeService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.dtos.PromoCodeListDto;
import com.btkAkademi.rentACar.business.requests.promoCodeRequests.CreatePromoCodeRequest;
import com.btkAkademi.rentACar.business.requests.promoCodeRequests.UpdatePromoCodeRequest;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorDataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
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
	public DataResult<List<PromoCodeListDto>> getAll() {
		List<PromoCode> promoCodelist = this.promoCodeDao.findAll();
		List<PromoCodeListDto> response = promoCodelist.stream()
				.map(promoCode -> modelMapperService
				.forDto().map(promoCode, PromoCodeListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<PromoCodeListDto>>(response);
	}
	
	@Override
	public DataResult<PromoCodeListDto> getById(int promoCodeId) {
		if (promoCodeDao.existsById(promoCodeId)) 
		{
			PromoCode promoCode = this.promoCodeDao.findById(promoCodeId).get();
			PromoCodeListDto response = modelMapperService.forDto().map(promoCode, PromoCodeListDto.class);
			return new SuccessDataResult<PromoCodeListDto>(response);
		} 
			return new ErrorDataResult<>();
	}
	
	@Override
	public Result add(CreatePromoCodeRequest createPromoCodeRequest) {
		Result result = BusinessRules.run(
				promoCodeAlreadyExits(createPromoCodeRequest.getCode()),
				checkIfDatesAreCorrect(createPromoCodeRequest.getCodeStartDate(), createPromoCodeRequest.getCodeEndDate())
				
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
				checkPromoCodeIdExists(updatePromoCodeRequest.getPromoCodeId()),
				checkIfDatesAreCorrect(updatePromoCodeRequest.getCodeStartDate(), updatePromoCodeRequest.getCodeEndDate())
				
				);
		
		if(result!=null) 
		{
			return result;
			
		}
		
		PromoCode promoCode = this.modelMapperService.forRequest().map(updatePromoCodeRequest,PromoCode.class);
		this.promoCodeDao.save(promoCode);
		return new SuccessResult(Messages.promoCodeUpdated);
	}
	
	@Override
	public Result delete(int id) {
		if(this.promoCodeDao.existsById(id) )
		{
			this.promoCodeDao.deleteById(id);
			return new SuccessResult(Messages.promoCodeDeleted);
		}
		return new ErrorResult();
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

	private Result checkIfDatesAreCorrect(LocalDate startDate, LocalDate endDate) {
		if (startDate.isBefore(endDate)) 
		{
			return new ErrorResult(Messages.datesAreIncorrect);
		} 
		else
			return new SuccessResult();
	}






}
