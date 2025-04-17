package com.armacare.dao;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDateTime;

import com.armacare.model.Professional;
import com.armacare.model.Professional.AccountStatus;

@DataJpaTest
@ActiveProfiles("test")
public class ProfessionalRepositoryTest {

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Test
    void shouldGetAllProfessionals() {
        // Act
        List<Professional> professionals = professionalRepository.findAll();

        // Assert
        assertEquals(3, professionals.size());
        assertEquals("Saliou", professionals.get(0).getFirstName());
        assertEquals("Fatima", professionals.get(1).getFirstName());
        assertEquals("Saidou", professionals.get(2).getFirstName());
    }

    @Test
    void shouldGetProfessionalById() {
        // Act
        Professional professional = professionalRepository.findById(1L).orElse(null);

        // Assert
        assertEquals("Saliou", professional.getFirstName());
        assertEquals("Diop", professional.getLastName());
        assertEquals("Medecin Chirurgien", professional.getSpeciality());
        assertEquals("123456789", professional.getRegistrationNumber());
        assertEquals("saliou.diop@example.com", professional.getEmail());
        assertEquals("+221772345678", professional.getPhone());
        assertEquals("123 Keur Massar", professional.getAddress());
        assertEquals("Dakar", professional.getCity());
        assertNotNull(professional.getCountry());
        assertEquals(AccountStatus.ACTIVE, professional.getAccountStatus());
        assertNotNull(professional.getStatusChangeReason());
        LocalDateTime expectedDateTime = LocalDateTime.parse("2023-01-01T16:00:00");
        LocalDateTime actualDateTime = professional.getStatusChangeDate();
        assertEquals(expectedDateTime, actualDateTime);
        assertEquals("path/to/identity/document1", professional.getIdentityDocumentPath());
        assertEquals("path/to/diploma1", professional.getDiplomaPath());
        assertEquals("path/to/license1", professional.getLicensePath());
        assertEquals("path/to/professional/insurance1", professional.getProfessionalInsurancePath());
        assertEquals("path/to/bank/account/number1", professional.getBankAccountNumberPath());
    }

    @Test
    void shouldGetProfessionalByEmail() {
        Optional<Professional> professional = professionalRepository.findByEmail("saliou.diop@example.com");
        assertEquals("Saliou", professional.get().getFirstName());
        assertEquals("Diop", professional.get().getLastName());
        assertEquals("Medecin Chirurgien", professional.get().getSpeciality());
        assertEquals("123456789", professional.get().getRegistrationNumber());
        assertEquals("saliou.diop@example.com", professional.get().getEmail());
        assertEquals("+221772345678", professional.get().getPhone());
        assertEquals("123 Keur Massar", professional.get().getAddress());
        assertEquals("Dakar", professional.get().getCity());
        assertNotNull(professional.get().getCountry());
        assertEquals(AccountStatus.ACTIVE, professional.get().getAccountStatus());
        assertNotNull(professional.get().getStatusChangeReason());
        LocalDateTime expectedDateTime = LocalDateTime.parse("2023-01-01T16:00:00");
        LocalDateTime actualDateTime = professional.get().getStatusChangeDate();
        assertEquals(expectedDateTime, actualDateTime);
        assertEquals("path/to/identity/document1", professional.get().getIdentityDocumentPath());
        assertEquals("path/to/diploma1", professional.get().getDiplomaPath());
        assertEquals("path/to/license1", professional.get().getLicensePath());
        assertEquals("path/to/professional/insurance1", professional.get().getProfessionalInsurancePath());
        assertEquals("path/to/bank/account/number1", professional.get().getBankAccountNumberPath());
    }
    @Test
    void shouldGetProfessionalByPhone() {
        Optional<Professional> professional = professionalRepository.findByPhone("+221772345678");
        assertEquals("Saliou", professional.get().getFirstName());
        assertEquals("Diop", professional.get().getLastName());
        assertEquals("Medecin Chirurgien", professional.get().getSpeciality());
        assertEquals("123456789", professional.get().getRegistrationNumber());
        assertEquals("saliou.diop@example.com", professional.get().getEmail());
        assertEquals("+221772345678", professional.get().getPhone());
        assertEquals("123 Keur Massar", professional.get().getAddress());
        assertEquals("Dakar", professional.get().getCity());
        assertNotNull(professional.get().getCountry());
        assertEquals(AccountStatus.ACTIVE, professional.get().getAccountStatus());
        assertNotNull(professional.get().getStatusChangeReason());
        LocalDateTime expectedDateTime = LocalDateTime.parse("2023-01-01T16:00:00");
        LocalDateTime actualDateTime = professional.get().getStatusChangeDate();
        assertEquals(expectedDateTime, actualDateTime);
        assertEquals("path/to/identity/document1", professional.get().getIdentityDocumentPath());
        assertEquals("path/to/diploma1", professional.get().getDiplomaPath());
        assertEquals("path/to/license1", professional.get().getLicensePath());
        assertEquals("path/to/professional/insurance1", professional.get().getProfessionalInsurancePath());
        assertEquals("path/to/bank/account/number1", professional.get().getBankAccountNumberPath());
    }
    @Test
    void shouldGetProfessionalByRegistrationNumber() {
        Optional<Professional> professional = professionalRepository.findByRegistrationNumber("123456789");
        assertEquals("Saliou", professional.get().getFirstName());
        assertEquals("Diop", professional.get().getLastName());
        assertEquals("Medecin Chirurgien", professional.get().getSpeciality());
        assertEquals("123456789", professional.get().getRegistrationNumber());
        assertEquals("saliou.diop@example.com", professional.get().getEmail());
        assertEquals("+221772345678", professional.get().getPhone());
        assertEquals("123 Keur Massar", professional.get().getAddress());
        assertEquals("Dakar", professional.get().getCity());
        assertNotNull(professional.get().getCountry());
        assertEquals(AccountStatus.ACTIVE, professional.get().getAccountStatus());
        assertNotNull(professional.get().getStatusChangeReason());
        LocalDateTime expectedDateTime = LocalDateTime.parse("2023-01-01T16:00:00");
        LocalDateTime actualDateTime = professional.get().getStatusChangeDate();
        assertEquals(expectedDateTime, actualDateTime);
        assertEquals("path/to/identity/document1", professional.get().getIdentityDocumentPath());
        assertEquals("path/to/diploma1", professional.get().getDiplomaPath());
        assertEquals("path/to/license1", professional.get().getLicensePath());
        assertEquals("path/to/professional/insurance1", professional.get().getProfessionalInsurancePath());
        assertEquals("path/to/bank/account/number1", professional.get().getBankAccountNumberPath());
    }

    @Test
    void shouldGetProfessionalBySpeciality() {
        List<Professional> professionals = professionalRepository.findBySpeciality("Medecin Chirurgien");
        assertEquals(1, professionals.size());
        assertEquals("saliou.diop@example.com", professionals.get(0).getEmail());
    }
    @Test
    void shouldGetProfessionalByCity() {
        List<Professional> professionals = professionalRepository.findByCity("Dakar");
        assertEquals(2, professionals.size());
        assertEquals("saliou.diop@example.com", professionals.get(0).getEmail());
    }
    @Test
    void shouldGetProfessionalByLastName() {
        List<Professional> professionals = professionalRepository.findByLastName("Diop");
        assertEquals(2, professionals.size());
        assertEquals("saliou.diop@example.com", professionals.get(0).getEmail());
    }
    @Test
    void shouldGetProfessionalByFirstName() {
        List<Professional> professionals = professionalRepository.findByFirstName("Saliou");
        assertEquals(1, professionals.size());
        assertEquals("saliou.diop@example.com", professionals.get(0).getEmail());
    }
    
    @Test
    void shouldSaveProfessional() {
        // Arrange
        Professional professional = new Professional();
        professional.setFirstName("Rokhiya");
        professional.setLastName("Seck");
        professional.setSpeciality("Gynécologiste");
        professional.setRegistrationNumber("4413456789");
        professional.setEmail("rokhiya.seck@example.com");
        professional.setPhone("+221772345678");
        professional.setAddress("123 HLM");
        professional.setCity("Dakar");
        professional.setCountry("Sénégal");
        professional.setAccountStatus(AccountStatus.ACTIVE);
        professional.setStatusChangeDate(LocalDateTime.parse("2025-04-14T18:23:00"));
        professional.setStatusChangeReason("Documents validés");
        professional.setIdentityDocumentPath("path/to/identity/documentRokhiya");
        professional.setDiplomaPath("path/to/diplomaRokhiya");
        professional.setLicensePath("path/to/licenseRokhiya");
        professional.setProfessionalInsurancePath("path/to/professional/insuranceRokhiya");
        professional.setBankAccountNumberPath("path/to/bank/account/numberRokhiya");
        
        // Act
        Professional savedProfessional = professionalRepository.save(professional);
        
        // Assert
        assertEquals("Rokhiya", savedProfessional.getFirstName());
        assertEquals("Seck", savedProfessional.getLastName());
        assertEquals("Gynécologiste", savedProfessional.getSpeciality());
        assertEquals("4413456789", savedProfessional.getRegistrationNumber());
        assertEquals("rokhiya.seck@example.com", savedProfessional.getEmail());
        assertEquals("+221772345678", savedProfessional.getPhone());
        assertEquals("123 HLM", savedProfessional.getAddress());
        assertEquals("Dakar", savedProfessional.getCity());
        assertNotNull(savedProfessional.getCountry());
        assertEquals(AccountStatus.ACTIVE, savedProfessional.getAccountStatus());
        LocalDateTime expectedDateTime = LocalDateTime.parse("2025-04-14T18:23:00");
        LocalDateTime actualDateTime = savedProfessional.getStatusChangeDate();
        assertEquals(expectedDateTime, actualDateTime);
        assertNotNull(savedProfessional.getStatusChangeReason());
        assertEquals("path/to/identity/documentRokhiya", savedProfessional.getIdentityDocumentPath());
        assertEquals("path/to/diplomaRokhiya", savedProfessional.getDiplomaPath());
        assertEquals("path/to/licenseRokhiya", savedProfessional.getLicensePath());
        assertEquals("path/to/professional/insuranceRokhiya", savedProfessional.getProfessionalInsurancePath());
        assertEquals("path/to/bank/account/numberRokhiya", savedProfessional.getBankAccountNumberPath());
    }

    @Test
    void shouldUpdateProfessional() {
        Professional professional = professionalRepository.findById(1L).get();
        professional.setFirstName("Cheikh Saliou");
        Professional updatedProfessional = professionalRepository.save(professional);

        assertEquals("Cheikh Saliou", updatedProfessional.getFirstName());
    }

    @Test
    void shouldDeleteProfessional() {
        Professional professional = professionalRepository.findById(1L).get();
        professionalRepository.delete(professional);

        Optional<Professional> deletedProfessional = professionalRepository.findById(1L);
        assertFalse(deletedProfessional.isPresent());
    }
}