# Booking System Backend

This is the backend API implementation for a class-based booking system supporting country-specific packages, waitlists, and real-time booking with credit-based access control. This project is built for mobile application consumption.

## 🚀 Tech Stack

- **Java 17**
- **Spring Boot 3.0.5**
- **MySQL 8**
- **Redis** – For concurrency-safe booking (prevent overbooking)
- **Spring Security** – Basic + Bearer Token Authentication
- **Lombok**, **JPA/Hibernate**, **AOP Logging**

## 📦 Modules

### 1. **User Module**
- Register, Login (Email verification mock supported)
- Profile, Change Password, Reset Password
- JWT Token-based security
- Role-based access control

### 2. **Package Module**
- See available packages by country
- Purchase packages
- Track remaining credits
- Expired packages are flagged accordingly

### 3. **Class Schedule / Booking Module**
- List available classes by country
- Book class using available credits
- Cancel booking and get refund (if >4 hours before class)
- Class capacity handling with Redis-based concurrency
- Waitlist support (FIFO auto-promotion when slots free)
- Credits auto-refunded to waitlist after class ends
- Overlapping class time prevention
- User check-in at class start time

## 🧪 Mocked Services

> These are mocked functions (not real implementations) to simulate email verification and payments:

```java
public boolean AddPaymentCard(...) { return true; }

public boolean PaymentCharge(...) { return true; }

public boolean SendVerifyEmail(...) { return true; }

```
🛠️ Run Locally
```angular2html
git clone https://github.com/SOETHIHAKYAW/booking-system-backend.git
cd booking-system-backend
```
```
    
📊 Swagger Documentation
Access Swagger UI at:
http://localhost:8080/swagger-ui/index.html
```
``
🧩 Database

Located under /schema/``

🔒 Authentication Flow

Before Login: Basic Auth for login/register 
After Login: Bearer Token in Authorization header 

📓 Bonus Features
AOP-based logging for key request/response activities

Concurrency-safe booking with Redis cache

Clean entity mapping and DTO layering

```angular2html
📩 Author
Soe Thiha Kyaw
GitHub: @SOETHIHAKYAW
```

