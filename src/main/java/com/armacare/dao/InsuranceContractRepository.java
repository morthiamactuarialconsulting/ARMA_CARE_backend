package com.armacare.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.armacare.model.InsuranceContract;

public interface InsuranceContractRepository extends JpaRepository<InsuranceContract, Long> {

}
