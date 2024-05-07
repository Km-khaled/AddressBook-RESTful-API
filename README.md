# Carnet d'adresses RESTful API

Cette application est une API RESTful développée en Java avec Jersey qui permet de gérer un carnet d'adresses. Elle fournit des fonctionnalités pour créer, lire, mettre à jour et supprimer des carnets d'adresses et des adresses associées.

## Fonctionnalités

- Créer un nouveau carnet d'adresses
- Lister tous les carnets d'adresses existants
- Supprimer un carnet d'adresses existant
- Ajouter une nouvelle adresse à un carnet spécifique
- Lister toutes les adresses d'un carnet spécifique
- Récupérer les détails d'une adresse spécifique
- Supprimer une adresse spécifique d'un carnet

## Technologies utilisées

- Java
- Jersey (JAX-RS)
- MySQL

## Prérequis

- Java Development Kit (JDK) installé
- MySQL Server installé

## Configuration

1. Clonez le dépôt GitHub sur votre machine locale.
2. Configurez les paramètres de connexion à la base de données MySQL dans le fichier `SingletonConnection.java`.
3. Créez la base de données et les tables nécessaires en exécutant les scripts SQL fournis.

## Exécution

1. Compilez le code source Java.
2. Déployez l'application sur un serveur d'applications compatible avec Jersey.
3. Accédez aux ressources RESTful en utilisant l'URL de base et les chemins d'accès appropriés.

## Exemples d'utilisation

- Créer un nouveau carnet d'adresses :
  `POST /carnets` avec les données du carnet dans le corps de la requête.

- Lister tous les carnets d'adresses :
  `GET /carnets`

- Supprimer un carnet d'adresses :
  `DELETE /carnets` avec le nom du carnet dans le corps de la requête.

- Ajouter une nouvelle adresse :
  `POST /carnets/{nom_carnet}/adresses` avec les données de l'adresse dans le corps de la requête.

- Lister toutes les adresses d'un carnet :
  `GET /carnets/{nom_carnet}/adresses`

- Récupérer les détails d'une adresse :
  `GET /carnets/{nom_carnet}/adresses/{nom_personne}`

- Supprimer une adresse :
  `DELETE /carnets/{nom_carnet}/adresses` avec le nom de la personne dans le corps de la requête.

## Contribution

Les contributions sont les bienvenues ! Si vous avez des suggestions d'amélioration, des corrections de bogues ou de nouvelles fonctionnalités, n'hésitez pas à soumettre une pull request.

## Licence

Ce projet est sous licence [MIT](LICENSE).
