# Enviro365 Investment Withdrawal System

Junior Software Developer technical assessment — a full-stack system that lets
investors view their portfolios, submit withdrawal notices, and export
withdrawal statements.

> **Status:** Phase 2 complete (project scaffolding, configuration, folder
> structure). Business logic, entities, and real pages are added in later
> phases per the development roadmap. See "Roadmap" below.

## Project Structure

```
enviro365-investment-withdrawal-system/
├── backend/     Spring Boot REST API (Java 17, Maven, H2)
├── frontend/    React app (Vite, Axios)
└── README.md    You are here
```

## Prerequisites

- Java 17+
- Maven 3.8+ (or use the Maven wrapper once added)
- Node.js 18+ and npm

## Running the Backend

```bash
cd backend
mvn spring-boot:run
```

The API starts on **http://localhost:8080**.

- H2 console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/enviro365`
  - Username: `sa`
  - Password: *(leave blank)*
- The database file is created under `backend/data/` on first run.

## Running the Frontend

```bash
cd frontend
npm install
npm run dev
```

The app starts on **http://localhost:5173** and is already configured
(via `vite.config.js` and the backend's `CorsConfig.java`) to talk to the
backend at `http://localhost:8080/api`.

## Why Vite instead of Create React App?

Both satisfy the assessment's mandatory React/JS/HTML/CSS/Axios requirement.
Vite was chosen because it installs fewer dependencies, starts the dev server
almost instantly, and produces smaller, faster builds — all useful when a
project needs to be cloned and run quickly during grading. This is a build
tooling choice only; it does not change any application code, the component
model, or how the frontend talks to the API.

## Why no Lombok?

The backend deliberately avoids Lombok and writes explicit
getters/setters/constructors. This keeps every class fully readable without
needing an IDE plugin, and makes it easier to explain the entity code
line-by-line in a follow-up interview.

## Package Naming

Backend classes currently use the placeholder package
`com.enviro.assessment.junior.candidate`. This will be renamed to
`com.enviro.assessment.junior.<yourname>` in one pass once finalized.

## Roadmap

| Phase | Status |
|---|---|
| 1. Analysis | ✅ Done |
| 2. Backend project scaffold | ✅ Done |
| 3. Database/entities + seed data | ⬜ Next |
| 4. Portfolio APIs | ⬜ |
| 5. Withdrawal functionality + business rules | ⬜ |
| 6. Error handling | ⬜ |
| 7. Advanced features (DTOs, validation, tests) | ⬜ |
| 8. CSV export | ⬜ |
| 9. Frontend pages | ⬜ |
| 10. Frontend/backend integration | ⬜ |
| 11. Full testing | ⬜ |
| 12. Final rubric review | ⬜ |

## AI Usage

Claude (Anthropic) was used to help scaffold this project's structure and
will assist in generating and reviewing subsequent code. All AI-assisted
code is reviewed, tested, and understood before being treated as final —
a full disclosure section will be completed in this README once the
application is finished, listing exactly which parts were AI-assisted.
