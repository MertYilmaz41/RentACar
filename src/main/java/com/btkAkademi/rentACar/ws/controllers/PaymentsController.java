package com.btkAkademi.rentACar.ws.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.btkAkademi.rentACar.business.abstracts.PaymentService;
import com.btkAkademi.rentACar.business.dtos.PaymentListDto;
import com.btkAkademi.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
import com.btkAkademi.rentACar.business.requests.paymentRequests.UpdatePaymentRequest;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

public class PaymentsController {
	private PaymentService paymentService;

	public PaymentsController(PaymentService paymentService) {
		super();
		this.paymentService = paymentService;
	}
	
	@GetMapping("getall")
	public DataResult<List<PaymentListDto>> getAll()
	{
		return this.paymentService.getAll();
	}

	@GetMapping("getById/{id}")
	public Result getById(@PathVariable int id) 
	{
		return paymentService.getByPaymentId(id);	
	}
	
	@PostMapping("add")
	public Result add(@RequestBody @Valid CreatePaymentRequest createPaymentRequest) {

		return this.paymentService.add(createPaymentRequest);
	}
	
	@PostMapping("update")
	public Result update(@RequestBody @Valid UpdatePaymentRequest updatePaymentRequest) {

		return this.paymentService.update(updatePaymentRequest);
	}
	
	@DeleteMapping("delete/{id}")
	public Result delete(@PathVariable int id) {
		return this.paymentService.delete(id);
	}
}
