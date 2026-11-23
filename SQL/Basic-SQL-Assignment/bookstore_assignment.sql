
-- ============================================================
-- SQL BASIC ASSIGNMENT
-- Online Bookstore Database
-- ============================================================

-- Create Database
CREATE DATABASE IF NOT EXISTS bookstore_db;
USE bookstore_db;

-- Create Authors Table
CREATE TABLE Authors (
    author_id INT PRIMARY KEY,
    author_name VARCHAR(100) NOT NULL,
    country VARCHAR(50)
);

-- Create Books Table
CREATE TABLE Books (
    book_id INT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    author_id INT,
    category VARCHAR(50),
    price DECIMAL(10,2),
    published_year INT,
    FOREIGN KEY (author_id) REFERENCES Authors(author_id)
);

-- ============================================================
-- INSERT SAMPLE AUTHORS
-- ============================================================

INSERT INTO Authors (author_id, author_name, country) VALUES
(1, 'Robert Martin', 'USA'),
(2, 'Ramesh Menon', 'India'),
(3, 'J.K. Rowling', 'UK'),
(4, 'Yuval Noah Harari', 'Israel'),
(5, 'Paulo Coelho', 'Brazil');

-- ============================================================
-- INSERT SAMPLE BOOKS
-- ============================================================

INSERT INTO Books
(book_id, title, author_id, category, price, published_year)
VALUES
(101, 'Clean Code', 1, 'Programming', 850.00, 2008),
(102, 'SQL Fundamentals', 2, 'Programming', 650.00, 2022),
(103, 'Software Engineering', 1, 'Programming', 750.00, 2021),
(104, 'Harry Potter', 3, 'Fiction', 550.00, 1997),
(105, 'The Alchemist', 5, 'Fiction', 450.00, 1988),
(106, 'Sapiens', 4, 'History', 900.00, 2011),
(107, 'Homo Deus', 4, 'History', 780.00, 2015),
(108, 'Python Basics', 2, 'Programming', 720.00, 2023),
(109, 'Science Today', 2, 'Science', 600.00, 2022),
(110, 'Start With Why', 5, 'Business', 800.00, 2021),
(111, 'Story of My Life', 3, 'Biography', 500.00, 2024),
(112, 'Java Programming', 1, 'Programming', 950.00, 2023);

-- ============================================================
-- SECTION 1: BASIC QUERIES
-- ============================================================

-- Task 1: Display all books
SELECT * FROM Books;

-- Task 2: Display book title and price
SELECT title, price FROM Books;

-- Task 3: Display title and price using aliases
SELECT
    title AS 'Book Name',
    price AS 'Book Price'
FROM Books;

-- Task 4: Display all unique book categories
SELECT DISTINCT category FROM Books;

-- ============================================================
-- SECTION 2: FILTERING DATA
-- ============================================================

-- Task 5: Display books priced above Rs. 700
SELECT * FROM Books
WHERE price > 700;

-- Task 6: Display books priced between Rs. 500 and Rs. 800
SELECT * FROM Books
WHERE price BETWEEN 500 AND 800;

-- Task 7: Display Programming category books
SELECT * FROM Books
WHERE category = 'Programming';

-- Task 8: Display books whose titles start with S
SELECT * FROM Books
WHERE title LIKE 'S%';

-- Task 9: Display books published after 2020
SELECT * FROM Books
WHERE published_year > 2020;

-- ============================================================
-- SECTION 3: SORTING AND LIMITING RESULTS
-- ============================================================

-- Task 10: Display books sorted by price descending
SELECT * FROM Books
ORDER BY price DESC;

-- Task 11: Display top 3 most expensive books
SELECT * FROM Books
ORDER BY price DESC
LIMIT 3;

-- ============================================================
-- SECTION 4: AGGREGATE FUNCTIONS
-- ============================================================

-- Task 12: Total books, total price, average,
-- highest price and lowest price

SELECT
    COUNT(*) AS total_books,
    SUM(price) AS total_price,
    ROUND(AVG(price), 2) AS average_price,
    MAX(price) AS highest_price,
    MIN(price) AS lowest_price
FROM Books;

-- ============================================================
-- SECTION 5: GROUP BY AND HAVING
-- ============================================================

-- Task 13: Average book price for each category
SELECT
    category,
    ROUND(AVG(price), 2) AS average_price
FROM Books
GROUP BY category;

-- Task 14: Categories with average price above Rs. 650
SELECT
    category,
    ROUND(AVG(price), 2) AS average_price
FROM Books
GROUP BY category
HAVING AVG(price) > 650;

-- ============================================================
-- SECTION 6: JOINS
-- ============================================================

-- Task 15: Display each book with author's name
-- INNER JOIN

SELECT
    Books.book_id,
    Books.title,
    Books.category,
    Books.price,
    Authors.author_name
FROM Books
INNER JOIN Authors
ON Books.author_id = Authors.author_id;

-- Task 16: Display all authors with their books
-- LEFT JOIN

SELECT
    Authors.author_id,
    Authors.author_name,
    Authors.country,
    Books.title,
    Books.category
FROM Authors
LEFT JOIN Books
ON Authors.author_id = Books.author_id;

-- Task 17: Display all books with author details
-- RIGHT JOIN

SELECT
    Books.book_id,
    Books.title,
    Books.price,
    Authors.author_id,
    Authors.author_name,
    Authors.country
FROM Authors
RIGHT JOIN Books
ON Authors.author_id = Books.author_id;

-- ============================================================
-- SECTION 7: DATA MANIPULATION
-- ============================================================

-- Task 18: Insert a new book

INSERT INTO Books
(book_id, title, author_id, category, price, published_year)
VALUES
(113, 'SQL Advanced Concepts', 2, 'Programming', 700.00, 2025);

-- Verify insertion
SELECT * FROM Books
WHERE book_id = 113;

-- Task 19: Update the inserted book's price

UPDATE Books
SET price = 850.00
WHERE book_id = 113;

-- Verify update
SELECT * FROM Books
WHERE book_id = 113;

-- Task 20: Delete the inserted book

DELETE FROM Books
WHERE book_id = 113;

-- Verify deletion
SELECT * FROM Books
WHERE book_id = 113;

-- ============================================================
-- SECTION 8: CREATING TABLES AND CONSTRAINTS
-- ============================================================

-- Task 21: Create Publishers table with constraints

CREATE TABLE Publishers (
    publisher_id INT PRIMARY KEY AUTO_INCREMENT,
    publisher_name VARCHAR(100) NOT NULL UNIQUE,
    country VARCHAR(50) NOT NULL,
    established_year INT DEFAULT 2000
);

-- Verify table creation
DESCRIBE Publishers;
