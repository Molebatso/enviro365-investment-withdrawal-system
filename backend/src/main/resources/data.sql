-- ==============================================================
-- Seed / demo data for Enviro365 Investment Withdrawal System
-- Reloaded fresh on every application startup (see application.properties:
-- ddl-auto=create-drop + spring.sql.init.mode=always).
--
-- Three investors, deliberately sized so every scenario in the
-- assessment brief can be demonstrated immediately:
--
--   Investor 1 (Thabo Nkosi, age 42, < 65): balance R150,000.00
--     - Successful STANDARD withdrawal:      R50,000  (<= 90% of 150k = 135k) -> APPROVED
--     - Retirement withdrawal rejected:       any amount, RETIREMENT type      -> REJECTED (age <= 65)
--
--   Investor 2 (Susan van der Merwe, age 71, > 65): balance R100,000.00
--     - Retirement withdrawal allowed:        R50,000  (<= 90% of 100k = 90k)  -> APPROVED
--
--   Investor 3 (Ayesha Patel, age 55, < 65): balance R250,000.00
--     - Withdrawal exceeding balance:         R300,000 (> 250k)                -> REJECTED
--     - Withdrawal exceeding 90% cap:         R230,000 (> 225k, but <= 250k)   -> REJECTED
--
-- Two withdrawal_notice rows are pre-seeded so the History table and
-- CSV export have data to display/filter on first load, before the
-- evaluator submits anything new.
-- ==============================================================

-- --------------------------------------------------------------
-- Investors
-- --------------------------------------------------------------
INSERT INTO investor (id, first_name, last_name, age, email) VALUES
  (1, 'Thabo', 'Nkosi', 42, 'thabo.nkosi@example.com'),
  (2, 'Susan', 'van der Merwe', 71, 'susan.vandermerwe@example.com'),
  (3, 'Ayesha', 'Patel', 55, 'ayesha.patel@example.com');

-- --------------------------------------------------------------
-- Portfolios (one per investor)
-- --------------------------------------------------------------
INSERT INTO portfolio (id, investor_id, available_balance) VALUES
  (1, 1, 150000.00),
  (2, 2, 100000.00),
  (3, 3, 250000.00);

-- --------------------------------------------------------------
-- Investment products
-- --------------------------------------------------------------
INSERT INTO investment_product (id, portfolio_id, product_name, current_value) VALUES
  (1, 1, 'Money Market Fund', 90000.00),
  (2, 1, 'Balanced Unit Trust', 60000.00),
  (3, 2, 'Retirement Annuity', 70000.00),
  (4, 2, 'Preservation Fund', 30000.00),
  (5, 3, 'Equity Growth Fund', 150000.00),
  (6, 3, 'Bond Fund', 100000.00);

-- --------------------------------------------------------------
-- Withdrawal notice history (pre-existing, APPROVED)
-- --------------------------------------------------------------
INSERT INTO withdrawal_notice (id, investor_id, withdrawal_type, amount, status, requested_date, processed_date) VALUES
  (1, 1, 'STANDARD', 20000.00, 'APPROVED', '2026-08-01 09:15:00', '2026-08-01 09:15:05'),
  (2, 2, 'RETIREMENT', 15000.00, 'APPROVED', '2026-08-15 11:30:00', '2026-08-15 11:30:04');

ALTER TABLE withdrawal_notice ALTER COLUMN id RESTART WITH 3;
