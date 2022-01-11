package com.btkAkademi.rentACar.ws.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btkAkademi.rentACar.business.abstracts.CarService;
import com.btkAkademi.rentACar.business.dtos.CarListDto;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;


@RestController
@RequestMapping("/api/cars")
public class CarsControllers {
	private CarService carService;

	public CarsControllers(CarService carService) {
		this.carService = carService;
	}
	
	@GetMapping("getall")
	public DataResult<List<CarListDto>> getAll()
	{
		return this.carService.getAll();
	}
}
