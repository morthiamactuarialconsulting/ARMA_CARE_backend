package com.armacare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

// Couverture spécifique (dentaire, optique, hospitalisation, etc.)
@Entity
@Table(name = "coverages")
public class Coverage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le type de couverture est obligatoire")
    private String coverageType; // Ex: "dentaire", "optique", "hospitalisation"
    
    @NotNull(message = "Le taux de couverture est obligatoire")
    @Min(value = 1, message = "Le taux de couverture doit être au moins de 1%")
    @Max(value = 100, message = "Le taux de couverture ne peut pas dépasser 100%")
    @Column(name = "coverage_rate")
    private Integer coverageRate; // Taux en pourcentage (ex: 80 pour 80%)
    
    @Column(name = "coverage_ceiling")
    private Double coverageCeiling; // Plafond (montant maximal remboursable)
    
    // Relation avec le contrat parent
    @ManyToOne
    @NotNull(message = "Le contrat est obligatoire")
    private InsuranceContract contract;
    
    // Constructeurs
    public Coverage() {
    }
    
    public Coverage(String coverageType, Integer coverageRate, Double coverageCeiling) {
        this.coverageType = coverageType;
        this.coverageRate = coverageRate;
        this.coverageCeiling = coverageCeiling;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCoverageType() {
        return coverageType;
    }
    
    public void setCoverageType(String coverageType) {
        this.coverageType = coverageType;
    }
    
    public Integer getCoverageRate() {
        return coverageRate;
    }
    
    public void setCoverageRate(Integer coverageRate) {
        this.coverageRate = coverageRate;
    }
    
    public Double getCoverageCeiling() {
        return coverageCeiling;
    }
    
    public void setCoverageCeiling(Double coverageCeiling) {
        this.coverageCeiling = coverageCeiling;
    }
    
    public InsuranceContract getContract() {
        return contract;
    }
    
    public void setContract(InsuranceContract contract) {
        this.contract = contract;
    }
    
    @Override
    public String toString() {
        return "Coverage [id=" + id 
                + ", type=" + coverageType 
                + ", taux=" + coverageRate + "%" 
                + ", plafond=" + coverageCeiling 
                + "]";
    }
}