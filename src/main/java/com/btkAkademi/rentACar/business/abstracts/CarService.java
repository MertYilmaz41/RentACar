package com.btkAkademi.rentACar.business.abstracts;

import java.util.List;

import com.btkAkademi.rentACar.business.dtos.CarListDto;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;

public interface CarService {
	DataResult<List<CarListDto>> getAll();
}
