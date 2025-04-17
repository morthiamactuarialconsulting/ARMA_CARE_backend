package com.armacare.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.armacare.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}
