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
	private int individualCustomerId;	
	private int carId;
	//private Integer rentedKilometer;
	//private Integer returnedKilometer;
	//private int promoCode;


}
