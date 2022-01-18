package com.btkAkademi.rentACar.business.abstracts;

import java.util.List;

import com.btkAkademi.rentACar.business.dtos.CorporateCustomerInvoiceDto;
import com.btkAkademi.rentACar.business.requests.corporateCustomerInvoiceRequests.CreateCorporateCustomerInvoiceRequest;
import com.btkAkademi.rentACar.business.requests.corporateCustomerInvoiceRequests.UpdateCorporateCustomerInvoiceRequest;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

public interface CorporateCustomerInvoiceService {
	DataResult<List<CorporateCustomerInvoiceDto>> getAll();
	DataResult<CorporateCustomerInvoiceDto> getInvoiceForCorporateCustomer(int rentalId);
	Result add(CreateCorporateCustomerInvoiceRequest corporateCustomerInvoiceRequest);
	Result update(UpdateCorporateCustomerInvoiceRequest updateCorporateCustomerInvoiceRequest);
	Result delete(int id);

}
