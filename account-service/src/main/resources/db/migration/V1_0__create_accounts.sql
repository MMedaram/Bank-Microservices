CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    customer_number VARCHAR(50) NOT NULL,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0,
    branch_code VARCHAR(50) NOT NULL,
    account_type VARCHAR(50) NOT NULL
);