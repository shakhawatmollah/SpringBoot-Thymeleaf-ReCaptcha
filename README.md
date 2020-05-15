# SpringBoot-Thymeleaf-ReCaptcha
SpringBoot Thymeleaf Google ReCaptcha with Contact-Form. Sending mail and store contact information in the database

# Features!
  - Spring Boot with Thymeleaf
  - Front-end and Back-end validation
  - After maximum allowed time to block client IP.
  - Sending mail (Java MimeMessage)
  - Contact information store in database
 
## System Requirements
- Java 8 or higher
- Apache Maven 3.3+
- MySQL 5.6+

## How to use
Set your secret key in application.properties file:

[Google reCaptcha Admin Console](https://www.google.com/recaptcha/admin/create)

```properties
# Google reCAPTCHA v2 keys
google.recaptcha.key.site=<your_site_key>
google.recaptcha.key.secret=<your_secret_key>
```
Mail Configuration
```properties
spring.mail.username=<gamil_id>
spring.mail.password=<gmail_password>
```
MySQL Database Configuration
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/<database_name>
spring.datasource.username=<DB_USERNAME>
spring.datasource.password=<DB_PASSWORD>
```

Befor run the application, make sure you have installed
```sh
# run the command for check Java
$ java -version
# run the command for check maven
$ mvn -version
```

### Installation
Install the dependencies and start the server.

```sh
$ git clone git@github.com:shakhawatm/SpringBoot-Thymeleaf-ReCaptcha.git
$ cd SpringBoot-Thymeleaf-ReCaptcha
$ mvn clean install
$ mvn spring-boot:run
```
### SpringBoot-Thymeleaf-Captcha Web Example
Testing: [http://localhost:8090/contact-us](http://localhost:8090/contact-us)