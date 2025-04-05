# Système de Gestion Bancaire

Une application bancaire en console permettant aux utilisateurs de gérer leurs comptes et d'effectuer diverses opérations bancaires.

## Fonctionnalités

### Pour les Clients

- Connexion (authentification avec nom d'utilisateur et mot de passe)
- Dépôt d'argent
- Retrait d'argent
- Consultation du solde
- Virement entre comptes
- Consultation de l'historique des transactions

### Pour les Administrateurs

- Connexion
- Gestion des utilisateurs (ajout, modification, suppression)
- Consultation de toutes les transactions

## Prérequis

- Java JDK 8 ou supérieur
- XAMPP (pour la base de données MySQL)
- MySQL Connector/J (pilote JDBC)

## Instructions d'Installation

### 1. Configuration de la Base de Données (avec XAMPP)

1. Démarrer XAMPP et s'assurer que le service MySQL est en cours d'exécution
2. Ouvrir phpMyAdmin (http://localhost/phpmyadmin)
3. Créer une nouvelle base de données nommée `bank`
4. Copier et exécuter le SQL suivant dans l'onglet SQL de phpMyAdmin :

```sql
-- Création de la table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    userId INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Création de la table des comptes
CREATE TABLE IF NOT EXISTS accounts (
    accountId INT PRIMARY KEY AUTO_INCREMENT,
    userId INT NOT NULL,
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (userId) REFERENCES users(userId)
);

-- Création de la table des transactions
CREATE TABLE IF NOT EXISTS transactions (
    transactionId INT PRIMARY KEY AUTO_INCREMENT,
    fromAccountId INT NOT NULL,
    toAccountId INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    dateTime DATETIME NOT NULL,
    type VARCHAR(20) NOT NULL,
    FOREIGN KEY (fromAccountId) REFERENCES accounts(accountId),
    FOREIGN KEY (toAccountId) REFERENCES accounts(accountId)
);

-- Insertion de l'utilisateur administrateur par défaut
INSERT INTO users (username, password, role) VALUES ('admin', 'admin', 'ADMIN');
INSERT INTO accounts (userId, balance)
SELECT userId, 0.00 FROM users WHERE username = 'admin';
```

### 2. Configuration du Projet

1. Cloner le dépôt :

```bash
git clone https://github.com/mamour-dx/java-project
cd java-project
```

2. Télécharger MySQL Connector/J :

   - Aller sur https://dev.mysql.com/downloads/connector/j/
   - Télécharger le fichier JAR MySQL Connector/J (version 8.x)
   - Créer un répertoire `lib` à la racine du projet
   - Placer le fichier JAR téléchargé dans le répertoire `lib`

3. Compiler le projet :

```bash
javac -cp src/main/java:lib/mysql-connector-j-8.x.x.jar src/main/java/main/Main.java src/main/java/model/*.java src/main/java/repository/*.java src/main/java/service/*.java src/main/java/util/*.java
```

4. Exécuter l'application :

```bash
java -cp src/main/java:lib/mysql-connector-j-8.x.x.jar main.Main
```

Note : Sous Windows, utiliser le point-virgule (;) au lieu des deux-points (:) dans le classpath.

## Compte Administrateur par Défaut

- Nom d'utilisateur : admin
- Mot de passe : admin

## Structure du Projet

```
src/main/java/
├── main/
│   └── Main.java
├── model/
│   ├── User.java
│   ├── Account.java
│   └── Transaction.java
├── repository/
│   ├── UserRepository.java
│   ├── AccountRepository.java
│   └── TransactionRepository.java
├── service/
│   ├── AuthService.java
│   ├── UserService.java
│   └── AccountService.java
└── util/
    ├── DBConnection.java
    └── ConsoleInput.java
```

## Schéma de la Base de Données

### Table Users

- userId (INT, CLÉ PRIMAIRE)
- username (VARCHAR(50), UNIQUE)
- password (VARCHAR(50))
- role (VARCHAR(20))

### Table Accounts

- accountId (INT, CLÉ PRIMAIRE)
- userId (INT, CLÉ ÉTRANGÈRE)
- balance (DECIMAL(10,2))

### Table Transactions

- transactionId (INT, CLÉ PRIMAIRE)
- fromAccountId (INT, CLÉ ÉTRANGÈRE)
- toAccountId (INT, CLÉ ÉTRANGÈRE)
- amount (DECIMAL(10,2))
- dateTime (DATETIME)
- type (VARCHAR(20))

## Notes de Sécurité

- Ceci est une application prototype. En environnement de production :
  - Les mots de passe devraient être hachés
  - Utiliser des requêtes préparées pour toutes les opérations sur la base de données
  - Implémenter une gestion de session appropriée
  - Ajouter une validation et une désinfection des entrées
  - Utiliser une connexion sécurisée (SSL/TLS) pour la communication avec la base de données

## Résolution des Problèmes

1. Problèmes de Connexion à la Base de Données :

   - Vérifier que le service MySQL de XAMPP est en cours d'exécution
   - Vérifier que le nom de la base de données est "bank"
   - Vérifier que les tables sont correctement créées dans phpMyAdmin
   - La connexion par défaut utilise l'utilisateur root sans mot de passe

2. Problèmes de Compilation :

   - Vérifier que Java JDK est installé et que JAVA_HOME est configuré
   - Vérifier que MySQL Connector/J est dans le répertoire lib
   - Vérifier que le classpath inclut à la fois src/main/java et le connecteur MySQL

3. Problèmes d'Exécution :
   - S'assurer que tous les fichiers nécessaires sont compilés
   - Vérifier que le classpath inclut à la fois src/main/java et le connecteur MySQL
   - Vérifier les messages d'erreur dans la console
