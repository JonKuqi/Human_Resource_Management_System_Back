### Technologies Used

- **Spring Boot** – Main framework for building the backend REST API.
- **Spring Security** – Secures endpoints via JWT-based authentication and role-based authorization.
- **Spring Data JPA** – ORM layer for database access using repositories.
- **PostgreSQL** – Relational database with schema-level multitenancy.
- **JWT (JSON Web Tokens)** – Stateless token-based authentication.
- **Flyway** – SQL-based DB migration tool for setting up tenant schemas.

---

### Architecture Summary

#### Multitenancy Strategy

- **Schema-per-tenant**: Each tenant has its own schema, dynamically resolved at runtime.
- `public` schema holds shared entities (like `user`, `tenant`, `tenant_permission`).
- `tenant_*` schemas hold isolated tenant data (e.g., `user_tenant`, `role`, `contract`, etc.).


![image](https://github.com/user-attachments/assets/45556dc0-90ff-405a-a62a-58749ab167c6)




#### Layered Architecture

- **Model** – Entities mapped to tables using JPA annotations.
- **Repository** – Interfaces extend Spring Data JPA, with custom queries for tenant-aware access.
- **Service** – Business logic layer, often transactional and reusable.
- **Controller** – REST endpoints exposed via `@RestController`, returning DTOs.
- **Middleware** – Custom filters for:
  - `SchemaRoutingFilter` – sets schema based on tenant
  - `JwtAuthenticationFilter` – handles token validation and user authentication
  - `AuthorizationFilter` – applies role-permission checks for RBAC

![Uploading ChatGPT Image May 24, 2025, 01_25_45 AM.png…]()


 
