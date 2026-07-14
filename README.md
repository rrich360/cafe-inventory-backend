# Cafe Inventory Management System — Backend

Spring Boot REST API for a full-stack cafe inventory management system: inventory/category/product tracking, vendor management, stock order generation with PDF export, and JWT-based authentication with role-based access control.

This is the local-only baseline version of the project — runs entirely against a local MySQL instance with no cloud dependencies. The AWS-deployed version (EC2 + RDS + S3) lives in `cafe-inventory-backend-aws` (link added once that repo is live).

## Tech stack

- Java 21, Spring Boot 3.3.2
- Spring Data JPA + MySQL
- Spring Security with JWT (jjwt + java-jwt)
- iText / PDFBox for stock order PDF generation
- Maven

## Getting started

1. Create a local MySQL database named `cafe_management_sys`.
2. Update `src/main/resources/application.properties` with your own local MySQL username/password (the checked-in values are placeholders, not real credentials).
3. Build and run:
   ```
   mvn clean install
   mvn spring-boot:run
   ```
4. The API starts on `http://localhost:8081`.

## Related

- Frontend: [cafe-inventory-frontend](https://github.com/rrich360/cafe-inventory-frontend) (Angular 14)
- AWS-deployed variant: `cafe-inventory-backend-aws` (coming soon)
- Part of the "From Code to Cloud" article series documenting this project's journey from localhost to AWS
