# Shop 

Shop is a user management microservice built with Java 17 and Spring Boot. The service provides secure user authentication, profile management, and email-based verification with JWT token-based security.

## Key Features

### User Management System
- **Secure Registration & Authentication**: JWT-based authentication with email verification
- **Profile Management**: Complete user profile system with customizable information
- **Password Security**: Advanced password validation with reset functionality via email
- **Account Verification**: Email-based account activation and verification
- **Token Blacklisting**: Secure logout with JWT token invalidation using Redis

### Advanced Features
- **Email Notifications**: Account verification and password reset emails
- **Event-Driven Architecture**: Apache Kafka integration for scalable messaging
- **Caching**: Redis-based session management and token blacklisting
- **Real-time Processing**: Kafka consumers for order processing events

## Tech Stack

### Backend Framework
- **Java 17**: Modern Java features and performance improvements
- **Spring Boot 3.3.4**: Latest Spring Boot with enhanced features
- **Spring Security**: Comprehensive security framework
- **Spring Data JPA**: Database abstraction layer
- **Hibernate**: ORM framework for database operations

### Messaging & Communication
- **Apache Kafka**: High-performance message streaming and event processing
- **Spring Mail**: Email service integration

### Database Systems
- **PostgreSQL**: Primary relational database for user data (production)
- **H2**: In-memory database for development and testing
- **Redis**: In-memory caching and token blacklist management

### Security & Authentication
- **JWT (JSON Web Tokens)**: Stateless authentication
- **Spring Security**: Security configuration and filters
- **Passay**: Password strength validation library
- **Email Verification**: Secure account activation process

### Development Tools
- **Lombok**: Reduce boilerplate code
- **Maven**: Build automation and dependency management
- **SpringDoc OpenAPI**: API documentation and Swagger UI
- **Docker**: Containerization for development and deployment

## Installation and Running

### Prerequisites
- Java 17 or higher
- Maven 3.8+
- Docker and Docker Compose
- PostgreSQL database
- Redis server
- Apache Kafka

### Environment Setup

1. **Start the required services using Docker Compose:**
   ```bash
   docker-compose up -d
   ```
   This will start:
   - PostgreSQL database (port 5433)
   - Apache Kafka (port 9092)
   - Zookeeper (port 2181)
   - Shop Order Service (port 8081)

### Build and Run

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd shop
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

The application will start on port 8080 by default.

## API Endpoints

### User Registration & Authentication
- `POST /signup` - Register a new user account
- `GET /verify-email?token={token}` - Verify email address
- `POST /login` - User authentication
- `POST /reset-password?email={email}` - Request password reset
- `POST /reset-password-check?token={token}&newPassword={password}` - Reset user password

### User Profile Management
- `GET /account/details` - Get user profile information
- `PUT /account/{username}` - Update user profile
- `DELETE /account/delete` - Delete user account
- `POST /account/logout` - Secure logout with token blacklisting

### API Documentation
Access the interactive API documentation at:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

## Database Schema

### PostgreSQL Tables
- `shop_user`: User accounts and profile information
- `verification_token`: Email verification and password reset tokens

## Security Features

### Authentication
- JWT-based stateless authentication
- Token blacklisting for secure logout using Redis
- Password strength validation using Passay library
- Email verification for account activation

### Authorization
- Secure API endpoint protection
- User-specific resource access control
- Request authentication validation

### Data Protection
- Password hashing with BCrypt
- Secure token generation
- Input validation and sanitization
- SQL injection prevention

## Testing

### Run Tests
```bash
# Unit tests
mvn test

# Integration tests
mvn failsafe:integration-test
```

### Test Coverage
The project includes comprehensive testing:
- **Integration Tests**: Full application context testing with H2 database
- **Controller Tests**: REST API endpoint testing

## Development Profiles

### Available Profiles
- `dev` (default): Development environment with H2 database
- `test`: Testing environment with H2 in-memory database
- `prod`: Production environment with PostgreSQL

### Profile Configuration
Activate specific profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Microservices Architecture

This service is part of a larger e-commerce microservices ecosystem:
- **Shop Service** (this service): User management and authentication
- **Shop Order Service**: Order processing and management
- **Apache Kafka**: Inter-service communication and event streaming

## Error Handling

The application provides comprehensive error handling:
- **Global Exception Handler**: Centralized error processing
- **Custom Exceptions**: Domain-specific error types
- **Validation Errors**: Detailed field-level error messages
- **Security Errors**: Authentication and authorization failures

Example error response:
```json
{
  "status": "BAD_REQUEST",
  "message": "Email address is already registered"
}
```

## Monitoring and Logging

### Application Logging
- Structured logging for different environments
- Configurable logging levels per component
- Database query logging for development

### Health Monitoring
- Spring Boot Actuator integration ready
- Database connection health checks
- Kafka connectivity monitoring
- Redis cache availability checks

## Contributing

### Code Style
- Follow Java coding conventions
- Use Lombok for boilerplate reduction
- Implement comprehensive testing
- Document public APIs

### Pull Request Process
1. Fork the repository
2. Create feature branch
3. Implement changes with tests
4. Update documentation
5. Submit pull request

## Docker Support

The application includes Docker support for easy deployment:
- **Dockerfile**: Application containerization
- **docker-compose.yaml**: Complete development environment setup
- **Multiservice orchestration**: Kafka, PostgreSQL, and application services

Start the complete environment:
```bash
docker-compose up -d
```