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

### Backend (Java Spring Boot)
Requirements: Java 17+, Maven 3.9+

```
cd backend
mvn spring-boot:run
```

The API will start at http://localhost:8080

Sample endpoints:
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


