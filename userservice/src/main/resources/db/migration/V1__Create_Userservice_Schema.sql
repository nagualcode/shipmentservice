CREATE SCHEMA IF NOT EXISTS userservice;

CREATE TABLE IF NOT EXISTS userservice.users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS userservice.packages (
    id SERIAL PRIMARY KEY,
    tracking_number VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(255),  -- Add the status column here
    user_id INTEGER REFERENCES userservice.users(id)
);
