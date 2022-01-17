package com.btkAkademi.rentACar.business.requests.corporateCustomerInvoiceRequests;

import java.time.LocalDate;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCorporateCustomerInvoiceRequest {
	private LocalDate creationDate;
	private int rentalId;
}
