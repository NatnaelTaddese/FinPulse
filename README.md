# FinPulse

## Overview
FinPulse is a comprehensive financial management platform built with **Java Spring Boot** and **Vaadin Flow**. It helps users:
- Manage personal finances
- Set and track budgeting goals
- Monitor financial health metrics
- Generate detailed financial reports
- Integrate with Alipay for real-time transaction tracking

## Features
- **Smart Analytics**: Track spending patterns with advanced insights
- **Budget Management**: Create and manage spending categories
- **Goal Setting**: Set financial goals with progress tracking
- **Alipay Integration**: Connect your Alipay account for transaction monitoring
- **Secure Authentication**: Role-based access control system

## Prerequisites
- Java 17 or higher
- Maven 3.8+
- PostgreSQL 13+
- Git
- Alipay Developer Account

## Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/NatnaelTaddese/finpulse.git
cd finpulse
```

### 2. Database Setup

FinPulse uses PostgreSQL as its database. Ensure PostgreSQL is installed and running on your system if you want it to run locally, or connect to a remote database server. Update the `application.properties` file with your database credentials:

make sure you create a database named `finpulse` in your PostgreSQL server.


1. Create PostgreSQL database:
```sql
CREATE DATABASE finpulse;
```

2. Configure database connection in `application.properties`


```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finpulse
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 3. Alipay Integration Setup

1. Create Alipay Developer Account:
   - Register at [Alipay Open Platform](https://open.alipay.com)
   - Complete business verification
   - Create application in development console

2. Generate API Keys:
   - Go to Alipay Developer Center
   - Create new application
   - Generate public/private key pair
   - Upload public key to Alipay platform
   - Copy credentials to `.env` file:
     - `ALIPAY_APP_ID`
     - `ALIPAY_PRIVATE_KEY`
     - `ALIPAY_PUBLIC_KEY`

3. Configure Redirect URI:
   - Add your domain to Alipay whitelist
   - Update `alipay.redirectUri` in  `application.properties`


   - For development, use:
     ```properties
     alipay.serverUrl=https://openapi.alipaydev.com/gateway.do
     alipay.redirectUri=http://localhost:8080/alipay/connect
     ```
   - For production, use:
     ```properties
     alipay.serverUrl=https://openapi.alipay.com/gateway.do
     alipay.redirectUri=https://your-domain.com/alipay/connect
     ```

### 4. Spring Security Setup

1. Configure initial admin credentials in `.env`


```properties
SPRING_SECURITY_USER_NAME=username(admin by default)
SPRING_SECURITY_USER_PASSWORD=password(admin by default)
```


```properties
spring.security.remember-me.token-validity-seconds=86400
server.servlet.session.timeout=1h
```


### 5. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

Access the application at: http://localhost:8080



---

## Project Structure

FinPulse adheres to the **Maven project structure** for maintainability and modular development:

```
.
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/vaadin/example/
│   │   │       ├── controller/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       ├── service/
│   │   │       ├── views/
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   └── db/
│   │   ├── frontend/
│   │   │   ├── index.html
│   │   │   └── styles/
│   ├── test/
│   │   ├── java/
│   │   │   └── org/vaadin/example/
├── pom.xml
├── .gitignore
└── README.md
```

### Key Directories:
- **controller/**: Manages API logic and routes.
- **model/**: Defines data entities.
- **repository/**: Interfaces for database queries.
- **service/**: Business logic layer.
- **security/**: Authentication and authorization.
- **views/**: Frontend UI components.

---
