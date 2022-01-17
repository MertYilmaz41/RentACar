package com.btkAkademi.rentACar.business.requests.carRequests;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCarRequest {

	private int colorId;
	private int brandId;
	private double dailyPrice;
	private int modelYear;
	private String description;
	private int findexScore;
	private int minAge;

	

	

}
