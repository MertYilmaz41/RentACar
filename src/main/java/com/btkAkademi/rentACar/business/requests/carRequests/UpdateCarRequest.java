package com.btkAkademi.rentACar.business.requests.carRequests;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarRequest {
	private int carId;
	private double dailyPrice;
	private int modelYear;
	private String description;
	private int findexScore;
	private int kilometer;
	private int brandId;
	private int colorId;
	private int minAge;
	private Integer segmentId;
	private String imageUrl;






}
