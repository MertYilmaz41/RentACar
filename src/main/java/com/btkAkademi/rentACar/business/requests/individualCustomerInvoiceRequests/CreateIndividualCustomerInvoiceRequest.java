package com.btkAkademi.rentACar.business.requests.individualCustomerInvoiceRequests;

import java.time.LocalDate;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateIndividualCustomerInvoiceRequest {
	private LocalDate creationDate;
	private int rentalId;
}
