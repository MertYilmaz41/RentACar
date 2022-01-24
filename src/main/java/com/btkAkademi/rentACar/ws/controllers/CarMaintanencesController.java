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

import com.btkAkademi.rentACar.business.abstracts.CarMaintanenceService;
import com.btkAkademi.rentACar.business.dtos.CarMaintanenceListDto;
import com.btkAkademi.rentACar.business.requests.carInServiceRequests.CreateCarMaintenanceRequest;
import com.btkAkademi.rentACar.business.requests.carInServiceRequests.UpdateCarMaintenanceRequest;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

@RestController
@RequestMapping("/api/carmaintanences")
@CrossOrigin
public class CarMaintanencesController {
	private CarMaintanenceService carMaintanenceService;

	@Autowired
	public CarMaintanencesController(CarMaintanenceService carMaintanenceService) {
		super();
		this.carMaintanenceService = carMaintanenceService;
	}
	
	@GetMapping("getall")
	public DataResult<List<CarMaintanenceListDto>> getAll()
	{
		return this.carMaintanenceService.getAll();
	}
	
	@GetMapping("getbyid/{id}")
	public Result getById(@PathVariable int id) 
	{
		return carMaintanenceService.getByCarMaintanenceId(id);	
	}
	
	@PostMapping("add")
	public Result add(@RequestBody @Valid CreateCarMaintenanceRequest createCarMaintenanceRequest) 
	{
		return this.carMaintanenceService.add(createCarMaintenanceRequest);
	}
	
	@PostMapping("update")
	public Result update(@RequestBody @Valid UpdateCarMaintenanceRequest updateCarMaintenanceRequest) 
	{
		return this.carMaintanenceService.update(updateCarMaintenanceRequest);
	}
	
	@DeleteMapping("delete/{id}")
	public Result add(@PathVariable int id) {
		return this.carMaintanenceService.delete(id);
	}
	
}
