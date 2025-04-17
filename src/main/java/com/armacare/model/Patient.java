package com.armacare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Patient (Assuré)
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le prénom du patient est obligatoire")
    private String firstName;
    
    @NotBlank(message = "Le nom du patient est obligatoire")
    private String lastName;
    
    // Informations personnelles
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateOfBirth;
    
    @Pattern(regexp = "^[MF]$", message = "Le sexe doit être 'M' pour masculin ou 'F' pour féminin")
    private String gender;
    
    @Column(unique = true)
    @NotBlank(message = "Le numéro d'identification national est obligatoire")
    private String nationalId; // Numéro d'identification national (CNI)
    
    // Informations de contact
    private String address;
    private String city;
    private String postalCode;
    private String country = "Sénégal";
    
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^(\\+221|00221)?[7-9][0-9]{8}$", 
            message = "Format de téléphone sénégalais invalide. Exemple: +221770001122 ou 770001122")
    private String phone;
    
    @Email(message = "Format d'email invalide")
    private String email;
    
    // Relation avec les contrats d'assurance
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<InsuranceContract> insuranceContracts = new ArrayList<>();
    
    // Informations médicales
    private String bloodGroup; // Groupe sanguin
    
    @Size(max = 500, message = "Les allergies ne peuvent pas dépasser 500 caractères")
    private String allergies;
    
    @Size(max = 500, message = "Les conditions médicales ne peuvent pas dépasser 500 caractères")
    private String medicalConditions;
    
    // Statut du patient
    private boolean active = true;

    public Patient() {
    }

    public Patient(String firstName, String lastName, String address, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }
    
    // Constructeur plus complet
    public Patient(String firstName, String lastName, LocalDate dateOfBirth, String gender, String nationalId, 
                  String address, String city, String postalCode, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.nationalId = nationalId;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getNationalId() {
        return nationalId;
    }
    
    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<InsuranceContract> getInsuranceContracts() {
        return insuranceContracts;
    }

    public void setInsuranceContracts(List<InsuranceContract> insuranceContracts) {
        this.insuranceContracts = insuranceContracts;
    }
    
    // Méthode utilitaire pour obtenir l'assurance actuelle
    public Insurance getCurrentInsurance() {
        if (insuranceContracts == null || insuranceContracts.isEmpty()) {
            return null;
        }
        
        // Trouver le contrat actif le plus récent
        return insuranceContracts.stream()
            .filter(contract -> contract.isActive())
            .findFirst()
            .map(contract -> contract.getInsurance())
            .orElse(null);
    }
    
    public String getBloodGroup() {
        return bloodGroup;
    }
    
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
    
    public String getAllergies() {
        return allergies;
    }
    
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
    
    public String getMedicalConditions() {
        return medicalConditions;
    }
    
    public void setMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Patient [id=" + id 
        + ", prénom=" + firstName 
        + ", nom=" + lastName 
        + ", date de naissance=" + dateOfBirth
        + ", sexe=" + gender
        + ", numéro d'identification=" + nationalId
        + ", adresse=" + address 
        + ", ville=" + city
        + ", code postal=" + postalCode
        + ", pays=" + country
        + ", téléphone=" + phone 
        + ", email=" + email 
        + ", nombre de contrats=" + (insuranceContracts != null ? insuranceContracts.size() : 0)
        + ", groupe sanguin=" + bloodGroup
        + ", allergies=" + allergies
        + ", conditions médicales=" + medicalConditions
        + ", actif=" + active + "]";
    }
}
