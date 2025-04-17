package com.armacare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

// Assureur 
@Entity
@Table(name = "insurances")
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le nom de l'assureur est obligatoire")
    private String name;
    
    @NotBlank(message = "Le type d'assurance est obligatoire")
    private String type;
    
    // Informations de contact
    @Email(message = "Format d'email invalide")
    @Column(unique = true)
    private String email;
    
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^(\\+221|00221)?[7-9][0-9]{8}$", 
            message = "Format de téléphone sénégalais invalide. Exemple: +221770001122 ou 770001122")
    private String phoneNumber;
    
    private String address;
    private String city;
    private String postalCode;
    private String country = "Sénégal";
    
    // Informations d'authentification
    @Column(unique = true)
    private String username;
    
    private String password; // À encoder avant stockage
    
    // Informations professionnelles
    private String licenseNumber; // Numéro de licence
    
    // Informations du responsable
    private String contactPersonName; // Nom du responsable
    private String contactPersonPosition; // Poste du responsable
    private String contactPersonEmail; // Email direct du responsable
    private String contactPersonPhone; // Téléphone direct du responsable
    
    // Documents légaux et contractuels
    private String registrationNumber; // Numéro d'enregistrement légal
    private LocalDate registrationDate; // Date d'enregistrement officiel
    private String armaContractNumber; // Numéro du contrat avec ARMA-CARE
    private LocalDate armaContractStartDate; // Date de début du contrat avec ARMA-CARE
    private LocalDate armaContractEndDate; // Date de fin du contrat avec ARMA-CARE
    
    // Chemins vers les documents stockés
    private String registrationDocumentPath; // Chemin vers le document d'enregistrement
    private String licensePath; // Chemin vers la licence
    private String armaContractPath; // Chemin vers le contrat avec ARMA-CARE
    
    // Relations
    @OneToMany(mappedBy = "insurance")
    private List<InsuranceContract> contracts = new ArrayList<>();
    
    // Statut du compte
    private boolean active = true;
    
    // Constructeurs, getters et setters
    public Insurance() {
    }
    
    public Insurance(String name, String type, String email, String phoneNumber) {
        this.name = name;
        this.type = type;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonPosition() {
        return contactPersonPosition;
    }

    public void setContactPersonPosition(String contactPersonPosition) {
        this.contactPersonPosition = contactPersonPosition;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getArmaContractNumber() {
        return armaContractNumber;
    }

    public void setArmaContractNumber(String armaContractNumber) {
        this.armaContractNumber = armaContractNumber;
    }

    public LocalDate getArmaContractStartDate() {
        return armaContractStartDate;
    }

    public void setArmaContractStartDate(LocalDate armaContractStartDate) {
        this.armaContractStartDate = armaContractStartDate;
    }

    public LocalDate getArmaContractEndDate() {
        return armaContractEndDate;
    }

    public void setArmaContractEndDate(LocalDate armaContractEndDate) {
        this.armaContractEndDate = armaContractEndDate;
    }

    public String getRegistrationDocumentPath() {
        return registrationDocumentPath;
    }

    public void setRegistrationDocumentPath(String registrationDocumentPath) {
        this.registrationDocumentPath = registrationDocumentPath;
    }

    public String getLicensePath() {
        return licensePath;
    }

    public void setLicensePath(String licensePath) {
        this.licensePath = licensePath;
    }

    public String getArmaContractPath() {
        return armaContractPath;
    }

    public void setArmaContractPath(String armaContractPath) {
        this.armaContractPath = armaContractPath;
    }

    public List<InsuranceContract> getContracts() {
        return contracts;
    }

    public void setContracts(List<InsuranceContract> contracts) {
        this.contracts = contracts;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Insurance [id=" + id 
                + ", nom de l'assureur=" + name 
                + ", type=" + type 
                + ", email=" + email 
                + ", numéro de téléphone=" + phoneNumber
                + ", adresse=" + address 
                + ", ville=" + city 
                + ", code postal=" + postalCode 
                + ", pays=" + country
                + ", nom d'utilisateur=" + username 
                + ", numéro de licence=" + licenseNumber
                + ", nom du responsable=" + contactPersonName
                + ", poste du responsable=" + contactPersonPosition
                + ", email du responsable=" + contactPersonEmail
                + ", téléphone du responsable=" + contactPersonPhone
                + ", numéro d'enregistrement=" + registrationNumber
                + ", date d'enregistrement=" + registrationDate
                + ", numéro du contrat ARMA-CARE=" + armaContractNumber
                + ", date de début du contrat ARMA-CARE=" + armaContractStartDate
                + ", date de fin du contrat ARMA-CARE=" + armaContractEndDate
                + ", nombre de contrats=" + (contracts != null ? contracts.size() : 0)
                + ", compte actif=" + active + "]";
    }
}
