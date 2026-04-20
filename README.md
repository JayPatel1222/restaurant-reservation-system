# Restaurant Reservation System

## 🚀 Portfolio Project: Full-Stack Java Microservices Application

A comprehensive restaurant reservation management system demonstrating advanced Java development skills, microservices architecture, and enterprise-level design patterns. This project showcases expertise in building scalable, maintainable applications using modern Java technologies and Clean Architecture principles.

## 📋 Project Overview

This full-stack application manages restaurant events, menus, table assignments, and customer reservations. Built as a term project at New brunswick community college as piece to demonstrate proficiency in Java ecosystem, microservices design, and domain-driven development. The system handles complex business workflows including reservation approval processes, email notifications, and secure authentication.

**Key Business Value**: Streamlines restaurant operations by automating reservation management, reducing manual coordination, and providing real-time availability tracking.

## 🛠️ Technologies & Skills Demonstrated

### Backend & Architecture
- **Java 21** - Latest LTS version with modern language features
- **Spring Boot 4.0.2** - Framework for rapid microservices development
- **Maven Multi-Module** - Advanced project structure management
- **Clean Architecture** - Domain-Driven Design implementation
- **OAuth2 Resource Server** - Secure authentication and authorization

### Development Practices
- **Test-Driven Development** - Comprehensive unit and integration testing
- **SOLID Principles** - Object-oriented design best practices
- **RESTful API Design** - Clean, versioned API endpoints
- **Validation Framework** - Custom business rule validation
- **Modular Architecture** - Separation of concerns across layers

### Infrastructure & Tools
- **Multi-Module Maven Setup** - Complex dependency management
- **Spring Validation** - Declarative validation with custom constraints
- **Email Integration** - Automated notification systems
- **Version Control** - Git workflow and branching strategies

## 🏗️ Architecture & Design Decisions

### Clean Architecture Implementation
The system follows Clean Architecture principles with strict layer separation:

- **Domain Layer**: Pure business logic with entities like `RestroEvent`, `DiningTable`, `ReservationRequest`
- **Application Layer**: Use case orchestration and business workflows
- **Infrastructure Layer**: External concerns (persistence, email, security)
- **Presentation Layer**: REST APIs and web interfaces

### Microservices Design
- **Independent Services**: Separate auth, restaurant, and email services
- **API Gateway Pattern**: Centralized request routing and authentication
- **Service Discovery**: Inter-service communication patterns
- **Database per Service**: Isolated data management

### Key Design Patterns
- **Repository Pattern**: Data access abstraction
- **Service Layer**: Business logic encapsulation
- **DTO Pattern**: Data transfer optimization
- **Factory Pattern**: Object creation management
- **Observer Pattern**: Event-driven notifications

## ✨ Key Features & Technical Highlights

### Event Management System
- CRUD operations for restaurant events with validation
- Date range queries and availability checking
- Menu association and pricing management

### Advanced Reservation Workflow
- Multi-step approval process (Pending → Approved/Denied)
- Capacity validation against table assignments
- Automated email notifications for status changes

### Table & Seating Optimization
- Dynamic table assignment to seating slots
- Capacity tracking and availability management
- Version-controlled updates for concurrency

### Security Implementation
- OAuth2 integration for secure API access
- Role-based access control
- Token validation across microservices

## 🚀 Getting Started

### Prerequisites
- Java 21 JDK
- Maven 3.6+
- Git

### Quick Setup
```bash
git clone <your-repo-url>
cd restaurant-reservation-system
mvn clean install
```

### Running Services
```bash
# Start Restaurant API
cd app-restaurant-rest-api && mvn spring-boot:run

# Start Auth API
cd app-auth-rest-api && mvn spring-boot:run

# Start Web App
cd app-web && mvn spring-boot:run
```

Access at: http://localhost:8080

## 🎯 What I Learned & Challenges Overcome

### Technical Achievements
- **Microservices Complexity**: Mastered inter-service communication and data consistency
- **Clean Architecture**: Implemented domain-driven design in a real-world application
- **OAuth2 Integration**: Built secure authentication flows across multiple services
- **Validation Frameworks**: Created custom validation rules for complex business logic

### Development Challenges
- **Multi-Module Dependencies**: Resolved complex Maven dependency management
- **Concurrent Updates**: Implemented optimistic locking for table assignments
- **Email Service Integration**: Built reliable notification system with error handling
- **API Design**: Created consistent, versioned REST APIs across services

### Best Practices Applied
- **Code Quality**: Maintained high standards with comprehensive testing
- **Documentation**: UML diagrams for system design and data flow
- **Version Control**: Proper branching and commit practices
- **Security**: Implemented industry-standard authentication patterns

## 👥 Contributor
Gandara Gallegos, Vianey — my classmate and collaborator on this project.