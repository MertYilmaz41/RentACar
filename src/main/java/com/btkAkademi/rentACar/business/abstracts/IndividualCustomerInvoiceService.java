package com.btkAkademi.rentACar.business.abstracts;

import java.util.List;

import com.btkAkademi.rentACar.business.dtos.IndividualCustomerInvoiceDto;
import com.btkAkademi.rentACar.business.requests.individualCustomerInvoiceRequests.CreateIndividualCustomerInvoiceRequest;
import com.btkAkademi.rentACar.business.requests.individualCustomerInvoiceRequests.UpdateIndividualCustomerInvoiceRequest;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

public interface IndividualCustomerInvoiceService {
	DataResult<List<IndividualCustomerInvoiceDto>> getAll();
	DataResult<IndividualCustomerInvoiceDto> getInvoiceForIndividualCustomer(int rentalId);
	Result add(CreateIndividualCustomerInvoiceRequest createIndividualCustomerInvoiceRequest);
	Result update(UpdateIndividualCustomerInvoiceRequest updateIndividualCustomerInvoiceRequest);
	Result delete(int id);
}
