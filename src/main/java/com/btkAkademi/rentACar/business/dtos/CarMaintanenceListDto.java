package com.btkAkademi.rentACar.business.dtos;

import java.time.LocalDate;

import com.btkAkademi.rentACar.entities.concretes.Car;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarMaintanenceListDto {
	private int id;
	private Car car;
	private LocalDate maintanenceStartTime;
	private LocalDate maintanenceEndTime;
}
