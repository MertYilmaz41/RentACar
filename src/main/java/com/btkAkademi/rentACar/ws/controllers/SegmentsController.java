package com.btkAkademi.rentACar.ws.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.btkAkademi.rentACar.business.abstracts.SegmentService;
import com.btkAkademi.rentACar.business.dtos.SegmentListDto;
import com.btkAkademi.rentACar.business.requests.segmentRequests.CreateSegmentRequest;
import com.btkAkademi.rentACar.business.requests.segmentRequests.UpdateSegmentRequest;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

@RestController
@RequestMapping("/api/segments")
@CrossOrigin
public class SegmentsController {
	private SegmentService segmentService;
	
	@Autowired
	public SegmentsController(SegmentService segmentService) {
		super();
		this.segmentService = segmentService;
	}
	
	@GetMapping("getall")
	public DataResult<List<SegmentListDto>> getAll()
	{
		return this.segmentService.getAll();
	}
	
	@GetMapping("getbyid/{id}")
	public Result getById(@PathVariable int id) 
	{
		return segmentService.getBySegmentId(id);	
	}

	@PostMapping("add")
	public Result add(@RequestBody @Valid CreateSegmentRequest createSegmentRequest) 
	{
		return this.segmentService.add(createSegmentRequest);
	}
	
	@PostMapping("update")
	public Result update(@RequestBody @Valid UpdateSegmentRequest updateSegmentRequest) 
	{
		return this.segmentService.update(updateSegmentRequest);
	}
	
	@DeleteMapping("delete/{id}")
	public Result delete(@Valid @PathVariable int id) {
		return this.segmentService.delete(id);
	}
	
}
