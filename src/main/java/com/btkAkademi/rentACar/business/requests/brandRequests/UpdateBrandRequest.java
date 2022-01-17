package com.btkAkademi.rentACar.business.requests.brandRequests;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.btkAkademi.rentACar.business.constants.Messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBrandRequest {

	private int brandId;
	@NotNull 
	@Size(min=3, max=20, message = Messages.invalidBrandName)
	private String name;
}
