# Pay_My_Buddy

<p>The java application "Pay My Buddy" is run with Spring Boot and Maven.</p>

## Infos - Configuration

- **Author:** Ludovic Tuccio

- **Java version:** 11

- **MySql version:** 8.0

- **Spring Boot version:** 2.3.2

- **Spring Data Jpa**

- **Spring Security**

- **Hibernate**

- **Actuators in service:** health, info, httptrace & metrics

- **Tests:** the app has unit tests and integration tests written.

## Endpoints

- **Application:**  http://localhost:9090/

- **Actuator:**  http://localhost:8080/


## Security

- Database password encryption

## Installing

1.Install Java:

https://www.oracle.com/java/technologies/javase-jdk11-downloads.html

2.Install Maven:

https://maven.apache.org/install.html

3. Install MySql:
https://dev.mysql.com/downloads/installer/

## Testing

**Launch :**
 
- **mvn test** : for launch tests

- **mvn jacoco:report** : for jacoco coverage report (available in *"target/site/jacoco"*).

- **mvn site** : for checkstyle, findbugs & surefire reports (available in *"target/site"*).


**To test endpoints:**
<ol>
	<li>Install Postman https://www.postman.com/</li> 
		OR
<li>Use localhost http://localhost:8080/{}</li> 
</ol>

## UML 

-

## MPD

-

## Features

### Registration

**POST** - http://localhost:9090/registration >>> add a new user. Conflict if email addree already exist in DB, or with a null value
<pre><code>
	[
	    {
			"lastname": "Prevert",
			"firstname": "Jacques",
			"email": "j.prevert@gmail.com",
			"password": "love-france",
			"phone": "0238440000"
}
	]
</pre></code>
