-- Tabla de donaciones para PawPaw
CREATE TABLE donations (
    id_donation SERIAL PRIMARY KEY,
    id_user INTEGER REFERENCES users(id_user),
    donor_name VARCHAR(255),
    donor_email VARCHAR(255),
    amount DECIMAL(10,2) NOT NULL,
    message TEXT,
    is_anonymous BOOLEAN DEFAULT FALSE,
    payment_status VARCHAR(20) DEFAULT 'pending',
    paypal_order_id VARCHAR(255),
    paypal_capture_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
