package com.armacare.Controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.armacare.controller.ProfessionalController;
import com.armacare.dto.ProfessionalDto;
import com.armacare.model.Professional;
import com.armacare.model.Professional.AccountStatus;
import com.armacare.service.ProfessionalService;

@WebMvcTest(ProfessionalController.class)
public class ProfessionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfessionalService professionalService;

    @Test
    void shouldGetAllProfessionals() throws Exception {
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
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/diploma1",
                "path/to/bank/account/number1");
        Professional professional2 = new Professional(
                "Fatima",
                "Diop",
                "Medecin Gynécologue",
                "7773456789",
                "762345679",
                "fatima.diop@example.com",
                "12 Thiaroye",
                "Dakar",
                "Sénégal",
                "PENDING_VERIFICATION",
                "En attente de vérification des documents",
                "2023-02-01T16:01:00",
                "path/to/identity/document2",
                "path/to/license2",
                "path/to/professional/insurance2",
                "path/to/diploma2",
                "path/to/bank/account/number2");

        when(professionalService.findAll()).thenReturn(List.of(professional1, professional2));

        mockMvc.perform(get("/api/professionals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("Saliou"))
                .andExpect(jsonPath("$[0].lastName").value("Diop"))
                .andExpect(jsonPath("$[0].speciality").value("Medecin Chirurgien"))
                .andExpect(jsonPath("$[0].registrationNumber").value("123456789"))
                .andExpect(jsonPath("$[0].email").value("saliou.diop@example.com"))
                .andExpect(jsonPath("$[0].phone").value("+221772345678"))
                .andExpect(jsonPath("$[0].address").value("123 Keur Massar"))
                .andExpect(jsonPath("$[0].city").value("Dakar"))
                .andExpect(jsonPath("$[0].country").value("Sénégal"))
                .andExpect(jsonPath("$[0].accountStatus").value("ACTIVE"))
                .andExpect(jsonPath("$[0].statusChangeReason").value("Documents validés"))
                .andExpect(jsonPath("$[0].statusChangeDate").value("2023-01-01T16:00:00"))
                .andExpect(jsonPath("$[0].identityDocumentPath").value("path/to/identity/document1"))
                .andExpect(jsonPath("$[0].diplomaPath").value("path/to/diploma1"))
                .andExpect(jsonPath("$[0].licensePath").value("path/to/license1"))
                .andExpect(jsonPath("$[0].professionalInsurancePath").value("path/to/professional/insurance1"))
                .andExpect(jsonPath("$[0].bankAccountNumberPath").value("path/to/bank/account/number1"))
                .andExpect(jsonPath("$[1].firstName").value("Fatima"))
                .andExpect(jsonPath("$[1].lastName").value("Diop"))
                .andExpect(jsonPath("$[1].speciality").value("Medecin Gynécologue"))
                .andExpect(jsonPath("$[1].registrationNumber").value("7773456789"))
                .andExpect(jsonPath("$[1].email").value("fatima.diop@example.com"))
                .andExpect(jsonPath("$[1].phone").value("762345679"))
                .andExpect(jsonPath("$[1].address").value("12 Thiaroye"))
                .andExpect(jsonPath("$[1].city").value("Dakar"))
                .andExpect(jsonPath("$[1].country").value("Sénégal"))
                .andExpect(jsonPath("$[1].accountStatus").value("PENDING_VERIFICATION"))
                .andExpect(jsonPath("$[1].statusChangeReason").value("En attente de vérification des documents"))
                .andExpect(jsonPath("$[1].statusChangeDate").value("2023-02-01T16:01:00"))
                .andExpect(jsonPath("$[1].identityDocumentPath").value("path/to/identity/document2"))
                .andExpect(jsonPath("$[1].diplomaPath").value("path/to/diploma2"))
                .andExpect(jsonPath("$[1].licensePath").value("path/to/license2"))
                .andExpect(jsonPath("$[1].professionalInsurancePath").value("path/to/professional/insurance2"))
                .andExpect(jsonPath("$[1].bankAccountNumberPath").value("path/to/bank/account/number2"));

    }

    @Test
    void shouldGetProfessionalById() throws Exception {
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
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/diploma1",
                "path/to/bank/account/number1");
        professional.setId(1L);

        when(professionalService.findById(1L)).thenReturn(professional);

        mockMvc.perform(get("/api/professionals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Saliou"))
                .andExpect(jsonPath("$.lastName").value("Diop"))
                .andExpect(jsonPath("$.speciality").value("Medecin Chirurgien"))
                .andExpect(jsonPath("$.registrationNumber").value("123456789"))
                .andExpect(jsonPath("$.email").value("saliou.diop@example.com"))
                .andExpect(jsonPath("$.phone").value("+221772345678"))
                .andExpect(jsonPath("$.address").value("123 Keur Massar"))
                .andExpect(jsonPath("$.city").value("Dakar"))
                .andExpect(jsonPath("$.country").value("Sénégal"))
                .andExpect(jsonPath("$.accountStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.statusChangeReason").value("Documents validés"))
                .andExpect(jsonPath("$.statusChangeDate").value("2023-01-01T16:00:00"))
                .andExpect(jsonPath("$.identityDocumentPath").value("path/to/identity/document1"))
                .andExpect(jsonPath("$.diplomaPath").value("path/to/diploma1"))
                .andExpect(jsonPath("$.licensePath").value("path/to/license1"))
                .andExpect(jsonPath("$.professionalInsurancePath").value("path/to/professional/insurance1"))
                .andExpect(jsonPath("$.bankAccountNumberPath").value("path/to/bank/account/number1"));
    }

    @Test
    void shouldGetProfessionalByPhone() throws Exception {
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
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/diploma1",
                "path/to/bank/account/number1");

        when(professionalService.findByPhone("+221772345678")).thenReturn(Optional.of(professional));

        mockMvc.perform(get("/api/professionals/by-phone?phone=+221772345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Saliou"))
                .andExpect(jsonPath("$.lastName").value("Diop"))
                .andExpect(jsonPath("$.speciality").value("Medecin Chirurgien"))
                .andExpect(jsonPath("$.registrationNumber").value("123456789"))
                .andExpect(jsonPath("$.email").value("saliou.diop@example.com"))
                .andExpect(jsonPath("$.phone").value("+221772345678"))
                .andExpect(jsonPath("$.address").value("123 Keur Massar"))
                .andExpect(jsonPath("$.city").value("Dakar"))
                .andExpect(jsonPath("$.country").value("Sénégal"))
                .andExpect(jsonPath("$.accountStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.statusChangeReason").value("Documents validés"))
                .andExpect(jsonPath("$.statusChangeDate").value("2023-01-01T16:00:00"))
                .andExpect(jsonPath("$.identityDocumentPath").value("path/to/identity/document1"))
                .andExpect(jsonPath("$.diplomaPath").value("path/to/diploma1"))
                .andExpect(jsonPath("$.licensePath").value("path/to/license1"))
                .andExpect(jsonPath("$.professionalInsurancePath").value("path/to/professional/insurance1"))
                .andExpect(jsonPath("$.bankAccountNumberPath").value("path/to/bank/account/number1"));
    }

    @Test
    void shouldGetProfessionalByEmail() throws Exception {
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
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/diploma1",
                "path/to/bank/account/number1");

        when(professionalService.findByEmail("saliou.diop@example.com")).thenReturn(Optional.of(professional));

        mockMvc.perform(get("/api/professionals/by-email?email=saliou.diop@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Saliou"))
                .andExpect(jsonPath("$.lastName").value("Diop"))
                .andExpect(jsonPath("$.speciality").value("Medecin Chirurgien"))
                .andExpect(jsonPath("$.registrationNumber").value("123456789"))
                .andExpect(jsonPath("$.email").value("saliou.diop@example.com"))
                .andExpect(jsonPath("$.phone").value("+221772345678"))
                .andExpect(jsonPath("$.address").value("123 Keur Massar"))
                .andExpect(jsonPath("$.city").value("Dakar"))
                .andExpect(jsonPath("$.country").value("Sénégal"))
                .andExpect(jsonPath("$.accountStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.statusChangeReason").value("Documents validés"))
                .andExpect(jsonPath("$.statusChangeDate").value("2023-01-01T16:00:00"))
                .andExpect(jsonPath("$.identityDocumentPath").value("path/to/identity/document1"))
                .andExpect(jsonPath("$.diplomaPath").value("path/to/diploma1"))
                .andExpect(jsonPath("$.licensePath").value("path/to/license1"))
                .andExpect(jsonPath("$.professionalInsurancePath").value("path/to/professional/insurance1"))
                .andExpect(jsonPath("$.bankAccountNumberPath").value("path/to/bank/account/number1"));
    }

    @Test
    void shouldGetProfessionalByRegistrationNumber() throws Exception {
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
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/diploma1",
                "path/to/bank/account/number1");

        when(professionalService.findByRegistrationNumber("123456789")).thenReturn(Optional.of(professional));

        mockMvc.perform(get("/api/professionals/by-registration-number?registrationNumber=123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Saliou"))
                .andExpect(jsonPath("$.lastName").value("Diop"))
                .andExpect(jsonPath("$.speciality").value("Medecin Chirurgien"))
                .andExpect(jsonPath("$.registrationNumber").value("123456789"))
                .andExpect(jsonPath("$.email").value("saliou.diop@example.com"))
                .andExpect(jsonPath("$.phone").value("+221772345678"))
                .andExpect(jsonPath("$.address").value("123 Keur Massar"))
                .andExpect(jsonPath("$.city").value("Dakar"))
                .andExpect(jsonPath("$.country").value("Sénégal"))
                .andExpect(jsonPath("$.accountStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.statusChangeReason").value("Documents validés"))
                .andExpect(jsonPath("$.statusChangeDate").value("2023-01-01T16:00:00"))
                .andExpect(jsonPath("$.identityDocumentPath").value("path/to/identity/document1"))
                .andExpect(jsonPath("$.diplomaPath").value("path/to/diploma1"))
                .andExpect(jsonPath("$.licensePath").value("path/to/license1"))
                .andExpect(jsonPath("$.professionalInsurancePath").value("path/to/professional/insurance1"))
                .andExpect(jsonPath("$.bankAccountNumberPath").value("path/to/bank/account/number1"));
    }

    @Test
    void shouldGetProfessionalsBySpeciality() throws Exception {
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
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/diploma1",
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
                "PENDING_VERIFICATION",
                "En attente de vérification des documents",
                "2023-02-01T16:01:00",
                "path/to/identity/document2",
                "path/to/license2",
                "path/to/professional/insurance2",
                "path/to/diploma2",
                "path/to/bank/account/number2");

        when(professionalService.findBySpeciality("Medecin Chirurgien")).thenReturn(List.of(professional1, professional2));

        mockMvc.perform(get("/api/professionals/by-speciality?speciality=Medecin Chirurgien"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("Saliou"))
                .andExpect(jsonPath("$[0].lastName").value("Diop"))
                .andExpect(jsonPath("$[0].speciality").value("Medecin Chirurgien"))
                .andExpect(jsonPath("$[0].registrationNumber").value("123456789"))
                .andExpect(jsonPath("$[0].email").value("saliou.diop@example.com"))
                .andExpect(jsonPath("$[0].phone").value("+221772345678"))
                .andExpect(jsonPath("$[0].address").value("123 Keur Massar"))
                .andExpect(jsonPath("$[0].city").value("Dakar"))
                .andExpect(jsonPath("$[0].country").value("Sénégal"))
                .andExpect(jsonPath("$[0].accountStatus").value("ACTIVE"))
                .andExpect(jsonPath("$[0].statusChangeReason").value("Documents validés"))
                .andExpect(jsonPath("$[0].statusChangeDate").value("2023-01-01T16:00:00"))
                .andExpect(jsonPath("$[0].identityDocumentPath").value("path/to/identity/document1"))
                .andExpect(jsonPath("$[0].diplomaPath").value("path/to/diploma1"))
                .andExpect(jsonPath("$[0].licensePath").value("path/to/license1"))
                .andExpect(jsonPath("$[0].professionalInsurancePath").value("path/to/professional/insurance1"))
                .andExpect(jsonPath("$[0].bankAccountNumberPath").value("path/to/bank/account/number1"))
                .andExpect(jsonPath("$[1].firstName").value("Fatima"))
                .andExpect(jsonPath("$[1].lastName").value("Diop"))
                .andExpect(jsonPath("$[1].speciality").value("Medecin Chirurgien"))
                .andExpect(jsonPath("$[1].registrationNumber").value("7773456789"))
                .andExpect(jsonPath("$[1].email").value("fatima.diop@example.com"))
                .andExpect(jsonPath("$[1].phone").value("762345679"))
                .andExpect(jsonPath("$[1].address").value("12 Thiaroye"))
                .andExpect(jsonPath("$[1].city").value("Dakar"))
                .andExpect(jsonPath("$[1].country").value("Sénégal"))
                .andExpect(jsonPath("$[1].accountStatus").value("PENDING_VERIFICATION"))
                .andExpect(jsonPath("$[1].statusChangeReason").value("En attente de vérification des documents"))
                .andExpect(jsonPath("$[1].statusChangeDate").value("2023-02-01T16:01:00"))
                .andExpect(jsonPath("$[1].identityDocumentPath").value("path/to/identity/document2"))
                .andExpect(jsonPath("$[1].diplomaPath").value("path/to/diploma2"))
                .andExpect(jsonPath("$[1].licensePath").value("path/to/license2"))
                .andExpect(jsonPath("$[1].professionalInsurancePath").value("path/to/professional/insurance2"))
                .andExpect(jsonPath("$[1].bankAccountNumberPath").value("path/to/bank/account/number2"));
    }

    @Test
    void shouldGetProfessionalsByCity() throws Exception {
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
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/diploma1",
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
                "PENDING_VERIFICATION",
                "En attente de vérification des documents",
                "2023-02-01T16:01:00",
                "path/to/identity/document2",
                "path/to/license2",
                "path/to/professional/insurance2",
                "path/to/diploma2",
                "path/to/bank/account/number2");

        when(professionalService.findByCity("Dakar")).thenReturn(List.of(professional1, professional2));

        mockMvc.perform(get("/api/professionals/by-city?city=Dakar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("Saliou"))
                .andExpect(jsonPath("$[0].lastName").value("Diop"))
                .andExpect(jsonPath("$[0].speciality").value("Medecin Chirurgien"))
                .andExpect(jsonPath("$[0].registrationNumber").value("123456789"))
                .andExpect(jsonPath("$[0].email").value("saliou.diop@example.com"))
                .andExpect(jsonPath("$[0].phone").value("+221772345678"))
                .andExpect(jsonPath("$[0].address").value("123 Keur Massar"))
                .andExpect(jsonPath("$[0].city").value("Dakar"))
                .andExpect(jsonPath("$[0].country").value("Sénégal"))
                .andExpect(jsonPath("$[0].accountStatus").value("ACTIVE"))
                .andExpect(jsonPath("$[0].statusChangeReason").value("Documents validés"))
                .andExpect(jsonPath("$[0].statusChangeDate").value("2023-01-01T16:00:00"))
                .andExpect(jsonPath("$[0].identityDocumentPath").value("path/to/identity/document1"))
                .andExpect(jsonPath("$[0].diplomaPath").value("path/to/diploma1"))
                .andExpect(jsonPath("$[0].licensePath").value("path/to/license1"))
                .andExpect(jsonPath("$[0].professionalInsurancePath").value("path/to/professional/insurance1"))
                .andExpect(jsonPath("$[0].bankAccountNumberPath").value("path/to/bank/account/number1"))
                .andExpect(jsonPath("$[1].firstName").value("Fatima"))
                .andExpect(jsonPath("$[1].lastName").value("Diop"))
                .andExpect(jsonPath("$[1].speciality").value("Medecin Chirurgien"))
                .andExpect(jsonPath("$[1].registrationNumber").value("7773456789"))
                .andExpect(jsonPath("$[1].email").value("fatima.diop@example.com"))
                .andExpect(jsonPath("$[1].phone").value("762345679"))
                .andExpect(jsonPath("$[1].address").value("12 Thiaroye"))
                .andExpect(jsonPath("$[1].city").value("Dakar"))
                .andExpect(jsonPath("$[1].country").value("Sénégal"))
                .andExpect(jsonPath("$[1].accountStatus").value("PENDING_VERIFICATION"))
                .andExpect(jsonPath("$[1].statusChangeReason").value("En attente de vérification des documents"))
                .andExpect(jsonPath("$[1].statusChangeDate").value("2023-02-01T16:01:00"))
                .andExpect(jsonPath("$[1].identityDocumentPath").value("path/to/identity/document2"))
                .andExpect(jsonPath("$[1].diplomaPath").value("path/to/diploma2"))
                .andExpect(jsonPath("$[1].licensePath").value("path/to/license2"))
                .andExpect(jsonPath("$[1].professionalInsurancePath").value("path/to/professional/insurance2"))
                .andExpect(jsonPath("$[1].bankAccountNumberPath").value("path/to/bank/account/number2"));
    }

    @Test
    void shouldGetProfessionalsByAccountStatus() throws Exception {
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
                "PENDING_VERIFICATION",
                "En attente de vérification des documents",
                "2023-01-01T16:00:00",
                "path/to/identity/document1",
                "path/to/license1",
                "path/to/professional/insurance1",
                "path/to/diploma1",
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
                "PENDING_VERIFICATION",
                "En attente de vérification des documents",
                "2023-02-01T16:01:00",
                "path/to/identity/document2",
                "path/to/license2",
                "path/to/professional/insurance2",
                "path/to/diploma2",
                "path/to/bank/account/number2");

        when(professionalService.findByAccountStatus(AccountStatus.PENDING_VERIFICATION)).thenReturn(List.of(professional1, professional2));

        mockMvc.perform(get("/api/professionals/by-status?status=PENDING_VERIFICATION"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("Saliou"))
                .andExpect(jsonPath("$[0].lastName").value("Diop"))
                .andExpect(jsonPath("$[0].speciality").value("Medecin Chirurgien"))
                .andExpect(jsonPath("$[0].registrationNumber").value("123456789"))
                .andExpect(jsonPath("$[0].email").value("saliou.diop@example.com"))
                .andExpect(jsonPath("$[0].phone").value("+221772345678"))
                .andExpect(jsonPath("$[0].address").value("123 Keur Massar"))
                .andExpect(jsonPath("$[0].city").value("Dakar"))
                .andExpect(jsonPath("$[0].country").value("Sénégal"))
                .andExpect(jsonPath("$[0].accountStatus").value("PENDING_VERIFICATION"))
                .andExpect(jsonPath("$[0].statusChangeReason").value("En attente de vérification des documents"))
                .andExpect(jsonPath("$[0].statusChangeDate").value("2023-01-01T16:00:00"))
                .andExpect(jsonPath("$[0].identityDocumentPath").value("path/to/identity/document1"))
                .andExpect(jsonPath("$[0].diplomaPath").value("path/to/diploma1"))
                .andExpect(jsonPath("$[0].licensePath").value("path/to/license1"))
                .andExpect(jsonPath("$[0].professionalInsurancePath").value("path/to/professional/insurance1"))
                .andExpect(jsonPath("$[0].bankAccountNumberPath").value("path/to/bank/account/number1"))
                .andExpect(jsonPath("$[1].firstName").value("Fatima"))
                .andExpect(jsonPath("$[1].lastName").value("Diop"))
                .andExpect(jsonPath("$[1].speciality").value("Medecin Chirurgien"))
                .andExpect(jsonPath("$[1].registrationNumber").value("7773456789"))
                .andExpect(jsonPath("$[1].email").value("fatima.diop@example.com"))
                .andExpect(jsonPath("$[1].phone").value("762345679"))
                .andExpect(jsonPath("$[1].address").value("12 Thiaroye"))
                .andExpect(jsonPath("$[1].city").value("Dakar"))
                .andExpect(jsonPath("$[1].country").value("Sénégal"))
                .andExpect(jsonPath("$[1].accountStatus").value("PENDING_VERIFICATION"))
                .andExpect(jsonPath("$[1].statusChangeReason").value("En attente de vérification des documents"))
                .andExpect(jsonPath("$[1].statusChangeDate").value("2023-02-01T16:01:00"))
                .andExpect(jsonPath("$[1].identityDocumentPath").value("path/to/identity/document2"))
                .andExpect(jsonPath("$[1].diplomaPath").value("path/to/diploma2"))
                .andExpect(jsonPath("$[1].licensePath").value("path/to/license2"))
                .andExpect(jsonPath("$[1].professionalInsurancePath").value("path/to/professional/insurance2"))
                .andExpect(jsonPath("$[1].bankAccountNumberPath").value("path/to/bank/account/number2"));
    }

    @Test
    void shouldCreateProfessional() throws Exception {
        String json = """
                {
                "firstName": "Saliou",
                "lastName": "Diop",
                "speciality": "Medecin Chirurgien",
                "registrationNumber": "123456789",
                "email": "saliou.diop@example.com",
                "phone": "+221772345678",
                "address": "123 Keur Massar",
                "city": "Dakar",
                "country": "Sénégal",
                "identityDocumentPath": "path/to/identity/document1",
                "diplomaPath": "path/to/diploma1",
                "licensePath": "path/to/license1",
                "professionalInsurancePath": "path/to/professional/insurance1",
                "bankAccountNumberPath": "path/to/bank/account/number1"
                }
                """;

        Professional professional = new Professional();
        professional.setId(1L);
        professional.setFirstName("Saliou");
        professional.setLastName("Diop");
        professional.setSpeciality("Medecin Chirurgien");
        professional.setRegistrationNumber("123456789");
        professional.setEmail("saliou.diop@example.com");
        professional.setPhone("+221772345678");
        professional.setAddress("123 Keur Massar");
        professional.setCity("Dakar");
        professional.setCountry("Sénégal");
        professional.setIdentityDocumentPath("path/to/identity/document1");
        professional.setDiplomaPath("path/to/diploma1");
        professional.setLicensePath("path/to/license1");
        professional.setProfessionalInsurancePath("path/to/professional/insurance1");
        professional.setBankAccountNumberPath("path/to/bank/account/number1");
        professional.setAccountStatus(AccountStatus.PENDING_VERIFICATION);
        professional.setStatusChangeReason("En attente de vérification des documents");

        // Configurer le mock pour retourner un professionnel
        when(professionalService.createProfessional(org.mockito.ArgumentMatchers.any(ProfessionalDto.class)))
            .thenReturn(professional);

        // Exécuter la requête et vérifier le statut HTTP et quelques champs de base
        mockMvc.perform(post("/api/professionals")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Saliou"))
                .andExpect(jsonPath("$.lastName").value("Diop"))
                .andExpect(jsonPath("$.speciality").value("Medecin Chirurgien"))
                .andExpect(jsonPath("$.registrationNumber").value("123456789"))
                .andExpect(jsonPath("$.email").value("saliou.diop@example.com"))
                .andExpect(jsonPath("$.phone").value("+221772345678"))
                .andExpect(jsonPath("$.address").value("123 Keur Massar"))
                .andExpect(jsonPath("$.city").value("Dakar"))
                .andExpect(jsonPath("$.country").value("Sénégal"))
                .andExpect(jsonPath("$.identityDocumentPath").value("path/to/identity/document1"))
                .andExpect(jsonPath("$.diplomaPath").value("path/to/diploma1"))
                .andExpect(jsonPath("$.licensePath").value("path/to/license1"))
                .andExpect(jsonPath("$.professionalInsurancePath").value("path/to/professional/insurance1"))
                .andExpect(jsonPath("$.bankAccountNumberPath").value("path/to/bank/account/number1"))
                .andExpect(jsonPath("$.accountStatus").value("PENDING_VERIFICATION"))
                .andExpect(jsonPath("$.statusChangeReason").value("En attente de vérification des documents"))
                .andExpect(jsonPath("$.statusChangeDate").isNotEmpty());
                
    }

    @Test
    void shouldUpdateProfessional() throws Exception {
        String json = """
                {
                "firstName": "Saliou",
                "lastName": "Diop",
                "speciality": "Medecin Chirurgien",
                "registrationNumber": "123456789",
                "email": "saliou.diop@example.com",
                "phone": "+221772345678",
                "address": "123 Keur Massar",
                "city": "Dakar",
                "country": "Sénégal",
                "accountStatus": "ACTIVE",
                "statusChangeReason": "Documents validés",
                "statusChangeDate": "2023-01-01T16:00:00",
                "identityDocumentPath": "path/to/identity/document1",
                "diplomaPath": "path/to/diploma1",
                "licensePath": "path/to/license1",
                "professionalInsurancePath": "path/to/professional/updated/insurance1",
                "bankAccountNumberPath": "path/to/bank/updated/account/number1"
                }
                """;
                Professional existingProfessional = new Professional();
                existingProfessional.setId(1L);
                existingProfessional.setFirstName("Saliou");
                existingProfessional.setLastName("Diop");
                existingProfessional.setSpeciality("Medecin Chirurgien");
                existingProfessional.setRegistrationNumber("123456789");
                existingProfessional.setEmail("saliou.diop@example.com");
                existingProfessional.setPhone("+221772345678");
                existingProfessional.setAddress("123 Keur Massar");
                existingProfessional.setCity("Dakar");
                existingProfessional.setCountry("Sénégal");
                existingProfessional.setAccountStatus(AccountStatus.ACTIVE);
                existingProfessional.setStatusChangeReason("Documents validés");
                existingProfessional.setStatusChangeDate(LocalDateTime.parse("2023-01-01T16:00:00"));
                existingProfessional.setIdentityDocumentPath("path/to/identity/document1");
                existingProfessional.setDiplomaPath("path/to/diploma1");
                existingProfessional.setLicensePath("path/to/license1");
                existingProfessional.setProfessionalInsurancePath("path/to/professional/insurance1");
                existingProfessional.setBankAccountNumberPath("path/to/bank/account/number1");

                Professional updatedProfessional = new Professional();
                updatedProfessional.setId(1L);
                updatedProfessional.setFirstName("Saliou");
                updatedProfessional.setLastName("Diop");
                updatedProfessional.setSpeciality("Medecin Chirurgien");
                updatedProfessional.setRegistrationNumber("123456789");
                updatedProfessional.setEmail("saliou.diop@example.com");
                updatedProfessional.setPhone("+221772345678");
                updatedProfessional.setAddress("123 Keur Massar");
                updatedProfessional.setCity("Dakar");
                updatedProfessional.setCountry("Sénégal");
                updatedProfessional.setAccountStatus(AccountStatus.ACTIVE);
                updatedProfessional.setStatusChangeReason("Documents validés");
                updatedProfessional.setIdentityDocumentPath("path/to/identity/document1");
                updatedProfessional.setDiplomaPath("path/to/diploma1");
                updatedProfessional.setLicensePath("path/to/license1");
                updatedProfessional.setProfessionalInsurancePath("path/to/professional/updated/insurance1");
                updatedProfessional.setBankAccountNumberPath("path/to/bank/updated/account/number1");

                when(professionalService.findById(1L)).thenReturn(existingProfessional);
                when(professionalService.updateProfessional(eq(1L), any(ProfessionalDto.class)))
                    .thenReturn(updatedProfessional);

                mockMvc.perform(put("/api/professionals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1L))
                        .andExpect(jsonPath("$.firstName").value("Saliou"))
                        .andExpect(jsonPath("$.lastName").value("Diop"))
                        .andExpect(jsonPath("$.speciality").value("Medecin Chirurgien"))
                        .andExpect(jsonPath("$.registrationNumber").value("123456789"))
                        .andExpect(jsonPath("$.email").value("saliou.diop@example.com"))
                        .andExpect(jsonPath("$.phone").value("+221772345678"))
                        .andExpect(jsonPath("$.address").value("123 Keur Massar"))
                        .andExpect(jsonPath("$.city").value("Dakar"))
                        .andExpect(jsonPath("$.country").value("Sénégal"))
                        .andExpect(jsonPath("$.accountStatus").value("ACTIVE"))
                        .andExpect(jsonPath("$.statusChangeReason").value("Documents validés"))
                        .andExpect(jsonPath("$.statusChangeDate").isNotEmpty())
                        .andExpect(jsonPath("$.identityDocumentPath").value("path/to/identity/document1"))
                        .andExpect(jsonPath("$.diplomaPath").value("path/to/diploma1"))
                        .andExpect(jsonPath("$.licensePath").value("path/to/license1"))
                        .andExpect(jsonPath("$.professionalInsurancePath").value("path/to/professional/updated/insurance1"))
                        .andExpect(jsonPath("$.bankAccountNumberPath").value("path/to/bank/updated/account/number1"));
    }
    @Test 
    void shouldDeleteProfessional() throws Exception {
        Professional professional = new Professional();
                professional.setId(1L);
                professional.setFirstName("Saliou");
                professional.setLastName("Diop");
                professional.setSpeciality("Medecin Chirurgien");
                professional.setRegistrationNumber("123456789");
                professional.setEmail("saliou.diop@example.com");
                professional.setPhone("+221772345678");
                professional.setAddress("123 Keur Massar");
                professional.setCity("Dakar");
                professional.setCountry("Sénégal");
                professional.setAccountStatus(AccountStatus.ACTIVE);
                professional.setStatusChangeReason("Documents validés");
                professional.setStatusChangeDate(LocalDateTime.parse("2023-01-01T16:00:00"));
                professional.setIdentityDocumentPath("path/to/identity/document1");
                professional.setDiplomaPath("path/to/diploma1");
                professional.setLicensePath("path/to/license1");
                professional.setProfessionalInsurancePath("path/to/professional/insurance1");
                professional.setBankAccountNumberPath("path/to/bank/account/number1");

        when(professionalService.findById(1L)).thenReturn(professional);

        mockMvc.perform(delete("/api/professionals/1"))
                .andExpect(status().isNoContent());
                
    }

    @Test
    void shouldSuspendProfessional() throws Exception {
        Professional professional = new Professional();
        professional.setId(1L);
        professional.setFirstName("Saliou");
        professional.setLastName("Diop");
        professional.setSpeciality("Medecin Chirurgien");
        professional.setRegistrationNumber("123456789");
        professional.setEmail("saliou.diop@example.com");
        professional.setPhone("+221772345678");
        professional.setAddress("123 Keur Massar");
        professional.setCity("Dakar");
        professional.setCountry("Sénégal");
        professional.setAccountStatus(AccountStatus.ACTIVE);
        professional.setStatusChangeReason("Documents validés");
        professional.setStatusChangeDate(LocalDateTime.parse("2023-01-01T16:00:00"));
        professional.setIdentityDocumentPath("path/to/identity/document1");
        professional.setDiplomaPath("path/to/diploma1");
        professional.setLicensePath("path/to/license1");
        professional.setProfessionalInsurancePath("path/to/professional/insurance1");
        professional.setBankAccountNumberPath("path/to/bank/account/number1");

        when(professionalService.findById(1L)).thenReturn(professional);

        // Créer un professionnel avec le statut PENDING_VERIFICATION pour le résultat de suspendAccount
        Professional suspendedProfessional = new Professional();
        suspendedProfessional.setId(1L);
        suspendedProfessional.setFirstName("Saliou");
        suspendedProfessional.setLastName("Diop");
        suspendedProfessional.setSpeciality("Medecin Chirurgien");
        suspendedProfessional.setRegistrationNumber("123456789");
        suspendedProfessional.setEmail("saliou.diop@example.com");
        suspendedProfessional.setPhone("+221772345678");
        suspendedProfessional.setAddress("123 Keur Massar");
        suspendedProfessional.setCity("Dakar");
        suspendedProfessional.setCountry("Sénégal");
        suspendedProfessional.setAccountStatus(AccountStatus.SUSPENDED);
        suspendedProfessional.setStatusChangeReason("Compte suspendu temporairement.");
        suspendedProfessional.setStatusChangeDate(LocalDateTime.now());
        suspendedProfessional.setIdentityDocumentPath("path/to/identity/document1");
        suspendedProfessional.setDiplomaPath("path/to/diploma1");
        suspendedProfessional.setLicensePath("path/to/license1");
        suspendedProfessional.setProfessionalInsurancePath("path/to/professional/insurance1");
        suspendedProfessional.setBankAccountNumberPath("path/to/bank/account/number1");
        
        when(professionalService.suspendAccount(eq(1L), eq("Compte suspendu temporairement.")))
                .thenReturn(suspendedProfessional);
        
        mockMvc.perform(put("/api/professionals/1/suspend")
                .param("reason", "Compte suspendu temporairement."))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Saliou"))
                .andExpect(jsonPath("$.lastName").value("Diop"))
                .andExpect(jsonPath("$.speciality").value("Medecin Chirurgien"))
                .andExpect(jsonPath("$.registrationNumber").value("123456789"))
                .andExpect(jsonPath("$.email").value("saliou.diop@example.com"))
                .andExpect(jsonPath("$.phone").value("+221772345678"))
                .andExpect(jsonPath("$.address").value("123 Keur Massar"))
                .andExpect(jsonPath("$.city").value("Dakar"))
                .andExpect(jsonPath("$.country").value("Sénégal"))
                .andExpect(jsonPath("$.accountStatus").value("SUSPENDED"))
                .andExpect(jsonPath("$.statusChangeReason").value("Compte suspendu temporairement."))
                .andExpect(jsonPath("$.statusChangeDate").isNotEmpty());
    }
    
    @Test
    void shouldActivateProfessional() throws Exception {
        Professional professional = new Professional();
        professional.setId(1L);
        professional.setFirstName("Saliou");
        professional.setLastName("Diop");
        professional.setSpeciality("Medecin Chirurgien");
        professional.setRegistrationNumber("123456789");
        professional.setEmail("saliou.diop@example.com");
        professional.setPhone("+221772345678");
        professional.setAddress("123 Keur Massar");
        professional.setCity("Dakar");
        professional.setCountry("Sénégal");
        professional.setIdentityDocumentPath("path/to/identity/document1");
        professional.setDiplomaPath("path/to/diploma1");
        professional.setLicensePath("path/to/license1");
        professional.setProfessionalInsurancePath("path/to/professional/insurance1");
        professional.setBankAccountNumberPath("path/to/bank/account/number1");
        professional.setAccountStatus(AccountStatus.SUSPENDED);
        professional.setStatusChangeReason("Compte suspendu temporairement.");
        
        Professional activatedProfessional = new Professional();
        activatedProfessional.setId(1L);
        activatedProfessional.setFirstName("Saliou");
        activatedProfessional.setLastName("Diop");
        activatedProfessional.setSpeciality("Medecin Chirurgien");
        activatedProfessional.setRegistrationNumber("123456789");
        activatedProfessional.setEmail("saliou.diop@example.com");
        activatedProfessional.setPhone("+221772345678");
        activatedProfessional.setAddress("123 Keur Massar");
        activatedProfessional.setCity("Dakar");
        activatedProfessional.setCountry("Sénégal");
        activatedProfessional.setIdentityDocumentPath("path/to/identity/document1");
        activatedProfessional.setDiplomaPath("path/to/diploma1");
        activatedProfessional.setLicensePath("path/to/license1");
        activatedProfessional.setProfessionalInsurancePath("path/to/professional/insurance1");
        activatedProfessional.setBankAccountNumberPath("path/to/bank/account/number1");
        activatedProfessional.setAccountStatus(AccountStatus.ACTIVE);
        activatedProfessional.setStatusChangeReason("Compte réactif");
        activatedProfessional.setStatusChangeDate(LocalDateTime.now());
        
        when(professionalService.findById(1L)).thenReturn(professional);
        when(professionalService.activateAccount(1L)).thenReturn(activatedProfessional);
        
        mockMvc.perform(put("/api/professionals/1/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Saliou"))
                .andExpect(jsonPath("$.lastName").value("Diop"))
                .andExpect(jsonPath("$.speciality").value("Medecin Chirurgien"))
                .andExpect(jsonPath("$.registrationNumber").value("123456789"))
                .andExpect(jsonPath("$.email").value("saliou.diop@example.com"))
                .andExpect(jsonPath("$.phone").value("+221772345678"))
                .andExpect(jsonPath("$.address").value("123 Keur Massar"))
                .andExpect(jsonPath("$.city").value("Dakar"))
                .andExpect(jsonPath("$.country").value("Sénégal"))
                .andExpect(jsonPath("$.identityDocumentPath").value("path/to/identity/document1"))
                .andExpect(jsonPath("$.diplomaPath").value("path/to/diploma1"))
                .andExpect(jsonPath("$.licensePath").value("path/to/license1"))
                .andExpect(jsonPath("$.professionalInsurancePath").value("path/to/professional/insurance1"))
                .andExpect(jsonPath("$.bankAccountNumberPath").value("path/to/bank/account/number1"))
                .andExpect(jsonPath("$.accountStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.statusChangeReason").value("Compte réactif"))
                .andExpect(jsonPath("$.statusChangeDate").isNotEmpty());
    }

    @Test
    void shouldHandleExceptions() throws Exception {
        RuntimeException exception = new RuntimeException("Une erreur est survenue");
        when(professionalService.findById(1L)).thenThrow(exception);
        
        mockMvc.perform(get("/api/professionals/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Une erreur est survenue")));
    }
}