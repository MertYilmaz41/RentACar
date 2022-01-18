package com.btkAkademi.rentACar.ws.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btkAkademi.rentACar.business.abstracts.IndividualCustomerInvoiceService;
import com.btkAkademi.rentACar.business.dtos.IndividualCustomerInvoiceDto;
import com.btkAkademi.rentACar.business.requests.individualCustomerInvoiceRequests.CreateIndividualCustomerInvoiceRequest;
import com.btkAkademi.rentACar.business.requests.individualCustomerInvoiceRequests.UpdateIndividualCustomerInvoiceRequest;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/individualcustomerinvoices")
public class IndividualCustomerInvoicesController {
	private IndividualCustomerInvoiceService individualCustomerInvoiceService;

	public IndividualCustomerInvoicesController(IndividualCustomerInvoiceService individualCustomerInvoiceService) {
		super();
		this.individualCustomerInvoiceService = individualCustomerInvoiceService;
	}
	
	@GetMapping("getall")
	public DataResult<List<IndividualCustomerInvoiceDto>> getAll()
	{
		return this.individualCustomerInvoiceService.getAll();
	}
	
	
	@GetMapping("getinvoiceforindividualcustomer/{rentalId}")
	public DataResult<IndividualCustomerInvoiceDto> getInvoiceForIndividualCustomer(@PathVariable int rentalId) {
		return this.individualCustomerInvoiceService.getInvoiceForIndividualCustomer(rentalId);
	}
	
	@PostMapping("add")
	public Result add(@RequestBody @Valid CreateIndividualCustomerInvoiceRequest createIndividualCustomerInvoiceRequest) {

		return this.individualCustomerInvoiceService.add(createIndividualCustomerInvoiceRequest);
	}
	
	@PostMapping("update")
	public Result update(@RequestBody @Valid UpdateIndividualCustomerInvoiceRequest updateIndividualCustomerInvoiceRequest) 
	{
		return this.individualCustomerInvoiceService.update(updateIndividualCustomerInvoiceRequest);
	}
	
	@DeleteMapping("delete/{id}")
	public Result delete(@Valid @PathVariable int id) {
		return this.individualCustomerInvoiceService.delete(id);
	}
}
