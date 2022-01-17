package com.btkAkademi.rentACar.business.requests.corporateCustomerInvoiceRequests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCorporateCustomerInvoiceRequest {
	private int corporateCustomerInvoiceId;
	private LocalDate creationDate;
	private int rentalId;
}
