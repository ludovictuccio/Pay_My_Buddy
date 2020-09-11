# Pay_My_Buddy

<p>The java application "Pay My Buddy" is an app used to send money to friends.</p> 
<p>The user can add friends with email (if exists in database), send money to friend's app account, make personal payments (bank account to app account, via bank card) or personal transfer (app account to bank account, via Iban). </p>
<p>This app is run with Spring Boot and Maven.</p>
<p>Only model, service and repository application layers are available for this version 1.0</p>

## Infos - Configuration

- **Author:** Ludovic Tuccio

- **Java** 11

- **MySql** 8.0

- **Spring Boot** 2.3.2

- **Spring Data Jpa**

- **Hibernate**

- **Actuators in service:** health, info, httptrace & metrics

## Database

- The application used a database named "pmb_database".

- The file **schema.sql** (available in *"/src/main/resources"*) contains SQL Scrypt to create dev database.

- The file **dropAndCreate.sql** (available in *"/src/test/resources"*) contains SQL Scrypt to create test database, and **dbTest.sql** contains data to use the db for tests.

## Endpoints

- **Application:**  http://localhost:9090/

- **Actuator:**  http://localhost:8080/


## Security

- Database password and username encryption with Jasypt. The user's email and password are encrypted with BCryptPasswordEncoder (Spring Security).

## Installing

1.Install Java:

https://www.oracle.com/java/technologies/javase-jdk11-downloads.html

2.Install Maven:

https://maven.apache.org/install.html

3.Install MySql:

https://dev.mysql.com/downloads/installer/

## Testing

**Launch: mvn site** : for checkstyle, findbugs, surefire and jacoco reports (available in *"target/site"*).

## MPD

![Screenshot](MPD.png)

## Modèle métier

![Screenshot](Modele_metier.png)

## Modèle Java

![Screenshot](Modele_java.png)
