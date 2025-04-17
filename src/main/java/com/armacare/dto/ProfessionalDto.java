package com.armacare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

import com.armacare.model.Professional.AccountStatus;

public class ProfessionalDto {

    private Long id;

    @NotBlank(message = "Le prénom du professionnel est obligatoire")
    private String firstName;

    @NotBlank(message = "Le nom du professionnel est obligatoire")
    private String lastName;

    @NotBlank(message = "La spécialité est obligatoire")
    private String speciality;

    @NotBlank(message = "Le numéro d'enregistrement est obligatoire")
    private String registrationNumber;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^(\\+221|00221)?[7-9][0-9]{8}$", message = "Format de téléphone sénégalais invalide. Exemple: +221770001122 ou 770001122")
    private String phone;

    @Email(message = "Format d'email invalide")
    private String email;

    private String address;
    private String city;
    private String country = "Sénégal";
    private String identityDocumentPath;
    private String licensePath;
    private String professionalInsurancePath;
    private String diplomaPath;
    private String bankAccountNumberPath;
    private AccountStatus accountStatus;
    private String statusChangeReason;
    private LocalDateTime statusChangeDate;

    public ProfessionalDto() {
    }

    public ProfessionalDto(String firstName, String lastName, String speciality,
            String registrationNumber, String phone, String email,
            String address, String city, String country,
            String identityDocumentPath, String licensePath,
            String professionalInsurancePath, String diplomaPath, String bankAccountNumberPath) {
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
        this.identityDocumentPath = identityDocumentPath;
        this.diplomaPath = diplomaPath;
        this.licensePath = licensePath;
        this.professionalInsurancePath = professionalInsurancePath;
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
        if (country != null && !country.isEmpty()) {
            this.country = country;
        }
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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

    public String getBankAccountNumberPath() {
        return bankAccountNumberPath;
    }

    public void setBankAccountNumberPath(String bankAccountNumberPath) {
        this.bankAccountNumberPath = bankAccountNumberPath;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
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

    @Override
    public String toString() {
        return "ProfessionalDto [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName
                + ", speciality=" + speciality + ", phone=" + phone + ", email=" + email
                + ", address=" + address + ", city=" + city + ", country=" + country
                + ", registrationNumber=" + registrationNumber + ", identityDocumentPath="
                + identityDocumentPath + ", diplomaPath=" + diplomaPath + ", licensePath=" + licensePath
                + ", professionalInsurancePath=" + professionalInsurancePath + ", bankAccountNumberPath="
                + bankAccountNumberPath + ", accountStatus=" + accountStatus + ", statusChangeReason="
                + statusChangeReason + ", statusChangeDate=" + statusChangeDate + "]";
    }
}
