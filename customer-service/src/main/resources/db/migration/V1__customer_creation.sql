CREATE TABLE IF NOT EXISTS customers (
    id BIGSERIAL PRIMARY KEY,
    customer_number VARCHAR(50) UNIQUE NOT NULL,  -- This will hold branchCode-prefixed customer number
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    branch_code  VARCHAR(50) NOT NULL
);