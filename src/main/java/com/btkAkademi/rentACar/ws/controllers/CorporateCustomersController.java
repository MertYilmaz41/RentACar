package com.btkAkademi.rentACar.ws.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.btkAkademi.rentACar.business.abstracts.CorporateCustomerService;
import com.btkAkademi.rentACar.business.dtos.CorporateListDto;
import com.btkAkademi.rentACar.business.requests.corporateRequests.CreateCorporateRequest;
import com.btkAkademi.rentACar.business.requests.corporateRequests.UpdateCorporateRequest;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

@RestController
@RequestMapping("/api/corporatecustomers")
@CrossOrigin
public class CorporateCustomersController {
	private CorporateCustomerService corporateCustomerService;

	public CorporateCustomersController(CorporateCustomerService corporateCustomerService) {
		super();
		this.corporateCustomerService = corporateCustomerService;
	}
	
	@GetMapping("getall")
	public DataResult<List<CorporateListDto>> getAll()
	{
		return this.corporateCustomerService.getAll();
	}
	
	@GetMapping("getbyid/{id}")
	public Result getById(@PathVariable int id) 
	{
		return corporateCustomerService.getByCorporateCustomerId(id);	
	}
	
	@PostMapping("add")
	public Result add(@RequestBody @Valid CreateCorporateRequest createCorporateRequest) 
	{
		return  this.corporateCustomerService.add(createCorporateRequest);
	}
	
	@PostMapping("update")
	public Result update(@RequestBody @Valid UpdateCorporateRequest updateCorporateRequest) 
	{
		return this.corporateCustomerService.update(updateCorporateRequest);
	}
	
	@DeleteMapping("delete/{id}")
	public Result delete(@Valid @PathVariable int id) {

		return this.corporateCustomerService.delete(id);
	}
}
