package com.armacare.exception;

public class ProfessionalNotFoundException extends RuntimeException {
    public ProfessionalNotFoundException(Long id) {
        super("Professionnel non trouvé avec l'ID: " + id);
    }
}
