# Smart Travel Platform 🏖️✈️

A comprehensive **microservices-based travel booking system** built with Spring Boot that enables users to seamlessly book flights and hotels for their travel needs.

## 🏗️ Architecture

This project follows a **microservices architecture** with 6 independent services:

- **User Service** (Port 8082): User management and authentication
- **Flight Service** (Port 8083): Flight search and availability
- **Hotel Service** (Port 8084): Hotel search and booking
- **Booking Service** (Port 8087): Main orchestration service for bookings
- **Payment Service** (Port 8085): Payment processing
- **Notification Service** (Port 8086): Email notifications

### Service Communication
- **WebClient**: Reactive HTTP client for async operations (user validation, payments, notifications)
- **Feign Client**: Declarative HTTP client for synchronous service calls (flight/hotel availability)

## 🛠️ Technologies Used

- **Backend**: Spring Boot 3.5.8, Spring Cloud 2024.0.0
- **Database**: MySQL 8.0 with Hibernate ORM
- **Build Tool**: Maven (multi-module project)
- **Language**: Java 17
- **HTTP Clients**: WebClient (reactive), OpenFeign (declarative)
- **Testing**: JUnit 5, Spring Boot Test
- **Other**: Lombok, Spring Validation

## 🚀 Key Features

- ✅ **User Management**: User registration and profile management
- ✅ **Flight Booking**: Search flights with real-time availability checking
- ✅ **Hotel Booking**: Find and reserve hotels with availability validation
- ✅ **Integrated Booking**: Combine flights and hotels in single transactions
- ✅ **Payment Processing**: Secure payment handling with status tracking
- ✅ **Notification System**: Automated email notifications for booking updates
- ✅ **RESTful APIs**: Comprehensive API endpoints for all services
- ✅ **Microservices Communication**: Inter-service calls using WebClient and Feign

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0**
- **Git**

## 🏃‍♂️ Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/smart-travel-platform.git
cd smart-travel-platform
```

### 2. Database Setup
Create MySQL databases for each service:
```sql
CREATE DATABASE travel_user_db;
CREATE DATABASE travel_flight_db;
CREATE DATABASE travel_hotel_db;
CREATE DATABASE travel_booking_db;
CREATE DATABASE travel_payment_db;
CREATE DATABASE travel_notification_db;
```

Update database credentials in each service's `application.properties` if needed.

### 3. Build the Project
```bash
mvn clean install
```

### 4. Start Services
Start each service in separate terminals:

```bash
# Terminal 1 - User Service
cd user-service
mvn spring-boot:run

# Terminal 2 - Flight Service
cd flight-service
mvn spring-boot:run

# Terminal 3 - Hotel Service
cd hotel-service
mvn spring-boot:run

# Terminal 4 - Payment Service
cd payment-service
mvn spring-boot:run

# Terminal 5 - Notification Service
cd notification-service
mvn spring-boot:run

# Terminal 6 - Booking Service (main service)
cd booking-service
mvn spring-boot:run
```

## 🎯 Usage Examples

### Create a Booking
```bash
POST http://localhost:8087/api/bookings
Content-Type: application/json

{
  "userId": 1,
  "flightId": 1,
  "hotelId": 1,
  "travelDate": "2025-02-10"
}
```

### Get All Flights
```bash
GET http://localhost:8083/api/flights
```

### Check Flight Availability
```bash
GET http://localhost:8083/api/flights/1/availability
```

### Get User Details
```bash
GET http://localhost:8082/api/users/1
```

## 📚 API Endpoints

### User Service (Port 8082)
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Flight Service (Port 8083)
- `GET /api/flights` - Get all flights
- `GET /api/flights/{id}` - Get flight by ID
- `GET /api/flights/{id}/availability` - Check flight availability
- `POST /api/flights` - Create new flight

### Hotel Service (Port 8084)
- `GET /api/hotels` - Get all hotels
- `GET /api/hotels/{id}` - Get hotel by ID
- `GET /api/hotels/{id}/availability` - Check hotel availability
- `POST /api/hotels` - Create new hotel

### Booking Service (Port 8087)
- `GET /api/bookings` - Get all bookings
- `GET /api/bookings/{id}` - Get booking by ID
- `GET /api/bookings/user/{userId}` - Get bookings by user
- `POST /api/bookings` - Create new booking
- `PUT /api/bookings/{id}` - **Update booking** (hotel, travel date, total cost)
- `PUT /api/bookings/{id}/confirm` - Confirm booking
- `PUT /api/bookings/{id}/cancel` - Cancel booking
- `DELETE /api/bookings/{id}` - Delete booking

### Payment Service (Port 8085)
- `GET /api/payments` - Get all payments
- `GET /api/payments/{id}` - Get payment by ID
- `GET /api/payments/booking/{bookingId}` - Get payments by booking ID
- `GET /api/payments/transaction/{transactionId}` - Get payment by transaction ID
- `POST /api/payments` - Process payment
- `PUT /api/payments/{id}` - **Update payment** (amount, payment method)
- `PUT /api/payments/{id}/refund` - **Refund payment**
- `DELETE /api/payments/{id}` - **Delete payment**

### Notification Service (Port 8086)
- `POST /api/notifications/send` - Send notification
- `POST /api/notifications/booking-confirmation` - Send booking confirmation
- `POST /api/notifications/payment-confirmation` - Send payment confirmation

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Test with Postman
Import the following collection and test the APIs:

1. **Create User** → **Create Flight** → **Create Hotel**
2. **Create Booking** (tests all inter-service communication)
3. **Update Booking** (modify hotel, travel date, or total cost)
4. **Confirm/Cancel Booking** (change booking status)
5. **Process Payment** (handle payment transactions)
6. **Update/Refund Payment** (modify or refund payments)
7. **Delete Booking/Payment** (remove records from database)
8. **Verify Status Changes** automatically with booking and payment operations

### Sample Test Data
- **Users**: Harshana, Kasun, Saman
- **Flights**: Colombo-Dubai, Singapore-Bangkok, Mumbai-Delhi, Tokyo-Seoul
- **Hotels**: Cinnamon Grand (Colombo), Marriott (Bangkok), Taj Mahal (Delhi), Shinchon (Seoul)

## 🔧 Configuration

Each service has its own `application.properties`:

- **Database**: MySQL connection settings
- **Server Port**: Unique port for each service
- **Service URLs**: Inter-service communication endpoints
- **JPA**: Hibernate settings for database operations

## 📝 Business Flow

1. **User Registration**: Users create accounts via User Service
2. **Search & Select**: Users browse flights and hotels
3. **Booking Creation**: Booking Service validates availability via Feign clients
4. **Booking Management**: Update bookings (hotel, date, cost) before confirmation
5. **Payment Processing**: Payment Service handles transactions asynchronously
6. **Payment Management**: Update, refund, or delete payments as needed
7. **Confirmation**: Booking status updates and notifications are sent
8. **Completion**: User receives booking confirmation email




