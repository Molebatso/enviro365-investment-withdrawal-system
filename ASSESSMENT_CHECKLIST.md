# Final Assessment Review

Self-review against the rubric in the assessment brief. Nothing below is
marked complete unless it actually exists in the codebase and works.

| Rubric Component | Weight | Status | Where | How to Demonstrate |
|---|---|---|---|---|
| **Backend Functionality** | 20% | ✅ Done | `controller/`, `service/` | Run `mvn spring-boot:run`, call each endpoint (see API docs in README) |
| **Frontend Functionality** | 20% | ✅ Done | `frontend/src/components/` | Run `npm run dev`, walk through Dashboard → Withdrawal → History → CSV |
| **Code Quality** | 20% | ✅ Done | Whole codebase | Thin controllers, logic in services, DTOs at the boundary, no Lombok magic, constants named, no duplicate loading/error markup (shared `StatusStates`) |
| **Spring Boot & Integration** | 20% | ✅ Done | `config/CorsConfig.java`, `api/httpClient.js` | Frontend and backend run simultaneously; a withdrawal submitted in the UI is visible in the H2 console and the History page |
| **Advanced Features** | 15% | ✅ Done (all 5, brief only requires 3) | See below | See below |
| **Documentation** | 5% | ✅ Done | `README.md`, Swagger UI | Full setup, API docs, business rules, assumptions; interactive API docs at `/swagger-ui.html` |

## Advanced Features (3 required, 5 implemented)

| Feature | Implemented? | Where |
|---|---|---|
| Global exception handling | ✅ | `exception/GlobalExceptionHandler.java` |
| DTO layer | ✅ | `dto/request/`, `dto/response/` |
| Input validation | ✅ | `@NotNull`/`@Positive` on `WithdrawalRequest`, handled by `GlobalExceptionHandler` |
| Unit tests | ✅ | `WithdrawalServiceTest.java`, `InvestorServiceTest.java` |
| UI validation | ✅ | `WithdrawalForm.jsx` `validateForm()` |

## Business Rule Requirement Checklist

| Requirement | Implemented? | Where | Demonstrate |
|---|---|---|---|
| Retrieve investor portfolio | ✅ | `GET /api/investors/{id}/portfolio` | Dashboard page |
| Create withdrawal notices | ✅ | `POST /api/investors/{id}/withdrawals` | Withdrawal Form |
| Calculate withdrawal balance | ✅ | `WithdrawalService.createWithdrawal` | Remaining balance shown after submit |
| Validate withdrawal rules | ✅ | `WithdrawalService.validate*` methods | Submit an over-limit withdrawal |
| Withdrawal history | ✅ | `GET /api/investors/{id}/withdrawals` | History page |
| CSV export with filtering | ✅ | `GET /api/withdrawals/export` | Download CSV button, or `curl` |
| Retirement rule (age > 65) | ✅ | `validateRetirementRule` + `WithdrawalServiceTest` | Try investor 1 (age 42) with RETIREMENT type |
| Balance rule | ✅ | `validateBalanceRule` + test | Try investor 3 (balance 250k) withdrawing 300k |
| 90% cap rule (independent of balance rule) | ✅ | `validateNinetyPercentCapRule` + test | Try investor 3 withdrawing 230k (passes balance rule, fails 90% rule) |
| No raw stack traces exposed | ✅ | `GlobalExceptionHandler.handleUnexpectedError` | Generic message returned; full detail only in server logs |

## Known Gaps / Honest Limitations
- **Frontend has no automated tests** — only backend service-layer
  tests were required and implemented; the frontend relies on its UI
  validation plus manual end-to-end testing.
