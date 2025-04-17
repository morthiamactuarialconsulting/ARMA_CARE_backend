package com.armacare.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.armacare.model.Professional;
import com.armacare.model.Professional.AccountStatus;

import java.util.List;
import java.util.Optional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
    Optional<Professional> findById(Long professionalId);
    List<Professional> findAll();
    Optional<Professional> findByEmail(String email);
    Optional<Professional> findByPhone(String phone);
    List<Professional> findBySpeciality(String speciality);
    List<Professional> findByCity(String city);
    List<Professional> findByLastName(String lastName);
    List<Professional> findByFirstName(String firstName);
    Optional<Professional> findByRegistrationNumber(String registrationNumber);
    List<Professional> findByAccountStatus(AccountStatus accountStatus);
    void deleteById(Long professionalId);
   
}
