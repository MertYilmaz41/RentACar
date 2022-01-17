package com.btkAkademi.rentACar.business.dtos;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorporateListDto {
	private int id;
	private String email;
	private String password;
	private String companyName;
	private String taxNumber;
}
