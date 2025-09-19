# Smart Logistics Optimizer

Smart Logistics Optimizer is a scalable system that improves delivery routes and warehouse stocking by combining classical optimization algorithms with AI/LLMs. It showcases robust backend engineering, clean API design, and AI integration patterns suitable for production. This repo is designed for quick local runs, clear extensibility, and portfolio-ready presentation.

Smart Logistics Optimizer is a monorepo that provides:
- A Java Spring Boot backend for orders, deliveries, and warehouse stock APIs
- A Python optimizer service for route planning and stocking using AI/LLMs

## Goals
- Provide clean CRUD APIs for core logistics objects: orders, deliveries, stock
- Offer a Python service to host optimization algorithms and LLM integrations
- Make local development fast: simple Maven run for Java and venv for Python

## Repository Structure

```
.
├─ backend/      # Java Spring Boot API (Maven)
├─ optimizer/    # Python optimizer service
└─ docs/         # Documentation
```

## Running Locally

### Prerequisites
- Java 17+
- Maven 3.9+
- Python 3.10+
- PostgreSQL 12+ (or Docker)

### Database Setup

#### Option 1: Docker (Recommended)
```bash
# Start PostgreSQL container
docker run --name smart-logistics-db \
  -e POSTGRES_DB=smart_logistics \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d postgres:15

# Verify connection
docker exec -it smart-logistics-db psql -U postgres -d smart_logistics
```

#### Option 2: Local PostgreSQL Installation
1. Install PostgreSQL 12+ from [postgresql.org](https://www.postgresql.org/download/)
2. Create database:
```sql
CREATE DATABASE smart_logistics;
CREATE USER postgres WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE smart_logistics TO postgres;
```

### Backend (Java Spring Boot)
```bash
cd backend

# Set database environment variables (optional - defaults provided)
export DB_URL=jdbc:postgresql://localhost:5432/smart_logistics
export DB_USERNAME=postgres
export DB_PASSWORD=password

# Run the application
mvn spring-boot:run
```

The API will start at http://localhost:8080

**Database Configuration:**
- Default connection: `localhost:5432/smart_logistics`
- Username: `postgres`, Password: `password`
- Override with environment variables: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`

**Sample endpoints:**
- GET /orders (with pagination, sorting, filtering)
- GET /deliveries (with pagination, sorting, filtering)
- GET /stock (with pagination, sorting, filtering)

### Postman Usage Examples

Import these requests into Postman or use curl.

Orders:
```bash
# Create
curl -s -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName":"Acme Corp",
    "destinationAddress":"123 Market St, SF",
    "quantity":10,
    "deliveryDate":"2026-01-01T10:00:00",
    "status":"PENDING"
  }'

# Get by id
curl -s http://localhost:8080/orders/1

# Get all (with pagination)
curl -s http://localhost:8080/orders

# Get paginated results
curl -s "http://localhost:8080/orders?page=0&size=5&sort=status,asc"

# Filter by status and customer name
curl -s "http://localhost:8080/orders?status=PENDING&customerName=Acme"

# Update
curl -s -X PUT http://localhost:8080/orders/1 \
  -H "Content-Type: application/json" \
  -d '{
    "customerName":"Globex",
    "destinationAddress":"456 Elm St, NY",
    "quantity":8,
    "deliveryDate":"2026-01-02T12:00:00",
    "status":"CONFIRMED"
  }'

# Delete
curl -s -X DELETE http://localhost:8080/orders/1 -i
```

Deliveries:
```bash
# Create (requires an orderId)
curl -s -X POST http://localhost:8080/deliveries \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleId":"TRUCK-42",
    "route":"A-B-C",
    "orderId":1
  }'

# Get by id
curl -s http://localhost:8080/deliveries/1

# Get all (with pagination)
curl -s http://localhost:8080/deliveries

# Get paginated results
curl -s "http://localhost:8080/deliveries?page=0&size=5&sort=status,asc"

# Filter by status and vehicle ID
curl -s "http://localhost:8080/deliveries?status=IN_TRANSIT&vehicleId=TRUCK-42"

# Update
curl -s -X PUT http://localhost:8080/deliveries/1 \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleId":"TRUCK-9",
    "route":"A-C",
    "orderId":1
  }'

# Delete
curl -s -X DELETE http://localhost:8080/deliveries/1 -i
```

Stock:
```bash
# Create
curl -s -X POST http://localhost:8080/stock \
  -H "Content-Type: application/json" \
  -d '{
    "warehouseId":"W1",
    "productName":"Widget",
    "quantity":100,
    "sku":"SKU-001",
    "location":"A1"
  }'

# Get by id
curl -s http://localhost:8080/stock/1

# Get all (with pagination)
curl -s http://localhost:8080/stock

# Get paginated results
curl -s "http://localhost:8080/stock?page=0&size=5&sort=quantity,asc"

# Filter by warehouse ID and product name
curl -s "http://localhost:8080/stock?warehouseId=W1&productName=Widget"

# Update
curl -s -X PUT http://localhost:8080/stock/1 \
  -H "Content-Type: application/json" \
  -d '{
    "warehouseId":"W2",
    "productName":"Widget",
    "quantity":80,
    "sku":"SKU-001",
    "location":"B2"
  }'

# Delete
curl -s -X DELETE http://localhost:8080/stock/1 -i
```

### Optimizer (Python)
Requirements: Python 3.10+

```
cd optimizer
python -m venv .venv
.\.venv\Scripts\activate  # Windows PowerShell
pip install -r requirements.txt
python main.py
```

Expected output:
```
Logistics Optimizer Ready
```

## API Features

### Pagination, Sorting & Filtering

All GET endpoints for `/orders`, `/deliveries`, and `/stock` support:

**Pagination:**
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)

**Sorting:**
- `sort`: Field and direction (e.g., "status,asc", "deliveryDate,desc")

**Filtering:**

**Orders (`/orders`):**
- `status`: Filter by order status (PENDING, CONFIRMED, IN_PROGRESS, DELIVERED, CANCELLED)
- `customerName`: Filter by customer name (partial match, case-insensitive)

**Deliveries (`/deliveries`):**
- `status`: Filter by delivery status (PENDING, IN_TRANSIT, DELIVERED, FAILED, CANCELLED)
- `vehicleId`: Filter by vehicle ID (exact match)

**Stock (`/stock`):**
- `warehouseId`: Filter by warehouse ID (exact match)
- `productName`: Filter by product name (partial match, case-insensitive)

**Example API Calls:**

```bash
# Paginated orders with sorting
curl "http://localhost:8080/orders?page=0&size=5&sort=status,asc"

# Filter orders by status and customer
curl "http://localhost:8080/orders?status=PENDING&customerName=Acme"

# Paginated deliveries with filtering
curl "http://localhost:8080/deliveries?page=1&size=10&status=IN_TRANSIT&vehicleId=TRUCK-42"

# Stock items sorted by quantity
curl "http://localhost:8080/stock?sort=quantity,desc&warehouseId=W1"
```

**Response Format:**
All paginated endpoints return responses in this format:
```json
{
  "content": [...],
  "page": 0,
  "size": 10,
  "totalElements": 25,
  "totalPages": 3,
  "first": true,
  "last": false,
  "hasNext": true,
  "hasPrevious": false
}
```

## Notes
- Backend now uses PostgreSQL (dev) and H2 (tests) via JPA.
- All GET endpoints support pagination, sorting, and filtering.
- Optimizer service is a skeleton; plug in your algorithms and LLM calls.


