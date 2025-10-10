-- Connect to the target database
-- Replace 'bank_db' with your database name
\c bank_db;

-- =========================
-- Branch Service Schema
-- =========================
-- Create user
CREATE USER branch WITH PASSWORD 'branch';
CREATE SCHEMA branch AUTHORIZATION branch;

-- =========================
-- Customer Service Schema
-- =========================
CREATE USER customer WITH PASSWORD 'customer';
CREATE SCHEMA customer AUTHORIZATION customer;

-- =========================
-- Account Service Schema
-- =========================
CREATE USER account WITH PASSWORD 'account';
CREATE SCHEMA account AUTHORIZATION account;

-- =========================
-- Grant privileges (optional but recommended)
GRANT ALL ON SCHEMA branch TO branch;
GRANT ALL ON SCHEMA customer TO customer;
GRANT ALL ON SCHEMA account TO account;

-- You can also grant usage to other roles if needed
-- GRANT USAGE ON SCHEMA branch TO some_other_role;
