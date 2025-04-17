package com.armacare.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.armacare.model.Insurance;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

}
