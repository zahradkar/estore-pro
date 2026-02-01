# todo the following:

- testing ?
- review this document

# 🛒 E-Store Pro: Demo E-Commerce Backend

**E-Store Pro** is a RESTful API built with Spring Boot, designed to handle online retail. It features a layered
architecture and secure authentication integration.

---

## 🚀 Key Features

* **Product Management:** Full CRUD operations with category filtering and search
* **Secure Auth:** JWT-based authentication and role-based access control (RBAC).
* **Shopping Cart:** Persistent cart logic with real-time stock validation.
* **Order Processing:** Automated invoice generation and order status tracking.
* **Documentation:** Fully interactive API docs via Swagger/OpenAPI.

---

## 🛠️ Tech Stack

| Category       | Technology                  |
|----------------|-----------------------------|
| **Framework**  | Spring Boot 4.x             |
| **Language**   | Java 25                     |
| **Database**   | MySQL                       |
| **Security**   | Spring Security & JWT       |
| **ORM**        | Spring Data JPA (Hibernate) |
| **Build Tool** | Gradle                      |

---

## 📐 Architecture Overview

1. **Controller Layer:** Handles HTTP requests and maps them to DTOs.
2. **Service Layer:** Contains business logic and transaction management.
3. **Repository Layer:** Interacts with the database using JPA.

---

## 🏁 Getting Started

### Prerequisites

* JDK 17+ (project is set up for 25)
* Gradle 9.2+
* MySQL (or Docker for database)

### Installation

1. **Clone the repository**

    ```bash
    git clone https://github.com/zahradkar/estore-pro.git
    cd sotre
    ```

2. **Configure** <br>
    1. Rename `.env.example` to `.env` and set appropriate values to all fields. (for `MAIL_PASSWORD` property read next step)
    2. The application is configured for sending e-mail from Gmail account so, in order to work properly, is necessary in your gmail account to:
        - enable 2-factor authentication
        - set password for the application and then set it to `MAIL_PASSWORD` property in `.env` file. Password has the following
          pattern: `ygkh kdjv zbvl icma`
    3. Create MySQL database called `store` OR in `src/main/resources/application.yml` update url to your needs. Example:

    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/store?useUnicode=true&createDatabaseIfNotExist=true
    ```

3. **Run the application** <br>
   In application directory run:

    ```bash
    ./gradlew bootRun
    ```

---

## 📖 API Documentation

Once the app is running, you can explore the API endpoints visually:

* **Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **API Docs:** `http://localhost:8080/v3/api-docs`
