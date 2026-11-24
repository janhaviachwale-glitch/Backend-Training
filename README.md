# SQL Advanced Assignment – Online Shopping Platform

## Overview

This project demonstrates advanced SQL concepts using a MySQL database for an online shopping platform. It covers advanced joins, subqueries, set operations, window functions, CTEs, views, indexes, stored procedures, functions, transactions, triggers and query performance optimization.

## Database Details

**Database Name:** `online_shopping_db`

### Tables

| Table | Description |
|---|---|
| Customers | Stores customer details such as name, city, and email. |
| Products | Stores product details, categories, prices, and creation dates. |
| Orders | Stores customer orders, products, quantities, and order dates. |
| Order_Audit | Stores audit records whenever a new order is inserted. |

## Concepts Covered

### 1. Advanced Joins
- INNER JOIN – Display customers with the products they ordered.
- CROSS JOIN – Display every possible customer and product combination.
- LEFT JOIN – Display all customers, including those without orders.

### 2. Subqueries
- Find products priced above the average product price.
- Find customers whose total spending is above average customer spending.
- Find the highest-priced products in each category using a correlated subquery.

### 3. Set Operations
- UNION – Combine Electronics and Furniture products.
- INTERSECT-style alternative – Find customers from Pune who have placed orders.
- EXCEPT-style alternative – Find customers who have not placed any orders.

### 4. Window Functions
- RANK() – Rank products by price within each category.
- LAG() – Retrieve the previous product's price.
- SUM() – Calculate a running total of product prices.

### 5. Common Table Expressions (CTEs)
- Use a CTE to identify customers spending above average.
- Use a recursive CTE to generate numbers from 1 to 10.

### 6. Views
- Create the `ExpensiveProducts` view to display products priced above 1,000.

### 7. Indexes and Query Performance
- Create an index on the product category.
- Use EXPLAIN to inspect query execution plans.

### 8. Stored Procedures and Functions
- Create `GetProductsByCategory` to retrieve products by category.
- Create `CalculateOrderTotal` to calculate the total value of an order.

### 9. Transactions
- Use COMMIT to save a product price update.
- Use ROLLBACK to undo another product price update.

### 10. Triggers
- Create an `AfterOrderInsert` trigger.
- Automatically insert an audit record into `Order_Audit` whenever a new order is added.

### 11. Performance Tuning
- Compare the original query with an optimized query.
- Replace YEAR() filtering with a date range.
- Avoid unnecessary DISTINCT and SELECT *.
- Create a composite index on category and created_date.
- Compare execution plans using EXPLAIN.

## How to Run the Project

1. Clone the repository.
2. Open MySQL Workbench and connect to your MySQL Server.
3. Open `SQL_Advanced_Assignment.sql`.
4. Execute the SQL script to create the database, tables, and sample data.
5. Execute individual queries to explore and verify the concepts.

## Technologies Used

- MySQL 8.0+
- MySQL Workbench
- SQL