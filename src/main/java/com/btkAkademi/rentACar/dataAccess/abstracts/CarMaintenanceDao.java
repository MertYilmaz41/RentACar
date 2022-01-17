package com.btkAkademi.rentACar.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;

import com.btkAkademi.rentACar.entities.concretes.CarMaintanence;



public interface CarMaintenanceDao extends JpaRepository<CarMaintanence, Integer>{
	//CarMaintanence findByCarIdAndMaintenanceStartTimeIsNull(int id);
	//@Query("select cm from CarMaintanence cm where cm.maintenanceEndTime = null")
	 CarMaintanence findByCarIdAndMaintenanceEndTimeIsNull(int carId);
	// CarMaintanence findByMaintenanceEndTimeIsNull(int carId);
}
