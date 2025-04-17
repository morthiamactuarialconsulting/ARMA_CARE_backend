# Modèle conceptuel de données (MCD) d'ARMA-CARE

Ce document sert de référence pour comprendre les différents attributs et concepts utilisés dans le système ARMA-CARE. Il sera mis à jour régulièrement pour refléter les évolutions du modèle de données.

## Entité Insurance (Assureur)

| Attribut | Description | Utilité |
|----------|-------------|---------|
| id | Identifiant unique de l'assureur | Clé primaire pour identifier chaque assureur de manière unique |
| name | Nom de l'assureur | Identification de la compagnie d'assurance |
| type | Type d'assurance | Catégorisation des assureurs (privé, public, mutuelle, etc.) |
| email | Adresse email de l'assureur | Communication électronique et authentification |
| phoneNumber | Numéro de téléphone | Contact direct, format sénégalais validé (commence par +221 ou 00221, suivi de 9 chiffres) |
| address | Adresse postale | Localisation physique de l'assureur |
| city | Ville | Précision de l'adresse |
| postalCode | Code postal | Précision de l'adresse |
| country | Pays | Par défaut "Sénégal" |
| username | Nom d'utilisateur | Authentification dans le système |
| password | Mot de passe | Sécurisation de l'accès (à encoder) |
| licenseNumber | Numéro de licence | Identification légale de l'assureur |
| contactPersonName | Nom du responsable | Identification du point de contact principal |
| contactPersonPosition | Poste du responsable | Précise la fonction et le niveau de responsabilité |
| contactPersonEmail | Email du responsable | Communication directe avec le décideur |
| contactPersonPhone | Téléphone du responsable | Contact direct en cas d'urgence ou pour les questions importantes |
| registrationNumber | Numéro d'enregistrement légal | Identifiant officiel auprès des autorités de régulation |
| registrationDate | Date d'enregistrement | Date à laquelle l'assureur a été officiellement enregistré |
| armaContractNumber | Numéro du contrat ARMA-CARE | Référence unique du contrat liant l'assureur à la plateforme |
| armaContractStartDate | Date de début du contrat | Début de la période de validité du partenariat |
| armaContractEndDate | Date de fin du contrat | Fin de la période de validité du partenariat |
| registrationDocumentPath | Chemin du document d'enregistrement | Accès au document légal d'enregistrement |
| licensePath | Chemin de la licence | Accès au document de licence |
| armaContractPath | Chemin du contrat ARMA-CARE | Accès au document contractuel avec la plateforme |
| contracts | Liste des contrats | Relation OneToMany avec les contrats gérés par cet assureur |
| active | Statut actif | Indique si l'assureur est actuellement en activité |

### Relations importantes

**Relation avec les contrats (contracts)**
- Un assureur peut gérer plusieurs contrats d'assurance
- Permet de retrouver facilement tous les contrats associés à un assureur
- Facilite la gestion des portefeuilles de contrats par assureur
- Remplace l'ancienne relation directe avec les patients, qui passe maintenant par les contrats

## Entité Patient (Assuré)

| Attribut | Description | Utilité |
|----------|-------------|---------|
| id | Identifiant unique du patient | Clé primaire pour identifier chaque patient |
| firstName | Prénom du patient | Identification personnelle |
| lastName | Nom de famille du patient | Identification personnelle |
| dateOfBirth | Date de naissance | Vérification de l'âge et calcul des tarifs liés à l'âge |
| gender | Sexe (M/F) | Informations démographiques pertinentes pour certaines couvertures |
| nationalId | Numéro d'identification national | Identification légale (CNI) |
| address | Adresse | Localisation du patient |
| city | Ville | Précision de l'adresse |
| postalCode | Code postal | Précision de l'adresse |
| country | Pays | Par défaut "Sénégal" |
| phone | Numéro de téléphone | Contact direct, format sénégalais validé |
| email | Adresse email | Communication électronique |
| insuranceContracts | Liste des contrats | Relation OneToMany avec les contrats d'assurance du patient |
| bloodGroup | Groupe sanguin | Information médicale importante en cas d'urgence |
| allergies | Allergies connues | Informations médicales pour la sécurité du patient |
| medicalConditions | Conditions médicales | Informations sur les pathologies existantes |
| active | Statut actif | Indique si le dossier patient est actif ou archivé |

### Méthodes utilitaires

**getCurrentInsurance()**
- Retourne l'assurance actuelle du patient basée sur ses contrats actifs
- Utilise le stream API pour filtrer les contrats actifs et obtenir l'assurance associée
- Permet d'accéder facilement à l'assureur sans avoir à parcourir manuellement les contrats
- Retourne null si le patient n'a pas de contrat actif

### Relations importantes

**Relation avec les contrats d'assurance (insuranceContracts)**
- Un patient peut avoir plusieurs contrats d'assurance (relation OneToMany)
- Les contrats sont liés au patient via la propriété "patient" dans l'entité InsuranceContract
- La cascade CascadeType.ALL permet de propager les opérations (comme la suppression) aux contrats associés
- Cette relation remplace l'ancienne relation directe avec l'assureur, qui passe maintenant par les contrats

## Entité InsuranceContract (Contrat d'assurance)

| Attribut | Description | Utilité |
|----------|-------------|---------|
| id | Identifiant unique du contrat | Clé primaire pour identifier chaque contrat |
| contractNumber | Numéro de contrat | Référence externe du contrat |
| contractType | Type de contrat | Catégorisation du niveau de contrat (basique, standard, premium, etc. à définir avec le direction d'ARMA-CARE) |
| startDate | Date de début de couverture | Définit le début de la période de validité |
| endDate | Date de fin de couverture | Définit la fin de la période de validité |
| deductible | Franchise globale | Montant à la charge du patient avant remboursement pour l'ensemble du contrat |
| active | Statut actif | Indique si le contrat est actuellement en vigueur |
| coverages | Liste des couvertures | Relation OneToMany avec l'entité Coverage pour les différentes couvertures spécifiques |

### Explications détaillées des attributs clés du contrat

**Type de contrat (contractType)**
- Catégorisation du contrat d'assurance (ex: basique, standard, premium, VIP)
- Permet de distinguer rapidement les différents niveaux de contrat offerts
- Facilite la classification et la recherche des contrats
- Aide à la tarification standardisée des services
- Exemple : "Contrat Basique", "Contrat Premium", "Contrat Familial"

**Franchise globale (deductible)**
- Montant que le patient doit payer avant que l'assurance ne commence à rembourser
- Réduit les petites réclamations et les coûts administratifs associés
- Permet de proposer des primes moins élevées (plus la franchise est élevée, moins la prime est chère)
- Responsabilise le patient dans sa consommation de soins
- Exemple : Avec une franchise de 50 000 FCFA, le patient paie les premiers 50 000 FCFA de frais médicaux, puis l'assurance prend en charge selon le taux convenu

**Statut (active)**
- Indique si le contrat est actuellement actif ou non
- Permet de savoir si le contrat est en vigueur sans avoir à vérifier les dates
- Facilite la gestion des contrats suspendus ou résiliés
- Permet de désactiver un contrat sans le supprimer (conservation de l'historique)
- Utile pour les requêtes de recherche de contrats actifs uniquement
- Exemple de statuts : actif, suspendu (pour non-paiement), résilié, expiré

**Relation avec les couvertures (coverages)**
- Un contrat peut avoir plusieurs types de couverture spécifiques
- Permet une grande flexibilité dans la définition des contrats
- Facilite l'ajout de nouvelles couvertures sans modifier la structure existante
- Permet des requêtes précises sur des types de couverture spécifiques

## Entité Coverage (Couverture spécifique)

| Attribut | Description | Utilité |
|----------|-------------|---------|
| id | Identifiant unique de la couverture | Clé primaire pour identifier chaque couverture |
| coverageType | Type de couverture | Catégorie de soins couverts (dentaire, optique, hospitalisation, etc.) |
| coverageRate | Taux de couverture | Pourcentage des frais pris en charge pour ce type de soins (ex: 80%) |
| coverageCeiling | Plafond de couverture | Montant maximal remboursable pour ce type de soins |
| contract | Contrat d'assurance | Relation ManyToOne avec le contrat parent |

### Explications détaillées des attributs de la couverture

**Type de couverture (coverageType)**
- Catégorie spécifique de soins couverts par cette couverture
- Permet de définir précisément quels types de soins sont pris en charge
- Exemples : "dentaire", "optique", "hospitalisation", "consultation", "médicaments", etc.
- Chaque type de couverture peut avoir ses propres conditions (taux, plafond)

**Taux de couverture (coverageRate)**
- Représente le pourcentage des frais médicaux pris en charge par l'assurance pour ce type de soins
- Essentiel pour calculer la part remboursée lors du traitement des factures
- Stocké comme un entier (par exemple, 80 pour 80%)
- Peut varier selon le type de soins (ex: 90% pour hospitalisation, 70% pour dentaire)

**Plafond de couverture (coverageCeiling)**
- Définit le montant maximal que l'assurance s'engage à rembourser pour ce type de soins
- Protège l'assureur contre des coûts excessifs
- Particulièrement important pour les traitements coûteux
- Exemple : plafond de 200 000 FCFA pour les soins dentaires, 1 000 000 FCFA pour hospitalisation

### Avantages de cette approche avec entités séparées

1. **Flexibilité** : Un contrat peut avoir plusieurs couvertures différentes (dentaire, optique, hospitalisation...)
2. **Précision** : Chaque couverture a son propre taux et plafond
3. **Validation des données** : Contraintes spécifiques pour chaque type de couverture
4. **Requêtes plus efficaces** : Possibilité de rechercher par type de couverture spécifique
5. **Évolutivité** : Facilité d'ajout de nouveaux types de couverture sans modifier la structure existante

## Entité Invoice (Facture)

| Attribut | Description | Utilité |
|----------|-------------|---------|
| id | Identifiant unique | Clé primaire pour identifier chaque facture |
| invoiceDate | Date de la facture | Date d'émission de la facture, par défaut date du jour |
| totalAmount | Montant total | Coût total des services médicaux |
| reimbursableAmount | Montant remboursable | Partie du montant éligible au remboursement |
| status | Statut | État de la facture (EN_ATTENTE, PAYEE, REJETEE, REMBOURSEE, PARTIELLEMENT_REMBOURSEE) |
| professional | Professionnel de santé | Prestataire des services médicaux |
| patient | Patient | Bénéficiaire des services médicaux |
| contract | Contrat d'assurance | Contrat applicable pour le remboursement |
| invoiceDocumentPath | Chemin du document | Accès au document de facture numérisé |

### Méthodes utilitaires

**getPatientShare()**
- Calcule automatiquement la part restant à la charge du patient
- Formule : totalAmount - reimbursableAmount
- Permet d'obtenir rapidement le montant que le patient doit payer directement

### Statuts possibles (InvoiceStatus)

| Statut | Description |
|--------|-------------|
| EN_ATTENTE | La facture a été émise mais n'a pas encore été traitée |
| PAYEE | La facture a été payée par le patient |
| REJETEE | La demande de remboursement a été rejetée par l'assurance |
| REMBOURSEE | La facture a été intégralement remboursée par l'assurance |
| PARTIELLEMENT_REMBOURSEE | La facture a été partiellement remboursée par l'assurance |

## Entité Professional (Professionnel de santé)

| Attribut | Description | Utilité |
|----------|-------------|---------|
| id | Identifiant unique | Clé primaire pour identifier chaque professionnel |
| firstName | Prénom du professionnel | Identification personnelle |
| lastName | Nom de famille | Identification personnelle |
| speciality | Spécialisation | Domaine d'expertise médicale |
| phone | Numéro de téléphone | Contact direct, format sénégalais validé |
| email | Adresse email | Communication électronique |
| address | Adresse (rue et numéro) | Localisation précise du cabinet/clinique |
| city | Ville | Localisation géographique |
| postalCode | Code postal | Facilite le tri géographique |
| country | Pays | Par défaut "Sénégal" |
| registrationNumber | Numéro d'enregistrement | Identifiant unique auprès des autorités médicales |
| bankAccountNumberPath | Chemin vers le RIB | Stockage du document contenant les coordonnées bancaires pour les paiements |
| active | Statut actif | Indique si le professionnel est actuellement en activité |
| identityDocumentPath | Chemin vers la pièce d'identité | Stockage du document d'identité (CNI, passeport) |
| diplomaPath | Chemin vers le diplôme | Stockage du diplôme médical |
| licensePath | Chemin vers la licence | Stockage de la licence d'exercice |
| professionalInsurancePath | Chemin vers l'assurance | Stockage de l'attestation d'assurance responsabilité professionnelle |
| documentsVerified | Vérification des documents | Indique si les documents ont été vérifiés par l'administration d'ARMA-CARE |
| accountStatus | Statut du compte | État actuel du compte (PENDING_VERIFICATION, PENDING_ACTIVATION, ACTIVE, SUSPENDED, INACTIVE) |
| statusChangeReason | Raison du changement | Commentaire administratif expliquant le changement de statut |
| statusChangeDate | Date et heure du changement | Date et heure du dernier changement de statut (stocké comme LocalDateTime) |

### Méthodes utilitaires

**getFullName()**
- Retourne le nom complet du professionnel (prénom + nom)
- Facilite l'affichage du nom complet sans concaténation manuelle
- Format : "prénom nom"

**isAccountUsable()**
- Vérifie si le compte est actuellement utilisable (statut ACTIVE)
- Permet de contrôler rapidement l'accès aux fonctionnalités du système
- Retourne un booléen (true si utilisable, false sinon)

### Statuts de compte

| Statut | Description |
|--------|-------------|
| PENDING_VERIFICATION | État initial après inscription, documents en attente de vérification |
| PENDING_ACTIVATION | Documents vérifiés, en attente d'activation par l'administrateur |
| ACTIVE | Compte pleinement activé et utilisable |
| SUSPENDED | Compte temporairement suspendu (suite à un problème) |
| INACTIVE | Compte définitivement inactif (retraite, décès, etc.) |

### Validations importantes

- Format de téléphone sénégalais : commence par +221 ou 00221, suivi de 9 chiffres, ou simplement 9 chiffres commençant par 7, 8 ou 9
- Email valide : format standard d'email
- Numéro d'enregistrement unique : chaque professionnel doit avoir un numéro d'enregistrement différent

## Exemple concret de contrat avec couvertures multiples

Voici un exemple illustrant comment la nouvelle structure permet de gérer différents types de couverture au sein d'un même contrat :

### Contrat d'assurance (InsuranceContract)

```
{
  "id": 1,
  "contractNumber": "CONT-2025-001",
  "contractType": "Premium",
  "startDate": "2025-01-01",
  "endDate": "2025-12-31",
  "deductible": 25000.0,
  "active": true,
  "patient": {
    "id": 42,
    "firstName": "Amadou",
    "lastName": "Diallo"
  },
  "insurance": {
    "id": 3,
    "name": "Sunu Assurance"
  }
}
```

### Couvertures associées (Coverage)

**Couverture 1 : Hospitalisation**
```
{
  "id": 101,
  "coverageType": "hospitalisation",
  "coverageRate": 90,
  "coverageCeiling": 1000000.0,
  "contract": { "id": 1 }
}
```

**Couverture 2 : Consultations**
```
{
  "id": 102,
  "coverageType": "consultation",
  "coverageRate": 80,
  "coverageCeiling": 300000.0,
  "contract": { "id": 1 }
}
```

**Couverture 3 : Soins dentaires**
```
{
  "id": 103,
  "coverageType": "dentaire",
  "coverageRate": 70,
  "coverageCeiling": 200000.0,
  "contract": { "id": 1 }
}
```

**Couverture 4 : Optique**
```
{
  "id": 104,
  "coverageType": "optique",
  "coverageRate": 60,
  "coverageCeiling": 150000.0,
  "contract": { "id": 1 }
}
```

Dans cet exemple, le patient Amadou Diallo a un contrat Premium avec Sunu Assurance qui couvre différents types de soins à des taux variables :
- 90% pour l'hospitalisation (jusqu'à 1 000 000 FCFA)
- 80% pour les consultations (jusqu'à 300 000 FCFA)
- 70% pour les soins dentaires (jusqu'à 200 000 FCFA)
- 60% pour l'optique (jusqu'à 150 000 FCFA)

Une franchise globale de 25 000 FCFA s'applique à l'ensemble du contrat.

---

*Ce glossaire sera mis à jour régulièrement pour refléter les évolutions du modèle de données ARMA-CARE.*