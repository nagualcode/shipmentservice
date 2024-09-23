CREATE SCHEMA IF NOT EXISTS trackingservice;

CREATE TABLE IF NOT EXISTS trackingservice.packages (
    id SERIAL PRIMARY KEY,
    tracking_number VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(255) NOT NULL
);
