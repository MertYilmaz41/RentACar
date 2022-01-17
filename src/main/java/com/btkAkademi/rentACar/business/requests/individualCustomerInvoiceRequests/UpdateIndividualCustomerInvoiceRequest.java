package com.btkAkademi.rentACar.business.requests.individualCustomerInvoiceRequests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIndividualCustomerInvoiceRequest {
	private int individualCustomerInvoiceId;
	private LocalDate creationDate;
	private int rentalId;
}
