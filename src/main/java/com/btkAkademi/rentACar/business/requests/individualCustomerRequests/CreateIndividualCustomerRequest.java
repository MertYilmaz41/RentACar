package com.btkAkademi.rentACar.business.requests.individualCustomerRequests;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateIndividualCustomerRequest {
	@NotNull
	private String email;
	@NotNull
	private String password;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	
	private LocalDate birthDate;
	
	private String tcNo;
	

}
