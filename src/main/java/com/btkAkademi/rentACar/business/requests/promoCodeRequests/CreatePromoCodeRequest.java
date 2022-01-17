package com.btkAkademi.rentACar.business.requests.promoCodeRequests;

import java.time.LocalDate;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePromoCodeRequest {
	private String code;
	private double discountRate;
	private LocalDate codeStartDate;
	private LocalDate codeEndDate;
	
}
