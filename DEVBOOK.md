# Guide des Bonnes Pratiques de Développement - ARMA-CARE

## Introduction

Ce document présente les bonnes pratiques de développement à suivre pour le projet ARMA-CARE. Il sert de référence pour tous les développeurs travaillant sur le projet afin d'assurer la cohérence, la maintenabilité et la qualité du code.

## Structure du Projet

### Architecture en Couches

Le projet ARMA-CARE suit une architecture en couches classique :

1. **Couche Présentation** : Contrôleurs REST, gestion des requêtes et réponses
2. **Couche Service** : Logique métier
3. **Couche Repository** : Accès aux données
4. **Couche Modèle** : Entités et objets de transfert de données (DTO)

### Organisation des Packages

```
com.armacare
├── controller    // Contrôleurs REST
├── service       // Services métier
├── repository    // Repositories pour l'accès aux données
├── model         // Entités JPA
├── dto           // Objets de transfert de données
├── exception     // Exceptions personnalisées
├── config        // Configuration
└── util          // Classes utilitaires
```

## Conventions de Nommage

### Général

- Utiliser l'anglais pour les noms de classes, méthodes, variables et attributs
- Utiliser le français pour les commentaires et la documentation
- Utiliser l'anglais pour les termes techniques standards (ex: repository, service)

### Classes

- Noms en PascalCase (ex: `PatientService`, `BillingController`)
- Suffixes selon le rôle : `Controller`, `Service`, `Repository`, etc.

### Méthodes

- Noms en camelCase (ex: `findPatientById`, `calculateReimbursableAmount`)
- Verbes d'action pour décrire l'opération

### Variables et Attributs

- Noms en camelCase (ex: `totalAmount`, `invoiceDate`)
- Noms descriptifs et complets (éviter les abréviations)

## Gestion des Exceptions

- Créer des exceptions métier personnalisées dans le package `exception`
- Utiliser un gestionnaire global d'exceptions pour les contrôleurs REST
- Journaliser les exceptions avec des niveaux appropriés

## Tests

- Écrire des tests unitaires pour chaque service et repository
- Utiliser JUnit 5 et Mockito
- Viser une couverture de code d'au moins 80%

### Tests des dates et heures

- Utiliser `LocalDateTime` pour représenter les dates et heures dans les entités
- Dans les tests, comparer les objets `LocalDateTime` directement avec `assertEquals` pour une vérification précise
- Pour les tests de repository, s'assurer que les dates sont correctement formatées dans les scripts SQL (utiliser `TIMESTAMP` pour H2)
- Exemple de test pour les dates :
  ```java
  LocalDateTime expectedDateTime = LocalDateTime.parse("2023-01-01T16:00:00");
  LocalDateTime actualDateTime = professional.getStatusChangeDate();
  assertEquals(expectedDateTime, actualDateTime);
  ```

## Validation des Données

- Utiliser les annotations de validation de Jakarta Bean Validation
- Valider les entrées au niveau des contrôleurs
- Documenter les contraintes de validation dans les entités

## Sécurité

- Ne jamais stocker de mots de passe en clair
- Utiliser Spring Security pour l'authentification et l'autorisation
- Valider toutes les entrées utilisateur pour prévenir les injections

## Documentation

- Documenter toutes les API REST avec Swagger/OpenAPI
- Ajouter des commentaires JavaDoc pour les méthodes publiques
- Maintenir à jour ce guide et le modèle de données de référence

## Versionnement

- Utiliser Git avec la convention de branches GitFlow
- Format des messages de commit : `[TYPE] Description concise`
  - Types : FEAT, FIX, DOCS, STYLE, REFACTOR, TEST, CHORE

## Performance

- Optimiser les requêtes de base de données (utiliser des projections quand approprié)
- Paginer les résultats pour les listes volumineuses
- Mettre en cache les données fréquemment accédées et peu modifiées

## Internationalisation

- Externaliser tous les messages dans des fichiers de propriétés
- Supporter le français comme langue principale
- Préparer l'infrastructure pour d'autres langues futures

## Logging

- Utiliser SLF4J avec Logback
- Définir des niveaux de log appropriés selon l'environnement
- Inclure des informations contextuelles dans les logs

## Revue de Code

- Toutes les modifications doivent passer par une revue de code
- Vérifier la conformité aux standards définis dans ce document
- Utiliser des outils d'analyse statique de code (SonarQube)