# Guide d'Implémentation du Dashboard Administrateur - ARMA-CARE

## Introduction

Ce document détaille l'architecture, les fonctionnalités et les bonnes pratiques pour l'implémentation du dashboard administrateur d'ARMA-CARE. Le rôle de l'administrateur est crucial dans le système car il est le seul autorisé à créer des profils d'assureurs et d'assurés, ainsi qu'à gérer l'ensemble du système.

## Architecture du Module Administrateur

### 1. Modèle de Données

#### Entité Administrator

```java
@Entity
@Table(name = "administrators")
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String username;
    private String password; // Haché
    
    @Enumerated(EnumType.STRING)
    private AdminRole role;
    
    private boolean active;
    private LocalDateTime lastLogin;
    private String sessionToken;
    
    // Enum pour les rôles d'administrateur
    public enum AdminRole {
        SUPER_ADMIN, // Accès complet au système
        INSURANCE_MANAGER, // Gestion des assureurs
        PATIENT_MANAGER, // Gestion des patients
        PROFESSIONAL_VALIDATOR, // Validation des professionnels
        BILLING_ADMIN // Gestion de la facturation
    }
    
    // Getters, setters, etc.
}
```

#### Entité AdminAction (Journal d'activité)

```java
@Entity
@Table(name = "admin_actions")
public class AdminAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Administrator administrator;
    
    private LocalDateTime actionDate;
    private String actionType; // CREATE, UPDATE, DELETE, LOGIN, etc.
    private String entityType; // INSURANCE, PATIENT, PROFESSIONAL, etc.
    private Long entityId;
    private String details; // Détails de l'action en JSON
    private String ipAddress;
    
    // Getters, setters, etc.
}
```

### 2. Services Métier

#### AdminService

```java
@Service
public class AdminService {
    // Authentification
    public Administrator authenticate(String username, String password);
    
    // Gestion des administrateurs
    public Administrator createAdmin(Administrator admin, String creatorUsername);
    public Administrator updateAdmin(Long adminId, Administrator adminData);
    public void deactivateAdmin(Long adminId);
    public List<Administrator> getAllAdmins();
    
    // Journal d'activité
    public void logAction(String username, String actionType, String entityType, Long entityId, String details);
    public List<AdminAction> getActionsByAdmin(Long adminId, LocalDate startDate, LocalDate endDate);
    
    // Gestion des assureurs
    public Insurance createInsurance(Insurance insurance, String adminUsername);
    public void approveInsurance(Long insuranceId, String adminUsername);
    
    // Gestion des patients
    public Patient createPatient(Patient patient, String adminUsername);
    
    // Validation des professionnels
    public void validateProfessionalDocuments(Long professionalId, boolean approved, String reason, String adminUsername);
    public void changeProfessionalStatus(Long professionalId, AccountStatus newStatus, String reason, String adminUsername);
}
```

#### DashboardService

```java
@Service
public class DashboardService {
    // Statistiques générales
    public DashboardStats getGeneralStats();
    
    // Statistiques des professionnels
    public ProfessionalStats getProfessionalStats();
    
    // Statistiques des patients
    public PatientStats getPatientStats();
    
    // Statistiques des assureurs
    public InsuranceStats getInsuranceStats();
    
    // Statistiques de facturation
    public BillingStats getBillingStats();
    
    // Alertes et notifications
    public List<AdminAlert> getPendingAlerts(String adminUsername);
}
```

### 3. API REST

#### AdminController

```java
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    // Authentification
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request);
    
    // Gestion des administrateurs
    @PostMapping("/administrators")
    public ResponseEntity<Administrator> createAdmin(@RequestBody AdminRequest request);
    
    @PutMapping("/administrators/{id}")
    public ResponseEntity<Administrator> updateAdmin(@PathVariable Long id, @RequestBody AdminRequest request);
    
    @DeleteMapping("/administrators/{id}")
    public ResponseEntity<Void> deactivateAdmin(@PathVariable Long id);
    
    @GetMapping("/administrators")
    public ResponseEntity<List<Administrator>> getAllAdmins();
    
    // Journal d'activité
    @GetMapping("/actions")
    public ResponseEntity<Page<AdminAction>> getActions(
            @RequestParam(required = false) Long adminId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable);
}
```

#### InsuranceAdminController

```java
@RestController
@RequestMapping("/api/admin/insurances")
public class InsuranceAdminController {
    @PostMapping
    public ResponseEntity<Insurance> createInsurance(@RequestBody InsuranceRequest request);
    
    @PutMapping("/{id}")
    public ResponseEntity<Insurance> updateInsurance(@PathVariable Long id, @RequestBody InsuranceRequest request);
    
    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveInsurance(@PathVariable Long id);
    
    @GetMapping
    public ResponseEntity<Page<Insurance>> getAllInsurances(Pageable pageable);
    
    @GetMapping("/{id}")
    public ResponseEntity<Insurance> getInsurance(@PathVariable Long id);
}
```

#### PatientAdminController

```java
@RestController
@RequestMapping("/api/admin/patients")
public class PatientAdminController {
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody PatientRequest request);
    
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody PatientRequest request);
    
    @GetMapping
    public ResponseEntity<Page<Patient>> getAllPatients(Pageable pageable);
    
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id);
}
```

#### ProfessionalAdminController

```java
@RestController
@RequestMapping("/api/admin/professionals")
public class ProfessionalAdminController {
    @GetMapping
    public ResponseEntity<Page<Professional>> getAllProfessionals(
            @RequestParam(required = false) AccountStatus status,
            Pageable pageable);
    
    @GetMapping("/{id}")
    public ResponseEntity<Professional> getProfessional(@PathVariable Long id);
    
    @PostMapping("/{id}/validate-documents")
    public ResponseEntity<Void> validateDocuments(
            @PathVariable Long id,
            @RequestBody DocumentValidationRequest request);
    
    @PostMapping("/{id}/change-status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Long id,
            @RequestBody StatusChangeRequest request);
}
```

#### DashboardController

```java
@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardController {
    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getGeneralStats();
    
    @GetMapping("/professionals/stats")
    public ResponseEntity<ProfessionalStats> getProfessionalStats();
    
    @GetMapping("/patients/stats")
    public ResponseEntity<PatientStats> getPatientStats();
    
    @GetMapping("/insurances/stats")
    public ResponseEntity<InsuranceStats> getInsuranceStats();
    
    @GetMapping("/billing/stats")
    public ResponseEntity<BillingStats> getBillingStats();
    
    @GetMapping("/alerts")
    public ResponseEntity<List<AdminAlert>> getAlerts();
}
```

## Fonctionnalités du Dashboard

### 1. Tableau de Bord Principal

- **Vue d'ensemble** : Statistiques globales du système
- **Activité récente** : Dernières actions administratives
- **Alertes** : Notifications importantes nécessitant une attention

### 2. Gestion des Professionnels

- **Liste des professionnels** : Filtrable par statut, spécialité, ville
- **Validation des documents** : Interface de vérification des pièces fournies
- **Gestion des statuts** : Activation, suspension ou désactivation des comptes
- **Historique** : Suivi des modifications de statut

### 3. Gestion des Assureurs

- **Création d'assureurs** : Formulaire complet avec validation
- **Gestion des contrats** : Supervision des contrats ARMA-CARE
- **Rapports** : Statistiques de remboursement par assureur

### 4. Gestion des Patients

- **Création de patients** : Enregistrement des nouveaux assurés
- **Gestion des contrats** : Attribution de contrats d'assurance
- **Historique médical** : Accès aux dossiers (avec restrictions)

### 5. Facturation et Finances

- **Suivi des paiements** : État des remboursements
- **Rapports financiers** : Chiffre d'affaires, commissions
- **Alertes de retard** : Identification des paiements en retard

### 6. Administration Système

- **Gestion des administrateurs** : Création et gestion des comptes admin
- **Journal d'audit** : Suivi complet des actions administratives
- **Paramètres système** : Configuration globale de la plateforme

## Sécurité et Contrôle d'Accès

### 1. Authentification Renforcée

- Authentification à deux facteurs (2FA)
- Politique de mots de passe forts
- Verrouillage après tentatives échouées

### 2. Contrôle d'Accès Basé sur les Rôles (RBAC)

| Rôle | Permissions |
|--------|-------------|
| SUPER_ADMIN | Accès complet à toutes les fonctionnalités |
| INSURANCE_MANAGER | Gestion des assureurs et de leurs contrats |
| PATIENT_MANAGER | Gestion des patients et de leurs dossiers |
| PROFESSIONAL_VALIDATOR | Validation des professionnels de santé |
| BILLING_ADMIN | Gestion de la facturation et des finances |

### 3. Journalisation et Audit

- Enregistrement de toutes les actions administratives
- Horodatage et identification de l'administrateur
- Conservation de l'adresse IP et des détails de session

## Bonnes Pratiques de Développement

### 1. Architecture Frontend

- Utilisation de React ou Angular pour le dashboard
- Composants réutilisables pour les tableaux, formulaires et graphiques
- State management centralisé (Redux ou Ngrx)

### 2. Sécurité

- Protection contre les attaques CSRF
- Validation des entrées côté client et serveur
- Chiffrement des données sensibles
- Expiration des sessions après inactivité

### 3. Performance

- Pagination pour les listes volumineuses
- Chargement paresseux (lazy loading) des modules
- Mise en cache des données statiques
- Optimisation des requêtes SQL

### 4. Expérience Utilisateur

- Interface intuitive et responsive
- Feedback immédiat pour les actions
- Notifications en temps réel
- Thème cohérent avec l'identité visuelle d'ARMA-CARE

## Meilleures Pratiques d'Implémentation par Module

### 1. Gestion des Professionnels

#### Architecture et Conception

- **Pattern State** : Implémenter le pattern State pour gérer les transitions entre les différents statuts des professionnels (PENDING_VERIFICATION → PENDING_ACTIVATION → ACTIVE).

```java
public interface ProfessionalState {
    void verifyDocuments(Professional professional, Administrator admin, boolean approved);
    void activateAccount(Professional professional, Administrator admin);
    void suspendAccount(Professional professional, Administrator admin, String reason);
    void deactivateAccount(Professional professional, Administrator admin, String reason);
}

public class PendingVerificationState implements ProfessionalState {
    @Override
    public void verifyDocuments(Professional professional, Administrator admin, boolean approved) {
        if (approved) {
            professional.setAccountStatus(AccountStatus.PENDING_ACTIVATION);
            professional.setStatusChangeDate(LocalDateTime.now());
            professional.setStatusChangeReason("Documents vérifiés et approuvés par " + admin.getUsername());
            // Journaliser l'action
        } else {
            // Gérer le rejet des documents
        }
    }
    
    // Autres méthodes implémentées...
}
```

- **Validation des Documents** : Utiliser un service dédié pour la validation des documents avec des vérifications de sécurité (antivirus, type MIME, taille).

```java
@Service
public class DocumentValidationService {
    public ValidationResult validateDocument(MultipartFile document, String expectedType) {
        // Vérifications de sécurité
        // Analyse antivirus
        // Vérification du type MIME
        // Validation de la taille
        return new ValidationResult(true, "Document valide");
    }
}
```

#### Interface Utilisateur

- **Tableau de Bord des Professionnels** : Implémenter un tableau de bord avec filtres avancés (statut, spécialité, localisation) et actions contextuelles selon le statut.

- **Visualiseur de Documents** : Intégrer un visualiseur de documents sécurisé pour examiner les pièces fournies sans téléchargement.

- **Formulaire de Validation** : Créer un formulaire de validation avec liste de contrôle (checklist) pour standardiser le processus de vérification.

#### Workflow et Notifications

- **Workflow Automatisé** : Implémenter un workflow qui guide l'administrateur à travers les étapes de validation.

- **Notifications Automatiques** : Envoyer des notifications au professionnel à chaque changement de statut.

```java
@Service
public class ProfessionalNotificationService {
    public void notifyStatusChange(Professional professional, AccountStatus oldStatus, AccountStatus newStatus) {
        String subject = "Mise à jour du statut de votre compte ARMA-CARE";
        String content = generateStatusChangeEmailContent(professional, oldStatus, newStatus);
        emailService.sendEmail(professional.getEmail(), subject, content);
    }
}
```

### 2. Gestion des Assureurs

#### Architecture et Conception

- **Pattern Builder** : Utiliser le pattern Builder pour la création complexe des entités d'assurance avec de nombreux attributs.

```java
public class InsuranceBuilder {
    private Insurance insurance = new Insurance();
    
    public InsuranceBuilder withName(String name) {
        insurance.setName(name);
        return this;
    }
    
    public InsuranceBuilder withType(String type) {
        insurance.setType(type);
        return this;
    }
    
    // Autres méthodes de construction...
    
    public Insurance build() {
        // Validation finale
        return insurance;
    }
}
```

- **Pattern Composite** : Implémenter le pattern Composite pour gérer les hiérarchies d'assureurs (groupes d'assurance avec filiales).

#### Validation et Contrôles

- **Validation des Données Légales** : Mettre en place des validations spécifiques pour les numéros d'enregistrement et licences.

- **Vérification des Doublons** : Implémenter un système de détection des doublons basé sur plusieurs critères (nom, numéro d'enregistrement, contact).

```java
@Service
public class InsuranceDuplicationService {
    public List<Insurance> findPotentialDuplicates(Insurance newInsurance) {
        // Recherche par nom similaire (algorithme de similarité de texte)
        // Recherche par numéro d'enregistrement
        // Recherche par coordonnées de contact
        return potentialDuplicates;
    }
}
```

#### Gestion des Contrats

- **Versionnement des Contrats** : Implémenter un système de versionnement pour suivre les modifications des contrats ARMA-CARE.

- **Génération Automatique de Documents** : Créer un système de génération automatique des contrats et avenants basé sur des modèles.

```java
@Service
public class ContractGenerationService {
    public byte[] generateContract(Insurance insurance, ContractTemplate template) {
        // Charger le modèle
        // Remplacer les variables
        // Générer le PDF
        return contractPdf;
    }
}
```

### 3. Gestion des Patients

#### Architecture et Conception

- **Pattern Decorator** : Utiliser le pattern Decorator pour ajouter des fonctionnalités spécifiques aux patients selon leur type de couverture.

```java
public interface PatientService {
    void registerPatient(Patient patient);
    void assignInsurance(Patient patient, InsuranceContract contract);
    // Autres méthodes...
}

public class BasicPatientService implements PatientService {
    // Implémentation de base
}

public class PremiumPatientDecorator extends PatientServiceDecorator {
    public PremiumPatientDecorator(PatientService patientService) {
        super(patientService);
    }
    
    @Override
    public void registerPatient(Patient patient) {
        super.registerPatient(patient);
        // Fonctionnalités supplémentaires pour patients premium
    }
}
```

- **Gestion de la Confidentialité** : Implémenter un système de contrôle d'accès granulaire aux données médicales sensibles.

#### Validation et Contrôles

- **Vérification d'Identité** : Mettre en place un processus de vérification d'identité robuste pour éviter les usurpations.

- **Validation des Données Médicales** : Implémenter des validations spécifiques pour les données médicales (groupe sanguin, allergies).

```java
@Service
public class MedicalDataValidationService {
    public ValidationResult validateBloodGroup(String bloodGroup) {
        // Vérifier le format du groupe sanguin
        return isValidBloodGroup(bloodGroup) ? 
            new ValidationResult(true, "Groupe sanguin valide") : 
            new ValidationResult(false, "Format de groupe sanguin invalide");
    }
}
```

#### Gestion des Contrats d'Assurance

- **Moteur de Règles** : Implémenter un moteur de règles pour déterminer l'éligibilité des patients à différents types de contrats.

- **Historique des Contrats** : Maintenir un historique complet des contrats d'assurance du patient avec leurs périodes de validité.

```java
@Service
public class PatientContractHistoryService {
    public List<InsuranceContract> getContractHistory(Patient patient) {
        // Récupérer tous les contrats, y compris les expirés
        return contractRepository.findAllByPatientOrderByStartDateDesc(patient);
    }
}
```

### 4. Facturation et Finances

#### Architecture et Conception

- **Pattern Strategy** : Utiliser le pattern Strategy pour les différents algorithmes de calcul de remboursement selon le type de contrat.

```java
public interface ReimbursementStrategy {
    double calculateReimbursement(Invoice invoice, InsuranceContract contract);
}

public class BasicReimbursementStrategy implements ReimbursementStrategy {
    @Override
    public double calculateReimbursement(Invoice invoice, InsuranceContract contract) {
        // Logique de calcul pour contrat basique
    }
}

public class PremiumReimbursementStrategy implements ReimbursementStrategy {
    @Override
    public double calculateReimbursement(Invoice invoice, InsuranceContract contract) {
        // Logique de calcul pour contrat premium
    }
}
```

- **Pattern Chain of Responsibility** : Implémenter le pattern Chain of Responsibility pour le processus d'approbation des remboursements.

```java
public abstract class ReimbursementApprovalHandler {
    protected ReimbursementApprovalHandler nextHandler;
    
    public void setNextHandler(ReimbursementApprovalHandler handler) {
        this.nextHandler = handler;
    }
    
    public abstract ApprovalResult handleApproval(Claim claim);
}

public class AutomaticApprovalHandler extends ReimbursementApprovalHandler {
    @Override
    public ApprovalResult handleApproval(Claim claim) {
        if (claim.getAmount() < 10000) {
            // Approbation automatique pour petits montants
            return new ApprovalResult(true, "Approuvé automatiquement");
        } else if (nextHandler != null) {
            return nextHandler.handleApproval(claim);
        }
        return new ApprovalResult(false, "Montant trop élevé pour approbation automatique");
    }
}
```

#### Suivi et Rapports

- **Tableaux de Bord Financiers** : Implémenter des tableaux de bord financiers avec graphiques et indicateurs clés de performance (KPI).

- **Rapports Automatisés** : Mettre en place un système de génération automatique de rapports financiers périodiques.

```java
@Service
public class FinancialReportService {
    public byte[] generateMonthlyReport(YearMonth period) {
        // Collecter les données pour la période
        // Générer le rapport au format PDF
        return reportPdf;
    }
    
    @Scheduled(cron = "0 0 1 1 * ?") // Premier jour de chaque mois
    public void generateAndSendMonthlyReports() {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        byte[] report = generateMonthlyReport(lastMonth);
        // Envoyer aux administrateurs concernés
    }
}
```

#### Gestion des Paiements

- **Intégration de Passerelles de Paiement** : Implémenter des intégrations sécurisées avec les passerelles de paiement locales (Wave, Orange Money, etc.).

- **Réconciliation Automatique** : Développer un système de réconciliation automatique des paiements reçus.

```java
@Service
public class PaymentReconciliationService {
    public ReconciliationResult reconcilePayments(LocalDate date) {
        // Récupérer les paiements attendus
        // Récupérer les paiements reçus
        // Identifier les écarts
        return new ReconciliationResult(matched, unmatched, excess);
    }
}
```

#### Sécurité Financière

- **Double Validation** : Implémenter un système de double validation pour les opérations financières importantes.

- **Piste d'Audit Financière** : Maintenir une piste d'audit détaillée pour toutes les transactions financières.

```java
@Entity
@Table(name = "financial_audit_logs")
public class FinancialAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime timestamp;
    private String action; // PAYMENT_RECEIVED, REIMBURSEMENT_APPROVED, etc.
    private String entityType; // INVOICE, CLAIM, etc.
    private Long entityId;
    private Double amount;
    private String currency;
    private String performedBy; // Username de l'administrateur
    private String ipAddress;
    private String details;
    
    // Getters, setters, etc.
}
```

## Plan d'Implémentation

### Phase 1 : Fondation

1. Création du modèle de données administrateur
2. Implémentation de l'authentification et du RBAC
3. Développement du tableau de bord principal

### Phase 2 : Gestion des Entités

1. Module de gestion des professionnels
2. Module de gestion des assureurs
3. Module de gestion des patients

### Phase 3 : Fonctionnalités Avancées

1. Système de rapports et statistiques
2. Module de facturation et finances
3. Système d'alertes et notifications

### Phase 4 : Sécurité et Optimisation

1. Audit de sécurité complet
2. Tests de performance
3. Optimisation et ajustements

## Tests

### 1. Tests Unitaires

- Tests des services administrateur
- Tests des contrôleurs REST
- Tests des validations et contraintes

### 2. Tests d'Intégration

- Tests des flux complets (ex: création d'un assureur)
- Tests des interactions entre modules

### 3. Tests de Sécurité

- Tests de pénétration
- Tests d'injection SQL
- Tests de validation des entrées

## Documentation

- Documentation API avec Swagger/OpenAPI
- Manuel administrateur détaillé
- Guide de développement pour les évolutions futures

---

*Ce document sera mis à jour régulièrement pour refléter les évolutions du module administrateur d'ARMA-CARE.*