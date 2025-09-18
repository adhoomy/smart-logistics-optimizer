-- Smart Logistics Optimizer Database Schema
-- PostgreSQL 12+

-- Create database (run this manually if needed)
-- CREATE DATABASE smart_logistics;

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    destination_address VARCHAR(500) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    delivery_date TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' 
        CHECK (status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS', 'DELIVERED', 'CANCELLED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Deliveries table
CREATE TABLE IF NOT EXISTS deliveries (
    id BIGSERIAL PRIMARY KEY,
    vehicle_id VARCHAR(100) NOT NULL,
    route TEXT,
    estimated_time TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'IN_TRANSIT', 'DELIVERED', 'FAILED', 'CANCELLED')),
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Stock table
CREATE TABLE IF NOT EXISTS stock (
    id BIGSERIAL PRIMARY KEY,
    warehouse_id VARCHAR(100) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    sku VARCHAR(100) UNIQUE,
    location VARCHAR(255),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_customer_name ON orders(customer_name);
CREATE INDEX IF NOT EXISTS idx_orders_delivery_date ON orders(delivery_date);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders(created_at);

CREATE INDEX IF NOT EXISTS idx_deliveries_status ON deliveries(status);
CREATE INDEX IF NOT EXISTS idx_deliveries_vehicle_id ON deliveries(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_deliveries_order_id ON deliveries(order_id);
CREATE INDEX IF NOT EXISTS idx_deliveries_estimated_time ON deliveries(estimated_time);

CREATE INDEX IF NOT EXISTS idx_stock_warehouse_id ON stock(warehouse_id);
CREATE INDEX IF NOT EXISTS idx_stock_product_name ON stock(product_name);
CREATE INDEX IF NOT EXISTS idx_stock_sku ON stock(sku);
CREATE INDEX IF NOT EXISTS idx_stock_quantity ON stock(quantity);
CREATE INDEX IF NOT EXISTS idx_stock_location ON stock(location);

-- Sample data (optional - for development/testing)
INSERT INTO orders (customer_name, destination_address, quantity, delivery_date, status) VALUES
('Acme Corp', '123 Main St, New York, NY 10001', 5, '2024-01-15 14:00:00', 'PENDING'),
('Tech Solutions Inc', '456 Oak Ave, San Francisco, CA 94102', 3, '2024-01-16 10:30:00', 'CONFIRMED'),
('Global Logistics', '789 Pine St, Chicago, IL 60601', 10, '2024-01-17 16:45:00', 'PENDING')
ON CONFLICT DO NOTHING;

INSERT INTO stock (warehouse_id, product_name, quantity, sku, location) VALUES
('WH-001', 'Laptop Computer', 50, 'LAPTOP-001', 'A1-B2-C3'),
('WH-001', 'Wireless Mouse', 200, 'MOUSE-001', 'A1-B2-C4'),
('WH-002', 'Office Chair', 25, 'CHAIR-001', 'B1-C2-D3'),
('WH-002', 'Desk Lamp', 75, 'LAMP-001', 'B1-C2-D4'),
('WH-003', 'Monitor Stand', 30, 'STAND-001', 'C1-D2-E3')
ON CONFLICT (sku) DO NOTHING;
