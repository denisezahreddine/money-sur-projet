MoneySur Backend
## Project Setup Check

If the project is not recognized as a Maven project, right-click on pom.xml and select Add as Maven Project.

## Development Server

Navigate to the backend application folder:

cd backend/app


## Build and run the Docker image:

docker compose up


## Run the Spring Boot application:

mvn spring-boot:run


## Configuration des variables d'environnement


À la racine du projet, créez un fichier nommé `.env`. Ce fichier est ignoré par Git pour des raisons de sécurité. 

**Étapes :**
1. Copiez le fichier d'exemple fourni :
   ```bash
   cp .env.example .env
Ouvrez le fichier .env et remplissez les champs vides, notamment vos identifiants Mailtrap (MAIL_USERNAME et MAIL_PASSWORD).

## Flux de Test (Postman & Mailtrap)
L'application utilise Mailtrap Sandbox pour intercepter les emails en développement.

**Étape 1 : Inscription (Postman)**
Méthode : POST

URL : http://localhost:8080/api/auth/register

Body (JSON) :

JSON
```properties
{
"firstName": "John",
"lastName": "Doe",
"email": "john.doe@example.com",
"password": "Password123!"
}
```

**Étape 2 : Validation d'Email (Mailtrap)**
Allez sur votre compte https://mailtrap.io

Dans Email Testing > Inboxes, ouvrez le mail "Bienvenue sur MoneySur".

Cliquez sur le bouton ACTIVER MON COMPTE.

**Étape 3 : Vérification et JWT**
Le clic ouvrira votre navigateur sur : http://localhost:8080/api/auth/verify-email?token=...

Si le token est valide, message de Succès s'affiche 
Base de données : Le champ is_email_verified passera à 1 pour cet utilisateur.