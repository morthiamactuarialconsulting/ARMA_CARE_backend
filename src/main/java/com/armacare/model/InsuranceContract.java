package com.armacare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

// Contrat d'assurance
@Entity
@Table(name = "insurance_contracts")
public class InsuranceContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le numéro de contrat est obligatoire")
    @Column(unique = true)
    private String contractNumber;

    // Type de contrat (Basique, Standard, Premium, etc.)
    @NotBlank(message = "Le type de contrat est obligatoire")
    private String contractType;

    // Dates de couverture
    @NotNull(message = "La date de début de couverture est obligatoire")
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    @FutureOrPresent(message = "La date de fin de couverture doit être dans le présent ou le futur")
    private Date endDate;
    
    // Franchise globale du contrat
    private Double deductible;

    // Statut du contrat
    private boolean active = true;

    // Relations
    @ManyToOne
    @NotNull(message = "Le patient est obligatoire")
    private Patient patient;

    @ManyToOne
    @NotNull(message = "L'assureur est obligatoire")
    private Insurance insurance;
    
    // Relation avec les couvertures spécifiques
    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coverage> coverages = new ArrayList<>();

    // Constructeurs
    public InsuranceContract() {
    }

    public InsuranceContract(String contractNumber, String contractType, Date startDate,
            Patient patient, Insurance insurance) {
        this.contractNumber = contractNumber;
        this.contractType = contractType;
        this.startDate = startDate;
        this.patient = patient;
        this.insurance = insurance;
    }

    public InsuranceContract(String contractNumber, String contractType, 
            Date startDate, Date endDate, Double deductible,
            Patient patient, Insurance insurance) {
        this.contractNumber = contractNumber;
        this.contractType = contractType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deductible = deductible;
        this.patient = patient;
        this.insurance = insurance;
    }
    
    // Méthodes pour gérer la relation avec Coverage
    public void addCoverage(Coverage coverage) {
        coverages.add(coverage);
        coverage.setContract(this);
    }
    
    public void removeCoverage(Coverage coverage) {
        coverages.remove(coverage);
        coverage.setContract(null);
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }
    
    public String getContractType() {
        return contractType;
    }
    
    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getDeductible() {
        return deductible;
    }

    public void setDeductible(Double deductible) {
        this.deductible = deductible;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }
    
    public List<Coverage> getCoverages() {
        return coverages;
    }
    
    public void setCoverages(List<Coverage> coverages) {
        this.coverages = coverages;
    }

    @Override
    public String toString() {
        return "InsuranceContract [id=" + id 
                + ", numéro de contrat=" + contractNumber
                + ", type de contrat=" + contractType
                + ", date de début=" + startDate 
                + ", date de fin=" + endDate 
                + ", franchise=" + deductible 
                + ", nombre de couvertures=" + (coverages != null ? coverages.size() : 0)
                + ", patient=" + (patient != null ? patient.getFirstName() + " " + patient.getLastName() : "aucun") 
                + ", assureur=" + (insurance != null ? insurance.getName() : "aucun") 
                + ", actif=" + active + "]";
    }
}