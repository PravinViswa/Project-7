
# Product Assessment

### 1. Run Backend

```bash
cd backend
./mvnw clean install   # For Linux/Mac

mvnw.cmd clean install # For Windows

# To Start backend
java -jar target/product-engineering-1.0.0.jar
```

Backend runs on:[http://localhost:8080]

---

### 2. Run Frontend

```bash
cd frontend
npm install
npm start
```

Frontend runs on:[http://localhost:3000]

---

## To Run Using Docker

### 1. Build & Start Containers

```bash
docker-compose up --build
```

This will:

* Start Spring Boot backend on [http://localhost:8080]
* Start React frontend on [http://localhost:3000]

---

### 2. Stop Containers

```bash
docker-compose down
```

---
