-- Define the ENUM type for transaction_type
CREATE TYPE transaction_type_enum AS ENUM ('CREDIT', 'DEBIT', 'TRANSFER');

-- Create the transactions table
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL,
    transaction_type transaction_type_enum NOT NULL,  -- Use the defined ENUM type
    amount DECIMAL(15,2) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE
);