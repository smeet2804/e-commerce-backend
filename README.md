# e-commerce backend application

## Technologies Used

- **Spring Boot**: For building microservices.
- **Apache Kafka**: For message processing.
- **Jenkins**: For CI/CD (Continuous Integration and Continuous Deployment).
- **AWS (Amazon Web Services)**: For deployment and cloud services.
- **PostgreSQL**: For relational database needs.
- **MongoDB**: For NoSQL database needs.
- **Redis**: For caching and real-time data processing.
- **Spring Cloud**: For microservices communication and configuration.
- **JUnit 5 and MockMvc**: For unit testing and integration testing.
- **Stripe**: For payment processing.
- **Google OAuth 2.0**: For authorization and authentication.

## Services

### 1. User Service
- **Authentication and Authorization**: Utilizing Google OAuth 2.0 and OpenID.
- **User Management**: CRUD APIs for user management.

### 2. Product Service
- **Product Management**: CRUD APIs for managing products.
- **Product Browsing**: APIs for browsing products.

### 3. Email Service
- **User Notifications**: Send emails to new users upon login and update order status using Kafka.