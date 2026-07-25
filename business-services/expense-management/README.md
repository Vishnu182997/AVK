# Expense and Budget Management

This Java 8 Spring module exposes owner-scoped expense operations. The hosting authentication layer must populate request attribute `expenseActor` with an `ExpenseActor`; request models intentionally contain no user id.

## API

- `POST/GET /api/expense-transactions`, `GET/PUT/DELETE /api/expense-transactions/{id}`. List filters: `from`, `to`, `category`, and `type`.
- `POST/GET /api/expense-budgets`, `GET/PUT/DELETE /api/expense-budgets/{id}`. Listing requires `month` and `year`.
- `GET /api/expense-reports/monthly?month=7&year=2026` returns totals, category spending, and budget comparisons.

Amounts and limits must be positive, required dates/categories/types must be supplied, months are 1-12, and years are 1900-9999. One budget per owner/category/month/year is allowed. `expense.budget.warning-threshold` is a percentage and defaults to `80`; spending at 100% is `EXCEEDED`.

Schema objects are in `src/main/resources/db/migration/V1__create_expense_management_tables.sql`. Apply the forward-only script with the deployment's migration process. Run tests from the repository root with `mvn -pl business-services/expense-management -am test`.
