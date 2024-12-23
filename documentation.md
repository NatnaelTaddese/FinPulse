# FinPulse Documentation

## Introduction

Welcome to the **FinPulse Documentation**! FinPulse is a sophisticated financial management platform developed using **Java Spring Boot** and **Vaadin Flow**. It empowers users to manage their finances, set budgeting goals, monitor financial health, and generate comprehensive financial reports. This documentation is tailored for developers, contributors, and technical stakeholders to provide a detailed understanding of the platform's architecture, setup, and extensibility.

## Overview

**FinPulse** integrates a suite of modern technologies to deliver a seamless financial management experience:

- **Java 17**: Provides robust backend logic and application scalability.
- **Spring Boot**: Streamlines backend service management and dependency injection.
- **Vaadin Flow**: Enables dynamic frontend UI components.
- **Hibernate ORM**: Simplifies object-relational mapping for database interactions.
- **PostgreSQL**: Ensures data integrity and efficient relational database management.
- **Spring Security**: Implements secure authentication and authorization.
- **Alipay SDK**: Facilitates transaction handling and financial data synchronization.

## Key Components

### Dashboard

The dashboard provides an overview of the user's financial status, including:

- **Current Savings**: Displays the user's current savings with an option to toggle the visibility of the amount.
- **Transaction Pie Chart**: Visual representation of the user's expenses categorized by type.
- **Spending Performance**: A solid gauge chart showing the user's spending performance against their daily limit.
- **Spending Trends**: A column chart displaying the user's spending trends over the last 12 days.

### Budget Management

Users can manage their budget through the BudgetView, which includes:

- **Savings Goal**: Set and track monthly savings goals.
- **Spending Categories**: Add and manage spending categories with specific budget amounts.
- **Daily Spending Limit**: Set and update daily spending limits to control expenses.

### Alipay Integration

The Alipay integration allows users to:

- **Connect Alipay Account**: Users can connect their Alipay account to FinPulse for seamless data synchronization.
- **View Account Balance**: Display the current balance of the connected Alipay account.
- **Fetch Recent Transactions**: Retrieve and display recent transactions from the Alipay account.

### Security

FinPulse uses Spring Security to ensure secure user authentication and authorization. Key security features include:

- **Login and Signup**: Secure login and signup processes with password encryption.
- **Remember Me**: Option to remember user sessions for convenience.
- **Role-Based Access Control**: Different user roles with specific permissions.


This documentation will cover:
- Project setup and configuration
- Database schema and optimization
- Core modules and service layers
- REST API endpoints
- Alipay payment integration
- Testing methodologies
- Deployment strategies

---

## Table of Contents

1. [Introduction](#introduction)
2. [Project Structure](#project-structure)
3. [Setup and Installation](#setup-and-installation)
    - [Prerequisites](#prerequisites)
    - [Cloning the Repository](#cloning-the-repository)
    - [Environment Configuration](#environment-configuration)
    - [Running the Application](#running-the-application)
4. [Database Setup](#database-setup)
    - [Database Configuration](#database-configuration)
    - [Entities Overview](#entities-overview)
    - [Database Migrations](#database-migrations)
5. [Spring Boot Overview](#spring-boot-overview)
    - [Application Configuration](#application-configuration)
    - [Dependency Injection](#dependency-injection)
    - [Service Layer](#service-layer)
6. [Security](#security)
    - [Authentication and Authorization](#authentication-and-authorization)
    - [User Roles](#user-roles)
    - [Session Management](#session-management)
7. [Vaadin UI](#vaadin-ui)
    - [Views and Components](#views-and-components)
    - [Routing and Navigation](#routing-and-navigation)
    - [State Management](#state-management)
8. [REST APIs](#rest-apis)
    - [API Endpoints](#api-endpoints)
    - [Request and Response Handling](#request-and-response-handling)
    - [Error Handling](#error-handling)
9. [Alipay Integration](#alipay-integration)
    - [Connecting Alipay Account](#connecting-alipay-account)
    - [Synchronizing Alipay Data](#synchronizing-alipay-data)
    - [Handling Payment Transactions](#handling-payment-transactions)
10. [Testing](#testing)
    - [Unit Testing](#unit-testing)
    - [Integration Testing](#integration-testing)
    - [Performance Testing](#performance-testing)
11. [Deployment](#deployment)
    - [Building the Application](#building-the-application)
    - [Deploying to Server](#deploying-to-server)
    - [Monitoring and Scaling](#monitoring-and-scaling)
12. [Troubleshooting](#troubleshooting)
13. [Further Improvements](#further-improvements)
14. [Useful Links](#useful-links)

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

## Setup and Installation

### Prerequisites
Ensure the following tools are installed:
- Java 17
- Maven
- PostgreSQL
- Git

### Cloning the Repository
```bash
git clone https://github.com/NatnaelTaddese/finpulse.git
cd finpulse
```

### Environment Configuration
Update **application.properties**:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finpulse
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### Running the Application
Start the application:
```bash
mvn spring-boot:run
```
Access via `http://localhost:8080`.

---

## Database Setup

### Database Configuration

FinPulse uses PostgreSQL as its database. Ensure PostgreSQL is installed and running on your system. Update the `application.properties` file with your database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finpulse
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```
## Entities Overview
The database schema is defined using JPA entities. Below are the key entities used in FinPulse:  
### User Entity
The User entity represents the users of the application.
```
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "alipay_token")
    private String alipayToken;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    private double income;
    private double dailySpendingLimit;

    @Column(name = "monthly_saving_goal", nullable = true)
    private double monthlySavingGoal;

    @Column(name = "current_balance", nullable = true)
    private double currentBalance;
    @Column(name = "current_savings", nullable = true)
    private double currentSavings;

    private String paySchedule;
    private String firstName;
    private String lastName;
    private String preferredCurrency;

    private boolean onboardingCompleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FinancialGoal> financialGoals;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SpendingCategory> spendingCategories;

    // Getters and setters
}

```

### FinancialGoal Entity
The FinancialGoal entity represents the financial goals set by users.
```
@Entity
@Table(name = "financial_goals")
public class FinancialGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double targetAmount;
    private LocalDate targetDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and setters
}
```

### SpendingCategory Entity

The SpendingCategory entity represents the categories of spending defined by users.

```
@Entity
@Table(name = "spending_categories")
public class SpendingCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double budgetAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and setters
}

```

### Expense Entity
The Expense entity represents the expenses logged by users.

```

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private SpendingCategory category;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String description;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Constructors, getters, and setters
}

```




This documentation aims to provide clarity and technical depth. Your feedback is highly valued!

