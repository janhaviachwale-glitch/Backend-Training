# SQL Basic Assignment – Online Bookstore

## 1. Assignment Overview

This project demonstrates fundamental SQL concepts using an Online Bookstore database.

The assignment covers:
- Database and table creation
- Inserting sample records
- SELECT queries
- Filtering and sorting
- Aggregate functions
- GROUP BY and HAVING
- INNER JOIN, LEFT JOIN and RIGHT JOIN
- INSERT, UPDATE and DELETE operations
- Primary Key, Foreign Key and other constraints

## 2. Technologies Used

- MySQL 8.0
- SQL
- MySQL Workbench
- Git and GitHub

## 3. Database Schema

Database Name: `bookstore_db`

### Authors Table

| Column | Data Type | Constraint |
|---|---|---|
| author_id | INT | PRIMARY KEY |
| author_name | VARCHAR(100) | NOT NULL |
| country | VARCHAR(50) | - |

### Books Table

| Column | Data Type | Constraint |
|---|---|---|
| book_id | INT | PRIMARY KEY |
| title | VARCHAR(100) | NOT NULL |
| author_id | INT | FOREIGN KEY |
| category | VARCHAR(50) | - |
| price | DECIMAL(10,2) | - |
| published_year | INT | - |

### Publishers Table

| Column | Data Type | Constraint |
|---|---|---|
| publisher_id | INT | PRIMARY KEY, AUTO_INCREMENT |
| publisher_name | VARCHAR(100) | NOT NULL, UNIQUE |
| country | VARCHAR(50) | NOT NULL |
| established_year | INT | DEFAULT 2000 |

## 4. Sample Data

- 5 authors from different countries
- 12 books
- Multiple book categories including Programming, Fiction, History, Science, Business and Biography

## 5. SQL Tasks Completed

### Section 1: Basic Queries
Tasks 1–4: SELECT, column selection, aliases and DISTINCT.

### Section 2: Filtering Data
Tasks 5–9: WHERE, BETWEEN, LIKE and comparison operators.

### Section 3: Sorting and Limiting
Tasks 10–11: ORDER BY and LIMIT.

### Section 4: Aggregate Functions
Task 12: COUNT, SUM, AVG, MAX and MIN.

### Section 5: GROUP BY and HAVING
Tasks 13–14: Category-wise average prices and HAVING conditions.

### Section 6: Joins
Tasks 15–17: INNER JOIN, LEFT JOIN and RIGHT JOIN.

### Section 7: Data Manipulation
Tasks 18–20: INSERT, UPDATE and DELETE.

### Section 8: Creating Tables and Constraints
Task 21: Publishers table with PRIMARY KEY, NOT NULL, UNIQUE and DEFAULT constraints.

## 6. Steps to Execute the SQL Script

1. Install MySQL Server and MySQL Workbench.
2. Open MySQL Workbench.
3. Connect to the local MySQL server.
4. Open `bookstore_assignment.sql`.
5. Execute the entire script.
6. Verify the output of the SQL queries.
7. Run `DESCRIBE Publishers;` to verify Task 21.

## 7. Expected Results

The aggregate query should return:

- Total Books: 12
- Total Price: 8500.00
- Average Price: 708.33
- Highest Price: 950.00
- Lowest Price: 450.00