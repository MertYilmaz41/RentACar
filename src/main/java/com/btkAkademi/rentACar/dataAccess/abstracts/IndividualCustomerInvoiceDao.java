package com.btkAkademi.rentACar.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.btkAkademi.rentACar.entities.concretes.IndividualCustomerInvoice;

public interface IndividualCustomerInvoiceDao extends JpaRepository<IndividualCustomerInvoice, Integer>{
	IndividualCustomerInvoice findByRentalId(int rentalId);
}
