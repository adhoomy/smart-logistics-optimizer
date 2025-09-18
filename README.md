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
- GET /orders
- GET /deliveries  
- GET /stock

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

## Notes
- The backend uses in-memory stores for simplicity. Replace with a database later.
- The optimizer service is a skeleton; plug in your algorithms and LLM calls.


