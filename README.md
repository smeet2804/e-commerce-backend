# E-Commerce Backend Application

This repository contains the backend application for an e-commerce platform built using Java Spring Boot. The application is designed with a microservices architecture to ensure scalability, reliability, and maintainability.

## Table of Contents
- [Technologies Used](#technologies-used)
- [Services](#services)
  - [User Service](#1-user-service)
  - [Product Service](#2-product-service)
  - [Email Service](#3-email-service)
  - [Cart Service](#4-cart-service)
  - [Order Service](#5-order-service)
  - [Payment Service](#6-payment-service)
- [Setup Instructions](#setup-instructions)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Technologies Used

- 🌱 **Spring Boot**: Framework for building microservices.
- 📨 **Apache Kafka**: Asynchronous message processing.
- ⚙️ **Jenkins**: Continuous Integration and Continuous Deployment (CI/CD).
- ☁️ **AWS (Amazon Web Services)**: Deployment and cloud services.
- 🛢️ **PostgreSQL**: Relational database.
- 📦 **MongoDB**: NoSQL database.
- ⚡ **Redis**: Caching and real-time data processing.
- 🌐 **Spring Cloud**: Microservices communication and configuration.
- 🧪 **JUnit 5 and MockMvc**: Unit testing and integration testing.
- 💳 **Stripe**: Payment processing.
- 🔐 **Google OAuth 2.0**: Authorization and authentication.

## Services

### 1. User Service
- 🔐 **Authentication and Authorization**: Using Google OAuth 2.0 and OpenID.
- 👤 **User Management**: CRUD APIs for user management.

### 2. Product Service
- 🛒 **Product Management**: CRUD APIs for managing products.
- 🔍 **Product Browsing**: APIs for browsing products.

### 3. Email Service
- 📧 **User Notifications**: Sends emails to new users upon login and updates order status using Kafka.

### 4. Cart Service
- 🛍️ **Cart Management**: Add products to cart, remove products from cart, delete cart.

### 5. Order Service
- 📦 **Order Management**: Create, delete, and view orders; handle payments.

### 6. Payment Service
- 💳 **Payment Processing**: Generate payment links via Stripe.

## Contributing

Contributions are welcome! Please fork this repository and submit pull requests for any enhancements, bug fixes, or additional features.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
