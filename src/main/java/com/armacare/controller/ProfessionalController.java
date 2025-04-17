package com.armacare.controller;

import java.util.List;

import com.armacare.model.Professional;
import com.armacare.model.Professional.AccountStatus;
import com.armacare.service.ProfessionalService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.armacare.dto.ProfessionalDto;
import com.armacare.exception.ProfessionalNotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/professionals")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    // Récupérer tous les professionnels
    @GetMapping
    public List<Professional> getAllProfessionals() {
        return professionalService.findAll();
    }
    
    // Récupérer un professionnel par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Professional> getProfessionalById(@PathVariable Long id) {
        try {
            Professional professional = professionalService.findById(id);
            return ResponseEntity.ok(professional);
        } catch (ProfessionalNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Créer un professionnel
    @PostMapping
    public ResponseEntity<Professional> createProfessional(@Valid @RequestBody ProfessionalDto professionalDto) {
        Professional createdProfessional = professionalService.createProfessional(professionalDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfessional);
    }
    
    // Mettre à jour un professionnel
    @PutMapping("/{id}")
    public ResponseEntity<Professional> updateProfessional(
            @PathVariable Long id, 
            @Valid @RequestBody ProfessionalDto professionalDto) {
        try {
            Professional updatedProfessional = professionalService.updateProfessional(id, professionalDto);
            return ResponseEntity.ok(updatedProfessional);
        } catch (ProfessionalNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Supprimer un professionnel (désactivation)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessional(@PathVariable Long id) {
        try {
            professionalService.deleteProfessional(id);
            return ResponseEntity.noContent().build();
        } catch (ProfessionalNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Rechercher des professionnels par spécialité
    @GetMapping("/by-speciality")
    public List<Professional> getProfessionalsBySpeciality(@RequestParam String speciality) {
        return professionalService.findBySpeciality(speciality);
    }
    
    // Rechercher des professionnels par ville
    @GetMapping("/by-city")
    public List<Professional> getProfessionalsByCity(@RequestParam String city) {
        return professionalService.findByCity(city);
    }
    
    // Rechercher des professionnels par statut de compte
    @GetMapping("/by-status")
    public List<Professional> getProfessionalsByStatus(@RequestParam AccountStatus status) {
        return professionalService.findByAccountStatus(status);
    }
    
    // Activer le compte d'un professionnel
    @PutMapping("/{id}/activate")
    public ResponseEntity<Professional> activateAccount(@PathVariable Long id) {
        try {
            Professional professional = professionalService.activateAccount(id);
            return ResponseEntity.ok(professional);
        } catch (ProfessionalNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Suspendre le compte d'un professionnel
    @PutMapping("/{id}/suspend")
    public ResponseEntity<Professional> suspendAccount(
            @PathVariable Long id, 
            @RequestParam String reason) {
        try {
            Professional professional = professionalService.suspendAccount(id, reason);
            return ResponseEntity.ok(professional);
        } catch (ProfessionalNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Gestionnaire d'exceptions global pour ce contrôleur
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExceptions(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur est survenue: " + e.getMessage());
    }
}
