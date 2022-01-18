package com.btkAkademi.rentACar.business.requests.rentalRequests;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRentalRequestForIndividual {

	private LocalDate rentDate;
	private int rentedKilometer;
	private int pickUpCityId;
	private int returnCityId;
	
	private int segmentName;
	private String tcNo;
	private int individualCustomerId;	
	private int carId;


}
