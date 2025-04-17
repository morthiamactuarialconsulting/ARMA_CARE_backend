package com.armacare.service;

import com.armacare.dao.ProfessionalRepository;
import com.armacare.model.Professional;
import com.armacare.model.Professional.AccountStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.armacare.exception.ProfessionalNotFoundException;
import org.springframework.stereotype.Service;
import com.armacare.dto.ProfessionalDto;

@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    
    public ProfessionalService(ProfessionalRepository professionalRepository) {
        this.professionalRepository = professionalRepository;
    }
    
    public Professional findById(Long id) {
        return professionalRepository.findById(id)
                .orElseThrow(() -> new ProfessionalNotFoundException(id));
    }
    
    public List<Professional> findAll() {
        return professionalRepository.findAll();
    }

    public Professional createProfessional(ProfessionalDto professionalDto) {
        if (professionalDto.getAccountStatus() == null) {
            professionalDto.setAccountStatus(AccountStatus.PENDING_VERIFICATION);
        }
        if (professionalDto.getStatusChangeDate() == null) {
            professionalDto.setStatusChangeDate(LocalDateTime.now());
        }
        if (professionalDto.getStatusChangeReason() == null) {
            professionalDto.setStatusChangeReason("En attente de vérification des documents");
        }
        Professional professional = new Professional();
        updateProfessionalFromDto(professional, professionalDto);
        return professionalRepository.save(professional);
    }
    
    public Professional updateProfessional(Long id, ProfessionalDto professionalDto) {
        if (professionalDto.getAccountStatus() == null) {
            professionalDto.setAccountStatus(AccountStatus.PENDING_VERIFICATION);
        }
        if (professionalDto.getStatusChangeDate() == null) {
            professionalDto.setStatusChangeDate(LocalDateTime.now());
        }
        if (professionalDto.getStatusChangeReason() == null) {
            professionalDto.setStatusChangeReason("En attente de vérification des documents");
        }
        Professional professional = findById(id);
        updateProfessionalFromDto(professional, professionalDto);
        return professionalRepository.save(professional);
    }
    
    private void updateProfessionalFromDto(Professional professional, ProfessionalDto dto) {
        professional.setFirstName(dto.getFirstName());
        professional.setLastName(dto.getLastName());
        professional.setSpeciality(dto.getSpeciality());
        professional.setRegistrationNumber(dto.getRegistrationNumber());
        professional.setPhone(dto.getPhone());
        professional.setEmail(dto.getEmail());
        professional.setAddress(dto.getAddress());
        professional.setCity(dto.getCity());
        professional.setCountry(dto.getCountry());

        professional.setIdentityDocumentPath(dto.getIdentityDocumentPath());
        professional.setLicensePath(dto.getLicensePath());
        professional.setProfessionalInsurancePath(dto.getProfessionalInsurancePath());
        professional.setDiplomaPath(dto.getDiplomaPath());
        professional.setBankAccountNumberPath(dto.getBankAccountNumberPath());
        
        // Mise à jour des statuts si spécifiés dans le DTO
        if (dto.getAccountStatus() != null && 
            dto.getAccountStatus() != professional.getAccountStatus()) {
            updateAccountStatus(professional, dto.getAccountStatus(), dto.getStatusChangeReason());
        }
    }

    public Optional<Professional> findByEmail(String email) {
        return professionalRepository.findByEmail(email);
    }

    public Optional<Professional> findByPhone(String phone) {
        return professionalRepository.findByPhone(phone);
    }

    public Optional<Professional> findByRegistrationNumber(String registrationNumber) {
        return professionalRepository.findByRegistrationNumber(registrationNumber);
    }
    
    public List<Professional> findBySpeciality(String speciality) {
        return professionalRepository.findBySpeciality(speciality);
    }
    
    public List<Professional> findByCity(String city) {
        return professionalRepository.findByCity(city);
    }
    
    public List<Professional> findByAccountStatus(AccountStatus status) {
        return professionalRepository.findByAccountStatus(status);
    }
    
    public void deleteProfessional(Long id) {
        Professional professional = findById(id);
        // Désactiver le professionnel au lieu de le supprimer complètement
        professional.setAccountStatus(AccountStatus.INACTIVE);
        professional.setStatusChangeReason("Suppression du compte");
        professional.setStatusChangeDate(LocalDateTime.now());
        professionalRepository.save(professional);
    }
    
    public Professional activateAccount(Long id) {
        Professional professional = findById(id);
        updateAccountStatus(professional, AccountStatus.ACTIVE, "Compte activé");
        return professionalRepository.save(professional);
    }
    
    public Professional suspendAccount(Long id, String reason) {
        Professional professional = findById(id);
        updateAccountStatus(professional, AccountStatus.SUSPENDED, reason);
        return professionalRepository.save(professional);
    }
    
    private void updateAccountStatus(Professional professional, AccountStatus status, String reason) {
        professional.setAccountStatus(status);
        professional.setStatusChangeReason(reason);
        professional.setStatusChangeDate(LocalDateTime.now());
    }
}
