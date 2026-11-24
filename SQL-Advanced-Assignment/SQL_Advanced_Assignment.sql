-- ============================================================
-- SQL ADVANCED ASSIGNMENT - ONLINE SHOPPING PLATFORM
-- Database: online_shopping_db
-- MySQL version: 8.0+
-- ============================================================

CREATE DATABASE IF NOT EXISTS online_shopping_db;
USE online_shopping_db;

-- ============================================================
-- DATABASE SETUP: TABLES
-- ============================================================

CREATE TABLE Customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(100) NOT NULL,
    city VARCHAR(50),
    email VARCHAR(100) UNIQUE
);

CREATE TABLE Products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    price DECIMAL(10, 2) NOT NULL,
    created_date DATE
);

CREATE TABLE Orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    product_id INT,
    quantity INT NOT NULL,
    order_date DATE,
    CONSTRAINT fk_orders_customers
        FOREIGN KEY (customer_id) REFERENCES Customers(customer_id),
    CONSTRAINT fk_orders_products
        FOREIGN KEY (product_id) REFERENCES Products(product_id)
) ENGINE = InnoDB;

-- ============================================================
-- SAMPLE DATA: at least 10 customers, 10 products, 20 orders
-- ============================================================

INSERT INTO Customers (customer_name, city, email) VALUES
('Aarav Sharma', 'Pune', 'aarav@gmail.com'),
('Priya Patil', 'Mumbai', 'priya@gmail.com'),
('Rahul Verma', 'Delhi', 'rahul@gmail.com'),
('Sneha Joshi', 'Pune', 'sneha@gmail.com'),
('Rohan Mehta', 'Bangalore', 'rohan@gmail.com'),
('Ananya Desai', 'Mumbai', 'ananya@gmail.com'),
('Karan Shah', 'Hyderabad', 'karan@gmail.com'),
('Neha Kulkarni', 'Pune', 'neha@gmail.com'),
('Aditya Rao', 'Chennai', 'aditya@gmail.com'),
('Isha Gupta', 'Delhi', 'isha@gmail.com');

INSERT INTO Products (product_name, category, price, created_date) VALUES
('Laptop', 'Electronics', 55000.00, '2024-01-10'),
('Smartphone', 'Electronics', 25000.00, '2024-02-15'),
('Headphones', 'Electronics', 2000.00, '2024-03-05'),
('Keyboard', 'Electronics', 1500.00, '2024-04-12'),
('Office Chair', 'Furniture', 8000.00, '2024-05-20'),
('Study Table', 'Furniture', 12000.00, '2024-06-18'),
('SQL Basics', 'Books', 500.00, '2024-07-10'),
('Java Programming', 'Books', 900.00, '2024-08-15'),
('Running Shoes', 'Fashion', 3000.00, '2024-09-05'),
('Backpack', 'Fashion', 1800.00, '2024-10-12');

INSERT INTO Orders (customer_id, product_id, quantity, order_date) VALUES
(1, 1, 1, '2024-11-01'),
(1, 3, 2, '2024-11-02'),
(2, 2, 1, '2024-11-03'),
(2, 7, 3, '2024-11-04'),
(3, 5, 1, '2024-11-05'),
(3, 8, 2, '2024-11-06'),
(4, 4, 1, '2024-11-07'),
(4, 9, 2, '2024-11-08'),
(5, 6, 1, '2024-11-09'),
(5, 10, 1, '2024-11-10'),
(6, 1, 1, '2024-11-11'),
(6, 2, 2, '2024-11-12'),
(7, 3, 1, '2024-11-13'),
(7, 5, 2, '2024-11-14'),
(8, 7, 2, '2024-11-15'),
(8, 8, 1, '2024-11-16'),
(9, 9, 1, '2024-11-17'),
(9, 4, 2, '2024-11-18'),
(2, 6, 1, '2024-11-19'),
(1, 10, 2, '2024-11-20');

-- ============================================================
-- SECTION 1: ADVANCED JOINS
-- ============================================================

-- TASK 1: Display customers with the products they ordered.
SELECT
    c.customer_name,
    p.product_name,
    o.quantity,
    o.order_date
FROM Customers AS c
INNER JOIN Orders AS o
    ON c.customer_id = o.customer_id
INNER JOIN Products AS p
    ON o.product_id = p.product_id;

-- TASK 2: Display every possible customer/product combination.
-- With 11 customers and 10 products, this returns 110 rows.
SELECT
    c.customer_name,
    p.product_name
FROM Customers AS c
CROSS JOIN Products AS p;

-- TASK 3: Display all customers, including those without orders.
SELECT
    c.customer_id,
    c.customer_name,
    o.order_id,
    o.order_date
FROM Customers AS c
LEFT JOIN Orders AS o
    ON c.customer_id = o.customer_id;

-- ============================================================
-- SECTION 2: SUBQUERIES
-- ============================================================

-- TASK 4: Find products priced above the average product price.
SELECT
    product_id,
    product_name,
    price
FROM Products
WHERE price > (
    SELECT AVG(price)
    FROM Products
);

-- TASK 5: Find customers whose total spending is above average
-- customer spending. Spending = product price * quantity.
-- Customers with no orders are counted as spending zero.
SELECT
    c.customer_id,
    c.customer_name,
    COALESCE(SUM(p.price * o.quantity), 0) AS total_purchase
FROM Customers AS c
LEFT JOIN Orders AS o
    ON c.customer_id = o.customer_id
LEFT JOIN Products AS p
    ON o.product_id = p.product_id
GROUP BY c.customer_id, c.customer_name
HAVING total_purchase > (
    SELECT AVG(customer_total)
    FROM (
        SELECT
            c2.customer_id,
            COALESCE(SUM(p2.price * o2.quantity), 0) AS customer_total
        FROM Customers AS c2
        LEFT JOIN Orders AS o2
            ON c2.customer_id = o2.customer_id
        LEFT JOIN Products AS p2
            ON o2.product_id = p2.product_id
        GROUP BY c2.customer_id
    ) AS customer_spending
);

-- TASK 6: Find the highest-priced product(s) in each category.
-- This is a correlated subquery because p.category is referenced
-- from inside the subquery.
SELECT
    p.product_id,
    p.product_name,
    p.category,
    p.price
FROM Products AS p
WHERE p.price = (
    SELECT MAX(p2.price)
    FROM Products AS p2
    WHERE p2.category = p.category
);

-- ============================================================
-- SECTION 3: SET OPERATIONS
-- ============================================================

-- TASK 7: Combine Electronics and Furniture products using UNION.
SELECT product_id, product_name, category
FROM Products
WHERE category = 'Electronics'
UNION
SELECT product_id, product_name, category
FROM Products
WHERE category = 'Furniture';

-- TASK 8A: INTERSECT-style alternative using INNER JOIN.
-- Customers who live in Pune AND have at least one order.
SELECT DISTINCT
    c.customer_id,
    c.customer_name,
    c.city
FROM Customers AS c
INNER JOIN Orders AS o
    ON c.customer_id = o.customer_id
WHERE c.city = 'Pune';

-- TASK 8B: EXCEPT-style alternative using LEFT JOIN.
-- Customers who do not have any matching order.
SELECT
    c.customer_id,
    c.customer_name,
    c.city
FROM Customers AS c
LEFT JOIN Orders AS o
    ON c.customer_id = o.customer_id
WHERE o.order_id IS NULL;

-- ============================================================
-- SECTION 4: WINDOW FUNCTIONS
-- ============================================================

-- TASK 9: Rank products by price within each category.
SELECT
    product_id,
    product_name,
    category,
    price,
    RANK() OVER (
        PARTITION BY category
        ORDER BY price DESC
    ) AS product_rank
FROM Products;

-- TASK 10: Show previous product price and running total.
SELECT
    product_id,
    product_name,
    price,
    LAG(price) OVER (
        ORDER BY product_id
    ) AS previous_product_price,
    SUM(price) OVER (
        ORDER BY product_id
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS running_total
FROM Products;

-- ============================================================
-- SECTION 5: COMMON TABLE EXPRESSIONS (CTEs)
-- ============================================================

-- TASK 11: Use a CTE to find customers spending above average.
WITH CustomerSpending AS (
    SELECT
        c.customer_id,
        c.customer_name,
        COALESCE(SUM(p.price * o.quantity), 0) AS total_purchase
    FROM Customers AS c
    LEFT JOIN Orders AS o
        ON c.customer_id = o.customer_id
    LEFT JOIN Products AS p
        ON o.product_id = p.product_id
    GROUP BY c.customer_id, c.customer_name
)
SELECT
    customer_id,
    customer_name,
    total_purchase
FROM CustomerSpending
WHERE total_purchase > (
    SELECT AVG(total_purchase)
    FROM CustomerSpending
);

-- TASK 12: Recursive CTE to generate numbers from 1 to 10.
WITH RECURSIVE NumberSequence AS (
    SELECT 1 AS number
    UNION ALL
    SELECT number + 1
    FROM NumberSequence
    WHERE number < 10
)
SELECT number
FROM NumberSequence;

-- ============================================================
-- SECTION 6: VIEWS
-- ============================================================

-- TASK 13: Create a view for products priced above 1,000.
CREATE OR REPLACE VIEW ExpensiveProducts AS
SELECT
    product_id,
    product_name,
    category,
    price
FROM Products
WHERE price > 1000.00;

-- Retrieve rows from the view.
SELECT * FROM ExpensiveProducts;

-- ============================================================
-- SECTION 7: INDEXES AND QUERY PERFORMANCE
-- ============================================================

-- TASK 14: Create an index on Products.category.
CREATE INDEX idx_products_category
ON Products(category);

-- TASK 15: Use EXPLAIN to inspect the category filter query.
EXPLAIN
SELECT
    product_id,
    product_name,
    price
FROM Products
WHERE category = 'Electronics';

-- ============================================================
-- SECTION 8: STORED PROCEDURES AND FUNCTIONS
-- ============================================================

-- TASK 16: Stored procedure to retrieve products by category.
DELIMITER //

CREATE PROCEDURE GetProductsByCategory(
    IN input_category VARCHAR(50)
)
BEGIN
    SELECT
        product_id,
        product_name,
        category,
        price
    FROM Products
    WHERE category = input_category;
END //

DELIMITER ;

-- Execute the stored procedure.
CALL GetProductsByCategory('Electronics');

-- TASK 17: Function to calculate the total value of an order.
-- The function returns price * quantity for the supplied order ID.
DELIMITER //

CREATE FUNCTION CalculateOrderTotal(input_order_id INT)
RETURNS DECIMAL(12, 2)
NOT DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE total_amount DECIMAL(12, 2) DEFAULT 0.00;

    SELECT COALESCE(p.price * o.quantity, 0.00)
    INTO total_amount
    FROM Orders AS o
    INNER JOIN Products AS p
        ON o.product_id = p.product_id
    WHERE o.order_id = input_order_id;

    RETURN total_amount;
END //

DELIMITER ;

-- Execute the function.
SELECT CalculateOrderTotal(1) AS order_total;

-- NOTE: If MySQL rejects CREATE FUNCTION because binary logging
-- is enabled and function creators are restricted, ask your
-- database administrator about log_bin_trust_function_creators.
-- Do not change server-wide settings unless you have permission.

-- ============================================================
-- SECTION 9: TRANSACTIONS
-- ============================================================

-- TASK 18A: Update a product price and COMMIT the change.
START TRANSACTION;

UPDATE Products
SET price = 60000.00
WHERE product_id = 1;

SELECT product_id, product_name, price
FROM Products
WHERE product_id = 1;

COMMIT;

-- Confirm the committed value.
SELECT product_id, product_name, price
FROM Products
WHERE product_id = 1;

-- TASK 18B: Make another update and ROLLBACK the change.
START TRANSACTION;

UPDATE Products
SET price = 65000.00
WHERE product_id = 1;

SELECT product_id, product_name, price
FROM Products
WHERE product_id = 1;

ROLLBACK;

-- The price should return to 60000.00 after rollback.
SELECT product_id, product_name, price
FROM Products
WHERE product_id = 1;

-- ============================================================
-- SECTION 10: SQL TRIGGERS
-- ============================================================

-- TASK 19: Create an audit table for inserted orders.
CREATE TABLE Order_Audit (
    audit_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    customer_id INT,
    product_id INT,
    quantity INT,
    order_date DATE,
    action_type VARCHAR(20),
    audit_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- Create a trigger that records each newly inserted order.
DELIMITER //

CREATE TRIGGER AfterOrderInsert
AFTER INSERT ON Orders
FOR EACH ROW
BEGIN
    INSERT INTO Order_Audit (
        order_id,
        customer_id,
        product_id,
        quantity,
        order_date,
        action_type
    )
    VALUES (
        NEW.order_id,
        NEW.customer_id,
        NEW.product_id,
        NEW.quantity,
        NEW.order_date,
        'INSERT'
    );
END //

DELIMITER ;

-- Test the trigger by inserting one new order.
-- The trigger should automatically add a matching audit record.
INSERT INTO Orders (customer_id, product_id, quantity, order_date)
VALUES (1, 2, 1, '2024-11-21');

-- Verify the audit entry created by the trigger.
SELECT * FROM Order_Audit;

-- ============================================================
-- SECTION 11: PERFORMANCE TUNING
-- ============================================================

-- TASK 20: Original, less efficient query from the assignment.
-- YEAR(created_date) applies a function to the date column.
-- DISTINCT is unnecessary because product_id is a primary key.
-- SELECT * also returns columns that may not be needed.
SELECT DISTINCT *
FROM Products
WHERE YEAR(created_date) = 2024
  AND category = 'Electronics';

-- Optimized query: select needed columns and use a date range.
SELECT
    product_id,
    product_name,
    price,
    category,
    created_date
FROM Products
WHERE category = 'Electronics'
  AND created_date >= '2024-01-01'
  AND created_date < '2025-01-01';

-- Composite index to support the category + date filters.
CREATE INDEX idx_products_category_created
ON Products(category, created_date);

-- Compare the execution plan for the optimized query.
EXPLAIN
SELECT
    product_id,
    product_name,
    price,
    category,
    created_date
FROM Products
WHERE category = 'Electronics'
  AND created_date >= '2024-01-01'
  AND created_date < '2025-01-01';
