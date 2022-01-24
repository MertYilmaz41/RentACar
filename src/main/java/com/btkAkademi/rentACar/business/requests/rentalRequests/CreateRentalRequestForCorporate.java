package com.btkAkademi.rentACar.business.requests.rentalRequests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRentalRequestForCorporate {
	private LocalDate rentDate;
	private int corporateCustomerId;	
	private int carId;
	private int promoCode;
}
