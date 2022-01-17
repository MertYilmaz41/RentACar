package com.btkAkademi.rentACar.business.requests.carInServiceRequests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarMaintenanceRequest {
	private int carMaintenanceId;
	private int carId;
	private LocalDate maintanenceStartTime;
}
