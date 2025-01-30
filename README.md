# PlainJDBC_DAO Repository 🌱

A lightweight **Plain JDBC** project illustrating low-level database operations via **Java** and **SQL**.  
*Experience the fundamentals of relational data access by writing raw queries and mapping them yourself!*

---

## 📖 Overview

This repository demonstrates how to integrate **pure JDBC** for data persistence, bypassing higher-level frameworks like Spring or JPA. By using direct **DriverManager** connections and **PreparedStatements**, you gain full control over SQL execution, transaction scoping, and error handling—offering a clear look into the underpinnings of typical ORM-based solutions.

---

## ✨ Key Features

### 🧩 **Direct SQL Connection Handling**
- **`DriverManager.getConnection`** with dynamic credentials for flexible dev/test setups  
- Manual resource management (`try-with-resources`) to ensure proper statement and result set closure  
- Fine-grained logging of SQL exceptions and debugging info via **SLF4J**  

### ⚙️ **Manual Entity & Query Mappings**
- Straightforward queries: `SELECT`, `INSERT`, `UPDATE`, `DELETE`  
- Custom Java mappings: converting between **ResultSet** rows and domain objects (`Author`, `Book`)  
- Reduced magic—developers see exactly how data is fetched and transformed  

### 🛠 **Simple DAO Implementation**
- **CoreDao** interface for shared connection logic  
- **PlainDaoImpl** for explicit CRUD operations (e.g., `findAll`, `create`, `update`, `delete`)  
- Minimal abstraction—ideal for small-scale or legacy codebases  

### 🧪 **JUnit Testing**
- **JUnit 5** test cases verifying CRUD correctness  
- Database state reset between tests for reliable outcomes  
- Automatic **assertions** ensuring entity creation, updates, and queries function as expected  

---

## 🛠️ Built With
- **Java 21** – Modern Java features and performance  
- **MySQL Connector/J** – Native MySQL connectivity  
- **SLF4J & Logback** – Flexible logging for debugging and production readiness  
- **JUnit 5** – Clean, annotation-driven testing 
