# 📚 BookStore Backend

## Overview

BookStore Backend is a secure and scalable RESTful backend application for an online bookstore, developed using **Java 21 and Spring Boot**.

The application provides complete backend functionality for managing users, administrators, books, shopping carts, wishlists, orders, customer details, and product feedback. The application also implements modern backend technologies such as **JWT Authentication, Google OAuth2, Redis, RabbitMQ, and Email Services**.

The project follows a **layered architecture** to keep the application modular, maintainable, secure, and easy to extend.

---

## 🎯 Application

The BookStore application is designed around two primary roles:

### 👤 User

Users can:

* Register and verify their account
* Login securely
* Login using Google OAuth2
* Use Remember Me functionality
* Recover forgotten passwords
* Reset passwords
* View their profile
* Edit their customer details
* Browse available books
* Search books by name or author
* Get a book by ID
* Manage their shopping cart
* Manage their wishlist
* Place orders
* Add and view product feedback

### 👨‍💼 Admin

Admins are responsible for managing the bookstore.

Admins can:

* Register and login
* Add books
* Update book information
* Delete books
* View all customer orders
* Update order status

Role-based authorization ensures that administrative operations are accessible only to authorized administrators.

---

## ✨ Key Features

### 🔐 Secure Authentication

The application uses **Spring Security and JWT** for secure, stateless authentication.

After successful login, the server generates a JWT token. The token is then used to authenticate requests to protected resources.

Passwords are securely encrypted using **BCrypt** before being stored in the database.

---

### 🔑 Google OAuth2 Login

The application supports authentication through **Google OAuth2**.

The authentication flow is:

User → Google Authentication → OAuth2 Success Handler → Application User → JWT Token

Google authentication allows users to access the bookstore without creating or entering a separate local password.

---

### 💾 Remember Me

The application supports Remember Me functionality using JWT expiration.

A normal login generates a token with a normal validity period, while enabling Remember Me generates a token with a longer validity period.

This allows users to remain authenticated for an extended period.

---

### 📧 Email Verification

During registration, users receive an email containing a verification token.

The verification process ensures that the registered email address belongs to the user.

Flow:

Registration → Verification Email → Verification Token → Account Verified

---

### 🔄 Forgot & Reset Password

The application provides a secure password recovery mechanism.

Flow:

Forgot Password → Reset Token → Redis → RabbitMQ → Email → Token Validation → New Password → Password Updated

Reset tokens are temporarily stored and validated before allowing the password to be changed.

---

### 👤 User Profile & Customer Details

Authenticated users can view and update their own profile information.

The application supports:

* View user profile
* Edit customer details
* Update first name
* Update last name
* Update email

Only authenticated users can access protected customer information.

---

### 📚 Book Management

Books are the primary products of the bookstore.

Administrators can manage books by:

* Adding new books
* Updating existing books
* Deleting books

Users can:

* Browse available books
* Search books
* Get a book by ID

---

### 🔎 Book Search

The application provides book search functionality.

Users can search books using a keyword based on:

* Book name
* Author name

This allows users to quickly find relevant books.

---

### 🛒 Shopping Cart

Users can manage books before placing an order.

The cart supports:

* Adding books
* Updating quantities
* Removing books
* Viewing cart items

---

### ❤️ Wishlist

Users can save books that they may want to purchase later.

The wishlist supports:

* Adding books
* Removing books
* Viewing saved books

---

### 📦 Order Management

Users can place orders for books selected through their shopping cart.

Order flow:

Select Books → Cart → Review Items → Place Order → Order Created

Administrators can:

* View customer orders
* View all orders
* Update order status

Current order statuses include:

* `PENDING`
* `CONFIRMED`
* `CANCELLED`

---

### ⭐ Product Feedback

Users can provide feedback for products.

The application supports:

* Adding feedback
* Viewing feedback

This provides product-related information that can help other users.

---

## ⚡ Redis Caching

Redis is integrated into the application to improve performance through caching.

Frequently requested data can be temporarily stored in Redis to reduce repeated database queries.

General flow:

Request → Check Redis → Data Available → Return Cached Data

If the data is not available:

Request → Database → Fetch Data → Store in Redis → Return Data

Redis is also used for temporarily storing password reset tokens.

---

## 📨 RabbitMQ Messaging

RabbitMQ is integrated as a message broker for asynchronous and event-driven processing.

The application uses RabbitMQ for events such as:

* Password reset
* Order creation

The general flow is:

Application Event → RabbitMQ → Message Queue → Consumer → Event Processing

Using messaging helps separate background operations from the main request flow and provides a foundation for scalable asynchronous processing.

---

## 📬 Email Service

The application uses **Spring Mail** for email communication.

Email services are used for functionality such as:

* Account verification
* Forgot password
* Password recovery

RabbitMQ can be used to process email-related events asynchronously.

---

## 🗄️ Database

The application uses **PostgreSQL** as its primary relational database.

**Spring Data JPA** is used for database interaction and object-relational mapping.

JPA helps manage:

* Entities
* Database persistence
* Repository operations
* Relationships
* Database queries

---

## 🛡️ Security

Security is implemented using **Spring Security**.

The application follows a stateless authentication architecture.

Client Request → JWT Filter → Token Validation → User Authentication → Role Validation → Protected Resource

Security features include:

* JWT Authentication
* Google OAuth2
* BCrypt Password Encryption
* Role-Based Authorization
* Stateless Session Management
* Protected Admin Operations
* Email Verification
* Secure Password Recovery
* Remember Me functionality

---

## 🏗️ Architecture

The application follows a layered architecture:

Client
↓
Controller Layer
↓
Service Layer
↓
Repository Layer
↓
PostgreSQL Database

Supporting technologies such as Security, Redis, RabbitMQ, and Email Service are integrated according to their respective responsibilities.

### Controller Layer

Handles incoming HTTP requests and returns responses.

### Service Layer

Contains the application's business logic.

### Repository Layer

Handles database operations using Spring Data JPA.

### Model Layer

Contains entities representing the application's domain objects.

### DTO Layer

Handles data transfer between the client and application layers without directly exposing database entities.

### Security Layer

Handles JWT authentication, authorization, and Google OAuth2 authentication.

### Messaging Layer

Handles RabbitMQ-based messaging and event processing.

### Exception Layer

Provides centralized exception handling and consistent error responses.

---

## 📁 Project Structure

BookStore Backend

├── config
├── controller
├── dto
├── enums
├── exception
├── mapper
├── messaging
│   └── rabbitmq
├── model
├── repository
├── security
├── service
└── util

The project structure separates responsibilities into dedicated packages, making the application easier to maintain, test, and extend.

---

## 🧩 Technology Stack

### Backend

* Java 21
* Spring Boot
* Spring MVC
* Spring Security
* Spring Data JPA

### Authentication & Security

* JWT
* Google OAuth2
* BCrypt
* Role-Based Authorization

### Database

* PostgreSQL

### Caching

* Redis

### Messaging

* RabbitMQ

### Email

* Spring Mail

### API Documentation & Testing

* Swagger / OpenAPI
* Postman

### Development Tools

* Maven
* IntelliJ IDEA
* Git
* GitHub

---

## 🔄 Overall Application Flow

The overall BookStore backend works through the following flow:

User / Admin
↓
Authentication
↓
JWT / Google OAuth2
↓
Spring Security
↓
Controller
↓
Service
↓
Repository
↓
PostgreSQL

Additional infrastructure:

Application → Redis → Caching / Temporary Data

Application → RabbitMQ → Asynchronous Events

Application → Spring Mail → Email Communication

---

## 🌟 Project Highlights

* Secure JWT-based authentication
* Google OAuth2 authentication
* Role-based Admin and User authorization
* Email verification
* Forgot Password
* Reset Password
* Remember Me functionality
* BCrypt password encryption
* User profile management
* Customer details management
* Book management
* Book search
* Get Book By ID
* Shopping cart management
* Wishlist management
* Order creation
* Admin order management
* Admin order status update
* Product feedback
* PostgreSQL database integration
* Spring Data JPA
* Redis caching
* RabbitMQ messaging
* Email service
* Centralized exception handling
* Swagger/OpenAPI documentation
* Postman API testing
* Layered and maintainable architecture

---

## 🚀 Future Enhancements

The application can be further enhanced with:

* Pagination and sorting
* Advanced search and filtering
* Additional asynchronous events
* Unit and integration testing
* Frontend integration
* Production deployment
* Monitoring and logging
* Improved order processing
* Payment gateway integration

---

## 📌 Project Status

**Backend Development – In Progress**

The core backend functionality has been implemented, including authentication, authorization, Google OAuth2, Remember Me, email verification, password recovery, user profile management, customer details management, book management, search, cart, wishlist, orders, feedback, Redis caching, RabbitMQ messaging, and email services.

Additional admin functionality includes book management, viewing customer orders, and updating order status.

The project is being developed incrementally with a focus on security, scalability, maintainability, and clean backend architecture.

---

## 👩‍💻 Author

**Muskan Kapoor**

Java | Spring Boot | Backend Development
