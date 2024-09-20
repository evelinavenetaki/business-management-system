# Business Management System - Backend

## Περιγραφή

Αυτό είναι το backend για το σύστημα διαχείρισης επιχειρήσεων, το οποίο έχει υλοποιηθεί με το Spring Boot και χρησιμοποιεί PostgreSQL για τη βάση δεδομένων. Παρέχει API endpoints για διαχείριση χρηστών, αιτήσεων, και ρόλων, με χρήση JWT για αυθεντικοποίηση.

## Προαπαιτούμενα

Πριν ξεκινήσετε, βεβαιωθείτε ότι έχετε εγκαταστήσει τα εξής:

- [Java 17+](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/)

## Οδηγίες Εγκατάστασης

Ακολουθήστε τα παρακάτω βήματα για να ρυθμίσετε το backend και να το εκτελέσετε τοπικά.

### 1. Κλώνος του Αποθετηρίου

Κάντε κλώνο του αποθετηρίου GitHub στον τοπικό σας υπολογιστή:
git clone https://github.com/evelinavenetaki/business-management-system.git

Για να ρυθμίσετε τη βάση δεδομένων:

Δημιουργήστε μία νέα βάση δεδομένων στο PostgreSQL με το όνομα business_management:

Ανοίξτε το τερματικό και εκτελέστε την παρακάτω εντολή:
createdb -U <username> business_management


Επαναφέρετε τη βάση δεδομένων από το αρχείο business_management_database.sql

psql -U <username> -d business_management -f business_management_database.sql

3. Ρύθμιση των Παραμέτρων
Ανοίξτε το αρχείο src/main/resources/application.properties και ελέγξτε τις παρακάτω παραμέτρους για να βεβαιωθείτε ότι είναι σωστά ρυθμισμένες:

properties
Copy code
# PostgreSQL settings
spring.datasource.url=jdbc:postgresql://localhost:5432/business_management
spring.datasource.username=<username>
spring.datasource.password=<password>

# JPA settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect


