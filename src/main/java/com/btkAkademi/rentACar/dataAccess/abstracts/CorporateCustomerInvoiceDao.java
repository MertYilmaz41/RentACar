package com.btkAkademi.rentACar.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.btkAkademi.rentACar.entities.concretes.CorporateCustomerInvoice;


public interface CorporateCustomerInvoiceDao extends JpaRepository<CorporateCustomerInvoice, Integer>{
	CorporateCustomerInvoice findByRentalId(int rentalId);
}
