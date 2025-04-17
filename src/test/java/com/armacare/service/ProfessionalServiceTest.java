package com.armacare.service;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.ArgumentCaptor;

import com.armacare.model.Professional.AccountStatus;
import com.armacare.dto.ProfessionalDto;
import com.armacare.exception.ProfessionalNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.armacare.dao.ProfessionalRepository;
import com.armacare.model.Professional;

@SpringBootTest // par ce que j'ai besoin de charger tout le contexte de l'application
@ActiveProfiles("test") // Utiliser le profil de test avec la base de données H2
public class ProfessionalServiceTest {

    @Mock
    private ProfessionalRepository professionalRepository;

    @InjectMocks
    private ProfessionalService professionalService;

    @Test
    void shouldFindAllProfessionals() {
        Professional professional1 = new Professional(
                "Saliou",
                "Diop",
                "Medecin Chirurgien",
                "123456789",
                "+221772345678",
                "saliou.diop@example.com",
                "123 Keur Massar",
                "Dakar",
                "Sénégal",
                "ACTIVE",
                "Documents validés",
                "2023-01-01T16:00:00",
                "path/to/identity/document1",
                "path/to/diploma1",
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/bank/account/number1");
        Professional professional2 = new Professional(
                "Fatima",
                "Diop",
                "Medecin gynécologiste",
                "7773456789",
                "762345679",
                "fatima.diop@example.com",
                "12 Thiaroye",
                "Dakar",
                "Sénégal",
                "PENDING_ACTIVATION",
                "Documents vérifiés et en attente d'activation",
                "2023-02-01T16:01:00",
                "path/to/identity/document2",
                "path/to/diploma2",
                "path/to/license2",
                "path/to/professional/insurance2",
                "path/to/bank/account/number2");

        when(professionalRepository.findAll()).thenReturn(List.of(professional1, professional2));

        List<Professional> professionals = professionalService.findAll();

        assertThat(professionals).hasSize(2).containsExactly(professional1, professional2);
    }

    @Test
    void shouldGetProfessionalById() {
        Professional professional = new Professional(
                "Saliou",
                "Diop",
                "Medecin Chirurgien",
                "123456789",
                "+221772345678",
                "saliou.diop@example.com",
                "123 Keur Massar",
                "Dakar",
                "Sénégal",
                "ACTIVE",
                "Documents validés",
                "2023-01-01T16:00:00",
                "path/to/identity/document1",
                "path/to/diploma1",
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/bank/account/number1");

        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));

        Professional result = professionalService.findById(1L);

        assertThat(result).isEqualTo(professional);
    }

    @Test
    void shouldReturnProfessionalOnSaveProfessional() {
        // Préparation des données de test
        ProfessionalDto professionalDto = new ProfessionalDto(
                "Saliou",
                "Diop",
                "Medecin Chirurgien",
                "123456789",
                "+221772345678",
                "saliou.diop@example.com",
                "123 Keur Massar",
                "Dakar",
                "Sénégal",
                "path/to/identity/document1",
                "path/to/diploma1",
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/bank/account/number1");
        
        // Création d'un objet Professional attendu après la sauvegarde
        Professional expectedProfessional = new Professional();
        expectedProfessional.setFirstName("Saliou");
        expectedProfessional.setLastName("Diop");
        expectedProfessional.setSpeciality("Medecin Chirurgien");
        expectedProfessional.setRegistrationNumber("123456789");
        expectedProfessional.setEmail("saliou.diop@example.com");
        expectedProfessional.setPhone("+221772345678");
        expectedProfessional.setAddress("123 Keur Massar");
        expectedProfessional.setCity("Dakar");
        expectedProfessional.setCountry("Sénégal");
        expectedProfessional.setIdentityDocumentPath("path/to/identity/document1");
        expectedProfessional.setDiplomaPath("path/to/diploma1");
        expectedProfessional.setLicensePath("path/to/license1");
        expectedProfessional.setProfessionalInsurancePath("path/to/professional/insurance1");
        expectedProfessional.setBankAccountNumberPath("path/to/bank/account/number1");
        expectedProfessional.setAccountStatus(AccountStatus.PENDING_VERIFICATION);
        expectedProfessional.setStatusChangeDate(LocalDateTime.now());
        expectedProfessional.setStatusChangeReason("En attente de vérification des documents");
        
        // Configuration du mock pour capturer l'objet Professional qui sera sauvegardé
        ArgumentCaptor<Professional> professionalCaptor = ArgumentCaptor.forClass(Professional.class);
        when(professionalRepository.save(professionalCaptor.capture())).thenReturn(expectedProfessional);
        
        // Exécution de la méthode à tester
        Professional result = professionalService.createProfessional(professionalDto);
        
        // Vérification des résultats
        assertThat(result).isEqualTo(expectedProfessional);
        
        // Vérification que les données du DTO ont été correctement transférées à l'objet Professional
        Professional savedProfessional = professionalCaptor.getValue();
        assertThat(savedProfessional.getFirstName()).isEqualTo("Saliou");
        assertThat(savedProfessional.getLastName()).isEqualTo("Diop");
        assertThat(savedProfessional.getSpeciality()).isEqualTo("Medecin Chirurgien");
        assertThat(savedProfessional.getRegistrationNumber()).isEqualTo("123456789");
        assertThat(savedProfessional.getEmail()).isEqualTo("saliou.diop@example.com");
        assertThat(savedProfessional.getPhone()).isEqualTo("+221772345678");
        assertThat(savedProfessional.getAccountStatus()).isEqualTo(AccountStatus.PENDING_VERIFICATION);
    }

    @Test
    void shouldReturnProfessionalOnUpdateProfessional() {
        // Création d'un professionnel existant avec un ID
        Professional existingProfessional = new Professional(
                "Saliou",
                "Diop",
                "Medecin Chirurgien",
                "123456789",
                "+221772345678",
                "saliou.diop@example.com",
                "123 Keur Massar",
                "Dakar",
                "Sénégal",
                "ACTIVE",
                "Documents validés",
                "2023-01-01T16:00:00",
                "path/to/identity/document1",
                "path/to/diploma1",
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/bank/account/number1");
        existingProfessional.setId(1L); // Définir un ID
        
        // DTO avec les nouvelles valeurs à mettre à jour
        ProfessionalDto updateDto = new ProfessionalDto(
            "Serigne Saliou", // Nouveau prénom
            "Diop",
            "Medecin Chirurgien",
            "123456789",
            "+221772345678",
            "saliou.diop@example.com",
            "123 Keur Massar",
            "Dakar",
            "Sénégal",
            "path/to/identity/document1",
            "path/to/diploma1",
            "path/to/license1",
            "path/to/professional/insurance1",
            "path/to/bank/account/number1");

        // Professionnel mis à jour attendu
        Professional updatedProfessional = new Professional(
            "Serigne Saliou", // Nouveau prénom
            "Diop",
            "Medecin Chirurgien",
            "123456789",
            "+221772345678",
            "saliou.diop@example.com",
            "123 Keur Massar",
            "Dakar",
            "Sénégal",
            "ACTIVE",
            "Documents validés",
            "2023-01-01T16:00:00",
            "path/to/identity/document1",
            "path/to/diploma1",
            "path/to/license1",
            "path/to/professional/insurance1",
            "path/to/bank/account/number1");
        updatedProfessional.setId(1L); // Même ID que l'existant
        
        // Mock du repository pour retourner le professionnel existant quand findById est appelé
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(existingProfessional));
        
        // Mock du repository pour retourner le professionnel mis à jour quand save est appelé
        when(professionalRepository.save(any(Professional.class))).thenReturn(updatedProfessional);
        
        // Exécution de la méthode à tester
        Professional result = professionalService.updateProfessional(1L, updateDto);
        
        // Vérification des résultats
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Serigne Saliou");
        
        // Vérifier que le repository a été appelé avec les bonnes méthodes
        verify(professionalRepository).findById(1L);
        verify(professionalRepository).save(any(Professional.class));
    }

    @Test 
    void shouldReturnProfessionalOnFindByEmail() {
        Professional professional = new Professional(
                "Saliou",
                "Diop",
                "Medecin Chirurgien",
                "123456789",
                "+221772345678",
                "saliou.diop@example.com",
                "123 Keur Massar",
                "Dakar",
                "Sénégal",
                "ACTIVE",
                "Documents validés",
                "2023-01-01T16:00:00",
                "path/to/identity/document1",
                "path/to/diploma1",
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/bank/account/number1");
        
        when(professionalRepository.findByEmail("saliou.diop@example.com")).thenReturn(Optional.of(professional));
        
        Professional result = professionalService.findByEmail("saliou.diop@example.com").get();
        
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(professional);
    }

    @Test 
    void shouldReturnProfessionalOnFindByPhone() {
        Professional professional = new Professional(
                "Saliou",
                "Diop",
                "Medecin Chirurgien",
                "123456789",
                "+221772345678",
                "saliou.diop@example.com",
                "123 Keur Massar",
                "Dakar",
                "Sénégal",
                "ACTIVE",
                "Documents validés",
                "2023-01-01T16:00:00",
                "path/to/identity/document1",
                "path/to/diploma1",
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/bank/account/number1");
        when(professionalRepository.findByPhone("+221772345678")).thenReturn(Optional.of(professional));

        Professional result = professionalService.findByPhone("+221772345678").get();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(professional);
    }

    @Test
    void shouldReturnProfessionalOnFindByRegistrationNumber() {
        Professional professional = new Professional(
                "Saliou",
                "Diop",
                "Medecin Chirurgien",
                "123456789",
                "+221772345678",
                "saliou.diop@example.com",
                "123 Keur Massar",
                "Dakar",
                "Sénégal",
                "ACTIVE",
                "Documents validés",
                "2023-01-01T16:00:00",
                "path/to/identity/document1",
                "path/to/diploma1",
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/bank/account/number1");
        when(professionalRepository.findByRegistrationNumber("123456789")).thenReturn(Optional.of(professional));

        Professional result = professionalService.findByRegistrationNumber("123456789").get();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(professional);
    }

    @Test 
    void shouldReturnListOnFindBySpeciality() {
        Professional professional1 = new Professional(
                "Saliou",
                "Diop",
                "Medecin Chirurgien",
                "123456789",
                "+221772345678",
                "saliou.diop@example.com",
                "123 Keur Massar",
                "Dakar",
                "Sénégal",
                "ACTIVE",
                "Documents validés",
                "2023-01-01T16:00:00",
                "path/to/identity/document1",
                "path/to/diploma1",
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/bank/account/number1");
        Professional professional2 = new Professional(
                "Fatima",
                "Diop",
                "Medecin Chirurgien",
                "7773456789",
                "762345679",
                "fatima.diop@example.com",
                "12 Thiaroye",
                "Dakar",
                "Sénégal",
                "PENDING_ACTIVATION",
                "Documents vérifiés et en attente d'activation",
                "2023-02-01T16:01:00",
                "path/to/identity/document2",
                "path/to/diploma2",
                "path/to/license2",
                "path/to/professional/insurance2",
                "path/to/bank/account/number2");
        
        when(professionalRepository.findBySpeciality("Medecin Chirurgien")).thenReturn(List.of(professional1, professional2));
        
        List<Professional> result = professionalService.findBySpeciality("Medecin Chirurgien");
        
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2).containsExactly(professional1, professional2);
    }

    @Test
    void shouldReturnListOnFindByCity() {
        Professional professional1 = new Professional(
                "Saliou",
                "Diop",
                "Medecin Chirurgien",
                "123456789",
                "+221772345678",
                "saliou.diop@example.com",
                "123 Keur Massar",
                "Dakar",
                "Sénégal",
                "ACTIVE",
                "Documents validés",
                "2023-01-01T16:00:00",
                "path/to/identity/document1",
                "path/to/diploma1",
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/bank/account/number1");
        Professional professional2 = new Professional(
                "Fatima",
                "Diop",
                "Medecin Chirurgien",
                "7773456789",
                "762345679",
                "fatima.diop@example.com",
                "12 Thiaroye",
                "Dakar",
                "Sénégal",
                "PENDING_ACTIVATION",
                "Documents vérifiés et en attente d'activation",
                "2023-02-01T16:01:00",
                "path/to/identity/document2",
                "path/to/diploma2",
                "path/to/license2",
                "path/to/professional/insurance2",
                "path/to/bank/account/number2");
        
        when(professionalRepository.findByCity("Dakar")).thenReturn(List.of(professional1, professional2));
        
        List<Professional> result = professionalService.findByCity("Dakar");
        
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2).containsExactly(professional1, professional2);
    }

    @Test 
    void shouldReturnListOnFindByAccountStatus() {
        Professional professional1 = new Professional(
                "Saliou",
                "Diop",
                "Medecin Chirurgien",
                "123456789",
                "+221772345678",
                "saliou.diop@example.com",
                "123 Keur Massar",
                "Dakar",
                "Sénégal",
                "PENDING_ACTIVATION",
                "Documents vérifiés et en attente d'activation",
                "2023-01-01T16:00:00",
                "path/to/identity/document1",
                "path/to/diploma1",
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/bank/account/number1");
        Professional professional2 = new Professional(
                "Fatima",
                "Diop",
                "Medecin Chirurgien",
                "7773456789",
                "762345679",
                "fatima.diop@example.com",
                "12 Thiaroye",
                "Dakar",
                "Sénégal",
                "PENDING_ACTIVATION",
                "Documents vérifiés et en attente d'activation",
                "2023-02-01T16:01:00",
                "path/to/identity/document2",
                "path/to/diploma2",
                "path/to/license2",
                "path/to/professional/insurance2",
                "path/to/bank/account/number2");
        
        when(professionalRepository.findByAccountStatus(AccountStatus.PENDING_ACTIVATION)).thenReturn(List.of(professional1, professional2));
        
        List<Professional> result = professionalService.findByAccountStatus(AccountStatus.PENDING_ACTIVATION);
        
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2).containsExactly(professional1, professional2);
    }
    
    @Test
    void shouldDeleteProfessional() {
        // Création d'un professionnel existant avec un ID
        Professional professional = new Professional(
                "Saliou",
                "Diop",
                "Medecin Chirurgien",
                "123456789",
                "+221772345678",
                "saliou.diop@example.com",
                "123 Keur Massar",
                "Dakar",
                "Sénégal",
                "ACTIVE",
                "Documents validés",
                "2023-01-01T16:00:00",
                "path/to/identity/document1",
                "path/to/diploma1",
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/bank/account/number1");
        professional.setId(1L);
        
        // Mock du repository pour retourner le professionnel existant
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));
        
        // Capture l'objet Professional qui sera sauvegardé
        ArgumentCaptor<Professional> professionalCaptor = ArgumentCaptor.forClass(Professional.class);
        when(professionalRepository.save(professionalCaptor.capture())).thenReturn(professional);
        
        // Exécution de la méthode à tester
        professionalService.deleteProfessional(1L);
        
        // Vérification que findById a été appelé
        verify(professionalRepository).findById(1L);
        
        // Vérification que save a été appelé
        verify(professionalRepository).save(any(Professional.class));
        
        // Vérification que le statut a été changé à INACTIVE
        Professional savedProfessional = professionalCaptor.getValue();
        assertThat(savedProfessional.getAccountStatus()).isEqualTo(AccountStatus.INACTIVE);
        assertThat(savedProfessional.getStatusChangeReason()).isEqualTo("Suppression du compte");
        assertThat(savedProfessional.getStatusChangeDate()).isNotNull();
    }
    
    
    @Test 
    void shouldThrowProfessionalNotFoundExceptionWhenProfessionalNotFound() {
        when(professionalRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ProfessionalNotFoundException.class, () -> {
            professionalService.findById(1L);
        });
    }
    
    @Test
    void shouldActivateAccount() {
        // Création d'un professionnel existant avec un ID
        Professional professional = new Professional(
                "Saliou",
                "Diop",
                "Medecin Chirurgien",
                "123456789",
                "+221772345678",
                "saliou.diop@example.com",
                "123 Keur Massar",
                "Dakar",
                "Sénégal",
                "PENDING_ACTIVATION", // Statut initial en attente d'activation
                "Documents vérifiés et en attente d'activation",
                "2023-01-01T16:00:00",
                "path/to/identity/document1",
                "path/to/diploma1",
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/bank/account/number1");
        professional.setId(1L);
        
        // Mock du repository pour retourner le professionnel existant
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));
        
        // Capture l'objet Professional qui sera sauvegardé
        ArgumentCaptor<Professional> professionalCaptor = ArgumentCaptor.forClass(Professional.class);
        when(professionalRepository.save(professionalCaptor.capture())).thenReturn(professional);
        
        // Exécution de la méthode à tester
        Professional result = professionalService.activateAccount(1L);
        
        // Vérification que findById a été appelé
        verify(professionalRepository).findById(1L);
        
        // Vérification que save a été appelé
        verify(professionalRepository).save(any(Professional.class));
        
        // Vérification que le statut a été changé à ACTIVE
        Professional savedProfessional = professionalCaptor.getValue();
        assertThat(savedProfessional.getAccountStatus()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(savedProfessional.getStatusChangeReason()).isEqualTo("Compte activé");
        assertThat(savedProfessional.getStatusChangeDate()).isNotNull();
    }
    
    @Test
    void shouldSuspendAccount() {
        // Création d'un professionnel existant avec un ID
        Professional professional = new Professional(
                "Saliou",
                "Diop",
                "Medecin Chirurgien",
                "123456789",
                "+221772345678",
                "saliou.diop@example.com",
                "123 Keur Massar",
                "Dakar",
                "Sénégal",
                "ACTIVE", // Statut initial actif
                "Compte activé",
                "2023-01-01T16:00:00",
                "path/to/identity/document1",
                "path/to/diploma1",
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/bank/account/number1");
        professional.setId(1L);
        
        // Mock du repository pour retourner le professionnel existant
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));
        
        // Capture l'objet Professional qui sera sauvegardé
        ArgumentCaptor<Professional> professionalCaptor = ArgumentCaptor.forClass(Professional.class);
        when(professionalRepository.save(professionalCaptor.capture())).thenReturn(professional);
        
        // Raison de la suspension
        String suspensionReason = "Non-respect des règles déontologiques";
        
        // Exécution de la méthode à tester
        Professional result = professionalService.suspendAccount(1L, suspensionReason);
        
        // Vérification que findById a été appelé
        verify(professionalRepository).findById(1L);
        
        // Vérification que save a été appelé
        verify(professionalRepository).save(any(Professional.class));
        
        // Vérification que le statut a été changé à SUSPENDED
        Professional savedProfessional = professionalCaptor.getValue();
        assertThat(savedProfessional.getAccountStatus()).isEqualTo(AccountStatus.SUSPENDED);
        assertThat(savedProfessional.getStatusChangeReason()).isEqualTo(suspensionReason);
        assertThat(savedProfessional.getStatusChangeDate()).isNotNull();
    }
}