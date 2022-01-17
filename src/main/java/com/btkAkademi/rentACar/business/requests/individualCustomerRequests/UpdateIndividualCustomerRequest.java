package com.btkAkademi.rentACar.business.requests.individualCustomerRequests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIndividualCustomerRequest {
	
	private int individualCustomerId;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private LocalDate birthDate;
	private String tcNo;
}
