package com.armacare.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.armacare.model.Coverage;

public interface CoverageRepository extends JpaRepository<Coverage, Long> {

}
