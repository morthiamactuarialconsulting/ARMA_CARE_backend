package com.armacare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;

// Facture
@Entity
@Table(name = "invoices")
public class Invoice {
    
    // Enum pour les statuts possibles
    public enum InvoiceStatus {
        EN_ATTENTE, 
        PAYEE, 
        REJETEE, 
        REMBOURSEE, 
        PARTIELLEMENT_REMBOURSEE
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La date de facture est obligatoire")
    private LocalDate invoiceDate = LocalDate.now();
    
    @NotNull(message = "Le montant total est obligatoire")
    @Min(value = 0, message = "Le montant total doit être positif")
    private Double totalAmount; // Montant total de la facture
    
    @NotNull(message = "Le montant remboursable est obligatoire")
    @Min(value = 0, message = "Le montant remboursable doit être positif")
    private Double reimbursableAmount; // Montant à rembourser
    
    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status = InvoiceStatus.EN_ATTENTE;
    
    @ManyToOne
    @NotNull(message = "Le professionnel de santé est obligatoire")
    private Professional professional;
    
    @ManyToOne
    @NotNull(message = "Le patient est obligatoire")
    private Patient patient;
    
    @ManyToOne
    @NotNull(message = "Le contrat d'assurance est obligatoire")
    private InsuranceContract contract;
    
    // Chemin vers le document de facture
    private String invoiceDocumentPath;

    public Invoice() {
    }

    public Invoice(LocalDate invoiceDate,
                  Double totalAmount, 
                  Double reimbursableAmount, 
                  InvoiceStatus status, 
                  Professional professional, 
                  Patient patient, 
                  InsuranceContract contract) {
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.reimbursableAmount = reimbursableAmount;
        this.status = status;
        this.professional = professional;
        this.patient = patient;
        this.contract = contract;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }
    
    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getReimbursableAmount() {
        return reimbursableAmount;
    }

    public void setReimbursableAmount(Double reimbursableAmount) {
        this.reimbursableAmount = reimbursableAmount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public Professional getProfessional() {
        return professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public InsuranceContract getContract() {
        return contract;
    }

    public void setContract(InsuranceContract contract) {
        this.contract = contract;
    }
    
    public String getInvoiceDocumentPath() {
        return invoiceDocumentPath;
    }
    
    public void setInvoiceDocumentPath(String invoiceDocumentPath) {
        this.invoiceDocumentPath = invoiceDocumentPath;
    }
    
    // Méthode utilitaire pour calculer le montant restant à la charge du patient
    public Double getPatientShare() {
        return totalAmount - reimbursableAmount;
    }

    @Override
    public String toString() {
        return "Invoice [id=" + id 
                + ", date=" + invoiceDate
                + ", montant total=" + totalAmount 
                + ", montant remboursable=" + reimbursableAmount
                + ", part patient=" + getPatientShare()
                + ", statut=" + status 
                + ", professionnel=" + professional 
                + ", patient=" + patient 
                + ", contrat=" + contract
                + "]";
    }
}
