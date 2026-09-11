# Smart Banking System

A secure and scalable **Smart Banking System** developed using **Java, Spring Boot, Spring Security, JWT, JPA/Hibernate, and MySQL**.

The system provides core banking operations such as customer management, account management, deposits, withdrawals, fund transfers, beneficiary management, transaction tracking, audit logging, and role-based access control.

##  Features

* Customer registration and login
* JWT-based authentication
* Password encryption using BCrypt
* Role-based authorization
* Customer management
* Bank account creation and management
* Savings and account-type support
* Deposit and withdrawal operations
* Secure money transfer between accounts
* Beneficiary management
* Transaction history
* Admin management
* Account status management
* Audit logging
* Global exception handling
* RESTful APIs
* MySQL database integration

##  User Roles

### Customer

* Register and login
* View account information
* Manage beneficiaries
* Transfer money
* View transaction history

### Bank Employee

* Access banking operations
* View customer/account information
* Support account-related operations

### Administrator

* Manage customers
* Manage accounts
* Update account status
* View audit logs
* Monitor banking operations

##  Technologies Used

| Technology      | Purpose                          |
| --------------- | -------------------------------- |
| Java            | Backend programming              |
| Spring Boot     | Application framework            |
| Spring Security | Authentication and authorization |
| JWT             | Token-based authentication       |
| Spring Data JPA | Database access                  |
| Hibernate       | ORM                              |
| MySQL           | Relational database              |
| Gradle          | Build and dependency management  |
| REST API        | Backend communication            |

##  Project Architecture

```text
Smart Banking System
│
├── Authentication
│   ├── Registration
│   ├── Login
│   └── JWT Authentication
│
├── Customer Management
│
├── Account Management
│   ├── Create Account
│   ├── Deposit
│   └── Withdraw
│
├── Beneficiary Management
│
├── Transaction Management
│   ├── Money Transfer
│   └── Transaction History
│
├── Administration
│   ├── Customer Management
│   ├── Account Management
│   └── Account Status
│
└── Audit Management
    └── Activity Logging
```

##  Package Structure

```text
com.smartbanking
│
├── account
├── admin
├── audit
├── auth
├── beneficiary
├── config
├── customer
├── employee
├── enums
├── exception
├── security
├── transaction
└── validation
```

##  Security

The application implements:

* JWT authentication
* BCrypt password hashing
* Role-based access control
* Protected REST endpoints
* Stateless session management
* Authentication filters
* Authorization checks
* Global exception handling

## 🗄️ Database

The project uses **MySQL** for persistent data storage.

Main entities include:

* Customers
* Accounts
* Transactions
* Beneficiaries
* Audit Logs

The database schema is managed through **JPA/Hibernate**.

## ▶️ How to Run

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/smart-banking-system.git
```

### 2. Open the project

Open the project using **IntelliJ IDEA** or another Java IDE.

### 3. Configure MySQL

Create the database:

```sql
CREATE DATABASE smart_banking_db;
```

Update the database credentials in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smart_banking_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 4. Run the application

Run:

```text
SmartBankingSystemApplication.java
```

The application runs on:

```text
http://localhost:8080
```

##  Default Administrator

For local development, the application initializes a default administrator account.

```text
Email: admin@smartbank.com
Password: Admin@123
```

**Change the default credentials before using the application in a production environment.**

##  API Modules

```text
/api/auth
/api/customers
/api/accounts
/api/transactions
/api/beneficiaries
/api/admin
/api/employees
/api/audit
```

The APIs can be tested using tools such as **Postman** or Swagger/OpenAPI when configured.

##  Future Enhancements

The project is designed to be extended with intelligent banking capabilities.

Planned enhancements include:

* AI-powered transaction analysis
* Expense prediction
* Spending pattern analysis
* Fraud detection
* Financial recommendations
* SQL-based analytics dashboards
* Personalized financial insights
* Intelligent transaction categorization

These AI and analytics features will be developed as future enhancements to the current banking backend.

##  Project Objective

The objective of this project is to develop a secure and modular banking backend that demonstrates real-world concepts including:

* Backend development
* REST API design
* Authentication and authorization
* Database management
* Object-relational mapping
* Secure financial transactions
* Role-based access control
* Auditability
* Scalable software architecture

##  Author

**Shree Saravanan B**

B.Tech Artificial Intelligence and Data Science

##  License

This project is licensed under the MIT License.
