package com.btkAkademi.rentACar.ws.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.btkAkademi.rentACar.business.abstracts.RentalService;
import com.btkAkademi.rentACar.business.dtos.RentalListDto;
import com.btkAkademi.rentACar.business.requests.rentalRequests.CreateRentalRequestForCorporate;
import com.btkAkademi.rentACar.business.requests.rentalRequests.CreateRentalRequestForIndividual;
import com.btkAkademi.rentACar.business.requests.rentalRequests.UpdateRentalRequest;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

@RestController
@RequestMapping("/api/rentals")
public class RentalsController {
	private RentalService rentalService;

	public RentalsController(RentalService rentalService) {
		super();
		this.rentalService = rentalService;
	}
	
	@GetMapping("getall")
	public DataResult<List<RentalListDto>> getAll(@RequestParam int pageNo, @RequestParam(defaultValue = "10") int pageSize)
	{
		return this.rentalService.getAll(pageNo, pageSize);
	}
	
	@GetMapping("getById/{id}")
	public Result getById(@PathVariable int id) 
	{
		return rentalService.getRentalById(id);	
	}
	
	@PostMapping("add/individualcustomer")
	public Result add(@RequestBody @Valid CreateRentalRequestForIndividual createRentalRequestForIndividual) 
	{
		return this.rentalService.add(createRentalRequestForIndividual);
	}
	
	@PostMapping("add/corporatecustomer")
	public Result add(@RequestBody @Valid CreateRentalRequestForCorporate createRentalRequestForCorporate) 
	{
		return this.rentalService.add(createRentalRequestForCorporate);
	}
	
	@PostMapping("update")
	public Result update(@RequestBody @Valid UpdateRentalRequest updateRentalRequest) 
	{
		return this.rentalService.update(updateRentalRequest);
	}
	
	@DeleteMapping("delete/{id}")
	public Result delete(@PathVariable int id) {
		return this.rentalService.delete(id);
	}


}
