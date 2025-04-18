package com.armacare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

// Professionnel de santé
@Entity
@Table(name = "professionals")
public class Professional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le prénom du professionnel est obligatoire")
    private String firstName;
    
    @NotBlank(message = "Le nom du professionnel est obligatoire")
    private String lastName;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^(\\+221|00221)?[7-9][0-9]{8}$", 
            message = "Format de téléphone sénégalais invalide. Exemple: +221770001122 ou 770001122")
    private String phone;

    @Email(message = "Format d'email invalide")
    private String email;
    
    @NotBlank(message = "La spécialité est obligatoire")
    private String speciality;

    @NotBlank(message = "Le numéro d'enregistrement est obligatoire")
    @Column(unique = true)
    private String registrationNumber; // Numéro de registre unique
    
    // Informations d'adresse
    private String address; // Rue et numéro
    private String city; // Ville
    private String country = "Sénégal"; // Pays (valeur par défaut)
    
    // Documents d'inscription
    private String identityDocumentPath; // Pièce d'identité (CNI, passeport)
    private String diplomaPath; // Diplôme médical
    private String licensePath; // Licence d'exercice
    private String professionalInsurancePath; // Assurance responsabilité professionnelle
    private String bankAccountNumberPath; // RIB 
    
    // Statut du compte
    public enum AccountStatus {
        PENDING_VERIFICATION, // En attente de vérification des documents
        ACTIVE,               // Compte actif et utilisable
        SUSPENDED,            // Compte temporairement suspendu
        INACTIVE              // Compte inactif (ex: retraite, décès)
    }
    
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    private LocalDateTime statusChangeDate; // Date du dernier changement de statut
    private String statusChangeReason; // Raison du changement de statut
    
    public Professional() {
    }
    
    // Constructeur complet
    public Professional(String firstName, String lastName, String speciality, 
    String registrationNumber,String phone, String email, 
            String address, String city, String country,
            String accountStatus, String statusChangeReason, String statusChangeDate,
            String identityDocumentPath, String licensePath, String professionalInsurancePath, 
            String diplomaPath, String bankAccountNumberPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.speciality = speciality;
        this.registrationNumber = registrationNumber;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.city = city;
        if (country != null && !country.isEmpty()) {
            this.country = country;
        }
        this.accountStatus = AccountStatus.valueOf(accountStatus);
        this.statusChangeReason = statusChangeReason;
        this.statusChangeDate = LocalDateTime.parse(statusChangeDate);
        this.identityDocumentPath = identityDocumentPath;
        this.licensePath = licensePath;
        this.professionalInsurancePath = professionalInsurancePath;
        this.diplomaPath = diplomaPath;
        this.bankAccountNumberPath = bankAccountNumberPath;
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
    
    // Méthode utilitaire pour obtenir le nom complet
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getBankAccountNumberPath() {
        return bankAccountNumberPath;
    }

    public void setBankAccountNumberPath(String bankAccountNumberPath) {
        this.bankAccountNumberPath = bankAccountNumberPath;
    }
    
    public boolean isActive() {
        return accountStatus == AccountStatus.ACTIVE;
    }

    public String getIdentityDocumentPath() {
        return identityDocumentPath;
    }

    public void setIdentityDocumentPath(String identityDocumentPath) {
        this.identityDocumentPath = identityDocumentPath;
    }

    public String getDiplomaPath() {
        return diplomaPath;
    }

    public void setDiplomaPath(String diplomaPath) {
        this.diplomaPath = diplomaPath;
    }

    public String getLicensePath() {
        return licensePath;
    }

    public void setLicensePath(String licensePath) {
        this.licensePath = licensePath;
    }

    public String getProfessionalInsurancePath() {
        return professionalInsurancePath;
    }

    public void setProfessionalInsurancePath(String professionalInsurancePath) {
        this.professionalInsurancePath = professionalInsurancePath;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
        this.statusChangeDate = LocalDateTime.now();
    }
    
    public void setAccountStatus(AccountStatus accountStatus, String reason) {
        this.accountStatus = accountStatus;
        this.statusChangeReason = reason;
        this.statusChangeDate = LocalDateTime.now();
    }

    public String getStatusChangeReason() {
        return statusChangeReason;
    }

    public void setStatusChangeReason(String statusChangeReason) {
        this.statusChangeReason = statusChangeReason;
    }

    public LocalDateTime getStatusChangeDate() {
        return statusChangeDate;
    }

    public void setStatusChangeDate(LocalDateTime statusChangeDate) {
        this.statusChangeDate = statusChangeDate;
    }
    
    // Méthode utilitaire pour vérifier si le compte est utilisable
    public boolean isAccountUsable() {
        return accountStatus == AccountStatus.ACTIVE;
    }

    @Override
    public String toString() {
        return "Professional [id=" + id 
                + ", prénom=" + firstName 
                + ", nom=" + lastName 
                + ", spécialité=" + speciality 
                + ", téléphone=" + phone
                + ", email=" + email 
                + ", adresse=" + address 
                + ", ville=" + city
                + ", pays=" + country
                + ", numéro de registre=" + registrationNumber
                + ", numéro de compte bancaire=" + bankAccountNumberPath 
                + ", statut du compte=" + accountStatus
                + ", raison du statut du compte=" + statusChangeReason
                + ", date du statut du compte=" + statusChangeDate
                + ", identité du professionnel=" + identityDocumentPath
                + ", diplôme du professionnel=" + diplomaPath
                + ", licence du professionnel=" + licensePath
                + ", assurance professionnelle du professionnel=" + professionalInsurancePath
                + ", numéro de compte bancaire=" + bankAccountNumberPath + "]";
    }
}
