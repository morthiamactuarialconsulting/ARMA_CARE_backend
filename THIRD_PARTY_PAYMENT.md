# Guide d'Implémentation du Système Tiers-Payant - ARMA-CARE

## Introduction

Ce document détaille l'architecture et les étapes d'implémentation du système de tiers-payant dans ARMA-CARE. Le tiers-payant permet aux patients de ne payer que la part non remboursée par leur assurance, l'établissement de santé se chargeant de récupérer directement le remboursement auprès de l'assureur.

## Concepts Clés

### Acteurs du Système

1. **Patient** : Bénéficiaire des soins, possède un contrat d'assurance
2. **Professionnel de Santé** : Fournit les soins et émet la facture
3. **Assureur** : Rembourse une partie des frais selon le contrat
4. **ARMA-CARE** : Plateforme intermédiaire facilitant les échanges

### Flux de Paiement

```
Patient → Paiement partiel → Professionnel de Santé
                                    ↓
                           Demande de remboursement
                                    ↓
                                 Assureur → Remboursement → Professionnel de Santé
```

## Architecture du Système

### Entités Principales

- **Invoice** : Facture émise par le professionnel
- **InsuranceContract** : Contrat d'assurance du patient
- **Coverage** : Couvertures spécifiques dans le contrat
- **Claim** : Demande de remboursement auprès de l'assureur
- **Payment** : Paiement (patient vers professionnel ou assureur vers professionnel)

### Statuts de Traitement

| Statut | Description |
|--------|-------------|
| EN_ATTENTE | Facture émise, en attente de paiement patient |
| PAYEE_PARTIELLEMENT | Part patient payée, en attente de remboursement assurance |
| RECLAMATION_ENVOYEE | Demande de remboursement envoyée à l'assureur |
| RECLAMATION_EN_TRAITEMENT | Demande en cours de traitement par l'assureur |
| RECLAMATION_ACCEPTEE | Demande acceptée, en attente de paiement |
| RECLAMATION_REJETEE | Demande rejetée par l'assureur |
| REMBOURSEE | Remboursement reçu de l'assureur |
| COMPLETE | Tous les paiements reçus, facture soldée |

## Étapes d'Implémentation

### 1. Modèle de Données

#### Nouvelle Entité : Claim (Demande de Remboursement)

```java
@Entity
@Table(name = "claims")
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
    
    @ManyToOne
    @JoinColumn(name = "insurance_id", nullable = false)
    private Insurance insurance;
    
    private LocalDate submissionDate;
    private LocalDate responseDate;
    private String claimNumber; // Numéro de dossier chez l'assureur
    private ClaimStatus status;
    private String rejectionReason;
    private Double claimedAmount;
    private Double approvedAmount;
    
    // Enum pour les statuts
    public enum ClaimStatus {
        SUBMITTED, PROCESSING, APPROVED, REJECTED, PAID
    }
    
    // Getters, setters, etc.
}
```

#### Nouvelle Entité : Payment (Paiement)

```java
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
    
    private LocalDate paymentDate;
    private Double amount;
    private String transactionReference;
    
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    // Type de paiement (patient ou assurance)
    public enum PaymentType {
        PATIENT_PAYMENT, INSURANCE_REIMBURSEMENT
    }
    
    // Méthode de paiement
    public enum PaymentMethod {
        CASH, CARD, BANK_TRANSFER, CHECK, MOBILE_MONEY
    }
    
    // Getters, setters, etc.
}
```

#### Mise à Jour de l'Entité Invoice

Ajouter des relations avec les nouvelles entités :

```java
@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Claim> claims = new ArrayList<>();

@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Payment> payments = new ArrayList<>();
```

### 2. Services Métier

#### Calcul du Reste à Payer - Pattern Stratégie

##### Interface Stratégie pour le Calcul

```java
public interface PatientShareCalculationStrategy {
    /**
     * Calcule le montant restant à la charge du patient
     * @param invoice Facture à traiter
     * @param contract Contrat d'assurance du patient
     * @param coverages Liste des couvertures applicables
     * @return Montant à la charge du patient
     */
    Double calculatePatientShare(Invoice invoice, InsuranceContract contract, List<Coverage> coverages);
    
    /**
     * Détermine si cette stratégie est applicable pour ce type de contrat
     */
    boolean isApplicable(InsuranceContract contract, String serviceType);
}
```

##### Implémentations Concrètes pour Différents Types de Contrats

```java
@Service
@Qualifier("basicContractStrategy")
public class BasicContractStrategy implements PatientShareCalculationStrategy {
    @Override
    public Double calculatePatientShare(Invoice invoice, InsuranceContract contract, List<Coverage> coverages) {
        // Logique pour contrats basiques
        // Exemple: franchise fixe + pourcentage restant selon couverture
        double totalAmount = invoice.getTotalAmount();
        double deductible = contract.getDeductible();
        
        // Trouver la couverture applicable
        Optional<Coverage> applicableCoverage = coverages.stream()
            .filter(c -> c.getCoverageType().equals(invoice.getServiceType()))
            .findFirst();
            
        if (applicableCoverage.isPresent()) {
            double coverageRate = applicableCoverage.get().getCoverageRate() / 100.0;
            double amountAfterDeductible = Math.max(0, totalAmount - deductible);
            double insurancePart = amountAfterDeductible * coverageRate;
            return totalAmount - insurancePart;
        } else {
            // Pas de couverture pour ce service
            return totalAmount;
        }
    }
    
    @Override
    public boolean isApplicable(InsuranceContract contract, String serviceType) {
        return "BASIC".equals(contract.getContractType());
    }
}

@Service
@Qualifier("premiumContractStrategy")
public class PremiumContractStrategy implements PatientShareCalculationStrategy {
    @Override
    public Double calculatePatientShare(Invoice invoice, InsuranceContract contract, List<Coverage> coverages) {
        // Logique pour contrats premium
        // Exemple: pas de franchise, taux de couverture élevé
        double totalAmount = invoice.getTotalAmount();
        
        // Trouver la couverture applicable
        Optional<Coverage> applicableCoverage = coverages.stream()
            .filter(c -> c.getCoverageType().equals(invoice.getServiceType()))
            .findFirst();
            
        if (applicableCoverage.isPresent()) {
            double coverageRate = applicableCoverage.get().getCoverageRate() / 100.0;
            double coverageCeiling = applicableCoverage.get().getCoverageCeiling();
            
            // Calcul avec plafond de couverture
            double insurancePart = Math.min(totalAmount * coverageRate, coverageCeiling);
            return totalAmount - insurancePart;
        } else {
            // Pas de couverture pour ce service
            return totalAmount;
        }
    }
    
    @Override
    public boolean isApplicable(InsuranceContract contract, String serviceType) {
        return "PREMIUM".equals(contract.getContractType());
    }
}

@Service
@Qualifier("seniorContractStrategy")
public class SeniorContractStrategy implements PatientShareCalculationStrategy {
    @Override
    public Double calculatePatientShare(Invoice invoice, InsuranceContract contract, List<Coverage> coverages) {
        // Logique spécifique pour les contrats senior
        // Exemple: couverture plus élevée pour certains services
        // ...
    }
    
    @Override
    public boolean isApplicable(InsuranceContract contract, String serviceType) {
        return "SENIOR".equals(contract.getContractType());
    }
}
```

##### Contexte pour le Calcul

```java
@Service
public class PatientShareCalculationService {
    private final List<PatientShareCalculationStrategy> strategies;
    
    @Autowired
    public PatientShareCalculationService(List<PatientShareCalculationStrategy> strategies) {
        this.strategies = strategies;
    }
    
    /**
     * Calcule le montant restant à la charge du patient en utilisant la stratégie appropriée
     */
    public Double calculatePatientShare(Invoice invoice, InsuranceContract contract) {
        // Récupérer les couvertures du contrat
        List<Coverage> coverages = contract.getCoverages();
        String serviceType = invoice.getServiceType();
        
        // Trouver la stratégie applicable
        PatientShareCalculationStrategy strategy = strategies.stream()
            .filter(s -> s.isApplicable(contract, serviceType))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Aucune stratégie de calcul trouvée pour ce contrat"));
        
        // Appliquer la stratégie
        return strategy.calculatePatientShare(invoice, contract, coverages);
    }
}
```

#### ClaimService

```java
@Service
public class ClaimService {
    // Création d'une demande de remboursement
    public Claim createClaim(Invoice invoice);
    
    // Envoi de la demande à l'assureur
    public Claim submitClaim(Long claimId);
    
    // Mise à jour du statut de la demande
    public Claim updateClaimStatus(Long claimId, ClaimStatus newStatus, String comment);
    
    // Enregistrement de la réponse de l'assureur
    public Claim recordInsuranceResponse(Long claimId, boolean approved, Double approvedAmount, String reason);
}
```

#### PaymentService

```java
@Service
public class PaymentService {
    private final PatientShareCalculationService calculationService;
    
    @Autowired
    public PaymentService(PatientShareCalculationService calculationService) {
        this.calculationService = calculationService;
    }
    
    // Enregistrement du paiement patient
    public Payment recordPatientPayment(Invoice invoice, Double amount, PaymentMethod method, String reference);
    
    // Enregistrement du remboursement assurance
    public Payment recordInsuranceReimbursement(Claim claim, Double amount, String reference);
    
    // Calcul du solde restant sur une facture
    public Double calculateRemainingBalance(Invoice invoice) {
        // Utilise le service de calcul pour déterminer la part patient
        Double patientShare = calculationService.calculatePatientShare(
            invoice, invoice.getPatient().getCurrentInsurance());
            
        // Soustraire les paiements déjà effectués
        Double paidAmount = invoice.getPayments().stream()
            .mapToDouble(Payment::getAmount)
            .sum();
            
        return patientShare - paidAmount;
    }
}
```

#### InvoiceService (Mise à jour)

```java
@Service
public class InvoiceService {
    // Mise à jour automatique du statut de la facture
    public void updateInvoiceStatus(Long invoiceId);
    
    // Génération du document de facture
    public String generateInvoiceDocument(Long invoiceId);
    
    // Génération de la demande de remboursement
    public String generateClaimDocument(Long claimId);
}
```

### 3. API REST

#### ClaimController

```java
@RestController
@RequestMapping("/api/claims")
public class ClaimController {
    @PostMapping("/invoices/{invoiceId}")
    public ResponseEntity<Claim> createClaim(@PathVariable Long invoiceId);
    
    @PostMapping("/{claimId}/submit")
    public ResponseEntity<Claim> submitClaim(@PathVariable Long claimId);
    
    @PutMapping("/{claimId}/status")
    public ResponseEntity<Claim> updateStatus(@PathVariable Long claimId, @RequestBody ClaimStatusUpdateDTO dto);
    
    @GetMapping("/insurance/{insuranceId}")
    public ResponseEntity<Page<Claim>> getClaimsByInsurance(@PathVariable Long insuranceId, Pageable pageable);
}
```

#### PaymentController

```java
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @PostMapping("/patient/{invoiceId}")
    public ResponseEntity<Payment> recordPatientPayment(@PathVariable Long invoiceId, @RequestBody PaymentDTO dto);
    
    @PostMapping("/insurance/{claimId}")
    public ResponseEntity<Payment> recordInsurancePayment(@PathVariable Long claimId, @RequestBody PaymentDTO dto);
    
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<Payment>> getPaymentsByInvoice(@PathVariable Long invoiceId);
}
```

### 4. Intégration avec les Assureurs - Pattern Stratégie

#### Interface Stratégie

```java
public interface InsuranceIntegrationStrategy {
    // Envoi électronique de la demande
    ClaimSubmissionResult submitClaim(Claim claim, byte[] supportingDocuments);
    
    // Vérification du statut d'une demande
    ClaimStatus checkClaimStatus(String claimNumber, Insurance insurance);
    
    // Notification de paiement reçu
    void notifyPaymentReceived(String claimNumber, Double amount, String reference);
    
    // Vérification d'éligibilité
    EligibilityResult checkEligibility(Patient patient, Professional professional, String serviceCode);
}
```

#### Implémentations Concrètes de la Stratégie

```java
@Service
@Qualifier("genericInsurance")
public class GenericInsuranceStrategy implements InsuranceIntegrationStrategy {
    // Implémentation par défaut
    @Override
    public ClaimSubmissionResult submitClaim(Claim claim, byte[] supportingDocuments) {
        // Logique générique d'envoi de demande
        return new ClaimSubmissionResult(true, "GEN-" + System.currentTimeMillis(), null);
    }
    
    // Autres méthodes implémentées...
}

@Service
@Qualifier("sunuAssurance")
public class SunuAssuranceStrategy implements InsuranceIntegrationStrategy {
    // Implémentation spécifique pour Sunu Assurance
    @Override
    public ClaimSubmissionResult submitClaim(Claim claim, byte[] supportingDocuments) {
        // Logique spécifique à Sunu Assurance
        // Par exemple: format spécial, API propriétaire, etc.
        return new ClaimSubmissionResult(true, "SUNU-" + System.currentTimeMillis(), null);
    }
    
    // Autres méthodes implémentées...
}

@Service
@Qualifier("amsa")
public class AmsaStrategy implements InsuranceIntegrationStrategy {
    // Implémentation spécifique pour AMSA
    // ...
}
```

#### Contexte Utilisant la Stratégie

```java
@Service
public class InsuranceIntegrationService {
    private final Map<String, InsuranceIntegrationStrategy> strategies;
    
    @Autowired
    public InsuranceIntegrationService(List<InsuranceIntegrationStrategy> strategyList) {
        // Initialiser la map des stratégies disponibles
        strategies = new HashMap<>();
        for (InsuranceIntegrationStrategy strategy : strategyList) {
            String name = strategy.getClass().getAnnotation(Qualifier.class).value();
            strategies.put(name, strategy);
        }
    }
    
    public InsuranceIntegrationStrategy getStrategy(Insurance insurance) {
        // Déterminer quelle stratégie utiliser en fonction de l'assureur
        String strategyName = insurance.getIntegrationStrategy();
        return strategies.getOrDefault(strategyName, strategies.get("genericInsurance"));
    }
    
    public ClaimSubmissionResult submitClaim(Claim claim, byte[] documents) {
        // Obtenir la stratégie appropriée et l'exécuter
        InsuranceIntegrationStrategy strategy = getStrategy(claim.getInsurance());
        return strategy.submitClaim(claim, documents);
    }
    
    // Autres méthodes déléguant à la stratégie appropriée...
}
```

### 5. Sécurité et Authentification

- Authentification forte pour toutes les opérations financières
- Journalisation détaillée de toutes les transactions
- Chiffrement des données sensibles
- Signature électronique des documents de réclamation

### 6. Rapports et Tableaux de Bord

#### Rapports pour les Professionnels

- Factures en attente de remboursement
- Historique des remboursements par assureur
- Délais moyens de remboursement
- Taux de rejet par assureur

#### Rapports pour les Administrateurs

- Volume global de transactions
- Montants totaux traités par période
- Performance du système de tiers-payant
- Alertes sur les retards de paiement

## Plan de Test

1. **Tests Unitaires** : Validation des calculs de remboursement
2. **Tests d'Intégration** : Flux complet de facturation et remboursement
3. **Tests de Performance** : Capacité à traiter un volume important de demandes
4. **Tests de Sécurité** : Protection des données financières

## Déploiement et Mise en Production

1. Déploiement progressif par phases
2. Formation des utilisateurs
3. Période de double-saisie pour validation
4. Surveillance renforcée post-déploiement

## Patterns de Conception Additionnels

### Pattern Observateur

Utilisé pour notifier les différentes parties des changements de statut des demandes:

```java
public interface ClaimStatusObserver {
    void onClaimStatusChanged(Claim claim, ClaimStatus oldStatus, ClaimStatus newStatus);
}

// Implémentations possibles:
@Service
public class ProfessionalNotifier implements ClaimStatusObserver {
    // Notifie le professionnel des changements de statut
}

@Service
public class PatientNotifier implements ClaimStatusObserver {
    // Notifie le patient des changements de statut
}

@Service
public class AdminNotifier implements ClaimStatusObserver {
    // Notifie les administrateurs des changements de statut
}
```

### Pattern Factory Method

Pour créer les documents appropriés selon le type de demande:

```java
public abstract class DocumentFactory {
    public abstract Document createDocument(Claim claim);
}

public class ClaimDocumentFactory extends DocumentFactory {
    @Override
    public Document createDocument(Claim claim) {
        // Logique de création du document de demande
    }
}

public class InvoiceDocumentFactory extends DocumentFactory {
    @Override
    public Document createDocument(Claim claim) {
        // Logique de création de la facture
    }
}
```

### Pattern Chain of Responsibility

Pour le traitement des demandes par différents niveaux d'approbation:

```java
public abstract class ClaimApprovalHandler {
    protected ClaimApprovalHandler nextHandler;
    
    public void setNextHandler(ClaimApprovalHandler handler) {
        this.nextHandler = handler;
    }
    
    public abstract ApprovalResult handleApproval(Claim claim);
}

// Exemples d'implémentations:
public class AutomaticApprovalHandler extends ClaimApprovalHandler {
    // Approuve automatiquement les petites demandes
}

public class ManagerApprovalHandler extends ClaimApprovalHandler {
    // Nécessite l'approbation d'un gestionnaire pour les demandes moyennes
}

public class DirectorApprovalHandler extends ClaimApprovalHandler {
    // Nécessite l'approbation d'un directeur pour les grandes demandes
}
```

## Évolutions Futures

- Intégration d'une API de vérification d'éligibilité en temps réel
- Application mobile pour suivi des remboursements
- Intelligence artificielle pour prédiction des délais de remboursement
- Blockchain pour sécuriser l'historique des transactions
- Extension du système de patterns pour plus de flexibilité et d'adaptabilité