package com.btkAkademi.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btkAkademi.rentACar.business.abstracts.SegmentService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.dtos.SegmentListDto;
import com.btkAkademi.rentACar.business.requests.segmentRequests.CreateSegmentRequest;
import com.btkAkademi.rentACar.business.requests.segmentRequests.UpdateSegmentRequest;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorDataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessResult;
import com.btkAkademi.rentACar.dataAccess.abstracts.SegmentDao;

import com.btkAkademi.rentACar.entities.concretes.Segment;

@Service
public class SegmentManager implements SegmentService{
	private SegmentDao segmentDao;
	private ModelMapperService modelMapperService;
	
	@Autowired
	public SegmentManager(SegmentDao segmentDao, ModelMapperService modelMapperService) {
		super();
		this.segmentDao = segmentDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<SegmentListDto>> getAll() {
		List<Segment> segmentList = this.segmentDao.findAll();
		List<SegmentListDto> response = segmentList.stream()
				.map(segment -> modelMapperService.forDto()
				.map(segment, SegmentListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<SegmentListDto>>(response);
	}

	@Override
	public DataResult<SegmentListDto> getBySegmentId(int segmentId) 
	{
		if (segmentDao.existsById(segmentId)) 
		{
			Segment segment  = this.segmentDao.findById(segmentId).get();
			SegmentListDto response = modelMapperService.forDto().map(segment, SegmentListDto.class);
			return new SuccessDataResult<SegmentListDto>(response);
		} 
		else
			return new ErrorDataResult<>();
	}

	@Override
	public Result add(CreateSegmentRequest createSegmentRequest) {
		Result result = BusinessRules.run(
				checkIfSegmentNameAlreadyExists(createSegmentRequest.getSegmentName())
				);
		
		if(result!=null) 
		{
			return result;
		}
		
		Segment segment = this.modelMapperService.forRequest().map(createSegmentRequest, Segment.class);
		this.segmentDao.save(segment);
		return new SuccessResult(Messages.segmentAdded);
	}

	@Override
	public Result update(UpdateSegmentRequest updateSegmentRequest) {
		Result result = BusinessRules.run(
				checkIfSegmentIdIsExists(updateSegmentRequest.getId())
				);
		
		if(result!=null) 
		{
			return result;
		}
		
		Segment segment = this.modelMapperService.forRequest().map(updateSegmentRequest, Segment.class);
		this.segmentDao.save(segment);
		return new SuccessResult(Messages.segmentUpdated);
	}

	@Override
	public Result delete(int id) {
		if(this.segmentDao.existsById(id)) 
		{
			this.segmentDao.deleteById(id);
			return new SuccessResult(Messages.segmentDeleted);
		}
		return new ErrorResult();
	}
	
	private Result checkIfSegmentNameAlreadyExists(String segmentName) 
	{
		if(this.segmentDao.findBySegmentName(segmentName)!=null) 
		{
			return new ErrorResult(Messages.segmentNameAlreadyExists);
		}
		return new SuccessResult();
	}
	
	private Result checkIfSegmentIdIsExists(int id) 
	{
		if(this.segmentDao.existsById(id)) 
		{
			return new ErrorResult(Messages.segmentIdExists);
		}
		return new SuccessResult();
	}

}
