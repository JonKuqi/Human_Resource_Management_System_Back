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


![image](https://github.com/user-attachments/assets/35a7e588-57fd-4954-af1a-58dc1e30abb4)

---

#### Permission-Based Role Architecture

To support fine-grained authorization within each tenant, the system implements role-permission logic based on four key tables:

##### Role-Based Tables (in each tenant schema):

- **`role`** – Defines roles like `OWNER`, `MANAGER`, `WORKER` for that tenant.
- **`user_role_table`** – Associates `user_tenant` records with one or more roles.
- **`role_permission`** – Maps roles to one or more global permissions, and optionally targets specific roles (e.g. “MANAGER can edit WORKER”).
- **`tenant_permission`** (in `public` schema) – Defines all available permissions globally (e.g., `POST /api/v1/tenant/user-tenant`).

These tables are used by the `AuthorizationFilter` middleware to determine whether a request should be allowed, based on:

- The authenticated user's roles (`user_role_table`)
- The permissions linked to those roles (`role_permission`)
- The HTTP verb and path (`tenant_permission`)
- Whether the permission is scoped to a target role (`target_role_id`)

This setup supports:
- Global permission definitions (shared across tenants)
- Per-tenant role-to-permission mappings
- Optional cross-role restrictions for controller logic (e.g. worker-to-worker updates denied)

![image](https://github.com/user-attachments/assets/3270fa5d-07ea-478e-9e44-3b93146a4234)

---

#### RESTful API Structure with BaseController

The backend is designed following RESTful principles, exposing resource-based endpoints using standard HTTP verbs (`GET`, `POST`, `PUT`, `DELETE`).

To reduce boilerplate and enforce consistency, the system includes a generic `BaseController` pattern for CRUD operations.

##### BaseController Pattern

- **`BaseController<T, ID>`** – A generic abstract controller providing default CRUD endpoints:
  - `GET /resource` – Retrieve all entities
  - `GET /resource/{id}` – Retrieve an entity by ID
  - `POST /resource` – Create a new entity
  - `PUT /resource/{id}` – Update an existing entity
  - `DELETE /resource/{id}` – Delete an entity

##### Extended Controllers

- Domain-specific controllers (e.g., `JobListingController`, `ContractController`) extend `BaseController` to inherit the CRUD endpoints without duplication.
- For tenant-secured models, a separate `BaseUserSpecificController` adds role-based filtering logic using `target_roles` extracted from the authorization layer.

##### Example Endpoint Inheritance

- `/api/v1/tenant/job-listing` → uses `BaseController` for standard CRUD
- `/api/v1/tenant/role-based/job-listing/role-filtered` → uses `BaseUserSpecificController` to enforce role-scoped logic

This structure improves maintainability, reduces repetition, and keeps controller logic focused on extensions and overrides when needed.








 
