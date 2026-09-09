# Enviro365 Investment Withdrawal System

A full-stack system that lets Enviro365 investors view their portfolios,
submit withdrawal notices, and export withdrawal statements — built as a
Junior Software Developer technical assessment.

## Table of Contents

1. [Project Overview](#project-overview)
2. [Business Problem](#business-problem)
3. [Features](#features)
4. [Technology Stack](#technology-stack)
5. [Architecture](#architecture)
6. [Project Structure](#project-structure)
7. [Database / Domain Model](#database--domain-model)
8. [Business Rules](#business-rules)
9. [Running the Backend](#running-the-backend)
10. [Running the Frontend](#running-the-frontend)
11. [H2 Configuration](#h2-configuration)
12. [API Documentation](#api-documentation)
13. [Testing](#testing)
14. [CSV Export](#csv-export)
15. [Screenshots](#screenshots)
16. [AI Usage](#ai-usage)
17. [Assumptions](#assumptions)

---

## Project Overview

Enviro365 Investments is automating its withdrawal notice process to
eliminate manual errors, improve efficiency, and provide a better investor
experience. This system replaces manual withdrawal handling with a
REST API (Spring Boot) and a web UI (React) that together enforce all
withdrawal business rules consistently, every time.

## Business Problem

Investors need a self-service way to:
- View their portfolio and what it's invested in
- Submit a withdrawal request and know immediately whether it's allowed
- See their past withdrawals
- Export their withdrawal statement for their own records

Previously handled manually, this is error-prone and slow. This system
enforces the rules automatically and gives investors instant, clear
feedback.

## Features

**Backend**
- Retrieve investor and portfolio details (with investment products)
- Submit withdrawal notices with full business-rule validation
- Withdrawal history per investor
- CSV statement export with optional filtering

**Frontend**
- Portfolio Dashboard
- Withdrawal Form with live balance/limit display and client-side validation
- Withdrawal History table with status filtering
- CSV download button

**Cross-cutting**
- Global exception handling — no raw stack traces ever reach the client
- DTO layer — entities are never exposed directly over the API
- Jakarta Bean Validation on all inputs
- Unit tests covering every business rule and its boundary cases

## Technology Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.3, Spring Web, Spring Data JPA, Jakarta Validation |
| API Docs | springdoc-openapi (Swagger UI) |
| Database | H2 (in-memory) |
| Testing | JUnit 5, Mockito, AssertJ |
| Frontend | React 18, Vite, Axios |
| Build tools | Maven (backend), npm (frontend) |

## Architecture

```
React Frontend (Axios)
        |  HTTP/JSON
        v
Spring Boot REST Controllers  (thin - request/response only)
        |
        v
Service Layer               (business rules, balance calc, orchestration)
        |
        v
Repository Layer (Spring Data JPA)
        |
        v
H2 Database (in-memory, reseeded on every restart)
```

Controllers only translate HTTP <-> DTOs. All business logic - balance
calculations, the three withdrawal rules, deciding approve/reject -
lives in the service layer, which is what the unit tests exercise
directly (no web server or database needed to run them).

## Project Structure

```
enviro365-investment-withdrawal-system/
|-- backend/
|   `-- src/main/java/com/enviro/assessment/junior/candidate/
|       |-- controller/    REST endpoints (thin)
|       |-- service/       Business rules and orchestration
|       |-- repository/    Spring Data JPA interfaces
|       |-- entity/        JPA entities + enums
|       |-- dto/
|       |   |-- request/   Inbound request bodies (validated)
|       |   `-- response/  Outbound response shapes
|       |-- exception/     Custom exceptions + @ControllerAdvice
|       |-- validation/    (reserved for custom validators)
|       `-- config/        CORS configuration
|   `-- src/test/java/...  Service-layer unit tests
|-- frontend/
|   `-- src/
|       |-- api/                Axios calls, separated from UI
|       |-- components/
|       |   |-- Dashboard/
|       |   |-- WithdrawalForm/
|       |   |-- WithdrawalHistory/
|       |   |-- InvestorPicker.jsx
|       |   `-- common/         Shared loading/error/empty states
|       `-- App.jsx             Tab navigation + page wiring
`-- README.md
```

## Database / Domain Model

```
Investor (1) --- (1) Portfolio (1) --- (*) InvestmentProduct
   |
   `---(*) WithdrawalNotice
```

| Entity | Key Fields |
|---|---|
| `Investor` | id, firstName, lastName, age, email |
| `Portfolio` | id, investor (FK), availableBalance (BigDecimal) |
| `InvestmentProduct` | id, portfolio (FK), productName, currentValue (BigDecimal) |
| `WithdrawalNotice` | id, investor (FK), withdrawalType, amount (BigDecimal), status, requestedDate, processedDate |

All monetary fields use `BigDecimal` - never `double`/`float` - to avoid
floating-point rounding errors with money.

## Business Rules

| # | Rule | Enforced in |
|---|---|---|
| 1 | Retirement withdrawals require investor age **strictly greater than 65** | `WithdrawalService.validateRetirementRule` |
| 2 | Withdrawal amount must not exceed the available balance | `WithdrawalService.validateBalanceRule` |
| 3 | Withdrawal amount must not exceed **90%** of the available balance (checked independently of Rule 2, not as a substitute) | `WithdrawalService.validateNinetyPercentCapRule` |
| 4 | Amount must be positive (defense in depth - also enforced by `@Positive` at the API boundary) | `WithdrawalService.validatePositiveAmount` |

Invalid withdrawals are **never persisted**. All four checks run before
anything is saved; the entire operation is wrapped in `@Transactional`.

## Running the Backend

**Prerequisites:** Java 17+, Maven 3.8+

```bash
cd backend
mvn spring-boot:run
```

- API base URL: `http://localhost:8080/api`
- H2 console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:enviro365`
  - Username: `sa` - Password: *(blank)*

## Running the Frontend

**Prerequisites:** Node.js 18+

```bash
cd frontend
npm install
npm run dev
```

- App URL: `http://localhost:5173`
- Already configured to call the backend at `http://localhost:8080/api`
  (see `frontend/src/api/httpClient.js`)

Run the backend first - the frontend has nothing to show until it can
reach the API.

## H2 Configuration

The database is **in-memory** and recreated fresh on every backend
restart (`spring.jpa.hibernate.ddl-auto=create-drop`), with seed data
loaded automatically from `backend/src/main/resources/data.sql`.

This was a deliberate choice over a persistent file-based database: an
evaluator restarting the app repeatedly gets the same predictable demo
data every time, rather than accumulating withdrawal history or hitting
duplicate-seed-data errors across runs.

Seed data includes 3 investors sized specifically so every rule can be
demonstrated immediately - see comments at the top of `data.sql` for
exact figures and which rule each one triggers.

## API Documentation

Base URL: `http://localhost:8080/api`

**Interactive docs:** with the backend running, visit
`http://localhost:8080/swagger-ui.html` to browse and call every
endpoint directly from the browser (powered by springdoc-openapi).
The reference below covers the same endpoints with concrete
request/response/error examples.

### `GET /api/investors`
Returns all investors (used by the frontend's investor picker).

**Success (200):**
```json
[
  { "id": 1, "firstName": "Thabo", "lastName": "Nkosi", "age": 42, "email": "thabo.nkosi@example.com" }
]
```

### `GET /api/investors/{id}`
Returns a single investor's details.

**Success (200):**
```json
{ "id": 1, "firstName": "Thabo", "lastName": "Nkosi", "age": 42, "email": "thabo.nkosi@example.com" }
```

**Error (404):**
```json
{ "timestamp": "2026-09-08T10:15:00", "status": 404, "error": "Not Found",
  "message": "Investor not found with id: 999", "path": "/api/investors/999" }
```

### `GET /api/investors/{id}/portfolio`
Returns the investor's portfolio and investment products.

**Success (200):**
```json
{
  "id": 1,
  "availableBalance": 150000.00,
  "investmentProducts": [
    { "id": 1, "productName": "Money Market Fund", "currentValue": 90000.00 },
    { "id": 2, "productName": "Balanced Unit Trust", "currentValue": 60000.00 }
  ]
}
```

### `POST /api/investors/{id}/withdrawals`
Submits a withdrawal request.

**Request body:**
```json
{ "withdrawalType": "STANDARD", "amount": 50000.00 }
```

**Success (201):**
```json
{
  "id": 3, "withdrawalType": "STANDARD", "amount": 50000.00, "status": "APPROVED",
  "requestedDate": "2026-09-08T10:20:00", "processedDate": "2026-09-08T10:20:00",
  "remainingBalance": 100000.00
}
```

**Error - business rule violation (400):**
```json
{ "timestamp": "2026-09-08T10:21:00", "status": 400, "error": "Bad Request",
  "message": "Withdrawal amount exceeds 90% of the available balance.",
  "path": "/api/investors/1/withdrawals" }
```

**Error - input validation (400):**
```json
{ "timestamp": "2026-09-08T10:22:00", "status": 400, "error": "Bad Request",
  "message": "Withdrawal amount must be positive",
  "path": "/api/investors/1/withdrawals" }
```

### `GET /api/investors/{id}/withdrawals`
Returns the investor's withdrawal history, most recent first.

### `GET /api/withdrawals/export`
Downloads a CSV withdrawal statement. Query parameters (both optional,
independent, combinable):

| Param | Example | Effect |
|---|---|---|
| `investorId` | `?investorId=1` | Only this investor's withdrawals |
| `status` | `?status=APPROVED` | Only withdrawals with this status |

Returns `Content-Type: text/csv` with a `Content-Disposition: attachment`
header, columns: `Withdrawal ID, Investor ID, Investor Name, Withdrawal Type, Amount, Date, Status`.

## Testing

**Backend unit tests:**
```bash
cd backend
mvn test
```

`WithdrawalServiceTest` covers, at minimum:
- Successful withdrawal (and correct balance reduction)
- Retirement withdrawal at exactly age 65 (boundary - must reject)
- Retirement withdrawal under 65 (reject) and over 65 (approve)
- Withdrawal exceeding balance (reject)
- Withdrawal exceeding 90% (reject) vs. exactly 90% (approve - boundary)
- Negative and zero withdrawal amounts (reject)
- Investor not found

All repositories are mocked with Mockito - no database or Spring
context is needed to run these tests.

**Manual end-to-end testing:** with both servers running, walk through
the scenarios described in the seed-data comments (`data.sql`) using
the actual UI - this exercises the full stack, not just the service layer.

## CSV Export

Click **Download CSV** on the History page. The button reflects
whatever status filter is currently selected, so what's shown in the
table is what gets exported. The backend endpoint can also be called
directly for testing:

```bash
curl "http://localhost:8080/api/withdrawals/export?investorId=1" -o statement.csv
```

## Screenshots

*(To be added once the application is running locally: Portfolio
Dashboard, Withdrawal Form, successful withdrawal, a validation/error
message, Withdrawal History, and the CSV download. Screenshots should
be captured from the actual running app, not created separately.)*

## AI Usage

Claude (Anthropic) was used throughout this project's development for:
- Scaffolding the Spring Boot and React project structures
- Generating entity, repository, service, controller, and DTO classes
  from the assessment's functional and business requirements
- Generating the unit test suite for the withdrawal business rules
- Drafting this documentation

All AI-generated code was reviewed, and is understood well enough to be
explained and defended in a follow-up technical interview, including
the reasoning behind specific design decisions (see the code comments
throughout, and the [Assumptions](#assumptions) section below).

## Assumptions

Documented here since the assessment brief explicitly asks that
unstated requirements be resolved with a clearly labelled assumption
rather than guessed silently:

- **Withdrawal types:** modeled as `RETIREMENT` and `STANDARD`. Only
  `RETIREMENT` withdrawals are subject to the age > 65 rule.
- **Available balance:** tracked as a single running `Portfolio.availableBalance`
  field, decremented on each approved withdrawal - rather than summing
  investment product values minus historical withdrawals on every
  request. Simpler to reason about and test, at the cost of that field
  being the single source of truth that must be kept in sync.
- **Withdrawal status:** modeled as `APPROVED`/`REJECTED`, but since
  rejected withdrawals are never persisted (per the brief), only
  `APPROVED` rows currently exist in the database. `REJECTED` is kept
  in the enum for a self-documenting model and to allow persisting
  rejected attempts later without a schema change.
- **No authentication:** deliberately out of scope per the brief. The
  frontend's investor picker stands in for "who is logged in".
- **In-memory H2, reseeded every restart:** chosen over a persistent
  file so a repeatedly-restarted demo always shows the same
  predictable data (see [H2 Configuration](#h2-configuration)).
