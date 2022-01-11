package com.btkAkademi.rentACar.business.abstracts;

import java.util.List;


import com.btkAkademi.rentACar.business.dtos.ColorListDto;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;

public interface ColorService {
	DataResult<List<ColorListDto>> getAll();
}
