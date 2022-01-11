package com.btkAkademi.rentACar.ws.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btkAkademi.rentACar.business.abstracts.ColorService;
import com.btkAkademi.rentACar.business.dtos.ColorListDto;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;


@RestController
@RequestMapping("/api/colors")
public class ColorsControllers {
	private ColorService colorService;

	public ColorsControllers(ColorService colorService) {
		this.colorService = colorService;
	}
	
	@GetMapping("getall")
	public DataResult<List<ColorListDto>> getAll()
	{
		return this.colorService.getAll();
	}
}
